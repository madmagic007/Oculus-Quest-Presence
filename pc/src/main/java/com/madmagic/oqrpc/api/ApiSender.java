package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import okhttp3.*;
import org.json.JSONObject;

public class ApiSender {

	private static OkHttpClient c = new OkHttpClient();
	private static MediaType jT = MediaType.parse("application/json; charset=utf-8");

	public static JSONObject ask(String url, String message) {
		JSONObject post = new JSONObject()
				.put("pcAddress", Main.getIp())
				.put("message", message);

		Request.Builder r = new Request.Builder()
				.url(url);

		if (!url.contains("github")) {
			r.post(RequestBody.create(jT, post.toString(4)));
		}

		JSONObject ro = new JSONObject();
		try {
			Response rs = c.newCall(r.build()).execute();
			ro = new JSONObject(rs.body().string());
		} catch (Exception ignored) {
			System.out.println("failed to send request to the quest");
			Discord.terminate();
		}
		return ro;
	}
}
