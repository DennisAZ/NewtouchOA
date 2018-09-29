//package org.hibernate.util;
package yh.subsys.oa.vmeet.act;

/**
 * 字符串处理,来自Hibernate
 * BuildTime:  7:23:00 PM, Dec 11, 2008
 * @author liyu
 */
public final class StringHelper
{
	
	private StringHelper()
	{
	}
	
	public static boolean isNotEmpty(String string)
	{
		return string != null && string.length() > 0;
	}
	
	public static boolean isEmpty(String string)
	{
		return string == null || string.length() == 0;
	}
	
	
}
