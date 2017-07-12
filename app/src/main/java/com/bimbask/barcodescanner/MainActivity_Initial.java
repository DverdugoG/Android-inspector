package com.bimbask.barcodescanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class MainActivity_Initial extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout input_layout_editText_name;
    TextInputLayout   input_layout_editText_password;
     EditText inputName;
   // private static final String TAG = RegisterActivity.class.getSimpleName();
  //  private Button btnLogin;
  //  private Button btnLinkToRegister;
   // private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog = null;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_initial);
        input_layout_editText_name = (TextInputLayout) findViewById(R.id.input_layout_editText_name);
        input_layout_editText_password  = (TextInputLayout) findViewById(R.id.input_layout_editText_password);
        inputName = (EditText) findViewById(R.id.editText_name);
        inputPassword = (EditText) findViewById(R.id.editText_password);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btnRegister =(Button)findViewById(R.id.btnRegister);
        btn_login.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        // Progress dialog

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
       //     Intent intent = new Intent(this, MainActivity_Initial.class);
         //   startActivity(intent);
           // finish();
        }


        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    inputPassword.requestFocus();
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitForm(v.getRootView().getContext());
                    return true;
                }
                return false;
            }
        });
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
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn_login:
                submitForm(v.getRootView().getContext());
                break;

            case R.id.btnRegister:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    public static  class create_url_list extends AsyncTask<Result, Void, Intent> {
        private AlertDialog dialog;
        private AlertDialog.Builder builder ;
        private Context ctx;
        public create_url_list(Context ctx) {this.ctx=ctx;}
        @Override
        protected void onPreExecute(){
            builder = new AlertDialog.Builder(ctx);
            builder.setMessage("verificando codigo");
            builder.setCancelable(false);
            dialog = builder.create();
            dialog.show();
        }
        @Override
        protected Intent doInBackground(Result... params)  {
            Intent  MainActivity_Points= new Intent(ctx, MainActivity.class);
              return MainActivity_Points;}

        @Override
        protected void onPostExecute(Intent result) {
            if(result!=null){
                try{dialog.dismiss();
                }catch( IllegalArgumentException ee){ee.printStackTrace();
                }
                ctx.startActivity(result);

            }
        }
    }

    /**
     * Validating form
     */
    private void submitForm(Context ctx) {
        if (!validateName()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
      //  Toast.makeText(this, "Thank You!", Toast.LENGTH_SHORT).show();
        String email = inputName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        // login user
        checkLogin(ctx,email, password);
       // create_url_list create_url_list = new create_url_list(this);
       // create_url_list.execute();

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            input_layout_editText_name.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            input_layout_editText_name.setErrorEnabled(false);
        }

        return true;
    }

    /**private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }**/

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            input_layout_editText_password.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            input_layout_editText_password.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(Context ctx,final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        try {if(pDialog==null){pDialog = new ProgressDialog(ctx);}
            pDialog.setCancelable(false);
            pDialog.setMessage("Iniciando sesión ...");
        }catch(NullPointerException ee){ ee.printStackTrace();
            pDialog = new ProgressDialog(ctx);
            pDialog.setCancelable(false);
            pDialog.setMessage("Iniciando sesión ...");
        }
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {

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
                        session.setLogin(true);
                        // Now store the user in SQLite
                        String unique_id = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        // Inserting row in users table
                        db.addUser(name, email, unique_id, created_at);
                        SharedPreference mSharedPreference = new SharedPreference(MainActivity_Initial.this);
                        mSharedPreference.set_isUserLoggedIn(true);
                        mSharedPreference.set_email(email);
                        mSharedPreference.set_name(name);
                        mSharedPreference.set_unique_id(unique_id);
                        // Launch main activity
                        information_user.set_information_user(unique_id,name,email);

                        Intent  intent= new Intent(MainActivity_Initial.this, MainActivity_Select_Option.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), "Problema de conexión, intentar denuevo", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),  "Problema de conexión, intentar denuevo", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),  "Problema de conexión, intentar denuevo", Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
      try {
          if (!pDialog.isShowing())
              pDialog.show();
      }catch(NullPointerException ee){ee.printStackTrace();}catch (IllegalArgumentException ee){ee.printStackTrace();}
    }

    private void hideDialog() {
       try{ if (pDialog.isShowing())
            pDialog.dismiss(); }catch(NullPointerException ee){ee.printStackTrace();
       }catch (IllegalArgumentException ee){ee.printStackTrace();}
    }
    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view) {this.view = view;}
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_name:
                    validateName();
                    break;
                //case R.id.input_email:
                //   validateEmail();
                //  break;
                case R.id.editText_password:
                    validatePassword();
                    break;
            }
        }
    }
}
