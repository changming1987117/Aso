package com.awesome.aso.job.hw.core;

import java.net.URLEncoder;

public class StringUtils {

	public static String URLEncoder(String str) {
		if (str != null) {
			try {
				str = URLEncoder.encode(str, "utf-8").replace("+", "%20")
						.replace("*", "%2A").replace("~", "%7E");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	

}
