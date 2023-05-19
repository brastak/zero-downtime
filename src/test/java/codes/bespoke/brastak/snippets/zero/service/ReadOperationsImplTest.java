package codes.bespoke.brastak.snippets.zero.service;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.mapper.PostMapperImpl;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureDataJdbc
@EnableJdbcRepositories(basePackages = "codes.bespoke.brastak.snippets.zero.repository")
@ContextConfiguration(classes = { ReadOperationsImplTest.DbUnitConfiguration.class, PostMapperImpl.class, ReadOperationsImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = "classpath:dbunit/posts.xml", type = DatabaseOperation.INSERT)
@DatabaseTearDown(value = "classpath:dbunit/clean.xml", type = DatabaseOperation.TRUNCATE_TABLE)
public class ReadOperationsImplTest {
    @Autowired
    private ReadOperationsImpl readOperations;

    @TestConfiguration
    static class DbUnitConfiguration {
        @Bean
        public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource) {
            DatabaseConfigBean bean = new DatabaseConfigBean();
            bean.setDatatypeFactory(new PostgresqlDataTypeFactory());

            DatabaseDataSourceConnectionFactoryBean dbConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(dataSource);
            dbConnectionFactory.setDatabaseConfig(bean);
            return dbConnectionFactory;
        }
    }

    @Test
    public void testFindById_success() {
        Optional<PostDto> actual = readOperations.findById(1);
        Optional<PostDto> expected = Optional.of(new PostDto(1, "tester", "test subject",
            "test post body", true));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindById_notFound() {
        Optional<PostDto> actual = readOperations.findById(-1);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindByAuthor_all() {
        List<PostDto> actual = readOperations.findByAuthor("tester", false);
        List<PostDto> expected = List.of(
            new PostDto(2, "tester", "one more subject","one more test post body", false),
            new PostDto(1, "tester", "test subject","test post body", true)
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByAuthor_publishedOnly() {
        List<PostDto> actual = readOperations.findByAuthor("tester", true);
        List<PostDto> expected = List.of(
            new PostDto(1, "tester", "test subject","test post body", true)
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFindByAuthor_noData() {
        List<PostDto> actual = readOperations.findByAuthor("fake author", false);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    public void testFindByAuthor_paging() {
        List<PostDto> actual = readOperations.findByAuthor("spammer", true);
        Assertions.assertEquals(20, actual.size());
        Assertions.assertTrue(actual.stream().map(PostDto::id).allMatch(id -> id >= 4 && id <= 23));
    }
}
