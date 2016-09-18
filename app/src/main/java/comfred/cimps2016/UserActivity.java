package comfred.cimps2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtName;
    private EditText txtAfiliation;
    private EditText txtPay;
    private Switch switchGaffete;
    private Button btnSave;
    private Button btnCancel;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtName = (EditText) findViewById(R.id.txtName);
        txtAfiliation = (EditText) findViewById(R.id.txtAfiliation);
        txtPay = (EditText) findViewById(R.id.txtPay);
        switchGaffete = (Switch) findViewById(R.id.switchGaffete);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.KEY_CIMPER_ID);
        getCimper();
    }

    private void getCimper(){
        // Retrieve information from the assistant
        class GetCimper extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(UserActivity.this, "Obteniendo...", "Espere...", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                showCimper(s);
            }

            @Override
            protected String doInBackground(Void... params){
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_CIMPER, id);
                return s;
            }
        }

        GetCimper cimperC = new GetCimper();
        cimperC.execute();
    }

    private void showCimper(String json){
        try{
            // Retrieve assistant information
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            String name = c.getString(Config.TAG_NAME);
            String afiliation = c.getString(Config.TAG_AFIL);
            String gaffete = c.getString(Config.TAG_GAFFETE);
            String accept = c.getString(Config.TAG_ACCEPT);

            Log.d(name, "NOMBRE OBTENIDO");
            Log.d(afiliation, "AFILIACION OBTENIDA");
            Log.d(gaffete, "GAFFETE OBTENIDO");
            Log.d(accept, "PAGO OBTENIDO");

            if(name.equals("null")){
                // Could not retrieve user or this one does not exists
                Toast.makeText(UserActivity.this, "No pudimos obtener datos del asistente. Intente de nuevo.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            }else{
                // Display information from the assistant
                txtName.setText(name);
                txtAfiliation.setText(afiliation);
                if(accept.equals("1")){
                    txtPay.setText("Efectuado");
                }else{
                    txtPay.setText("NO efectuado");
                }
                if(gaffete.equals("1")){
                    switchGaffete.setChecked(true);
                }else{
                    switchGaffete.setChecked(false);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setCimper(){
        final String gaf;
        if (switchGaffete.isChecked()){
            gaf = "1";
        }else{
            gaf = "0";
        }

        class SetCimper extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(UserActivity.this, "Registrando...", "Espere...", false, false);
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(UserActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_CIMPER_GAFFETE, gaf);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_SET_CIMPER, hashMap);

                return s;
            }
        }

        SetCimper setC = new SetCimper();
        setC.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == btnSave){
            // Perform the update in the database
            setCimper();

            // Send alert for successful update and go back to main menu
            startActivity(new Intent(this, MainActivity.class));
        }

        if(v == btnCancel){
            // Go back to the main menu
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
