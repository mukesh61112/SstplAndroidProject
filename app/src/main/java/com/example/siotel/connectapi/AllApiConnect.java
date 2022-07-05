package com.example.siotel.connectapi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.siotel.SharedPrefManager;
import com.example.siotel.adapters.HouseholdsDetailsAdapter;
import com.example.siotel.adapters.RechargeHisDetailsAdapter;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.HRHDetailsModel;
import com.example.siotel.models.HouseholdsDetailsModel;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;
import com.example.siotel.sqlitedatabase.HouseholdDatabase;
import com.example.siotel.sqlitedatabase.MyDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllApiConnect {

    public void housholdApi(Context context)
    {
         MyDatabase myDatabase=new MyDatabase(context);
         SharedPrefManager sharedPrefManager=new SharedPrefManager(context);



        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();






        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<List<HouseholdsModel>> call = requestApi.getAllMeter(tokenstr,saveEmail);

        call.enqueue(new Callback<List<HouseholdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HouseholdsModel>> call, @NonNull Response<List<HouseholdsModel>> response) {
                if (response.isSuccessful()) {
                    // Toast.makeText(getContext()," response mila h ",Toast.LENGTH_LONG).show();
                    Log.v("haha"," have no error");
                    List<HouseholdsModel> dlist = response.body();
                    if(myDatabase.getHouseholdCount()<dlist.size())
                    {
                        int old=myDatabase.getHouseholdCount();
                        int nw=dlist.size();

                        for(int i=old;i<nw;i++)
                        {
                            String date=dlist.get(i).getDate();
                            String arr[]=date.split("[T]");
                            myDatabase.addHouseHold(dlist.get(i).getHouseholdname(),dlist.get(i).getMetersno(),arr[0]);
                        }
                    }
                }
                else
                {
                    Toast.makeText(context," not connect household api  ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HouseholdsModel>> call, Throwable t) {
                Log.v("err",t.toString());
            }
        });

    }
    public void householdDetailsApi(Context context,String meterno){


        MyDatabase myDatabase=new MyDatabase(context);
        SharedPrefManager sharedPrefManager=new SharedPrefManager(context);



        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        String url="houseListApi/"+meterno;
        Call<List<HouseholdsDetailsModel>> call = requestApi.getMetersDtl(url,tokenstr);

        call.enqueue(new Callback<List<HouseholdsDetailsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HouseholdsDetailsModel>> call, @NonNull Response<List<HouseholdsDetailsModel>> response) {
                if (response.isSuccessful()) {
                   // Toast.makeText(context," meter response mila h ",Toast.LENGTH_LONG).show();
                    Log.v("mhaha"," meter have no error");

                    List<HouseholdsDetailsModel> responselist=response.body();

                    if(myDatabase.getHouseholdDetailsCount(meterno)<responselist.size())
                    {
                        int old=myDatabase.getHouseholdDetailsCount(meterno);
                        int nw=responselist.size();

                        for(int i=old;i<nw;i++)
                        {

                           myDatabase.addHouseHoldDetails(responselist.get(i).getMeterSN(),responselist.get(i).getCum_eb_kwh(),
                                                           responselist.get(i).getBalance_amount(),responselist.get(i).getDate());

                        }
                    }
                  /*  List<HouseholdsDetailsModel> dlist1 = response.body();

                    for (HouseholdsDetailsModel d : dlist1) {

                       myDatabase.addHouseHoldDetails(d.getMeterSN(),d.getCum_eb_kwh(),d.getBalance_amount(),d.getDate());
                    }


                    List<HouseholdsDetailsModel> dlist = response.body();
                    for (HouseholdsDetailsModel d : dlist) {

                        myDatabase.addHouseHoldDetails(d.getMeterSN(),d.getCum_eb_kwh(),d.getBalance_amount(),d.getDate());
                    } */


                }
                else
                {
                    Toast.makeText(context," meter details nahi mili ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HouseholdsDetailsModel>> call, Throwable t) {
                Log.v("merr",t.toString());
            }
        });


    }
    public void householdRechargeDetailsApi(Context context,String meterno)
    {
        MyDatabase myDatabase=new MyDatabase(context);
        SharedPrefManager sharedPrefManager=new SharedPrefManager(context);


        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);

        String url="RechargeApi/"+meterno;

        Call<List<HRHDetailsModel>> call=requestApi.getHouseholdsRechargeHistory(url,tokenstr);

        call.enqueue(new Callback<List< HRHDetailsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List< HRHDetailsModel>> call, @NonNull Response<List< HRHDetailsModel>> response) {
                if (response.isSuccessful()) {
                  //  Toast.makeText(context," kuch recharge details  mili h ",Toast.LENGTH_LONG).show();
                    Log.v("mhaha"," meter have no error");
                    List<HRHDetailsModel> responselist = response.body();
//                    List< HRHDetailsModel> dlist2=new ArrayList<>();
//                    dlist2.add(new  HRHDetailsModel("House Name"," houshold Number","Date and T time","Amount"));


                    if(myDatabase.getHouseholdRechargeDetailsCount(meterno)<responselist.size())
                    {
                        int old=myDatabase.getHouseholdRechargeDetailsCount(meterno);
                        int nw=responselist.size();

                        for(int i=old;i<nw;i++)
                        {
                            myDatabase.addHouseholdRechargeDetails(responselist.get(i).getHouse(),responselist.get(i).getDevid(),
                                    responselist.get(i).getDate(),responselist.get(i).getAmount());

                        }
                    }


                    /*for ( HRHDetailsModel d : responselist) {

                        myDatabase.addHouseholdRechargeDetails(d.getHouse(),d.getDevid(),
                           d.getDate(),d.getAmount());
                    } */
                }
                else
                {
                    Toast.makeText(context," householdRechargeDetail nahi mili ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HRHDetailsModel>> call, Throwable t) {
                Log.v("merr",t.toString());
            }
        });
    }

    public void totalRecharge(Context context)
    {

        MyDatabase myDatabase=new MyDatabase(context);
        SharedPrefManager sharedPrefManager=new SharedPrefManager(context);

        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<Integer> call = requestApi.getTotalRecharge(tokenstr,saveEmail);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {



                if (response.isSuccessful()) {
                    int totalrcg=response.body();

//                    totalRecharge.setText(Integer.toString(totalrcg));
//                    //Toast.makeText(getContext(),totalRechargemodel.getRecharge().toString(),Toast.LENGTH_LONG).show();

                  int getRecharge=myDatabase.getRechargeDB();



                }
                else {
                    Toast.makeText(context," recharge get me kush grbr ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v("err",t.toString());
                Toast.makeText(context,t.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
