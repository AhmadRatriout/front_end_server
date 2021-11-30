import org.apache.log4j.BasicConfigurator;

import org.json.JSONArray;
import org.json.JSONObject;
import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class frontendServer {
    public static void main(String[] args) {
        BasicConfigurator.configure();

        get("/search/:value", (req, res) ->{       //here we take the value then send it in the URL to the catalog server
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.250:4567/search/" + topic);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/json");

            // here is the returned response from the catalog server for the search request 

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage()); 
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            
// TO MAKE A NEW JSON FILE OR JSON ARRAY OF OBJECTS
// this JSON File or JSON ARRAY are used for the caching part later

            JSONArray booksList= new JSONArray();
            sb.deleteCharAt(0);
            String finalString = sb.toString();
            finalString= finalString.replace("]", "");
            finalString= finalString.replace("{", "");
            finalString = finalString.trim();
            System.out.println(finalString);

            String[] objects = finalString.split("},");
            String[] contentObject;
            for(int i=0; i<objects.length; i++) {
            	objects[i]= objects[i].replace("}", "");
            	System.out.println(objects[i]);
            	// Take the content of the object
            	contentObject= objects[i].split(",");
            	JSONObject temporary = new JSONObject();
            	for(int j=0; j<contentObject.length; j++) {
            		
            		String[] singleContent = contentObject[j].split(":");
            		System.out.println(singleContent[0]+ "=>" +singleContent[1]);
            		temporary.put(singleContent[0].replace("\"", ""), singleContent[1].replace("\"", ""));
            		
            	}
            	URL u= new URL("http://localhost:4567/info/"+temporary.get("id").toString().replaceAll(" ", "%20"));
            	temporary.put("Information", u);
            	booksList.put(temporary);
            }
            return booksList;
        });
        
        //here is the info request from the front end to the catalog server

        get("/info/:value", (req, res) ->{
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.250:4567/info/" + topic); // here we add the stored value in topic to the url to be sent inside the request 
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/json");

            //here is the response message from the catalog server to the front end server

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            
// TO MAKE A NEW JSON FILE OR JSON ARRAY OF OBJECTS
// this JSON File or JSON ARRAY are used for the caching part later

            JSONArray booksList= new JSONArray();
            sb.deleteCharAt(0);
            String finalString = sb.toString();
            finalString= finalString.replace("]", "");
            finalString= finalString.replace("{", "");
            finalString = finalString.trim();
            System.out.println(finalString);

            String[] objects = finalString.split("},");
            String[] contentObject;
            for(int i=0; i<objects.length; i++) {
            	objects[i]= objects[i].replace("}", "");
            	System.out.println(objects[i]);
            	// Take the content of the object
            	contentObject= objects[i].split(",");
            	JSONObject temporary = new JSONObject();
            	for(int j=0; j<contentObject.length; j++) {
            		
            		String[] singleContent = contentObject[j].split(":");
            		System.out.println(singleContent[0]+ "=>" +singleContent[1]);
            		temporary.put(singleContent[0].replace("\"", ""), singleContent[1].replace("\"", ""));
            		
            	}
            	URL u= new URL("http://localhost:4567/purchase/"+topic);
            	temporary.put("Buy", u);
            	booksList.put(temporary);
            }
            return booksList;
        });
        
        //here is the purchase request from the front end server to the order server
        get("/purchase/:value", (req, res) ->{
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.233:4567/purchase/" + topic); // we add the stored value in the topic to the url that we are going to send with the request 
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST"); 
            http.setRequestProperty("Accept", "application/json");

         // here is the response message from the order server to the front end server 

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage()); 
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
        	return sb.toString(); 
        });
        
        
        
    }
}