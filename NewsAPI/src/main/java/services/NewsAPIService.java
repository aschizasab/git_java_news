package services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.NewsAPIException;
import model.NewsInfo;
import modelnewsdb.ErrorResponse;
import modelnewsdb.NewsResult;
import modelnewsdb.Result;

public class NewsAPIService {

	private final String API_URL;
	private final String API_KEY;

	public NewsAPIService(String aPI_URL, String aPI_KEY) {
		API_URL = aPI_URL;
		API_KEY = aPI_KEY;
	}
	// ---------------------------------------------------------------------------------------------------
	// Τους τρέχοντες κορυφαίους τίτλους ειδήσεων που αφορούν τη χώρα στην οποία
	// βρίσκεται ο χρήστης
	// Επιλογή άλλης χώρας
	// Επιλογή κατηγορίας ειδήσεων

	// https://newsapi.org/v2/top-headlines?country=gr&category=sports
	// -----------------------------------------------------------------------------------------------------
	public List<NewsInfo> searchForPopularNews(String parameter, String parameter2, String parameter3,
			String parameter4, String parameter5, String parameter6, String parameter7) throws NewsAPIException {
		NewsResult result = getAPIData("top-headlines", null, null, null, null, null, parameter6, parameter7, API_URL,
				API_KEY);
		List<NewsInfo> newsInfoList = new ArrayList<>(result.getArticles().size());
		for (Result theResult : result.getArticles()) {
			newsInfoList.add(new NewsInfo(theResult));
		}
		return newsInfoList;
	}

	// --------------------------------------------------------------------------------------------------------
	// Τίτλους ειδήσεων που έχουν ανακτηθεί μετά από αναζήτηση με βάση συγκεκριμένα
	// κριτήρια, όπως:
	// Λέξεις ή φράσεις που περιλαμβάνονται στον τίτλο ή το σώμα μιας είδησης (π.χ.
	// Apple).
	// Κατηγορία (π.χ. business, entertainment κλπ.) - δεν υπάρχει σαν Request
	// parameter στο API.
	// Πηγή (π.χ. Νew York Τimes).
	// Γλώσσα (π.χ. Αγγλικά).
	// Χρονική στιγμή δημοσίευσης (π.χ. αναζήτηση τίτλων ειδήσεων που αναρτήθηκαν
	// στο διάστημα από 10/11/2021 έως 10/12/2021)
	// https://newsapi.org/v2/everything?q=TESLA&sources=engadget&language=en&from=26/12/2021&to=26/01/2022
	// ---------------------------------------------------------------------------------------------------------------------------------
	public List<NewsInfo> searchForNews(String parameter, String parameter2, String parameter3, String parameter4,
			String parameter5, String parameter6, String parameter7) throws NewsAPIException {
		NewsResult result = getAPIData("everything", parameter, parameter2, parameter3, parameter4, parameter5, null,
				null, API_URL, API_KEY);
		List<NewsInfo> newsInfoList = new ArrayList<>(result.getArticles().size());
		for (Result theResult : result.getArticles()) {
			newsInfoList.add(new NewsInfo(theResult));
		}
		return newsInfoList;
	}

	// GET API DATA
	private NewsResult getAPIData(String apiFunction, String parameter, String parameter2, String parameter3,
			String parameter4, String parameter5, String parameter6, String parameter7, String API_URL, String API_KEY)
			throws NewsAPIException {
		try {
			// https://newsapi.org/v2/everything?q=TESLA&sources=engadget&language=en&from=26/12/2021&to=26/01/2022
			// https://newsapi.org/v2/top-headlines?country=gr&category=sports
			final URIBuilder uriBuilder = new URIBuilder(API_URL).setPathSegments("v2", apiFunction)
					.addParameter("apiKey", API_KEY);
			if (parameter != null && !parameter.isBlank() || parameter2 != null && !parameter2.isBlank()
					|| parameter3 != null && !parameter3.isBlank() || parameter4 != null && !parameter4.isBlank()
					|| parameter5 != null && !parameter5.isBlank() || parameter6 != null && !parameter6.isBlank()
					|| parameter7 != null && !parameter7.isBlank()) {
				switch (apiFunction) {
				case "everything":
					uriBuilder.addParameter("q", parameter);
					uriBuilder.addParameter("sources", parameter2);
					uriBuilder.addParameter("language", parameter3);
					uriBuilder.addParameter("from", parameter4);
					uriBuilder.addParameter("to", parameter5);
					break;
				case "top-headlines":
					uriBuilder.addParameter("country", parameter6);
					uriBuilder.addParameter("category", parameter7);
					break;
				}
			}
			final URI uri = uriBuilder.build();
			//σύνδεση με mssqlserver
			String connectionUrl = "jdbc:sqlserver://LAPTOP-73S7VOMB\\SQLEXPRESS;databaseName=NewsApi;user=Test;password=12345";
//			System.out.println("OK");
			try (Connection connection = DriverManager.getConnection(connectionUrl)) {
				//δημιουργία string sql query για να γινει insert το uri στη βάση
				String sql = new StringBuilder().append("INSERT URI (Uri)").append("VALUES (?)").toString();
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, uri.toString());
				statement.executeUpdate();
				
			} catch (Exception e) {
				System.out.println();
				e.printStackTrace();
			}
//			System.out.println("OK");
			final HttpGet getRequest = new HttpGet(uri);
			// System.out.println(getRequest);
			//System.out.println(uri);
			final CloseableHttpClient httpclient = HttpClients.createDefault();
			try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
				final HttpEntity entity = response.getEntity();
				final ObjectMapper mapper = new ObjectMapper();
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					ErrorResponse errorResponse = mapper.readValue(entity.getContent(), ErrorResponse.class);
					if (errorResponse.getStatus() != null)
						throw new NewsAPIException("Error occurred on API call: " + errorResponse.getStatus());
				}
				return mapper.readValue(entity.getContent(), NewsResult.class);
			} catch (IOException e) {
				throw new NewsAPIException("Error requesting data from NewsDB API.", e);
			}
		} catch (URISyntaxException e) {
			throw new NewsAPIException("Unable to create request URI.", e);
		}
	}
}
