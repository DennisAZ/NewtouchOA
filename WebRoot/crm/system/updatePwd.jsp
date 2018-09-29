<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>修改登录信息</title>
    <link rel="shortcut icon" href="favicon.ico"/>
  	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <style type="text/css">
		#formTab td{
			height:25px;
		}
	</style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/sys.js"></script>
	<script type="text/javascript">
	  
		function check(){
			var re =/^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){0,29}$/; 
			var rs =/^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){3,19}$/; 
			 
			var errStr = "";
			if(isEmpty("login")){
				errStr+="- 未填写登录名！\n";
			}
			else if(!re.test($("login").value)){
			 	errStr+="- 登录名请输入30位以内的字母或数字！\n";
			}
			if($("isRepeat").value==1){
				errStr+="- 此登录名已存在！\n";
			}
			if(isEmpty("pwd")){   
				errStr+="- 未填写原始密码！\n"; 
			}
			else if($("pwd").value!='${limUser.userPwd}'){
				errStr+="- 原始密码不正确！\n";
			}
			
			if(isEmpty("pwd1")){
				errStr+="- 未填写新密码！\n";
			}
			else if(!rs.test($("pwd1").value)){
			 	errStr+="- 密码请输入4-20位的字母或数字！\n";
			}
			if(isEmpty("pwd2")){
				errStr+="- 未填写确认密码！\n";
			}
			else if($("pwd1").value!=$("pwd2").value){
				errStr+="- 新密码与确认的密码不一致！\n";	   
			}
			 
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","保存中...");
				$("editPwd").submit();
			}		  
		}
		var ms='${msg}';
		if(ms!=null && ms!=""){
			alert(ms);
		}
	</script>
  </head>
  
  <body>
  <logic:notEmpty name="limUser">
  <div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此登录名已存在</div>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 修改登录信息</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
           		<tr>
            		<th>
	      			<div id="tabType">
						<div id="tabType1" class="tabTypeWhite" style="cursor:default">修改登录信息</div>                           
           			</div>
				 	</th>
            	</tr>
            </table>
			<div id="listContent" style="padding:20px">
                <div class="grayBack" style="width:500px; padding:10px;">
                	<div class="blue bold middle" style="text-align:left; padding-bottom:12px;">修改登录信息</div>
                    <form action="userAction.do" method="post" id="editPwd" style="padding:0px; margin:0px">
                    <input type="hidden" name="op" value="updatePwd">
                    <input type="hidden" name="userCode" value="${limUser.userCode}">
                    <input type="hidden" id="isRepeat" />
                    <table id="formTab" cellpadding="0" cellspacing="0" class="normal noBr" style=" width:90%">
                        <tr>
                            <td style="text-align:right;vertical-align:top; width:120px"><span class='red'>*</span>&nbsp;登&nbsp;录&nbsp;名：</td>
                            <td colspan="3"><input type="text" class="inputSize2" style="width:90%" name="loginName" id="login" value="<c:out value="${limUser.userLoginName}"/>" onBlur="checkIsRepeat(this,'userAction.do?op=checkLoginName&loginName=','<c:out value="${limUser.userLoginName}"/>')">
                    		</td>
                        </tr>
                        <tr>
                            <td style="text-align:right;"><span class='red'>*</span>原始密码：</td>
                            <td colspan="3"><input type="password" class="inputSize2" style="width:90%" name="" id="pwd"></td>
                        </tr>
                        <tr>
                            <td style="text-align:right"><span class='red'>*</span>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
                            <td colspan="3"><input type="password" class="inputSize2" style="width:90%" name="userPwd" id="pwd1"></td>
                        </tr>
                        <tr>
                            <td style="text-align:right"><span class='red'>*</span>确认密码：</td>
                            <td colspan="3"><input type="password" class="inputSize2" style="width:90%" name="" id="pwd2"></td>
                        </tr>
                        <tr>
                            <td colspan="4" style="text-align:center">
                                <input type="button" class="butSize1" id="Submit" value="保存" onClick="checkRepeatForm(new Array('userAction.do?op=checkLoginName&loginName=','login','<c:out value="${limUser.userLoginName}"/>'))"/>
                            </td>
                        </tr>
                    </table>
                </form>
                <br/>
                <div class="tipsLayer">
                    <ul>
                        <li>1.&nbsp;带&nbsp;<span class='red'>*</span>&nbsp;为必填项。</li>
                        <li>2.&nbsp;登录名为30位以内的字母或数字。</li>
                        <li>3.&nbsp;密码为4-20位的字母或数字。</li>
                    </ul>
                </div>
                </div>
                
        	</div>
  		</div> 
	 </div>
  </logic:notEmpty>
	<logic:empty name="limUser">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该账号已被删除</div>
	</logic:empty>
  </body>
</html>
