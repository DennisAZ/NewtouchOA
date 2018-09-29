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
    <title>来往记录详情</title>
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
			$("praType").innerHTML = getCusTxtValue("t_praType","${salPra.praType}");
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
    <c:if test="${!empty salPra}">
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
                                    <c:if test="${!empty salPra.cusCorCus}">
                                        <a href="customAction.do?op=showCompanyCusDesc&corCode=${salPra.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${salPra.cusCorCus.corName}</a>
                                    </c:if>&nbsp;
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th>联系日期：</th>
                            <td><span id="exe"></span>&nbsp;</td>
                            <th>联系类型：</th>
                            <td><span id="praType"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>执行人：</th>
                            <td>${salPra.person.userName}&nbsp;</td>
                            <th>费用：</th>
                            <td><c:if test="${!empty salPra.praCost}">￥<fmt:formatNumber value='${salPra.praCost}' pattern='#,##0.00'/></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>联系人：</th>
                            <td colspan="3">${salPra.cusContact.conName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>联系内容：</th>
                            <td colspan="3">${salPra.praRemark}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                <c:if test="${!empty salPra.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salPra.praId}','cusPra','reload')"/>
                                 </c:if>
                                 <c:if test="${empty salPra.attachments}">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salPra.praId}','cusPra','reload')"/>
                                 </c:if>
                                附件：
                            </th>
                            <td colspan="3">
                                <c:forEach var="attList" items="${salPra.attachments}" varStatus="idx">
                                    <span id="fileDLLi${idx.index}"></span>
                                    <script type="text/javascript">createFileToDL("${idx.index}","${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                </c:forEach>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="descStamp">
                    由
                    <span>${salPra.praInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<c:if test="${!empty salPra.praUpdUser}">，最近由
                    <span>${salPra.praUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </c:if>
               </div>
               <script type="text/javascript">
					removeTime("exe","${salPra.praExeDate}",1);
					removeTime("inpDate","${salPra.praInsDate}",2);
					removeTime("changeDate","${salPra.praUpdDate}",2);
				</script>
            </div>
         </div>
      </c:if>
	 <c:if test="${empty salPra}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该来往记录已被删除</div>
	</c:if>
     </div>
  </body>
</html>
