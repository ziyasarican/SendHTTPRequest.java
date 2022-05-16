import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;


public class Main {
    public static final String delimiter = ";";

    private static HttpURLConnection connection;
    public static void main(String[] args) throws  IOException {
	// write your code here
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();
        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/albums");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();


            if(status <299) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line=reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
                System.out.println(responseContent);
            }
            File file = new File("convertJson.json");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file,false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(responseContent.toString());
            bufferedWriter.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }

        //Gson formata çevirmek için:
        //Gson gson = new Gson();
        //System.out.println(gson.toJson(responseContent));



        String text = "{\"fileName\": " + String.valueOf(responseContent) + "}";
        System.out.println(text);
        JSONObject output;
        try {
            output = new JSONObject(text);
            JSONArray docs = output.getJSONArray("fileName");
            File file = new File("convertCsv.csv");
            String csv = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csv);
            System.out.println(file);
            System.out.println(csv);
        }
        catch(Exception e) {
            e.printStackTrace();
        }



    }

}
