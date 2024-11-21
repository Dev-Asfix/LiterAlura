package com.alura.literatura.repository;

import com.alura.literatura.model.Autores;
import com.alura.literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface SerieRepository  extends JpaRepository<Libros, Long>{
    // Buscar libros por título (ya existente)
    Optional<Libros> findByTituloContainsIgnoreCase(String titulo);

    // Buscar autores por nombre
    @Query("SELECT a FROM Autores a WHERE LOWER(a.nombre) = LOWER(:nombre)")
    Optional<Autores> findByNombreIgnoreCase(String nombre);

    // Recuperar todos los autores con sus libros
    @Query("SELECT a FROM Autores a JOIN FETCH a.libros")
    List<Autores> findAllAutoresWithLibros();

    @Query("SELECT a FROM Autores a WHERE a.fechaNacimiento >= :fecha")
    List<Autores> findAutoresVivosDesde(Integer fecha);

    @Query(value = "SELECT * FROM libros l WHERE :idioma = ANY (l.idiomas)", nativeQuery = true)
    List<Libros> findLibrosByIdioma(String idioma);



}
