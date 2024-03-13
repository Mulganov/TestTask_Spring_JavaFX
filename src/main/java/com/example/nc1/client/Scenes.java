package com.example.nc1.client;

import com.example.nc1.OnOpenWeb;
import com.example.nc1.Var;
import com.example.nc1.client.model.News;
import com.example.nc1.client.scene.BaseScene;
import com.example.nc1.client.scene.ListNewsScene;
import com.example.nc1.client.scene.SplashScene;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class Scenes {

    private static final Var<BaseScene> currentScene = new Var<>();

    static {
        currentScene.addObserver(new Var.OnEdit<BaseScene>() {
            @Override
            public void onEdit(ArrayList<BaseScene> values) {
                if (values.size() > 1){
                    BaseScene oldValue = values.get(1);
                    oldValue.onDestroy();
                }
            }
        });
    }

    public static BaseScene SPLASH(float width, float height){
        currentScene.setValue(new SplashScene(width, height));

        return currentScene.getValue();
    }
    public static BaseScene LIST_NEWS(float width, float height, List<News> list, OnOpenWeb onOpenWeb){
        currentScene.setValue(new ListNewsScene(width, height, list, onOpenWeb));

        return currentScene.getValue();
    }

}
