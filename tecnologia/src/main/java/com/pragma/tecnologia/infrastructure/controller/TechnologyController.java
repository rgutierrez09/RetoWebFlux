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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/v1/tecnologias")
@RequiredArgsConstructor
public class TechnologyController {

    private final CreateTechnologyUseCase createTechnologyUseCase;
    private final ListTechnologyUseCase listTechnologyUseCase;
    private final TechnologyMapper technologyMapper;

    @Operation(
            summary = "Registrar una nueva tecnologia (HU1)",
            description = "Registra una tecnologia en la base de datos",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "tecnologia creada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo creacion Exitosa",
                                                    value = """
                            {
                              "id": 1,
                              "name": "Java",
                              "description": "Lenguaje de programacion"
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validacion o nombre duplicado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo Nombre Duplicado",
                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El nombre de la tecnologia ya existe"
                            }
                            """
                                            ),
                                            @ExampleObject(
                                                    name = "Ejemplo Error de Validacion",
                                                    value = """
                            {
                              "status": 400,
                              "error": "Bad Request",
                              "message": "El nombre es obligatorio"
                            }
                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo Error 500",
                                                    value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error interno del servidor"
                            }
                            """
                                            )
                                    }
                            )
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
            summary = "Listar tecnologias (HU2)",
            description = "Retorna lista de tecnologias, orden asc/desc por nombre",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de tecnologias obtenida"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo Error 500",
                                                    value = """
                            {
                              "status": 500,
                              "error": "Internal Server Error",
                              "message": "Error interno del servidor"
                            }
                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PagedResponseDto<TechnologyDto>> listTechnologies(
            @Parameter(description = "Orden de clasificación (asc/desc)", required = false)
            @RequestParam(name = "sort", defaultValue = "asc") String sortOrder,
            @Parameter(description = "Número de página (0-based)", required = false)
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página", required = false)
            @RequestParam(defaultValue = "10") int size
    ) {
        return listTechnologyUseCase.execute(sortOrder, page, size)
                .map(pagedResponse -> PagedResponseDto.<TechnologyDto>builder()
                        .content(pagedResponse.getContent().stream()
                                .map(technologyMapper::toDto)
                                .toList())
                        .pageNumber(pagedResponse.getPageNumber())
                        .pageSize(pagedResponse.getPageSize())
                        .totalElements(pagedResponse.getTotalElements())
                        .totalPages(pagedResponse.getTotalPages())
                        .isLastPage(pagedResponse.isLastPage())
                        .build());
    }
}