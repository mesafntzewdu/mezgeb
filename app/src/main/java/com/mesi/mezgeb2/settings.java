package com.mesi.mezgeb2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class settings extends Fragment {

    TextView kebdbirr;
    TextView tbirr;
    TextView aboutUs;
    TextView importBtn;
    TextView exportBtn;
    EditText filter;
    Button submitFilter;
    Button importInfo;
    Button exportInfo;
    String[] PERMISSIONS;
    String PATH;
    WorkRequest importWorkerRequest;
    WorkRequest exportWorkerRequest;
    boolean ieFlag;

    public settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        //create worker instance here for import
        importWorkerRequest = OneTimeWorkRequest.from(importWorkerManager.class);

        //Create worker instance here for export
        exportWorkerRequest = OneTimeWorkRequest.from(exportWorkManager.class);

        //permission to write on the external storage
        PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Import and export flag- set export true;
        ieFlag = true;

        //String path for external storage to store the csv file
        PATH = Environment.getExternalStorageDirectory() + "/MEZGEB_DATA/file/user_data.csv";
        aboutUsMethod(v);
        importMethod(v);
        exportMethod(v);
        filterMethod(v);
        displayBirr(v);

        importAndExportInfo(v);

        return v;
    }


    private void importAndExportInfo(View v) {

        importInfo = v.findViewById(R.id.import_info);
        exportInfo = v.findViewById(R.id.export_info);

        importInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("'Import' ከማድረግዎ በፊት 'File system' ለይ MEZGEB_DATA የሚለው ፎልደር ውስጥ 'user_data.csv'ን ማስቀመጥ እንዳይረሱ");
                //alert.setIcon();
                alert.setCancelable(true);
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        exportInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setCancelable(true);
                alert.setMessage("'Export' ሲያደርጉ 'File system' ለይ ያለውን አጥፍተው ከ 'Database' አዲስ እየፈጠሩ ነው");

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

    }

    private void filterMethod(View v) {

        filter = v.findViewById(R.id.filter);
        submitFilter = v.findViewById(R.id.filter_button);

        submitFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filter.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "'Filter' ባዶ መሆን የለበትም", Toast.LENGTH_SHORT).show();
                    return;
                }
                FilterClass.getFilterInstance().setFilter_data(Integer.parseInt(filter.getText().toString()));
                Toast.makeText(v.getContext(), "ተሳክቶአል", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayBirr(View v) {
        kebdbirr = v.findViewById(R.id.kebdbirr);
        tbirr = v.findViewById(R.id.netprofit);

        dbHelper db = new dbHelper(getContext());
        kebdbirr.setText(String.valueOf(db.getTotalkebd()));
        tbirr.setText(String.valueOf(db.getTotalPrice()));
    }

    private void exportMethod(View v) {

        exportBtn = v.findViewById(R.id.exports);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    createFolder();
                    WorkManager.getInstance(getContext()).enqueue(exportWorkerRequest);

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        try {
                            Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            Uri ui = Uri.fromParts("package", requireActivity().getPackageName(), null);
                            i.setData(ui);

                            startActivity(i);
                        } catch (Exception e) {
                            Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(i);
                        }
                    } else {
                        storagePermissionExport.launch(PERMISSIONS[0]);
                    }

                }

            }
        });
    }


    private void importMethod(View v) {
        importBtn = v.findViewById(R.id.imports);
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {
                    createFolder();

                    WorkManager.getInstance(getContext()).enqueue(importWorkerRequest);
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        try {
                            Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            Uri ui = Uri.fromParts("package", requireActivity().getPackageName(), null);
                            i.setData(ui);

                            startActivity(i);
                        } catch (Exception e) {
                            Intent i = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(i);
                        }
                    } else {
                        storagePermissionImport.launch(PERMISSIONS[0]);
                    }

                }

            }
        });
    }

    //import method task performed here


    private void aboutUsMethod(View v) {

        aboutUs = v.findViewById(R.id.about_us);
        aboutUs.setOnClickListener(view -> {

            fragmentSwitch(new info());

        });
    }

    private void fragmentSwitch(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    //Permission and export methods are done here 
    //Folder Creating Mezgeb_data
    public void createFolder() {
        File file = new File(Environment.getExternalStorageDirectory() + "/MEZGEB_DATA/file");
        if (!file.exists())
            file.mkdir();
    }

    //permission method goes here
    public boolean checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(getContext(), PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED;
        }
    }

    ActivityResultLauncher<String> storagePermissionImport = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            WorkManager.getInstance(getContext()).enqueue(importWorkerRequest);
        } else {
            Toast.makeText(getContext(), "Allow ሚለውን ካልመረጡ 'Import' 'Export' ማድረግ አንችልም!", Toast.LENGTH_LONG).show();
        }

    });

    ActivityResultLauncher<String> storagePermissionExport = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            createFolder();
            WorkManager.getInstance(getContext()).enqueue(exportWorkerRequest);
        } else {
            Toast.makeText(getContext(), "Allow ሚለውን ካልመረጡ 'Import' 'Export' ማድረግ አንችልም!", Toast.LENGTH_LONG).show();
        }

    });
}