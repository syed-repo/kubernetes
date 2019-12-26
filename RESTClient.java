import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RESTClient {
	public static void main(String[] args) {
		System.out.println("REST API Client application");
                try { 
			URL url = new URL("http://localhost:5000/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                	conn.setRequestMethod("GET");
                	conn.setRequestProperty("Accept", "application/xml");
			if (conn.getResponseCode() != 200) {
				System.out.println("Error in GET request, " + conn.getResponseCode());
			} else {
				System.out.println("Sucessfully executed the API");
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
