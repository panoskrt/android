package com.panoskrt.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GET {

	public GET() {
	}

	public String getRequest(String URL, String agent) {
		String getResponse = null;
		try {
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", agent);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
			in.close();
			getResponse = response.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getResponse;
	}
}
