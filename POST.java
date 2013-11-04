package com.panoskrt.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class POST {

	public void HTTP() {
	}

	public String postRequest(String URL, String parameters, String agent) {
		String postResponse = null;
		try {
			URL obj = new URL(URL.toString());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Request Header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", agent);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
			in.close();
			postResponse = response.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return postResponse;
	}
}
