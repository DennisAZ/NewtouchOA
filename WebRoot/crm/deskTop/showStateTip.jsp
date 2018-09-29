<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0,count2=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>显示状态</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
        <style type="text/css">
    	body{
			background-color:#fff;
			text-align:left;
		}
    </style>
     <script type="text/javascript">
    	function initForm(){
    		if("${state}" ==""){
    			createCusSel("corState","t_state","");
    		}else{
    			createCusSel("corState","t_state","${state}");
    		}
    		if("${seNo}" != ""){
    			$("seNo").value = "${seNo}";
    		}
		}
		
    	window.onload=function(){
			initForm();
			//增加清空按钮
			//createCancelButton("",'searchForm',-50,5,'searButton','after');
		}
    </script>
  </head>
  
  <body>
  			   <div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm"  action="customAction.do">
                	    <input type="hidden" name="op" value="toShowCusState" />
                	  客户状态： <select id="corState" class="inputBoxAlign" name="corState"><option  value="">请选择</option></select>&nbsp;&nbsp;
                         业务员 ：<c:if test="${!empty empList}"><select id="seNo" name="seNo" class="inputBoxAlign">
                         			<option  value="">请选择</option>
                                    <c:forEach var="emp" items="${empList}">
                         				<option value="${emp.seNo}">${emp.seName}</option>
                         			</c:forEach>
                             </select></c:if>
                          	<c:if test="${empty empList}"><select id="stateId" class="inputBoxAlign" disabled><option value="">未添加</option></select></c:if>&nbsp;
                         	<input type="submit" class="butSize3 inputBoxAlign" id="searButton" value="查询" />&nbsp;&nbsp;
                    </form>
                </div>
                <logic:notEmpty name="list">
                <div style="padding:10px; text-align:center;">
                  <table  class="normal rowstable" cellpadding="0" cellspacing="0" style="width:95%">		
                    <tr>
	                    <th style="width:40%">单位名称</th>
	                    <th style="width:30%">到期日期</th>
	                    <th style="width:30%;align:center">业务员</th>
                	</tr>
                    <logic:iterate id="l" name="list">
                        <tr id="tr<%= count2%>" onMouseOver="focusTr('tr',<%= count2%>,1)" onMouseOut="focusTr('tr',<%= count2%>,0)">
                            <td>&nbsp;<a href='javascript:void(0)' onclick="descPop('customAction.do?op=showCompanyCusDesc&corCode=${l.corCode}')">${l.corName}</a></td>
							<td>&nbsp;<span id="corOnDate<%= count2%>"></span></td>
							<td>&nbsp;${l.salEmp.seName}</td>
                        </tr>
                        <script type="text/javascript">
                            rowsBg('tr',<%=count2%>);
                            dateInitToShow("corOnDate<%= count2%>",'${l.corOnDate}');
                        </script>
                     <%count2++;%>
                    </logic:iterate>
                  </table>
                  </div>
                </logic:notEmpty>
                
                <logic:empty name="list">
        			<div class="gray">&nbsp;&nbsp;没有数据</div>
                </logic:empty>
  </body>

</html>
