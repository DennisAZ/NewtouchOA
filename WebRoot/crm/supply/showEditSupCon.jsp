<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>修改联系人</title>
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
    <script type="text/javascript" src="crm/js/sup.js"></script>

	<script type="text/javascript" >
	    function showSelect(){
			if("${supConInfo.scnSex}"=="男")
				$("sex1").checked="true";
			else if("${supConInfo.scnSex}"=="女")
				$("sex2").checked="true";
		}
    	function check(){
			var errStr = "";
			if(isEmpty("conName")){
				 errStr+="- 未填写联系人名称！\n";
			 }
			 else if(checkLength("conName",50)){
				errStr+="- 联系人名称不能超过50个字！\n";
			 }
			 if(isEmpty("supName")){   
				errStr+="- 未选择供应商！\n"; 
			 }
			 if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			 else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("edit").submit();
			 }
		}
		
		function chooseSup(){
			parent.addDivBrow(7);
		}
		window.onload=function(){
			if('${supConInfo}'!=null&&'${supConInfo}'!="")
			{
				showSelect();
			}
		}

  </script></head>
  
  <body>

  <div class="inputDiv">
    <logic:notEmpty name="supConInfo">
  	<form id="edit" action="salSupplyAction.do" method="post">
  	<input type="hidden" name="op" value="changeSupConInfo" />
	<input type="hidden" name="conId" value="${supConInfo.scnId}"/>
	<logic:equal value="2" name="isEdit" >              
	 <input type="hidden" name="isIfrm" value="1"/>
    </logic:equal>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">姓名：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="conName" class="inputSize2L" type="text" name="supContact.scnName" value="${supConInfo.scnName}" onBlur="autoShort(this,50)"/></th>
                </tr>
            </thead>
            <tbody>
            	<tr>  
                    <th class="required">对应供应商：<span class='red'>*</span></th>
                    <td>
                        <input id="supName" class="inputSize2S lockBack" type="text" value="${supConInfo.salSupplier.ssuName}" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2" onClick="chooseSup()">选择</button>
                        <input type="hidden" name="supId" id="supId" value="${supConInfo.salSupplier.ssuId}" />
                    </td>
                    <th>性别：</th>
                    <td>
                      <input type="radio" name="supContact.scnSex" id="sex1" value="男" checked/><label for="sex1">男&nbsp;&nbsp;</label>
                      <input type="radio" name="supContact.scnSex" id="sex2" value="女"/><label for="sex2">女&nbsp;</label>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea rows="3" class="inputSize2L" name="supContact.scnRemark" onBlur="autoShort(this,4000)">${supConInfo.scnRemark}</textarea></td>
                </tr>
            </tbody>
			<thead>
            	<tr>
                    <td colspan="4"><div>联系方式</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>手机：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnPhone" value="${supConInfo.scnPhone}" onBlur="autoShort(this,25)"/></td>
                    <th>电子邮件：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnEmail" value="${supConInfo.scnEmail}" onBlur="autoShort(this,50)"/></td>
                </tr>
                <tr>
                    <th>QQ：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnQq" value="${supConInfo.scnQq}" onBlur="autoShort(this,25)"/></td>
                    <th>MSN：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnMsn" value="${supConInfo.scnMsn}" onBlur="autoShort(this,50)"/></td>
                </tr>
                <tr>
                    <th>邮编：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnZipCode" value="${supConInfo.scnZipCode}" onBlur="autoShort(this,25)"/></td>
                    <th>家庭电话：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnHomePho" value="${supConInfo.scnHomePho}" onBlur="autoShort(this,25)"/></td>
                </tr>
                <tr>
                    <th>联系地址：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="supContact.scnAdd" value="${supConInfo.scnAdd}" onBlur="autoShort(this,1000)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>其他联系方式：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="supContact.scnOthLink" value="${supConInfo.scnOthLink}" onBlur="autoShort(this,1000)"/></td>
                </tr>
            </tbody>
           	<thead>
            	<tr>
                    <td colspan="4"><div>单位信息</div></td>
                </tr>
            </thead>
           	<tbody>
            	<tr>
                    <th>部门：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnDep" value="${supConInfo.scnDep}" onBlur="autoShort(this,1000)"/></td>
                    <th>职务：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnService" value="${supConInfo.scnService}" onBlur="autoShort(this,100)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>办公电话：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnWorkPho" value="${supConInfo.scnWorkPho}" onBlur="autoShort(this,25)"/></td>
                    <th>传真：</th>
                    <td><input class="inputSize2" type="text" name="supContact.scnFex" value="${supConInfo.scnFex}" onBlur="autoShort(this,25)"/></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                </tr>
            </tbody>
        </table>
	</form>
	</logic:notEmpty>
	 <logic:empty name="supConInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该联系人已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>
