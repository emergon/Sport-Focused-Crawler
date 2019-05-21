/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawling;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import wekaClassifier.MyFilteredClassifier;

/**
 *
 * @author anastasios
 */
public class Robot {

    // Fields
    private final int MAX_PAGES_TO_SEARCH = 5000; // For unfocused crawling
    private static final int MAX_DEPTH_FOR_SEARCH = 3;
    private Set<String> pagesVisited = new HashSet<String>();
    private LinkedList<RetrievedLink> pagesToVisit = new LinkedList<RetrievedLink>();
    public static Set<String> relevantPages = new HashSet<String>();
    public static Set<String> nonRelevantPages = new HashSet<String>();
    private RetrievedLink retlink;

    /**
     * Returns the next URL to visit (in the order that they were found). We
     * also do a check to make sure this method doesn't return a URL that has
     * already been visited.
     *
     * @return
     */
    private RetrievedLink nextUrl() {
        RetrievedLink nextUrlToProcess = null;

        do {
            System.out.println("pagesToVisit size + nextURL -->> " + pagesToVisit.size());
            if (pagesToVisit.size() > 0) {
                nextUrlToProcess = pagesToVisit.removeFirst(); //--------- FOR DFS
            } else {
                System.out.println("NO MORE PAGES TO CRAWL");
                return null;
            }
        } while (pagesVisited.contains(nextUrlToProcess.getURL()) || nextUrlToProcess.getDepth() > 3);
        pagesVisited.add(nextUrlToProcess.getURL());
        return nextUrlToProcess;
    }

    /**
     * Our main launching point for the Robot's functionality. Internally it
     * creates robot legs that make an HTTP request and parse the response (the
     * web page).
     *
     * @param url - The starting point of the robot
     * @param classifier
     */
    public void search(String url, MyFilteredClassifier classifier) {
        System.out.println("******************PagesVisited size = " + pagesVisited.size());
        while (pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
            String currentUrl = "";
            int currentUrlDepth = 0;
            RobotLeg leg = new RobotLeg();
            if (pagesToVisit.isEmpty()) {
                System.out.println("pagesToVisit is empty");
                currentUrl = url;
                pagesVisited.add(url);
                leg.crawl(currentUrl, 1);
            } else {

                System.out.println("pagesToVisit is NOT  empty");
                retlink = nextUrl();
                if (retlink == null) // shows no more URL to crawl
                {
                    break;
                }

                currentUrl = retlink.getURL();
                currentUrlDepth = retlink.getDepth();
                System.out.println("current url = " + currentUrl + "  DEPTH :: " + currentUrlDepth);
                //TimeUnit.SECONDS.sleep(1);  // To maintain 1 sec politeness before requesting the serverS
                leg.crawl(currentUrl, currentUrlDepth);

            }
            boolean success = leg.searchForCategory(classifier, "sport");
            if (success || pagesToVisit.isEmpty()) // the OR part ensures that even if category is not found on seed page, the links from the seed page are traversed
            {
                if (success) {
                    System.out.println(String.format("**Success** Category %s found at %s", RobotTest.kathgoria, currentUrl));
                    relevantPages.add(currentUrl);
                }

                if (currentUrlDepth <= MAX_DEPTH_FOR_SEARCH) {
                    //int count = 0;
                    //System.out.println("Pages to visit are "+ pagesToVisit.size());
                    for (RetrievedLink link : leg.getLinksOfInterest()) // ----- FOR  DFS
                    {
                        //count++;
                        pagesToVisit.addFirst(link);
                    }
                    //System.out.println("Added "+count+" pages to pages to visit. Now they are "+ pagesToVisit.size());
                }
            } else {
                nonRelevantPages.add(currentUrl);
            }
            System.out.println("pagesVisited :: " + pagesVisited.size());
            System.out.println("Total Relevant Pages Found :: " + relevantPages.size());
            System.out.println("Pages To Visit  :: " + pagesToVisit.size());

        }
        System.out.println("\n**Done** Visited " + pagesVisited.size() + " web page(s)");
        for (String linkss : relevantPages) {
            System.out.println("--->>> " + linkss);

        }

    }

    public Set<String> getAllRelevantLinks() {
        return relevantPages;
    }

    public Set<String> getAllNonRelevantLinks() {
        return nonRelevantPages;
    }
}
