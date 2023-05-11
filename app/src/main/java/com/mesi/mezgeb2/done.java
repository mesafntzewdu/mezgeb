package com.mesi.mezgeb2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class done extends Fragment {



    public done() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_done, container, false);

        listUserMethod(v);

        return v;
    }

    private void listUserMethod(View v) {
        ListView lv;
        dbHelper db;
        homeAdapter adapter;
        List<userDAO> userList;

        lv = v.findViewById(R.id.compeleted_list);

        db = new dbHelper(v.getContext());

        userList = db.getcUsers();

        adapter = new homeAdapter(v.getContext(), userList);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapte, View view, int position, long l) {

                int id =  (int)adapte.getItemIdAtPosition(position);
                List<userDAO> clickedDAOS =  db.getUsersForList(id);

                listViewSingleUserList ulist = listViewSingleUserList.listViewSingleInstance();
                ulist.setFname(clickedDAOS.get(0).getFname());
                ulist.setFdate(clickedDAOS.get(0).getFdate());
                ulist.setPhone(clickedDAOS.get(0).getPhone());
                ulist.setShoulder(clickedDAOS.get(0).getShoulder());
                ulist.setChest(clickedDAOS.get(0).getChest());
                ulist.setHeight(clickedDAOS.get(0).getHeight());
                ulist.setHeight1(clickedDAOS.get(0).getHeight1());
                ulist.setDateap(clickedDAOS.get(0).getDateap());
                ulist.setDale(clickedDAOS.get(0).getDale());
                ulist.setWaist(clickedDAOS.get(0).getWaist());
                ulist.setFootWidth(clickedDAOS.get(0).getFootWidth());
                ulist.setImageName(clickedDAOS.get(0).getImageName());
                ulist.setAdvancePayment(clickedDAOS.get(0).getAdvancePayment());
                ulist.setTotalFee(clickedDAOS.get(0).getTotalFee());
                ulist.setComment(clickedDAOS.get(0).getComment());
                ulist.setStyle(clickedDAOS.get(0).getStyle());
                Toast.makeText(getContext(), ""+clickedDAOS.get(0).getFname(), Toast.LENGTH_SHORT).show();

                replaceFragment();
            }
        });
    }

    private void replaceFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new completed_detail());
        fragmentTransaction.commit();
    }
}