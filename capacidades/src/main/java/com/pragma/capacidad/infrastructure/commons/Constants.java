package com.pragma.capacidad.infrastructure.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    public static final String CAPACITY_NAME_REQUIRED = "El nombre de la capacidad es obligatorio";
    public static final String CAPACITY_NAME_MAX_LENGTH = "El nombre no puede superar 50 caracteres";
    public static final String CAPACITY_DESCRIPTION_REQUIRED = "La descripción es obligatoria";
    public static final String CAPACITY_DESCRIPTION_MAX_LENGTH = "La descripción no puede superar 90 caracteres";
    public static final String MINIMUM_TECHNOLOGIES = "La capacidad debe incluir al menos 3 tecnologías";
    public static final String TECHNOLOGIES_RANGE = "La capacidad debe tener entre 3 y 20 tecnologías";
    public static final String TECHNOLOGIES_NOT_FOUND = "Algunas tecnologías no existen en el sistema";
    public static final String DUPLICATE_CAPACITY_NAME = "Ya existe una capacidad con el nombre: %s";
    public static final String INVALID_SORT_ORDER = "sortOrder debe ser 'asc' o 'desc'";
    public static final String INVALID_SORT_BY = "sortBy debe ser 'nombre' o 'techCount'";

    public static final int MIN_TECHNOLOGIES = 3;
    public static final int MAX_TECHNOLOGIES = 20;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 90;

    public static final String NAME = "nombre";
    public static final String DEFAULT_SORT_ASC = "asc";
    public static final String SORT_BY_NAME = "nombre";
    public static final String SORT_BY_TECH_COUNT = "techCount";
    public static final String SORT_ORDER_DESC = "desc";

    public static final String BASE_URL = "/webflux/v1/capacidades";
    public static final String TECHNOLOGY_SEARCH_PATH = "/webflux/v1/tecnologias/buscar";
    public static final String BASE_URL_TECHNOLOGIES = "http://localhost:8080";

    public static final String CAPACITIES_TABLE = "capacidades";
    public static final String CAPACITY_TECHNOLOGIES_TABLE = "capacidad_tecnologias";

}