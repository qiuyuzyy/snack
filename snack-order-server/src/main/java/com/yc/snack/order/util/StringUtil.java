package com.yc.snack.order.util;

public class StringUtil {
	/*
	 * 判空
	 */
	public static boolean checkNull(String ... strs) {
		if(strs == null || strs.length <= 0) {
			return true;
		}
		for(String str : strs) {
			if(str == null || "".equals(str)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkNull(Object object, Object object2) {
		// TODO Auto-generated method stub
		return false;
	}

}
