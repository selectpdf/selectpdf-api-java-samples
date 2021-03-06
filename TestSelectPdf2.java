import java.io.*;
import java.net.*;
import java.util.*;

/*
 * This code converts an url to pdf in Java using SelectPdf HTML To PDF REST API through a POST request. 
 * The parameters are url encoded. The content is saved into a file on the disk.
 *
 */
public class TestSelectPdf2 {
    public static void main(String[] args) throws Exception {
		HttpURLConnection urlConnection = null;

		try {
			String apiEndpoint = "http://selectpdf.com/api2/convert/";
			String key = "your license key here";
			String testUrl = "http://selectpdf.com";
			File localFile = new File("test.pdf");

			Map<String,Object> parameters = new LinkedHashMap<>();
			parameters.put("key", key);
			parameters.put("url", testUrl);

			String encodedParameters = encodeParameters(parameters);
			byte[] postDataBytes = encodedParameters.getBytes("UTF-8");

			URL apiUrl = new URL(apiEndpoint);
			urlConnection = (HttpURLConnection)apiUrl.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			urlConnection.setDoOutput(true);
			urlConnection.getOutputStream().write(postDataBytes);

			BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile));

			byte[] b = new byte[8 * 1024];
			int read = 0;
			while ((read = inputStream.read(b)) > -1) {
				outputStream.write(b, 0, read);
			}
			outputStream.flush();
			outputStream.close();
			inputStream.close();

			System.out.println("Test pdf document generated successfully!"); 
		}
		catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());

			if (urlConnection != null) {
				if (urlConnection.getResponseCode() != 200) {
					System.out.println("HTTP Response Code: " + urlConnection.getResponseCode());
					System.out.println("HTTP Response Message: " + urlConnection.getResponseMessage());
				}
			}
		}
    }

	private static String encodeParameters(Map<String,Object> params) throws Exception {
		try {
			StringBuilder data = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
				if (data.length() != 0) data.append('&');
				data.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				data.append('=');
				data.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}

			return data.toString();
		}
		catch (Exception ex) {
			throw(ex);
		}
	}
}