<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>入库单详细资料</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
   	<link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/sup/sup.js"></script>
	<script type="text/javascript">
		createProgressBar();
		
		function initPage(){
			removeTime("pinDate","${prodIn.pinDate}",1);
			if('${prodIn.pinState}'=='0'){
				$("pinState").innerHTML="已撤销";
			}
			else{
				$("pinState").innerHTML="已入库";
			}
		}
		
		window.onload=function(){
			initPage();
			closeProgressBar();
		}
  	</script>
  	</head>
  <body>
    <div id="descContainer">
    <c:if test="${!empty prodIn}">
        <div class="descInf">
            <table class="dashTab descForm2" cellpadding="0" cellspacing="0">
            	<thead>
                </thead>
            	<tbody>
                	<tr>
                    	<th>入库单号：</th>
                        <td>${prodIn.pinCode}&nbsp;</td>
                    	<th>采购日期：</th>
                        <td><span id="pinDate"></span>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>对应库存：</th>
                        <td>
                        	<c:if  test="${! empty prodIn.prodStore}">
                        		${prodIn.prodStore.pstName}
                        	</c:if>
                        	&nbsp;
                        </td>
                        <th>经手人：</th>
                        <td>${prodIn.pinRespMan}&nbsp;</td>  
                    </tr>
                    <tr>
                    	<th>入库数量：</th>
                        <td>${prodIn.pinInNum}&nbsp;</td>
                        <th>入库状态：</th>
                        <td><span id="pinState"></span>&nbsp;</td>   
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3">${prodIn.pinRemark}&nbsp;</td>
                    </tr>
                </tbody>
			</table>
            <div class="descStamp">
                由
                <span>${prodIn.pinCreMan}</span>
                于
                <span id="creTime"></span>&nbsp;
                录入<c:if test="${!empty prodIn.pinUpdMan}">，最近由
                <span>${prodIn.pinUpdMan}</span>
                于
                <span id="updTime"></span>&nbsp;
                修改
                </c:if>
            </div>
            <script type="text/javascript">
                removeTime("creTime","${prodIn.pinCreTime}",2);
                removeTime("updTime","${prodIn.pinUpdTime}",2);
            </script>
        </div>
    </c:if>
	<c:if test="${empty prodIn}">
		<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该入库单不存在</div>
	</c:if>
	</div>
  </body>
  <script type="text/javascript">
	closeProgressBar();
  </script>
</html>


