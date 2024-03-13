package com.example.nc1.client;

import com.example.nc1.IApplication;
import com.example.nc1.OnOpenWeb;
import com.example.nc1.StatusApplication;
import com.example.nc1.client.model.News;
import com.example.nc1.client.tools.DownloadTools;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.List;

public class ClientApplication extends Application implements IApplication {
    private final float width = 1920 * 0.6f;
    private final float height = 1080 * 0.6f;
    private static OnCreate onCreate;
    private final OnOpenWeb onOpenWeb = new OnOpenWeb() {
        @Override
        public void onOpenWeb(String url) {
            getHostServices().showDocument(url);
        }
    };


    // Пришлось сделать вот такой костыль, ибо в JavaFX при launch() создается новый класс, и из-за этого
    // могут быть проблемы из-за разных ссылок на класс
    // (в MainApplication один класс хранится, а по факту работает другой)
    public static void create(OnCreate onCreate){
        ClientApplication.onCreate = onCreate;

        new Thread(new Runnable() {
            @Override
            public void run() {
                launch(ClientApplication.class);
            }
        }).start();
    }

    private OnClose onClose;
    private Stage stage;
    private boolean stop = false;

    @Override
    public void onCreate(String[] args) {
        stage.show();
    }

    @Override
    public void onDestroy() {
        Platform.exit();
    }

    @Override
    public void stop() {
        this.stop = true;

        if (onClose != null)
            onClose.onClose();
    }

    @Override
    public void init() {

    }

    @Override
    public void start(Stage stage) {
        stage.setScene(Scenes.SPLASH(width, height));

        this.stage = stage;

        if (onCreate != null)
            onCreate.onCreate(this);
    }

    public void downloadNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadTools.downloadNews(new DownloadTools.OnDownloadReturn() {
                    @Override
                    public void onSuccess(List<News> newsList) {
                        Platform.runLater(() -> {
                            stage.setScene(Scenes.LIST_NEWS(width, height, newsList, onOpenWeb));
                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        exception.printStackTrace();
                    }

                    @Override
                    public void onFinish() {
                        StatusApplication.setStatus(StatusApplication.Status.DOWNLOAD_NEWS_END);
                    }
                });
            }
        }).start();
    }

    public OnClose getOnClose() {
        return onClose;
    }

    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    public boolean isStop() {
        return stop;
    }

    public interface OnCreate {
        void onCreate(ClientApplication application);
    }
}
