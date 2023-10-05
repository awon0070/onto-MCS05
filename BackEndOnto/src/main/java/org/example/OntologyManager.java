package org.example;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;


public class OntologyManager {
    public OntologyManager() {

        // Create an ontology model
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

        // Load the OntologyManager ontology (which includes imports)
        String ontologyFilePath = "doid.owl";
        model.read(ontologyFilePath);



        //Querying
        //ankyrin-B-related cardiac arrhythmia
        //findSymptoms.OntologyManager(model);

        //syncope, dyspnea
        findDisease.main(model);

        System.out.println("END");
    }




}



