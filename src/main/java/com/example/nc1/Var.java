package com.example.nc1;

import javafx.application.Platform;

import java.util.ArrayList;

public class Var<T> {
    private T value;
    private ArrayList<OnEdit<T>> onEdits = new ArrayList<>();
    private ArrayList<OnEdit<T>> onEditsIOThread = new ArrayList<>();
    private ArrayList<T> history = new ArrayList<>();

    public Var(T value) {
        this.value = value;
    }
    public Var() {}

    public void addObserver(OnEdit<T> onEdit){
        onEdits.add(onEdit);
    }
    public void addObserverIOThread(OnEdit<T> onEdit){
        onEditsIOThread.add(onEdit);
    }
    public void removeObserver(OnEdit<T> onEdit){
        onEdits.remove(onEdit);
        onEditsIOThread.remove(onEdit);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        history.add(0, value);

        this.value = value;

        while (history.size() > 7){
            history.remove( history.size() - 1 );
        }

        for (OnEdit<T> onEdit: onEdits){
            onEdit.onEdit(history);
        }

        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (OnEdit<T> onEdit: onEditsIOThread){
                        onEdit.onEdit(history);
                    }
                }
            });
        }catch (Exception ignored){}
    }

    public interface OnEdit<T>{
        void onEdit(ArrayList<T> values);
    }
}
