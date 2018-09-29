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
    <title>供应商信息编辑</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
	   	function initForm(){
	   		if($("typeId")!=null){
				$("typeId").value="${supplyInfo.typeList.typId}";
			}
		   	var countryId='${supplyInfo.country.areId}';
		   	$("country").value=countryId;
		   	getCityInfo('cou','${supplyInfo.province.prvId}','${supplyInfo.city.cityId}');
		}
	   function check(){
	   	var errStr = "";
		 if(isEmpty("ssuName")){
			 errStr+="- 未填写供应商名称！\n";
		 }
		 else if(checkLength("ssuName",100)){
		 	errStr+="- 供应商名称不能超过100个字！\n";
		 }
		 if($("isRepeat").value==1){
		 	errStr+="- 该供应商名称已存在！\n";
		 }
		 if(errStr!=""){
		 	errStr+="\n请返回修改...";
		 	alert(errStr);
			return false;
		 }
		 else{
		 	waitSubmit("supSave","保存中...");
		 	waitSubmit("doCancel");
			return $("create").submit();
		 }
		}
		//判断名称重复
		function checkName(obj){
			if(obj != undefined){
				autoShort(obj,100);
				checkIsRepeat(obj,"salSupplyAction.do?op=checkIsRepeatName&supName=","${supplyInfo.ssuName}");
			}
			else{
				autoShort($('ssuName'),100);
				checkRepeatForm(new Array('salSupplyAction.do?op=checkIsRepeatName&supName=','ssuName','${supplyInfo.ssuName}'));
			}
		}
		
	window.onload=function(){
		 initForm();
	}
  </script></head>
  
  <body>

  <div class="inputDiv">
  <logic:notEmpty name="supplyInfo">
  	<form id="create" action="salSupplyAction.do" method="post">
        <input type="hidden" name="op" value="changeSupplyInfo" />
		<input type="hidden" name="supId" value="${supplyInfo.ssuId }" />
        <input type="hidden" id="isRepeat" value=""/>
        <div id="errDiv" class="errorDiv redWarn" style="display:none">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;&nbsp;此供应商名称在系统(包括回收站)中已存在</div>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                  	<th class="descTitleL">供应商名称<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input type="text" id="ssuName" class="inputSize2L" name="salSupplier.ssuName" value="${supplyInfo.ssuName}" onBlur="checkName(this)"/>   
                	</th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                	<th>供应商类别：</th>
                    <td colspan="3">
                    	<logic:notEmpty name="typeList">
                        	<select id="typeId" name="typeId" class="inputSize2 inputBoxAlign">
                            	<option value=""></option>
                               <logic:iterate id="typeList" name="typeList">
                               <option value="${typeList.typId}">${typeList.typName}</option>
                               </logic:iterate>
                        	</select>
                        </logic:notEmpty>
                        <logic:empty name="typeList">
                            <select class="inputSize2 inputBoxAlign" disabled="disabled">
                                <option>未添加供应商类别</option>
                            </select>
                        </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
            	<tr>
                    <th>供应产品：</th>
                    <td colspan="3">
                    <textarea id="corName" class="inputSize2L" name="salSupplier.ssuPrd" rows="3" onBlur="autoShort(this,4000)">${supplyInfo.ssuPrd}</textarea>
                    </td>
                </tr>
                <tr class="noBorderBot">
                	<th>备注：</th>
                    <td colspan="3"><textarea name="salSupplier.ssuRemark" class="inputSize2L" rows="3"  onblur="autoShort(this,4000)">${supplyInfo.ssuRemark}</textarea></td>
                </tr>
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>联系方式</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>所在地区：</th>
                    <td colspan="3" class="longTd">
                    	国家&nbsp;
                        <select class="inputBoxAlign inputSize4" id="country"  name="countryId" onChange="getCityInfo('cou')">
                            <logic:notEmpty name="cusAreaList">
                               <logic:iterate id="cusAreaList" name="cusAreaList">
                               <option value="${cusAreaList.areId}">${cusAreaList.areName}</option>
                               </logic:iterate>
                            </logic:notEmpty>
                        </select>	 
                        &nbsp;省份&nbsp;
                        <select class="inputBoxAlign inputSize4" id="pro" name="provinceId" onChange="getCityInfo('province')">
                        </select>	
                        &nbsp;城市&nbsp;
                        <select class="inputBoxAlign inputSize4" id="city" name="cityId">
                        </select>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
                <tr>
                    <th>电话：</th>
                    <td>
                      <input class="inputSize2" type="text"  name="salSupplier.ssuPhone" value="${supplyInfo.ssuPhone}"  onblur="autoShort(this,25)"/>
                    </td>
                    <th>传真：</th>
                    <td><input class="inputSize2" type="text" name="salSupplier.ssuFex" value="${supplyInfo.ssuFex}" onBlur="autoShort(this,25)"/></td>
                </tr>
                <tr>
                    <th>邮编：</th>
                    <td><input class="inputSize2" type="text" name="salSupplier.ssuZipCode" value="${supplyInfo.ssuZipCode}" onBlur="autoShort(this,25)"/></td>
                    <th>邮箱：</th>
                    <td><input class="inputSize2" type="text" name="salSupplier.ssuEmail" value="${supplyInfo.ssuEmail}" onBlur="autoShort(this,25)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>地址：</th>
                    <td><input class="inputSize2" type="text" name="salSupplier.ssuAdd" value="${supplyInfo.ssuAdd}" onBlur="autoShort(this,1000)"/></td>
                    <th>网址：</th>
                    <td>http://<input class="inputSize2" type="text" name="salSupplier.ssuNet" value="${supplyInfo.ssuNet}"  style="width:119px;" onBlur="autoShort(this,100)"/></td>
                </tr>
            </tbody>
            <thead>
            	<tr>
                    <td colspan="4"><div>付款信息</div></td>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>开户银行：</th>
                    <td>
                      <input class="inputSize2" type="text" name="salSupplier.ssuBank" value="${supplyInfo.ssuBank}"  onblur="autoShort(this,50)"/>
                    </td>
                    <th>开户账号：</th>
                    <td>
                      <input class="inputSize2" type="text"  name="salSupplier.ssuBankCode" value="${supplyInfo.ssuBankCode}" onBlur="autoShort(this,25)"/>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>开户名称：</th>
                    <td colspan="3">
                      <input class="inputSize2" type="text" name="salSupplier.ssuBankName" value="${supplyInfo.ssuBankName}" onBlur="autoShort(this,50)"/>
                    </td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="supSave" class="butSize1" type="button" value="保存" onClick="checkName()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                </tr>
            </tbody>
        </table>
	</form>
	 </logic:notEmpty>
	 <logic:empty name="supplyInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该供应商已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>
