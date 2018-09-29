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
    <title>新建部门</title>
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
		   
		function check(){
			var errStr = "";
			if(isEmpty("soCode")){
				errStr+="- 未填写部门编号！\n";
			}
			else if(checkLength("soCode",25)){
				errStr+="- 部门编号不能超过25位！\n";
			}
			if(isEmpty("soName")){   
				errStr+="- 未填写部门名称！\n"; 
			}
			else if(checkLength("soName",50)){
				errStr+="- 部门名称不能超过50个字！\n";
			}
			/*if($("addNum").value!=""&&!isint($("addNum").value)){
				errStr+="- 部门编制数请填写数字！\n";
			}*/
			 
			if($("isRepeat1").value==1){
				errStr+="- 此部门编号已存在！\n";
			}
			if($("isRepeat2").value==1){
				errStr+="- 此部门名称已存在！\n";
			}
			 
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("addButton","保存中...");
				waitSubmit("doCancel");
			 	return $("create").submit();
			}
	 	} 
		
		
		//表单有重复值返回时载入
		function loadOrg(){
			$("upOrg").value='${newSalOrg.salOrg.soCode}';
			$("orgNature").value='<c:out value="${newSalOrg.soOrgNature}"/>'
		}
		
		//载入上级组织
		function loadUpOrg(){
			$("upOrg").value="${upOrg}";
		}
		
		//有重复值提醒
		if('${msg}'!=null&&'${msg}'!="")
	    	alert('${msg}');
		
		
		function checkOrgNum(obj){
			autoShort(obj,25);
			checkIsRepeat(obj,'empAction.do?op=checkOrgCode&orgCode=',null,1)
		}
		
		function checkOrgName(obj){
			autoShort(obj,50);
			checkIsRepeat(obj,'empAction.do?op=checkOrgName&orgName=',null,2)
		}
		
		function checkOrgForm(){
			autoShort($('soCode'),25);
			autoShort($('soName'),50);
			checkRepeatForm(new Array('empAction.do?op=checkOrgCode&orgCode=','soCode',null,1),new Array('empAction.do?op=checkOrgName&orgName=','soName',null,2));
		}
		
		window.onload=function(){
			if('${newSalOrg}'!=null)
				loadOrg();
			if("${upOrg}"!=null)
				loadUpOrg();
		}
  </script> 
</head>
  
  <body>
  	<div class="inputDiv">
  		<form action="empAction.do" method="post" name="create">
  			<input type="hidden" name="op" value="saveSalOrg">
            <input type="hidden" id="isRepeat1" />
            <input type="hidden" id="isRepeat2" />
            <div id="errDiv1" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此部门编号已存在</div>
            <div id="errDiv2" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此部门名称已存在</div>
			<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th style="width:80px;"><span class='red'>*</span>部门编号：</th>
                        <td><input name="salOrg.soOrgCode" id="soCode" type="text" value="<c:out value="${newSalOrg.soOrgCode}"/>" class="inputSize2" onBlur="checkOrgNum(this)"/>
                        </td>	
                        <th><span class='red'>*</span>部门名称：</th>
                        <td><input name="salOrg.soName" id="soName" type="text" value="<c:out value="${newSalOrg.soName}"/>" class="inputSize2" onBlur="checkOrgName(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <th>上级部门：</th>
                        <td>
                        <select id="upOrg" name="addSoCode" class="inputSize2">
                            <option value=""></option>
                            <logic:notEmpty name="orgList">
                            <logic:iterate id="sorg" name="orgList">
                                <option value="${sorg.soCode}">${sorg.soName}</option>
                            </logic:iterate>
                            </logic:notEmpty>
                        </select>
                        </td>
                        <th><nobr>机构性质：</nobr></th>
                        <td>
                        <select id="orgNature" name="salOrg.soOrgNature" class="inputSize2">
                            <option value="">请选择</option>
                            <option value="总部">总部</option>
                            <option value="分公司">分公司</option>
                            <option value="子公司">子公司</option>
                            <option value="部门">部门</option>
                            <option value="其他">其他</option>
                        </select>
                        </td>
                    </tr>
                    <tr>
                        <th><nobr>成本中心：</nobr></th>
                        <td colspan="3"><input style="width:432px" name="salOrg.soCostCenter"  value="<c:out value="${newSalOrg.soCostCenter}"/>" type="text" class="inputSize2" onBlur="autoShort(this,100)"/></td>
                    </tr>
                    <tr>
                        <th>负责人：</th>
                        <td>
                     	<input id="soUserName" name="salOrg.soUserCode" class="inputSize2" style="width:120px" type="text" onBlur="autoShort(this,25)"/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                        </td>
                        <th>编制数：</th>
                        <td>
                            <input type="text" id="addNum" name="salOrg.soEmpNum" value="<c:out value="${newSalOrg.soEmpNum}"/>" class="inputSize2" onBlur="autoShort(this,25)"/>
                        </td>
                    </tr>
                    <tr>
                        <th>办公地点：</th>
                        <td colspan="3"><input class="inputSize2" style="width:432px" name="salOrg.soLoc" value="<c:out value="${newSalOrg.soLoc}"/>" type="text" onBlur="autoShort(this,500)"/></td>
                    </tr>
                    <tr>
                        <th>掌管区域：</th>
                        <td colspan="3"><input style="width:432px" name="salOrg.soConArea" value="<c:out value="${newSalOrg.soConArea}"/>" type="text" class="inputSize2"  onblur="autoShort(this,100)"></td>
                    </tr>
                    
                    <tr>
                        <th style="vertical-align:top">部门职责：</th>
                        <td colspan="3">
                            <textarea name="salOrg.soResp" style="width:432px" rows="1" onBlur="autoShort(this,1000)">${newSalOrg.soResp}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td style=" border:0px; text-align:center" colspan="4">
                        <input type="button" class="butSize1" id="addButton" value="保存"  onClick="checkOrgForm()">
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
