package com.hxshijie.Thread;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import com.hxshijie.service.impl.Client;
import com.hxshijie.smarthome.MainActivity;
import com.hxshijie.util.JSON;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ClientAgentThread extends Thread {

    private boolean flag = true;
    private MainActivity father;
    private DataInputStream in;
    private DataOutputStream out;
    private JSON jsonString;
    private Client client;

    ClientAgentThread(MainActivity father) {
        this.father = father;
        Message msg = father.getHandler().obtainMessage();
        Bundle bundle=new Bundle();
        try {
            in = new DataInputStream(father.getSocket().getInputStream());
            out = new DataOutputStream(father.getSocket().getOutputStream());
            @SuppressLint("HardwareIds")
            String name = Build.SERIAL;
            jsonString = new JSON();
            jsonString.setValue("msg","CLIENT_ONLINE");
            jsonString.setValue("name", name);
            out.write(jsonString.printJson().getBytes());
            msg.what = 0;
            bundle.putString("text", "服务器连接成功！");
            client = new Client();
            father.setClientCtrlThread(new ClientCtrlThread(this));
            father.getClientCtrlThread().start();
        } catch (Exception e) {
            msg.what = 1;
            bundle.putString("text", "软件内部错误！");
        } finally {
            msg.setData(bundle);
            father.getHandler().sendMessage(msg);
        }
    }

    public void run() {
        while (flag) {
            byte[] bytes = new byte[1024];
            try {
                String json = new String(bytes, 0, in.read(bytes));
                jsonString = new JSON(json);
                String msg = jsonString.getValue("msg");
                if (msg.startsWith("Doki")) {
                    client.dokiDoki(this);
                } else if (msg.startsWith("HOME_ONLINE")) {
                    client.home_online(this);
                } else if (msg.startsWith("result")) {
                    client.result(this);
                } else {
                    client.kicked(this);
                }
            } catch (Exception e) {
                client.kicked(this);
            }
        }
    }

    public MainActivity getMainActivity() {
        return father;
    }

    public DataInputStream getDataInputStream() {
        return in;
    }

    public DataOutputStream getDataOutputStream() {
        return out;
    }

    public JSON getJsonString() {
        return jsonString;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
