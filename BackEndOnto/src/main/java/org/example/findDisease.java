package org.example;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDFS;

import java.util.Scanner;
public class findDisease {
    public static void main(OntModel model) {
        //get user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter symptom(s) (separated by ','): ");
        String userInput = scanner.nextLine();
        String[] symptoms = userInput.split(",");
        scanner.close();

        // Load your ontology model from a file or any other source
        //Model model = FileManager.get().loadModel("file:///Users/Jia%20Xian%20Wong/Desktop/3162/MAVENJENA/doid(newest).owl");

        //creating queries for getting the IRI of all input symptoms
        String[] symptomIRIQuery = new String[symptoms.length];
        for (int i = 0; i < symptoms.length; i++ ) {
            //get IRI of symptoms
            String symptomIRI = "";

            //queries for getting the symptom IRI
            String queryIRI = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?class\n" +
                    "WHERE {\n" +
                    "  ?class rdfs:label \"" + symptoms[i] + "\".\n" +
                    "}";

            // Create a Query object
            Query iriQuery = QueryFactory.create(queryIRI);

            // Execute the query
            try (QueryExecution qexec = QueryExecutionFactory.create(iriQuery, model)) {
                ResultSet results = qexec.execSelect();

                // Process the results
                while (results.hasNext()) {
                    QuerySolution solution = results.nextSolution();
                    String classIRI = solution.getResource("class").getURI();
                    System.out.println("Class IRI: " + classIRI);
                    symptomIRIQuery[i] = classIRI;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String symptomQuery = "";

        //process symptoms IRI list into valid query string
        //symptoms = {"http://purl.obolibrary.org/obo/SYMP_0000537", "http://purl.obolibrary.org/obo/SYMP_0019153", "http://purl.obolibrary.org/obo/SYMP_0019177", "http://purl.obolibrary.org/obo/SYMP_0000539"};
        for (int i = 0; i < symptomIRIQuery.length; i++ ){

            symptomQuery += "?disease rdfs:subClassOf [ " +
                    "    rdf:type owl:Restriction ; " +
                    "    owl:onProperty do:RO_0002452 ; " +
                    "    owl:someValuesFrom do:"+ symptomIRIQuery[i].substring(31) +
                    "] . ";

        }


        //query to get the disease that have all the input symptoms
        String queryString =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "PREFIX do: <http://purl.obolibrary.org/obo/> " +
                        "SELECT ?disease " +
                        "WHERE { " +
//                        "?disease rdf:type owl:Class . " +
//                        "?disease rdfs:subClassOf [ " +
//                        "    rdf:type owl:Restriction ; " +
//                        "    owl:onProperty do:RO_0002452 ; " +
//                        //"    owl:someValuesFrom do:SYMP_0000407 " +
//                        //"    owl:someValuesFrom do:" + symptomIRI +
//                        "    owl:someValuesFrom do:SYMP_0000537 " +
//                        "] . " +
//
//                        "?disease rdfs:subClassOf [ " +
//                        "    rdf:type owl:Restriction ; " +
//                        "    owl:onProperty do:RO_0002452 ; " +
//                        "    owl:someValuesFrom do:SYMP_0000730 " +
//                        "] . " +
                        symptomQuery +
                        "}";

        // Create the query object
        Query query = QueryFactory.create(queryString);

        // Execute the query
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            //process the results
            while (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                RDFNode disease = solution.get("disease");
                System.out.println("Disease IRI: " + disease);

                Resource classResource = model.getResource(disease.toString());
                Statement labelStatement = classResource.getProperty(RDFS.label);

                if (labelStatement != null) {
                    String label = labelStatement.getString();
                    System.out.println("Disease: " + label);
                } else {
                    System.out.println("Label not found for the class");
                }
            }
        }

    }
}
