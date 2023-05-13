package com.mesi.mezgeb2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class completed_detail extends Fragment {

    TextView fname;
    TextView phone;
    TextView shoulder;
    TextView chest;
    TextView height;
    TextView height1;
    TextView chapa;
    TextView waist;
    TextView fwidth;
    TextView appointment;
    TextView tfee;
    TextView kebd;
    TextView comment;
    TextView style;

    TextView imageName;
    ImageView csamleImage;


    Button notYet;
    Button delete4v;

    public completed_detail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.completed_list_view_detail, container, false);

        initWidgetAndSetVal(v);

        return v;
    }

    private void initWidgetAndSetVal(View v) {

        fname = v.findViewById(R.id.comp_detail_full_name);
        phone = v.findViewById(R.id.comp_detail_phone);
        shoulder = v.findViewById(R.id.comp_detail_sholder);
        chest = v.findViewById(R.id.comp_detail_chest);
        height = v.findViewById(R.id.comp_detail_height);
        height1 = v.findViewById(R.id.comp_detail_height1);
        imageName = v.findViewById(R.id.image_name);
        chapa = v.findViewById(R.id.comp_detail_chapa);
        waist = v.findViewById(R.id.comp_detail_waist);
        fwidth = v.findViewById(R.id.comp_detail_footwidth);
        appointment = v.findViewById(R.id.comp_detail_appointment);
        tfee = v.findViewById(R.id.comp_detail_total_fee);
        kebd = v.findViewById(R.id.kebd_detail_kebdi);
        comment = v.findViewById(R.id.comp_detail_comment);
        style = v.findViewById(R.id.comp_detail_fashn);
        notYet = v.findViewById(R.id.notyet);
        delete4v = v.findViewById(R.id.forevdelete);
        csamleImage = v.findViewById(R.id.csamleImage);

        listViewSingleUserList lvc =  listViewSingleUserList.listViewSingleInstance();

        fname.setText(lvc.getFname());
        phone.setText(lvc.getPhone());
        shoulder.setText(lvc.getShoulder());
        chest.setText(lvc.getChest());
        height.setText(lvc.getHeight());
        height1.setText(lvc.getHeight1());
        imageName.setText(lvc.getImageName());
        chapa.setText(lvc.getDale());
        waist.setText(lvc.getWaist());
        fwidth.setText(lvc.getFootWidth());
        appointment.setText(calculateDate(lvc.getDateap()));
        tfee.setText(lvc.getTotalFee());
        kebd.setText(lvc.getAdvancePayment());
        comment.setText(lvc.getComment());
        style.setText(lvc.getStyle());

        Picasso.get().load(new File(lvc.getImagePath())).into(csamleImage);

        notYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert!");
                alert.setMessage("ወዳላለቁ ስራወች ውስጥ ይግባ?");
                alert.setCancelable(false);
                alert.setPositiveButton("ይግባ",(DialogInterface.OnClickListener) (dialog, which) -> {

                    dbHelper db = new dbHelper(v.getContext());
                    if(db.updateCUser(lvc.getFname()))
                        Toast.makeText(getContext(), "አልተሳካም", Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(getContext(), "ተሳክቶአል", Toast.LENGTH_SHORT).show();
                        replaceFragment();
                    }

                });

                alert.setNegativeButton("አይግባ", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                AlertDialog alertDialog = alert.create();

                alertDialog.show();

            }
        });

        delete4v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper db = new dbHelper(v.getContext());
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Alert!");
                alert.setMessage("ከጠፋ መልሶ ምግኘት አይቻልም");
                alert.setCancelable(false);
                alert.setPositiveButton("ይጥፋ",(DialogInterface.OnClickListener) (dialog, which) -> {
                    // When the user click yes button user will be deleted
                    if(db.deleteUsers(lvc.getFname()))
                        Toast.makeText(getContext(), "አልተሳካም", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "ተሳክቶአል", Toast.LENGTH_SHORT).show();
                    replaceFragment();
                });

                alert.setNegativeButton("አይጥፋ ", (DialogInterface.OnClickListener) (dialog, which) -> {
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
        fragmentTransaction.replace(R.id.frame_layout, new done());
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