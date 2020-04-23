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

    String url = "http://127.0.0.2:3000";

    String user = "";

    private final OkHttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        OkHttpExample test = new OkHttpExample();
        RSAUtil keygen = new RSAUtil();
        keygen.keyCheck();
        test.user = "Matthew";
        //test.registerUser("Mattew", "asdfasdf", "asdfasdf");

        String authToken = test.login(test.user, "asdfasdf");
        //test.createGroup("test", "asdfasdf", authToken);

        //test.joinGroup("3", "asdfasdf", authToken);
        //test.listUsers("3", authToken);

        //test.sendMessage("I am testing the new system", keygen.getPublicKey(), authToken, "Shad");
        test.getMessages(keygen.getPrivateKey(), authToken);
        //test.createGroup("test", "asdfasdf", authToken);
    }

    /**
     *
     * @param key The key to use to decrypt the message
     * @param authToken The session authentication token
     * @return A list of messages
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public List<String> getMessages(String key, String authToken, int groupNum) throws Exception {
        Request request = new Request.Builder()
                .addHeader("Authorization", authToken)
                .url(url + "/groups/" + groupNum +"/messages")
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            String jsonString =  response.body().string();
            List<String> messageList = new ArrayList<>();

            //System.out.println(jsonString);

            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++){
                String sender = json.getJSONObject(i).getString("title");
                String reciever = json.getJSONObject(i).getString("recipient");
                String message = json.getJSONObject(i).getString("text");
                messageList.add("From:" + sender + "To: " + reciever + RSAUtil.decrypt(message, key));
            }
            System.out.println(messageList);
            return messageList;
        }

    }

    /**
     *
     * @param message The message to be sent
     * @param key Private key to encrypt the message
     * @param authToken The session authentication token
     * @param recipient The user this message is intended for (will be needed to
     *                  determine to determine which key to use to encrypt)
     * @param groupNum The group this message is intended for
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void sendMessage(String message, String key, String authToken, String recipient, int groupNum) throws Exception {
        String encryptedMessage = RSAUtil.encrypt(message, key);
        //System.out.println(encryptedMessage);
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", user + ": ")
                .add("message[recipient]",  recipient + ": ")
                .add("message[key]", "false")
                .add("message[text]", encryptedMessage )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupNum + "/messages")
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
            return authenticationToken;
        }
    }

    /**
     * Creates a user in the database with credentials specified by user
     * @param username The username of user to be created
     * @param password The Password of user to be created
     * @param passwordConfirmation Must match the password
     * @throws Exception Throws exception when the response to packet is not a success
     */
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

    /**
     * Creates a group with name and password specified by user
     * @param groupName The name of the group to be created
     * @param password The password of the group to be created
     * @param authToken The session authentication token
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void createGroup(String groupName, String password, String authToken) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("title", groupName)
                .add("password", password )
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

    /**
     * Lists all of the users in a group
     * @param groupID the id of the group that you want to list members of (must be in group to list users)
     * @param authToken Session authentication token
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void listUsers(String groupID, String authToken) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupID + "/users" )
                .addHeader("Authorization", authToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString =  response.body().string();
            List<String> userList = new ArrayList<>();

            //System.out.println(jsonString);

            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++) {
                String user = json.getJSONObject(i).getString("name");
                userList.add(user);
            }
            System.out.println(userList);
        }
    }

    /**
     * Joins a group
     * @param groupID The id of the group that you want join
     * @param password The password of the group
     * @param authToken Session authentication token
     * @param publicKey The user's public key to be posted to the group upon joining
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void joinGroup(String groupID, String password, String authToken, String publicKey) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("password", password )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupID + "/adduser")
                .addHeader("Authorization", authToken)
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String thing =  response.body().string();

            // Get response body
            System.out.println(thing);
        }
        postPublicKey(publicKey, authToken, );
    }

    /**
     * Sends the public key of the user as a message to a group (works much like
     * sendMessage() but does not encrypt the payload)
     * @param publicKey The public key of the user to be posted
     * @param authToken The session authentication token
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void postPublicKey(String publicKey, String authToken, int groupNum) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", user + ": ")
                .add("message[recipient]",  "all: ")
                .add("message[key]", "true")
                .add("message[text]", publicKey )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupNum + "/messages")
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

}

