package com.sozolab.clientserver;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    String TAG = UdpClientThread.class.getName();

    int port;
    String serverIP;
    private boolean running;
    Context context;
    MainActivity.UdpClientHandler handler;

    DatagramSocket socket;
    DatagramPacket packet;
    InetAddress address;
    LocationJson locationJson;

    public UdpClientThread(Context context, String serverIP, int port, MainActivity.UdpClientHandler handler) {
        super();
        this.context = context;
        this.serverIP = serverIP;
        this.port = port;
        this.handler = handler;
        locationJson = new LocationJson(context);
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void closeSocket(){
        Log.d(TAG, "closeSocket()");

        if(socket != null){
            socket.close();
            handler.sendEmptyMessage(MainActivity.UdpClientHandler.UPDATE_END);
        }
    }

    private void sendState(String state){
        handler.sendMessage(Message.obtain(handler, MainActivity.UdpClientHandler.UPDATE_STATE, state));
    }

    @Override
    public void run() {

        try{
            socket = new DatagramSocket(port);
            address = InetAddress.getByName(serverIP);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while(running){
            try {

                // send request
                byte[] buf = new byte[2048];

                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

                sendState("connected");

                // get response
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                locationJson.saveJson(message);
                //locationJson.readJson();
                //Log.d(TAG, "message : " + message);

                handler.sendMessage(Message.obtain(handler, MainActivity.UdpClientHandler.UPDATE_MSG, message));
                Thread.sleep(1000);

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
