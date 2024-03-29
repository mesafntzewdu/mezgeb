package com.mesi.mezgeb2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Ask telephony permission here
        telephonyPermissionMethod();

    }

    public void checkPay() {
        Calendar cal = Calendar.getInstance();

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.MONTH, Calendar.JUNE);
        cal2.set(Calendar.YEAR, 2023);
        cal2.set(Calendar.DATE, 15);

        if (getIMEIDeviceId().equals("c94c8a0952") || getIMEIDeviceId().equals("123456789") || cal.getTime().before(cal2.getTime())) {
            fragmentSwitch();
        } else {
            alertDialogForL();
        }
    }
    private void telephonyPermissionMethod() {

        String[] PERMISSION = new String[]{Manifest.permission.READ_PHONE_STATE};

        //check the phone state permission
        if (ContextCompat.checkSelfPermission(this, PERMISSION[0]) == PackageManager.PERMISSION_GRANTED) {

            checkPay();

        } else {
            readPhoneStatePermission.launch(PERMISSION[0]);
        }
    }

    ActivityResultLauncher<String> readPhoneStatePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{

        if (isGranted)
        {
            checkPay();
        }else
        {
            alertDialogForDeny();
        }
    });

    private void alertDialogForL() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Activation Alert.");
        alert.setMessage("ይህንን መተግበሪያ ለመጠቀም ከታች ባሉት ስልኮች ይደውሉ\n\t\tID:" +getIMEIDeviceId() + "\n\t\t1:0703747672\n\t\t2:0938654940\n\t\tመሳፍንት ዘውዱ");
        alert.setCancelable(false);

        AlertDialog builder = alert.create();
        builder.show();
    }

    //user deny phone read
    private void alertDialogForDeny() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Permission Alert.");
        alert.setMessage("ይህንን መተግበሪያ Verify ለማድረግ 'Allow' ሚለውን ይምረጡ");
        alert.setCancelable(false);

        AlertDialog builder = alert.create();
        builder.show();
    }

    //method to switch between the layout clicked
    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void fragmentSwitch() {
        //First load the home page
        replaceFragment(new home());
        //Bottom navigation listener and layout switching method
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.nav_home:
                    replaceFragment(new home());
                    break;

                case R.id.nav_add:
                    replaceFragment(new add());
                    break;

                case R.id.nav_statics:
                    replaceFragment(new done());
                    break;

                case R.id.nav_profile:
                    replaceFragment(new settings());
                    break;

            }

            return true;
        });
    }

    public String getIMEIDeviceId() {

        String deviceId;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);

            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }

}



