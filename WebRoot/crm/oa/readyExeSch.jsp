<%@ page language="java"  import="com.psit.struts.util.Page" pageEncoding="UTF-8"%>
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

    Page pageobj=(Page)request.getAttribute("page"); 
    String msg=(String)request.getAttribute("searchmsg");
    String praTitle="";
   if((String)request.getAttribute("praTitle")==null)
   {praTitle="";}
   else 
   { praTitle=(String)request.getAttribute("praTitle");}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>日程安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
		#todayTips ul,#lastTips ul {
			padding:0px;
			margin:0px;
		}
		#todayTips li,#lastTips li {
			padding:2px;
			border-bottom:#999999 1px dotted;
		}
		#todayTips span,#lastTips span{
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
  </head>
  
  <body>
  	<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
    	<tr>
    		<td style="width:49%; vertical-align:top;">
          		<div class="tipBox" style="height:200px;">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendarT.gif" alt="日程安排提醒" class="imgAlign" />&nbsp;<span class="bigger">今天</span>&nbsp;有&nbsp;<span class="bigger">${num}</span>&nbsp;条待办日程
                    </div>
                    <div id="todayTips" class="divWithScroll2" style="height:172px;">
                        <logic:notEmpty name="curSchList">			
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="schedules" name="curSchList">
                                    <li><img src="crm/images/content/blueArr.gif"/>&nbsp;
                                    <logic:equal value="已完成" name="schedules" property="schState">
                                        <logic:notEmpty name="schedules" property="schType">
                                        	<span class="brown">[${schedules.schType.typName}]</span>
                                        </logic:notEmpty>
                                        <a href="javascript:void(0)" onClick="oaPopDiv(32,${schedules.schId},1);return false;">${schedules.schTitle}</a>
                                        <span><img class="imgAlign" src="crm/images/content/suc.gif" alt="已完成"/></span>
                                    </logic:equal>
                                    <logic:equal value="未完成" name="schedules" property="schState">
                                        <logic:notEmpty name="schedules" property="schType">
                                        	<span class="brown">[${schedules.schType.typName}]</span>
                                        </logic:notEmpty>
                                       	<a href="javascript:void(0)" onClick="oaPopDiv(32,${schedules.schId},1);return false;">${schedules.schTitle}</a>
                                        <span id="${schedules.schId}"><img class="imgAlign" src="crm/images/content/execute.gif" border="0" style="cursor:pointer" onClick="modifySchState('${schedules.schId}')" alt="点击完成"/></span>
                                        <span id="y${schedules.schId}" style="display:none"><img class="imgAlign" src="crm/images/content/suc.gif" alt="已完成"/></span>
                                    </logic:equal>
                                     <span class="gray"><nobr>${schedules.schStartTime}&nbsp;-&nbsp;${schedules.schEndTime}</nobr></span>                                
                                    </li>
                                  </logic:iterate>
                            </ul>
                        </logic:notEmpty>  
                        <logic:empty name="curSchList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                今天没有日程安排
                            </div>
                        </logic:empty>
                    </div>
                </div>
            </td>
            <td style="width:5px">&nbsp;</td>
            <td style="vertical-align:top;">
            	<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendar7.gif" alt="日程安排提醒" class="imgAlign" />&nbsp;<span class="bigger">七天内</span>&nbsp;有&nbsp;<span class="bigger">${num2}</span>&nbsp;条待办日程
                    </div>
                    <div id="lastTips" class="divWithScroll2" style="height:172px">
                        <logic:notEmpty name="lastSchList">		
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="schedules2" name="lastSchList">
                                    <li><img src="crm/images/content/blueArr.gif"/>&nbsp;
                                    	<logic:notEmpty name="schedules2" property="schType">
                                        	<span class="brown aAlign">[${schedules2.schType.typName}]</span>
                                        </logic:notEmpty>
                                        <a href="javascript:void(0)" onClick="oaPopDiv(32,${schedules2.schId},1);return false;">${schedules2.schTitle}</a>&nbsp;
                                        <span class="gray"><nobr><label id="sdate1<%=count%>"></label>&nbsp;</nobr></span>
                                        <span class="gray"><nobr>${schedules2.schStartTime}&nbsp;-&nbsp;${schedules2.schEndTime}</nobr></span>
                                    </li>
                                    <script type="text/javascript">
										dateFormat('sdate1','${schedules2.schStartDate}',<%=count%>);
									</script>
									<% count++; %>
                                </logic:iterate>
                            </ul>
                        </logic:notEmpty>
                        <logic:empty name="lastSchList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                七天内没有日程安排
                            </div>
                        </logic:empty>
                    </div>
               </div>
            </td>
        </tr>
   	</table>
  </body>
</html>
