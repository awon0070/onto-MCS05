package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
@SpringBootApplication
@Import(CorsConfig.class)
public class FetchDataApplication {

    public static void main(String[] args) {
        OntologyManager OM = new OntologyManager();
        SpringApplication.run(FetchDataApplication.class, args);
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Server IP Address: " + inetAddress.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @CrossOrigin(origins = "*")
    @RestController
    public static class DataController {
        @PostMapping("/api/saveData")
        public ResponseEntity<Map<String, String>> saveData(@RequestBody TextData textData) {
            String text = textData.getText();
            //Print the received data to the server console
            System.out.println("Received data: " + textData);
            System.out.println("Received data: " + text);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Data received and processed: " + text);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        public static class TextData {
            private String text;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}