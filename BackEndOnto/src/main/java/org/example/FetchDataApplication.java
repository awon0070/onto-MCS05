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
import java.util.List;
import java.util.Map;
@SpringBootApplication
@Import(CorsConfig.class)
public class FetchDataApplication {
    public static OntologyManager OM;
    public static void main(String[] args) {
        OM = new OntologyManager();
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
        public ResponseEntity<List<String[]>> saveData(@RequestBody TextData textData) {
            String text = textData.getText();
            int number = textData.getNumber();
            List<String[]> infoOutput;
            //Print the received data to the server console
            if(number==1){
                infoOutput= OM.findDisease(text);
            }
            else if(number==0){
                infoOutput = OM.findSymptom(text);
            }
            else {
                // Handle other cases or errors if needed
                return ResponseEntity.badRequest().build();
            }

            System.out.println("Received data: " + text);
            System.out.println("Received number: " + number);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Data received and processed: " + text);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(infoOutput);
        }

        public static class TextData {
            private String text;
            private int number; // Add a new field for the number

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getNumber() {
                return number;
            }

            public void setNumber(int number) {
                this.number = number;
            }
        }
    }
}