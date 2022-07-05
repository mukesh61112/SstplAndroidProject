package com.example.siotel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
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

    static String meterno="";
    public HouseholdsDetailsFragment(String toString) {
        meterno=toString;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_households_details, container, false);
        householdsDetailsRv=v.findViewById(R.id.households_details_hori_rv);
        sharedPrefManager=new SharedPrefManager(getContext());


        //getMetersData();
        getHouseholdDetailsFromDB(meterno);

        return v;
    }
    private void getMetersData() {
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        householdsDetailsRv.setLayoutManager(llm);


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
                    Toast.makeText(getContext()," meter response mila h ",Toast.LENGTH_LONG).show();
                    Log.v("mhaha"," meter have no error");
                    List<HouseholdsDetailsModel> dlist1 = response.body();
                    List<HouseholdsDetailsModel> dlist2=new ArrayList<>();
                    dlist2.add(new HouseholdsDetailsModel("Household Number","Cum Eb Kwh","Balance","Date and T time"));



                    for (HouseholdsDetailsModel d : dlist1) {


                    }
                    List<HouseholdsDetailsModel> merge =new ArrayList<>();
                    merge.addAll(dlist2);
                    merge.addAll(dlist1);
                    arrayList=new ArrayList<>(merge);
                    householdsDetailsAdapter=new HouseholdsDetailsAdapter(arrayList);
                    householdsDetailsRv.setAdapter(householdsDetailsAdapter);


                }
                else
                {
                    Toast.makeText(getContext()," meter details nahi mili ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HouseholdsDetailsModel>> call, Throwable t) {
                Log.v("merr",t.toString());
            }
        });

    }
    private void getHouseholdDetailsFromDB(String meterno)
    {
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        householdsDetailsRv.setLayoutManager(llm);

        myDatabase=new MyDatabase(getContext());
        arrayList=new ArrayList<>();

        arrayList=myDatabase.getHouseholdsDetails(meterno);

        List<HouseholdsDetailsModel> dlist2=new ArrayList<>();
        dlist2.add(new HouseholdsDetailsModel("Household Number","Cum Eb Kwh","Balance","Date andTtime"));
        dlist2.addAll(arrayList);
        householdsDetailsAdapter=new HouseholdsDetailsAdapter(dlist2);
        householdsDetailsRv.setAdapter(householdsDetailsAdapter);

    }

}