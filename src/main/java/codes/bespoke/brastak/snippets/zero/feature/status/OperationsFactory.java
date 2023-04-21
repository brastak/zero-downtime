package codes.bespoke.brastak.snippets.zero.feature.status;

import codes.bespoke.brastak.snippets.zero.config.ZeroDTFeatures;
import codes.bespoke.brastak.snippets.zero.feature.status.old.mapper.OldPostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.target.mapper.PostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.target.repository.PostRepository;
import codes.bespoke.brastak.snippets.zero.feature.status.target.service.ReadOperationsImpl;
import codes.bespoke.brastak.snippets.zero.feature.status.target.service.WriteOperationsImpl;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.mapper.TempPostMapper;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.repository.TempPostRepository;
import codes.bespoke.brastak.snippets.zero.feature.status.temp.service.TempWriteOperationsImpl;
import codes.bespoke.brastak.snippets.zero.feature.status.old.repository.OldPostRepository;
import codes.bespoke.brastak.snippets.zero.service.ReadOperations;
import codes.bespoke.brastak.snippets.zero.feature.status.old.service.OldReadOperationsImpl;
import codes.bespoke.brastak.snippets.zero.service.WriteOperations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.togglz.core.manager.FeatureManager;

@Configuration
public class OperationsFactory {
    @Bean
    @RequestScope
    public ReadOperations readOperations(ApplicationContext context, FeatureManager manager) {
        if (manager.isActive(ZeroDTFeatures.FEATURE_READ_POST_STATUS)) {
            return new ReadOperationsImpl(
                context.getBean(PostRepository.class),
                context.getBean(PostMapper.class)
            );
        } else {
            return new OldReadOperationsImpl(
                context.getBean(OldPostRepository.class),
                context.getBean(OldPostMapper.class)
            );
        }
    }

    @Bean
    @RequestScope
    public WriteOperations writeOperations(ApplicationContext context, FeatureManager manager) {
        if (manager.isActive(ZeroDTFeatures.FEATURE_WRITE_POST_STATUS_ONLY)) {
            return new WriteOperationsImpl(
                context.getBean(PostRepository.class),
                context.getBean(PostMapper.class)
            );
        } else {
            return new TempWriteOperationsImpl(
                context.getBean(TempPostRepository.class),
                context.getBean(TempPostMapper.class)
            );
        }
    }
}
