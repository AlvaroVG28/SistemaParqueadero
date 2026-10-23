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

    // Formato de fecha y hora
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Arreglos paralelos para el control de celdas
    private static String[] tiposVehiculos = new String[CAPACIDAD_TOTAL];
    private static LocalDateTime[] fechasIngreso = new LocalDateTime[CAPACIDAD_TOTAL];

    // Variables acumuladoras de recaudo
    private static double recaudoCarros = 0;
    private static double recaudoMotos = 0;

    public static void main(String[] args) {

}
}