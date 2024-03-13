package com.example.nc1;

import com.example.nc1.client.ClientApplication;
import com.example.nc1.client.Scenes;
import com.example.nc1.client.scene.BaseScene;
import com.example.nc1.server.ServerApplication;

import java.util.ArrayList;


public class MainApplication implements IApplication.OnClose{

	private final static ServerApplication serverApplication = new ServerApplication();
	private static ClientApplication clientApplication;
	private final static MainApplication mainApplication = new MainApplication();

	public static void main(String[] args) {
		serverApplication.setOnStartServer(new OnStartServer() {
			@Override
			public void onStartServer() {
				// был баг, что если закрыть окно раньше инита Спринга - то он не мог завершить свой процесс
				// эта штука фиксит этот баг
				if (clientApplication.isStop()){
					serverApplication.onDestroy();
				}

				StatusApplication.setStatus(StatusApplication.Status.INIT_SERVER_END);
			}
		});

		// Запуск серверной части
		serverApplication.onCreate(args);

		// Пришлось сделать вот такой костыль, ибо в JavaFX при launch() создается новый класс, и из-за этого
		// могут быть проблемы из-за разных ссылок на класс
		// (в MainApplication один класс хранится, а по факту работает другой)
		ClientApplication.create(new ClientApplication.OnCreate() {
			@Override
			public void onCreate(ClientApplication application) {
				// вот теперь в clientApplication будет храниться верная ссылка на обьект класса ClientApplication
				clientApplication = application;
				// сделал событие для закрывания окна JavaFX что бы сразу и прекратить работу сервера
				clientApplication.setOnClose(mainApplication);
				// Запуск клиентской части
				clientApplication.onCreate(args);
			}
		});

		// Создал наблюдателя, для того что бы
		StatusApplication.STATUS.addObserver(new Var.OnEdit<StatusApplication.Status>() {
			@Override
			public void onEdit(ArrayList<StatusApplication.Status> values) {
				if (clientApplication.isStop()){
					serverApplication.onDestroy();

					return;
				}

				// Первое скачивания после сканирование сайта, по сути нужно что для SplashScene и по сути инициализации
				// главной сцены
				//[SCAN_NEWS_END, SCAN_NEWS_BEGIN, INIT_SERVER_END, INIT_SERVER_BEGIN]
				if (values.size() == 4 ){
					if (values.get(values.size() - 1) == StatusApplication.Status.INIT_SERVER_BEGIN){
						clientApplication.downloadNews();
					}
				}
			}
		});
	}

	public void onClose() {
		// говорим нашим апликейшинам что нужно завершить свою работу
		serverApplication.onDestroy();
		clientApplication.onDestroy();
	}
}
