﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>新建账号</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  	 <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
     <script type="text/javascript" src="crm/js/prototype.js"></script>
	 <script type="text/javascript" src="crm/js/common.js"></script>
     <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/sys.js"></script>
	<script type="text/javascript">
		function check(){
			var re =/^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){0,29}$/; 
			var rs =/^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){3,19}$/; 
			var errStr = "";
			if(isEmpty("soUserName")){
				errStr+="- 未选择使用人！\n";
			}
			else if($("isRepeat2").value==1){
				errStr+="- 此员工已分配了使用账号！\n";
			}
			if(isEmpty("login")){
				errStr+="- 未填写登录名！\n";
			}
			else if(!re.test($("login").value)){
				errStr+="- 登录名请输入30位以内的字母或数字！\n";
			}
			if($("isRepeat1").value==1){
				errStr+="- 此登录名已存在！\n";
			}
			if(isEmpty("pwd")){
				errStr+="- 未填写密码！\n";
			}
			else if(!rs.test($("pwd").value)){
				errStr+="- 密码请输入4-20位以内的字母或数字！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","保存中...");
				waitSubmit("cancel1");
				selectEmp.submit();
			}
		}
		function clearEmpValue(obj){
		 	obj.value="";
			$("seNo").value="";
			$("rolName").innerHTML="";
			$("errDiv2").style.display="none";
		}

		//保存表单
		function toSaveUser(){
			var check1 = new Array('userAction.do?op=checkLoginName&loginName=','login',null,1);
			var check2 = new Array('empAction.do?op=checkIsUseEmp&seNo=','seNo',null,2);
			checkRepeatForm(check1,check2);
		}
		//选择使用人员赋值结束后调用
		function checkIsUseEmp(){
		    checkIsRepeat($("seNo"),'empAction.do?op=checkIsUseEmp&seNo=',null,2);
		}
	</script>
  </head>
  
  <body>
	<div class="inputDiv">
    	<form action="userAction.do" method="post" id="" name="selectEmp">
			<input type="hidden" name="op" value="addEmp">
            <input type="hidden" id="isRepeat1"/>
            <input type="hidden" id="isRepeat2"/>
            <div id="errDiv1" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此登录名已存在</div>
            <div id="errDiv2" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此员工已分配了使用账号</div>
    		<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                    <tr>
                    <th>上级账号：</th>
                    <td style="font-weight:normal">
                        <input type="hidden" name="upCode" id="upCode" value="${upUserCode}"/>
                        <logic:equal name="isAddLowLev" value="0">
                            <input id="seName" class="inputSize2 lockBack" style="width:120px" type="text" title="此处文本无法编辑，双击可清空文本"  ondblClick="clearInput(this,'upCode')" readonly/>&nbsp;
                            <button id="selUp" class="butSize2" onClick="parent.addDivBrow(14)">选择</button>
                     	</logic:equal>
                     	<logic:equal name="isAddLowLev" value="1">${upUserName}</logic:equal>
			     	</td>
                    </tr>
                     <tr>
                        <th><span class='red'>*</span>使用人员：</th>
                        <td>
                            <input type="hidden" id="seNo" name="seNo">
                            <input id="soUserName" class="inputSize2 lockBack" style="width:120px" type="text" title="此处文本无法编辑，双击可清空文本"  ondblClick="clearEmpValue(this)" readonly/>
                            <input type="button" class="butSize2" onClick="parent.addDivBrow(12,'sys')" value="选择"/>
                            &nbsp;&nbsp;<input type="button" class="butSize2" onClick="parent.addDivBrow(15)" value="添加"/>
                        </td>
                    </tr>
                    <tr>
                      <th>职位：</th>
                      <td><span id="rolName"></span>&nbsp;</td>
                    </tr>
                    <tr>
                        <th style="vertical-align:top;"><span class='red'>*</span>登录名：</th>
                        <td colspan="3" style="height:30px; vertical-align:middle; font-weight:normal;"><input type="text" class="inputSize2" name="limUser.userLoginName" id="login" onBlur="checkIsRepeat(this,'userAction.do?op=checkLoginName&loginName=',null,1)">&nbsp;<span class="gray">30位以内的字母或数字</span>
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>密码：</th>
                        <td colspan="3" style="height:30px; vertical-align:middle; font-weight:normal;"><input type="text"class="inputSize2 inputBoxAlign" name="limUser.userPwd" id="pwd"><span id="pwdTxt" class="gray">&nbsp;4-20位的字母或数字</span>&nbsp;</td>
                    </tr>
                   
                    <tr>
                        <th style="vertical-align:top">备注：</th>
                        <td colspan="3">
                            <textarea name="limUser.userDesc"  cols="32" rows="2" onBlur="autoShort(this,4000)">${limUser.userDesc}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border:0px; text-align:center">
                            <input type="button" class="butSize1" id="Submit" value="保存" onClick="toSaveUser()">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="button" class="butSize1" id="cancel1" value="取消" onClick="cancel()"></td>
                    </tr>	
                </tbody>
			      
    		</table>
    	</form>
    </div>
  </body>
</html>
