package com.pragma.tecnologia.infrastructure.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String API_BASE_PATH = "/webflux/v1";
    public static final String API_TECHNOLOGIES_PATH = "/tecnologias";
    public static final String API_SEARCH_PATH = "/buscar";

    public static final String DEFAULT_PAGE_SIZE = " 10 ";
    public static final String DEFAULT_PAGE_NUMBER = " 0";
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "esc";

    public static final String NAME_REQUIRED = " El nombre es obligatorio";
    public static final String NAME_LENGTH = " El nombre no puede superar 50 caracteres";
    public static final String DESCRIPTION_REQUIRED = " La descripcion es obligatoria";
    public static final String DESCRIPTION_LENGTH = " La descripcion no puede superar 90 caracteres";

    public static final String ERROR_INVALID_SORT = "El orden debe ser 'asc' o 'desc'";
    public static final String ERROR_TECH_NOT_FOUND = "Tecnologia no encontrada";
    public static final String ERROR_TECH_EXISTS = "El nombre de la tecnologia ya existe: ";
    public static final String ERROR_INVALID= " Entrada invalida : ";
    public static final String ERROR_ARGUMENT_INVALID= "Argumento invalido: ";
    public static final String ERROR_INTERN= "Error interno : ";

    public static final String SWAGGER_TITLE = "TECNOLOGIAS";
    public static final String SWAGGER_DESCRIPTION = "Microservicio para gestionar Tecnologias del reto de Reactivo";
    public static final String SWAGGER_VERSION = "1.0.0";

    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 90;
}
