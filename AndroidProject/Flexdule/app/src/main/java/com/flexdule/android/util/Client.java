package com.flexdule.android.util;

import com.flexdule.core.dtos.ScheduleWithActivities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public class Client {

    public static final int HELLO = 0;
    public static final int SEND_SCHEDULES = 1;

    private Socket socketCliente;
    private String IP;
    private int port;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(String IP, int port) throws Exception {
        this.IP = IP;
        this.port = port;
    }

    private void connectServer() throws Exception {
        try {
            socketCliente = new Socket(IP, port);
            oos = new ObjectOutputStream(socketCliente.getOutputStream());
            ois = new ObjectInputStream(socketCliente.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void disconnect() throws Exception {
        try {
            socketCliente.close();
            ois.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean helloRequest() throws Exception {
        Boolean response = null;
        try {
            connectServer();

            oos.writeInt(HELLO);
            oos.flush();

            Boolean hello = true;
            oos.writeObject(hello);
            oos.flush();

            response = (Boolean) ois.readObject();
            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return response;
    }

    public String sendSchedulesRequest(List<ScheduleWithActivities> schedules) throws Exception {
        String response = null;
        try {
            connectServer();

            oos.writeInt(SEND_SCHEDULES);
            oos.flush();

            oos.writeObject(schedules);
            oos.flush();

            disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return response;
    }
}