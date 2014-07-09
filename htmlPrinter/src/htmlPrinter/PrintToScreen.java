package htmlPrinter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PrintToScreen {
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		String urlToFetch = "https://www.cs.drexel.edu/~wmm24/cs275_su14/labs/wxunderground.html";
		
		// Connect to the URL
		URL url = new URL(urlToFetch);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.connect();
		
		// Get the HTML content
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) request.getContent()));
		
		// Read in source code until end of file
		String pageHtml;
		while((pageHtml = in.readLine()) != null){
			//Print HTML to the screen.
			System.out.println(pageHtml);
		}
		
		// close the input stream
		in.close();
	}
}
