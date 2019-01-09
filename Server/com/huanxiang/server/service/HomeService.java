package com.huanxiang.server.service;

import com.huanxiang.server.thread.ServerAgentThread;

public interface HomeService {

	public boolean client_online(ServerAgentThread serverAgentThread);
	
	public boolean ctrl(ServerAgentThread serverAgentThread);
	
	public void kicked(ServerAgentThread serverAgentThread);

}
