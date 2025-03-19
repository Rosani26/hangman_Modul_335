package com.example.hangman355;

import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiService {
    private static final String BASE_URL = "https://random-word-api.vercel.app/api?words=1";

    public static void fetchWordFromApi(OnWordFetchedListener listener) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(BASE_URL).build();

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : null;

                if (responseBody != null && !responseBody.isEmpty()) {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    String randomWord = jsonArray.getString(0).toUpperCase();
                    listener.onWordFetched(randomWord);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public interface OnWordFetchedListener {
        void onWordFetched(String word);
    }
}