 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reservacioneshotel;

/**
 *
 * @author alexander
 */

import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        // Inicializar Firebase
        try {
            FireBaseConnection.inicializar();
        } catch (Exception e) {
            System.out.println("Error al inicializar Firebase (puedes seguir probando en memoria): " + e.getMessage());
        }

        // Crear servicios
        ClienteService clienteService = new ClienteService();
        HabitacionService habitacionService = new HabitacionService();
        ReservacionService reservacionService = new ReservacionService();

        // Scanner y menú principal
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n==============================");
            System.out.println("   SISTEMA HOTEL - MENU");
            System.out.println("==============================");
            System.out.println("1. Gestion de clientes");
            System.out.println("2. Gestion de habitaciones");
            System.out.println("3. Gestion de reservaciones");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    menuClientes(sc, clienteService);
                    break;
                case 2:
                    menuHabitaciones(sc, habitacionService);
                    break;
                case 3:
                    menuReservaciones(sc, reservacionService, clienteService, habitacionService);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo.");
            }

        } while (opcion != 0);

        sc.close();
    }

    // ===========================
    // SUBMENU CLIENTES
    // ===========================
    private static void menuClientes(Scanner sc, ClienteService clienteService) {
        int opcion;
        do {
            System.out.println("\n--- MENU CLIENTES ---");
            System.out.println("1. Crear cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Correo: ");
                    String email = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();

                    Cliente nuevo = new Cliente();
                    nuevo.setNombreCliente(nombre);
                    nuevo.setEmailCliente(email);
                    nuevo.setTelefonoCliente(telefono);

                    String id = clienteService.crearCliente(nuevo);
                    System.out.println("Cliente creado con ID: " + id);
                    break;

                case 2:
                    System.out.println("\n--- LISTA DE CLIENTES ---");
                    for (Cliente c : clienteService.obtenerTodosLosClientes()) {
                        System.out.println(c.getIdCliente() + " - " + c);
                    }
                    break;

                case 3:
                    System.out.print("Ingrese ID del cliente: ");
                    String idBuscado = sc.nextLine();
                    Cliente encontrado = clienteService.obtenerClienteId(idBuscado);
                    if (encontrado != null) {
                        System.out.println("Cliente encontrado: " + encontrado);
                    } else {
                        System.out.println("No existe un cliente con ese ID.");
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    // ===========================
    // SUBMENU HABITACIONES
    // ===========================
    private static void menuHabitaciones(Scanner sc, HabitacionService habitacionService) {
        int opcion;
        do {
            System.out.println("\n--- MENU HABITACIONES ---");
            System.out.println("1. Crear habitacion");
            System.out.println("2. Listar habitaciones");
            System.out.println("3. Listar habitaciones DISPONIBLES");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    System.out.print("Numero de habitacion: ");
                    String numero = sc.nextLine();
                    System.out.print("Tipo (Individual/Doble/Suite): ");
                    String tipo = sc.nextLine();
                    System.out.print("Precio por noche: ");
                    double precio = leerDouble(sc);

                    Habitacion h = new Habitacion();
                    h.setNumeroHabitacion(numero);
                    h.setTipoHabitacion(tipo);
                    h.setPrecioHabitacion(precio);
                    h.setEstadoHabitacion("DISPONIBLE");

                    String idHab = habitacionService.crearHabitacion(h);
                    System.out.println("Habitacion creada con ID: " + idHab);
                    break;

                case 2:
                    System.out.println("\n--- TODAS LAS HABITACIONES ---");
                    for (Habitacion hab : habitacionService.obtenerTodasLasHabitaciones()) {
                        System.out.println(hab.getIdHabitacion() + " - " + hab);
                    }
                    break;

                case 3:
                    System.out.println("\n--- HABITACIONES DISPONIBLES ---");
                    for (Habitacion hab : habitacionService.obtenerHabitacionesDisponibles()) {
                        System.out.println(hab.getIdHabitacion() + " - " + hab);
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    // ===========================
    // SUBMENU RESERVACIONES
    // ===========================
    private static void menuReservaciones(Scanner sc,
                                          ReservacionService reservacionService,
                                          ClienteService clienteService,
                                          HabitacionService habitacionService) {
        int opcion;
        do {
            System.out.println("\n--- MENU RESERVACIONES ---");
            System.out.println("1. Crear reservacion");
            System.out.println("2. Listar reservaciones");
            System.out.println("0. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    System.out.println("\n--- CREAR RESERVACION ---");
                    System.out.print("ID Cliente: ");
                    String idCliente = sc.nextLine();
                    Cliente cliente = clienteService.obtenerClienteId(idCliente);
                    if (cliente == null) {
                        System.out.println("Cliente no encontrado. Primero debe existir el cliente.");
                        break;
                    }

                    System.out.print("ID Habitacion: ");
                    String idHabitacion = sc.nextLine();
                    Habitacion habitacion = habitacionService.obtenerHabitacionId(idHabitacion);
                    if (habitacion == null) {
                        System.out.println("Habitacion no encontrada.");
                        break;
                    }

                    // Fechas: ahora + 1 día y ahora + 2 días
                    LocalDateTime inicio = LocalDateTime.now().plusDays(1);
                    LocalDateTime fin = LocalDateTime.now().plusDays(2);

                    // Validar si está disponible en esas fechas
                    boolean disponible = reservacionService.habitacionDisponibleEnFechas(
                            idHabitacion, inicio, fin
                    );
                    if (!disponible) {
                        System.out.println("La habitación NO está disponible en ese rango de fechas.");
                        break;
                    }

                    Reservacion r = new Reservacion();
                    r.setIdCliente(idCliente);
                    r.setIdHabitacion(idHabitacion);
                    // Guardamos las fechas como String (ISO)
                    r.setInicio(inicio.toString());
                    r.setFin(fin.toString());
                    r.setEstadoReservacion("ACTIVA");
                    r.setFechaCreacion(LocalDateTime.now().toString());

                    String idRes = reservacionService.crearReservacion(r);
                    System.out.println("Reservacion creada con ID: " + idRes);
                    System.out.println(r);
                    break;

                case 2:
                    System.out.println("\n--- TODAS LAS RESERVACIONES ---");
                    for (Reservacion res : reservacionService.obtenerTodasLasReservaciones()) {
                        System.out.println(res.getIdReservacion() + " - " + res);
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);
    }

    // ===========================
    // METODOS DE LECTURA SEGURA
    // ===========================
    private static int leerEntero(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            sc.nextLine();
        }
        int valor = sc.nextInt();
        sc.nextLine(); // limpiar \n
        return valor;
    }

    private static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un numero valido: ");
            sc.nextLine();
        }
        double valor = sc.nextDouble();
        sc.nextLine(); // limpiar \n
        return valor;
    }
}
