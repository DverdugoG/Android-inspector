/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.lind.barcodescanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimbask.barcodescanner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    try {
                        registerUser(name, email, password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Porfavor ingrese la información!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity_Initial.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     *
     * @param name
     * @param email
     * @param password*/
    private void registerUser(final String name, final String email,
                              final String password) throws JSONException {
        // Tag used to cancel the request

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registrando ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "Usuario correctamente registrado. Intente iniciar sesión ahora!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity_Initial.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("X-CSRF-Token", params.get("_token"));
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                //Log.d("token",params.get("_token"));
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

     //   Sender s=new Sender(RegisterActivity.this,AppConfig.URL_REGISTER,name,email,password);
      //  s.execute();
      //  String tag_string_req = "req_register";

       // pDialog.setMessage("Registering ...");
       // showDialog();

      /**  StringRequest strReq = new StringRequest(Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity_Initial.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Log.e(TAG, "Registration Error2: " +error.toString());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "d");
                params.put("email", "d@d.d");
                params.put("password", "d");

                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
**/
        /**
        Log.d("name", name);
        Log.d("email", email);
        Log.d("password", password);
        final Map<String, String> postParams = new HashMap<String, String>();
        postParams.put("name", name);
        postParams.put("email", email);
        postParams.put("password", password);




        Response.Listener<JSONObject>  listener;
        Response.ErrorListener errorListener;
        final JSONObject jsonObject = new JSONObject(postParams);

       JsonObjectRequest jsonObjReq = new
                JsonObjectRequest(AppConfig.URL_REGISTER, jsonObject,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //Log.d("TAG", response.toString());
                                try {
                                    Toast.makeText(getApplicationContext(),
                                            response.getString("message"), Toast.LENGTH_LONG).show();


                                    if (response.getString("status").equals("success")){
                                        session.setLogin(true);

                                        pDialog.dismiss();
                                        Intent i = new
                                                Intent(RegisterActivity.this,MainActivity_Initial.class);
                                        startActivity(i);

                                        finish();
                                    }
                                } catch (JSONException e) {
                                    Log.e("TAG", e.toString());
                                }
                                pDialog.dismiss();
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        pDialog.dismiss();

                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                };
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name);
        jsonBody.put("email", email);
        jsonBody.put("password", password);
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jObj) {
                        Log.d("TAG", jObj.toString());
                        hideDialog();

                        try {

                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                // User successfully stored in MySQL
                                // Now store the user in sqlite
                                String uid = jObj.getString("uid");

                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user
                                        .getString("created_at");

                                // Inserting row in users table
                                db.addUser(name, email, uid, created_at);

                                Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                                // Launch login activity
                                Intent intent = new Intent(
                                        RegisterActivity.this,
                                        MainActivity_Initial.class);
                                startActivity(intent);
                                finish();
                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                       /** try {
                            //Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_LONG).show();
                            Toast.makeText(RegisterActivity.this, "Thank you for your post", Toast.LENGTH_LONG).show();

                            if (response.getBoolean("status")) {
                                pDialog.dismiss();
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", e.toString());
                        }
                        pDialog.dismiss();
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                pDialog.dismiss();
                if (isNetworkProblem(error)) {
                    Toast.makeText(RegisterActivity.this, "Internet Problem", Toast.LENGTH_SHORT).show();

                }
            }
        }) {

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    uee.printStackTrace();
                    return null;
                }
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
               // map.put("X-Device-Info","Android FOO BAR");

             //   map.put("Accept-Language", acceptLanguage);
                map.put("Content-Type", "application/json; charset=UTF-8");

                return map;
            }

        };

      //  jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(8000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);


        Volley.newRequestQueue(this).add(new JsonRequest<JSONArray>(Request.Method.POST,
                AppConfig.URL_REGISTER, jsonObject.toString(), new Response.Listener<JSONArray>() {
                                                      @Override
                                                      public void onResponse(JSONArray jsonArray) {
                                                          Log.d("response", "res-rec is" + jsonArray);
                                                          hideDialog();

                                                          if (jsonArray == null) {
                                                              pDialog.dismiss();
                                                            //  Snackbar.make(myview, "No services found", Snackbar.LENGTH_LONG).show();

                                                          } else {


                                                              for (int i = 0; i < jsonArray.length(); i++) {
                                                                  try {

                                                                      pDialog.dismiss();
                                                                      JSONObject jObj = jsonArray.getJSONObject(i);
                                                                      boolean error = jObj.getBoolean("error");
                                                                      if (!error) {
                                                                          String uid = jObj.getString("uid");

                                                                          JSONObject user = jObj.getJSONObject("user");
                                                                          String name = user.getString("name");
                                                                          String email = user.getString("email");
                                                                          String created_at = user
                                                                                  .getString("created_at");

                                                                          // Inserting row in users table
                                                                          db.addUser(name, email, uid, created_at);

                                                                          Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                                                                          // Launch login activity
                                                                          Intent intent = new Intent(
                                                                                  RegisterActivity.this,
                                                                                  MainActivity_Initial.class);
                                                                          startActivity(intent);
                                                                          finish();
                                                                      }else{

                                                                          // Error occurred in registration. Get the error
                                                                          // message
                                                                          String errorMsg = jObj.getString("error_msg");
                                                                          Toast.makeText(getApplicationContext(),
                                                                                  errorMsg, Toast.LENGTH_LONG).show();
                                                                      }
                                                                  } catch (JSONException e) {
                                                                      e.printStackTrace();
                                                                  }

                                                              }
                                                          }

                                                      }
                                                  }, new Response.ErrorListener() {
                                                      @Override
                                                      public void onErrorResponse(VolleyError volleyError) {
                                                          VolleyLog.d("Login request", "Error: " + volleyError.getMessage());
                                                          Log.d("Volley Error:", "Volley Error:" + volleyError.getMessage());
                                                          Toast.makeText(RegisterActivity.this, "Unable to connect to server, try again later", Toast.LENGTH_LONG).show();
                                                          pDialog.dismiss();
                                                      }
                                                  })

                                                  {
                                                      @Override
                                                      protected Map<String, String> getParams() throws AuthFailureError {


                                                          Map<String, String> params = new HashMap<String, String>();
                                                          // params.put("uniquesessiontokenid", "39676161-b890-4d10-8c96-7aa3d9724119");
                                                          params.put("name","j");
                                                          params.put("password", "j");
                                                          params.put("email", "j@j.j");

                                                          return super.getParams();
                                                      }

                                                      @Override
                                                      protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {


                                                          try {
                                                              String jsonString = new String(networkResponse.data,
                                                                      HttpHeaderParser
                                                                              .parseCharset(networkResponse.headers));
                                                              return Response.success(new JSONArray(jsonString),
                                                                      HttpHeaderParser
                                                                              .parseCacheHeaders(networkResponse));
                                                          } catch (UnsupportedEncodingException e) {
                                                              return Response.error(new ParseError(e));
                                                          } catch (JSONException je) {
                                                              return Response.error(new ParseError(je));
                                                          }

                                                          //  return null;
                                                      }

                                                  }


        );

         **/
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private static boolean isNetworkProblem (Object error){
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }


}
