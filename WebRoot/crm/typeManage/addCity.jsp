<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/type.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<title>添加城市</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<script type="text/javascript"> 
	   function setValue1(obj){
	   		document.getElementById("proId").value=obj.value;
	   }
	   
		      
        function check(){
			var errStr = "";
			if(isEmpty("ordTypeName")){
				errStr+="- 未输入城市名称！\n";
			}
			else if(checkLength("ordTypeName",50)){
				errStr+="- 城市名称不能超过50个字！\n";
			}
			if($("proId").value==""){
				errStr+="- 未选择国家和省份！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","保存中...");
				waitSubmit("doCancel");
                $("register").submit();
            }				  
    	}
    </script>
</head>

<body>
    <div class="inputDiv">
        <form id="register" name="register" method="post" action="customAction.do">
            <input type="hidden" name="op" value="addCity" />
            <logic:notEmpty name="province">
            	<input type="hidden" name="id" id="proId"  value="${province.prvId}" />
            </logic:notEmpty>
            <logic:empty name="province">
            	<input type="hidden" name="id" id="proId" value="" />
            </logic:empty>
            <table class="normal dashTab noBr" style="width:98%" cellspacing="0" cellpadding="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>国家或地区：</th>
                        <td>
                        <logic:notEmpty name="province">
                            <span class="textOverflow" style="width:155px" title="<c:out value="${province.cusArea.areName}"/>">${province.cusArea.areName}&nbsp;</span>
                        </logic:notEmpty>
                        <logic:empty name="province">
                            <select class="inputBoxAlign" id="country"  name="countryId" onChange="getCityInfo('cou')">
                            <logic:notEmpty name="areaList">
                               <logic:iterate id="cusAreaList" name="areaList">
                               <option value="${cusAreaList.areId}">${cusAreaList.areName}</option>
                               </logic:iterate>
                            </logic:notEmpty>
                            </select>	 
                        </logic:empty>
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>省份名称：</th>
                        <td id='te'>
                        <logic:notEmpty name="province">
                            <span class="textOverflow" style="width:155px" title="<c:out value="${province.prvName}"/>">${province.prvName}&nbsp;</span>
                        </logic:notEmpty>
                        <logic:empty name="province">
                            <select class="inputBoxAlign" id="pro" name="provinceId" onChange="setValue1(this)">
                            </select>
                        </logic:empty>
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>城市名称：</th>
                        <td><input type="text" name="cusCity.cityName" id="ordTypeName" onBlur="autoShort(this,50)"/></td>
                    </tr>
                    <tr>
                        <th>是否启用：</th>
                        <td>
                            <input type="radio" name="cusCity.cityIsenabled" id="isE1" value="1" checked/><label for="isE1">是&nbsp;</label>
                            <input type="radio" name="cusCity.cityIsenabled" id="isE2" value="0"/><label for="isE2">否</label></td>
                    </tr>
                    <tr>
                        <td style="text-align:center;border:0px" colspan="2">
                        <input type="button" class="butSize1" id="Submit" value="保存" onClick="check()"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" type="button" class="butSize1"  value="取消" onClick="cancel()"></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </div>
	</body>
</html>
