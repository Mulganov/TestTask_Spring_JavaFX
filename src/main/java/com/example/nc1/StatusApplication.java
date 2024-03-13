package com.example.nc1;

import javafx.beans.value.ObservableValue;

public class StatusApplication {
    public enum Status{
        INIT_SERVER_BEGIN,
        INIT_SERVER_END,
        SCAN_NEWS_BEGIN,
        SCAN_NEWS_END,
        DOWNLOAD_NEWS_BEGIN,
        DOWNLOAD_NEWS_END,
        NON_STATUS
    }

    public static Var<Status> STATUS = new Var<>(Status.NON_STATUS);
    public static void setStatus(Status status) {
        STATUS.setValue(status);
    }
}
