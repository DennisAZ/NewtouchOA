<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0);  
	
	int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>最新的新闻公告</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:center;
		}
    </style>
  
  </head>
  
  <body>
  	<table class="normal" style="width:100%" cellpadding="0" cellspacing="0">
    	<tr>
        	<td style="width:49%">
            	<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;&nbsp;最新发布的新闻动态
                    </div>
                    <div class="divWithScroll2" style="height:200px">
                        <logic:notEmpty name="lastNews1">			
                            <ul class="listtxt" style="margin:0px; padding:6px;word-break:break-all;">
                                <logic:iterate id="lastNews1" name="lastNews1">
                                    <li style="height:20px">
                                    	<img src="crm/images/content/blueArr.gif"/>
                                        <logic:notEmpty name="lastNews1" property="newType">
                                            <span class="brown">[${lastNews1.newType}]</span>
                                        </logic:notEmpty><a href="messageAction.do?op=showNewsInfo&newCode=${lastNews1.newCode}&isEdit=0" target="_blank">${lastNews1.newTitle}</a>
                                        <logic:equal value="0" name="lastNews1" property="newIstop">
                                        	<img src="crm/images/content/upButton.gif" alt="置顶"/>
                                        </logic:equal>
                                        <img id="newIcon<%=count%>" src="crm/images/gif/new.gif" style="display:none"/>
                                        <span class="gray">${lastNews1.newInsUser}&nbsp;发布于&nbsp;<label id="ndate<%=count%>"></label></span>
                                    </li>
                                     <script type="text/javascript">
                                        dateFormat('ndate','${lastNews1.newDate}',<%=count%>);
										
										//今日发布的显示new图标
										if('${lastNews1.newDate}'.substring(0,10)=='${today}'){
											$("newIcon"+<%=count%>).style.display="";
										}
                                    </script>
                                    <% count++; %>
                                  </logic:iterate>
                            </ul>
                        </logic:notEmpty>  
                        <logic:empty name="lastNews1">
                            <div align="center" class="gray" style="padding-top:30px">	
                                暂无新闻！
                            </div>
                        </logic:empty>
                    </div>
                </div>
            </td>
            <td style="width:5px">&nbsp;</td>
            <td>
            	<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;&nbsp;最新发布的通知公告
                    </div>
                    <div class="divWithScroll2" style="height:200px">
                        <logic:notEmpty name="lastNews2">			
                            <ul class="listtxt" style="margin:0px; padding:6px;word-break:break-all;">
                                <logic:iterate id="lastNews2" name="lastNews2">
                                    <li style="height:20px">
                                    	<img src="crm/images/content/blueArr.gif"/>
                                        <logic:notEmpty name="lastNews2" property="newType">
                                            <span class="brown">[${lastNews2.newType}]</span>
                                        </logic:notEmpty><a href="messageAction.do?op=showNewsInfo&newCode=${lastNews2.newCode}&isEdit=0" target="_blank">${lastNews2.newTitle}</a>
                                        <logic:equal value="0" name="lastNews2" property="newIstop">
                                        	<img src="crm/images/content/upButton.gif" alt="置顶"/>
                                        </logic:equal>
                                        <img id="newIcon<%=count%>" src="crm/images/gif/new.gif" style="display:none"/>
                                        <span class="gray">${lastNews2.newInsUser}&nbsp;发布于&nbsp;<label id="ndate<%=count%>"></label></span>
                                    </li>
                                     <script type="text/javascript">
                                        dateFormat('ndate','${lastNews2.newDate}',<%=count%>);
										
										//今日发布的显示new图标
										if('${lastNews2.newDate}'.substring(0,10)=='${today}'){
											$("newIcon"+<%=count%>).style.display="";
										}
                                    </script>
                                    <% count++; %>
                                  </logic:iterate>
                            </ul>
                        </logic:notEmpty>  
                        <logic:empty name="lastNews2">
                            <div align="center" class="gray" style="padding-top:30px">	
                                暂无公告！
                            </div>
                        </logic:empty>
                    </div>
                </div>
            </td>
        </tr>
    </table>
    
  </body>
</html>
