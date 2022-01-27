package model;

import modelnewsdb.Result;

public class NewsInfo {
	private String title;
	private String description;
	private String publishedAt;
	private String url;
	
	public NewsInfo(String title, String description, String publishedAt, String url) {
		super();
		this.title = title;
		this.description = description;
		this.publishedAt = publishedAt;
		this.url = url;
	}
	public NewsInfo(Result theResult) {
		this.title = theResult.getTitle();
		this.description = theResult.getDescription();
		this.publishedAt = theResult.getPublishedAt();
		this.url = theResult.getUrl();
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

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
    public String toString() {
        return "NewsInfo{" +
                "title='" + title + "'\n" +
                ", description='" + description + "'\n" +
                ", publishedAt='" + publishedAt + "'\n" +
                ", url='" + url + "'\n" +
                '}';
    }
	

}
