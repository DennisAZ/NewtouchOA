<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>   
    <title>选择产品</title><!--(订单)-->
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="StyleSheet" href="css/dtree.css" type="text/css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
			overflow-y:hidden;
		}
		#searchPro {
			 margin:8px; 
			 padding:0px;
		}
		a {
			zoom:1;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="js/choosePro.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
	function check(){
		var obj=$("pName");
		if(obj==null||obj.value==""){
			alert("请输入查询条件");
			return false;
		}else{
			return $("searchPro").submit();
		}
	}
	</script>
  </head>
 
  <body>
  	<div style="padding:5px;">
        <div align="left">
            <form id="searchPro" action="prodAction.do" method="post">
                <input type="hidden" name="op" value="proSearch">
                <input type="hidden" name="ord" value="ord">
                <input type="hidden" name="agr" value="1">
                <input type="hidden" name="cusId" value="${cusId}" />
                查询产品<span class="gray">&nbsp;(可按产品名称/编号查询)</span>
                <input type="text" class="inputSize2 inputBoxAlign" style="width:70%" id="pName" name="pName" value="${wprName}"/>
                <input type="button" id="Submit" value="查询" class="butSize1 inputBoxAlign" onClick="check()">
            </form>
            <logic:notEmpty name="wmsProduct">
                <table class="normal" cellpadding="0" cellspacing="0" border="0" width="95%">
                    <tr>
                        <td style="border-top:#CCCCCC 1px dashed; line-height:8px">&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="orange">
                            <img id="hideImg" class="imgAlign" src="images/content/hide.gif" onClick="showHide('searchResult','hideImg')" style="cursor:pointer;" alt="点击收起"/>&nbsp;查询结果：
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <div id="searchResult" style="display:block;">
                            <ul class="listtxt2">
                            <logic:iterate id="pro" name="wmsProduct" scope="request">
                                <li>
                                &nbsp;
                                <a href="javascript:void(0);" onClick="tbladdrow(2,'ordDesc','${pro.wprId}',new Array('${pro.wprName}','${pro.wprCode}'),'${pro.wprModel}','${pro.typeList.typName}','${pro.wprSalePrc}');return false;"><img src="images/content/ball.gif" style="vertical-align:middle; border:0px"/>${pro.wprName}<logic:notEmpty name="pro" property="typeList">[${pro.typeList.typName}]</logic:notEmpty></a><logic:equal value="1" name="pro" property="wprIscount"><img src="images/content/database.gif" alt="计算库存"></logic:equal >
                                </li>
                            </logic:iterate>
                            </ul>
                        </div>
                        </td>
                    </tr>
                </table>
            </logic:notEmpty>
            </div>
        <hr style="border:#fba01f 1px solid"/>
  		<fieldset>
  			<legend class="orange">&nbsp;产品列表&nbsp;</legend>
			<div align="left">
			    <ul class="listtxt1">
                      <logic:iterate id="type" name="wmsProType" scope="request">
                          <li>
                          <a href="javascript:void(0);" onClick="tbladdrow(2,'ordDesc','${type.wprId}',new Array('${type.wprName}','${type.wprCode}'),'${type.wprModel}','${type.typeList.typName}','${type.wprSalePrc}');return false;"><img src="images/content/ball.gif" style="vertical-align:middle; border:0px"/>${type.wprName}<logic:notEmpty name="type" property="typeList">[${type.typeList.typName}]</logic:notEmpty></a><logic:equal value="1" name="type" property="wprIscount"><img src="images/content/database.gif" alt="计算库存"></logic:equal >
                          </li>
                      </logic:iterate>
                </ul>
			</div>
		</fieldset>
		<logic:empty name="wmsProType">
            <logic:empty name="wmsProType1">
                <div class="dataList title blue bold" style="padding-top:30px">${msg}</div>
            </logic:empty>
        </logic:empty>
	</div>
  </body>
</html>
