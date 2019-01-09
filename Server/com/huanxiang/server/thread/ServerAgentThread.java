package com.huanxiang.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.huanxiang.server.main.Main;
import com.huanxiang.server.service.impl.Home;
import com.huanxiang.server.util.JSON;

public class ServerAgentThread extends Thread {

	private boolean flag = true;
	private Main main;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private JSON jsonString;
	private boolean isLogin = false;
	private Home home = new Home();
	private DokiDoki doki = new DokiDoki(this);

	public ServerAgentThread(Main main, Socket socket) {
		this.main = main;
		this.socket = socket;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			System.err.println("初始化数据流异常");
		}
	}

	public Main getMain() {
		return main;
	}
	public Socket getSocket() {
		return socket;
	}
	public InputStream getIn() {
		return in;
	}
	public OutputStream getOut() {
		return out;
	}
	public JSON getJsonString() {
		return jsonString;
	}
	public DokiDoki getDokiDoki() {
		return doki;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void run() {
		doki.start();
		while (flag) {
			String json = "";
			try {
				byte[] bytes = new byte[1024];
				json = new String(bytes, 0, in.read(bytes));
				jsonString = new JSON(json);
				String msg = jsonString.getValue("msg");
				if (msg.startsWith("Doki")) {
					doki.setDoki(true);
				} else if (msg.startsWith("CLIENT_ONLINE")) {
					//{"msg":"CLIENT_ONLINE","name":"home"}
					if(!isLogin ) {
						if(home.client_online(this)) {
							isLogin = true;
						}
					} else {
						home.kicked(this);
					}
				} else if (msg.startsWith("CTRL")) {
					//{"msg":"CTRL","aims":"L1","status":"on"}
					if(isLogin ) {
						JSON jsonString = new JSON();
						jsonString.setValue("msg", "result");
						if(home.ctrl(this)) {
							jsonString.setValue("result", "success");
						} else {
							jsonString.setValue("result", "failure");
						}
						out.write(jsonString.printJson().getBytes());
					} else {
						home.kicked(this);
					}
				} else {
					home.kicked(this);
				}
			} catch (Exception e) {
				home.kicked(this);
			}
		}
	}

}
