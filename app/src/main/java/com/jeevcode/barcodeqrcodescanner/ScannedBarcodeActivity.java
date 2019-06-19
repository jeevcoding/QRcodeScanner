package com.jeevcode.barcodeqrcodescanner;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final String TAG = "ScannedBarcodeActivity!";
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {
                    if (isEmail)
                        startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));//email_address is the key and intentData is the value.We are putting data in the bundle in key value pair.
                    else {


                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));//this line tells the android system to view whatever is there in the intentData
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "NO URL DETECTED!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "NOTHING AVAILABLE TO HANDLE!!!");
                            cameraSource.stop();
                            generateAlertBox();


                        }

                        // above we have created an implicit intent,Action_view ids to display stuff on web page.


                    }
                }


            }
        });
    }

    private void initialiseDetectorsAndSources()
    {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)//we can create BarcodeDetector instances using BarcodeDetector.Builder
                .setBarcodeFormats(Barcode.ALL_FORMATS)//BarcodeDetector is going to search for barcodes in every supported format.
                .build();


        //Below,in order to fetch the stream of images from the devices camera,and display them in the
        // surface view,we need to create an instance of CameraSource class.
        //now this CameraSource needs a Barcode detector to be able to detect the barcodes...we create
        // one barcodeDetector and attach it to the CameraSource.


        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)  //We can adjust the dimensions of the camera preview using SETREQUESTEDPREVIEWSIZE()
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        //Below,we add a callback to the SurfaceHolder of the SurfaceView,so that we know when we should start drawing the preview frames.

        //the callback has 3 methods:
        // 1. surfaceCreated()
        //2. surfaceChanged()
        //3. surfaceDestroyed()

        // 1. inside surfaceCreated,check if the permissions are granted and if granted,then call the start() method of cameraSource.
        //Because the start method expects you to handle an IOexception,we should call the start method from inside the try catch block.






        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { //context is for accessing the resources,and the second parameter is the permission to check.
                        cameraSource.start(surfaceView.getHolder());

                        //Above,we start the holder for the surface view.i.e we start fetching the stream of images from the camera.

                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);//if permission for camera has not been granted..prompt the user for that permission.

                            // The requestPermissions(): we pass the context,the list of permissions we
                        // want to ask and the 3rd parameter is the integer request code that you specify to
                        // identify this permission request.
                        //The callback method will get the result of the permission request.
                        //After the user responds to the prompt,the android system calls the app's callback method
                        // with the results,passing the same request code that the app passed to request_Permissions()...i.e REQUEST_CAMERA_PERMISSIONS.
                        //The REQUEST_CAMERA_PERMISSION integer is an integer to identify the permission request.(it acts as a unique identifier).

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)

            {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();

                //to stop fetching the preview frames,i.e images from the camera.


            }
        });



        //Below,we need to tell the BarcodeDetector what it should do when it detects a qr code.
        //setProcessor() is a method of the BarcodeDetector class.
        //we create an instance of a class that implements the Detector.Processor interface and pass it to setProcessor().
        //After doing this android studio will automaticaly give you the methods to be implemented i.e void release() and receiveDetections().

        //inside the receiveDetections,we create sparseArray of barcode objects by calling getDetectedItems() method of
        // the Detector.Detections class.





        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
        {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)

            {


                final SparseArray<Barcode> barcodes = detections.getDetectedItems();//the barcode recognition results are returned here..by the getDetecteditems()
                //the sparseASrray is going to have all the qr codes the barcodeDetecter detected.

                if (barcodes.size() != 0)

                {


                    //Note that the call to setText() method should be inside post() method of the textView,because receiveDetections does
                    // not run on the UI thread.







                    txtBarcodeValue.post(new Runnable()
                    {



                        @Override
                        public void run()
                        {



                            if (barcodes.valueAt(0).email != null) {


                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;//barcodes is an array defined above....

                                txtBarcodeValue.setText(intentData);

                                //the above setText() should be inside post().

                                isEmail = true;

                                btnAction.setText("EMAIL ID DETECTED:SEND CONTENT THROUGH EMAIL");

                            } else {
                                isEmail = false;

                                btnAction.setText("LAUNCH URL");

                                intentData = barcodes.valueAt(0).displayValue;//barcodes is an array defined above....

                                //each item of sparse array contains a barcode object.
                                //to fetch the raw contents of the qr code,you can use the Barcode objects's "rawValue" field.
                                //displayValue is another  field to fetch the contents of the qr code.

                                txtBarcodeValue.setText(intentData);


                                //the above setText() should be inside post().

                            }



                        }


                    });





                }




            }


        });




    }


    void generateAlertBox(){

        new AlertDialog.Builder(ScannedBarcodeActivity.this).setTitle("Scanned Result")
                .setMessage(intentData)
                .setPositiveButton("COPY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ClipboardManager manager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                        ClipData data=ClipData.newPlainText("Result",intentData);

                        manager.setPrimaryClip(data);



                        try {
                            if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { //context is for accessing the resources,and the second parameter is the permission to check.
                                cameraSource.start(surfaceView.getHolder());

                                //Above,we start the holder for the surface view.i.e we start fetching the stream of images from the camera.

                            } else {
                                ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);//if permission for camera has not been granted..prompt the user for that permission.

                                // The requestPermissions(): we pass the context,the list of permissions we
                                // want to ask and the 3rd parameter is the integer request code that you specify to
                                // identify this permission request.
                                //The callback method will get the result of the permission request.
                                //After the user responds to the prompt,the android system calls the app's callback method
                                // with the results,passing the same request code that the app passed to request_Permissions()...i.e REQUEST_CAMERA_PERMISSIONS.
                                //The REQUEST_CAMERA_PERMISSION integer is an integer to identify the permission request.(it acts as a unique identifier).

                            }

                        } catch (IOException s) {
                            s.printStackTrace();
                        }







                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();





                        try {
                            if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { //context is for accessing the resources,and the second parameter is the permission to check.
                                cameraSource.start(surfaceView.getHolder());

                                //Above,we start the holder for the surface view.i.e we start fetching the stream of images from the camera.

                            } else {
                                ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);//if permission for camera has not been granted..prompt the user for that permission.

                                // The requestPermissions(): we pass the context,the list of permissions we
                                // want to ask and the 3rd parameter is the integer request code that you specify to
                                // identify this permission request.
                                //The callback method will get the result of the permission request.
                                //After the user responds to the prompt,the android system calls the app's callback method
                                // with the results,passing the same request code that the app passed to request_Permissions()...i.e REQUEST_CAMERA_PERMISSIONS.
                                //The REQUEST_CAMERA_PERMISSION integer is an integer to identify the permission request.(it acts as a unique identifier).

                            }

                        } catch (IOException s) {
                            s.printStackTrace();
                        }




                    }
                })
                .create().show();



        //*********************************************************************************************
    }



    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initialiseDetectorsAndSources();

       /* try {
            if (intentData != null) {

                Toast.makeText(getApplicationContext(),"the value is"+intentData,Toast.LENGTH_SHORT).show();
                generateAlertBox();
            }
        }catch (NullPointerException e)
        {
            Log.d(TAG,"********The intent data is empty");
            Toast.makeText(getApplicationContext(),"the qr code is empty or has a space",Toast.LENGTH_SHORT).show();
        }
        */

    }
}
