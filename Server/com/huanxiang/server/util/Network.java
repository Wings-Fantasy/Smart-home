package com.huanxiang.server.util;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Network {

	/**
	 * 获取系统时间
	 * 
	 * @return 返回服务器系统当前时间<br />
	 *         格式：[yyyy-MM-dd][HH:mm:ss]<br />
	 *         例如：[2017-07-20][14:13:25]
	 * */
	public String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd][HH:mm:ss] ");
		Date date = new Date();
		return format.format(date);
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param socket
	 *            传入客户端链接的Socket
	 * 
	 * @return 客户端IP
	 * */
	public String getIP(Socket socket) {
		return socket.getInetAddress().toString();
	}

}
