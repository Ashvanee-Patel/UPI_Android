package com.ashvanee.upi.Common;


import com.ashvanee.upi.Model.Token_Res;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

//    https://ashvaneepatel.000webhostapp.com/ashvaneepatel/public_html/paytmallinone/init_Transaction.php

    // method,, return type ,, secondary url
    @Multipart
    @POST("paytmallinone/init_Transaction.php")
    Call<Token_Res> generateTokenCall(
            @Part("code") RequestBody language,
            @Part("MID") RequestBody mid,
            @Part("ORDER_ID") RequestBody order_id,
            @Part("AMOUNT") RequestBody amount
    );
}