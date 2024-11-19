package com.alura.literatura.model;

import java.util.List;

public class Libros {

    private String titulo;
    private List<DatosAutores> autores;
    private List<String> idiomas;
    private Integer descargas;

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.autores = datosLibros.autores();
        this.idiomas = datosLibros.idiomas();
        this.descargas = datosLibros.descargas();

    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public List<String> getLenguajes() {
        return idiomas;
    }

    public void setLenguajes(List<String> lenguajes) {
        this.idiomas = lenguajes;
    }

    public List<DatosAutores> getAutores() {
        return autores;
    }

    public void setAutores(List<DatosAutores> autores) {
        this.autores = autores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return
                "-----------------"+
                "\n📚 Título: '"  + titulo  +
                "\n✍️ Autores:  " + autores +
                "\n🌍 Idiomas: "  + idiomas +
                "\n⬇️ Descargas: " + descargas
                ;
    }
}
