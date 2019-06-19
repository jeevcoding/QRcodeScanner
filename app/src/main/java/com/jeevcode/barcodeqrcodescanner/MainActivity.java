package com.jeevcode.barcodeqrcodescanner;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGenBarcode, btnScanBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
       btnGenBarcode  = findViewById(R.id.btnGenBarcode);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);

        btnGenBarcode.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);//this means it is refering to the View.OnClickListener interface...
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnGenBarcode:
                startActivity(new Intent(MainActivity.this, GenerateActivity.class));
                break;
            case R.id.btnScanBarcode:
                startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
                break;
        }

    }
}
