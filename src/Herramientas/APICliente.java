package Herramientas;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICliente {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/235d67c33ee8ec7b2b321661/latest/";

    public static String obtenerDatosAPI(String monedaOrigen) {
        try {
            URL url = new URL(API_URL + monedaOrigen);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Agregar encabezado de User-Agent

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


//Your API Key: 235d67c33ee8ec7b2b321661
    //Example Request: https://v6.exchangerate-api.com/v6/235d67c33ee8ec7b2b321661/latest/USD

