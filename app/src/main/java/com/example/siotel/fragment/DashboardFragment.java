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
import android.widget.TextView;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.adapters.IotIntroAdapter;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.IotIntroModel;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;
import com.example.siotel.models.TotalRecharge;
import com.example.siotel.sqlitedatabase.HouseholdDatabase;
import com.example.siotel.sqlitedatabase.MyDatabase;
import com.jjoe64.graphview.GraphView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardFragment extends Fragment {


    GraphView graphView;
    TextView setHouseholdCount,totalRecharge;
    RecyclerView recyclerView;
    List<IotIntroModel> list;
    IotIntroAdapter iotIntroAdapter;
    SharedPrefManager sharedPrefManager;
    HouseholdDatabase householdDatabase;
    MyDatabase myDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_dashboard, container, false);
        findViewById(v);
        iotIntroRV();



           setHouseholdCount();
           getTotalRecharge();

        return v;
    }
    private void findViewById(View v)
    {

        recyclerView=v.findViewById(R.id.dashboardintro);
        setHouseholdCount=v.findViewById(R.id.householdCount);
        sharedPrefManager=new SharedPrefManager(getContext());
        householdDatabase=new HouseholdDatabase(getContext());
        totalRecharge=v.findViewById(R.id.totalRecharge);

    }
    private void iotIntroRV()
    {
        list=new ArrayList<>();
        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);
        iotIntroAdapter=new IotIntroAdapter(getContext(),list);
        recyclerView.setAdapter(iotIntroAdapter);

        list.add(new IotIntroModel(R.drawable.iot1));
        list.add(new IotIntroModel(R.drawable.iot2));
        list.add(new IotIntroModel(R.drawable.iot3));
        list.add(new IotIntroModel(R.drawable.iot4));
    }

    private void setHouseholdCount()
    {
//       int count=householdDatabase.getHouseholdCount();
//        setHouseholdCount.setText(Integer.toString(count)); myDatabase=new MyDatabase(getContext());
////        int count=myDatabase.getHouseholdCount();
////        setHouseholdCount.setText(Integer.toString(count));
//

       getHouseholdsCount();
    }
    private void getHouseholdsCount() {


        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);
      //  Toast.makeText(getContext(),tokenstr+"   "+email,Toast.LENGTH_LONG).show();









//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//          httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("Authorization", token1).build();
//                return chain.proceed(request);
//            }
//        });




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        // devicesText.setText(s);
       // Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();

       // Log.v("token",s);
        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<List<HouseholdsModel>> call = requestApi.getAllMeter(tokenstr,saveEmail);

        call.enqueue(new Callback<List<HouseholdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HouseholdsModel>> call, @NonNull Response<List<HouseholdsModel>> response) {


               Log.v("yesss",response.message());
                if (response.isSuccessful()) {
                    // Toast.makeText(getContext()," response mila h ",Toast.LENGTH_LONG).show();
                   // Log.v("haha"," have no error");
                    List<HouseholdsModel> dlist = response.body();

                    int count=0;
                    for (HouseholdsModel d : dlist) {
                        String name = "";
                        name += d.getHouseholdname();
                        Log.v("name", name);
                        count++;
                    }
                   String countstring=""+count;
                    setHouseholdCount.setText(countstring);






                }
                else {
                    Toast.makeText(getContext()," kucn kasjdfkjksdjf ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<HouseholdsModel>> call, Throwable t) {
                Log.v("err",t.toString());
                    Toast.makeText(getContext()," connet nahi huaa ",Toast.LENGTH_LONG).show();

            }
        });


    }

    private void getTotalRecharge()
    {
        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);
        Toast.makeText(getContext(),tokenstr+"   "+email,Toast.LENGTH_LONG).show();

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

                    totalRecharge.setText(Integer.toString(totalrcg));
                    //Toast.makeText(getContext(),totalRechargemodel.getRecharge().toString(),Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getContext()," recharge get me kush grbr ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v("err",t.toString());
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }


}