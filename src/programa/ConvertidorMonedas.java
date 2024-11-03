package programa;

import Herramientas.APICliente;
import Herramientas.Historial;
import Herramientas.Moneda;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConvertidorMonedas {
    private static final List<Moneda> monedas = new ArrayList<>();
    private static final Historial historial = new Historial();

    static {
        monedas.add(new Moneda("ARS", "Peso argentino"));
        monedas.add(new Moneda("BOB", "Boliviano"));
        monedas.add(new Moneda("BRL", "Real brasileño"));
        monedas.add(new Moneda("CLP", "Peso chileno"));
        monedas.add(new Moneda("COP", "Peso colombiano"));
        monedas.add(new Moneda("USD", "Dólar estadounidense"));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            mostrarHistorial();
            mostrarMenu();

            // Seleccionar opción
            System.out.print("Selecciona una opción: ");
            int opcion = scanner.nextInt();

            if (!esOpcionValida(opcion) && opcion != 8) {
                System.out.println("Selección no válida.");
                continue;
            }

            if (opcion == 8) {
                System.out.println("Saliendo del programa...");
                continuar = false;
                break;
            }

            convertirMoneda(opcion, scanner);

            System.out.print("¿Deseas realizar otra conversión? (1: Sí, 2: No, 3: No viste tu moneda agrega una nueva moneda): ");
            int continuarOpcion = scanner.nextInt();
            if (continuarOpcion == 1) {
                continue;
            } else if (continuarOpcion == 2) {
                System.out.println("Saliendo del programa...");
                continuar = false;
            } else if (continuarOpcion == 3) {
                agregarNuevaMoneda(scanner);
            }
        }
        scanner.close();
    }

    private static void mostrarHistorial() {
        //System.out.println("*****************************************");

        historial.mostrarHistorial();
        System.out.println("*****************************************");
    }

    private static void mostrarMenu() {
        System.out.println("Monedas disponibles:");
        for (int i = 0; i < monedas.size(); i++) {
            System.out.println((i + 1) + ". " + monedas.get(i));
        }
        System.out.println("8. Salir del programa");
        System.out.println("*****************************************");
    }

    private static void agregarNuevaMoneda(Scanner scanner) {
        scanner.nextLine();  // Consumir la nueva línea
        System.out.print("Ingresa el código de la nueva moneda (ej. EUR): ");
        String nuevaMoneda = scanner.nextLine().toUpperCase();
        System.out.print("Ingresa el nombre de la nueva moneda: ");
        String nombreMoneda = scanner.nextLine();
        monedas.add(new Moneda(nuevaMoneda, nombreMoneda));
    }

    private static boolean esOpcionValida(int opcion) {
        return opcion >= 1 && opcion <= monedas.size();
    }

    private static void convertirMoneda(int opcion, Scanner scanner) {
        Moneda monedaOrigen = monedas.get(opcion - 1);
        System.out.print("Selecciona el número de la moneda de destino: ");
        int indiceDestino = scanner.nextInt() - 1;
        System.out.print("Ingresa el monto a convertir: ");
        double monto = scanner.nextDouble();

        Moneda monedaDestino = monedas.get(indiceDestino);

        // Obtener la respuesta de la API
        String httpResponse = APICliente.obtenerDatosAPI(monedaOrigen.getCodigo());
        if (httpResponse == null) {
            System.out.println("Error al obtener los datos de la API.");
            return;
        }

        // Parsear JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = JsonParser.parseString(httpResponse).getAsJsonObject();
        double tasaCambio = jsonObject.getAsJsonObject("conversion_rates").get(monedaDestino.getCodigo()).getAsDouble();
        double resultado = monto * tasaCambio;

        // Mostrar resultado
        System.out.println("*****************************************");
        System.out.println("Monto convertido: " + resultado);
        System.out.println("*****************************************");

        // Guardar en historial
        historial.agregarConversion(monto + " " + monedaOrigen.getCodigo() + " son: " + resultado + " " + monedaDestino.getCodigo());

        // Mostrar historial actualizado
        mostrarHistorial();
    }
}
