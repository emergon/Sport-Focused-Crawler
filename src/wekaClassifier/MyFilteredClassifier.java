/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekaClassifier;

import java.io.*;
import java.util.ArrayList;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;

/**
 * This class implements a simple text classifier in Java using WEKA. It loads a
 * file with the text to classify, and the model that has been learnt with
 * MyFilteredLearner.java.
 *
 * @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
 * @see MyFilteredLearner
 */
/**
 *
 * @author anastasios
 */
public class MyFilteredClassifier {

    /**
     * String that stores the text to classify
     */
    String text;
    /**
     * Object that stores the instance.
     */
    Instances instances;
    /**
     * Object that stores the classifier.
     */
    FilteredClassifier classifier;

    /**
     * This method loads the text to be classified.
     *
     * @param fileName The name of the file that stores the text.
     */
    public void load(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
            System.out.println("===== Loaded text data: " + fileName + " =====");
            reader.close();
            System.out.println(text);
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }
    
    public void setText(String text){
        this.text = text;
    }

    /**
     * This method loads the model to be used as classifier.
     *
     * @param fileName The name of the file that stores the text.
     */
    public void loadModel(String fileName) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
            classifier = (FilteredClassifier) tmp;
            in.close();
            System.out.println("===== Loaded model: " + fileName + " =====");
        } catch (Exception e) {
            // Given the cast, a ClassNotFoundException must be caught along with the IOException
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    /**
     * This method creates the instance to be classified, from the text that has
     * been read.
     */
    public void makeInstance() {
        // Create the attributes, class and text
        ArrayList<String> fvNominalVal = new ArrayList(5);
        fvNominalVal.add("business");
        fvNominalVal.add("entertainment");
        fvNominalVal.add("politics");
        fvNominalVal.add("sport");
        fvNominalVal.add("tech");
        Attribute attribute1 = new Attribute("class", fvNominalVal);
        Attribute attribute2 = new Attribute("text", (ArrayList) null);
        // Create list of instances with one element
        ArrayList<Attribute> fvWekaAttributes = new ArrayList(2);
        fvWekaAttributes.add(attribute1);
        fvWekaAttributes.add(attribute2);
        instances = new Instances("Test relation", fvWekaAttributes, 1);
        // Set class index
        instances.setClassIndex(0);
        // Create and add the instance
        DenseInstance instance = new DenseInstance(2);
        instance.setValue(attribute2, text);
        // Another way to do it:
        // instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
        instances.add(instance);
        //System.out.println("===== Instance created with reference dataset =====");
        //System.out.println(instances);
    }

    
    public String classify() {
        String result;
        try {
            double pred = classifier.classifyInstance(instances.instance(0));
            //System.out.println("===== Classified instance =====");
            result = instances.classAttribute().value((int) pred);
            //System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
            System.out.println("Problem found when classifying the text");
        }
        return result;
    }
    
    public String loadTextAndClassify(String txtToClassify){
        setText(txtToClassify);
        makeInstance();
        String category = classify();
        return category;
    }

    public String loadModelAndClassify(String txtToClassify, String classifierModel) {
        load(txtToClassify);
        loadModel(classifierModel);
        makeInstance();
        String category = classify();
        return category;
    }
}
