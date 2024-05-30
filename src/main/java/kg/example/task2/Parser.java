package kg.example.task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import kg.example.task2.objects.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(9091), 0);
        server.createContext("/convert", new ConvertHandler());
        server.setExecutor(null);
        server.start();
    }

    static class ConvertHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
                    Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                    Data data = (Data) unmarshaller.unmarshal(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonString = objectMapper.writeValueAsString(data);

                    saveDataToFile(data, jsonString);

                    exchange.sendResponseHeaders(200, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, -1);
                } finally {
                    inputStream.close();
                    exchange.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
            }
        }

        private void saveDataToFile(Data data, String jsonString) throws IOException {
            String desktopPath = System.getProperty("user.home") + "/Desktop/parser/";

            Path folderPath = Paths.get(desktopPath);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            String type = data.getType();
            String fileName = type + "-" + formattedDateTime + ".json";

            Path filePath = Paths.get(desktopPath + fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(jsonString);
            }
        }
    }
}
