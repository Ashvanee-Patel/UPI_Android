package com.ashvanee.upi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.ashvanee.upi.Common.Common;
import com.ashvanee.upi.databinding.ActivityUpiBinding;

import java.util.ArrayList;

public class UpiActivity extends AppCompatActivity {

    ActivityUpiBinding binding;
    Context context = UpiActivity.this;
    final int UPI_PAYMENT = 123;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendBtnUpi.setOnClickListener(view -> {
            if( ! binding.amountEtUpi.getText().toString().isEmpty() && ! binding.nameEtUpi.getText().toString().isEmpty()
            && ! binding.noteEtUpi.getText().toString().isEmpty() && ! binding.upiIdEtUpi.getText().toString().isEmpty()){
                String amount,note,name,upiId;
                amount = binding.amountEtUpi.getText().toString().trim();
                note  = binding.noteEtUpi.getText().toString().trim();
                name = binding.nameEtUpi.getText().toString().trim();
                upiId = binding.upiIdEtUpi.getText().toString().trim();

                payUsingUPI(amount,upiId,name,note);

            }else {
                Toast.makeText(context, "Please fill the all fields..", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void payUsingUPI(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();
        /*Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount )
                  .appendQueryParameter("cu", "INR")
                .build();*/

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        //Will Always show a dialog to user to choose an app

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay with...");
        //check if intent resolves

        if(chooser.resolveActivity(getPackageManager()) != null){
                startActivityForResult(chooser,UPI_PAYMENT);
        }else {
            Toast.makeText(context,"No UPI Application found, please install and then try again",Toast.LENGTH_LONG).show();
        }


        /*Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", upiId)
                        .appendQueryParameter("pn", "RAM NARAYAN PHOOLCHAND")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", "123456789")
                        .appendQueryParameter("tn", "Application Testing")
                        .appendQueryParameter("am", "1.00")
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, UPI_PAYMENT);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case UPI_PAYMENT:
                System.out.println("UPI_PAYMENT resultCode " + resultCode +" requestCode "+requestCode);
                if(RESULT_OK == resultCode || requestCode == 11){
                    if(data != null){
                        String txt = data.getStringExtra("response");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(txt);
                        System.out.println("UPI_PAYMENT txt " + txt );
                        upiPaymentDataOperation(dataList);
                    }else {
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        System.out.println("UPI_PAYMENT nothing ");
                        upiPaymentDataOperation(dataList);
                    }
                }else {
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    System.out.println("UPI_PAYMENT nothing last");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> dataList) {

        if(Common.isConnectionAvailable(context)){
            String str = dataList.get(0);
            String paymentCancel = "";
            if(str == null){
                    str = "discard";
            }
            String status = "";
            String approvalReferenceNumber = "";
            String []response = str.split("&");
            for (String s : response) {
                System.out.println("Response " + s);
                String[] equalStr = s.split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("approval Ref".toLowerCase()) ||
                            equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalReferenceNumber = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment Cancelled by user";
                }
            }

            //----------------------

            if(status.equals("success")){
                    Toast.makeText(context,"Transaction Successful  "+approvalReferenceNumber,Toast.LENGTH_LONG).show();
            }
            else if ("Payment Cancelled by user".equals(paymentCancel)){
                Toast.makeText(context,"Payment Cancelled by user..",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context,"Transaction failed, please try again...",Toast.LENGTH_LONG).show();
            }


        }else {
            Toast.makeText(context,"Please enable internet then try again",Toast.LENGTH_LONG).show();
        }
    }
}












