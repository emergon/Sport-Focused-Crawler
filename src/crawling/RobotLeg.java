/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawling;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wekaClassifier.MyFilteredClassifier;

/**
 *
 * @author anastasios
 */
public class RobotLeg {

    private List<RetrievedLink> linksOfInterest = new LinkedList<RetrievedLink>(); // Just a list of URLs
    private Document htmlDocument; // This is our web page, or in other words, our document
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT
            = "Mozilla/5.0 (robotaki linux)";

    /**
     * This performs all the work. It makes an HTTP request, checks the
     * response, and then gathers up all the links on the page. Perform a
     * searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     * @param depth
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url, int depth) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document html = connection.get();
            this.htmlDocument = html;
            
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = html.select("a[href]");
            //System.out.println("Found (" + linksOnPage.size() + ") links");
            //int count = 0;
            //System.out.println("linksOfInterest are "+ linksOfInterest.size());
            
            for (Element link : linksOnPage) {
                RetrievedLink retrievedLink = new RetrievedLink();
                String absURL = link.absUrl("href");
                //From all the links of the page we keep the links that pass the below checks
                if (absURL.startsWith("https://en.wikipedia.org/wiki/")
                        && !(absURL.equals("https://en.wikipedia.org/wiki/Main_Page"))
                        && absURL.lastIndexOf(":") == 5
                        && !absURL.contains("#")
                        && !absURL.equals(url)) {
                    retrievedLink.setURL(absURL);
                    retrievedLink.setDepth(depth + 1);
                    //count++;
                    linksOfInterest.add(retrievedLink);
                }
            }
            //System.out.println("Added "+ count +" links. Now LinksOfInterest are " +linksOfInterest.size());

            return true;
        } catch (IOException ioe) {
            System.out.println("Not successful in the HTTP request");
            return false;
        }
    }

    /**
     * Performs a search on the body of on the HTML document that is retrieved.
     * This method should only be called after a successful crawl.
     *
     * @param classifier
     * @param category
     * @return whether or not the word was found
     */
    public boolean searchForCategory(MyFilteredClassifier classifier, String category)       
    {
        Boolean categoryFound;
        
        // Defensive coding. This method should only be used after a successful crawl.
        if (this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Looking for the category of the html page...");
        String bodyText = this.htmlDocument.body().text();
        
        //System.out.println("bodyyyy ---- >>");
        //System.out.println(bodyText);
        
        RobotTest.kathgoria = classifier.loadTextAndClassify(bodyText);
        System.out.println("This HTML page has a category of " + RobotTest.kathgoria.toUpperCase());
        if (RobotTest.kathgoria.equals(category)) {
            categoryFound = true;
        } else {
            categoryFound = false;
        }
        return categoryFound;
    }

    public List<RetrievedLink> getLinksOfInterest() {
        return this.linksOfInterest;
    }
}
