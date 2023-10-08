package org.example;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.List;

public class findDisease {
    public static List<String[]> main(OntModel model,String Info) {

        String[] symptoms = Info.split("\\s*,\\s*");
        List<String[]> output = new ArrayList<String[]>();
        //scanner.close();


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
        try {
            for (int i = 0; i < symptomIRIQuery.length; i++) {

                symptomQuery += "?disease rdfs:subClassOf [ " +
                        "    rdf:type owl:Restriction ; " +
                        "    owl:onProperty do:RO_0002452 ; " +
                        "    owl:someValuesFrom do:" + symptomIRIQuery[i].substring(31) +
                        "] . ";

            }
        }catch(Exception e){

            System.out.println("Invalid symptoms");
            //output.add(["Invalid symptoms"]);
            return(output);
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
                String[] diseaseInfo = new String[2];
                QuerySolution solution = results.nextSolution();
                RDFNode disease = solution.get("disease");

                if(disease == null){
                    System.out.println("No diseases with these symptoms");
                    break;
                }
                //System.out.println("Disease IRI: " + disease);
                diseaseInfo[1] = ("Disease IRI: " + disease);

                //output.add("Disease IRI: " + disease);
                //return(output);

                Resource classResource = model.getResource(disease.toString());
                Statement labelStatement = classResource.getProperty(RDFS.label);

                if (labelStatement != null) {
                    String label = labelStatement.getString();
                    //System.out.println("Disease: " + label);
                    diseaseInfo[0] = ("Disease: " + label);
                   // System.out.println(diseaseInfo[0]);
                    //output.add("Disease: " + label);
                    //return (output);
                } else {
                    //System.out.println("Label not found for the class");
                    diseaseInfo[0] = "Label not found for the class";
                    //output.add("Label not found for the class");
                    return(output);
                }
                output.add(diseaseInfo);
                System.out.println(diseaseInfo[0]);
                System.out.println(diseaseInfo[1]);
            }

        }

        System.out.println(output);
        return output;
    }
}
