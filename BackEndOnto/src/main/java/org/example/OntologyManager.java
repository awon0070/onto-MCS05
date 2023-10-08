package org.example;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.List;


public class OntologyManager {
    OntModel model;
    public OntologyManager() {

        // Create an ontology model
        this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Load the OntologyManager ontology (which includes imports)
        String ontologyFilePath = "doid.owl";
        model.read(ontologyFilePath);



        //Querying
        //ankyrin-B-related cardiac arrhythmia
        //findSymptoms.OntologyManager(model);

        //syncope, dyspnea


        System.out.println("END");
    }

    public List<String[]> findDisease(String Info){

       return findDisease.main(this.model,Info);
    }

    public List<String[]> findSymptom(String Info){

        return findSymptoms.main(this.model,Info);
    }



}



