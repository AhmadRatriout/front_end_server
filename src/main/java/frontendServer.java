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

        get("/search/:value", (req, res) ->{
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.250:4567/search/" + topic);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/json");

            
            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            
// TO MAKE A NEW JSON FILE OR JSON ARRAY OF OBJECTS
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
        
        
        get("/info/:value", (req, res) ->{
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.250:4567/info/" + topic);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/json");

            
            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();
            
// TO MAKE A NEW JSON FILE OR JSON ARRAY OF OBJECTS
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
        
        
        get("/purchase/:value", (req, res) ->{
        	String topic = req.params(":value");
        	topic = topic.replaceAll(" ", "%20");
        	URL url = new URL("http://192.168.1.233:4567/purchase/" + topic);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/json");
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
