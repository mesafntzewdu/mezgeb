package com.mesi.mezgeb2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "mezgeb.db";
    private static final int VERSION = 1;
    private final Context mContext;
    private SQLiteDatabase mDatabase;

    public dbHelper( Context context) {
        super(context, DBNAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table users(id INTEGER primary key, fname TEXT, phone TEXT, shoulder TEXT, chest TEXT, height TEXT, height1 TEXT, dateap TEXT, dale TEXT, waist TEXT, footwidth TEXT, imageName TEXT, tprice TEXT, advance TEXT, comment TEXT, style TEXT, cflag INTEGER, img TEXT)");
        db.execSQL("create table permission(permit TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists users");
    }


    //insert users into the sqlite database with this method
    public boolean inserUser(String fname, String phone, String shoulder, String chest, String height,String height1,String dateap,
                             String dale, String waist, String footwidth, String imageName, String tprice,
                             String advance, String comment, String style, String flag, String imgurl){

        try{
            //used to calculate the final date of the appointment and save it to db
            SimpleDateFormat sdt2 = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DATE, Integer.parseInt(dateap));
            String dateapc = sdt2.format(cal2.getTime());

            ContentValues contentValues = new ContentValues();
            contentValues.put("fname", fname);
            contentValues.put("phone", phone);
            contentValues.put("shoulder", shoulder);
            contentValues.put("chest", chest);
            contentValues.put("height", height);
            contentValues.put("height1", height1);
            contentValues.put("dateap", dateapc);
            contentValues.put("dale", dale);
            contentValues.put("waist", waist);
            contentValues.put("footwidth", footwidth);
            contentValues.put("imageName", imageName);
            contentValues.put("tprice", tprice);
            contentValues.put("advance", advance);
            contentValues.put("comment", comment);
            contentValues.put("style", style);
            contentValues.put("cflag", flag);
            contentValues.put("img", imgurl);

            mDatabase = this.getWritableDatabase();
            long result = mDatabase.insert("users", null, contentValues);

            return result == -1;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }
        return true;
    }

    //update mezgebru table
    public boolean updateUser(String fname, String phone, String shoulder, String chest, String height,String height1, String dateap,
                             String dale, String waist, String footwidth, String tprice,
                             String advance,String imageName, String comment, String style){

         try{
             mDatabase = this.getWritableDatabase();
             ContentValues contentValues = new ContentValues();
             contentValues.put("fname", fname);
             contentValues.put("phone", phone);
             contentValues.put("shoulder", shoulder);
             contentValues.put("chest", chest);
             contentValues.put("height", height);
             contentValues.put("height1", height1);
             contentValues.put("dateap", dateap);
             contentValues.put("dale", dale);
             contentValues.put("waist", waist);
             contentValues.put("footwidth", footwidth);
             contentValues.put("imageName", imageName);
             contentValues.put("tprice", tprice);
             contentValues.put("advance", advance);
             contentValues.put("comment", comment);
             contentValues.put("style", style);

             long result = mDatabase.update("users",  contentValues, "fname=?", new String[]{fname});

             return result == -1;
         }catch (Exception e){
             Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
         }finally {
             mDatabase.close();
         }

         return true;
    }

    //get every thing from the database method
    public List<userDAO> getUsers(){

        Cursor c=null;
       try{
           List<userDAO> uList = new ArrayList<>();
           mDatabase = this.getReadableDatabase();
          c = mDatabase.rawQuery("select * from users where cflag=5", null);

           if (c.getCount()>0) {
               c.moveToFirst();
               while (!c.isAfterLast()) {
                   userDAO user = new userDAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                           c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                           c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15), c.getString(16));

                   uList.add(user);
                   c.moveToNext();
               }
           }
           return uList;
       }catch (Exception e){
           Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
       }finally {
           mDatabase.close();
           c.close();
       }

       return null;
    }

    //Calculate and get total price
    public float getTotalPrice(){

        Cursor c=null;
        try{
            mDatabase = this.getReadableDatabase();
           c = mDatabase.rawQuery("select tprice from users", null);

            float sum = 0;
            if (c.getCount()>0){
                c.moveToFirst();
                while (!c.isAfterLast()){
                    sum += Float.parseFloat(c.getString(0));
                    c.moveToNext();
                }
            }
            return sum;
        }catch (Exception e){

             }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }
        return 0;
    }

    //Calculate and get total kebd
    public float getTotalkebd(){

        Cursor c=null;
        try{
            mDatabase = this.getReadableDatabase();
           c = mDatabase.rawQuery("select advance from users", null);

            float sum = 0;
            if (c.getCount()>0){
                c.moveToFirst();
                while (!c.isAfterLast()){
                    sum += Float.parseFloat(c.getString(0));
                    c.moveToNext();
                }
            }
            return sum;
        }catch (Exception e){

             }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }

        return 0;
    }
    //check user name for new user registration
    public boolean checkUser(String name){
        Cursor c=null;
        try{
            mDatabase = this.getReadableDatabase();

            c = mDatabase.rawQuery("select * from users where fname=? ", new String[]{name});

            return  c.getCount() > 0;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }

        return false;
    }

    //select users that got delete flag
    public List<userDAO> getcUsers(){

        try{
            userDAO user;
            List<userDAO> uList = new ArrayList<>();
            mDatabase = this.getReadableDatabase();
            Cursor c = mDatabase.rawQuery("select * from users where cflag=?", new String[]{"10"});

            if (c.getCount()>0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    user = new userDAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                            c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15), c.getString(16));

                    uList.add(user);
                    c.moveToNext();
                }
            }
            c.close();

            return uList;
        }catch(Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }

        return null;
    }

    //delete user from completed flag
    public boolean updateCflag(String name){
        try{
            mDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("cflag", 10);
            int result = mDatabase.update("users", contentValues,"fname=?", new String[]{name});

            return result==-1;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }
        return true;
    }

    //delete user from completed flag
    public boolean deleteUsers(String name){
        try{
            mDatabase = this.getWritableDatabase();

            int result = mDatabase.delete("users","fname=?", new String[]{name});

            return result==-1;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }

        return true;
    }


    public List<userDAO> getUsersForList(int id){

        Cursor c=null;
        try{
            List<userDAO> uList = new ArrayList<>();
            mDatabase = this.getReadableDatabase();
            c = mDatabase.rawQuery("select * from users where id=?", new String[]{String.valueOf(id)});

            if (c.getCount()>0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    userDAO user = new userDAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                            c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15), c.getString(16));

                    uList.add(user);
                    c.moveToNext();
                }
            }
            return uList;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }

        return null;
    }

    //update the cFlag back to 5 to display it to home
    public boolean updateCUser(String fname) {
        try{
            mDatabase = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("cflag", 5);
            int result = mDatabase.update("users", contentValues,"fname=?", new String[]{fname});

            return result==-1;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }

        return true;
    }

    //Search users from db
    public List<userDAO> searchUsers(String name){

        Cursor c=null;
        try{
            List<userDAO> uList = new ArrayList<>();
            mDatabase = this.getReadableDatabase();
            c = mDatabase.rawQuery("select * from users where fname=? AND cflag=5 ", new String[]{name});

            if (c.getCount()>0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    userDAO user = new userDAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                            c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15), c.getString(16));

                    uList.add(user);
                    c.moveToNext();
                }
            }
            return uList;
        }catch(Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }

        return null;
    }

    //Get all user to export
    public List<userDAO> getToExUsers(){

        Cursor c=null;
        try{
            List<userDAO> uList = new ArrayList<>();
            mDatabase = this.getReadableDatabase();
            c = mDatabase.rawQuery("select * from users", null);

            if (c.getCount()>0) {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    userDAO user = new userDAO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),
                            c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10),
                            c.getString(11), c.getString(12), c.getString(13), c.getString(14), c.getString(15), c.getString(16));

                    uList.add(user);
                    c.moveToNext();
                }
            }
            return uList;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
            assert c != null;
            c.close();
        }

        return null;
    }

    public boolean inserUserEx(String fname, String phone, String shoulder, String chest, String height,String height1,String dateap,
                             String dale, String waist, String footwidth, String imageName, String tprice,
                             String advance, String comment, String style, String flag){


        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("fname", fname);
            contentValues.put("phone", phone);
            contentValues.put("shoulder", shoulder);
            contentValues.put("chest", chest);
            contentValues.put("height", height);
            contentValues.put("height1", height1);
            contentValues.put("dateap", dateap);
            contentValues.put("dale", dale);
            contentValues.put("waist", waist);
            contentValues.put("footwidth", footwidth);
            contentValues.put("imageName", imageName);
            contentValues.put("tprice", tprice);
            contentValues.put("advance", advance);
            contentValues.put("comment", comment);
            contentValues.put("style", style);
            contentValues.put("cflag", flag);

            mDatabase = this.getWritableDatabase();
            long result = mDatabase.insert("users", null, contentValues);

            return result == -1;
        }catch (Exception e){
            Toast.makeText(mContext, "ውስጣዊ ችግር ተፈጥሮአል መተግበሪያውን ዘግተው ድጋሚ ይክፈቱ", Toast.LENGTH_LONG).show();
        }finally {
            mDatabase.close();
        }

        return true;
    }
}
