package com.jeevcode.barcodeqrcodescanner;



import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.pp:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.termsfeed.com/privacy-policy/139eccf173f5e239085d2eed4a09fdf7")));
                Toast.makeText(getApplicationContext(),"selected privacy policy",Toast.LENGTH_SHORT).show();
                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }

    }
}
