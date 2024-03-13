package com.example.nc1.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nc1.server.model.News;

@Service
public interface NewsService {

	void save(News news);
	void delete(News news);
	boolean isExist(String newsTitle);
	List<News> getAllNews();
}
