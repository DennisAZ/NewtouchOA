<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    
    <title>修改仓库</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
    <script type="text/javascript">
	         
		function check(){
			var errStr = "";
			if(isEmpty("pname")){   
				errStr+="- 未填写仓库名称！\n"; 
			}
			else if(checkLength("pname",100)){
				errStr+="- 仓库名称不能超过100个字！\n";
			}
			if($("isRepeat").value==1){
				errStr+="- 此仓库名称已存在！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("register").submit();
			}				  
		}
		function showIndex(){
			if($("wt")!=null){
				$("wt").value='${wmsStro.wmsStroType.typId}';	
			}		
			
			$("uc").value='${wmsStro.limUser.userCode}';		    		
		}
		
		function checkWmsName(obj){
			if(obj != undefined){
				autoShort(obj,100);
				checkIsRepeat(obj,'wmsManageAction.do?op=checkWmsName&wmsName=','${wmsStro.wmsName}');
			}
			else{
				autoShort($('pname'),100);
				checkRepeatForm(new Array('wmsManageAction.do?op=checkWmsName&wmsName=','pname','${wmsStro.wmsName}'))
			}
		}
		
		window.onload=function(){
			if("${wmsStro}"!=null&&"${wmsStro}"!="")
				showIndex();
		}
  </script> 
  </head>
  
  <body>
  <logic:notEmpty name="wmsStro">
 	 <div class="inputDiv">
  		<form action="wmsManageAction.do" method="post" id="register">
        <input type="hidden" name="op" value="wmsStroUpdate">
  		<input type="hidden" name="wmsCode" value="${wmsStro.wmsCode}">
  		<input type="hidden" name="wmsCreDate" value="${wmsStro.wmsCreDate}">
		<input type="hidden" name="wmsStro.wmsIsenabled" value="${wmsStro.wmsIsenabled}">
        <input type="hidden" name="oldName" value="${wmsStro.wmsName}"/>
		<input type="hidden" id="isRepeat"/>
        <div id="errDiv" class="errorDiv redWarn" style="display:none; ">&nbsp;<img class="imgAlign" src="images/content/fail.gif" alt="警告"/>&nbsp;此仓库名称在系统(包括回收站)中已存在</div>
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">仓库名称：<span class='red'>*</span></th>
                    <th class="descTitleR"><input name="wmsStro.wmsName" id="pname" type="text" value="${wmsStro.wmsName}" class="inputSize2" onBlur="checkWmsName(this)">
                    </th>
                </tr>
            </thead>
			<tbody>
            	<tr>
                    <th>仓库类别：</th>
                    <td>
                        <logic:notEmpty name="wmsStroType">
                        <select name="wmsType" id="wt" class="inputSize2 inputBoxAlign">
                            <option value="">&nbsp;</option>
                            <logic:iterate id="ws" name="wmsStroType" scope="request">
                            <option value="${ws.typId}">${ws.typName}</option>
                            </logic:iterate>
                        </select>
                        </logic:notEmpty>
                        <logic:empty name="wmsStroType">
                             <select id="wt" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加仓库类别</option>
                            </select>
                         </logic:empty>
                        <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                   </td>				
                </tr>
                <tr>
                    <th>仓库地点：</th>
                    <td><input type="text" name="wmsStro.wmsLoc" value="${wmsStro.wmsLoc}" class="inputSize2" onBlur="autoShort(this,500)">&nbsp;</td>
                </tr>
                <tr>
                    <th>负责账号：</th>
                    <td>
                        <logic:notEmpty name="limUser">
                        <select name="schRuserCode" id="uc" class="inputSize2">
                            <logic:iterate id="lu" name="limUser" scope="request">
                            <option value="${lu.userCode}">${lu.userSeName }</option>
                            </logic:iterate>
                        </select>
                        </logic:notEmpty>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea rows="2" class="inputSize2" name="wmsStro.wmsRemark" onBlur="autoShort(this,4000)">${wmsStro.wmsRemark}</textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="Button" class="butSize1" id="dosave" value="修改" onClick="checkWmsName()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" onClick="cancel()" value="取消"></td>
                </tr>
            </tbody>	
	  </table>
	</form>
  </div>
  </logic:notEmpty>
    <logic:empty name="wmsStro">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该仓库已被关闭</div>
	</logic:empty>
  </body>
</html>
