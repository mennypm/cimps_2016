package comfred.cimps2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Workshop extends AppCompatActivity implements View.OnClickListener {

    private TextView txtName;
    private Button btnSave;
    private Button btnCancel;
    private LinearLayout linearLayout;
    String id;
    private String eventos;
    private String asistencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);

        txtName = (TextView) findViewById(R.id.txtNameWorkshop);
        btnSave = (Button) findViewById(R.id.btnSaveWorkshop);
        btnCancel = (Button) findViewById(R.id.btnCancelWorkshop);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.checkboxes);
        Intent intent = getIntent();
        id = intent.getStringExtra(Config.KEY_CIMPER_ID);
        String json = intent.getStringExtra("json");
        addCheckboxes(json);
    }

    public void addCheckboxes(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String name = c.getString(Config.TAG_NAME);
            txtName.setText(name);
            eventos = "";
            for (int i = 1; i < result.length(); i++) {
                JSONObject row = result.getJSONObject(i);
                CheckBox checkBox = new CheckBox(this);
                // TODO: Maybe to change to String and viceversa
                checkBox.setId(Integer.parseInt(row.getString("id_evento")));
                eventos += row.getString("id_evento") + "|";
                checkBox.setText(row.getString("nombre_externo"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 30);
                checkBox.setLayoutParams(params);
                if (row.getInt("asistencia") == 1) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
                linearLayout.addView(checkBox);
                Log.d("eventos", checkBox.getId() + "  ");
            }
            eventos = eventos.substring(0, eventos.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCimper() {

        class SetCimper extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Workshop.this, "Actualizando...", "Espere...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Workshop.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_CIMPER_ID, id);
                hashMap.put("id_evento", eventos);
                hashMap.put("asistencia", asistencias);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Config.URL_SET_WORKSHOP, hashMap);
                return s;
            }
        }

        SetCimper setC = new SetCimper();
        setC.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            // Perform the update in the database
            asistencias = "";
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View view = linearLayout.getChildAt(i);
                if (view instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox.isChecked()) {
                        asistencias += "1|";
                    } else {
                        asistencias += "0|";
                    }
                }
            }
            asistencias = asistencias.substring(0, asistencias.length() - 1);
            Log.d("asistencias", asistencias);
            Log.d("eventos", eventos);
            setCimper();
            // Send alert for successful update and go back to main menu
            startActivity(new Intent(this, MainActivity.class));
        }
        if (v == btnCancel) {
            // Go back to the main menu
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
