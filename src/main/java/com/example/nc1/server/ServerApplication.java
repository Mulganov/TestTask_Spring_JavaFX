package com.example.nc1.server;

import com.example.nc1.IApplication;
import com.example.nc1.OnStartServer;
import com.example.nc1.StatusApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ServerApplication implements IApplication {
    private ConfigurableApplicationContext context;
    private OnStartServer onStartServer;

    @Override
    public void onCreate(String[] args) {
        // Показываю что сейчас происходит Инит Спринга
        StatusApplication.setStatus(StatusApplication.Status.INIT_SERVER_BEGIN);

        new Thread(new Runnable() {
            @Override
            public void run() {
                context = SpringApplication.run(ServerApplication.class, args);

                if (onStartServer != null)
                    onStartServer.onStartServer();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        if (context != null)
            context.close();
    }

    public void setOnStartServer(OnStartServer onStartServer) {
        this.onStartServer = onStartServer;
    }

}
