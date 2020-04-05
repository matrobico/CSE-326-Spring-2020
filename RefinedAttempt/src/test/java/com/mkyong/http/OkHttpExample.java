package com.mkyong.http;

/**
 * To-Do
 * Code that can make a GET request for messages
 * Code that can make a POST request for messages
 * Modify current code to more object oriented and structured
 */
// Registration is not yet working
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OkHttpExample {

    private final OkHttpClient httpClient = new OkHttpClient();
    //public String authToken;

    public static void main(String[] args) throws Exception {
        String authToken = "";
        OkHttpExample obj = new OkHttpExample();

        System.out.println("Register User");
        obj.registerUser("user3", "asdfasdf", "asdfasdf");

        System.out.println("Logging in");
        authToken = obj.login("user3", "asdfasdf");

        System.out.println(authToken);

        //System.out.println("Testing 1 - Send Http POST request");
        //obj.sendPost("Based on an agreement that was reached in 1966 between Iran and Romania to establish a tractor manufacturing company in Iran, the company was created in Tabriz in 1968. The first goal of the company was to manufacture 10,000 units tractors of 45-65 horsepower engines with single and double differential gearboxes. In 1976 Massey Ferguson started to assemble tractors in the company with the rate of 13000 units for each year. At the moment the production capacity has been increased up to 30000 units for each year.[1] On 1987 the factory started to increase its foundry capacity to be able to produce casting products for different industries. Nowadays it has the largest foundry capacity among middle east. In 1990s The factory started to produce small trucks and vans behind its main products. ", "SuperSecretKey", authToken);

        System.out.println("Testing 2 - Send Http GET request");
        obj.sendGet("SuperSecretKey", authToken);

    }

    public List<String> sendGet(String key, String authToken) throws Exception {
        String[] lines = new String[1000];
        //System.out.print(authToken);
        Request request = new Request.Builder()
                .addHeader("Authorization", authToken)
                .url("http://127.0.0.1:3000/messages")
                //.url("https://eph.nopesled.com/messages")1
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            String thing =  response.body().string();

            Document doc = Jsoup.parse(thing);

            Elements content = doc.getElementsByTag("b");
            List<String> stringList = new ArrayList<>();

            for (Element input : content){
                String poster = input.text().split(":")[0];
                if (input.text().contains("=")) {
                    String encryptmessage = input.text().split(":")[1].replaceAll("\\s+", "");
                    System.out.println(poster + ": "+ AES.decrypt(encryptmessage, key));
                    stringList.add(AES.decrypt(encryptmessage, key));
                }
            }
            return stringList;
        }

    }

    public void sendPost(String message, String key, String authToken) throws Exception {
        String encryptedMessage = AES.encrypt(message, key) ;
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", "Matthew: ")
                .add("message[text]", encryptedMessage )
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:3000/messages")
                .addHeader("Authorization", authToken)
                .addHeader("User-Agent", "OkHttp Bot")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            //System.out.println(thing);
        }
    }

    /* Method to login. Returns the authentication token if logic was
     * successful, null otherwise
     */
    public String login(String username, String password) throws Exception {
        String authenticationToken = "";
        // form parameters
        //System.out.println(username);
        //System.out.println(password);
        RequestBody formBody = new FormBody.Builder()
                .add("commit", "login")
                .add("name", username)
                .add("password", password )
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:3000/authenticate")
                .addHeader("User-Agent", "OkHttp Bot")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            // The response will not be successful if the login did not work. Returning
            // null here allows the use of a comparison statement in other parts of the
            // program to be able to check if the login was successful or not
            if (!response.isSuccessful()) {
                return null;
                //throw new IOException("Unexpected code " + response);
            }

            String thing =  response.body().string();

            // Get response body
            //System.out.println(thing);

            if (thing.contains("{\"auth_token\":")){
                authenticationToken = thing.split("\"")[3];
            }

            //if (authenticationToken.matches(".*[a-z].*")) {
            //    System.out.println("DO YOU SEE ME 1?");
            //}
            //System.out.println("DO YOU SEE ME 2?");
            return authenticationToken;
        }
    }

    public void registerUser(String username, String password, String passwordConfirmation) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("user[name]", username)
                .add("user[password]", password )
                .add("user[password_confirmation]", passwordConfirmation)
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:3000/user")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String thing =  response.body().string();

            System.out.println(thing);
        }
    }
}

