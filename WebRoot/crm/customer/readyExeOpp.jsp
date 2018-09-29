<%@ page language="java"  pageEncoding="UTF-8"%>
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
    
    <title>最近关注的销售机会</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:center;
		}
		#tipsTab td{
			padding-left:2px;
			padding-right:2px;
		}
		#todayTips ,#lastTips {
			height:191px;
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
    <script type="text/javascript" src="crm/js/cus.js"></script>

  </head>
  
  <body>
  	<table id="tipsTab" class="normal" cellpadding="0" cellspacing="0" width="100%">
    	<tr>
    		<td width="50%">
          		<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendarT.gif" alt="关注的销售机会" class="imgAlign" />&nbsp;<span class="bigger">今天</span>&nbsp;关注的销售机会
                    </div>
                    <div id="todayTips" class="divWithScroll2">
                        <logic:notEmpty name="curOppList">			
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="salOpps" name="curOppList">
                                    <li>
                                    	<img src="crm/images/content/blueBlock.gif"/>&nbsp;
                                    	<span class="brown" title="${salOpps.oppLev}">[<span id="lev<%=count%>"></span>]</span>
                                    	<a class="aAlign" href="cusServAction.do?op=showSalOppInfo&id=${salOpps.oppId}" target="_blank">${salOpps.oppTitle}</a>
                                        <logic:notEmpty name="salOpps" property="oppState">
                                        <span class="deepGreen" style="padding:2px" title="机会状态"><nobr>[${salOpps.oppState}]</nobr></span>
                                        </logic:notEmpty>
                                        <span class="mLink"><nobr>
                                            <logic:notEmpty name="salOpps" property="cusCorCus">
                                                <a title="对应客户" href="customAction.do?op=showCompanyCusDesc&corCode=${salOpps.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${salOpps.cusCorCus.corName}</a>
                                            </logic:notEmpty>
                                        </nobr></span>
                                    </li>
                                    <script type="text/javascript">
                                    	$("lev"+<%=count%>).innerHTML="${salOpps.oppLev}".substring(0,1);
                                    </script>
                                    <% count++; %>
                                  </logic:iterate>
                            </ul>
                        </logic:notEmpty>  
                        <logic:empty name="curOppList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                今天没有关注销售机会
                            </div>
                        </logic:empty>
                    </div>
                </div>
            </td>
            
            <td width="50%">
            	<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendar7.gif" alt="关注销售机会" class="imgAlign" />&nbsp;<span class="bigger">七天内</span>&nbsp;关注的销售机会提醒
                    </div>
                    <div id="lastTips" class="divWithScroll2">
                        <logic:notEmpty name="lastOppList">		
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="salOpps" name="lastOppList">
                                    <li>
                                    	<img src="crm/images/content/blueBlock.gif"/>&nbsp;
                                    	<span class="brown" title="${salOpps.oppLev}">[<span id="lev<%=count%>"></span>]</span>
                                    	<a href="cusServAction.do?op=showSalOppInfo&id=${salOpps.oppId}" target="_blank">${salOpps.oppTitle}</a>
                                        <span class="deepGreen" style="padding:2px" title="机会状态">
                                        	<logic:notEmpty name="salOpps" property="oppState"><nobr>[${salOpps.oppState}]</nobr></logic:notEmpty>
                                        </span>
                                        <span class="mLink"><nobr>
                                            <logic:notEmpty name="salOpps" property="cusCorCus">
                                            	<a title="对应客户" href="customAction.do?op=showCompanyCusDesc&corCode=${salOpps.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${salOpps.cusCorCus.corName}</a>
                                            </logic:notEmpty>
                                        </nobr></span>
                                        <span class='gray'><nobr><label id="oppDate<%=count%>"></label>&nbsp;</nobr></span>
                                        <script type="text/javascript">
											dateFormat('oppDate','${salOpps.oppExeDate}',<%=count%>);
											$("lev"+<%=count%>).innerHTML="${salOpps.oppLev}".substring(0,1);
										</script>
										<% count++; %>
                                    </li>
                                </logic:iterate>
                            </ul>
                        </logic:notEmpty>
                        <logic:empty name="lastOppList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                七天内没有关注的销售机会
                            </div>
                        </logic:empty>
                    </div>
               </div>
            </td>
        </tr>
   	</table> 
  </body>
</html>
