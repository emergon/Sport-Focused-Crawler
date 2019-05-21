/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawling;


import wekaClassifier.MyFilteredClassifier;

/**
 *
 * @author anastasios
 */
public class RobotTest {

    /**
     * This is our test. It creates a spider (which creates spider legs) and
     * crawls the web.
     *
     */
    public static String kathgoria;
    
    public static void main(String[] args) {
        MyFilteredClassifier classifier = new MyFilteredClassifier();
        //MyFilteredLearner learner = new MyFilteredLearner();
        //learner.trainAndSaveClassifier("/home2/tasos/Documents/temp/categoryFile.arff", "bbcClassifier");
        classifier.loadModel("bbcClassifier");

        Robot robot = new Robot();
        
        robot.search("https://en.wikipedia.org/wiki/Barcelona", classifier);
        
        TextWriter obj = new TextWriter(robot);
        obj.writeToTextFile();

    }
    
}
