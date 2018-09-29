/**
 * 
 */
package yh.oa.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import yh.core.util.YHGuid;
import yh.core.util.auth.YHAuthenticator;

/**
 * @author ljs
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String str=YHGuid.getRawGuid();
//		System.out.println(str);
//		System.out.println(System.getProperty("java.library.path"));
//		System.out.println(YHAuthenticator.ciphEncryptStr("dlhy@1234"));
		System.out.println(YHAuthenticator.ciphDecryptStr("vaRrwyc/Yps="));
		String c;
		  System.out.print("请输入要加紧的密码:");
		  BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
		  c=strin.readLine();
		 System.out.println(YHAuthenticator.ciphEncryptStr(c));
	}

}
