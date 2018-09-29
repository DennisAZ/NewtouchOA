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
    <title>出库单详细资料</title>
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
			removeTime("pouDate","${prodOut.pouDate}",1);
			if('${prodOut.pouState}'=='0'){
				$("pouState").innerHTML="已撤销";
			}
			else{
				$("pouState").innerHTML="已出库";
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
    <c:if test="${!empty prodOut}">
        <div class="descInf">
            <table class="dashTab descForm2" cellpadding="0" cellspacing="0">
            	<thead>
                </thead>
            	<tbody>
                	<tr>
                    	<th>出库单号：</th>
                        <td>${prodOut.pouCode}&nbsp;</td>
                    	<th>出库日期：</th>
                        <td><span id="pouDate"></span>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>对应库存：</th>
                        <td>
                        	<c:if  test="${! empty prodOut.prodStore}">
                        		${prodOut.prodStore.pstName}
                        	</c:if>
                        	&nbsp;
                        </td>
                        <th>出库数量：</th>
                        <td>${prodOut.pouOutNum}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>领料人：</th>
                        <td>${prodOut.pouPickMan}&nbsp;</td>   
                        <th>经手人：</th>
                        <td>${prodOut.pouRespMan}&nbsp;</td>  
                    </tr>
                    <tr>
                        <th>出库状态：</th>
                        <td colspan="3"><span id="pouState"></span>&nbsp;</td>   
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3">${prodOut.pouRemark}&nbsp;</td>
                    </tr>
                </tbody>
			</table>
            <div class="descStamp">
                由
                <span>${prodOut.pouCreMan}</span>
                于
                <span id="creTime"></span>&nbsp;
                录入<c:if test="${!empty prodOut.pouUpdMan}">，最近由
                <span>${prodOut.pouUpdMan}</span>
                于
                <span id="updTime"></span>&nbsp;
                修改
                </c:if>
            </div>
            <script type="text/javascript">
                removeTime("creTime","${prodOut.pouCreTime}",2);
                removeTime("updTime","${prodOut.pouUpdTime}",2);
            </script>
        </div>
    </c:if>
	<c:if test="${empty prodOut}">
		<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该出库单不存在</div>
	</c:if>
	</div>
  </body>
  <script type="text/javascript">
	closeProgressBar();
  </script>
</html>


