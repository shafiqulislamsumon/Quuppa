package com.sozolab;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class UDPServer {

    static UdpServerThread udpServerThread;

    public static void main(String[] args) throws IOException {
        System.out.println("Server start");
        System.out.println("Runtime Java: " + System.getProperty("java.runtime.version"));
        
        new UdpServerThread().start();
    }
    
	private static class UdpServerThread extends Thread{
        final int serverport = 4445;
        
        protected DatagramSocket socket = null;
        
        public UdpServerThread() throws IOException {
            this("UdpServerThread");
        }
        
        public UdpServerThread(String name) throws IOException {
            super(name);
            socket = new DatagramSocket(serverport);
            System.out.println("JavaUdpServer run on: " + serverport);
        }

        @Override
        public void run() {
            int i = 0;
            while(true){
                
                try {
                    byte[] buf = new byte[4096];
                    
                    // receive request
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    
                    String dString1 = "value : " + i;
                    
                    String dString = "{\r\n" + 
                    		"      \"areaId\": \"Tracking002_2D\",\r\n" + 
                    		"      \"areaName\": \"soundproof_area\",\r\n" + 
                    		"      \"color\": \"#FF0000\",\r\n" + 
                    		"      \"coordinateSystemId\": \"CoordSys002\",\r\n" + 
                    		"      \"coordinateSystemName\": \"kyuukoudai2020\",\r\n" + 
                    		"      \"covarianceMatrix\": [\r\n" + 
                    		"        0.06,\r\n" + 
                    		"        -0.05,\r\n" + 
                    		"        -0.05,\r\n" + 
                    		"        0.17\r\n" + 
                    		"      ],\r\n" + 
                    		"      \"id\": \"009d6baa3a10\",\r\n" + 
                    		"      \"name\": null,\r\n" + 
                    		"      \"position\": [\r\n" + 
                    		"        1.61,\r\n" + 
                    		"        4.98,\r\n" + 
                    		"        0.9\r\n" + 
                    		"      ],\r\n" + 
                    		"      \"positionAccuracy\": 0.13,\r\n" + 
                    		"      \"positionTS\": 1585638748369,\r\n" + 
                    		"      \"smoothedPosition\": [\r\n" + 
                    		"        1.63,\r\n" + 
                    		"        4.91,\r\n" + 
                    		"        0.9\r\n" + 
                    		"      ],\r\n" + 
                    		"      \"smoothedPositionAccuracy\": 0.13,\r\n" + 
                    		"      \"zones\": []\r\n" + 
                    		"    }";
                    
                    i++;
                    buf = dString.getBytes();
                    
                    // send the response to the client at "address" and "port"
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    System.out.println("Request from: " + address + ":" + port);
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);
                    
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }
                
            }
            
        }
	}

}
