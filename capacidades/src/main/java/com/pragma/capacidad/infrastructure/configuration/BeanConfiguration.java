package com.pragma.capacidad.infrastructure.configuration;

import com.pragma.capacidad.application.mapper.CapacityMapper;
import com.pragma.capacidad.application.usecase.CreateCapacityUseCase;
import com.pragma.capacidad.application.usecase.ListCapacityUseCase;
import com.pragma.capacidad.domain.repository.ICapacityRepository;
import com.pragma.capacidad.infrastructure.db.CapacityRepositoryAdapter;
import com.pragma.capacidad.infrastructure.db.repository.CapacityCrudRepository;
import com.pragma.capacidad.infrastructure.db.repository.CapacityTechnologyCrudRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ICapacityRepository capacityRepository(
            CapacityCrudRepository capacityCrud,
            CapacityTechnologyCrudRepository capTechCrud
    ) {
        return new CapacityRepositoryAdapter(capacityCrud, capTechCrud);
    }

    @Bean
    public CreateCapacityUseCase createCapacityUseCase(ICapacityRepository repository) {
        return new CreateCapacityUseCase(repository);
    }

    @Bean
    public ListCapacityUseCase listCapacityUseCase(ICapacityRepository repository) {
        return new ListCapacityUseCase(repository);
    }

    @Bean
    public CapacityMapper capacityMapper() {
        return new CapacityMapper();
    }
}