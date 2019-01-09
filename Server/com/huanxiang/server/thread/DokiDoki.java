package com.huanxiang.server.thread;

import java.io.IOException;

import com.huanxiang.server.service.impl.Home;
import com.huanxiang.server.util.JSON;

public class DokiDoki extends Thread {

	private ServerAgentThread serverAgentThread;
	private Home home = new Home();
	private boolean flag = true;
	private boolean doki = false;
	private boolean client = false;
	private int intervalTime = 60000;

	public DokiDoki(ServerAgentThread serverAgentThread) {
		this.serverAgentThread = serverAgentThread;
	}

	public void setDoki(boolean doki) {
		this.doki = doki;
	}

	public void setClient(boolean client) {
		this.client = client;
	}
	
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "用户"+serverAgentThread.getName()+"心跳线程异常");
		}
		while (flag) {
			doki = false;
			//{"msg":"Doki"}
			JSON jsonString = new JSON();
			jsonString.setValue("msg", "Doki");
			if (client) {
				jsonString.setValue("home", "offline");
				for (ServerAgentThread tmp : serverAgentThread.getMain().getOnlineList()) {
					if ("home".equals(tmp.getName())) {
						jsonString.setValue("home", "online");
						break;
					}
				}
			}
			try {
				serverAgentThread.getOut().write(jsonString.printJson().getBytes());
				Thread.sleep(intervalTime);
			} catch (IOException e) {
				home.kicked(serverAgentThread);
			}catch (InterruptedException e) {
				System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "用户"+serverAgentThread.getName()+"心跳线程异常");
			}
			if (!doki) {
				home.kicked(serverAgentThread);
				serverAgentThread.interrupt();
				flag = false;
			}
		}
	}

}
