package com.hxshijie.Thread;

import android.os.Bundle;
import android.os.Message;

import com.hxshijie.util.JSON;

public class ClientCtrlThread extends Thread {

    private boolean flag = true;
    private ClientAgentThread clientAgentThread;
    private String json = "";

    ClientCtrlThread(ClientAgentThread clientAgentThread) {
        this.clientAgentThread = clientAgentThread;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setJson(JSON jsonString) {
        this.json = jsonString.printJson();
    }

    public void run() {
        while (flag) {
            if (!"".equals(json)) {
                try {
                    clientAgentThread.getDataOutputStream().write(json.getBytes());
                } catch (Exception e) {
                    Message msg = clientAgentThread.getMainActivity().getHandler().obtainMessage();
                    Bundle bundle=new Bundle();
                    msg.what = 1;
                    bundle.putString("text", "软件内部错误！");
                    msg.setData(bundle);
                    clientAgentThread.getMainActivity().getHandler().sendMessage(msg);
                }
                json = "";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
