/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawling;

/**
 *
 * @author anastasios
 */
public class RetrievedLink {
    String URL ;
    int depth;


    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getURL() {
        return URL;
    }

    public int getDepth() {
        return depth;
}
}
