
package yh.oa.tools;
import java.math.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.security.*; 
public class SNUtil{
	private  static BigInteger n;	
    private  static BigInteger e;
    private  static BigInteger p;	
    private  static BigInteger q;
    private   static String version=null;
    static{
    	 byte ebyte[] = new byte[]{1,0,1}; e=new BigInteger(1,ebyte);byte nbyte[] = new byte[]{108,-16,-80,-10,-103,-19,-47,108,42,86,49,-28,106,34,-127,-51,-104,12,-112,-110,-89,-64,-84,-10,89,86,-1,28,61,-71,83,-112,-70,-126,93,89,-44,114,-112,68,102,78,32,76,-99,81,-33,2,-41,57,99,-106,-93,-18,66,15,19,-82,11,-59,99,-67,120,59,-53,-35,116,-49,-99,-68,0,59,-109,-90,71,50,31,-27,-85,124,-81,115,-2,-108,77,87,8,0,42,122,91,-95,70,107,35,-1,-109,-119,63,-99,-98,-72,19,33,41,-22,51,-75,-99,57,101,56,-51,52,102,-5,66,-18,26,28,33,25,13,99,-109,102,26,-35}; n=new BigInteger(1,nbyte);byte pbyte[] = new byte[]{0,-76,53,103,92,-31,-116,32,-117,-41,101,89,-63,-91,104,-102,-20,57,13,-10,106,76,50,-7,-3,-34,-60,-50,70,90,-64,-64,-21,-102,-109,112,68,-29,67,89,126,2,-93,-9,15,30,102,61,-32,-48,95,23,102,-115,-76,26,-118,106,14,-40,101,-109,116,-43,53}; p=new BigInteger(1,pbyte);byte qbyte[] = new byte[]{0,-102,-62,0,114,54,24,-107,-40,-66,49,-111,60,-122,-88,-33,-8,-23,-31,89,-69,98,-22,-61,-97,94,75,126,-80,18,-47,-41,116,23,86,16,-77,2,-42,-21,115,-100,103,-99,83,-96,115,-41,44,-80,21,-128,48,-71,85,-75,68,71,92,70,67,89,63,-84,9}; q=new BigInteger(1,qbyte);
        
    }
    public static String _$4(){
    	return version;
    }
    public static void _$4(String v){
        version=v;
    }
    public static BigInteger _$1(){
    	return e;
    }
    public static BigInteger _$2(){
    	return n;
    }
	private static String a1(byte byte0)
	{
		char ac[] = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F'
		};
		char ac1[] = new char[2];
		ac1[0] = ac[byte0 >>> 4 & 0xf];
		ac1[1] = ac[byte0 & 0xf];
		String s = new String(ac1);
		return s;
	}
    private static final String if1()
		throws IOException
	{
		Process process = Runtime.getRuntime().exec("ifconfig /all");
		BufferedInputStream bufferedinputstream = new BufferedInputStream(process.getInputStream());
		StringBuffer stringbuffer = new StringBuffer();
		do
		{
			int i = bufferedinputstream.read();
			if (i != -1)
			{
				stringbuffer.append((char)i);
			} else
			{
				String s = stringbuffer.toString();
				bufferedinputstream.close();
				return s;
			}
		} while (true);
	}
	public static byte[] _$1(byte[] input) throws Exception{
    java.security.MessageDigest alg=java.security.MessageDigest.getInstance("MD5");   
    alg.update(input);
    byte[] digest = alg.digest();   
    return digest;
  }
	private static final boolean int1(String s)
	{
		return s.length() == 17;
	}

	private static final String a1(String s)
		throws Exception
	{
		
		StringBuffer sb=new StringBuffer("|");
		try
		{
			
			StringTokenizer stringtokenizer = new StringTokenizer(s, "  ");	
		do
		{
			if (!stringtokenizer.hasMoreTokens())
				break;
			String s3 = stringtokenizer.nextToken().trim();		
			int i = s3.indexOf("HWaddr");
			if (i > 0)
			{
				String s4 = s3.substring(i + 6).trim();
				if (int1(s4))
				sb.append(s4).append("|");
			}
		} while (true);
	
		}
		catch (Exception e)
		{
			
			throw new Exception(e.getMessage());
		}
	return sb.toString();
	}
		public static final String _$3()
	{
		try
		{
			String s = System.getProperty("os.name");
		if (s.startsWith("Windows"))
			return if1(a1());
		if (s.startsWith("Linux"))
			return a1(if1());
		}
		catch(Exception e){	e.printStackTrace();
		}
		return "";
	}
	private static String for1(String s)
	{
		StringBuffer stringbuffer = null;
		try
		{
			
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			byte nbyte[] = messagedigest.digest(s.getBytes());
		  
			stringbuffer = new StringBuffer();
			for (int i = 0; i < nbyte.length; i++)
				stringbuffer.append(a1(nbyte[i]));

		}
		catch (Exception exception) { }
		return stringbuffer != null ? stringbuffer.toString() : null;
	}
		public static final boolean do1(String s)
	{
		return s.length() == 17;
	}
	private static final String if1(String s)
		throws Exception
	{
	
		StringBuffer sb=new StringBuffer("|");
		try
		{					
			StringTokenizer stringtokenizer = new StringTokenizer(s, "\n");
		do
		{
			if (!stringtokenizer.hasMoreTokens())
				break;
			String s3 = stringtokenizer.nextToken().trim();
			int i = s3.indexOf(":");
			if (i > 0)
			{
				String s4 = s3.substring(i + 1).trim();					
				if (do1(s4)){											
					sb.append(s4).append("|");
				}
				
			}
		} while (true);
		}
		catch (Exception e)
		{
			
			throw new Exception(e.getMessage());
		}			
		return sb.toString();
	}
	private static final String a1()
		throws IOException
	{
		Process process = Runtime.getRuntime().exec("ipconfig /all");
		BufferedInputStream bufferedinputstream = new BufferedInputStream(process.getInputStream());
		StringBuffer stringbuffer = new StringBuffer();
		do
		{
			int i = bufferedinputstream.read();
			if (i != -1)
			{
				stringbuffer.append((char)i);
			} else
			{
				String s = stringbuffer.toString();
				bufferedinputstream.close();
				return s;
			}
		} while (true);
	}

	public static byte _$1(InputStream inputstream)
		throws Exception
	{
		int i;
		int j;
		j = inputstream.read();
		i = j;
		while (j >= 0) 
		{
				return (byte)i;
		}
		   throw new Exception();	
	}

	public static short _$2(InputStream inputstream)
		throws Exception
	{
		int i;
		int j;
		i = inputstream.read();
		j = inputstream.read();		
		while ((i | j) >= 0) 
		{ 
			return (short)((i << 8) + j);
		}
		   throw new Exception();
	}

	public static long _$3(InputStream inputstream)
		throws Exception
	{	

		return ((long)_$4(inputstream) << 32) + ((long)_$4(inputstream) & 0xffffffffL);

	}
	
	public static int _$4(InputStream inputstream)
		throws Exception
	{
		int i;
		int j;
		int k;
		int l;
		i = inputstream.read();
		j = inputstream.read();
		k = inputstream.read();
		l = inputstream.read();	   
		while ((i | j | k | l) >= 0)
		{
			
			return (i << 24) + (j << 16) + (k << 8) + l;
		} 
		   throw new Exception();
	
	}
	
	public static String _$5(InputStream inputstream)
		throws Exception
	{
		short word0;
		short word1;
		word1 = _$2(inputstream);
		word0 = word1;
	 
		while (word1 == 0){
			return null;
		}
			
		StringBuffer stringbuffer=new StringBuffer(word0);
		
		
		for (int i = 0; i < word0; i++)
		{
			stringbuffer.append((char)_$2(inputstream));
		
		}
			

		return stringbuffer.toString();
	}
	

	public static boolean _$1(byte nbyte[], byte pbyte[])
	{
		while (nbyte == null || pbyte == null){
			return false;
		}
			
		while (nbyte.length != pbyte.length){
			return false;
		}
			
		for (int i = 0; i < nbyte.length; i++)
		{
		    if (nbyte[i] != pbyte[i])
			{
				return false;
			}
		}
			
				

		return true;
	}
	
	private static byte[] _$1(InputStream inputstream, byte nbyte[])
		throws Exception
	{
	
		if (nbyte.length == inputstream.read(nbyte)) 
		{
			return nbyte;
		}
	    else
		{
		   throw new Exception();		
		}
		
	}
}