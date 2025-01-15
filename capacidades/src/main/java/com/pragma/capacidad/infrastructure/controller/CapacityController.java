package com.pragma.capacidad.infrastructure.controller;

import com.pragma.capacidad.application.dto.CapacityDto;
import com.pragma.capacidad.application.dto.PagedResponseDto;
import com.pragma.capacidad.application.mapper.CapacityMapper;
import com.pragma.capacidad.application.usecase.CreateCapacityUseCase;
import com.pragma.capacidad.application.usecase.ListCapacityUseCase;
import com.pragma.capacidad.domain.model.Capacity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/webflux/v1/capacidades")
@RequiredArgsConstructor
public class CapacityController {

    private final CreateCapacityUseCase createCapacityUseCase;
    private final ListCapacityUseCase listCapacityUseCase;
    private final CapacityMapper capacityMapper;

    @Operation(
            summary = "Registrar una nueva capacidad (HU3)",
            description = "Registra una capacidad con un listado de tecnologías (mínimo 3, máximo 20).",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Capacidad creada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación o nombre duplicado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CapacityDto> createCapacity(@Valid @RequestBody CapacityDto dto) {
        Capacity domainModel = capacityMapper.toDomain(dto);
        return createCapacityUseCase.execute(domainModel)
                .map(capacityMapper::toDto);
    }

    @Operation(
            summary = "Listar capacidades (HU4)",
            description = """
                    Retorna la lista de capacidades con paginación y orden.
                    Se puede ordenar asc/desc por:
                     - nombre: usar ?sortBy=nombre&sortOrder=asc
                     - techCount: usar ?sortBy=techCount&sortOrder=asc
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista paginada de capacidades obtenida"),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<PagedResponseDto<CapacityDto>> listCapacities(
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return listCapacityUseCase.execute(sortBy, sortOrder, page, size)
                .map(paged -> PagedResponseDto.<CapacityDto>builder()
                        .content(
                                paged.getContent().stream()
                                        .map(capacityMapper::toDto)
                                        .toList()
                        )
                        .pageNumber(paged.getPageNumber())
                        .pageSize(paged.getPageSize())
                        .totalElements(paged.getTotalElements())
                        .totalPages(paged.getTotalPages())
                        .isLastPage(paged.isLastPage())
                        .build()
                );
    }
}