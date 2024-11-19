package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Autores {
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

    @Override
    public String toString() {
        return
                "------------------------------------"+
                "\n📖 Autor: " + nombre +
                "\n🎂 Fecha de nacimiento: " + fechaNacimiento +
                "\n🪦 Fecha de fallecimiento: " + fechaMuerte ;
    }
}
