package com.alura.literatura.principal;

import com.alura.literatura.model.*;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.Conversion;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner in = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private Conversion conversion = new Conversion();
    private List<DatosLibros> datosLista = new ArrayList<>();
    private List<Autores> datosDeAutores = new ArrayList<>();
    private ConsumoAPI consumoAPI = new ConsumoAPI();



    //Metodo menu para elegir lo que deseamos
    public void menu() {

        try {
            var opcion = -1;
            while (opcion != 0) {
                try {
                    var menu = """
                        ╔═════════════════════════════════════════════════╗
                        ║        🌟 MENÚ PRINCIPAL - LITERATURA 📚        ║
                        ╠═════════════════════════════════════════════════╣
                        ║  1️⃣  🔍 Buscar libro por título                 ║
                        ║  2️⃣  📖 Listar todos los libros registrados     ║
                        ║  3️⃣  🖋️ Listar todos los autores registrados    ║
                        ║  4️⃣  🗓️ Listar autores vivos por año            ║
                        ║  5️⃣  🌐 Listar libros por idioma                ║
                        ║  0️⃣  ❌ Salir                                   ║
                        ╚══════════════════════════════════════════════════╝
                        """;
                    System.out.println(menu);
                    System.out.print("➡️  Seleccione una opción: ");
                    opcion = in.nextInt();
                    in.nextLine();

                    switch (opcion) {
                        case 1 -> buscarLibroPorTitulo();
                        case 2 -> listarLibros();
                        case 3 -> listarAutores();
                        case 4 -> listarAutoresVivos();
                        case 0 -> System.out.println("\n ❌ Cerrando aplicación. ¡Hasta luego! 👋");
                        default -> System.out.println("Opcion Invalida");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entreda no valida. Ingresa un numero");
                    in.nextLine();
                }
            }

        } catch (Exception e) {
            System.out.println("Error en " + e);
        }


    }


    //Metodos de las opciones
    private DatosLibros getDatos() {
        System.out.println("\n🔍 Buscar libro por título:");
        System.out.print("➡️  Ingresa el título del libro: ");
        var titulo = in.nextLine();
        var json = consumoAPI.consumirApi(URL_BASE  + titulo.replace(" ", "%20"));
        var datos = conversion.convertirDatos(json, DatosLista.class);
        var primerResultado = datos.resultado()
                .stream()
                .findFirst()
                .orElse(null);

        if(primerResultado != null){
            System.out.println("✅ Primer resultado encontrado:");
        } else {
            System.out.println("❌ No se encontraron resultados para el título ingresado.");

        }



        return primerResultado;
    }

    public void buscarLibroPorTitulo(){
        DatosLibros datos = getDatos();
        Libros libros = new Libros(datos);
        datosLista.add(datos);
        System.out.println(libros);

        List<Autores> datosAutores = datos.autores().stream()
                .map(autor -> new Autores(new DatosAutores(autor.nombre(), autor.fechaNacimiento(), autor.fechaMuerte())))
                .collect(Collectors.toList());

        this.datosDeAutores.addAll(datosAutores);
    }

    public void listarLibros(){
        System.out.println("\n📚 Lista de libros registrados:");
        datosLista.forEach( e -> System.out.println("- " + e.titulo()));
    }

    public void listarAutores(){
        System.out.println(" 🖋️ Listar todos los autores registrados:");
        datosDeAutores.forEach(System.out::println);
    }

    public void listarAutoresVivos(){
        System.out.println("Dime el año en el que quieres buscar : ");
        var numeroAutor = in.nextInt();

        datosDeAutores.stream()
                .filter(e -> e.getFechaNacimiento() >= numeroAutor)
                .forEach(System.out::println);
    }


}
