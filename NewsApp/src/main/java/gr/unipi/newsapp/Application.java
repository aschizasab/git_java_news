package gr.unipi.newsapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import exception.NewsAPIException;
import model.NewsInfo;
import services.NewsAPIService;

import newsapi.NewsAPI;
import getmyip.getMyIp;

public class Application {
	Connection connection = null;

	public static void main(String[] args) throws Exception {
		final NewsAPIService newsDBService = NewsAPI.getNewsDBService();

		System.out.println("Welcome the the NewsApp");

		while (true) {
			System.out.println("Select one from the following options: ");
			System.out.println("1. Show top headline NEWS for specified country and category.");
			System.out.println("2. Search for NEWS.");
			System.out.println("3. Show top headline NEWS from your Country.");
			System.out.println("4. Show last 5 uri searched.");
			System.out.println("5. Exit.");
			System.out.println("Your choice: ");

			Scanner sc = new Scanner(System.in);
			String input = sc.nextLine();

			System.out.println("Your choice: " + input);

			switch (input) {
			case "1":
				System.out.println("Selected 1");
				System.out.println("Enter Country: ");
				try {
					final String countryInput = sc.nextLine();
					System.out.println("Enter Category: ");
					final String categoryInput = sc.nextLine();
					List<NewsInfo> results;
					results = newsDBService.searchForPopularNews(null, null, null, null, null, countryInput,
							categoryInput);
					System.out.println("Results are: ");
					System.out.println(results);

				} catch (NewsAPIException e) {
					System.err.println(e.getMessage());
				}

				break;
			case "2":
				System.out.println("Selected 2");
				System.out.print("Enter search parameter: ");
				try {
					String searchParam = sc.nextLine();
					System.out.print("Enter search Source: ");
					String sourceParam = sc.nextLine();
					System.out.print("Enter search Language: ");
					String languageParam = sc.nextLine();
					System.out.print("Enter search Start Date: ");
					String fromParam = sc.nextLine();
					System.out.print("Enter search End Date: ");
					String toParam = sc.nextLine();

					final List<NewsInfo> results = newsDBService.searchForNews(searchParam, sourceParam, languageParam,
							fromParam, toParam, null, null);
					System.out.println("Results are: ");
					System.out.println(results);
				} catch (NewsAPIException e) {
					System.err.println(e.getMessage());
				}
				break;
			case "3":
				try {
					final String findCountry = getMyIp.getCountry();
					List<NewsInfo> results;
					results = newsDBService.searchForPopularNews(null, null, null, null, null, findCountry, null);
					System.out.println("Results are: ");
					System.out.println(results);

				} catch (NewsAPIException e) {
					System.err.println(e.getMessage());
				}
				break;
			case "4":
				try {
					//σύνδεση με sqlserver "παίρνουμε" τις τελευταίες 5 καταχωρήσεις και εμφανίζονται στο console
					String connectionUrl = "jdbc:sqlserver://LAPTOP-73S7VOMB\\SQLEXPRESS;databaseName=NewsApi;user=Test;password=12345";
					Connection connection = DriverManager.getConnection(connectionUrl);
					Statement statement = connection.createStatement();
					String sql = "SELECT TOP(5) uri FROM uri ORDER BY ID DESC";
					ResultSet resultSet = statement.executeQuery(sql);
					System.out.println("Last 5 uri searches are: ");
					{
						while (resultSet.next()) {
							System.out.println(resultSet.getString(1));
						}
					}
				}
					catch (SQLException e) {
					System.err.println(e.getMessage());
				}
				break;
			case "5":
				System.out.println("Selected 5");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid input");
			}
		}
	}

}
