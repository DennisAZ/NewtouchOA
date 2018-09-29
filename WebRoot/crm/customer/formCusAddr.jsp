<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存客户地址</title>
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
    	function check(){
			var errStr = "";
			if(isEmpty("addr")){   
				errStr+="- 未填写地址！\n"; 
			}
			if(isEmpty("country")||isEmpty("pro")||isEmpty("city")){
				errStr+="- 未选择地区！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave");
				waitSubmit("doCancel");
				return $("saveForm").submit();
			}
		}
		
		function setContact(obj){
			var conId = obj.value;
			$("cadCont").value = obj.options[obj.selectedIndex].text;
			$("postCode").value = $("conZip"+conId).value;
			$("addr").value = $("conAdr"+conId).value;
			$("cadPho").value = $("conPho"+conId).value;
		}
		
		//初始化表单
		function initForm(){
			<c:if test="${empty cusAddr}">
			//新建
			disableInput('cadId');
			$("op").value = "saveCusAddr";
			$("cusId").value = "${customer.corCode}";
			$("country").value='${customer.cusArea.areId}';
			getCityInfo('cou','${customer.cusProvince.prvId}','${customer.cusCity.cityId}');
			</c:if>
			
			<c:if test="${!empty cusAddr}">
			//编辑
			$("op").value = "updCusAddr";
			$("cadId").value = "${cusAddr.cadId}";
			$("cusId").value = "${cusAddr.cadCus.corCode}";
			$("country").value='${cusAddr.cadProv.areId}';
		   	getCityInfo('cou','${cusAddr.cadCity.prvId}','${cusAddr.cadDistrict.cityId}');
			</c:if>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="saveForm" action="customAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
            <input type="hidden" id="cadId" name="cusAddr.cadId"/>
            <input type="hidden" id="cusId" name="cusId"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
                    <tr>
                        <th class="required">地址：<span class='red'>*</span></th>
                        <td colspan="3">
                        	<input class="inputSize2L" type="text" id="addr" name="cusAddr.cadAddr" onBlur="autoShort(this,300)" value="${cusAddr.cadAddr}" >
                        </td>
                    </tr>
                    <tr>
                        <th class="required">地区：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <select class="inputBoxAlign" id="country"  name="provId" style="width:135px" onChange="getCityInfo('cou')">
                                <c:if test="${!empty cusAreaList}">
                                   <c:forEach var="cusAreaList" items="${cusAreaList}">
                                   <option value="${cusAreaList.areId}">${cusAreaList.areName}</option>
                                   </c:forEach>
                                </c:if>
                            </select>&nbsp;
                            <select class="inputBoxAlign" id="pro" name="cityId" style="width:135px" onChange="getCityInfo('province')">
                            </select>&nbsp;
                            <select class="inputBoxAlign" id="city" name="districtId" style="width:135px" >
                            </select>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                    	<th>收货人：</th>
                        <td colspan="3" class="longTd">
                        <input class="inputSize2 inputBoxAlign" type="text" id="cadCont" name="cusAddr.cadContact" onBlur="autoShort(this,50)" value="${cusAddr.cadContact}" >&nbsp;&nbsp;
                        <c:if test="${!empty conList}">
                        <select id="cusCon" class="inputSize2 inputBoxAlign" onChange="setContact(this)">
                            <option value="">选择收货人</option>
                            <c:forEach var="cusCon" items="${conList}">
                            <option value="${cusCon.conId}"><c:out value="${cusCon.conName}"/></option>
                            </c:forEach>
                        </select>
                        <c:forEach var="cusCon" items="${conList}">
                        <input type="hidden" id="conPho${cusCon.conId}" value="${cusCon.conPhone}"/>
                        <input type="hidden" id="conAdr${cusCon.conId}" value="${cusCon.conAdd}"/>
                        <input type="hidden" id="conZip${cusCon.conId}" value="${cusCon.conZipCode}"/>
                        </c:forEach>
                        </c:if>
                        
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                    	<th>邮编：</th>
                        <td><input class="inputSize2" type="text" id="postCode" name="cusAddr.cadPostCode" onBlur="autoShort(this,50)" value="${cusAddr.cadPostCode}" ></td>
                        <th>联系电话：</th>
                        <td><input class="inputSize2" id="cadPho" type="text" name="cusAddr.cadPho" onBlur="autoShort(this,50)" value="${cusAddr.cadPho}" ></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
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
