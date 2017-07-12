package com.lind.barcodescanner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bimbask.barcodescanner.R;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.Listview_Friends_Chat_Adapter_Recycler;
import helper.SQLiteHandler;
import helper.SessionManager;
public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    ArrayList<HashMap<String, String>> arrayList_stations;
    RecyclerView list_word;
    public static Listview_Friends_Chat_Adapter_Recycler mListview_Friends_Chat_Adapter_Recycler =null;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("request",String.valueOf(requestCode));
        switch (requestCode) {
            case 127:
                mScannerView = (ZXingScannerView) findViewById(R.id.view);
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.main_initial, menu);return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreference mSharedPreference = new SharedPreference(MainActivity.this);
        if(mSharedPreference.get_isUserLoggedIn()) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            if(mListview_Friends_Chat_Adapter_Recycler==null){

                arrayList_stations = new  ArrayList<HashMap<String, String>>();

                HashMap<String, String> station = new HashMap<String, String>();
                station.put("name","Estación 1");
                station.put("status","no");
                station.put("description","Descripción Estación 1");
                arrayList_stations.add(station);
                station = new HashMap<String, String>();
                station.put("name","Estación 2");
                station.put("status","no");
                station.put("description","Descripción Estación 2");
                arrayList_stations.add(station);
                station = new HashMap<String, String>();
                station.put("name","Estación 3");
                station.put("status","no");
                station.put("description","Descripción Estación 3");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 4");
                station.put("status","no");
                station.put("description","Descripción Estación 4");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 5");
                station.put("status","Estación 1");
                station.put("description","Descripción Estación 5");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 6");
                station.put("status","no");
                station.put("description","Descripción Estación 6");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 7");
                station.put("status","no");
                station.put("description","Descripción Estación 7");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 8");
                station.put("status","no");
                station.put("description","Descripción Estación 8");
                arrayList_stations.add(station);station = new HashMap<String, String>();
                station.put("name","Estación 9");
                station.put("status","no");
                station.put("description","Descripción Estación 9");
                arrayList_stations.add(station);
                station = new HashMap<String, String>();
                station.put("name","Estación 10");
                station.put("status","no");
                station.put("description","Descripción Estación 10");
                arrayList_stations.add(station);
                station = new HashMap<String, String>();
                station.put("name","Estación 11");
                station.put("status","no");
                station.put("description","Descripción Estación 11");
                arrayList_stations.add(station);
                station = new HashMap<String, String>();
                station.put("name","Estación 12");
                station.put("status","no");
                station.put("description","Descripción Estación 12");
                arrayList_stations.add(station);
                mListview_Friends_Chat_Adapter_Recycler = new Listview_Friends_Chat_Adapter_Recycler(arrayList_stations);
            }



            information_user.set_information_user(mSharedPreference.get_unique_id(),mSharedPreference.get_name(),mSharedPreference.get_email());
            if (Build.VERSION.SDK_INT >= 23) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    list_word = (RecyclerView)findViewById(R.id.listView_chat_friend);
                    list_word.setLayoutManager(new LinearLayoutManager(this));
                    list_word.setAdapter(mListview_Friends_Chat_Adapter_Recycler);
                    mScannerView = (ZXingScannerView) findViewById(R.id.view);
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 127);


                } else {
                    list_word = (RecyclerView)findViewById(R.id.listView_chat_friend);
                    list_word.setLayoutManager(new LinearLayoutManager(this));
                    list_word.setAdapter(mListview_Friends_Chat_Adapter_Recycler);
                    mScannerView = (ZXingScannerView) findViewById(R.id.view);
                    try {
                        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                        mScannerView.startCamera();          // Start camera on resume
                    } catch (NullPointerException ee) {
                        ee.printStackTrace();
                    }
                }
            } else {
                list_word = (RecyclerView)findViewById(R.id.listView_chat_friend);
                list_word.setLayoutManager(new LinearLayoutManager(this));
                list_word.setAdapter(mListview_Friends_Chat_Adapter_Recycler);
                mScannerView = (ZXingScannerView) findViewById(R.id.view);
            }
        }else{
            Intent  intent= new Intent(MainActivity.this, MainActivity_Select_Option.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                showMessageOKCancel_one_sms(MainActivity.this, "Confirmación",
                        information_user.get_email(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout logout = new logout(MainActivity.this);
                                logout.execute();
                            }
                        },null);
                break;
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.in_back_pressed,R.anim.out_back_pressed);
                break;
        }return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreference mSharedPreference = new SharedPreference(MainActivity.this);
        if(mSharedPreference.get_isUserLoggedIn()) {
            try {Log.d("REsumen","+++++");
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();          // Start camera on resume
            } catch (NullPointerException ee) {
                ee.printStackTrace();
            }

        }else{
            Intent  intent= new Intent(MainActivity.this, MainActivity_Select_Option.class);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
        mScannerView.stopCamera();           // Stop camera on pause
    }catch(NullPointerException ee){ee.printStackTrace();}
    }

    @Override
    public void handleResult(Result rawResult) {
        create_url_list create_url_list = new create_url_list(this);
        create_url_list.execute(rawResult);

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
            Intent  MainActivity_Points= new Intent(ctx, MainActivity_Points.class);
            Log.v("TAG", params[0].getText()); // Prints scan results
            Log.v("TAG", params[0].getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
            String text=params[0].getText();
            String barcode = params[0].getBarcodeFormat().toString();
            MainActivity_Points.putExtra("text",text);
            MainActivity_Points.putExtra("barcode",barcode);
            MainActivity_Points.putExtra("unique_id",information_user.get_unique_id());
            MainActivity_Points.putExtra("name",information_user.get_name());
            MainActivity_Points.putExtra("email",information_user.get_email());
            return MainActivity_Points;}

        @Override
        protected void onPostExecute(Intent result) {
            if(result!=null){
                try{dialog.dismiss();
                }catch( java.lang.IllegalArgumentException ee){ee.printStackTrace();
                }
                ctx.startActivity(result);

            }
        }
    }

    public   class logout extends AsyncTask<Integer, Void, Integer> {
        private ProgressDialog pDialog;
        private Context ctx;
        private SessionManager session;
        private SQLiteHandler db;
        private SharedPreference mSharedPreference;
        public logout(Context ctx) {this.ctx=ctx;}
        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
        @Override
        protected void onPreExecute(){
            pDialog = new ProgressDialog(ctx);
            pDialog.setCancelable(false);
            pDialog.setMessage("Cerrando sesión ...");
            showDialog();
        }
        @Override
        protected Integer doInBackground(Integer... params)  {

            // SQLite database handler
            mSharedPreference = new SharedPreference(ctx);
            mSharedPreference.set_unique_id("");
            mSharedPreference.set_name("");
            mSharedPreference.set_email("");
            mSharedPreference.set_isUserLoggedIn(false);
            db = new SQLiteHandler(ctx);
            // Session manager
            session = new SessionManager(ctx);
            session.setLogin(false);
            db.deleteUsers();
            db.close();
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result==1){
                try{hideDialog();
                }catch( java.lang.IllegalArgumentException ee){ee.printStackTrace();
                }
                Toast.makeText(ctx, "Sessión cerrada!", Toast.LENGTH_LONG).show();
                Intent  MainActivity_Points= new Intent(ctx, MainActivity_Select_Option.class);
                startActivity(MainActivity_Points);



            }
        }
    }

    public static void showMessageOKCancel_one_sms(final Context ctx, String title, String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener okListener_) {


        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(ctx)
                .setTitle(Html.fromHtml("<font color='#077fad'>"+title+"</font>"))
                .setMessage(Html.fromHtml("<font color='#646464'>"
                        + message
                        + "</font>"))
                .setCancelable(false)
                .setPositiveButton("CERRAR SESION", okListener)
                .setNegativeButton("CANCELAR", okListener_);


        final android.support.v7.app.AlertDialog alert11 = builder1.create();

        //2. now setup to change color of the button
        alert11.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert11.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(ctx.getResources().getColor(R.color.plomo));
                alert11.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(ctx.getResources().getColor(R.color.orange_android));

            }
        });


        alert11.show();


    }
}
