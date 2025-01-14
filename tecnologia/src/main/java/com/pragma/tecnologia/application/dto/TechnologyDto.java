package com.pragma.tecnologia.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TechnologyDto {

    @Schema(description = "Identificador unico de la tecnologia", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
    @Schema(description = "Nombre de la tecnologia", example = "Java")
    private String name;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 90, message = "La descripcion no puede superar 90 caracteres")
    @Schema(description = "Descripcion de la tecnologia", example = "Lenguaje de programacion")
    private String description;
}