package com.madmagic.oqrpc;

import okhttp3.*;
import org.json.JSONObject;

public class ApiSender {
	
	 private static OkHttpClient c = new OkHttpClient();
	 private static MediaType jT = MediaType.parse("application/json; charset=utf-8");

	 public static JSONObject ask(String url, JSONObject o) {
	 	JSONObject ro = new JSONObject();
		 Request.Builder r = new Request.Builder()
				 .url(url);

		 if (!o.isEmpty()) {
		 	r.post(RequestBody.create(jT, o.toString(4)))
					.build();
		 }

		 try {
			 Response rs = c.newCall(r.build()).execute();
			 ro = new JSONObject(rs.body().string());
			 ResponseHandler.handle(ro);
		 } catch (Exception e) {
			 System.out.println("device not found");
			 Discord.terminate();
		 }
		 return ro;
	 }
}
