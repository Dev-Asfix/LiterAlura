package com.alura.literatura.principal;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.SerieRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.Conversion;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner in = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private Conversion conversion = new Conversion();
    private List<DatosLibros> datosLista = new ArrayList<>();
    private List<Autores> datosDeAutores = new ArrayList<>();
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Libros libros;
    private String titulo;
    private List<Libros> listLibros = new ArrayList<>();


    private SerieRepository repository;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }


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
                        case 5 -> listarLibrosPorIdioma();
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
        titulo = in.nextLine();
        var json = consumoAPI.consumirApi(URL_BASE + titulo.replace(" ", "%20"));
        var datos = conversion.convertirDatos(json, DatosLista.class);
        var primerResultado = datos.resultado()
                .stream()
                .findFirst()
                .orElse(null);


        if (primerResultado != null) {
            System.out.println("✅ Primer resultado encontrado:");
        } else {
            System.out.println("❌ No se encontraron resultados para el título ingresado.");

        }


        return primerResultado;
    }

    public void buscarLibroPorTitulo() {
        DatosLibros datos = getDatos();
        libros = new Libros(datos);
        datosLista.add(datos);

        Optional<Libros> librosOptional = listLibros.stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(titulo))
                .findFirst();

        if (librosOptional.isPresent()) {

            System.out.println("❌ El libro con el título '" + titulo + "' ya está registrado.");

        } else {

            Optional<Libros> libroEnBaseDeDatos = repository.findByTituloContainsIgnoreCase(titulo);

            if (libroEnBaseDeDatos.isPresent()) {
                // Si el libro ya existe en la base de datos, muestra un mensaje
                System.out.println("❌ El libro con el título '" + titulo + "' ya existe en la base de datos.");
            } else {
                List<Autores> datosAutores = datos.autores().stream()
                        .map(autor -> new Autores(new DatosAutores(autor.nombre(), autor.fechaNacimiento(), autor.fechaMuerte())))
                        .collect(Collectors.toList());


                libros.setAutores(datosAutores);
                this.datosDeAutores.addAll(datosAutores);
                repository.save(libros);
                System.out.println(libros);
                System.out.println("✅ El libro '" + titulo + "' ha sido registrado exitosamente.");
            }
        }


    }

    public void listarLibros() {
        System.out.println("\n📚 Lista de libros registrados:");
        List<Libros> librosList = repository.findAll();
        librosList.forEach(e -> System.out.println("- " + e.getTitulo()));
    }

    public void listarAutores() {
        System.out.println(" 🖋️ Listar todos los autores registrados:");
        datosDeAutores.forEach(System.out::println);
    }

    public void listarAutoresVivos() {
        System.out.println("Dime el año en el que quieres buscar : ");
        var numeroAutor = in.nextInt();

        datosDeAutores.stream()
                .filter(e -> e.getFechaNacimiento() >= numeroAutor)
                .forEach(System.out::println);
    }

    public void listarLibrosPorIdioma() {
        System.out.println("""
                en - Inglés
                es - Español
                fr - Francés
                it - Italiano
                pt - Portugués
                """);
        System.out.print("➡️  Ingresa el código del idioma: ");
        var idioma = in.nextLine();
        // Filtramos los libros por idioma y recolectamos los resultados en una lista
        // Filtramos los libros por idioma y recolectamos los resultados en una lista
        List<Libros> librosFiltrados = datosLista.stream()
                .filter(libro -> libro.idiomas().contains(idioma)) // Filtramos por idioma
                .map(e -> new Libros(e)) // Mapeamos los libros a objetos Libros
                .collect(Collectors.toList());
        librosFiltrados.forEach(System.out::println);

    }


}
