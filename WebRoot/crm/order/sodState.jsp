<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>修改交付信息</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="cache-control" content="no-cache"/>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
		.resConfirm {
			padding:8px 6px 6px 6px; 
		}
		.resConfirm form{
			margin:0px;
		}
		.formitem {
			text-align:left;
			font-weight:bold;
			color:#666;
			padding-left:5px;
		}
		.formInp {
			text-align:left;
			padding:5x 0px 10px 5px;
		}
		.formSubmit {
			border-top:#f3ae4c 1px solid;
			padding:5px;
			background-color:#ff9;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript">
		function initForm(){
			if($("stateId")!=null){
				$("stateId").value = "${order.sodShipState.typId}";
			}
			dateInit("deadLine","${order.sodDeadline}");
		}
		window.onload=function(){
			initForm();
		}
	</script>
  </head>
  
  <body>
  	<div class="resConfirm"> 
      	<form action="orderAction.do" method="post">
          	<input type="hidden" name="op" value="altSodSta">
          	<input type="hidden" name="code" value="${order.sodCode}">
            <div class="formitem">订单状态：</div>
			<div class="formInp">
                            <logic:notEmpty name="stateList">
                            <select name="stateId" id="stateId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="stateList" name="stateList">
                                    <option value="${stateList.typId}">${stateList.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="stateList">
                                <select class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>			</div>
            <div class="formitem">交付时间：</div>
			<div class="formInp">
				<input id="deadLine" name="deadLine" type="text" class="inputSize2 Wdate" style="cursor:hand; width:150px"  readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
			</div>
            <div class="formSubmit">
                <button class ="butSize1" type="submit">确认</button>		 
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button class ="butSize1" onClick="cancel()">取消</button>
            </div>
	  	</form>
    </div> 
	</body>
</html>
