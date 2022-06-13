package me.madmagic.oqrpc.api;

import me.madmagic.oqrpc.source.Config;
import me.madmagic.oqrpc.source.Main;
import okhttp3.*;
import org.json.JSONObject;

public class ApiSender {

	private static final OkHttpClient c = new OkHttpClient();
	private static final MediaType jT = MediaType.parse("application/json; charset=utf-8");

	public static JSONObject ask(String url, String message) throws Exception {
		JSONObject post = new JSONObject()
				.put("pcAddress", Main.getIp())
				.put("message", message)
				.put("sleepWake", Config.getSleepWake());

		Request.Builder r = new Request.Builder()
				.url(url);

		if (!url.contains("github")) {
			r.post(RequestBody.create(post.toString(), jT));
		}

		Response rs = c.newCall(r.build()).execute();
		return new JSONObject(rs.body().string());
	}
}
