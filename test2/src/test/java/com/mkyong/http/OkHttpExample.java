package com.mkyong.http;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class OkHttpExample {

    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) throws Exception {

        OkHttpExample obj = new OkHttpExample();

        //System.out.println("Testing 1 - Send Http POST request");
        //obj.sendPost("Based on an agreement that was reached in 1966 between Iran and Romania to establish a tractor manufacturing company in Iran, the company was created in Tabriz in 1968. The first goal of the company was to manufacture 10,000 units tractors of 45-65 horsepower engines with single and double differential gearboxes. In 1976 Massey Ferguson started to assemble tractors in the company with the rate of 13000 units for each year. At the moment the production capacity has been increased up to 30000 units for each year.[1] On 1987 the factory started to increase its foundry capacity to be able to produce casting products for different industries. Nowadays it has the largest foundry capacity among middle east. In 1990s The factory started to produce small trucks and vans behind its main products. ", "SuperSecretKey");

        System.out.println("Testing 2 - Send Http GET request");
        obj.sendGet("51", "SuperSecretKey");

    }

    private void sendGet(String msgNum, String key) throws Exception {
        String[] lines = new String[1000];

        Request request = new Request.Builder()
                .url("https://eph.nopesled.com/messages")
                .addHeader("custom-key", "mkyong")  // add request headers
                .addHeader("User-Agent", "OkHttp Bot")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body

            String thing =  response.body().string();

            //System.out.println(thing);

            Document doc = Jsoup.parse(thing);

            Elements content = doc.getElementsByTag("td");

            for (Element input : content){
                if (input.text().contains("=")) {
                    String encryptmessage = input.text().replaceAll("\\s+", "");
                    System.out.println(AES.decrypt(encryptmessage, key));
                }
            }
        }

    }

    private void sendPost(String message, String key) throws Exception {
        String encryptedMessage = AES.encrypt(message, key) ;
        // form parameters
        RequestBody formBody = new FormBody.Builder()
                .add("message[title]", "here is the test message")
                .add("message[text]", encryptedMessage )
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.2:3000/messages")
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
