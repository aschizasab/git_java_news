package getmyip;

import java.net.*;
import java.io.*;
import java.util.*;

public class getMyIp
{
	public static String getCountry() throws UnknownHostException	{
		// Returns the instance of InetAddress containing
		// local host name and address
		InetAddress localhost = InetAddress.getLocalHost();
		//System.out.println("System IP Address : " +
					//(localhost.getHostAddress()).trim());
		// Find public IP address
		String systemipaddress = "";
		try
		{
			//εμφάνιση public ip
			URL url_name = new URL("http://checkip.amazonaws.com");
			//URL url_name = new URL("https://ipinfo.io/130.43.62.192/country?token=f8a6ccc2101486");
			BufferedReader sc =
			new BufferedReader(new InputStreamReader(url_name.openStream()));
			// reads system IPAddress
			systemipaddress = sc.readLine().trim();
		}
		catch (Exception e)
		{
			systemipaddress = "Cannot Execute Properly";
		}
		//System.out.println("Public IP Address: " + systemipaddress +"\n");
		String systemcountry = "";
		try
		{
			//καλείται το παρακάτω API με μεταβλητή systemipaddress
			URL url_name = new URL("https://ipinfo.io/"+ systemipaddress +"/country?token=f8a6ccc2101486");
			BufferedReader sc =
			new BufferedReader(new InputStreamReader(url_name.openStream()));
			// reads system IPAddress
			systemcountry = sc.readLine().trim();
		}
		catch (Exception e)
		{
			systemcountry = "Cannot Execute Properly";
		}
		//System.out.println("Public IP Address: " + systemcountry +"\n");
		return systemcountry;
	}
}
