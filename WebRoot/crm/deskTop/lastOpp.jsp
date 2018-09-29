<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>最近销售机会列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/desk.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
		
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
  	
    </head>
  
  <body>
  	<div class="modTitle" style="margin-top:0px">今天</div>
    <logic:notEmpty name='curOppList' >
        <logic:iterate id='curOpp' name='curOppList'>
            <div class="modCon">
                <img src="images/content/blueBlock.gif"/>&nbsp;
                <span class="brown" title="${curOpp.oppLev}">[<span id="lev<%=count%>"></span>]</span>
                <a href="cusServAction.do?op=showSalOppInfo&id=${curOpp.oppId}&isEdit=0" target="_blank">${curOpp.oppTitle}</a>
                <logic:notEmpty name="curOpp" property="oppState">
                    <span class="deepGreen"><nobr>[${curOpp.oppState}]</nobr></span>
                </logic:notEmpty>
                <span class="mLink"><nobr>
                <logic:notEmpty name="curOpp" property="cusCorCus">
                    <a title="对应客户：${curOpp.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${curOpp.cusCorCus.corCode}" target="_blank" style="cursor:pointer"><img src="images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${curOpp.cusCorCus.corName}</a>
                </logic:notEmpty>
                </nobr></span>
           </div>
           <script type="text/javascript">
                $("lev"+<%=count%>).innerHTML="${curOpp.oppLev}".substring(0,1);
            </script>
            <% count++; %>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='curOppList'>
        <div class="gray">&nbsp;&nbsp;今天没有关注的销售机会</div>
    </logic:empty>
    
    <div class="modTitle">七天内</div>
    <logic:notEmpty name='lastOppList' >
        <logic:iterate id='lastOpp' name='lastOppList'>
           <div class="modCon">
                <img src="images/content/blueBlock.gif"/>&nbsp;
                <span class="brown" title="${lastOpp.oppLev}">[<span id="lev<%=count%>"></span>]</span>
                <a href="cusServAction.do?op=showSalOppInfo&id=${lastOpp.oppId}&isEdit=0" target="_blank">${lastOpp.oppTitle}</a>
                <logic:notEmpty name="lastOpp" property="oppState">
                    <span class="deepGreen"><nobr>[${lastOpp.oppState}]</nobr></span>
                </logic:notEmpty>
                <span class="mLink"><nobr>
                    <logic:notEmpty name="lastOpp" property="cusCorCus">
                        <a title="对应客户：${lastOpp.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${lastOpp.cusCorCus.corCode}" target="_blank"><img src="images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${lastOpp.cusCorCus.corName}</a>
                    </logic:notEmpty>
                </nobr></span>
                <span class='gray small'><nobr><label id="oppDate<%=count%>"></label>&nbsp;</nobr></span>
                <script type="text/javascript">
                    dateFormat('oppDate','${lastOpp.oppExeDate}',<%=count%>);
					$("lev"+<%=count%>).innerHTML="${lastOpp.oppLev}".substring(0,1);
                </script>
                <% count++; %>
           </div>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='lastOppList'>
        <div class="gray">&nbsp;&nbsp;七天内没有关注的销售机会</div>
    </logic:empty>
        
  </body>
</html>
