package com.example.siotel.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.interfaces.RecyclerViewInterface;
import com.example.siotel.models.HhModel;
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

public class HouseholdsFragment extends Fragment    {
    List<HouseholdsModel> arrayList;


    HouseHoldsAdapter houseHoldsAdapter;
    RecyclerView devicesRV;
    Button householdRecharge;


    SharedPrefManager sharedPrefManager;
    HouseholdDatabase householdDatabase;
    MyDatabase myDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_households, container, false);

        sharedPrefManager=new SharedPrefManager(getActivity().getApplicationContext());
        devicesRV=v.findViewById(R.id.devices_rv);
        householdRecharge=v.findViewById(R.id.household_recharge_but);








       // getMetersData();
        setHouseHoldsFromDatabase();



        return  v;
    }
    private void getMetersData() {


//        GridLayoutManager llm=new GridLayoutManager(getContext(),2);
//        devicesRV.setLayoutManager(llm);
       // householdDatabase=new HouseholdDatabase(getContext());
      //  arrayList=householdDatabase.getHouseHolds();

       // houseHoldsAdapter=new HouseHoldsAdapter(getContext(),arrayList);
       // devicesRV.setAdapter(houseHoldsAdapter);
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
                  //  householdDatabase=new HouseholdDatabase(getContext());
                    myDatabase=new MyDatabase(getContext());
                    myDatabase.dropAddHouseholds();
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



                 /*   for(HouseholdsModel d:dlist)
                    {
                          String date=d.getDate();
                          String arr[]=date.split("[T]");
                          myDatabase.addHouseHold(d.getHouseholdname(),d.getMetersno(),arr[0]);
                    } */

//                    arrayList=new ArrayList<HouseholdsModel>(dlist);
//                    houseHoldsAdapter =new HouseHoldsAdapter(getContext(),arrayList);
//                    devicesRV.setAdapter(houseHoldsAdapter);


//                   householdDatabase=new HouseholdDatabase(getContext());
//                    ArrayList<HouseholdsModel> list=householdDatabase.getHouseHolds();




                }
                else
                {
                    Toast.makeText(getContext()," kucn kasjdfkjksdjf ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<HouseholdsModel>> call, Throwable t) {
              Log.v("err",t.toString());
            }
        });


    }
    public void setHouseHoldsFromDatabase()
    {

        arrayList=new ArrayList<>();
        GridLayoutManager llm=new GridLayoutManager(getContext(),2);
        devicesRV.setLayoutManager(llm);
       // householdDatabase=new HouseholdDatabase(getContext());
       // arrayList=householdDatabase.getHouseHolds();

        myDatabase=new MyDatabase(getContext());
        arrayList=myDatabase.getHouseHolds();

        houseHoldsAdapter=new HouseHoldsAdapter(getContext(),arrayList);
        devicesRV.setAdapter(houseHoldsAdapter);
    }
    private void onClick()
    {

    }






}