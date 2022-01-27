package newsapi;
import services.NewsAPIService;

public class NewsAPI {
	public static NewsAPIService getNewsDBService() {
		// API key needed. Register and generate API KEY
		return new NewsAPIService("https://newsapi.org/", "541b4ecb4d9d44e28225b47371463dc6");
	}

}
