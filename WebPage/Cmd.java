package WebPage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class Cmd {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Ingrese la URL:");
            String url = reader.readLine();

            String response = analyzeURL(url);
            System.out.println("Respuesta de la API de Watson NLU:\n" + response);
        } catch (IOException e) {
            System.err.println("Error al leer la entrada del usuario: " + e.getMessage());
        }
    }

    public static String analyzeURL(String url) throws IOException {
        String apiKey = "YJaDwuQIFcWqE9eDM3sV_wvV7SoF7efcZZrcCvyYFLe5";
        String urlString = "https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/c36e7ec0-b19c-4f02-ab6a-fab0a132757f/v1/analyze?version=2019-07-12";

        // Construyendo el JSON payload
        String jsonPayload = "{\"url\":\"" + url + "\",\"features\":{\"sentiment\":{},\"categories\":{},\"concepts\":{},\"entities\":{},\"keywords\":{}}}";

        // Configurando la solicitud HTTP
        URL apiUrl = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(("apikey:" + apiKey).getBytes()));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Enviando la solicitud
        connection.getOutputStream().write(jsonPayload.getBytes());

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
