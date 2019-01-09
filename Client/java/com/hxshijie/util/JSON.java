package com.hxshijie.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 悲剧小白
 * @version 1.2.0
 * */
public class JSON {

	private JSONObject jsonString = null;

	/**
	 * 直接初始化，会创建空的json
	 * */
	public JSON() {
		jsonString = new JSONObject();
	}

	/**
	 * 用字符串创建json，字符串必须以"{"开头，以"}"结尾。<br>
	 * 否则会抛出JSONException异常
	 * */
	public JSON(String json) throws JSONException {
		jsonString = new JSONObject(json);
	}

	/**
	 * 获取json值
	 * 
	 * @param key
	 *            传入有效的json键
	 * 
	 * @return 得到的json值
	 * 
	 * @throws JSONException
	 *             如果传入的key值不存在则会抛出此错误
	 * */
	public String getValue(String key) throws JSONException {
		return jsonString.getString(key);
	}

	/**
	 * 设置json值<br>
	 * 重复设置时，会覆盖掉当前的值，达到修改的效果
	 * 
	 * @param key
	 *            传入需要设置/修改的键
	 * 
	 * @param value
	 *            传入一个目标值
	 * */
	public void setValue(String key, Object value) throws JSONException {
		jsonString.put(key, value);
	}

	/**
	 * 删除一个键值对
	 * 
	 * @param key
	 *            传入需要删除的键
	 * */
	public void remove(String key) {
		jsonString.remove(key);
	}

	/**
	 * 获取完整的json字符串
	 * 
	 * @return 返回当前完整的json字符串
	 * */
	public String printJson() {
		return jsonString.toString();
	}

}
