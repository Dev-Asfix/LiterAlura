package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLista(

        //@JsonAlias("count") String contador,
        @JsonAlias("results") List<DatosLibros> resultado

) {
}
