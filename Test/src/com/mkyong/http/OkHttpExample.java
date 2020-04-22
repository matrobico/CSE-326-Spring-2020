package com.mkyong.http;

/**
 * To-Do
 * Code that can make a GET request for messages
 * Code that can make a POST request for messages
 * Modify current code to more object oriented and structured
 */
// Registration is not yet working

import okhttp3.*;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OkHttpExample {

    String url = "http://127.0.0.1:3000";

    private final OkHttpClient httpClient = new OkHttpClient();
    //public String authToken;

    public static void main(String[] args) throws Exception {
        OkHttpExample test = new OkHttpExample();
        String authToken = test.login("Shad", "asdfasdf");
        test.createGroup("test", "asdfasdf", authToken);
    }

    public List<String> getMessages(String key, String authToken) throws Exception {
        String[] lines = new String[1000];
        //System.out.print(authToken);
        Request request = new Request.Builder()
                .addHeader("Authorization", authToken)
                .url(url + "/groups/4/messages")
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            String jsonString =  response.body().string();
            List<String> messageList = new ArrayList<>();

            System.out.println(jsonString);

            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++){
                String username = json.getJSONObject(i).getString("title");
                String message = json.getJSONObject(i).getString("text");
                messageList.add(username + AES.decrypt(message, key));
            }


//            Document doc = Jsoup.parse(thing);
//            Elements content = doc.getElementsByTag("b");
//            List<String> stringList = new ArrayList<>();
//
//            for (Element input : content){
//                String poster = input.text().split(":")[0];
//                if (input.text().contains("=")) {
//                    System.out.println("This is the input: " + input);
//                    String encryptmessage = input.text().split(":")[1].replaceAll("\\s+", "");
//                    System.out.println(poster + ": "+ AES.decrypt(encryptmessage, key));
//                    stringList.add(poster + ": " + AES.decrypt(encryptmessage, key));
//                }
//            }
            return messageList;
        }

    }

    public void sendMessage(String message, String key, String authToken, String user) throws Exception {
        String encryptedMessage = AES.encrypt(message, key) ;
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", user + ": ")
                .add("message[text]", encryptedMessage )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/4/messages")
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
                .url(url + "/authenticate")
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
                .url(url + "/user")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String thing =  response.body().string();

            System.out.println(thing);
        }
    }

    public void createGroup(String groupName, String password, String authToken) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("group[title]", groupName)
                .add("group[password]", password )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups")
                .addHeader("Authorization", authToken)
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            //System.out.println(thing);
        }
    }
}

