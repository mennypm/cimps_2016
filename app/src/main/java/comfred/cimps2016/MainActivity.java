package comfred.cimps2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, View.OnClickListener{
    private ZXingScannerView zxScanner;
    private Button btnScanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(this);
    }

    @Override
    public void handleResult(Result result) {
        // Take the user ID and make a request to the database server
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información del Usuario");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Go back to the main menu
        zxScanner.stopCamera();
        startActivity(new Intent(this, UserActivity.class));
    }

    @Override
    public void onClick(View v) {
        if(v == btnScanQR){
            zxScanner = new ZXingScannerView(this);
            setContentView(zxScanner);
            zxScanner.setResultHandler(this);
            zxScanner.startCamera();
        }
    }
}
