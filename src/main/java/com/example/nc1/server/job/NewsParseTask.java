package com.example.nc1.server.job;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import com.example.nc1.StatusApplication;
import com.example.nc1.server.ServerApplication;
import com.example.nc1.server.model.News;
import com.example.nc1.server.service.NewsService;
import com.example.nc1.server.tools.JsoupTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsParseTask {

	private String[] postKeys = new String[]{
			"post_text",
			"post__text",
			"post___text",
			"post_news_text",
			"post_article_text"
	};

	@Autowired
	NewsService newsService;

	// Тут можно настроить период Сканирование сайта, сейчас стоит 15 секунд, что бы можно было видеть
	// как меняется статус в интерфейсе
	@Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay = 3, fixedDelay = 15) // fixedDelay = 20 * 60 = 10 минут
	public void parseNews() {
		StatusApplication.setStatus(StatusApplication.Status.SCAN_NEWS_BEGIN);

		clearOldNews();

		String domain = "https://www.pravda.com.ua";
		String urlNews = domain + "/rus/news/";

		Document doc = JsoupTools.connect(urlNews);

		for (Element el: doc.getElementsByClass("article_news_list")) {
			String title = null;
			String url = null;
			String description = null;
			String time = JsoupTools.getTextByClass(el, "article_time");

			for (Element elTitle: el.getElementsByClass("article_header")){
				title = JsoupTools.getTextByTag(elTitle, "a");
				url = JsoupTools.getTextByAttributeInTag(elTitle, "href", "a");
			}

			if (url != null){
				if (!url.contains("https://"))
					url = domain + url;

				Document docDescription = JsoupTools.connect(url);
				description = JsoupTools.getTextByClass(docDescription, postKeys[0]);

				for (int i = 1; i < postKeys.length && (description == null || description.isEmpty()); i++){
					description = JsoupTools.getTextByClass(docDescription, postKeys[i]);
				}

				if (description == null)
					description = title;
			}

			long t = 0;

			if (time != null){
				String[] a = time.split(":");

				int minute = Integer.parseInt(a[1]);
				int hour = Integer.parseInt(a[0]);

				t = (hour * 60L + minute) * 60 * 1000;
			}

			if (!newsService.isExist(title)) {
				News obj = new News();

				obj.setTitle(title);
				obj.setTime(t);
				obj.setDescription(description);
				obj.setUrl(url);
				obj.setDay(new GregorianCalendar().get(Calendar.DAY_OF_MONTH));

				newsService.save(obj);
			}
		}

		StatusApplication.setStatus(StatusApplication.Status.SCAN_NEWS_END);
	}

	private void clearOldNews() {
		int day = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);

		for (News news: newsService.getAllNews()){
			if (news.getDay() != day){
				newsService.delete(news);
			}
		}
	}
}
