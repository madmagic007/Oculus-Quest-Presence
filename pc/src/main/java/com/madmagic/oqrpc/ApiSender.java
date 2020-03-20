package com.madmagic.oqrpc;

import okhttp3.*;
import org.json.JSONObject;

public class ApiSender {
	
	 private static OkHttpClient c = new OkHttpClient();
	 private static MediaType jT = MediaType.parse("application/json; charset=utf-8");
	 public static String ask(String address, JSONObject o) {
		 try {
			 Request r = new Request.Builder()
					 .url("http://" + address + ":8080")
					 .post(RequestBody.create(jT, o.toString(4)))
					 .build();
			 Response rs = c.newCall(r).execute();
			 ResponseHandler.handle(new JSONObject(rs.body().string()));
		 } catch (Exception e) {
			 System.out.println("device not found");
			 Discord.terminate();
		 }
		 return "";
	 }
}
