<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存纪念日</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("mdDate")){   
				errStr+="- 未填写纪念日名称！\n"; 
			}
			if(isEmpty("mdDate")){
				errStr+="- 未选提醒日期！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave");
				waitSubmit("doCancel");
				return $("saveForm").submit();
			}
		}
		
		//初始化表单
		function initForm(){
			<c:if test="${empty memDate}">
			//新建
			disableInput('mdId');
			$("op").value = "saveMemDate";
			$("conId").value = "${conId}";
			</c:if>
			
			<c:if test="${!empty memDate}">
			//编辑
			$("op").value = "updMemDate";
			$("mdId").value = "${memDate.mdId}";
			$("conId").value = "${memDate.mdContact.conId}";
			removeTime("mdDate","${memDate.mdDate}",1);
			</c:if>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="saveForm" action="customAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
            <input type="hidden" id="mdId" name="memDate.mdId"/>
            <input type="hidden" id="conId" name="conId"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
                    <tr>
                        <th class="required">纪念日名称：<span class='red'>*</span></th>
                        <td>
                        	<input class="inputSize2" type="text" id="mdName" name="memDate.mdName" onBlur="autoShort(this,50)" value="${memDate.mdName}" >
                        </td>
                        <th class="required">提醒日期：<span class='red'>*</span></th>
                        <td><input name="mdDate" id="mdDate" type="text" class="inputSize2 Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                    </tr>
                    <tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3">
                        	<textarea class="inputSize2L" rows="5" name="memDate.mdRemark" onBlur="autoShort(this,200)">${memDate.mdRemark}</textarea>
                        </td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                </tbody>
			</table>
		</form>
    </div>
	</body>
</html>
