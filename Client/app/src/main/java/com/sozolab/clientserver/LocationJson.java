package com.sozolab.clientserver;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class LocationJson {

    Context context;
    String path;
    String TAG = LocationJson.class.getName();

    public LocationJson(Context context){
        this.context = context;
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "QuuppaData";
        createQuuppaFolder();
    }

    public void createQuuppaFolder() {

        try {
            File quuppaFolderDir = new File(path);

            if (!quuppaFolderDir.exists()) {
                quuppaFolderDir.mkdirs();
                Log.d(TAG, "Quuppa folder directory : " + path); ///storage/emulated/0/QuuppaData
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveJson(String message){
        Log.d(TAG, message);
        try {
            JSONObject jsonObject = new JSONObject(message);
            double timeStamp = jsonObject.getDouble("positionTS");
            String id = jsonObject.getString("id");
            String fileName = id + "_" + String.format("%.0f", timeStamp);
            Log.d(TAG, fileName);

            String outputFilePath = path + File.separator + fileName + ".json";
            File file = new File(outputFilePath);
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(jsonObject.toString());
            output.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readJson(){
        try {
            String fileName = "1585638748369.json";
            File file = new File(path, fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            // This response will have Json Format String
            String response = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("position");
            Log.d(TAG, "Response : " + jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
