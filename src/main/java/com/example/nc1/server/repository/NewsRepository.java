package com.example.nc1.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nc1.server.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>{

}
