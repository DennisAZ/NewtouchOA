﻿<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>快速新建员工档案</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
  	 <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
     <script type="text/javascript" src="crm/js/prototype.js"></script>
	 <script type="text/javascript" src="crm/js/common.js"></script>
     <script type="text/javascript" src="crm/js/formCheck.js"></script>
     <script type="text/javascript" src="crm/js/hr.js"></script>
	<script type="text/javascript">
		function checkEmpNum(obj){
			if(obj != undefined){
				autoShort(obj,25);
				checkIsRepeat(obj,'empAction.do?op=checkSecode&seCode=','<c:out value="${salEmp.seCode}"/>');
			}
	  		else{
				autoShort($('seCode'),25);
				checkRepeatForm(new Array('empAction.do?op=checkSecode&seCode=','seCode'));
			}
	 	}
	</script>
  </head>
  
  <body>
	<div class="inputDiv">
    	<form action="empAction.do" id="creSalEmp" method="post">
			<input type="hidden" name="op" value="quickSaveSalEmp"/>
           	<input type="hidden" name="wIdPre" value="brow_"/>
			<input type="hidden" id="isRepeat" />
            <input type="hidden" name="salEmp.seRap" value="在职"/>
            <input type="hidden" name="salEmp.seMarry" value="未婚"/>
            <input type="hidden" name="salEmp.seIsovertime" value="否"/>
			<div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此员工号在系统(包括回收站)中已存在</div>
    		<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                	    <th style="vertical-align:top;"><span class='red'>*</span>姓名：</th>
                        <td><input class="inputSize2" style="width:80px" type="text" id="seName" name="salEmp.seName" onBlur="autoShort(this,25)"/></td>
                        <th><span class='red'>*</span>员工号：</th>
                        <td><input class="inputSize2" style="width:80px" type="text" id="seCode" name="salEmp.seCode" onBlur="checkEmpNum(this)"/></td>
                    </tr>
                     <tr>
                        <th><span class='red'>*</span>所属部门：</th>
                        <td>
                        <select name="soCode" id="soCode" style="width:80px">
                            <option value="">请选择</option>
                            <logic:notEmpty name="salOrg">	
                            <logic:iterate id="so" name="salOrg" scope="request">
                                <option value="${so.soCode}">${so.soName}</option>						
                            </logic:iterate>
                            </logic:notEmpty>
                        </select>
                        </td>
                      <th><span class='red'>*</span>职位：</th>
                      <td><select id="jobLev" name="seJobLev" style="width:80px">
                              <option value="">请选择</option>
                              <logic:notEmpty name="roleList">
                                 <logic:iterate id="roleList" name="roleList">
                                 <option value="${roleList.rolId}">${roleList.rolName}</option>
                                 </logic:iterate>
                              </logic:notEmpty>
                           </select>
                      </td>
                    </tr>
                    <tr>
                        <th style="vertical-align:top;">性别：</th>
                        <td colspan="3">
                        <input  type="radio" name="salEmp.seSex" id="sex1" value="男" checked><label for="sex1">男&nbsp;</label>
                        <input  type="radio" name="salEmp.seSex" id="sex2" value="女"><label for="sex2">女</label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border:0px; text-align:center">
                            <input type="button" class="butSize1" id="sub1" value="保存" onClick="checkEmpNum()">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="button" class="butSize1" id="cancel1" value="取消" onClick="cancel('brow_')">
                        </td>
                    </tr>	
                </tbody>
    		</table>
    	</form>
    </div>
  </body>
</html>
