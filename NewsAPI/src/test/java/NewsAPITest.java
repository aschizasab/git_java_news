import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.Test;

import exception.NewsAPIException;
import model.NewsInfo;
import services.NewsAPIService;

public class NewsAPITest {

	@Test
	public void testtopheadlinesAPI() throws NewsAPIException {
		final NewsAPIService movieSearchService = NewsAPI.getNewsDBService();
		final List<NewsInfo> results = movieSearchService.searchForPopularNews(null, null, null, null, null, "gb",
				"technology");
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testeverythingAPI() throws NewsAPIException {
		final NewsAPIService movieSearchService = NewsAPI.getNewsDBService();
		final List<NewsInfo> results = movieSearchService.searchForNews("covid", "", "en", "2022-02-16", "2022-02-18",
				null, null);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}
}
