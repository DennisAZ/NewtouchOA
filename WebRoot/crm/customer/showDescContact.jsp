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
    <title>联系人详细资料</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
		function edit(){
			cusPopDiv(21,['${contact.conId}']);
		}
		
		createProgressBar();
		window.onload=function(){
			if("${contact}"!=""){ loadXpTabSel(); }
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
       <c:if test="${!empty contact}">
		<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">
            	<span class="descOp">[<a href="javascript:void(0);" onClick="edit();return false;" title="编辑">编辑</a>]</span>
                联系人详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">姓名：</th>
                            <th class="descTitleR" colspan="3">
                                ${contact.conName}&nbsp;<c:if test="${contact.conIsCons == 1}"><span class="blue">&nbsp;&nbsp;[收货人]</span></c:if>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th class="blue">对应客户：</th>
                            <td class="blue mlink">
                            <c:if test="${!empty contact.cusCorCus}">
                                <a href="customAction.do?op=showCompanyCusDesc&corCode=${contact.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${contact.cusCorCus.corName}</a>
                            </c:if>&nbsp;</td>
                            <th>负责人：</th>
                            <td>${contact.person.userName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>性别：</th>
                            <td>${contact.conSex}&nbsp;</td>
                            <th>分类：</th>
                            <td>${contact.conLev}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>公司/部门：</th>
                            <td>${contact.conDep}&nbsp;</td>
                            <th>职务：</th>
                            <td>${contact.conPos}&nbsp;</td>
                        </tr>
                    </tbody>
        			<thead>
                    	<tr>
                            <td colspan="4"><div>联系方式</div></td>
                        </tr>
                    </thead>
                   	<tbody>
                   	    <tr>
                            <th>办公电话：</th>
                            <td>${contact.conWorkPho}&nbsp;</td>
                            <th>传真：</th>
                            <td>${contact.conFex}&nbsp;</td>
                        </tr>
                    	<tr>
                            <th>手机：</th>
                            <td>${contact.conPhone}&nbsp;</td>
                            <th>电子邮件：</th>
                            <td><c:if test="${!empty contact.conEmail}"><img src="crm/images/content/email.gif"  title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo(<c:out value="${contact.conEmail}"/>');return false;">${contact.conEmail}</a></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>QQ：</th>
                            <td><c:if test="${!empty contact.conQq}"><img src="crm/images/content/qq.gif"  title="点击开始qq对话"/><a target=_parent href="javascript:void(0)" onClick="qqTo('<c:out value="${contact.conQq}"/>');return false;">${contact.conQq}</a></c:if>&nbsp;</td>
                            <th>MSN：</th>
                            <td><c:if test="${!empty contact.conMsn}"><img src="crm/images/content/msn.gif"  title="点击开始msn对话"/><a href="javascript:void(0)" onClick="msnTo('<c:out value="${contact.conMsn}"/>');return false;">${contact.conMsn}</a></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>邮编：</th>
                            <td>${contact.conZipCode}&nbsp;</td>
                            <th>家庭电话：</th>
                            <td>${contact.conHomePho}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>地址：</th>
                            <td colspan="3">${contact.conAdd}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>其他联系方式：</th>
                            <td colspan="3">${contact.conOthLink}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>其他信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>爱好：</th>
                            <td>${contact.conHob}&nbsp;</td>
                            <th>忌讳：</th>
                            <td>${contact.conTaboo}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>教育背景：</th>
                            <td colspan="3">${contact.conEdu}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${contact.conRemark}&nbsp;</td>
                        </tr>	
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>关联数据</div></td>
                        </tr>
                    </thead>
                </table>
                <div class="xpTab"> 
                  <span id="xpTab1" class="xpTabGray" onClick="swapTab(1,2,'customAction.do?op=toListConMemDate&conId=${contact.conId}')">&nbsp;纪念日&nbsp;</span>
                  <!-- <span id="xpTab2" class="xpTabGray" onClick="swapTab(2,2,'cusServAction.do?op=toListContactPra&conId=${contact.conId}')">&nbsp;来往记录&nbsp;</span> -->
                </div>
                <div class="HackBox"></div>
				<div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                </div> 
                <div class="descStamp">
                    由
                    <span>${contact.conInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<c:if test="${!empty contact.conUpdUser}">，最近由
                    <span>${contact.conUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </c:if>
                </div>
                <script type="text/javascript">
				removeTime("inpDate","${contact.conCreDate}",2);
                removeTime("changeDate","${contact.conModDate}",2);
              </script>
            </div>
        </div>
       </c:if>
	    <c:if test="${empty contact}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该联系人已被删除</div>
		</c:if>
	</div>
  </body>
</html>
