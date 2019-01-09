package com.hxshijie.service.impl;

import android.os.Bundle;
import android.os.Message;

import com.hxshijie.Thread.ClientAgentThread;
import com.hxshijie.service.ClientService;
import com.hxshijie.util.JSON;

import org.json.JSONException;

import java.io.IOException;

public class Client implements ClientService {

    @Override
    public void dokiDoki(ClientAgentThread clientAgentThread) {
        JSON jsonString = new JSON();
        try {
            jsonString.setValue("msg","Doki");
            clientAgentThread.getDataOutputStream().write(jsonString.printJson().getBytes());
        } catch (Exception e) {
            this.kicked(clientAgentThread);
        }

        Message msg = clientAgentThread.getMainActivity().getHandler().obtainMessage();
        Bundle bundle=new Bundle();
        try {
            String home = clientAgentThread.getJsonString().getValue("home");
            if ("online".equals(home)) {
                msg.what = 0;
                bundle.putString("text", "被控端在线");
            } else if ("offline".equals(home)) {
                msg.what = 1;
                bundle.putString("text", "被控端已离线");
            } else {
                this.kicked(clientAgentThread);
            }
            msg.setData(bundle);
            clientAgentThread.getMainActivity().getHandler().sendMessage(msg);
        } catch (JSONException e) {
            this.kicked(clientAgentThread);
        }
    }

    @Override
    public void home_online(ClientAgentThread clientAgentThread) {
        Message msg = clientAgentThread.getMainActivity().getHandler().obtainMessage();
        Bundle bundle=new Bundle();
        msg.what = 0;
        bundle.putString("text", "被控端在线");
        msg.setData(bundle);
        clientAgentThread.getMainActivity().getHandler().sendMessage(msg);
    }

    @Override
    public void result(ClientAgentThread clientAgentThread) {
        try {
            String result = clientAgentThread.getJsonString().getValue("result");
            Message msg = clientAgentThread.getMainActivity().getHandler().obtainMessage();
            Bundle bundle=new Bundle();
            msg.what = 100;
            if("success".equals(result)) {
                bundle.putString("text", "成功");
            } else if ("failure".equals(result)){
                bundle.putString("text", "失败");
            } else {
                this.kicked(clientAgentThread);
            }
            msg.setData(bundle);
            clientAgentThread.getMainActivity().getHandler().sendMessage(msg);
        } catch (JSONException e) {
            this.kicked(clientAgentThread);
        }
    }

    @Override
    public void kicked(ClientAgentThread clientAgentThread) {
        Message msg = clientAgentThread.getMainActivity().getHandler().obtainMessage();
        Bundle bundle=new Bundle();
        try {
            clientAgentThread.getDataInputStream().close();
            clientAgentThread.getDataOutputStream().close();
            clientAgentThread.getMainActivity().getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientAgentThread.getMainActivity().getClientCtrlThread().setFlag(false);
            clientAgentThread.setFlag(false);
            msg.what = 1;
            bundle.putString("text", "与服务器断开链接，请稍后重试");
            msg.setData(bundle);
            clientAgentThread.getMainActivity().getHandler().sendMessage(msg);
            clientAgentThread.setFlag(false);
        }
    }
}
