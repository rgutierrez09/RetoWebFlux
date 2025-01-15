package com.pragma.capacidad.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CapacityDto {

    @Schema(description = "Identificador único de la capacidad", example = "1")
    private Long id;

    @NotBlank(message = "El nombre de la capacidad es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(description = "Nombre de la capacidad", example = "Backend Developer")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 90, message = "La descripción no puede superar 90 caracteres")
    @Schema(description = "Descripción de la capacidad", example = "Capacidad orientada a desarrollo backend con Java")
    private String description;

    @Schema(description = "Listado de IDs de las tecnologias asociadas")
    private List<Long> technologyIds;
}
