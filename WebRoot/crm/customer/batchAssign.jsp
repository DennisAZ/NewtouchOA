<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>分配负责人</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background:#fff;
		}
    </style>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
		function updUserSel(obj){
			if(obj.value=="0"){
				$("seNo").value="";
				$("soUserName").value="";
				disableInput("soUserName,userBtn");
			}
			else{
				restoreInput("soUserName,userBtn");
			}
		}
	
    	function check(){
			var errStr = "";
			if($("cusState")!=null&&isEmpty("cusState")){
				errStr+="- 未选择客户状态！\n";
			}
			if($("corState").value!="0"&&isEmpty("seNo")){
				errStr+="- 未选择负责人！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("doSave");
				waitSubmit("doCancel");
				$("ids").value=getBacthIds();//给隐藏域赋值
				return $("saveForm").submit();
			}
		}
		
		window.onload = function (){
			createCusSel("corState","t_state","0");
		}
	
  </script>
  </head>
  
  <body> 
  	<div style="padding:5px 0 0 0;">
        <form id="saveForm" action="customAction.do" method="post" style="margin:0; padding:0;">
            <input type="hidden" id="op" name="op" value="batchAssign" />
            <input type="hidden" id="ids" name="ids" value="" />
            <input type="hidden" name="type" value="${type}" />
            <table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
                <tbody>
                <c:if test="${type == 'cus'}">
                <tr>
                	<th class="required">客户状态：<span class="red">*</span></th>
                   	<td><select id="corState" name="corState" class="inputSize2" onChange="updUserSel(this)"></select></td>
                </tr>
                </c:if>
                <tr class="noBorderBot">
                     <th class="required">负责人：<span class="red">*</span></th>
                     <td><input type="hidden" name="seNo" id="seNo" />
                     <input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" disabled ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     <button id="userBtn" class="butSize2" onClick="parent.addDivBrow(12)" disabled>选择</button></td>
                </tr>
                <!--<tr class="noBorderBot">
                	<th>修改日期：</th>
                	<td>
                	<input name="assignDate" id="date"  type="text" class="Wdate inputSize2" style="cursor:hand" readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
                	</td>
                </tr>-->
                <tr class="submitTr">
                  <td colspan="2">
                    <button id="doSave" class ="butSize1" onClick="check()">确定</button>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <button id="doCancel" class ="butSize1" onClick="cancel()">取消</button>
                 </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div> 
	</body>
</html>
