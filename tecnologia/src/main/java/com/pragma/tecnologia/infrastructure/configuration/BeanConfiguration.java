package com.pragma.tecnologia.infrastructure.configuration;


import com.pragma.tecnologia.application.mapper.TechnologyMapper;
import com.pragma.tecnologia.application.usecase.CreateTechnologyUseCase;
import com.pragma.tecnologia.application.usecase.ListTechnologyUseCase;
import com.pragma.tecnologia.domain.repository.ITechnologyRepository;
import com.pragma.tecnologia.infrastructure.db.TechnologyRepositoryAdapter;
import com.pragma.tecnologia.infrastructure.db.repository.TechnologyCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ITechnologyRepository technologyRepository(TechnologyCrudRepository crudRepository) {
        return new TechnologyRepositoryAdapter(crudRepository);
    }

    @Bean
    public CreateTechnologyUseCase createTechnologyUseCase(ITechnologyRepository repository) {
        return new CreateTechnologyUseCase(repository);
    }

    @Bean
    public ListTechnologyUseCase listTechnologyUseCase(ITechnologyRepository repository) {
        return new ListTechnologyUseCase(repository);
    }

    @Bean
    public TechnologyMapper technologyMapper() {
        return new TechnologyMapper();
    }
}
