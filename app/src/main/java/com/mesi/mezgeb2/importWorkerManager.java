package com.mesi.mezgeb2;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class importWorkerManager extends Worker {
    public importWorkerManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        importTaskDone();

        return Result.success();
    }

    public void importTaskDone(){

        String PATH =  Environment.getExternalStorageDirectory()+"/MEZGEB_DATA/user_data.csv";

        try {
            dbHelper db = new dbHelper(getApplicationContext());
            CSVReader readerCh = new CSVReader(new FileReader(PATH));
            readerCh.readAll();

            if (db.getToExUsers().size()==readerCh.getLinesRead()){
                toastMethod("'Import' ማድረግ አያስፈልጎትም Database & File አንዳይነት ነው");
                return;
            }
            readerCh.close();
            CSVReader reader = new CSVReader(new FileReader(PATH));
            String[] nextLine;
            while ((nextLine = reader.readNext())!=null){

                if (!db.checkUser(nextLine[1])){
                    db.inserUserEx(nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], nextLine[6],
                            nextLine[7],nextLine[8], nextLine[9], nextLine[10], nextLine[11], nextLine[12],nextLine[13], nextLine[14], nextLine[15],nextLine[16]);
                }
            }
            toastMethod("ተሳክቶአል");

        }catch (FileNotFoundException e){
            toastMethod("user_data.csv እዚህ ፎልደር ውስጥ አላገኘነውም\n"+ Environment.getExternalStorageDirectory()+"/MEZGEB_DATA");
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public void toastMethod(String message){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });

    }
}
