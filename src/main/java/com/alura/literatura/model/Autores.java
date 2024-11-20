package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

//@Entity
//@Table(name ="autores")
public class Autores {

  //  @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Integer fechaNacimiento;
    private Integer fechaMuerte;
    private String nombre;

    public Autores(DatosAutores clase) {
        this.fechaNacimiento = clase.fechaNacimiento();
        this.fechaMuerte = clase.fechaMuerte();
        this.nombre = clase.nombre();
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @Override
    public String toString() {
        return
                "------------------------------------"+
                "\n📖 Autor: " + nombre +
                "\n🎂 Fecha de nacimiento: " + fechaNacimiento +
                "\n🪦 Fecha de fallecimiento: " + fechaMuerte ;
    }
}
