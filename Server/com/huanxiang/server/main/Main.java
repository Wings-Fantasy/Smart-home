package com.huanxiang.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.Vector;

import com.huanxiang.server.thread.ServerAgentThread;
import com.huanxiang.server.thread.ServerThread;
import com.huanxiang.server.util.JSON;
import com.huanxiang.server.util.Network;

public class Main {

	private Scanner in = new Scanner(System.in);
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private int port = 23598;
	private Network network = new Network();
	private Vector<ServerAgentThread> onlineList = new Vector<ServerAgentThread>();

	private Main() {
		this.InitServer();
		this.Command();
	}

	private void InitServer() {
		try {
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(this);
			serverThread.start();
			System.out.println(network.getTime()+"服务器已启动，端口:"+port);
		} catch (IOException e) {
			System.err.println("端口被占用");
		}
	}
	
	private void Command() {
		while(true) {
			String cmd = in.nextLine();
			if(cmd.equals("help")) {
				this.Help();
			} else if(cmd.equals("list")) {
				this.List();
			} else if(cmd.equals("stop")) {
				this.Stop();
			} else {
				System.out.println("未知命令，输入\"help\"查看帮助！");
			}
		}
	}

	private void Help() {
		System.out.println("服务器帮助：\n"
				+ "\"list\"查看在线列表\n"
				+ "\"stop\"关闭服务器");
	}

	private void List() {
		Vector<String> tmp = new Vector<String>();
		for (ServerAgentThread client : onlineList) {
			tmp.add(client.getName());
		}
		System.out.println("当前在线列表"+tmp);
	}

	private void Stop() {
		if (onlineList.size()>0) {
			JSON jsonString = new JSON();
			jsonString.setValue("msg", "SERVER_STOP");
			for (ServerAgentThread client : onlineList) {
				try {
					client.getOut().write(jsonString.printJson().getBytes());
				} catch (IOException e) {
					System.err.println(network.getTime()+"向"+client.getName()+"发送服务器离线消息失败");
				}
				client.getDokiDoki().setFlag(false);
				client.setFlag(false);
			}
		}
		serverThread.setFlag(false);
		onlineList.clear();
		try {
			serverSocket.close();
		} catch (IOException e) {
			System.err.println(network.getTime()+"关闭服务器主数据通道异常");
		}
		System.exit(0);
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public Network getNetwork() {
		return network;
	}

	public Vector<ServerAgentThread> getOnlineList() {
		return onlineList;
	}

	public static void main(String[] args) {
		new Main();
	}

}
