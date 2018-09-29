﻿<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>职位管理</title>
    <link rel="shortcut icon" href="favicon.ico"/>
 	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sys.js"></script>
    <script type="text/javascript">
		function loadXml(){	
			//读取配置文件信息
			var xmlDoc;
			if(window.ActiveXObject){
				//获得操作的xml文件的对象
				xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
				xmlDoc.async = false;
				xmlDoc.load("system/role.xml");
				if(xmlDoc == null){
					alert('您的浏览器不支持xml文件读取,于是本页面禁止您的操作,推荐使用IE5.0以上可以解决此问题!');	
					return;
				}
			}
			//解析xml文件，判断是否出错
			if(xmlDoc.parseError.errorCode != 0){
			   alert(xmlDoc.parseError.reason);
			   return;
			}
			var level=xmlDoc.getElementsByTagName("num");//读取XML里面的级别数
			var n=level[0].childNodes[0].nodeValue;
			$("maxLev").innerText=n;//赋值给文本框里面的级别数
		}
		
    	
	 	createProgressBar();	
		window.onload=function(){
			loadXml();
			closeProgressBar();
		}
    </script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 职位设置</div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='userAction.do?op=searchLimRole'">职位设置</div>
                        </div>
                   </th>
                    <td id="addPos">
                        <a href="javascript:void(0)" onClick="addDiv(8);return false;" class="newBlueButton">添加职位</a>
                    </td>
                    <script type="text/javascript">displayLimAllow("sy002","addPos");</script>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
			 <div id="listContent">
				 <div class="dataList">
                 	<div class="gray" style="padding:4px; text-align:left; padding-left:15px">
                    	当前可用职级数为<span id="maxLev" class="red bold middle" style="padding:2px"></span>
                        <span id="modRoleLev" class='blue'>[<a href='javascript:void(0)' onClick='addDiv(9);return false;'>修改可用职级数</a>]</span>
                	</div>	
					<table id="tab" class="normal gridTab" style="width:100%" cellpadding="0" cellspacing="0">
						<tr>
                        	<th style="width:8%">职级</th>
							<th style="width:27%">职位名称</th>
							<th style="width:55%">职位描述</th>
							<th style="width:10%; border-right:0"><nobr>操作</nobr></th>
						</tr>
						<logic:notEmpty name="limRole">
						<logic:iterate id="lr" name="limRole">
						<tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                        	<td>${lr.rolLev}&nbsp;</td>
							<td style="text-align:left">${lr.rolName}&nbsp;</td>
							
							<td style="text-align:left">${lr.roleDesc}&nbsp;</td>
							<td style="border-right:0">
                                <img onClick="addDiv(6,'${lr.rolId}')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                <img onClick="addDiv(7,'${lr.rolId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>
							</td>
						</tr>
						      <script type="text/javascript">
									rowsBg('tr',<%=count%>);
	 	              			</script>
						 <% count++; %>
						</logic:iterate>
                        </logic:notEmpty>
                        <logic:empty name="limRole">
                        <tr>
                            <td colspan="4" class="noDataTd">暂无数据</td>
                        </tr>
                    	</logic:empty>  
					</table>
				</div>		       
        	</div>
  		</div> 
	 </div>
  </body>
</html>
