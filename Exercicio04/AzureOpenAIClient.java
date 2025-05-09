import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class AzureOpenAIClient {

    public static void main(String[] args) {
        try {
            invokeRequestResponseService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void invokeRequestResponseService() throws IOException {
        String endpoint = "https://13881-mag7myly-swedencentral.cognitiveservices.azure.com/";
        String deployment = "gpt-4-32k";
        String apiVersion = "2024-03-01-preview";
        String apiKey = "50Rk35EgaxctzbkQcKxzgzVp9iTx5Hb9DYllLgZLIKZBWnukOJ0kJQQJ99BEACfhMk5XJ3w3AAAAACOGCjyQ";

        if (apiKey.isEmpty()) {
            throw new RuntimeException("API key must be provided");
        }

        URL url = new URL(
                endpoint + "openai/deployments/" + deployment + "/chat/completions?api-version=" + apiVersion);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // Allow all certificates (equivalent to .NET's
        // ServerCertificateCustomValidationCallback)
        connection.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        String requestBody = """
                {
                  "messages": [
                    {
                      "role": "user",
                      "content": "I am going to Paris, what should I see?"
                    },
                    {
                      "role": "assistant",
                      "content": "Paris, the capital of France, is known for its stunning architecture, art museums, historical landmarks, and romantic atmosphere. Here are some of the top attractions to see in Paris:\\n\\n1. The Eiffel Tower: The iconic Eiffel Tower is one of the most recognizable landmarks in the world and offers breathtaking views of the city.\\n2. The Louvre Museum: The Louvre is one of the world's largest and most famous museums, housing an impressive collection of art and artifacts, including the Mona Lisa.\\n3. Notre-Dame Cathedral: This beautiful cathedral is one of the most famous landmarks in Paris and is known for its Gothic architecture and stunning stained glass windows."
                    },
                    {
                      "role": "user",
                      "content": "What is so great about #1?"
                    }
                  ],
                  "max_tokens": 300,
                  "temperature": 1,
                  "top_p": 1,
                  "stop": []
                }
                """;

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes("UTF-8"));
        }

        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode < 400) ? connection.getInputStream() : connection.getErrorStream();

        StringBuilder rawResponse = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rawResponse.append(line.trim());
            }
        }

        if (responseCode == 200) {
            String content = extractContent(rawResponse.toString());
            System.out.println("🤖 Assistant reply:");
            System.out.println(content);
        } else {
            System.out.println("❌ Request failed. Status: " + responseCode);
            System.out.println("Raw response: " + rawResponse);
        }
    }

    // Simple JSON extractor (string-based)
    private static String extractContent(String json) {
        String marker = "\"content\":\"";
        int idx = json.indexOf(marker);
        if (idx == -1)
            return "(No assistant reply found)";
        int start = idx + marker.length();
        int end = json.indexOf("\"", start);

        // Unescape escaped characters (basic)
        String content = json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
        return content;
    }
}