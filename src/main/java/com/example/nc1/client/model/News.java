package com.example.nc1.client.model;

public class News {
	private long id;
	private String title;
	private String description;
	private long time;
	private String url;
	private int day;
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

	public String getTimeString(){
		if (time == 0)
			return "non time";

		StringBuilder sb = new StringBuilder();

		int second = (int) (time / 1000);
		int minute = second / 60;
		int hour = minute / 60;

		minute -= hour * 60;

		String mS = minute + "";
		String hS = hour + "";

		if (mS.length() == 1)
			mS = "0" + mS;
		if (hS.length() == 1)
			hS = "0" + hS;

		sb.append(hS).append(":").append(mS);

		return sb.toString();
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "News{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", time='" + time + '\'' +
				", url='" + url + '\'' +
				", day=" + day +
				'}';
	}
}
