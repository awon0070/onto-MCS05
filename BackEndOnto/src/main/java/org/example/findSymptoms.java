package org.example;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDFS;

import java.util.Scanner;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;

import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class findSymptoms {
    public static List<String[]> main(OntModel model,String disease) {
        List<String[]> output = new ArrayList<String[]>();

        //get user input



        // Load the OWL file into an RDF model
        //Model model = ModelFactory.createDefaultModel();
        //FileManager.get().readModel(model, "file:///Users/Jia%20Xian%20Wong/Desktop/3162/MAVENJENA/doid(newest).owl");

//        // Create an ontology model
//        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
//
//        // Load the main ontology (which includes imports)
//        String ontologyFilePath = "file:///Users/Jia%20Xian%20Wong/Desktop/3162/MAVENJENA/doid(newest).owl";
//        model.read(ontologyFilePath);


        // Define the SPARQL query to retrieve the class IRI by its label
        //disease = "ankyrin-B-related cardiac arrhythmia";
        String diseaseIRI = "";
        //String queryIRI = "";
        //query to get disease IRI
//        try{
//            queryIRI = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
//                    "SELECT ?class\n" +
//                    "WHERE {\n" +
//                    "  ?class rdfs:label \"" + disease + "\".\n" +
//                    "}";
//        }
//        catch (Exception e){
//                System.out.println("Invalid disease");
//        }
        String queryIRI = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?class\n" +
                "WHERE {\n" +
                "  ?class rdfs:label \"" + disease + "\".\n" +
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
                diseaseIRI = classIRI;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Make diseaseIRI into valid input
        String diseaseQuery = "  <" + diseaseIRI + "> rdf:type owl:Class ;\n";
        //String disease_IRI = "  <http://purl.obolibrary.org/obo/DOID_0040095> rdf:type owl:Class ;\n";

        //query to get symptoms of input disease
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT ?relatedClass\n" +
                "WHERE {\n" +

                diseaseQuery +

                "    rdfs:subClassOf ?restriction .\n" +
                "  ?restriction rdf:type owl:Restriction ;\n" +
                "    owl:onProperty <http://purl.obolibrary.org/obo/RO_0002452> ;\n" +
                "    (owl:someValuesFrom|rdf:resource) ?relatedClass .\n" +
                "}";

        // Create a Query object
        Query query = QueryFactory.create(queryString);

        // Execute the query
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            // Process the results
            while (results.hasNext()) {
                String[] symptomInfo = new String[2];

                QuerySolution solution = results.nextSolution();
                String relatedClass = solution.get("relatedClass").toString();

                symptomInfo[1] = relatedClass;
                //System.out.println("Symptom IRI: " + relatedClass);

                Resource classResource = model.getResource(relatedClass);
                Statement labelStatement = classResource.getProperty(RDFS.label);

                if (labelStatement != null) {
                    String label = labelStatement.getString();
                    symptomInfo[0] = label;
                    //System.out.println("Symptom: " + label);
                } else {
                    System.out.println("Label not found for the class with IRI: " + relatedClass);
                }
                output.add(symptomInfo);
                System.out.println(symptomInfo[0]);
                System.out.println(symptomInfo[1]);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        return output;
    }
}

