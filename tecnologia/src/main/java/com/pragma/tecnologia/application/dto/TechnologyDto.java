package com.pragma.tecnologia.application.dto;

import com.pragma.tecnologia.infrastructure.commons.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TechnologyDto {

    @Schema(description = "Identificador único de la capacidad (autogenerado)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = Constants.NAME_REQUIRED)
    @Size(max = Constants.MAX_NAME_LENGTH, message = Constants.NAME_LENGTH)
    @Schema(description = "Nombre de la tecnologia", example = "Java")
    private String name;

    @NotBlank(message = Constants.DESCRIPTION_REQUIRED)
    @Size(max =Constants.MAX_DESCRIPTION_LENGTH, message = Constants.DESCRIPTION_LENGTH)
    @Schema(description = "Descripcion de la tecnologia", example = "Lenguaje de programacion")
    private String description;
}