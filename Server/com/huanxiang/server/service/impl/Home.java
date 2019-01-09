package com.huanxiang.server.service.impl;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONException;

import com.huanxiang.server.service.HomeService;
import com.huanxiang.server.thread.ServerAgentThread;
import com.huanxiang.server.util.JSON;

public class Home implements HomeService {

	@Override
	public boolean client_online(ServerAgentThread serverAgentThread) {
		try {
			String name = serverAgentThread.getJsonString().getValue("name");
			for (ServerAgentThread temp:serverAgentThread.getMain().getOnlineList()) {
				if (temp.getName().equals(name)) {
					JSON jsonString = new JSON();
					jsonString.setValue("msg", "NAME_REPEAT");
					try {
						serverAgentThread.getOut().write(jsonString.printJson().getBytes());
						serverAgentThread.getIn().close();
						serverAgentThread.getOut().close();
						serverAgentThread.getSocket().close();
					} catch (IOException e) {
						System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "关闭"+serverAgentThread.getName()+"的数据流异常");
					}
					return false;
				}
			}
			
			serverAgentThread.setName(name);
			serverAgentThread.getMain().getOnlineList().add(serverAgentThread);
			System.out.println(serverAgentThread.getMain().getNetwork().getTime() + "用户"+serverAgentThread.getName()+"登陆成功");
			
			JSON jsonString = new JSON();
			jsonString.setValue("msg", "HOME_ONLINE");
			if ("home".equals(name)) {
				for (ServerAgentThread temp:serverAgentThread.getMain().getOnlineList()) {
					if (!temp.getName().equals(name)) {
						try {
							temp.getOut().write(jsonString.printJson().getBytes());
						} catch (IOException e) {
							System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "向"+temp.getName()+"发送数据失败");
						}
						break;
					}
				}
			} else {
				serverAgentThread.getDokiDoki().setClient(true);
				for (ServerAgentThread temp:serverAgentThread.getMain().getOnlineList()) {
					if (temp.getName().equals("home")) {
						try {
							serverAgentThread.getOut().write(jsonString.printJson().getBytes());
						} catch (IOException e) {
							System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "向"+serverAgentThread.getName()+"发送数据失败");
						}
						break;
					}
				}
			}
		} catch (JSONException e) {
			this.kicked(serverAgentThread);
		}
		return true;
	}

	@Override
	public boolean ctrl(ServerAgentThread serverAgentThread) {
		ServerAgentThread home = null;
		for (ServerAgentThread tmp : serverAgentThread.getMain().getOnlineList()) {
			if ("home".equals(tmp.getName())) {
				home = tmp;
				break;
			}
		}
		if (home != null) {
			try {
				String aims = serverAgentThread.getJsonString().getValue("aims");
				String status = serverAgentThread.getJsonString().getValue("status");
				
				//{"aims":"L1","status":"on"}
				JSON jsonString = new JSON();
				jsonString.setValue("aims", aims);
				jsonString.setValue("status", status);
				home.getOut().write(jsonString.printJson().getBytes());
				if ("on".equals(status)) {
					System.out.println(serverAgentThread.getMain().getNetwork().getTime() + serverAgentThread.getName() + "打开了"+aims);
				} else if ("off".equals(status)) {
					System.out.println(serverAgentThread.getMain().getNetwork().getTime() + serverAgentThread.getName() + "关闭了"+aims);
				}
				return true;
			} catch (JSONException e) {
				this.kicked(serverAgentThread);
			} catch (IOException e) {
				System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "向被控端发送控制指令失败");
			}
		}
		return false;
	}

	@Override
	public void kicked(ServerAgentThread serverAgentThread) {
		JSON jsonString = new JSON();
		jsonString.setValue("msg", "SERVER_KICKED");
		try {
			serverAgentThread.getOut().write(jsonString.printJson().getBytes());
			serverAgentThread.getIn().close();
			serverAgentThread.getOut().close();
			serverAgentThread.getSocket().close();
		} catch (IOException e) {
			System.err.println(serverAgentThread.getMain().getNetwork().getTime() + "关闭未知用户的数据流异常");
		} finally {
			serverAgentThread.setFlag(false);
			Vector<ServerAgentThread> onlineList = serverAgentThread.getMain().getOnlineList();
			onlineList.remove(serverAgentThread);
		}
	}

}
