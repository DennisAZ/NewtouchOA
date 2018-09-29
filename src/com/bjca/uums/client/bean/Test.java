package com.bjca.uums.client.bean;
import java.util.Random;

import yh.core.util.auth.YHAuthenticator;

public class Test {
	public static void main(String arg[]){
		int number = new Random().nextInt(10) + 1;
		System.out.println(number);
		try {
			System.out.println(YHAuthenticator.ciphDecryptStr("$1$.YUh7Dwq$hiEMT5eMTD637f2WFJCVV/"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
