import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import exception.NewsAPIException;
import model.NewsInfo;
import services.NewsAPIService;

public class NewsAPITest {

	@Test
	public void testtopheadlinesAPI() throws NewsAPIException {
		final NewsAPIService movieSearchService = NewsAPI.getNewsDBService();
		final List<NewsInfo> results = movieSearchService.searchForPopularNews( null, null, null, null, null, "gr", "sports");
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}
	
	@Test
	public void testeverythingAPI() throws NewsAPIException {
		final NewsAPIService movieSearchService = NewsAPI.getNewsDBService();
		final List<NewsInfo> results = movieSearchService.searchForNews("TESLA","", "en", "26/12/2021", "26/01/2022",null,null);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

}
