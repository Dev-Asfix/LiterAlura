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

        // Validar en memoria
        if (listLibros.stream().anyMatch(l -> l.getTitulo().equalsIgnoreCase(titulo))) {
            System.out.println("❌ El libro con el título '" + titulo + "' ya está registrado.");
            return;
        }

        // Validar en la base de datos
        if (repository.findByTituloContainsIgnoreCase(titulo).isPresent()) {
            System.out.println("❌ El libro con el título '" + titulo + "' ya existe en la base de datos.");
            return;
        }

        // Crear autores y asignarlos al libro
        List<Autores> datosAutores = datos.autores().stream()
                .distinct()
                .map(autor -> new Autores(new DatosAutores(autor.nombre(), autor.fechaNacimiento(), autor.fechaMuerte())))
                .collect(Collectors.toList());

        libros.setAutores(datosAutores);
        this.datosDeAutores.addAll(datosAutores);

        // Guardar en la base de datos
        try {
            repository.save(libros);
            System.out.println("✅ El libro '" + titulo + "' ha sido registrado exitosamente.");
        } catch (Exception e) {
            System.err.println("❌ Error al guardar el libro: " + e.getMessage());
        }
    }






    public void listarLibros() {
        System.out.println("\n📚 Lista de libros registrados:");
        List<Libros> librosList = repository.findAll();
        librosList.forEach(e -> System.out.println("- " + e.getTitulo()));
    }

    public void listarAutores() {
        System.out.println(" 🖋️ Listar todos los autores registrados:");
        //List<Autores>  autoresList = repository.findAllAutoresWithLibros();
        List<Autores> autoresList = repository.findAllAutoresWithLibros();
        if(autoresList.isEmpty()){
            System.out.println("❌ No hay autores registrados.");
        } else {
            autoresList.forEach(autores -> {
                System.out.println(autores);
                System.out.println("📚 Libros de este autor (" +autores.getLibros().size() + "):");

                autores.getLibros().forEach( libros1 ->
                        System.out.println(" -" + libros1.getTitulo()));
            });
        }

    }

    public void listarAutoresVivos() {
        System.out.println("Dime el año en el que quieres buscar : ");
        var numeroAutor = in.nextInt();

        //datosDeAutores.stream()
         //       .filter(e -> e.getFechaNacimiento() >= numeroAutor)
          //      .forEach(System.out::println);

        List<Autores> autores = repository.findAutoresVivosDesde(numeroAutor);
        if(autores.isEmpty()){
            System.out.println("No se encontraron autores vivos desde el año " + numeroAutor);
        } else {
            autores.forEach(System.out::println);
        }
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
        //List<Libros> librosFiltrados = datosLista.stream()
        //        .filter(libro -> libro.idiomas().contains(idioma)) // Filtramos por idioma
        //        .map(e -> new Libros(e)) // Mapeamos los libros a objetos Libros
        //        .collect(Collectors.toList());
        //librosFiltrados.forEach(System.out::println);

        List<Libros> librosList = repository.findLibrosByIdioma(idioma);
        if(librosList.isEmpty()){
            System.out.println("No se encontraron libros en el idioma seleccionado: " + idioma);
        } else {
            librosList.forEach(System.out::println);
        }

    }


}
