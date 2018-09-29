<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>新建联系人</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
     <script type="text/javascript" src="core/js/sys.js"></script>
    <script type="text/Javascript" src="core/js/cmp/select.js" ></script>
    <script type="text/Javascript" src="core/js/orgselect.js" ></script>
	<!--<script type="text/javascript" src="js/chooseBrow.js"></script>-->

	<script type="text/javascript" >
    function check(){
		var errStr = "";
		if(isEmpty("conName")){
			errStr+="- 未填写联系人姓名！\n";
		 }
		 else if(checkLength("conName",50)){
			errStr+="- 联系人姓名不能超过50个字！\n";
		 }
		 /*if(isEmpty("cusId")){   
			errStr+="- 未选择客户！\n"; 
		 }*/
		 if(isEmpty("seNo")){
			errStr+="- 未选择负责人！\n"; 
		 }
		 if(errStr!=""){
			errStr+="\n请返回修改...";
			alert(errStr);
			return false;
		}
		else{
			waitSubmit("save");
			waitSubmit("doCancel");
			return $("create").submit();
		}
	}
		
	/*function chooseCus(){
		parent.addDivBrow(1);
	}*/
	
	function initForm(){
		createCusCb("t_conShip","conIsConsTd","cusContact.conIsCons");
		createCusCb("t_sex","conSexTd","cusContact.conSex");
		createCusSel("levSel","t_conType");
	}
	
	window.onload=function(){
		initForm();
	}
  </script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<form id="create" action="customAction.do" method="post">
  		<input type="hidden" name="op" value="saveContactInfo" />
        <input type="hidden" name="cusId" id="cusId" value="${cusCorCusInfo.corCode}" />
        <c:if test="${!empty cusCorCusInfo}"><input type="hidden" name="isIfrm" value="1"/></c:if>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">姓名：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="conName" class="inputSize2L" type="text" name="cusContact.conName" onBlur="autoShort(this,50)"/></th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <!--<th class="required">对应客户：<span class='red'>*</span></th>
                    <td>
                    	<input id="cusName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" ondblClick="clearInput(this,'cusId')" value="<c:out value="${cusCorCusInfo.corName}"/>" readonly/>&nbsp;
                        <button class="butSize2" onClick="chooseCus()">选择</button>
                    </td>-->
                    
                     <th>负责人：</th>
                        <TD>
        <input type="hidden" name="seNo" id="seNo"  value="${LOGIN_USER.seqId}"> 	
        <input type="text" name="managerDesc" id="managerDesc" size="13" class="BigStatic" readonly value="<c:out value="${LOGIN_USER.userName}"/>" >&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['seNo', 'managerDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('seNo').value='';$('managerDesc').value='';">清空</a>
      </TD> 
                    <th>收货人：</th>
                    <td id="conIsConsTd">&nbsp;</td>
                </tr>
                <tr>
                    <th>性别：</th>
                    <td id="conSexTd">&nbsp;</td>
                    <th>分类：</th>
                    <td><select id="levSel" name="cusContact.conLev" class="inputSize2"><option value=""></option></select></td>
            	</tr>
            	<tr class="noBorderBot">
                    <th>公司/部门：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conDep" onBlur="autoShort(this,1000)"/></td>
                    <th>职务：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conPos" onBlur="autoShort(this,100)"/></td>
                </tr>
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>联系方式</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>办公电话：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conWorkPho" onBlur="autoShort(this,25)"/></td>
                    <th>传真：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conFex" onBlur="autoShort(this,25)"/></td>
                </tr>
            	<tr>
                    <th>手机：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conPhone" onBlur="autoShort(this,25)"/></td>
                    <th>电子邮件：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conEmail" onBlur="autoShort(this,50)"/></td>
                </tr>
                <tr>
                    <th>QQ：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conQq" onBlur="autoShort(this,25)"/></td>
                    <th>MSN：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conMsn" onBlur="autoShort(this,50)"/></td>
                </tr>
                <tr>
                    <th>邮编：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conZipCode" onBlur="autoShort(this,25)"/></td>
                    <th>家庭电话：</th>
                    <td><input class="inputSize2" type="text" name="cusContact.conHomePho" onBlur="autoShort(this,25)"/></td>
                </tr>
                <tr>
                    <th>地址：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusContact.conAdd"  onblur="autoShort(this,1000)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>其他联系方式：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusContact.conOthLink" onBlur="autoShort(this,1000)"/></td>
                </tr>	
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>其他信息</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>爱好：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusContact.conHob" onBlur="autoShort(this,100)"/></td>
                </tr>
                <tr>    
                    <th>忌讳：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusContact.conTaboo" onBlur="autoShort(this,100)"/></td>
                </tr>
                <tr>
                    <th>教育背景：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusContact.conEdu" onBlur="autoShort(this,100)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="4" name="cusContact.conRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>	
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="save" class="butSize1" type="button" value="保存" onClick="check()" />
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
