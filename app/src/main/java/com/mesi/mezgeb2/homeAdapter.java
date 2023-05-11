package com.mesi.mezgeb2;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class homeAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<userDAO> mUserList;

    public homeAdapter(Context mContext, List<userDAO> mUserList) {
        this.mContext = mContext;
        this.mUserList = mUserList;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mUserList.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") View v = View.inflate(mContext, R.layout.home_list_view, null);

       TextView uname = v.findViewById(R.id.home_list_uname);
       TextView style = v.findViewById(R.id.home_list_style);
       TextView dayleft = v.findViewById(R.id.home_list_dayleft);
       ImageView img = v.findViewById(R.id.home_list_image);

        String a = mUserList.get(position).getDateap();

       uname.setText(mUserList.get(position).getFname());
       style.setText(mUserList.get(position).getStyle());

       if (calculateDate(a)<0)
           dayleft.setText(String.valueOf(1));
       else
           dayleft.setText(String.valueOf(calculateDate(a)));
       //calculate the remaining date and set the image icon as the date left
        int filter = 6;
        int dbFilter = FilterClass.getFilterInstance().getFilter_data();
        if (dbFilter!=0)
            filter = dbFilter;

        if(calculateDate(a)<filter)
            img.setImageResource(R.drawable.r);
        else
            img.setImageResource(R.drawable.g);
        return v;
    }

    private long calculateDate(String date) {

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        //User last day calculated

        char da = date.charAt(0);
        char d1 = date.charAt(1);

        char ma = date.charAt(3);
        char m1 = date.charAt(4);

        char ya = date.charAt(6);
        char y1 = date.charAt(7);
        char yb = date.charAt(8);
        char yc = date.charAt(9);

        String day = ""+da+d1;

        String month = ""+ma+m1;

        String year = ""+ya+y1+yb+yc;

        cal2.set(Integer.parseInt(year), Integer.parseInt(month)-1,Integer.parseInt(day));

        long re = cal2.getTimeInMillis() - cal.getTimeInMillis();

        return re/86400000;
    }
}
