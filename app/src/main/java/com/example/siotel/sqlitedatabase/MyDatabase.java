package com.example.siotel.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.siotel.models.HRHDetailsModel;
import com.example.siotel.models.HouseholdsDetailsModel;
import com.example.siotel.models.HouseholdsModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME="MyDatabase";
    private static final int DB_VERSION=1;
    private static final String HOUSEHOLD="householdTable";
    private static final String HOUSEHOLDDETAILS="householdDetailsTable";
    private static final String RECHARGEDETAILS="rechargeTable";
    private static final String TOTALRECHARGE="totalRechargeTable";


    private static final String H_SN="h_sn";
    private static final String H_NAME="h_name";
    private static final String H_NUM="h_num";
    private static final String H_DATE="h_date";


    private static final String HD_SN="hd_sn";
    private static final String HD_NUM="hd_num";
    private static final String HD_BALANCE="hd_balance";
    private static final String HD_KWH="hd_kwh";
    private static final String HD_DATE="hd_date";


    private static final String HR_SN="hr_sn";
    private static final String HR_NAME="hr_name";
    private static final String HR_NUM="hr_num";
    private static final String HR_BALANCE="hr_balance";
    private static final String HR_DATE="hr_date";

    private static final String R_SN="r_sn";
    private static final String R_RECHARGE="r_recharge";

    String householdTable="CREATE TABLE " +HOUSEHOLD+" ("+
            H_SN+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + H_NAME +" TEXT,"
            +H_NUM+" TEXT,"
            +H_DATE+" TEXT)";


    String householdDetailsTable="CREATE TABLE " +HOUSEHOLDDETAILS+" ("+
            HD_SN+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + HD_NUM +" TEXT,"
            +HD_KWH  +" TEXT,"
            +HD_BALANCE+" TEXT,"
            +HD_DATE+" TEXT)";



    String householdRechargeTable="CREATE TABLE " +RECHARGEDETAILS+" ("+
            HR_SN+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +HR_NAME+" TEXT,"
            +HR_NUM+" TEXT,"
            +HR_DATE+" TEXT,"
            +HR_BALANCE+" TEXT)";

    String totalRecharge="CREATE TABLE " +TOTALRECHARGE+" ("+
            R_SN+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +R_RECHARGE+" INTEGER)";

    public MyDatabase( Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



        sqLiteDatabase.execSQL(householdTable);
        sqLiteDatabase.execSQL(householdDetailsTable);
        sqLiteDatabase.execSQL(householdRechargeTable);
        sqLiteDatabase.execSQL(totalRecharge);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +HOUSEHOLD );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ HOUSEHOLDDETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ RECHARGEDETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TOTALRECHARGE);
        onCreate(sqLiteDatabase);
    }

    public void deleteMyDatabase(Context context)
    {
        context.deleteDatabase(DB_NAME);
    }



    public void addHouseHold(String h_name,String h_num,String h_date)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(H_NAME,h_name);
        values.put(H_NUM,h_num);
        values.put(H_DATE,h_date);

        // after adding all values we are passing
        // content values to our table.
        db.insert(HOUSEHOLD, null, values);

        // at last we are closing our
        // database after adding database.

    }
    public ArrayList<HouseholdsModel> getHouseHolds() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLD, null);

        // on below line we are creating a new array list.
        ArrayList<HouseholdsModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new HouseholdsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return courseModalArrayList;
    }
    public void dropAddHouseholds()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + HOUSEHOLD);

        onCreate(db);
    }
    public int getHouseholdCount()
    {
        Cursor cursor;
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLD, null);


        // on below line we are creating a new array list.
        ArrayList<HouseholdsModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new HouseholdsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return courseModalArrayList.size();
    }
    public void addHouseHoldDetails(String hd_num,String hd_kwh,String hd_balance,String hd_date)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(HD_NUM,hd_num);
        values.put(HD_KWH,hd_kwh);

        values.put(HD_BALANCE,hd_balance);
        values.put(HD_DATE,hd_date);

        // after adding all values we are passing
        // content values to our table.
        db.insert(HOUSEHOLDDETAILS, null, values);

        // at last we are closing our
        // database after adding database.

    }
    public List<HouseholdsDetailsModel> getHouseholdsDetails(String meternum){


        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLDDETAILS , null);

        ArrayList<HouseholdsDetailsModel> list = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                    list.add(new HouseholdsDetailsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),cursor.getString(4)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return list;
    }
    public int  getHouseholdDetailsCount(String meternum)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLDDETAILS , null);

        ArrayList<HouseholdsDetailsModel> list = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                list.add(new HouseholdsDetailsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),cursor.getString(4)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return list.size();
    }
    public void addHouseholdRechargeDetails(String hr_name,String hr_num,String hr_date,String hr_balance)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(HR_NAME,hr_name);
        values.put(HR_NUM,hr_num);
        values.put(HR_DATE,hr_date);
        values.put(HR_BALANCE,hr_balance);

        // after adding all values we are passing
        // content values to our table.
        db.insert(RECHARGEDETAILS, null, values);

        // at last we are closing our
        // database after adding database.
    }
    public List<HRHDetailsModel> getHouseholdsRechargeDetails(String meternum){


        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RECHARGEDETAILS , null);

        ArrayList<HRHDetailsModel> list = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                list.add(new HRHDetailsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),cursor.getString(4)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return list;
    }

    public int  getHouseholdRechargeDetailsCount(String meternum)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLDDETAILS , null);

        ArrayList<HRHDetailsModel> list = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                list.add(new HRHDetailsModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),cursor.getString(4)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return list.size();
    }
    public void addRecharge(int rechagre)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(R_RECHARGE,rechagre);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TOTALRECHARGE, null, values);
    }
    public int getRechargeDB()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOUSEHOLD, null);

        // on below line we are creating a new array list.
         int   recharge=0;

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                   recharge=cursor.getInt(1);
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return  recharge;
    }
    public void updateRecharge(int recharge) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE STUDENT SET H_RECHARGE"+ " = "+recharge);
    }
}
