package comfred.cimps2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
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
    private EditText txtCategory;
    private EditText txtPay;
    private Switch switchGaffete;
    private Button btnSave;
    private Button btnCancel;

    private String id;
    private Boolean isPayAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        txtName = (EditText) findViewById(R.id.txtName);
        txtAfiliation = (EditText) findViewById(R.id.txtAfiliation);
        txtCategory = (EditText) findViewById(R.id.txtCategory);
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
            String category = c.getString(Config.TAG_CATEGORY);
            String gaffete = c.getString(Config.TAG_GAFFETE);
            String accept = c.getString(Config.TAG_ACCEPT);

            // The payment information value
            Log.d(accept, "THE ACCEPTED VALUE FROM ORDER");

            // Display information from the assistant
            txtName.setText(name);
            txtAfiliation.setText(afiliation);
            txtCategory.setText(category);
            if(accept.equals("1")){
                txtPay.setText("Efectuado");
                isPayAccepted = true;
            }else if (accept.equals("0")){
                txtPay.setText("NO efectuado");
                isPayAccepted = false;
            }else{
                txtPay.setText("No se pudo recuperar info");
            }
            if(gaffete.equals("1")){
                switchGaffete.setChecked(true);
            }else{
                switchGaffete.setChecked(false);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setCimper(){
        final String name = txtName.getText().toString().trim();
        final String afiliation = txtAfiliation.getText().toString().trim();
        final String gaffete;
        final String accept;

        if (switchGaffete.isChecked()){
            gaffete = "1";
        }else{
            gaffete = "0";
        }

        if (isPayAccepted){
            accept = "1";
        }else{
            accept = "0";
        }

        class SetCimper extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(UserActivity.this, "Actualizando...", "Espere...", false, false);
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
                hashMap.put(Config.KEY_CIMPER_ID, id);
                hashMap.put(Config.KEY_CIMPER_NAME, name);
                hashMap.put(Config.KEY_CIMPER_AFIL, afiliation);
                hashMap.put(Config.KEY_CIMPER_GAFFETE, gaffete);
                hashMap.put(Config.KEY_CIMPER_ACCEPT, accept);

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
