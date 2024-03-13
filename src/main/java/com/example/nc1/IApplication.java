package com.example.nc1;

public interface IApplication extends OnDestroy{

    void onCreate(String[] args);

    interface OnClose{
        void onClose();
    }
}
