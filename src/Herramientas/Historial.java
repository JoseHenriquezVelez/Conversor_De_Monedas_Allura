package Herramientas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Historial {
    private List<String> conversiones;

    public Historial() {
        this.conversiones = new ArrayList<>();
    }

    public void agregarConversion(String conversion) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fecha = sdf.format(new Date());
        conversiones.add(fecha + " - " + conversion);
    }

    public void mostrarHistorial() {
        System.out.println("****************"+"\n Historial de conversiones:");
        if (conversiones.isEmpty()) {
            System.out.println("No hay conversiones en el historial.");
        } else {
            for (String entry : conversiones) {
                System.out.println(entry);
            }
        }
    }
}
