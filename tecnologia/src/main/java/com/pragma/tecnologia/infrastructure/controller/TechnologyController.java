package com.pragma.tecnologia.infrastructure.controller;

import com.pragma.tecnologia.application.dto.PagedResponseDto;
import com.pragma.tecnologia.application.dto.TechnologyDto;
import com.pragma.tecnologia.application.mapper.TechnologyMapper;
import com.pragma.tecnologia.application.usecase.CreateTechnologyUseCase;
import com.pragma.tecnologia.application.usecase.ListTechnologyUseCase;
import com.pragma.tecnologia.domain.model.Technology;
import com.pragma.tecnologia.infrastructure.commons.Constants;
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

import java.lang.constant.Constable;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.API_BASE_PATH + Constants.API_TECHNOLOGIES_PATH)
@RequiredArgsConstructor
public class TechnologyController {

    private final CreateTechnologyUseCase createTechnologyUseCase;
    private final ListTechnologyUseCase listTechnologyUseCase;
    private final TechnologyMapper technologyMapper;

    @Operation(
            summary = "Registrar una nueva tecnologia",
            description = "Registra una tecnologia en la base de datos",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Tecnologia creada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validacion o nombre duplicado"
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
            summary = "Listar tecnologias",
            description = "Retorna lista de tecnologias, orden (asc/desc) por nombre",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tecnologias obtenida")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PagedResponseDto<TechnologyDto>> listTechnologies(
            @Parameter(description = "Orden de clasificacion (asc/desc)")
            @RequestParam(defaultValue = Constants.SORT_ASC) String sortOrder,
            @Parameter(description = "Numero de pagina")
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @Parameter(description = "Tamaño de pagina")
            @RequestParam(defaultValue = Constants.DEFAULT_PAGE_SIZE) int size
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
            summary = "Buscar tecnologia por nombre",
            description = "Busca una tecnologia especifica por su nombre exacto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tecnologia encontrada"),
                    @ApiResponse(responseCode = "404", description = Constants.ERROR_TECH_NOT_FOUND)
            }
    )
    @GetMapping(Constants.API_SEARCH_PATH)
    @ResponseStatus(HttpStatus.OK)
    public Mono<TechnologyDto> findByName(
            @Parameter(description = "Nombre de la tecnologia a buscar")
            @RequestParam("nombre") String name
    ) {
        return listTechnologyUseCase.findByName(name)
                .map(technologyMapper::toDto);
    }
}