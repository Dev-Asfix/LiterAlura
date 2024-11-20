package com.alura.literatura.repository;

import com.alura.literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SerieRepository extends JpaRepository<Libros, Long> {

}
