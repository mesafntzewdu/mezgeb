package com.mesi.mezgeb2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.BitmapCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Objects;

import javax.xml.transform.Result;

public class add extends Fragment {

    private String[] PERMISSIONS;
    private String spinner_style = null;
    private Bitmap bm;
    ImageView img;

    add frgAdd;

    public add() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, container, false);
        //Initialize array of permissions
        PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        frgAdd = new add();

        //image view widget instance
        img = v.findViewById(R.id.samleImage);

        addusermethod(v);
        addSampleImage(v);

        return v;
    }

    private void addusermethod(View v) {
        TextView fname = v.findViewById(R.id.user_name);
        TextView phone_no = v.findViewById(R.id.phone_no);
        TextView shoulder = v.findViewById(R.id.shoulder);
        TextView chest = v.findViewById(R.id.chest);
        TextView height = v.findViewById(R.id.height);
        TextView height1 = v.findViewById(R.id.height2);
        TextView keteroken = v.findViewById(R.id.keteroken);
        TextView hip = v.findViewById(R.id.hip);
        TextView waist = v.findViewById(R.id.waist);
        TextView foot_width = v.findViewById(R.id.foot_width);
        TextView image_name = v.findViewById(R.id.image_name);
        TextView total_fee = v.findViewById(R.id.total_fee);
        TextView advance_payment = v.findViewById(R.id.advance_payment);
        TextView comment = v.findViewById(R.id.comment);

        Spinner suri_spinner = v.findViewById(R.id.suri_spinner);
        Spinner t_shirt_spinner = v.findViewById(R.id.t_shirt_spinner);
        Spinner kemis_spinner = v.findViewById(R.id.kemis_spinner);
        Spinner shfnsf_spinner = v.findViewById(R.id.shfnsf_spinner);
        Spinner africa_spinner = v.findViewById(R.id.africa_spinner);


        //Set spinner selected value to the global variable for later use
        suriSpinnerMethod(suri_spinner, v);
        tshirtSpinnerMethod(t_shirt_spinner, v);
        kemisSpinnerMethod(kemis_spinner, v);
        shfnsfSpinnerMethod(shfnsf_spinner, v);
        africaSpinnerMethod(africa_spinner, v);

        //Add to database when this method is clicked
        Button submit = v.findViewById(R.id.submit);
        dbHelper db = new dbHelper(getContext());

        submit.setOnClickListener(view -> {

            if (storageCheckPermission()) {
                //if its true leave it to run to the insertion
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
                    storagePermission.launch(PERMISSIONS[0]);
                }

                return;
            }


            if (fname.getText().toString().equals("")) {
                Toast.makeText(getContext(), "ስም ባዶ መሆን የለበትም!", Toast.LENGTH_LONG).show();
                return;
            }

            if (keteroken.getText().toString().equals("")) {
                Toast.makeText(getContext(), "የቀጠሮ ቀን ባዶ መሆን የለበትም!", Toast.LENGTH_LONG).show();
                return;
            }

            if (db.checkUser(fname.getText().toString())) {
                Toast.makeText(getContext(), "ያልተመዘገበ ስም ይጠቀሙ!", Toast.LENGTH_LONG).show();
                return;
            }

            if (total_fee.getText().toString().equals("")) {
                Toast.makeText(getContext(), "ጠቅላላ ዋጋ ባዶ መሆን የለበትም!", Toast.LENGTH_LONG).show();
                return;
            }

            if (keteroken.getText().toString().length() > 3) {
                Toast.makeText(getContext(), keteroken.getText().toString() + " የቀጠሮ ቀን መሆን አይችልም", Toast.LENGTH_LONG).show();
                return;
            }

            if (advance_payment.getText().toString().equals("")) {
                Toast.makeText(getContext(), "ቀብድ ባዶ መሆን የለበትም!", Toast.LENGTH_LONG).show();
                return;
            }

            if (bm == null) {
                Toast.makeText(getContext(), "\"Sample\" ባዶ መሆን የለበትም!", Toast.LENGTH_LONG).show();
                return;
            }

            //Add Image into the folder and absolute path into the database
            Log.d("path", getImageAbPath());

            boolean result = db.inserUser(fname.getText().toString(), phone_no.getText().toString(), shoulder.getText().toString(), chest.getText().toString(), height.getText().toString(), height1.getText().toString(), keteroken.getText().toString(), hip.getText().toString(), waist.getText().toString(),
                    foot_width.getText().toString(), image_name.getText().toString(), total_fee.getText().toString(), advance_payment.getText().toString(), comment.getText().toString(), spinner_style, String.valueOf(5), getImageAbPath());

            if (result)
                Toast.makeText(v.getContext(), "ምዝገባው አልተሳካም", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(v.getContext(), "ምዝገባው ተሳክቶአል", Toast.LENGTH_SHORT).show();


        });
    }

    private String getImageAbPath() {

        File folder = new File(Environment.getExternalStorageDirectory() + "/MEZGEB_DATA/");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/MEZGEB_DATA/file" + Calendar.getInstance().getTimeInMillis() + "sample.png");


        try {
            FileOutputStream fos = new FileOutputStream(file);

            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();

            Log.d("File Write", "Succeeded");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private void kemisSpinnerMethod(Spinner kemis_spinner, View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.kemis, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kemis_spinner.setAdapter(adapter);
        kemis_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void africaSpinnerMethod(Spinner africa_spinner, View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.africa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        africa_spinner.setAdapter(adapter);
        africa_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void shfnsfSpinnerMethod(Spinner shfnsf_spinner, View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.shfnsff, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shfnsf_spinner.setAdapter(adapter);
        shfnsf_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void tshirtSpinnerMethod(Spinner t_shirt_spinner, View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.t_shirt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        t_shirt_spinner.setAdapter(adapter);
        t_shirt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void suriSpinnerMethod(Spinner suri_spinner, View v) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.suri, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suri_spinner.setAdapter(adapter);
        suri_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_style = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    //add sample image into folder and database
    private void addSampleImage(View v) {

        img = v.findViewById(R.id.samleImage);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoCamOrGallery();
            }
        });
    }

    private void photoCamOrGallery() {

        if (checkCameraPermission()) {
            getImageBitmap();
        } else {
            cameraPermission.launch(PERMISSIONS[1]);
        }
    }

    //Check if the storage permission is granted or not
    private boolean storageCheckPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED;
        }
    }

    //Camera permission checked here
    private boolean checkCameraPermission() {

        return ContextCompat.checkSelfPermission(requireContext(), PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;

    }


    ActivityResultLauncher<String> storagePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            //getImageBitmap();
        } else {
            Toast.makeText(getContext(), "All permissions has to be granted to work with the app.", Toast.LENGTH_LONG).show();
        }

    });

    ActivityResultLauncher<String> cameraPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

        if (isGranted) {
            getImageBitmap();
        } else {
            Toast.makeText(getContext(), "All permissions has to be granted to work with the app.", Toast.LENGTH_LONG).show();
        }

    });

    //Get image bitmap from camera or gallery
    private void getImageBitmap() {

        String[] items = {"Camera", "Gallery"};

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setCancelable(false);
        alert.setItems(items, (dialogInterface, position) -> {
            if (items[position].equals("Camera")) {
                openCameraAndTakePicture();
            }
            if (items[position].equals("Gallery")) {
                openGalleryAndSavePicture();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();

    }

    //Open gallery and get image bitmap to store on the local variable
    private void openGalleryAndSavePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select image"), 201);
    }

    //Open camera and get image bitmap to store on the local variable
    private void openCameraAndTakePicture() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            //Data from camera
            if (requestCode == 200) {

                if (data != null) {
                    bm = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(bm);
                }
            }
            //Data from gallery
            if (requestCode == 201) {

                if (data != null) {
                    try {
                        InputStream input = getContext().getContentResolver().openInputStream(data.getData());

                        bm = BitmapFactory.decodeStream(input);
                        img.setImageBitmap(bm);

                    } catch (Exception e) {

                    }
                }
            }
        }

    }
}