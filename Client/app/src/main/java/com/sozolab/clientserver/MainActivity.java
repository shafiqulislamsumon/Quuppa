package com.sozolab.clientserver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String serverIP = "192.168.11.2"; // 172.18.13.255
    int port = 4445; // 43261
    Button startButton;
    Button stopButton;
    TextView stateTextView;
    ListView udpDataListView;

    UdpClientHandler udpClientHandler;
    UdpClientThread udpClientThread;
    ArrayList<UDPData> listData;
    UDPListAdapter udpListAdapter;
    private static final int PERMISSION_REQUEST_CODE = 200;
    String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start);
        stopButton = (Button) findViewById(R.id.stop);
        stateTextView = (TextView)findViewById(R.id.state);
        udpDataListView = (ListView) findViewById(R.id.udpListView);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        listData = new ArrayList<UDPData>();
        udpClientHandler = new UdpClientHandler(this);

        UDPData udpData = new UDPData("UDP Message");
        listData.add(udpData);
        udpListAdapter = new UDPListAdapter(this, listData);
        udpDataListView.setAdapter(udpListAdapter);

        if (!checkPermission()) {
            requestPermission();
        } else {
            Log.d(TAG, "Permission already granted..");
        }

    }

    private void updateState(String state){
        stateTextView.setText("State : " + state);
    }

    private void updateMessage(String message){
        UDPData udpData = new UDPData(message);
        listData.add(udpData);
        udpListAdapter.notifyDataSetChanged();
    }

    private void clientEnd(){
        udpClientThread = null;
        stateTextView.setText("State : " + "client end");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.start:
                udpClientThread = new UdpClientThread(this, serverIP, port, udpClientHandler);
                udpClientThread.setRunning(true);
                udpClientThread.start();
                break;

            case R.id.stop:
                udpClientThread.setRunning(false);
                udpClientThread.closeSocket();
                break;

            default:
                break;
        }
    }

    private boolean checkPermission() {
        int writeResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return writeResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        Log.d(TAG, "Permission granted");
                    } else {
                        Log.d(TAG, "Permission denied");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow write permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public UdpClientHandler(MainActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateMessage((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}
