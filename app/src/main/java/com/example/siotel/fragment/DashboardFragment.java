package com.example.siotel.fragment;

import static android.widget.Toast.makeText;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.adapters.HCurrentAmountAdapter;
import com.example.siotel.adapters.IotIntroAdapter;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.CurrAmountRequestModel;
import com.example.siotel.models.CurrRechaModel;
import com.example.siotel.models.CurrentRechargeResponse;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.IotIntroModel;
import com.example.siotel.models.SaveEmail;
import com.example.siotel.models.Token;
import com.example.siotel.sqlitedatabase.MyDatabase;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardFragment extends Fragment {


    SliderView sliderView;
    TextView setHouseholdCount,totalRecharge;
    RecyclerView currentRecharge;
    List<IotIntroModel> list;
    List<CurrRechaModel> currRechList;
    IotIntroAdapter iotIntroAdapter;
    HCurrentAmountAdapter hCurrentAmountAdapter;
    SharedPrefManager sharedPrefManager;
    MyDatabase myDatabase;
    boolean first=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_dashboard, container, false);
        findViewById(v);
        iotIntroRV();



           setHouseholdCount();
           setHouseholdCurrentRecharge();
           setTotalRecharge();

        return v;
    }
    private void findViewById(View v)
    {

        sliderView=v.findViewById(R.id.introRv);
        setHouseholdCount=v.findViewById(R.id.householdCount);
        sharedPrefManager=new SharedPrefManager(getContext());

        totalRecharge=v.findViewById(R.id.totalRecharge);
        myDatabase=new MyDatabase(getContext());
        currentRecharge=v.findViewById(R.id.currentRecharge);

    }
    private void iotIntroRV()
    {
//        list=new ArrayList<>();
//        LinearLayoutManager llm=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerView.setLayoutManager(llm);
//        iotIntroAdapter=new IotIntroAdapter(getContext(),list);
//        recyclerView.setAdapter(iotIntroAdapter);
//


        list=new ArrayList<IotIntroModel>();





        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
       // list.add(new IotIntroModel(R.drawable.iot4));
        list.add(new IotIntroModel(R.drawable.iot3));
        list.add(new IotIntroModel(R.drawable.iot1));
        list.add(new IotIntroModel(R.drawable.iot2));


        // below method is use to set
        // scroll time in seconds.

        iotIntroAdapter=new IotIntroAdapter(getContext(),list);
        sliderView.setSliderAdapter(iotIntroAdapter);
        sliderView.setScrollTimeInSec(5);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

    }

    private void setHouseholdCount()
    {
        int count=myDatabase.getHouseholdCount();
        setHouseholdCount.setText(Integer.toString(count));
        callHouseholdApi();
    }
    private void setTotalRecharge()
    {
         int tr=myDatabase.getRechargeDB();
         totalRecharge.setText(Integer.toString(tr));
         callTotalRechargeApi();
    }
    private void setHouseholdCurrentRecharge()
    {
        currRechList=new ArrayList<>();
        GridLayoutManager llm=new GridLayoutManager(getContext(),2);
        currentRecharge.setLayoutManager(llm);
        myDatabase=new MyDatabase(getContext());
        hCurrentAmountAdapter=new HCurrentAmountAdapter(getContext(),currRechList);
        currentRecharge.setAdapter(hCurrentAmountAdapter);

        List<HouseholdsModel> hlist=new ArrayList<>();
        hlist=myDatabase.getHouseHolds();
        for(HouseholdsModel d:hlist)
        {
            getCurrentAmount(d.getMetersno());
        }

//        myDatabase.addCurrentAmount("b",645);
//        myDatabase.addCurrentAmount("a",789);
      //  myDatabase.updateCurrentBalance("a",14);
//
//        List<CurrRechaModel> dlist=myDatabase.getCurrBalance();
//        currRechList.addAll(dlist);
        hCurrentAmountAdapter.notifyDataSetChanged();


        //getCurrentAmount("506f98000000c1fd");

    }
    private void  callHouseholdApi() {


        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);










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
                .baseUrl("http://meters.siotel.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<List<HouseholdsModel>> call = requestApi.getAllMeter(tokenstr,saveEmail);

        call.enqueue(new Callback<List<HouseholdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<HouseholdsModel>> call, @NonNull Response<List<HouseholdsModel>> response) {
                if(response.isSuccessful()) {
                    List<HouseholdsModel> dlist = response.body();
                    if (myDatabase.getHouseholdCount() < dlist.size()) {
                        int old = myDatabase.getHouseholdCount();
                        int nw = dlist.size();

                        for (int i = old; i < nw; i++) {
                            String date = dlist.get(i).getDate();
                            String arr[] = date.split("[T]");
                            myDatabase.addHouseHold(dlist.get(i).getHouseholdname(), dlist.get(i).getMetersno(), arr[0]);
                        }
                    }
                }
                else {
                   // makeText(getContext(), " household api response not successfull  ", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<HouseholdsModel>> call, Throwable t) {
                Log.v("err",t.toString());
               // makeText(getContext()," please check internet connection ",Toast.LENGTH_SHORT).show();

            }
        });


    }
    private  void getCurrentAmount(String hid)
    {
        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);

        CurrAmountRequestModel currAmountRequestModel=new CurrAmountRequestModel(email,hid);
        OkHttpClient innerClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();
        Retrofit retrofit = new Retrofit.Builder().client(innerClient)
                .baseUrl("http://meters.siotel.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<CurrentRechargeResponse> call = requestApi.getHouseholdCurrentAmount(tokenstr,currAmountRequestModel);

        call.enqueue(new Callback<CurrentRechargeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CurrentRechargeResponse> call, @NonNull Response<CurrentRechargeResponse> response) {
                if(response.isSuccessful()) {
                   // makeText(getContext(),"  Curretn amount api response successfull ",Toast.LENGTH_SHORT).show();
                    CurrentRechargeResponse dlist = response.body();

                         currRechList.add(new CurrRechaModel(hid,dlist));
                         hCurrentAmountAdapter.notifyDataSetChanged();
//                    SQLiteDatabase db=myDatabase.getWritableDatabase();
//                    db.execSQL("DROP TABLE IF EXISTS currentAmount");
//
//                    myDatabase.addCurrentAmount(hid,dlist.getAmount());
                   // hCurrentAmountAdapter.notifyDataSetChanged();
                }
                else {
                  //  makeText(getContext(), "  Curretn amount api response not successfull  ", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CurrentRechargeResponse> call, Throwable t) {
                Log.v("curre",t.toString());
              //  Toast.makeText(getContext(),"  Curretn amount api not connect ",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void callTotalRechargeApi()
    {
        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getToken();
        String email=token.getEmail();
        SaveEmail saveEmail=new SaveEmail(email);
      //  makeText(getContext(),tokenstr+"   "+email,Toast.LENGTH_LONG).show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<Integer> call = requestApi.getTotalRecharge(tokenstr,saveEmail);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {


                if (response.isSuccessful()) {
                    int totalrcg = response.body();
                   // totalRecharge.setText(Integer.toString(totalrcg));

                    if (myDatabase.getRechargeDB() == 0)
                        myDatabase.addRecharge(totalrcg);
                    if (myDatabase.getRechargeDB() != totalrcg)
                        myDatabase.updateRecharge(totalrcg);
                }
                else {
                    //makeText(getContext()," recharge get me kush grbr ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v("err",t.toString());
                //Toast.makeText(getContext()," please check internet connection ",Toast.LENGTH_LONG).show();
            }
        });

    }


}