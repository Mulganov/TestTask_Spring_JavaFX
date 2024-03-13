package com.example.nc1.client.tools;

import com.example.nc1.StatusApplication;
import com.example.nc1.client.Scenes;
import com.example.nc1.client.model.News;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadTools {
    public static void downloadNews(OnDownloadReturn onDownloadReturn) {
        StatusApplication.setStatus(StatusApplication.Status.DOWNLOAD_NEWS_BEGIN);

        URL url = null;
        try {
            url = new URL("http://localhost:8080/news");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String text = reader.readLine();

            while (text != null){
                sb.append(text).append("\n");
                text = reader.readLine();
            }

            Type listType = new TypeToken<ArrayList<News>>(){}.getType();
            List<News> list = new Gson().fromJson(sb.toString(), listType);

            onDownloadReturn.onSuccess(list);

        } catch (Exception e) {
            onDownloadReturn.onError(e);
        }

        onDownloadReturn.onFinish();
    }

    public interface OnDownloadReturn{
        void onSuccess(List<News> newsList);
        void onError(Exception exception);
        void onFinish();
    }
}
