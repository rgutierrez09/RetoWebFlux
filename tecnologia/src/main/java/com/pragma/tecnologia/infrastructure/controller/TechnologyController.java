package com.pragma.tecnologia.infrastructure.controller;

import com.pragma.tecnologia.application.dto.PagedResponseDto;
import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.application.mapper.TechnologyMapper;
import com.pragma.tecnologia.application.usecase.CreateTechnologyUseCase;
import com.pragma.tecnologia.application.usecase.ListTechnologyUseCase;
import com.pragma.tecnologia.domain.model.Technology;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/webflux/v1/tecnologias")
@RequiredArgsConstructor
public class TechnologyController {

    private final CreateTechnologyUseCase createTechnologyUseCase;
    private final ListTechnologyUseCase listTechnologyUseCase;
    private final TechnologyMapper technologyMapper;

    @Operation(
            summary = "Registrar una nueva tecnología (HU1)",
            description = "Registra una tecnología en la base de datos",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tecnología creada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validación o nombre duplicado"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TechnologyDto> createTechnology(@Valid @RequestBody TechnologyDto dto) {
        Technology domainModel = technologyMapper.toDomain(dto);
        return createTechnologyUseCase.execute(domainModel)
                .map(technologyMapper::toDto);
    }

    @Operation(
            summary = "Listar tecnologías (HU2)",
            description = "Retorna lista de tecnologías, orden asc/desc por nombre",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tecnologías obtenida")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PagedResponseDto<TechnologyDto>> listTechnologies(
            @Parameter(description = "Orden de clasificación (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortOrder,
            @Parameter(description = "Número de página (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size
    ) {
        return listTechnologyUseCase.execute(sortOrder, page, size)
                .map(pagedResponse -> PagedResponseDto.<TechnologyDto>builder()
                        .content(pagedResponse.getContent().stream()
                                .map(technologyMapper::toDto)
                                .collect(Collectors.toList()))
                        .pageNumber(pagedResponse.getPageNumber())
                        .pageSize(pagedResponse.getPageSize())
                        .totalElements(pagedResponse.getTotalElements())
                        .totalPages(pagedResponse.getTotalPages())
                        .isLastPage(pagedResponse.isLastPage())
                        .build());
    }

    @Operation(
            summary = "Buscar tecnología por nombre",
            description = "Busca una tecnología específica por su nombre exacto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tecnología encontrada"),
                    @ApiResponse(responseCode = "404", description = "Tecnología no encontrada")
            }
    )
    @GetMapping("/buscar")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TechnologyDto> findByName(
            @Parameter(description = "Nombre de la tecnología a buscar")
            @RequestParam("nombre") String name
    ) {
        return listTechnologyUseCase.findByName(name)
                .map(technologyMapper::toDto);
    }
}