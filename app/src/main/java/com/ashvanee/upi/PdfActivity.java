package com.ashvanee.upi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ashvanee.upi.databinding.ActivityPdfBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfActivity extends AppCompatActivity {
    public static final int REQUEST_WRITE_STORAGE = 112;
    ActivityPdfBinding binding;
        int pageWidth = 1200, pageHeight =2010;
        String name,phone,item1Name,item2Name,item1quantity,item2quantity,Tag = "Ashvanee";
        Bitmap bitmap,scaledBitmap;
        Date date;
        DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED );

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.idc_flash);
//        scaledBitmap = Bitmap.createScaledBitmap(bitmap,482,74,false);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap,pageWidth,100,false);

        binding.generateBtnPdf.setOnClickListener(view -> {
            name = binding.userNameEtPdf.getText().toString();
            phone = binding.userPhoneEtPdf.getText().toString();
            item1Name = binding.item1NameETPdf.getText().toString();
            item2Name = binding.item2NameETPdf.getText().toString();
            item1quantity = binding.item1QuantityETPdf.getText().toString();
            item2quantity = binding.item2QuantityETPdf.getText().toString();

            if(name.isEmpty() || phone.isEmpty() || item1Name.isEmpty() || item2Name.isEmpty() || item1quantity.isEmpty() || item2quantity.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }else {
                requestPermission(this);
            }
        });


    }


    private void requestPermission(Activity context) {
        boolean hasPermission = (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // You are allowed to write external storage:
            createPdf();
        }
    }



    private void createPdf() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint  = new Paint();
        Paint titlePaint  = new Paint();
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight,1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        canvas.drawBitmap(scaledBitmap,0,100,myPaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(70);
        canvas.drawText("Grocery App",pageWidth/2,300,titlePaint);

        myPaint.setColor(Color.BLUE);
        myPaint.setTextSize(30f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Call: +91,805,210,2964",pageWidth-20,40,myPaint);
        canvas.drawText("+91,000,111,2222",pageWidth-20,80,myPaint);

        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
        canvas.drawText("Invoice",pageWidth/2,400,titlePaint);

        date = new Date();
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Invoice Number: 123456789",pageWidth-20,500,myPaint);
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Date: "+dateFormat.format(date),pageWidth-20,550,myPaint);
        dateFormat = new SimpleDateFormat("HH:mm:SS");
        canvas.drawText("Time: "+dateFormat.format(date),pageWidth-20,600,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(35f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Customer Name: "+name,20,500,myPaint);
        canvas.drawText("Contact Number: "+phone,20,550,myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(20,780,pageWidth-20,860,myPaint);


        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Sr. No.", 40,830,myPaint);
        canvas.drawText("Item Description",200,830,myPaint);
        canvas.drawText("Price",700,830,myPaint);
        canvas.drawText("Qty",900,830,myPaint);
        canvas.drawText("Total",1050,830,myPaint);

        canvas.drawLine(180,790,180,840,myPaint);
        canvas.drawLine(680,790,680,840,myPaint);
        canvas.drawLine(880,790,880,840,myPaint);
        canvas.drawLine(1030,790,1030,840,myPaint);

        float item1Price = 200, item2Price =400;
        canvas.drawText("1",40,950,myPaint);
        canvas.drawText(item1Name,200,950,myPaint);
        canvas.drawText(String.valueOf(item1Price),700,950,myPaint);
        canvas.drawText(item1quantity,900,950,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(Integer.parseInt(item1quantity)*item1Price),pageWidth-40,950,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("2",40,1050,myPaint);
        canvas.drawText(item2Name,200,1050,myPaint);
        canvas.drawText(String.valueOf(item2Price),700,1050,myPaint);
        canvas.drawText(item2quantity,900,1050,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(Integer.parseInt(item2quantity)*item2Price),pageWidth-40,1050,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        float subTotal = Integer.parseInt(item1quantity)*item1Price + Integer.parseInt(item2quantity)*item2Price;
        canvas.drawLine(pageWidth-550,1200,pageWidth-20,1200,myPaint);
        canvas.drawText("Sub Total",700,1250,myPaint);
        canvas.drawText(":",900,1250,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(subTotal),pageWidth-40,1250,myPaint);


        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("GST",700,1300,myPaint);
        canvas.drawText(":",900,1300,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("12%",pageWidth-40,1300,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setColor(Color.rgb(247,147,30));
        canvas.drawRect(pageWidth-550,1350,pageWidth-20,1450,myPaint);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextSize(50f);
        myPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Total",700,1415,myPaint);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(subTotal+subTotal*12/100),pageWidth-40,1415,myPaint);


        myPdfDocument.finishPage(myPage1);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UPI APP Grocery";
        File file = new File(path);
        if (!file.exists() && !file.mkdirs()) {
            // This should never happen - log handled exception!
            Toast.makeText(this, "Sorry error on Directory Creation", Toast.LENGTH_SHORT).show();
        }
        else {
            File fileName = new File(path,"Hello.pdf");
            try {
                myPdfDocument.writeTo(new FileOutputStream(fileName));
                Toast.makeText(this, "Congratulation Check In your External Storage", Toast.LENGTH_SHORT).show();
                Log.d(Tag,"fileName  "+file.toString());
            } catch (Exception e) {
                Log.d(Tag,"Exception  "+e.toString());
                Toast.makeText(this, "Sorry error on file creation", Toast.LENGTH_SHORT).show();
            }
        }
        myPdfDocument.close();
    }
}