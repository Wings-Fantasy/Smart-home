package com.hxshijie.Thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;

import com.hxshijie.smarthome.MainActivity;

import java.net.Socket;

public class ClientThread extends Thread {

    private MainActivity father;
    private ClientAgentThread clientAgentThread = null;

    public ClientThread(MainActivity father) {
        this.father = father;
    }

    public ClientAgentThread getClientAgentThread() {
        return clientAgentThread;
    }

    @SuppressLint("ResourceAsColor")
    public void run() {
        Message msg = father.getHandler().obtainMessage();
        Bundle bundle=new Bundle();
        try {
            ConnectivityManager cm = (ConnectivityManager) father.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null && cm.getActiveNetworkInfo().isAvailable()) {
                Socket socket = new Socket("192.168.50.100",23598);
                father.setSocket(socket);
                clientAgentThread = new ClientAgentThread(father);
                clientAgentThread.start();
            } else {
                msg.what = 1;
                bundle.putString("text", "请求服务器失败，请检查网络链接");
                msg.setData(bundle);
                father.getHandler().sendMessage(msg);
            }
        } catch (Exception e) {
            msg.what = 1;
            bundle.putString("text", "请求服务器失败，请检查网络链接");
            msg.setData(bundle);
            father.getHandler().sendMessage(msg);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
