package com.example.siotel.fragment;

import static java.time.LocalDateTime.of;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.adapters.HouseHoldsAdapter;
import com.example.siotel.adapters.HouseholdsDetailsAdapter;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.HouseholdsDetailsModel;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;
import com.example.siotel.sqlitedatabase.MyDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HouseholdsDetailsFragment extends Fragment{

    List<HouseholdsDetailsModel> arrayList;
    HouseholdsDetailsAdapter householdsDetailsAdapter;
    RecyclerView householdsDetailsRv;


   SharedPrefManager sharedPrefManager;
    MyDatabase myDatabase;
    static String householdId="";


    public HouseholdsDetailsFragment(String toString) {
        householdId=toString;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_households_details, container, false);

        findById(v);



        getHouseholdDetailsFromDB(householdId);

        return v;
    }

    private void findById(View v)
    {
        householdsDetailsRv=v.findViewById(R.id.households_details_hori_rv);
        sharedPrefManager=new SharedPrefManager(getContext());
        myDatabase=new MyDatabase(getContext());
    }
    private void getHouseholdDetailsFromDB(String meterno)
    {
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        householdsDetailsRv.setLayoutManager(llm);
        arrayList=new ArrayList<>();
        arrayList=myDatabase.getHouseholdsDetails(meterno);
        Collections.reverse(arrayList);
        List<HouseholdsDetailsModel> dlist2=new ArrayList<>();
        dlist2.add(new HouseholdsDetailsModel("Household Number","Cum Eb Kwh","Balance","Date andTtime"));
        dlist2.addAll(arrayList);
        householdsDetailsAdapter=new HouseholdsDetailsAdapter(dlist2);
        householdsDetailsRv.setAdapter(householdsDetailsAdapter);

        callHouseholdDetailsApi();

    }
    private void callHouseholdDetailsApi() {

        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        householdsDetailsRv.setLayoutManager(llm);


        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        String url="houseListApi/"+householdId;
      Call<List<HouseholdsDetailsModel>> call = requestApi.getMetersDtl(url,tokenstr);

        call.enqueue(new Callback<List<HouseholdsDetailsModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call<List<HouseholdsDetailsModel>> call, @NonNull Response<List<HouseholdsDetailsModel>> response) {
                if (response.isSuccessful()) {


                    List<HouseholdsDetailsModel> responselist=response.body();

                /*    for(HouseholdsDetailsModel d:responselist)
                    {
                        myDatabase.addHouseHoldDetails(d.getMeterSN(),d.getCum_eb_kwh(),
                                d.getBalance_amount(),d.getDate());
                    } */


                  if(myDatabase.getHouseholdDetailsCount(householdId)==0)
                    {

                         for(int i=responselist.size()-1;i>=0;i--)
                         {
                             HouseholdsDetailsModel d=responselist.get(i);
                             myDatabase.addHouseHoldDetails(d.getMeterSN(),d.getCum_eb_kwh(),
                                                            d.getBalance_amount(),d.getDate());
                         }
                    }
                     else{
                        List<HouseholdsDetailsModel> beforlist=myDatabase.getHouseholdsDetails(householdId);
                        int beforlistSize=myDatabase.getHouseholdDetailsCount(householdId);
                      Collections.reverse(beforlist);
                        String  beforTimeString=beforlist.get(0).getDate();
                        String beforTimeArray[]=beforTimeString.split("[-,T,:,.]");

                        LocalDateTime beforDate= of(Integer.parseInt(beforTimeArray[0]),
                                Integer.parseInt(beforTimeArray[1]),
                                Integer.parseInt(beforTimeArray[2]),
                                Integer.parseInt(beforTimeArray[3]),
                                Integer.parseInt(beforTimeArray[4]),
                                Integer.parseInt(beforTimeArray[5]));

                        for(int i=responselist.size()-1;i>=0;i--)
                        {
                            HouseholdsDetailsModel d=responselist.get(i);
                             String afterTimeString =d.getDate();
                             String afterTimeArray[]=afterTimeString.split("[-,T,:,.]");
                            LocalDateTime afterDate= of(Integer.parseInt(afterTimeArray[0]),
                                    Integer.parseInt(afterTimeArray[1]),
                                    Integer.parseInt(afterTimeArray[2]),
                                    Integer.parseInt(afterTimeArray[3]),
                                    Integer.parseInt(afterTimeArray[4]),
                                    Integer.parseInt(afterTimeArray[5]));

                            if(afterDate.isAfter(beforDate))
                            {
                                myDatabase.addHouseHoldDetails(d.getMeterSN(),d.getCum_eb_kwh(),
                                        d.getBalance_amount(),d.getDate());
                            }

                        }

                    }



                }
                else
                {
                    Toast.makeText(getContext()," household details response not successfull  ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HouseholdsDetailsModel>> call, Throwable t) {
                Toast.makeText(getContext()," please check internet connection ",Toast.LENGTH_SHORT).show();
                Log.v("merr",t.toString());
            }
        });

    }


}