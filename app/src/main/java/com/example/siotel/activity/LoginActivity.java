package com.example.siotel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lottieAnimationView=findViewById(R.id.animation_view);

        lottieAnimationView.addAnimatorUpdateListener((animation) -> {
            // Do something.
        });
        lottieAnimationView.playAnimation();

        if (lottieAnimationView.isAnimating()) {
            // Do something.
        }

        // Custom animation speed or duration.
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(animation -> {lottieAnimationView.setProgress((Float) animation.getAnimatedValue());});
        animator.start();

        FindViewById();
        OnClick();
    }
    private void FindViewById(){

        name=findViewById(R.id.username);
        password=findViewById(R.id.password);

        login=findViewById(R.id.login);

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
    private void LoginRetrofit() {
        String email = name.getText().toString();
        String passwordString = password.getText().toString();
        if (TextUtils.isEmpty(name.getText())) {
            name.setError("please enter email");
        } else if (TextUtils.isEmpty(password.getText())) {
            password.setError("please enter password");
        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://meters.siotel.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LoginModel loginModel = new LoginModel(email, passwordString);
            PostRequestApi postRequestApi = retrofit.create(PostRequestApi.class);


            // moke dara to test
            // PostModel postModel=new PostModel(nameString,passwordString);

            Call<LoginResponseModel> call;
            call = postRequestApi.LogintoServer(loginModel);

            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {


                    LoginResponseModel loginResponseModel = response.body();


                    if (response.isSuccessful()) {

//                    SharedPreferences sharedPreference=getSharedPreferences("SAVE_USER",Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor=sharedPreference.edit();
//                    editor.putString("token",loginResponseModel.getResponse());
//                    editor.putString("email",email);
//                    editor.apply();


                        SaveUser saveUser = new SaveUser(loginResponseModel.getResponse(), email);

                        sharedPrefManager.saveUser(saveUser);
                        Toast.makeText(LoginActivity.this," User Successfully Login",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        Toast.makeText(LoginActivity.this," please check email and password", Toast.LENGTH_SHORT).show();
                    }

                }


                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    //  textView.setText("Error found is : " + t.getMessage());
                    Toast.makeText(LoginActivity.this," please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.v("Tag", t.toString());
                }
            });
        }
     }
        @Override
        public void onBackPressed () {
            super.onBackPressed();
            Intent intent =getIntent();
            finish();
        }

}
