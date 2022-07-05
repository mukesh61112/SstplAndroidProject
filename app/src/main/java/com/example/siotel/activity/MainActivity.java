package com.example.siotel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.siotel.R;
import com.example.siotel.SharedPrefManager;
import com.example.siotel.api.PostRequestApi;
import com.example.siotel.connectapi.AllApiConnect;
import com.example.siotel.fragment.BalanceFragment;
import com.example.siotel.fragment.DashboardFragment;
import com.example.siotel.fragment.HouseholdsFragment;
import com.example.siotel.fragment.RechargeHistoryFragment;
import com.example.siotel.fragment.RechargeFragment;
import com.example.siotel.models.HouseholdsModel;
import com.example.siotel.models.Token;
import com.example.siotel.sqlitedatabase.HouseholdDatabase;
import com.example.siotel.sqlitedatabase.ImageDatabase;
import com.example.siotel.sqlitedatabase.MyDatabase;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {



    SharedPrefManager sharedPrefManager;
    HouseholdDatabase householdDatabase;
    MyDatabase myDatabase;
    AllApiConnect allApiConnect;


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageView profileImage,selectimage,show;
    FragmentContainerView fragmentContainerView;






    Uri imagePath;
    Bitmap imageStore;
    Toolbar toolbar;
    ImageDatabase database;


    private  static  final int  PIC_IMAGE=100;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View inflatedView = getLayoutInflater().inflate(R.layout.nav_header, null);
         findViewById();
         toolbar();
         drawer();
         viewClick();
         setOnView();
         navigationViewItemSelect();
         apiCall();



    }


    public  void findViewById()
    {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigation_drwaver);
        View headerView=navigationView.getHeaderView(0);
        selectimage = headerView.findViewById(R.id.selectimage);
        show=findViewById(R.id.show);
        profileImage = headerView.findViewById(R.id.imageView);
        database =new ImageDatabase(this);
        fragmentContainerView=findViewById(R.id.fragmentContainerView);

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        allApiConnect=new AllApiConnect();

        householdDatabase=new HouseholdDatabase(getApplicationContext());
        myDatabase=new MyDatabase(getApplicationContext());

    }
    public void drawer()
    {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    public void toolbar()
    {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
    public void viewClick(){
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*  Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              startActivityForResult(cameraIntent, PIC_IMAGE); */



                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        PIC_IMAGE);


             /*   Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,PIC_IMAGE); */
            }
        });

    }
    public void setOnView()
    {
        Bitmap b = getTheImage();
        profileImage.setImageBitmap(b);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.logout:
               logoutApp();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PIC_IMAGE && resultCode == RESULT_OK ) {

                imagePath=data.getData();
                imageStore=MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageStore.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();



              if(getTheImage()==null) {
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ImageDatabase.KEY_IMG_URL, byteArray);
                db.insert(ImageDatabase.TABLE_NAME, null, values);
                db.close();
                Bitmap b = getTheImage();
                profileImage.setImageBitmap(b);
            }
            else{
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ImageDatabase.KEY_IMG_URL, byteArray);
                db.update(ImageDatabase.TABLE_NAME, values,null,null);
                db.close();
                Bitmap b = getTheImage();
                profileImage.setImageBitmap(b);
            }

            }

        }catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
        }





    }
    public Bitmap getTheImage(){

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = (Cursor) db.rawQuery(" SELECT * FROM "+ImageDatabase.TABLE_NAME,null,null);
        if (cursor.moveToFirst()){
            @SuppressLint("Range") byte[] imgByte =  cursor.getBlob(cursor.getColumnIndex(ImageDatabase.KEY_IMG_URL));
            cursor.close();
            return BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return null;
    }
    private void navigationViewItemSelect() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
              /*  if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true); */

                // I THINK THAT I NEED EDIT HERE...


                //Closing drawer on item click
                drawerLayout.closeDrawers();


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment
                    case R.id.dashboard:
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainerView,new DashboardFragment());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.recharge_history:
                        FragmentManager fragmentManager1=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.fragmentContainerView,new RechargeHistoryFragment());
                        fragmentTransaction1.commit();
                        return true;

                    case R.id.household:
                        FragmentManager fragmentManager3=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3=fragmentManager3.beginTransaction();
                        fragmentTransaction3.replace(R.id.fragmentContainerView,new HouseholdsFragment());
                        fragmentTransaction3.commit();
                        return  true;
                    case R.id.balance:
                        FragmentManager fragmentManager4=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction4=fragmentManager4.beginTransaction();
                        getSupportFragmentManager().popBackStack();
                        fragmentTransaction4.replace(R.id.fragmentContainerView,new BalanceFragment());

                        fragmentTransaction4.commit();
                        return  true;

                }
                return false;


            }

        });
    }
    void logoutApp()
    {
        Token token=sharedPrefManager.getUser();
        String tokenstr="Bearer "+token.getEmail();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://meters.siotel.in:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        PostRequestApi requestApi = retrofit.create(PostRequestApi.class);
        Call<Token> call = requestApi.logoutApp(tokenstr);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                  if(response.isSuccessful())
                  {
                     myDatabase.deleteMyDatabase(getApplicationContext());
                     sharedPrefManager.logout();
                      Toast.makeText(MainActivity.this, "User LogOut", Toast.LENGTH_SHORT)
                              .show();
                      Intent intent=getIntent();
                      finish();

                  }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            finish();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }



    private void apiCall()
    {
        allApiConnect.housholdApi(getApplicationContext());

        List<HouseholdsModel> householdlist=myDatabase.getHouseHolds();

        for(int i=0;i<householdlist.size();i++)
        {
            allApiConnect.householdDetailsApi(getApplicationContext(),householdlist.get(i).getMetersno());

        }
        for(int i=0;i<householdlist.size();i++)
        {
            allApiConnect.householdRechargeDetailsApi(getApplicationContext(),householdlist.get(i).getMetersno());
        }

    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_IMAGE && resultCode == Activity.RESULT_OK) {

            imageStore = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageStore.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            if(getTheImage()==null) {
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ImageDatabase.KEY_IMG_URL, byteArray);
                db.insert(ImageDatabase.TABLE_NAME, null, values);
                db.close();
                Bitmap b = getTheImage();
                profileImage.setImageBitmap(b);
            }
            else{
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ImageDatabase.KEY_IMG_URL, byteArray);
                db.update(ImageDatabase.TABLE_NAME, values,null,null);
                db.close();
                Bitmap b = getTheImage();
                profileImage.setImageBitmap(b);
            }

        }
    }*/
}


