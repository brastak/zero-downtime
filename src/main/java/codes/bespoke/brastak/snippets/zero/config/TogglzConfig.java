package codes.bespoke.brastak.snippets.zero.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.cache.CachingStateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.spi.FeatureProvider;

@Configuration
public class TogglzConfig {
    @Bean
    public StateRepository stateRepository(DataSource dataSource) {
        return new CachingStateRepository(
            new JDBCStateRepository(dataSource),
            5_000
        );
    }

    @Bean
    public FeatureProvider featureProvider() {
        return new EnumBasedFeatureProvider(ZeroDTFeatures.class);
    }
}
