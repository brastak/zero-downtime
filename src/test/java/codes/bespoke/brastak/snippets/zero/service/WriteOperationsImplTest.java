package codes.bespoke.brastak.snippets.zero.service;

import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import codes.bespoke.brastak.snippets.zero.dto.CreatePostRequestDto;
import codes.bespoke.brastak.snippets.zero.dto.PostDto;
import codes.bespoke.brastak.snippets.zero.mapper.PostMapperImpl;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import org.dbunit.Assertion;
import org.dbunit.assertion.comparer.value.ValueComparers;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
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
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureDataJdbc
@Commit
@Transactional
@EnableJdbcRepositories(basePackages = "codes.bespoke.brastak.snippets.zero.repository")
@ContextConfiguration(classes = { WriteOperationsImplTest.DbUnitConfiguration.class, PostMapperImpl.class, WriteOperationsImpl.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
@DatabaseTearDown(value = "classpath:dbunit/clean.xml", type = DatabaseOperation.TRUNCATE_TABLE)
public class WriteOperationsImplTest {
    @Autowired
    private WriteOperationsImpl writeOperations;

    @Autowired
    private IDatabaseConnection connection;

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
    public void testCreatePost() throws Exception {
        CreatePostRequestDto createPostRequest = new CreatePostRequestDto("tester", "test subject", "test post body");
        PostDto post = writeOperations.create(createPostRequest);
        Assertions.assertTrue(post.id() > 0);
        Assertions.assertTrue(post.published());

        IDataSet actual = connection.createDataSet();
        IDataSet expected = new FlatXmlDataSetLoader().loadDataSet(getClass(), "classpath:dbunit/single-post.xml");

        Assertion.assertWithValueComparer(
            expected.getTable("post"),
            actual.getTable("post"),
            ValueComparers.isActualEqualToExpected,
            Map.of("id", ValueComparers.isActualGreaterThanOrEqualToExpected)
        );
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/single-post.xml", type = DatabaseOperation.INSERT)
    @ExpectedDatabase(value = "classpath:dbunit/hidden-post.xml", table = "post")
    public void testHidePost() {
        writeOperations.hide(1);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/single-post.xml", type = DatabaseOperation.INSERT)
    public void testLockPost_success() {
        Optional<Long> lock = writeOperations.lock(1);
        Assertions.assertTrue(lock.isPresent());
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/single-post.xml", type = DatabaseOperation.INSERT)
    public void testLockPost_failure() {
        Optional<Long> lock = writeOperations.lock(2);
        Assertions.assertFalse(lock.isPresent());
    }
}
