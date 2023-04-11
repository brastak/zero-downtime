package codes.bespoke.brastak.snippets.zero.service;

import codes.bespoke.brastak.snippets.zero.mapper.PostMapper;
import codes.bespoke.brastak.snippets.zero.repository.PostRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class OperationsFactory {
    @Bean
    @RequestScope
    public ReadOperations readOperations(ApplicationContext context) {
        return new ReadOperationsImpl(context.getBean(PostRepository.class), context.getBean(PostMapper.class));
    }

    @Bean
    @RequestScope
    public WriteOperations writeOperations(ApplicationContext context) {
        return new WriteOperationsImpl(context.getBean(PostRepository.class), context.getBean(PostMapper.class));
    }
}
