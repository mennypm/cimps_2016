package comfred.cimps2016;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnSave;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSave){
            // Perform the update in the database
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Information");
            builder.setMessage("The user information has been updated.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            startActivity(new Intent(this, MainActivity.class));
        }

        if(v == btnCancel){
            // Go back to the main menu
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
