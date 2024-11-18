package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Libros(

        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<Autores>autores,
        @JsonAlias("languages") List<String> lenguajes,
        @JsonAlias("dowload_count") Integer descargas

) {
}
