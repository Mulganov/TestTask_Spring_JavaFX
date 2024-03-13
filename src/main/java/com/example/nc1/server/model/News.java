package com.example.nc1.server.model;

import jakarta.persistence.*;

@Entity
public class News {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(columnDefinition="text", name="valueTitle")
	private String title;
	@Column(columnDefinition="text", name="valueDescription")
	private String description;
	@Column(columnDefinition="int", name="valueTime")
	private long time;
	@Column(columnDefinition="text", name="valueUrl")
	private String url;
	@Column(columnDefinition="int", name="valueDay")
	private long day;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDay() {
		return day;
	}

	public void setDay(long day) {
		this.day = day;
	}
}
