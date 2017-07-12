package helper;

/**
 * Created by dverdugo on 29-06-16.
 */
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Oclemmy on 3/31/2016 for ProgrammingWizards Channel.
 */
public class Connector {
    /*
 1.SHALL HELP US ESTABLISH A CONNECTION TO THE NETWORK
 2. WE ARE MAKING A POST REQUEST
  */
    public static HttpURLConnection connect(String urlAddress) {
        try
        {
            URL url=new URL(urlAddress);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            //SET PROPERTIES
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Content-Language", "en-US");
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoInput(true);
            con.setDoOutput(true);

            //RETURN
            return con;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
