package com.example.siotel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.adapters.HouseholdsDetailsAdapter;
import com.example.siotel.adapters.RechargeHisDetailsAdapter;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.HRHDetailsModel;
import com.example.siotel.models.HouseholdsDetailsModel;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;
import com.example.siotel.sqlitedatabase.MyDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RechargHistoryDetailsFragment extends Fragment  {


    SharedPrefManager sharedPrefManager;
    MyDatabase myDatabase;


  String meterno=null;
    public RechargHistoryDetailsFragment(String meterno) {
        // Required empty public constructor
        this.meterno=meterno;
    }



    RecyclerView hrhdRV;

    List<HRHDetailsModel> arrlist;

    RechargeHisDetailsAdapter rhdadapter;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recharg_history_details, container, false);
        hrhdRV=view.findViewById(R.id.households_recharge_hi_de_rv);

        sharedPrefManager=new SharedPrefManager(getContext());
        myDatabase=new MyDatabase(getContext());

        //getHouseholdsRechargeHistory();
        getHouseholdRechargeHistoryFromDB();
     return  view;
    }
    private void getHouseholdsRechargeHistory() {
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
       hrhdRV.setLayoutManager(llm);



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

        Call<List< HRHDetailsModel>> call=requestApi.getHouseholdsRechargeHistory(url,tokenstr);

        call.enqueue(new Callback<List< HRHDetailsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List< HRHDetailsModel>> call, @NonNull Response<List< HRHDetailsModel>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext()," kuch recharge details  mili h ",Toast.LENGTH_LONG).show();
                    Log.v("mhaha"," meter have no error");
                    List<HRHDetailsModel> dlist1 = response.body();
                    List< HRHDetailsModel> dlist2=new ArrayList<>();
                    dlist2.add(new  HRHDetailsModel("House Name"," houshold Number","Date and T time","Amount"));



                    for ( HRHDetailsModel d : dlist1) {


                    }
                    List< HRHDetailsModel> merge =new ArrayList<>();
                    merge.addAll(dlist2);
                    merge.addAll(dlist1);
                    arrlist=new ArrayList<>(merge);
                    rhdadapter=new RechargeHisDetailsAdapter(arrlist);
                    hrhdRV.setAdapter(rhdadapter);





                }
                else
                {
                    Toast.makeText(getContext()," householdRechargeDetail nahi mili ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HRHDetailsModel>> call, Throwable t) {
                Log.v("merr",t.toString());
            }
        });

    }
    private void getHouseholdRechargeHistoryFromDB()
    {
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        hrhdRV.setLayoutManager(llm);
        arrlist=new ArrayList<>();

        arrlist=myDatabase.getHouseholdsRechargeDetails(meterno);

        List< HRHDetailsModel> dlist2=new ArrayList<>();
        dlist2.add(new  HRHDetailsModel("House Name"," houshold Number","Date andTtime","Amount"));

        dlist2.addAll(arrlist);
        rhdadapter=new RechargeHisDetailsAdapter(dlist2);
        hrhdRV.setAdapter(rhdadapter);
    }



}