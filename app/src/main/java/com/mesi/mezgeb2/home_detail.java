package com.mesi.mezgeb2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class home_detail extends Fragment {

    TextView fname;
    EditText phone;
    EditText shoulder;
    EditText chest;
    EditText height;
    EditText height1;
    EditText chapa;
    EditText waist;
    EditText fwidth;
    EditText appointment;
    EditText tfee;
    EditText kebd;
    EditText comment;
    EditText style;
    EditText imageName;
    ImageView csamleImage;

    Button update;
    Button delete;
    listViewSingleUserList hcdao;
    dbHelper db;
    public home_detail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_list_view_detail, container, false);

        initWidgetAndSetVal(v);

        return v;
    }

    private void initWidgetAndSetVal(View v) {
        hcdao = listViewSingleUserList.listViewSingleInstance();

        fname = v.findViewById(R.id.home_detail_full_name);
        phone = v.findViewById(R.id.home_detail_phone);
        shoulder = v.findViewById(R.id.home_detail_sholder);
        chest = v.findViewById(R.id.home_detail_chest);
        height = v.findViewById(R.id.home_detail_height);
        height1 = v.findViewById(R.id.home_detail_height1);
        chapa = v.findViewById(R.id.home_detail_chapa);
        waist = v.findViewById(R.id.home_detail_waist);
        fwidth = v.findViewById(R.id.home_detail_footwidth);
        appointment = v.findViewById(R.id.home_detail_appointment);
        tfee = v.findViewById(R.id.home_detail_total_fee);
        kebd = v.findViewById(R.id.home_detail_kebdi);
        comment = v.findViewById(R.id.home_detail_comment);
        style = v.findViewById(R.id.home_detail_fashn);
        imageName = v.findViewById(R.id.home_detail_imagename);
        csamleImage = v.findViewById(R.id.csamleImage);

        update = v.findViewById(R.id.update_user);
        delete = v.findViewById(R.id.delete_user);

        fname.setText(hcdao.getFname());
        phone.setText(hcdao.getPhone());
        shoulder.setText(hcdao.getShoulder());
        chest.setText(hcdao.getChest());
        height.setText(hcdao.getHeight());
        height1.setText(hcdao.getHeight1());
        imageName.setText(hcdao.getImageName());
        chapa.setText(hcdao.getDale());
        waist.setText(hcdao.getWaist());
        fwidth.setText(hcdao.getFootWidth());
        appointment.setText(calculateDate(hcdao.getDateap()));
        tfee.setText(hcdao.getTotalFee());
        kebd.setText(hcdao.getAdvancePayment());
        comment.setText(hcdao.getComment());
        style.setText(hcdao.getStyle());

        Picasso.get().load(new File(hcdao.getImagePath())).into(csamleImage);

        db = new dbHelper(getContext());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (appointment.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "ቀጠሮ ባዶ መሆን የለበትም", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (appointment.getText().toString().length()>3){
                    Toast.makeText(getContext(), appointment.getText().toString()+" የቀጠሮ ቀን መሆን አይችልም", Toast.LENGTH_LONG).show();
                    return;
                }

                //used to calculate the final date of the appointment and save it to db
                SimpleDateFormat sdt2 = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal2 = Calendar.getInstance();
                cal2.add(Calendar.DATE, Integer.parseInt(appointment.getText().toString()));
               String updateDate = sdt2.format(cal2.getTime());


                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert!");
                alert.setMessage("መረጃውን ለመቀየር ይስማማሉ?");
                alert.setCancelable(false);
                alert.setPositiveButton("አዎ",(DialogInterface.OnClickListener) (dialog, which) -> {

                    if (db.updateUser(fname.getText().toString(), phone.getText().toString(), shoulder.getText().toString(), chest.getText().toString(),height.getText().toString(),
                            height1.getText().toString(),updateDate, chapa.getText().toString(), waist.getText().toString(),fwidth.getText().toString(),tfee.getText().toString(),kebd.getText().toString(),imageName.getText().toString(), comment.getText().toString(),style.getText().toString()))
                        Toast.makeText(getContext(), "ከንደገና ይሞክሩ", Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(getContext(), "ተቀይሮአል", Toast.LENGTH_SHORT).show();
                        replaceFragment();
                    }

                });

                alert.setNegativeButton("አይ", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                AlertDialog alertDialog = alert.create();

                alertDialog.show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert!");
                alert.setMessage("እርግጠኛ ነዎት ሰርተው ጨርሰዋል?");
                alert.setCancelable(false);
                alert.setPositiveButton("አዎ",(DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button user will be deleted
                    if(db.updateCflag(hcdao.getFname()))
                        Toast.makeText(getContext(), "አልተሳካም", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "ተሳክቶአል", Toast.LENGTH_SHORT).show();
                    replaceFragment();
                });

                alert.setNegativeButton("አይ", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                AlertDialog alertDialog = alert.create();

                alertDialog.show();

            }
        });

    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new home());
        fragmentTransaction.commit();
    }

    private String calculateDate(String date) {

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

        String x = String.valueOf(re/86400000);
        return x;
    }

}
