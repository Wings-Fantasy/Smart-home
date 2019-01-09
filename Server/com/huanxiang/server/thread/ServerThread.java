package com.huanxiang.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.huanxiang.server.main.Main;
import com.huanxiang.server.util.Network;

public class ServerThread extends Thread {

	private boolean flag = true;
	private Main main;
	private ServerSocket serverSocket;
	private Socket socket;
	private Network network;

	public ServerThread(Main main) {
		this.main = main;
		this.serverSocket = main.getServerSocket();
		this.network = main.getNetwork();
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		while (flag) {
			try {
				socket = serverSocket.accept();
				ServerAgentThread sat = new ServerAgentThread(main, socket);
				sat.start();
				System.out.println(network.getTime()+"新用户连入");
			} catch (IOException e) {
				System.err.println(network.getTime() + "有一个用户连接失败");
			}
		}
	}
}
