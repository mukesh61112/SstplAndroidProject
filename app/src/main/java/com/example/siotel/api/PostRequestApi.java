package com.example.siotel.api;

import com.example.siotel.models.HRHDetailsModel;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.HouseholdsDetailsModel;
import com.example.siotel.models.LoginModel;
import com.example.siotel.models.LoginResponseModel;
import com.example.siotel.models.PayModel;
import com.example.siotel.models.RozarPayResponse;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface PostRequestApi {



    //http://meters.siotel.in:8000/auth/




    @POST("auth/")
  Call<LoginResponseModel> LogintoServer(@Body LoginModel loginModel);
   // Call<Token> LogintoServer(@Body PostModel postModel);
   // Call<Token> getLoginResponse( @Body PostModel postModel);



    //http://meters.siotel.in:8000/houseListApi
//    @GET("houseListApi")
//    Call<List<HouseholdsModel>> getAllMeter(@Header("Authorization") String token);
    @POST("houseListApi/")
    Call<List<HouseholdsModel>> getAllMeter(@Header("Authorization") String token, @Body SaveEmail saveEmail);

    //http://meters.siotel.in:8000/houseListApi/506f9800000120e3

    @GET()
    Call<List<HouseholdsDetailsModel>> getMetersDtl(@Url String s, @Header("Authorization") String token);
    @GET("logoutApi/")
    Call<Token> logoutApp(@Header("Authorization")String token);

  @GET()
  Call<List<HRHDetailsModel>> getHouseholdsRechargeHistory(@Url String s, @Header("Authorization") String token);
  @POST("RechargesumApi/")
  Call<Integer>getTotalRecharge(@Header("Authorization") String token, @Body SaveEmail saveEmail);

  @POST("payments/MeterRechargeApi")
  Call<RozarPayResponse> getRzPay(@Body PayModel payModel);


}
