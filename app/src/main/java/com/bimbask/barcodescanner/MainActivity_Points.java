package com.bimbask.barcodescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class MainActivity_Points extends AppCompatActivity  {
EditText editText_temperature,editText_Pressure,editText_description;
    Toolbar toolbar;
    String KEY_STATION="",KEY_NAME="",KEY_EMAIL="",KEY_UNIQUE_ID="",KEY_TEMPERATURE="",KEY_PRESSURE="",KEY_DESCRIPTION="",KEY_VALIDATE_NAME="";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_point);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        editText_temperature = (EditText) findViewById(R.id.editText_temperature);
        editText_Pressure= (EditText)findViewById(R.id.editText_Pressure);
        editText_description =(EditText)findViewById(R.id.editText_description);

        editText_Pressure.addTextChangedListener(new MyTextWatcher(editText_Pressure));
        editText_temperature.addTextChangedListener(new MyTextWatcher(editText_temperature));
        editText_description.addTextChangedListener(new MyTextWatcher(editText_description));
        editText_temperature.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    editText_Pressure.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        editText_Pressure.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    editText_description.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        editText_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    KEY_TEMPERATURE = editText_temperature.getText().toString().trim();
                    KEY_DESCRIPTION = editText_description.getText().toString().trim();
                    KEY_PRESSURE = editText_Pressure.getText().toString().trim();

                    if(KEY_TEMPERATURE.isEmpty() && KEY_DESCRIPTION.isEmpty()&&KEY_PRESSURE.isEmpty() ){
                        Toast.makeText(getApplicationContext(), "Ingrese toda la información", Toast.LENGTH_LONG).show();

                    }else {
                        showMessageOKCancel_three(MainActivity_Points.this, "Confirmación",
                                "Presión",
                                KEY_PRESSURE, "Temperatura", KEY_TEMPERATURE, "Descripción", KEY_DESCRIPTION,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkLogin(KEY_STATION,
                                                KEY_UNIQUE_ID,
                                                KEY_TEMPERATURE,
                                                KEY_PRESSURE,
                                                KEY_DESCRIPTION,KEY_NAME,KEY_EMAIL);
                                    }
                                });
                    }
                    return true;
                }
                return false;
            }
        });

        if(getIntent()!=null){
            KEY_STATION = getIntent().getStringExtra("text");
            KEY_UNIQUE_ID = getIntent().getStringExtra("unique_id");
            KEY_NAME = getIntent().getStringExtra("name");
            KEY_EMAIL = getIntent().getStringExtra("email");
            final TextView  tb = (TextView)toolbar.findViewById(R.id.toolbar_title);
           if(KEY_STATION.equals("estacion_1")){
               tb.setText("Estación 1");
               KEY_VALIDATE_NAME="Estación 1";
           }else if(KEY_STATION.equals("estacion_2")){
               tb.setText("Estación 2");
               KEY_VALIDATE_NAME="Estación 2";
           }else if(KEY_STATION.equals("estacion_3")){
               tb.setText("Estación 3");
               KEY_VALIDATE_NAME="Estación 3";
           }else{
            tb.setText(KEY_STATION.replace("_"," "));
           }
        }


    }



    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String KEY_STATION,
                            final String KEY_UNIQUE_ID,
                            final String KEY_TEMPERATURE,
                            final String KEY_PRESSURE,
                            final String KEY_DESCRIPTION,
                            final String KEY_NAME_,
                                    final String KEY_EMAIL_) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Enviando información ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_STATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        try {
                            for (int i = 0; i < MainActivity.mListview_Friends_Chat_Adapter_Recycler.getCount(); i++) {

                                if(MainActivity.mListview_Friends_Chat_Adapter_Recycler.get_data().get(i).get("name").equals(KEY_VALIDATE_NAME)){
                                    MainActivity.mListview_Friends_Chat_Adapter_Recycler.get_data().get(i).remove("status");
                                    MainActivity.mListview_Friends_Chat_Adapter_Recycler.get_data().get(i).put("status","yes");
                                    MainActivity.mListview_Friends_Chat_Adapter_Recycler.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }catch(NullPointerException ee){ee.printStackTrace();}


                        Toast.makeText(getApplicationContext(), "OK!, información enviada a "+KEY_VALIDATE_NAME, Toast.LENGTH_LONG).show();
                        onBackPressed();
                        overridePendingTransition(R.anim.in_back_pressed,R.anim.out_back_pressed);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Problema de conexión, intentar denuevo", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Problema de conexión, intentar de nuevo", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("station", KEY_STATION);
                params.put("unique_id", KEY_UNIQUE_ID);
                params.put("temperature", KEY_TEMPERATURE);
                params.put("pressure", KEY_PRESSURE);
                params.put("description", KEY_DESCRIPTION);
                params.put("name", KEY_NAME_);
                params.put("email", KEY_EMAIL_);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enviar:
                KEY_TEMPERATURE = editText_temperature.getText().toString().trim();
                KEY_DESCRIPTION = editText_description.getText().toString().trim();
                KEY_PRESSURE = editText_Pressure.getText().toString().trim();
                if(KEY_TEMPERATURE.isEmpty() && KEY_DESCRIPTION.isEmpty()&&KEY_PRESSURE.isEmpty() ){

                }else {
                    showMessageOKCancel_three(MainActivity_Points.this, "Confirmación",
                            "Presión",
                            KEY_PRESSURE, "Temperatura", KEY_TEMPERATURE, "Descripción", KEY_DESCRIPTION,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkLogin(KEY_STATION,
                                            KEY_UNIQUE_ID,
                                            KEY_TEMPERATURE,
                                            KEY_PRESSURE,
                                            KEY_DESCRIPTION,KEY_NAME,KEY_EMAIL);
                                }
                            });

                }



                break;
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.in_back_pressed,R.anim.out_back_pressed);
               break;
        }return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.main_chat_friend, menu);return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_temperature:
                    ///validateName();
                    break;
                //case R.id.input_email:
                //   validateEmail();
                //  break;
                case R.id.editText_Pressure:
                    //validatePassword();
                    break;
            }
        }
    }

    public static void showMessageOKCancel_one_sms(final Context ctx, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener okListener_) {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx)
                .setTitle(Html.fromHtml("<font color='#077fad'>"+title+"</font>"))
                .setMessage(Html.fromHtml("<font color='#646464'>"
                        + message
                        + "</font>"))
                .setCancelable(false)
                .setPositiveButton("ENVIAR", okListener)
                .setNegativeButton("CANCELAR", okListener_);


        final AlertDialog alert11 = builder1.create();

        //2. now setup to change color of the button
        alert11.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ctx.getResources().getColor(R.color.plomo));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ctx.getResources().getColor(R.color.orange_android));

            }
        });


        alert11.show();


    }

    public static  void showMessageOKCancel_three(final Context ctx,String title,String type, String message,String type2, String message2,String type3, String message3, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx)
                .setTitle(Html.fromHtml("<font color='#077fad'>"+title+"</font>"))
                .setMessage(Html.fromHtml("<font color='#646464'><b>" + type + "</b>"+ "<br/>" +
                        message + "<br />"
                        + "<b>" + type2 + "</b>" + "<br/>" +
                        message2 + "<br />"+
                        "<b>" + type3 + "</b>" + "<br/>" +
                        message3 + "<br />"+
                        "</font>"))
                .setCancelable(false)
                .setPositiveButton("ENVIAR", okListener)
                .setNegativeButton("CANCELAR", null);

        final AlertDialog alert11 = builder1.create();

        //2. now setup to change color of the button
        alert11.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ctx.getResources().getColor(R.color.plomo));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ctx.getResources().getColor(R.color.orange_android));

            }
        });


        alert11.show();

    }
}
