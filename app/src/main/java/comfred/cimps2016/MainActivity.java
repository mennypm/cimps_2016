package comfred.cimps2016;

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

        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(this);
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
            zxScanner = new ZXingScannerView(this);
            setContentView(zxScanner);
            zxScanner.setResultHandler(this);
            zxScanner.startCamera();
        }
    }
}
