package app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Parqueadero {

    // Scanner único para todo el flujo
    private static final Scanner SCANNER = new Scanner(System.in);

    // Constantes del sistema
    private static final int CAPACIDAD_TOTAL = 10;
    private static final double TARIFA_HORA_CARRO = 4000;
    private static final double TARIFA_DIA_CARRO = 30000;
    private static final double TARIFA_HORA_MOTO = 2000;
    private static final double TARIFA_DIA_MOTO = 15000;
    private static final int UMBRAL_HORAS_DIA = 8;
    private static final int HORA_APERTURA = 8;
    private static final int HORA_CIERRE = 22;

    // Formato de fecha
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // Arreglos paralelos para las celdas
    private static String[] tiposVehiculos = new String[CAPACIDAD_TOTAL];
    private static LocalDateTime[] fechasIngreso = new LocalDateTime[CAPACIDAD_TOTAL];

    // Variables de recaudo
    private static double recaudoCarros = 0;
    private static double recaudoMotos = 0;

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            String opcion = leerOpcionMenu();

            switch (opcion) {
                case "1":
                    registrarEntrada();
                    break;
                case "2":
                    registrarSalida();
                    break;
                case "3":
                    mostrarMensaje("\n[Opción 3: Reporte de Ocupación - En desarrollo]");
                    break;
                case "4":
                    mostrarMensaje("\n[Opción 4: Reporte Financiero - En desarrollo]");
                    break;
                case "5":
                    salir = true;
                    mostrarMensaje("\n¡Gracias por usar el sistema de parqueadero!");
                    break;
            }
            mostrarMensaje("");
        }

        SCANNER.close();
    }

    private static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private static void mostrarMenu() {
        mostrarMensaje("=== SISTEMA DE GESTIÓN DE PARQUEADERO ===");
        mostrarMensaje("1. Registrar Entrada de Vehículo");
        mostrarMensaje("2. Registrar Salida y Cobro");
        mostrarMensaje("3. Reporte de Ocupación");
        mostrarMensaje("4. Reporte Financiero");
        mostrarMensaje("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // Muestra el menú, lee la opción y valida que esté entre 1 y 5
    private static String leerOpcionMenu() {
        while (true) {
            mostrarMenu();
            String opcion = SCANNER.nextLine().trim();
            if (opcion.equals("1") || opcion.equals("2") || opcion.equals("3")
                    || opcion.equals("4") || opcion.equals("5")) {
                return opcion;
            }
            mostrarMensaje("\nError: Opción no válida. Intente nuevamente.\n");
        }
    }

    // Método obligatorio: Celda Disponible
    private static int buscarCeldaDisponible() {
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (tiposVehiculos[i] == null) {
                return i;
            }
        }
        return -1;
    }

    // Cuenta cuántas celdas están libres
    private static int contarCeldasLibres() {
        int libres = 0;
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (tiposVehiculos[i] == null) {
                libres++;
            }
        }
        return libres;
    }

    // Método obligatorio: Registro de Entrada
    private static void registrarEntrada() {
        int posicion = buscarCeldaDisponible();

        if (posicion == -1) {
            mostrarMensaje("\nError: El parqueadero está lleno. No hay celdas disponibles.");
            return;
        }

        mostrarMensaje("\n--- REGISTRO DE ENTRADA ---");
        String tipo = leerTipoVehiculo();

        LocalDateTime fechaIngreso = leerFechaHora("Ingrese la fecha y hora de ingreso (yyyy-MM-dd HH:mm): ");
        tiposVehiculos[posicion] = tipo;
        fechasIngreso[posicion] = fechaIngreso;

        mostrarMensaje("Vehículo registrado exitosamente en la celda " + (posicion + 1) + ".");
        mostrarMensaje("Celdas libres disponibles: " + contarCeldasLibres());
    }

    // Método obligatorio: Registro de Salida
    private static void registrarSalida() {
        int numero = leerNumeroCelda();
        int posicion = numero - 1;

        if (tiposVehiculos[posicion] == null) {
            mostrarMensaje("\nError: La celda " + numero + " está vacía. No hay vehículo para registrar salida.");
            return;
        }

        LocalDateTime ingreso = fechasIngreso[posicion];
        LocalDateTime salida;
        while (true) {
            salida = leerFechaHora("Ingrese la fecha y hora de salida (yyyy-MM-dd HH:mm): ");
            if (!salida.isBefore(ingreso)) {
                break;
            }
            mostrarMensaje("Error: La fecha de salida no puede ser anterior a la de ingreso. Intente de nuevo.");
        }

        mostrarMensaje("\n--- REGISTRO DE SALIDA ---");
        mostrarMensaje("Celda: " + numero);
        mostrarMensaje("Tipo de vehículo: " + tiposVehiculos[posicion]);
        mostrarMensaje("Fecha de ingreso: " + ingreso.format(FORMATO_FECHA_HORA));
        mostrarMensaje("Fecha de salida: " + salida.format(FORMATO_FECHA_HORA));
        mostrarMensaje("[Liquidación y liberación de celda: en el siguiente paso]");
    }

    // Valida el tipo de vehículo: solo carro o moto
    private static String leerTipoVehiculo() {
        while (true) {
            System.out.print("Ingrese tipo de vehículo (carro / moto): ");
            String tipo = SCANNER.nextLine().trim().toLowerCase();
            if (tipo.equals("carro") || tipo.equals("moto")) {
                return tipo;
            }
            mostrarMensaje("Error: Tipo de vehículo no válido. Intente nuevamente.");
        }
    }

    // Valida el número de celda: debe estar entre 1 y CAPACIDAD_TOTAL
    private static int leerNumeroCelda() {
        while (true) {
            System.out.print("Ingrese el número de celda (1 a " + CAPACIDAD_TOTAL + "): ");
            String texto = SCANNER.nextLine().trim();
            int numero = leerDigitos(texto, 0, texto.length());
            if (numero >= 1 && numero <= CAPACIDAD_TOTAL) {
                return numero;
            }
            mostrarMensaje("Error: Número de celda no válido. Intente nuevamente.");
        }
    }

    // Lectura y validación de fecha/hora
    private static LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = SCANNER.nextLine().trim();

            if (!formatoFechaHoraValido(texto)) {
                mostrarMensaje("Error: Fecha u hora inválidas. Use yyyy-MM-dd HH:mm y horario 08:00-22:00.");
                continue;
            }

            LocalDateTime fechaParsed = LocalDateTime.parse(texto, FORMATO_FECHA_HORA);
            String textoReformateado = fechaParsed.format(FORMATO_FECHA_HORA);

            if (!texto.equals(textoReformateado)) {
                mostrarMensaje("Error: La fecha ingresada no existe en el calendario. Intente de nuevo.");
                continue;
            }

            return fechaParsed;
        }
    }

    private static boolean formatoFechaHoraValido(String texto) {
        if (texto.length() != 16
                || texto.charAt(4) != '-'
                || texto.charAt(7) != '-'
                || texto.charAt(10) != ' '
                || texto.charAt(13) != ':') {
            return false;
        }

        int anio = leerDigitos(texto, 0, 4);
        int mes = leerDigitos(texto, 5, 2);
        int dia = leerDigitos(texto, 8, 2);
        int hora = leerDigitos(texto, 11, 2);
        int minuto = leerDigitos(texto, 14, 2);

        return anio >= 1 && mes >= 1 && mes <= 12 && dia >= 1 && dia <= 31
                && hora >= HORA_APERTURA && hora <= HORA_CIERRE
                && (hora != HORA_CIERRE || minuto <= 0)
                && minuto >= 0 && minuto <= 59;
    }

    private static int leerDigitos(String texto, int inicio, int cantidad) {
        int valor = 0;
        for (int i = inicio; i < inicio + cantidad; i++) {
            char caracter = texto.charAt(i);
            if (caracter < '0' || caracter > '9') {
                return -1;
            }
            valor = valor * 10 + caracter - '0';
        }
        return valor;
    }
}