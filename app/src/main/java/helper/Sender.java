package helper;

/**
 * Created by dverdugo on 29-06-16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by Oclemmy on 3/31/2016 for ProgrammingWizards Channel and Camposha.com.
 * 1.SEND DATA FROM EDITTEXT OVER THE NETWORK
 * 2.DO IT IN BACKGROUND THREAD
 * 3.READ RESPONSE FROM A SERVER
 */
public class Sender extends AsyncTask<Void,Void,JSONObject> {
    Context c;
    String urlAddress;
    EditText nameTxt,emailTxt,passwordTxt;
    String name,email,password;
    ProgressDialog pd;
    static JSONObject jObj = null;
    static InputStream is = null;
    static JSONArray jAry = null;
    /*
            1.OUR CONSTRUCTOR
    2.RECEIVE CONTEXT,URL ADDRESS AND EDITTEXTS FROM OUR MAINACTIVITY
    */
    public Sender(Context c, String urlAddress,EditText...editTexts) {
        this.c = c;
        this.urlAddress = urlAddress;
        //INPUT EDITTEXTS
        this.nameTxt=editTexts[0];
        this.emailTxt=editTexts[1];
        this.passwordTxt=editTexts[2];
        //GET TEXTS FROM EDITEXTS
        name=nameTxt.getText().toString();
        email=emailTxt.getText().toString();
        password=passwordTxt.getText().toString();
    }
    /*
   1.SHOW PROGRESS DIALOG WHILE DOWNLOADING DATA
    */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Send");
        pd.setMessage("Sending..Please wait");
        pd.show();
    }
    /*
    1.WHERE WE SEND DATA TO NETWORK
    2.RETURNS FOR US A STRING
     */
    @Override
    protected JSONObject doInBackground(Void... params) {
        return this.send();
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);
        pd.dismiss();
        if(response != null)
        {
            //SUCCESS
            Toast.makeText(c,response.toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(c,"Successful ",Toast.LENGTH_LONG).show();

            nameTxt.setText("");
            emailTxt.setText("");
            passwordTxt.setText("");
        }else
        {
            //NO SUCCESS
            Toast.makeText(c,"Unsuccessful ",Toast.LENGTH_LONG).show();
        }
    }
    /*
    SEND DATA OVER THE NETWORK
    RECEIVE AND RETURN A RESPONSE
     */
    private JSONObject send()
    {
        //CONNECT
        HttpURLConnection con=Connector.connect(urlAddress);
        if(con==null)
        {return null;
        }
        try {
            OutputStream os=con.getOutputStream();
            //WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new DataPackager(name,email,password).packData());
            bw.flush();
            //RELEASE RES
            bw.close();
            os.close();
            //HAS IT BEEN SUCCESSFUL?
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
                //GET EXACT RESPONSE
             /**   BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();
                String line;
                //READ LINE BY LINE
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }
                //RELEASE RES
                br.close();
                return response.toString();

**/
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

               String json = sb.toString();

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                // return JSON String
                return jObj;

            }else
            {
                Log.e("JSON Parser", "hola "+String.valueOf(responseCode) );

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String json = sb.toString();

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                // return JSON String
                Log.e("JSON Parser", "hola:  "+jObj );
                return jObj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}