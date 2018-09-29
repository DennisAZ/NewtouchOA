package oa.spring.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DoubleCaulUtil {
	
	private static int scale = 3;//保留3位小数
	private static int roundingMode = BigDecimal.ROUND_HALF_UP;//四舍五入
	/**
	 * 对double数据进行取精度
	 * @param value double数据
	 * @return
	 */
	public static double round(double value){
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}
	
	/**
	 * 两个浮点数数相乘
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double mul(String d1,String d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.multiply(b2).doubleValue();
	}
	
	/**
	 * 两个浮点数数相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double sum(String d1,String d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.add(b2).doubleValue();
	}
	/**
	 * 两个浮点数数相减
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double sub(String d1,String d2){
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.subtract(b2).doubleValue();
	}
}