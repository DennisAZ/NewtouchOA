<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>修改工作</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
   <script type="text/javascript">      
			function check(){
				var errStr = "";
		 		if(isEmpty("satTitle")){
					errStr+="- 未填写工作名称！\n";
				}
				else if(checkLength("satTitle",200)){
					errStr+="- 工作名称不能超过200个字！\n";
				}
				if(isEmpty("nodeIds")){
					errStr+="- 未选择执行人！\n";
				}
				if(errStr!=""){
					errStr+="\n请返回修改...";
					alert(errStr);
					return false;
				}
				else{
					waitSubmit("dosend","保存中...");
					waitSubmit("doCancel");
					return $("create").submit();	
				}		  
			}
				
			function showIndex(){
				var userNames = '';
				var userIds = '';
				var sttName = '${salTask.salTaskType.typId}'; 
				if($("sttName")!=null){
					$("sttName").value=sttName;
				}
	    		
				var o='${salTask.stStu}';
				var p='${salTask.stLev}';
				if(o=="0"){
					$("stStu0").checked=true;
				}
				else if(o=="1"){
					$("stStu1").checked=true;
				}
				else if(o=="2"){
					$("stStu2").checked=true;
				}
				if(p=="0"){
					$("stLev0").checked=true;
				}
				else if(p=="1"){
					$("stLev1").checked=true;
				}
				else if(p=="2"){
					$("stLev2").checked=true;
				}
				<logic:notEmpty name="salTask" property="taLims">
				<logic:iterate id="taLs" name="salTask" property="taLims">
					<logic:equal value="1" name="taLs" property="taIsfin">
						userNames += '<c:out value="${taLs.salEmp.seName}[${taLs.salEmp.limRole.rolName}]"/>'+';&nbsp;';
						userIds += '${taLs.salEmp.seNo}' + ',';
					</logic:equal>
				</logic:iterate>
				</logic:notEmpty>
				
				$("nodeNames").innerHTML = userNames;
				$("nodeIds").value = userIds;
		}
		window.onload=function(){
			if('${salTask}'!=null&&'${salTask}'!=''){
			  if('${salTask.stStu}'!="")
				  showIndex();
			}
		    $("stFinDate").value="${salTask.stFinDate}".substring(0,10);
		    $("stStartDate").value="${salTask.stStartDate}".substring(0,10);	
		}
</script> 
</head>
  <body>
  	<div class="inputDiv">
		<form action="salTaskAction.do" method="post" name="create">
  			<input type="hidden" name="op" value="updateSalTask">
  			<input type="hidden" name="stId" value="${salTask.stId}">
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                    <tr>
                        <th class="descTitleL">工作名称：<span class='red'>*</span></th>
                        <th class="descTitleR" colspan="3"><input name="salTask.stTitle" id="satTitle" type="text" value="<c:out value="${salTask.stTitle}"/>" class="inputSize2L" onBlur="autoShort(this,200)"/></th>				
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>类别：</th>
                        <td>
                            <logic:notEmpty name="salTaskType">
                            <select name="taskType" id="sttName" class="inputSize2 inputBoxAlign">
                            	<option  value="">&nbsp;</option>
                                <logic:iterate id="stt" name="salTaskType" scope="request">
                                <option value="${stt.typId}">${stt.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="salTaskType">
                            <select id="sttName" class="inputSize2 inputBoxAlign" disabled>
                                <option  value="">未添加</option>
                            </select>
                            </logic:empty>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                        <th>优先级：</th>
                        <td>
                        <input type="radio" id="stLev0" name="salTask.stLev" value="0" checked="checked"><label for="stLev0">低&nbsp;&nbsp;</label>
                        <input type="radio" id="stLev1" name="salTask.stLev" value="1"><label for="stLev1">中&nbsp;&nbsp;</label>
                        <input type="radio" id="stLev2" name="salTask.stLev" value="2"><label for="stLev2">高&nbsp;&nbsp;</label></td>
                    </tr>
                    <tr>
                        <th>状态：</th>
                        <td class="longTd" colspan="3">
                        <input type="radio" id="stStu0" name="salTask.stStu" value="0" checked="checked"><label for="stStu0">执行中&nbsp;&nbsp;</label><input type="radio" id="stStu1" name="salTask.stStu" value="1"><label for="stStu1">已完成&nbsp;&nbsp;</label><input type="radio" id="stStu2" name="salTask.stStu" value="2"><label for="stStu2">取消&nbsp;&nbsp;</label></td>
                    </tr>
                    <tr>
                    	<th>开始日期：</th>
                        <td>
                        <input  id="stStartDate" name="stStartDate" type="text"  class="Wdate inputSize2" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                        <th>结束日期：</th>
                        <td>
                        <input id="stFinDate" name="stFinDate" type="text"   class="Wdate inputSize2" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                    </tr>
                    <tr>
                        <th class="required"><a href="javascript:void(0)" title="点击选择执行人" onClick="parent.addDivBrow(12,'getNames');return false;">执行人</a>：<span class='red'>*</span></th>
                        <td colspan="3">
                        	<a id="nodeNames" title="点击选择执行人" class="nodeNamesLayer inputSize2L" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                        	<input type="hidden" id="nodeIds" name="nodeIds"/>
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>描述：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="10" name="salTask.stRemark"  onblur="autoShort(this,4000)">${salTask.stRemark}</textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input type="button" class="butSize1" id="dosend" value="保存" onClick="check()">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" class="butSize1" id="doCancel"  value="取消" onClick="cancel()"></td>
                    </tr>	
                </tbody>		
	  		</table>
		</form>
  	    <script type="text/javascript">
		  removeTime('str','${salTask.stRelDate}',2);
        </script>
	</div>
  </body>
  
</html>
