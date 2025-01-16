package com.pragma.capacidad.application.dto;

import com.pragma.capacidad.infrastructure.commons.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CapacityDto {

    @Schema(description = "Identificador único de la capacidad ", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = Constants.CAPACITY_NAME_REQUIRED)
    @Size(max = Constants.MAX_NAME_LENGTH, message = Constants.CAPACITY_NAME_MAX_LENGTH)
    @Schema(description = "Nombre de la capacidad", example = "Backend Developer")
    private String name;

    @NotBlank(message = Constants.CAPACITY_DESCRIPTION_REQUIRED)
    @Size(max = Constants.MAX_DESCRIPTION_LENGTH, message = Constants.CAPACITY_DESCRIPTION_MAX_LENGTH)
    @Schema(description = "Descripción de la capacidad", example = "Capacidad enfocada en desarrollo backend con Java")
    private String description;

    @Schema(description = "Listado de nombres de las tecnologías asociadas")
    private List<String> technologyNames;
}