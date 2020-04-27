package com.mkyong.http;

import okhttp3.*;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OkHttpExample {

    ArrayList<User> userList = new ArrayList<>();
    ArrayList<Group> groupList = new ArrayList<>();
    String url = "http://127.0.0.1:3000";

    //String user = "";

    private final OkHttpClient httpClient = new OkHttpClient();

    /*
    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }



    public static void main(String[] args) throws Exception {
        OkHttpExample test = new OkHttpExample();
        RSAUtil keygen = new RSAUtil();
        // keyCheck() checks if there are files named "privateKey and publicKey" if there are not it
        // generates new keys and puts them into those files.
        keygen.keyCheck();
        //test.user = "Casey";
        int GroupID = 1;
        //test.registerUser(test.user, "asdfasdf", "asdfasdf");

        //String authToken = test.login(test.user, "asdfasdf");
        //test.createGroup("test", "asdfasdf", authToken);
        //Thread.sleep(2000);
        test.joinGroup(GroupID, "asdfasdf", authToken, keygen.getPublicKey());
        test.listUsers(GroupID, authToken);

        //test.listGroups(authToken);
        test.getMessages(keygen.getPrivateKey(), authToken, GroupID);

        //test.sendMessageToAll("testing a new thing", authToken, GroupID, test);
    }

     */

    /**
     * Retrieves all of the messages from a group
     * @param key The key to use to decrypt the message
     * @param authToken The session authentication token
     * @return A list of messages
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public List<String> getMessages(String key, String authToken, int groupID, String user) throws Exception {
        Request request = new Request.Builder()
                .addHeader("Authorization", authToken)
                .url(url + "/groups/" + groupID +"/messages")
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            String jsonString =  response.body().string();
            List<String> messageList = new ArrayList<>();

            //System.out.println(user + ": ");
            System.out.println("jsonString: " + jsonString);

            JSONArray json = new JSONArray(jsonString);
            userList.clear();

            for (int i = 0; i < json.length(); i++){
                String sender = json.getJSONObject(i).getString("title");
                String reciever = json.getJSONObject(i).getString("recipient");
                boolean keyBool = json.getJSONObject(i).getBoolean("key");
                String message = json.getJSONObject(i).getString("text");

                //System.out.println(sender + reciever + keyBool + message);
                System.out.println("This is the user: " + user);
                if ((reciever.contains(user) || reciever.contains("all: ")) && keyBool == false){
                    messageList.add(sender + RSAUtil.decrypt(message, key));
                } else if (reciever.equals("all: ") && keyBool == true){
                    if (userList.indexOf(new User(sender)) == -1) {
                        //System.out.println("Adding a new User");
                        //System.out.println(message);
                        userList.add(new User(sender, message));
                    }
                    messageList.add(sender + "has joined the chat");
                }
            }
            //System.out.println(messageList);
            return messageList;
        }

    }

    /**
     * Sends a message to a group
     * @param message The message to be sent
     * @param key Private key to encrypt the message
     * @param authToken The session authentication token
     * @param recipient The user this message is intended for (will be needed to
     *                  determine to determine which key to use to encrypt)
     * @param groupID The group this message is intended for
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void sendMessage(String message, String key, String authToken, String recipient, int groupID, String user) throws Exception {
        //System.out.println("This is the key inside sendMessage " + key + "\n");
        String encryptedMessage = RSAUtil.encrypt(message, key);
        //System.out.println(encryptedMessage);
        // form parameters


        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", user + ": ")
                .add("message[recipient]",  recipient)
                .add("message[key]", "false")
                .add("message[text]", encryptedMessage )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupID + "/messages")
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

    public void sendMessageToAll(String message, String authToken, int groupID, OkHttpExample test, String user) {
        //System.out.print(authToken);
        //System.out.println("This is the size of ");
        for (int i = 0; i < userList.size(); i++){
            try {
                System.out.println("UserList: " + userList.get(i).name);
                //System.out.println(userList.get(i).name + ": " + userList.get(i).publicKey);
                test.sendMessage(message, userList.get(i).publicKey, authToken, userList.get(i).name, groupID, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

            //System.out.println(thing);
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
    public List<String> listUsers(int groupID, String authToken) throws Exception {
        // form parameters
        List<String> userList = new ArrayList<>();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupID + "/users" )
                .addHeader("Authorization", authToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString =  response.body().string();
            //System.out.println(jsonString);

            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++) {
                String username = json.getJSONObject(i).getString("name");
                userList.add(username);
            }
            //System.out.println(userList);
        }
        return userList;
    }

    /**
     * Joins a group
     * @param groupID The id of the group that you want join
     * @param password The password of the group
     * @param authToken Session authentication token
     * @param publicKey The user's public key to be posted to the group upon joining
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void joinGroup(int groupID, String password, String authToken, String publicKey, String user) throws Exception {
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
            //System.out.println(thing);
            if (!thing.contains("{\"error\":\"User already added\"}")){
                postPublicKey(publicKey, authToken, groupID, user);
            } else {
                System.out.println("Not Posting My Public Key");
            }

        }
    }

    /**
     * Sends the public key of the user as a message to a group (works much like
     * sendMessage() but does not encrypt the payload)
     * @param publicKey The public key of the user to be posted
     * @param authToken The session authentication token
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void postPublicKey(String publicKey, String authToken, int groupID, String user) throws Exception {
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", user + ": ")
                .add("message[recipient]",  "all: ")
                .add("message[key]", "true")
                .add("message[text]", publicKey )
                .build();

        Request request = new Request.Builder()
                .url(url + "/groups/" + groupID + "/messages")
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


    /**
     * Lists all of the groups a user is a part of
     * @param authToken Session authentication token
     * @throws Exception Throws exception when the response to packet is not a success
     */
    public void listGroups(String authToken) throws Exception {
        // form parameters
        Request request = new Request.Builder()
                .url(url + "/user/1/groups/" )
                .addHeader("Authorization", authToken)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString =  response.body().string();

            //System.out.println(jsonString);

            JSONArray json = new JSONArray(jsonString);

            for (int i = 0; i < json.length(); i++) {
                Group temp = new Group();
                temp.setName(json.getJSONObject(i).getString("title"));
                temp.setId(json.getJSONObject(i).getInt("id"));
                groupList.add(temp);
            }
            //System.out.println(groupList.get(1).id);
        }
    }
}

