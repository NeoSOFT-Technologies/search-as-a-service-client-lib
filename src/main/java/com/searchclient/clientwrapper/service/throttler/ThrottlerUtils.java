package com.searchclient.clientwrapper.service.throttler;

public class ThrottlerUtils {
	public static double formatRequestSizeStringToDouble(String size) {
		String numericValue = size.substring(0, size.length()-2);
		return Double.parseDouble(numericValue);
	}
	
	public static double getSizeInkBs(String data) {
		int numberOfChars = data.length();
		return (8 * (int)((((numberOfChars) * 2) + 45) / 8))/1000;
	}
}