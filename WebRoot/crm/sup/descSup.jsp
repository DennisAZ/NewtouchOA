<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>供应商详细资料</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/sup/sup.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
		createProgressBar();
		window.onload = function(){
			loadXpTabSel();
		}
  	</script>
  	</head>
  <body>
    <div id="descContainer">
    <c:if test="${!empty supplier}">
        <div class="descInf">
            <table class="dashTab descForm2" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                    	<th class="descTitleL">供应商名称：</th>
                        <th class="descTitleR" colspan="7">${supplier.supName}&nbsp;</th>
                    </tr>
                </thead>
            	<tbody>
                	<tr>
                     	<th>供应商编号：</th>
                        <td colspan="3">${supplier.supCode}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>类别：</th>
                        <td>${supplier.supType.typName}&nbsp;</td>
                    	<th>联系人：</th>
                        <td>${supplier.supContactMan}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>电话：</th>
                        <td>${supplier.supPhone}&nbsp;</td>
                        <th>手机：</th>
                        <td>${supplier.supMob}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>传真：</th>
                        <td>${supplier.supFex}&nbsp;</td>
                        <th>电邮：</th>
                        <td>${supplier.supEmail}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>地址：</th>
                        <td>${supplier.supAdd}&nbsp;</td>
                        <th>邮编：</th>
                        <td>${supplier.supZipCode}&nbsp;</td>   
                    </tr>
                    <tr>
                     	<th>网址：</th>
                        <td>${supplier.supNet}&nbsp;</td>
                        <th>QQ：</th>
                        <td>${supplier.supQq}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>供应产品：</th>
                        <td colspan="3">${supplier.supProd}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>开户行：</th>
                        <td colspan="3">${supplier.supBank}&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>开户名称：</th>
                        <td>${supplier.supBankName}&nbsp;</td>
                    	<th>开户账号：</th>
                        <td>${supplier.supBankCode}&nbsp;</td>
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3">${supplier.supRemark}&nbsp;</td>
                    </tr>
                </tbody>
                <thead>
            	    <tr>
                        <td colspan="4"><div>关联数据</div></td>
                    </tr>
                </thead>
			</table>
			<input type="hidden" id="supId" value="${supplier.supId}" />
			<input type="hidden" id="supName" value="${supplier.supName}" />
		    <div class="xpTab"> 
                 <span id="xpTab1" class="xpTabGray" onClick="swapTab(1,3,'supAction.do?op=toListSupProd&supId=${supplier.supId}')">&nbsp;产品策略&nbsp;</span>
                <!--  <span id="xpTab2" class="xpTabGray" onClick="swapTab(2,3,'supAction.do?op=toListSupPaidPast&supId=${supplier.supId}')">&nbsp;付款记录&nbsp;</span> -->
                 <span id="xpTab3" class="xpTabGray" onClick="swapTab(3,3,'supAction.do?op=toListSupInvoice&supId=${supplier.supId}')">&nbsp;收票记录&nbsp;</span>
               </div>
               <div class="HackBox"></div>
			<div id="ifrContent" class="tabContent" style="display:none">
                   <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
           </div>
            <div class="descStamp">
                由
                <span>${supplier.supCreMan}</span>
                于
                <span id="creTime"></span>&nbsp;
                录入<c:if test="${!empty supplier.supUpdMan}">，最近由
                <span>${supplier.supUpdMan}</span>
                于
                <span id="updTime"></span>&nbsp;
                修改
                </c:if>
            </div>
            <script type="text/javascript">
                removeTime("creTime","${supplier.supCreTime}",2);
                removeTime("updTime","${supplier.supUpdTime}",2);
            </script>
        </div>
    </c:if>
	<c:if test="${empty supplier}">
		<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该供应商不存在</div>
	</c:if>
	</div>
  </body>
  <script type="text/javascript">
	closeProgressBar();
  </script>
</html>


