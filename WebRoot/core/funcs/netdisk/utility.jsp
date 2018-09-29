<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工具页</title>
</head>
<body>
<%!
class Msg
{
	private JspWriter out;
	public Msg(JspWriter out)
	{
		this.out = out;
	}
	public void message(String title, String content)
	{
		String style = "";
		message(title, content, style);
	}
	public void message(String title, String content, String style)
	{
		int width = content.length()*10 + 180;
		width = width > 500 ? 500 : width;
	  if(style.equals("blank"))
	  	width -= 70;
		if(style.equals(""))
	  {
	     if(title.equals("错误"))
	     {
		     	style = "error";
	     }
	     else if(title.equals("警告"))
	     {
		     	style = "warning";
	     }
	     else if(title.equals("停止"))
	     {
		     	style = "stop";
	     }
	     else if(title.equals("禁止"))
	     {
		     	style = "forbidden";
	     }
	     else if(title.equals("帮助"))
	     {
		     	style = "help";
	     }
	     else
	     {
		     	style = "info";
	     }
	  }
		try
		{
			this.out.println("<table class=\"MessageBox\" align=\"center\" width=" + width + "\">");
			this.out.println("<tr><td class=\"msg " + style + "\">");
			if(!title.equals(""))
			{
				this.out.println("<h4 class=\"title\">" + title + "</h4>");
			}
			this.out.println("<div class=\"content\" style=\"font-size:12pt\">" + content + "</div>");
			this.out.println("</td></tr></table>");
			out.flush();
		}
		catch(Exception ex)
		{
		  //System.out.println(ex.getMessage());
		}
	}
}
%>
</body>
</html>