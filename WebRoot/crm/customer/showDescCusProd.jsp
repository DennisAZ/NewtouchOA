<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <title>详情</title>
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
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript">
		function initPage(){
			
			
		}
		createProgressBar();
		window.onload=function(){
			initPage();
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
    <c:if test="${!empty cusProd}">
    	<div id="contentbox">
        	<div class="descInf">
            	<div id="descTop">来往记录详细信息</div>
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    </thead>
                	<tbody>
                    	<tr>
                            <th class="blue">对应客户：</th>
                            <td class="blue mlink" colspan="3">
                                <span id="cusName">
                                    <c:if test="${!empty cusProd.cusCorCus}">
                                        <a href="customAction.do?op=showCompanyCusDesc&corCode=${cusProd.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${cusProd.cusCorCus.corName}</a>
                                    </c:if>&nbsp;
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th>对应产品：</th>
                            <td>${cusProd.wmsProduct.wprName}&nbsp;</td>
                            <th>客户价格：</th>
                            <td>${cusProd.cpPrice}&nbsp;</td>
                        </tr>
    
                <tr>
                	 <th>预计月使用量：</th>
                	 <td>${cusProd.cpPreNumber}</td>
                	 <th>提前提醒天数: </th>
                	 <td>${cusProd.cpWarnDay}</td>
                </tr>
             
                        <tr>
                            <th>是否含税价：</th>
                   <td id="hasTaxTd">
                   <c:if test="${cusProd.cpHasTax==1}">
                   含税
                   </c:if>
                     <c:if test="${cusProd.cpHasTax==0}">
                   不含税
                   </c:if>
                   &nbsp;</td>
 
                            <th>备注：</th>
                            <td>${cusProd.cpRemark}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                <c:if test="${!empty cusProd.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusProd.cpId}','cusProd','reload')"/>
                                 </c:if>
                                 <c:if test="${empty cusProd.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusProd.cpId}','cusProd','reload')"/>
                                 </c:if>
                                附件：
                            </th>
                            <td colspan="3">
                                <c:forEach var="attList" items="${cusProd.attachments}" varStatus="idx">
                                    <span id="fileDLLi${idx.index}"></span>
                                    <script type="text/javascript">createFileToDL("${idx.index}","${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                </c:forEach>
                            </td>
                        </tr>
                    </tbody>
                </table>
              

            </div>
         </div>
      </c:if>
	 <c:if test="${empty cusProd}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该产品策略已被删除</div>
	</c:if>
     </div>
  </body>
</html>
