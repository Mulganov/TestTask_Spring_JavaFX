package com.example.nc1.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nc1.server.model.News;
import com.example.nc1.server.service.NewsService;

@RestController
public class NewsController {
	
	@Autowired
	NewsService newsService;
	
	@GetMapping(value = "/news")
	public List<News> getNews() {
		return newsService.getAllNews();
	}


}
