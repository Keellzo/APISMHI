import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Hello world!");

        fetchSMHI();
    }

    static void fetchSMHI() throws IOException, ParseException {
        //Saving URL to API
        URL url = new URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/13.013801/lat/55.609112/data.json");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.connect();

        if(conn.getResponseCode() == 200){
            System.out.println("Koppling lyckas");

            // Creating stringBuilder and scan object
            StringBuilder stringData = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            // Building string with data from API
            while (scanner.hasNext()){
                stringData.append(scanner.nextLine());
            }
            scanner.close();

            // Parse JSON data
            JSONParser parser = new JSONParser();
            JSONObject smhiData = (JSONObject) parser.parse(stringData.toString());

            //Extract the required fields
            JSONArray timeSeriesArray = (JSONArray) smhiData.get("timeSeries");
            JSONObject currentTimeseries = (JSONObject) timeSeriesArray.get(1);
            JSONArray parametersArray = (JSONArray) currentTimeseries.get("parameters");
            JSONObject temperatureObject = (JSONObject) parametersArray.get(10);
            JSONObject windSpeedObject = (JSONObject) parametersArray.get(14);

            double currentTemperature = Double.parseDouble(((JSONArray) temperatureObject.get("values")).get(0).toString());
            double currentWindSpeed = Double.parseDouble(((JSONArray) windSpeedObject.get("values")).get(0).toString());
            String currentTime = currentTimeseries.get("validTime").toString();

            System.out.println("Current time: " + currentTime);
            System.out.println("Current temperature: " + currentTemperature + " C");
            System.out.println("Current wind speed: " + currentWindSpeed + " m/s");

        }
        else {
            System.out.println("HTTP error: " + conn.getResponseCode());
        }

    }

}