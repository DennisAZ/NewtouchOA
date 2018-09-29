<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>  
    <title>导出数据</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
		function outCheck(){
			var chks = document.getElementsByName("checkbox");
			var dataCols=[];
			for(var i=0;i<chks.length;i++){
				if(chks[i].checked==true){ dataCols.push(chks[i].value); }
			}
			if(dataCols==""){ alert("请选择要导出的字段！"); }
			else{ batchOut(dataCols); }
		}
		
		function batchOut(dataCols){
			waitSubmit("doOut");
			waitSubmit("doCancel");
			$("dataCols").value=dataCols.toString();
            return $("outForm").submit();
		}
		
		function initPage(){
			var formArgs,colNames;
			var cusColNames = [ 
						["客户编号","客户名称","助记简称","行业"],
						["客户状态","来源","客户类型","客户级别"],
						["关系等级","所属人","最近联系日期","证件类型"],
						["证件号码","人员规模","客户简介","所在地区"],
						["地址","电话","手机","传真"],
						["邮编","网址","电子邮件","QQ"],
						["备注"]
					];
			var conColNames = [ 
						["姓名","对应客户","所属人","性别"],
						["公司/部门","职务","分类","关注事由"],
						["对应日期","手机","电子邮件","QQ"],
						["MSN", "邮编","家庭电话","地址"],
						["其他联系方式","办公电话","传真","爱好"],
						["忌讳","教育背景","备注"]
					];
			switch("${outObj}"){
				case "cus":
					formArgs = [
						["range","<c:out value="${range}" />"],
						["corName","<c:out value="${corName}" />"],
						["corNum","<c:out value="${corNum}" />"],
						["cType","<c:out value="${cType}" />"],
						["corAdd","<c:out value="${corAdd}" />"],
						["seName","<c:out value="${seName}" />"],
						["startTime","<c:out value="${startTime}" />"],
						["endTime","<c:out value="${endTime}" />"],
						["cusInd","<c:out value="${cusInd}" />"],
						["type","<c:out value="${type}" />"],
						["filter","<c:out value="${filter}" />"],
						["color","<c:out value="${color}" />"],
						["cusIds",getBacthIds()]
					];
					$("outForm").action = "customAction.do";
					$("op").value = "outCus";
					colNames = cusColNames;
					break;
				case "cusCon":
					formArgs = [
						["range","<c:out value="${range}" />"],
						["conName","<c:out value="${conName}" />"],
						["cusName","<c:out value="${cusName}" />"],
						["conLev","<c:out value="${conLev}" />"],
						["seName","<c:out value="${seName}" />"],
						["type","<c:out value="${type}" />"],
						["cusIds",getBacthIds()]
					];
					$("outForm").action = "customAction.do";
					$("op").value = "outCont";
					colNames = conColNames;
					break;
			}
			createHideInput(formArgs);
			createColTd(colNames);
			if(colNames!=undefined){ $("colNames").value = colNames.toString(); }
			restoreSubmit("doOut");
			restoreSubmit("doCancel");
		}
		
		function createHideInput(formArgs){
			var inputObj = null;
			for(var i=0; i<formArgs.length; i++){
				inputObj = document.createElement("input");
				inputObj.type = "hidden";
				inputObj.name = formArgs[i][0];
				inputObj.value = formArgs[i][1];
				$("outForm").appendChild(inputObj);
			}
		}
		
		function createColTd(colNames){
			var trObj = null;
			var tdObj = null;
			var n = 0;
			for(var i=0; i<colNames.length; i++){
				trObj = document.createElement("tr");
				if(i==colNames.length-1){
					trObj.className = "noBorderBot";
				}
				for(var j=0; j<colNames[i].length; j++){
					tdObj = document.createElement("td");
					tdObj.innerHTML = "<input type='checkbox' name='checkbox' id='col"+n+"' value='"+n+"'/><label for='col"+n+"'>"+colNames[i][j]+"</label>";
					trObj.appendChild(tdObj);
					n++;
				}
				$("colTd").appendChild(trObj);
			}
			
		}
		
		window.onload = function() {
			initPage();
		} 
	</script>
</head>
  <body>
  <div class="inputDiv">
  	<form id="outForm" method="post" >
  		<input type="hidden" id="op" name="op" />
        <input type="hidden" id="dataCols" name="dataCols" />
        <input type="hidden" id="colNames" name="colNames" />
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr><td colspan="4"><div><input type="checkbox" id="checkAll" onClick="cascadeSel('colTd','checkAll')" /><label for="checkAll">全选</label></div></td></tr>
            </thead>
            <tbody id="colTd"></tbody>
            <tr class="submitTr">	
                <td colspan="4">
                    <input type="button" class="butSize1" id="doOut" value="导出" onClick="outCheck()" disabled="true" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel"  value="取消" onClick="cancel()" disabled="true" />
                </td>
            </tr>
            <tr>
                <td class="tipsTd" colspan="4">
                    <div class="tipsLayer">
                        <ul>
                            <li>导出大量数据时请耐心等待，如长时间无反应可关闭此窗口重新操作，导出完成后请关闭此窗口</li>
                        </ul>
                    </div>
                </td>
            </tr>				
	  </table>
	</form>
  </div>
  </body>
</html>
