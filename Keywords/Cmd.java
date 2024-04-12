package Keywords;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Cmd {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Ingrese el texto:");
            String text = reader.readLine();

            System.out.println("Ingrese los objetivos (separados por coma):");
            String targetsInput = reader.readLine();
            String[] targets = targetsInput.split(",");

            String response = analyzeText(text, targets);
            System.out.println("Respuesta de la API de Watson NLU:\n" + response);
        } catch (IOException e) {
            System.err.println("Error al leer la entrada del usuario: " + e.getMessage());
        }
    }

    public static String analyzeText(String text, String[] targets) throws IOException {
        String apiKey = "YJaDwuQIFcWqE9eDM3sV_wvV7SoF7efcZZrcCvyYFLe5";
        String urlString = "https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/c36e7ec0-b19c-4f02-ab6a-fab0a132757f/v1/analyze?version=2019-07-12";

        // Construyendo el JSON payload
        StringBuilder jsonPayload = new StringBuilder();
        jsonPayload.append("{\"text\":\"").append(text).append("\",\"features\":{\"sentiment\":{\"targets\":[");
        for (int i = 0; i < targets.length; i++) {
            jsonPayload.append("\"").append(targets[i].trim()).append("\"");
            if (i < targets.length - 1) {
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
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        // Cerrando la conexión
        connection.disconnect();

        return response.toString();
    }
}
