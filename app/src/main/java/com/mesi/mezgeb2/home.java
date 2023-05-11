package com.mesi.mezgeb2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class home extends Fragment {

    EditText searchTxt;
    Button searchBtn;
    dbHelper db;
    ListView lv;
    homeAdapter adapter;
    List<userDAO> userDAOList;
    public home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);


        //load the list in this method
        loadintoMemUsers(v);
        searchMethod(v);

        return v;
    }

    private void searchMethod(View v) {
        searchTxt = v.findViewById(R.id.search_text);
        lv = v.findViewById(R.id.home_list);
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filters(s.toString());
            }
        });

    }

    private void filters(String toString) {

        db = new dbHelper(getContext());
        userDAOList = db.searchUsers(searchTxt.getText().toString());

        //filtered LinkedList
        LinkedList<userDAO> filteredUsers = new LinkedList<>();

        for (int i = 0; i < userDAOList.size(); i++) {
            if (userDAOList.get(i).getFname().equalsIgnoreCase(toString))
            {
                filteredUsers.add(userDAOList.get(i));
            }
        }

        adapter = new homeAdapter(getContext(), filteredUsers);
        lv.setAdapter(adapter);

    }


    private void loadintoMemUsers(View v) {

        lv = v.findViewById(R.id.home_list);
        db = new dbHelper(v.getContext());

        userDAOList = db.getUsers();

        adapter = new homeAdapter(getContext(), userDAOList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapte, View view, int position, long l) {

              int id =  (int)adapte.getItemIdAtPosition(position);
             List<userDAO> clickedDAOS =  db.getUsersForList(id);

                listViewSingleUserList ulist = listViewSingleUserList.listViewSingleInstance();
                ulist.setFname(clickedDAOS.get(0).getFname());
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
               replaceFragment(new home_detail());
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}