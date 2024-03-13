package com.example.nc1.client.scene;

import com.example.nc1.OnOpenWeb;
import com.example.nc1.StatusApplication;
import com.example.nc1.Var;
import com.example.nc1.client.Scenes;
import com.example.nc1.client.model.News;
import com.example.nc1.client.tools.DownloadTools;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ListNewsScene extends BaseScene {

    enum TypeNews{
        ALL(0, 1000 * 60 * 60 * 24),
        MORNING(0, 1000 * 60 * 60 * 8),
        DINNER(1000 * 60 * 60 * 8, 1000 * 60 * 60 * 16),
        EVENING(1000 * 60 * 60 * 16, 1000 * 60 * 60 * 24);

        public final long from;
        public final long to;

        TypeNews(long from, long to){
            this.from = from;
            this.to = to;
        }
    }

    private String statusText;
    private Label statusView;
    private TextArea textNews;

    private Var.OnEdit<StatusApplication.Status> onEdit;
    private List<News> newsList;
    private List<News> currentNewsList;
    private int index = 0;

    private final ListView<String> listView;
    private TypeNews typeNews = TypeNews.ALL;

    public ListNewsScene(float width, float height, List<News> newsList, OnOpenWeb onOpenWeb) {
        super(new HBox(), width, height);

        setList(newsList, false);

        HBox root = (HBox) getRoot();

        listView = new ListView<>();

        updateListView(TypeNews.ALL);

        listView.setMaxWidth(width / 2f);
        listView.setMinWidth(width / 2f);
        listView.setPrefSize(width / 2f, height);
        listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selected(observable.getValue().intValue());
            }
        });

        root.getChildren().add(listView);

        {
            BorderPane pane = new BorderPane();

            pane.setMaxWidth(width / 2f);
            pane.setMinWidth(width / 2f);
            pane.setPrefSize(width / 2f, height);

            {
                textNews = new TextArea("й");

                textNews.setWrapText(true);

                pane.setCenter(textNews);
            }

            {
                BorderPane pane1 = new BorderPane();

                {
                    ImageView imageView = null;

                    try {
                        Image image = new Image(new FileInputStream("files/img/arrow_left.png"));
                        imageView = new ImageView(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);

                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            addIndex(-1);
                        }
                    });

                    pane1.setCenter(imageView);
                }

                pane.setLeft(pane1);
            }


            {
                BorderPane pane1 = new BorderPane();

                {
                    ImageView imageView = null;

                    try {
                        Image image = new Image(new FileInputStream("files/img/arrow_left.png"));
                        imageView = new ImageView(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            addIndex(1);
                        }
                    });

                    imageView.setRotate(180);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);


                    pane1.setCenter(imageView);
                }

                pane.setRight(pane1);
            }

            {
                BorderPane paneTop = new BorderPane();

                pane.setMaxWidth(width / 2f);
                pane.setMinWidth(width / 2f);
                pane.setPrefSize(width / 2f, height);

                {
                    statusView = new Label("Статус");

                    statusView.setFont(new Font(20));
                    onEdit = new Var.OnEdit<StatusApplication.Status>() {
                        @Override
                        public void onEdit(ArrayList<StatusApplication.Status> values) {
                            updateStatus(values.get(0));
                        }
                    };

                    // Если меняется статус - меняется и текст
                    StatusApplication.STATUS.addObserverIOThread(onEdit);
                    // Это что бы показать текущий статус
                    updateStatus(StatusApplication.STATUS.getValue());

                    paneTop.setCenter(statusView);
                }
                {
                    ImageView imageView = null;

                    try {
                        Image image = new Image(new FileInputStream("files/img/reload.png"));
                        imageView = new ImageView(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            reload();
                        }
                    });

                    paneTop.setRight(imageView);
                }
                {
                    ImageView imageView = null;

                    try {
                        Image image = new Image(new FileInputStream("files/img/reload.png"));
                        imageView = new ImageView(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);

                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            reload();
                        }
                    });

                    paneTop.setLeft(imageView);
                }

                pane.setTop(paneTop);
            }
            {
                BorderPane paneBottom = new BorderPane();

                pane.setMaxWidth(width / 2f);
                pane.setMinWidth(width / 2f);
                pane.setPrefSize(width / 2f, height);

                {
                    Button button = new Button("Open in Web");

                    button.setFont(new Font(20));

                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            onOpenWeb.onOpenWeb(getCurrentNews().getUrl());
                        }
                    });

                    paneBottom.setCenter(button);
                }

                {
                    HBox box = new HBox();

                    Button button = new Button("Все новости");
                    Button button0 = new Button("Утро");
                    Button button1 = new Button("Обед");
                    Button button2 = new Button("Вечер");

                    box.getChildren().add(button);
                    box.getChildren().add(button0);
                    box.getChildren().add(button1);
                    box.getChildren().add(button2);

                    paneBottom.setRight(box);

                    button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            setTypeNews(TypeNews.ALL);
                        }
                    });
                    button0.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            setTypeNews(TypeNews.MORNING);
                        }
                    });
                    button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            setTypeNews(TypeNews.DINNER);
                        }
                    });
                    button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            setTypeNews(TypeNews.EVENING);
                        }
                    });
                }

                pane.setBottom(paneBottom);
            }

            root.getChildren().add(pane);
        }

        listView.getSelectionModel().select(0);
    }

    private void reload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownloadTools.downloadNews(new DownloadTools.OnDownloadReturn() {
                    @Override
                    public void onSuccess(List<News> newsList) {
                        Platform.runLater(() -> {
                            setList(newsList);
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

    private void setTypeNews(TypeNews typeNews) {
        setTypeNews(typeNews, true);
    }

    private void setTypeNews(TypeNews typeNews, boolean isUpdateListView) {
        this.typeNews = typeNews;

        if (isUpdateListView){
            updateListView(typeNews);
        }
    }

    private void setList(List<News> newsList) {
        setList(newsList, true);
    }

    private void setList(List<News> newsList, boolean isUpdateListView) {
        newsList.sort(new Comparator<News>() {
            @Override
            public int compare(News o1, News o2) {
                if (o1.getTime() > o2.getTime())
                    return -1;
                else if (o1.getTime() < o2.getTime())
                    return 1;

                return 0;
            }
        });

        this.newsList = newsList;

        if (isUpdateListView){
            updateListView(this.typeNews);
        }
    }

    private void updateListView(TypeNews typeNews) {
        List<News> newsList = new ArrayList<>(this.newsList);
        newsList = newsList.stream().filter(new Predicate<News>() {
            @Override
            public boolean test(News news) {
                return ( typeNews.from <= news.getTime() && news.getTime() <= typeNews.to ) ;
            }
        }).toList();

        currentNewsList = newsList;

        ArrayList<String> array = new ArrayList<>();

        for (News news : newsList) {
            array.add(new String((news.getTimeString() + " | " + news.getTitle()).getBytes(), StandardCharsets.UTF_8));
        }

        listView.setItems(FXCollections.observableArrayList(array));
    }

    private News getCurrentNews() {
        return newsList.get(index);
    }

    private void addIndex(int i) {
        index += i;

        if (index < 0)
            index = currentNewsList.size() - 1;

        if (index >= currentNewsList.size())
            index = 0;

        listView.scrollTo(index);
        listView.getSelectionModel().select(index);
    }

    private void selected(int index) {
        if (index == -1)
            index = 0;

        this.index = index;

        textNews.setText(new String(getCurrentNews().getDescription().getBytes(), StandardCharsets.UTF_8));
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

            default -> statusText = "На фоне нету задач";
        }

        statusView.setText(statusText);
    }


}
