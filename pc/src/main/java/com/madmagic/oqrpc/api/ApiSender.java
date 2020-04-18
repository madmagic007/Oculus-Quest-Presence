package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import com.madmagic.oqrpc.source.SystemTrayHandler;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

public class ApiSender {
	
	 private static OkHttpClient c = new OkHttpClient();
	 private static MediaType jT = MediaType.parse("application/json; charset=utf-8");

	 public static JSONObject ask(String url) {
	 	JSONObject ro = new JSONObject();
	 	JSONObject post = new JSONObject()
				.put("address", Main.getIp());

	 	Request.Builder r = new Request.Builder()
				.url(url);

	 	if(!url.contains("github")) {
	 		r.post(RequestBody.create(jT, post.toString(4)));
		}

	 	try {
	 		Response rs = c.newCall(r.build()).execute();
	 		ro = new JSONObject(rs.body().string());
	 	} catch (Exception e) {
	 		Discord.terminate();
	 	}
	 	return ro;
	 }
}
