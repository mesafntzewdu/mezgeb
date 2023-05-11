package com.mesi.mezgeb2;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class exportWorkManager extends Worker {
    public exportWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {
            grantedTaskExport();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Result.success();
    }

    public void grantedTaskExport() throws IOException {

        try{
           String  PATH = Environment.getExternalStorageDirectory()+"/MEZGEB_DATA/user_data.csv";
            dbHelper db = new dbHelper(getApplicationContext());

            List<userDAO> userList = db.getToExUsers();

            CSVWriter writer = new CSVWriter(new FileWriter(PATH));

            String[] rowArray;

            for (int i = 0; i < userList.size(); i++) {
                rowArray = new String[]{String.valueOf(userList.get(i).getId()), userList.get(i).getFname(), userList.get(i).getPhone(),
                        userList.get(i).getShoulder(),userList.get(i).getChest(), userList.get(i).getHeight(), userList.get(i).getHeight1(),
                        userList.get(i).getDateap(), userList.get(i).getDale(), userList.get(i).getWaist(), userList.get(i).getFootWidth(),
                        userList.get(i).getImageName(), userList.get(i).getTotalFee(), userList.get(i).getAdvancePayment(), userList.get(i).getComment(), userList.get(i).getStyle(), userList.get(i).cflag()};

                writer.writeNext(rowArray);

            }
            writer.flush();
            writer.close();
            setToastString("ተሳክቶአል፤ እዚህ ፎልደር ውስጥ መረጃውን ያገኛሉ\n"+PATH);

        }catch(Exception e){
            setToastString("አልተሳካም ከንደገና ይሞክሩ");
        }
    }

    public void setToastString(String message){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
