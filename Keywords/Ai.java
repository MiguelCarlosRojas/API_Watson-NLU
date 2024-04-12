package Keywords;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Ai {

    public static void main(String[] args) {
        String texto = "Me encantan las manzanas! No me gustan las naranjas.";
        String[] objetivos = {"manzanas", "naranjas", "brocoli"};

        try {
            String respuesta = analizarTexto(texto, objetivos);
            System.out.println("Respuesta de la API de Watson NLU:\n" + respuesta);
        } catch (IOException e) {
            System.err.println("Error al ejecutar la solicitud a Watson NLU: " + e.getMessage());
        }
    }

    public static String analizarTexto(String texto, String[] objetivos) throws IOException {
        String apiKey = "YJaDwuQIFcWqE9eDM3sV_wvV7SoF7efcZZrcCvyYFLe5";
        String urlString = "https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/c36e7ec0-b19c-4f02-ab6a-fab0a132757f/v1/analyze?version=2019-07-12";

        // Construyendo el payload JSON
        StringBuilder jsonPayload = new StringBuilder();
        jsonPayload.append("{\"text\":\"").append(texto).append("\",\"features\":{\"sentiment\":{\"targets\":[");
        for (int i = 0; i < objetivos.length; i++) {
            jsonPayload.append("\"").append(objetivos[i]).append("\"");
            if (i < objetivos.length - 1) {
                jsonPayload.append(",");
            }
        }
        jsonPayload.append("]},\"keywords\":{\"emotion\":true}}}");

        // Configurando la solicitud HTTP
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(("apikey:" + apiKey).getBytes()));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Enviando la solicitud
        connection.getOutputStream().write(jsonPayload.toString().getBytes());

        // Leyendo la respuesta
        StringBuilder respuesta = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String linea;
            while ((linea = in.readLine()) != null) {
                respuesta.append(linea);
            }
        }

        // Cerrando la conexión
        connection.disconnect();

        return respuesta.toString();
    }
}
