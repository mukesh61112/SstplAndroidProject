package com.example.siotel.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.PayModel;
import com.example.siotel.models.RozarPayResponse;
import com.example.siotel.models.Token;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RechargeFragment extends Fragment implements PaymentResultListener {


    SharedPrefManager sharedPrefManager;
    Token token;
    String eml;

    RozarPayResponse rpr;
    JSONObject js=new JSONObject();


    EditText amount,phone;
    TextView email,hid;

    Button button;

    String householdId;
    public    RechargeFragment(String householdId)
    {
        this.householdId=householdId;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recharge, container, false);
        sharedPrefManager=new SharedPrefManager(getContext());
        token=sharedPrefManager.getUser();




        email=view.findViewById(R.id.remail);
        amount=view.findViewById(R.id.ramount);
        phone=view.findViewById(R.id.rphone);
        hid=view.findViewById(R.id.rhid);
        button=view.findViewById(R.id.rrecharge);

        eml=token.getEmail();
        email.setText(eml);
        hid.setText(householdId);




        onClick();











        return view;
    }

    private void onClick()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =(AppCompatActivity)view.getContext();
                Activity activity1=getActivity();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,new BalanceFragment())
//                        .addToBackStack(null).commit();
              //  rozroPayApi();
                int intAmt=Math.round(Float.parseFloat(amount.getText().toString()));


            //   Toast.makeText(getContext(),rpr.getResponse().getID(),Toast.LENGTH_LONG).show();

               Checkout checkout=new Checkout();

                checkout.setKeyID("rzp_test_QdoGFyxDEOQ2PR");
                checkout.setImage(R.drawable.image);

                try {
                    JSONObject options = new JSONObject();

                    options.put("name", "Merchant Name");
                    options.put("description", "Reference No. #123456");
                    options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                    options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                    options.put("theme.color", "#3399cc");
                    options.put("currency", "INR");
                    options.put("amount", intAmt);//pass amount in currency subunits
                    options.put("prefill.email", eml);
                    options.put("prefill.contact",phone.getText().toString());
                    JSONObject retryObj = new JSONObject();
                    retryObj.put("enabled", true);
                    retryObj.put("max_count", "5000");
                    options.put("retry", retryObj);

                    checkout.open(activity1, options);

                } catch(Exception e) {
                    Log.e("err", "Error in starting Razorpay Checkout", e);
                    Toast.makeText(getContext(),"Error in starting Razorpay Checkout",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void rozroPayApi()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        PayModel payModel=new PayModel(eml,amount.getText().toString(),phone.getText().toString(),householdId);


        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);

        Call<RozarPayResponse> call=requestApi.getRzPay(payModel);

        call.enqueue(new Callback<RozarPayResponse>() {

            @Override
            public void onResponse(@NonNull Call<RozarPayResponse> call, @NonNull Response<RozarPayResponse> response) {
                if (response.isSuccessful()) {

                    Log.v("haha"," have no error");
                    RozarPayResponse object = Objects.requireNonNull(response).body();
                      Toast.makeText(getContext()," rpay api response mila h ",Toast.LENGTH_LONG).show();
                      Toast.makeText(getContext(),object.getResponseObject().getCurrency().toString(),Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getContext()," rozro pay me kuch grbr h ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RozarPayResponse> call, Throwable t) {
                Toast.makeText(getContext()," rozro pay api connect nahi hio",Toast.LENGTH_LONG).show();
                Log.v("err",t.toString());
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}