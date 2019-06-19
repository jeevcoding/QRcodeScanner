package com.jeevcode.barcodeqrcodescanner;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateActivity extends AppCompatActivity {
    private static final String Tag = "GenerateActivity";
    ActionBar actionBar;
    private Button genrate;
    //private AdView mAdView;
    private ImageView qr_code;
    private Button reset;
    private Button save;
    private Button share;
    private EditText text;
    private static final String TAG = "GenerateActivity!";

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }


    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_generate);
        this.text = (EditText) findViewById(R.id.text);
        this.save = (Button) findViewById(R.id.save);
        this.share = (Button) findViewById(R.id.share);
        this.qr_code = (ImageView) findViewById(R.id.qr_code);
        this.genrate = (Button) findViewById(R.id.generate);
        this.reset = (Button) findViewById(R.id.reset);

        this.actionBar = getActionBar();
        if (this.actionBar != null)
        {

            this.actionBar.setDisplayShowHomeEnabled(true);
            this.actionBar.setDisplayHomeAsUpEnabled(true);

        }



        this.genrate.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view) {
                String obj = GenerateActivity.this.text.getText().toString();
                if (!obj.equals(BuildConfig.FLAVOR) && !obj.isEmpty()) {
                    try {
                        GenerateActivity.this.qr_code.setImageBitmap(new BarcodeEncoder().createBitmap(new MultiFormatWriter().encode(obj, BarcodeFormat.QR_CODE, 500, 500)));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        this.save.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {


                try {

                    Bitmap bitmap = ((BitmapDrawable) GenerateActivity.this.qr_code.getDrawable()).getBitmap();
                    if (ContextCompat.checkSelfPermission(GenerateActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        GenerateActivity.this.saveToGallery(bitmap);
                    } else if (VERSION.SDK_INT >= 23) {
                        GenerateActivity.this.getPermission(1);
                    }
                }catch (NullPointerException a)

                {
                    Toast.makeText(getApplicationContext(), "Cannot be kept empty!please generate a qrcode!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"caught an exception");

                }



            }




        });


        this.share.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {


                try {
                    Bitmap bitmap = ((BitmapDrawable) GenerateActivity.this.qr_code.getDrawable()).getBitmap();
                    if (ContextCompat.checkSelfPermission(GenerateActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        GenerateActivity.this.shareImage(bitmap);

                        //shareImage() and getPermission() are two user defined functions


                    } else if (VERSION.SDK_INT >= 23) {
                        GenerateActivity.this.getPermission(2);


                    }

                }catch (NullPointerException a)
                {
                    Toast.makeText(getApplicationContext(), "Cannot be kept empty!please generate a qrcode first!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"caught an exception");

                }



            }
        });


        this.reset.setOnClickListener(new OnClickListener()
        {
            public void onClick(View view) {
                if (GenerateActivity.this.text.getText() != null) {
                    GenerateActivity.this.text.getText().clear();
                }

            }
        });




    }




    public void saveToGallery(Bitmap bitmap)
    {
        String file = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file);
        stringBuilder.append("/QrCode");
        File file2 = new File(stringBuilder.toString());
        file2.mkdirs();
        file = Tag;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Midr value is: ");
        stringBuilder.append(file2);
        Log.i(file, stringBuilder.toString());
        int currentTimeMillis = (int) System.currentTimeMillis();
        stringBuilder = new StringBuilder();
        stringBuilder.append("Image-");
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append(".jpg");
        File file3 = new File(file2, stringBuilder.toString());
        file = Tag;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("File value is ");
        stringBuilder2.append(file3);
        Log.i(file, stringBuilder2.toString());
        if (file3.exists()) {
            file3.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
            String str = Tag;
            stringBuilder = new StringBuilder();
            stringBuilder.append("outputStream Value is ");
            stringBuilder.append(fileOutputStream);
            Log.i(str, stringBuilder.toString());
            fileOutputStream.flush();
            fileOutputStream.close();
            str = Tag;
            stringBuilder = new StringBuilder();
            stringBuilder.append("outputStream Value final is ");
            stringBuilder.append(fileOutputStream.toString());
            Log.i(str, stringBuilder.toString());
            //Toast.makeText(this, "saved", 0).show();
            Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this, "something error", 0).show();
            Toast.makeText(getApplicationContext(),"some error took place",Toast.LENGTH_SHORT).show();
        } catch (IOException e2) {
            e2.printStackTrace();
            //Toast.makeText(this, "something error", 0).show();
            Toast.makeText(getApplicationContext(),"some error!!",Toast.LENGTH_SHORT).show();
        }
    }





    public void shareImage(Bitmap bitmap) {
        String file = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file);
        stringBuilder.append("/QrCode");
        File file2 = new File(stringBuilder.toString());
        file2.mkdirs();
        file = Tag;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Midr value is: ");
        stringBuilder.append(file2);
        Log.i(file, stringBuilder.toString());
        int currentTimeMillis = (int) System.currentTimeMillis();
        stringBuilder = new StringBuilder();




        //Now i have to decide the name by which an image will be stored in the internal storage.....The next 3 lines do that
        stringBuilder.append("Image-");
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append(".jpg");

        //An example of the name of an image that is stored : Image-1870761145.jpg




        File file3 = new File(file2, stringBuilder.toString());
        file = Tag;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("File value is ");
        stringBuilder2.append(file3);
        Log.i(file, stringBuilder2.toString());


        if (file3.exists()) {
            file3.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file3);
            bitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);
            String str = Tag;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("outputStream Value is ");
            stringBuilder3.append(fileOutputStream);
            Log.i(str, stringBuilder3.toString());
            fileOutputStream.flush();
            fileOutputStream.close();
            str = Tag;
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("outputStream Value final is ");
            stringBuilder3.append(fileOutputStream);
            Log.i(str, stringBuilder3.toString());


            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", getImageContentUri(this, file3));
            Intent createChooser = Intent.createChooser(intent, "Share image");

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(createChooser);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong",Toast.LENGTH_SHORT).show();
        } catch (IOException e2) {
            e2.printStackTrace();
            Toast.makeText(this, "Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri getImageContentUri(Context context, File file) {
        String absolutePath = file.getAbsolutePath();
        String str = Tag;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("File path is ");
        stringBuilder.append(absolutePath);
        Log.i(str, stringBuilder.toString());
        Cursor query = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{absolutePath}, null);
        if (query != null && query.moveToFirst()) {
            int i = query.getInt(query.getColumnIndex("_id"));
            Uri parse = Uri.parse("content://media/external/image/jpeg/media");
            query.close();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(BuildConfig.FLAVOR);
            stringBuilder2.append(i);
            return Uri.withAppendedPath(parse, stringBuilder2.toString());
        } else if (!file.exists()) {
            return null;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", absolutePath);
            return context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }

    private void getPermission(final int i) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            new AlertDialog.Builder(this).setTitle(R.string.storage_Permission).setMessage(R.string.storage_Permission_msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(GenerateActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, i);
        }
    }

    @RequiresApi(api = 23)
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        Bitmap bitmap;
        if (i == 1) {
            if (iArr.length > 0 && iArr[0] == 0) {
                bitmap = ((BitmapDrawable) this.qr_code.getDrawable()).getBitmap();
                if (bitmap != null) {
                    saveToGallery(bitmap);
                }
            } else if (iArr[0] != 0 && !shouldShowRequestPermissionRationale(strArr[0])) {
                Toast.makeText(this, "Go to Setting and give storage permission",Toast.LENGTH_SHORT).show();
            }
        } else if (i != 2) {
        } else {
            if (iArr.length > 0 && iArr[0] == 0) {
                bitmap = ((BitmapDrawable) this.qr_code.getDrawable()).getBitmap();
                if (bitmap != null) {
                    shareImage(bitmap);
                }
            } else if (iArr[0] != 0 && !shouldShowRequestPermissionRationale(strArr[0])) {
                Toast.makeText(this, "Go to Setting and give storage permission",Toast.LENGTH_SHORT).show();
            }
        }
    }





}

