package com.alura.literatura.repository;

import com.alura.literatura.model.Autores;
import com.alura.literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SerieRepository  extends JpaRepository<Libros, Long>{
    Optional<Libros> findByTituloContainsIgnoreCase(String titulo);

    //@Query("SELECT a FROM autores a JOIN FETCH a.libro_id")
    //List<Autores> findAllAutoresWithLibros();
}
