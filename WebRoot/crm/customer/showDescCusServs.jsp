<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户服务详情</title>
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
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
    <logic:notEmpty name="cusServ">
    	<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">客户服务详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">主题：</th>
                            <th class="descTitleR" colspan="3">
                                ${cusServ.serTitle}&nbsp;
                            </th>
                        </tr>
                    </thead>
                	<tbody>
                    	<tr>
                            <th class="blue">对应客户：</th>
                            <td colspan="3"  class="blue mlink">
                                <logic:notEmpty name="cusServ" property="cusCorCus"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;"><a href="customAction.do?op=showCompanyCusDesc&corCode=${cusServ.cusCorCus.corCode}" target="_blank">${cusServ.cusCorCus.corName}</a>
                                </logic:notEmpty>&nbsp;
                            </td>
                        </tr>
                    </tbody>
                    <thead>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>状态：</th>
                            <td colspan="3">${cusServ.serState}&nbsp;</td>
                        </tr>
                        <tr>    
                            <th>客服方式：</th>
                            <td colspan="3">${cusServ.serMethod}&nbsp;</td>
                        </tr>
                        <tr  class="noBorderBot">
                            <th>客服内容：</th>
                            <td colspan="3">${cusServ.serContent }&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>反馈信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>处理结果：</th>
                            <td colspan="6">${cusServ.serRemark }&nbsp;</td>
                        </tr>
                       <tr>
                            <th>处理人：</th>
                            <td>${cusServ.salEmp.seName}&nbsp;</td>
                            <th>处理日期：</th>
                            <td><label id="exe"></label>&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                <logic:notEmpty name="cusServ" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusServ.serCode}','cusServ','reload')"/>
                                 </logic:notEmpty>
                                 <logic:empty name="cusServ" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusServ.serCode}','cusServ','reload')"/>
                                 </logic:empty>
                                 附件：
                            </th>
                            <td colspan="3">
                                <logic:iterate id="attList" name="cusServ" property="attachments">
                                    <span id="fileDLLi<%=count%>"></span>
                                    <script type="text/javascript">createFileToDL(<%=count%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                    <% count++;%>
                                </logic:iterate>
                           </td>
                        </tr>
                    </tbody>
                </table>
                <div class="descStamp">
                    由
                    <span>${cusServ.serInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="cusServ" property="serUpdUser">，最近由
                    <span class="bold">${cusServ.serUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>
                <script type="text/javascript">
					removeTime("exe","${cusServ.serExeDate}",1);
					removeTime("inpDate","${cusServ.serInsDate}",2);
					removeTime("changeDate","${cusServ.serUpdDate}",2);
                </script>
            </div>
        </div>
     </logic:notEmpty>
	 <logic:empty name="cusServ">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该客户服务已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>
