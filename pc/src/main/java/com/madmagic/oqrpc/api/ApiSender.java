package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import okhttp3.*;
import org.json.JSONObject;

public class ApiSender {

	private static final OkHttpClient c = new OkHttpClient();
	private static final MediaType jT = MediaType.parse("application/json; charset=utf-8");

	public static JSONObject ask(String url, String message) {
		JSONObject post = new JSONObject()
				.put("pcAddress", Main.getIp())
				.put("message", message)
				.put("sleepWake", Config.getSleepWake());

		Request.Builder r = new Request.Builder()
				.url(url);

		if (!url.contains("github")) {
			r.post(RequestBody.create(jT, post.toString(4)));
		}

		String rStr = "";
		JSONObject ro = new JSONObject();
		try {
			Response rs = c.newCall(r.build()).execute();
			rStr = rs.body().string();
			ro = new JSONObject(rStr);
		} catch (Exception ignored) {
			System.out.println("resp: " + rStr + "\npost: " +  post.toString(4));
			System.out.println("failed to send request to url: " + url);
			Discord.terminate();
		}
		return ro;
	}
}
