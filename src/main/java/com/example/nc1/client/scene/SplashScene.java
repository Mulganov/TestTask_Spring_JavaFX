package com.example.nc1.client.scene;

import com.example.nc1.StatusApplication;
import com.example.nc1.Var;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class SplashScene extends BaseScene {

    private String statusText;
    private final Label statusView;

    private final Var.OnEdit<StatusApplication.Status> onEdit;

    public SplashScene(float width, float height) {
        super(new BorderPane(), width, height);

        BorderPane root = (BorderPane) getRoot();

        {
            BorderPane pane = new BorderPane();

            {
                ProgressBar progressBar = new ProgressBar();

                progressBar.setMaxWidth(width / 2f);
                progressBar.setMinWidth(width / 2f);
                progressBar.setTranslateY(-200);

                pane.setCenter(progressBar);
            }

            root.setBottom(pane);
        }

        {
            BorderPane pane = new BorderPane();

            {
                Label text = new Label("Loading");

                text.setFont(new Font(100));

                pane.setCenter(text);
            }

            root.setTop(pane);
        }

        {
            statusView = new Label("Загрузка");

            statusView.setFont(new Font(30));
            statusView.setTranslateY(-100);

            root.setCenter(statusView);

            onEdit = new Var.OnEdit<StatusApplication.Status>() {
                @Override
                public void onEdit(ArrayList<StatusApplication.Status> values) {
                    updateStatus(values.get(0));
                }
            };

            // Если меняется статус - меняется и текст
            StatusApplication.STATUS.addObserver(onEdit);

            // Это что бы показать текущий статус
            updateStatus(StatusApplication.STATUS.getValue());
        }
    }

    @Override
    public void onDestroy() {
        // После удаление Сцены - нужно удалить наблюдателя
        StatusApplication.STATUS.removeObserver(onEdit);
    }

    private void updateStatus(StatusApplication.Status status) {
        switch (status){
            case NON_STATUS -> statusText = "Программа ничего не делает";
            case DOWNLOAD_NEWS_BEGIN -> statusText = "Скачивания новостей";
            case INIT_SERVER_BEGIN -> statusText = "Инит сервера";
            case SCAN_NEWS_BEGIN -> statusText = "Сканируем новостной сайт";

            default -> statusText = "Статуса нету(Ждем запуска Парсера)";
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                statusView.setText(statusText);
            }
        });

    }


}
