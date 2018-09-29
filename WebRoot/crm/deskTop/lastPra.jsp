<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>最近来往记录列表</title>
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
    <script type="text/javascript" src="js/cus.js"></script>
    </head>
  
  <body>
  	<div class="modTitle" style="margin-top:0px">今天</div>
    <logic:notEmpty name='curPraList' >
        <logic:iterate id='curPra' name='curPraList' indexId='count'>
            <div class="modCon">
                <img src="images/content/blueBlock.gif"/>&nbsp;
                <logic:notEmpty name="curPra" property="praType">
                    <span class="brown">[${curPra.praType}]</span>
                </logic:notEmpty><a href="cusServAction.do?op=showSalPraInfo&id=${curPra.praId}&isEdit=0" target="_blank">${curPra.praRemark}</a>
                <span class="mLink"><nobr>
                    <logic:notEmpty name="curPra" property="cusCorCus">
                        <a title="对应客户：<c:out value="${curPra.cusCorCus.corName}"/>" href="customAction.do?op=showCompanyCusDesc&corCode=${curPra.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${curPra.cusCorCus.corName}</a>
                    </logic:notEmpty>
                </nobr></span>
                <span id="exePraLayer${count}" style="display:none">
                    <span id='${curPra.praId}'>
                        <nobr>&nbsp;<img class='imgAlign' src='images/content/execute.gif' style='cursor:pointer;' onClick="modifyPraState('${curPra.praId}','${curPra.praExeDate}','${curPra.cusCorCus.corCode}')" border='0' alt='立即执行'/></nobr>
                    </span>
                    <span id='y${curPra.praId}' style='display:none'>
                        <img class='imgAlign' src='images/content/suc.gif' alt='已执行'/>
                    </span>
                </span>
                <script type="text/javascript">displayLimAllow("r015","exePraLayer${count}");</script>
           </div>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='curPraList'>
        <div class="gray">&nbsp;&nbsp;今天没有待执行的来往记录</div>
    </logic:empty>
        
    <div class="modTitle">七天内</div>
    <logic:notEmpty name='lastPraList' >
        <logic:iterate id='lastPra' name='lastPraList'>
            <div class="modCon">
                <img src="images/content/blueBlock.gif"/>&nbsp;
                <logic:notEmpty name="lastPra" property="praType">
                    <span class="brown aAlign">[${lastPra.praType}]</span>
                </logic:notEmpty>
                <a href="cusServAction.do?op=showSalPraInfo&id=${lastPra.praId}&isEdit=0" target="_blank">${lastPra.praRemark}</a>
                <span class="mLink"><nobr>
                    <logic:notEmpty name="lastPra" property="cusCorCus">
                        <a title="对应客户：<c:out value="${lastPra.cusCorCus.corName}"/>" href="customAction.do?op=showCompanyCusDesc&corCode=${lastPra.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${lastPra.cusCorCus.corName}</a>
                    </logic:notEmpty>
                </nobr></span>
                <span class='gray small'><nobr><label id="jhexe${count}"></label></nobr></span>
                <script type="text/javascript">
                    dateFormat('jhexe','${lastPra.praExeDate}','${count}');
                </script>
            </div>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='lastPraList'>
        <div class="gray">&nbsp;&nbsp;七天内没有待执行来往记录</div>
    </logic:empty>
  </body>
</html>
