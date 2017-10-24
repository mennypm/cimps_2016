package comfred.cimps2017;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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

        zxScanner = new ZXingScannerView(this);
        zxScanner.setResultHandler(this);

        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(this);

        // Requesting the permission for the camera
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    Config.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void handleResult(Result result) {
        // Take the user id from the QR code and start the activity to see its data
        zxScanner.stopCamera();
        Intent intent = new Intent(getBaseContext(), UserActivity.class);
        intent.putExtra(Config.KEY_CIMPER_ID, result.getText());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        // Scan a new assistant QR code
        if(v == btnScanQR){
            setContentView(zxScanner);
            zxScanner.startCamera();
        }
    }
}
