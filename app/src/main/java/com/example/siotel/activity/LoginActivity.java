package com.example.siotel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.models.LoginModel;
import com.example.siotel.models.LoginResponseModel;
import com.example.siotel.models.SaveUser;
import com.example.siotel.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText name,password;
    Button login;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FindViewById();
        OnClick();
    }
    private void FindViewById(){

        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        name=findViewById(R.id.username);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());

    }
    private void OnClick(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginRetrofit();
                // Toast.makeText(getActivity()," clik huaa h",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void LoginRetrofit()
    {
        String email=name.getText().toString();
        String passwordString=password.getText().toString();


        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginModel loginModel=new LoginModel(email,passwordString);
        PostRequestApi postRequestApi=retrofit.create(PostRequestApi.class);











        // moke dara to test
        // PostModel postModel=new PostModel(nameString,passwordString);

        Call<LoginResponseModel> call;
        call = postRequestApi.LogintoServer(loginModel);

        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {






                LoginResponseModel loginResponseModel=response.body();





                if(response.isSuccessful())
                {

//                    SharedPreferences sharedPreference=getSharedPreferences("SAVE_USER",Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor=sharedPreference.edit();
//                    editor.putString("token",loginResponseModel.getResponse());
//                    editor.putString("email",email);
//                    editor.apply();


                      SaveUser saveUser=new SaveUser(loginResponseModel.getResponse(),email);

                      sharedPrefManager.saveUser(saveUser);

                //    Toast.makeText(LoginActivity.this," kuch response mils hai"+loginResponseModel.getResponse(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }


            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                //  textView.setText("Error found is : " + t.getMessage());
                Toast.makeText(LoginActivity.this,t.toString(),Toast.LENGTH_SHORT).show();
                Log.v("Tag",t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }
}
