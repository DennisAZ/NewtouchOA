<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>供应商联系人详细资料</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
		
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
       <logic:notEmpty name="supConInfo">
		<div id="contentbox">
        	<div id="title">采购管理 > 供应商联系人 > 联系人详细信息</div>
            <div class="descInf">
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">姓名：</th>
                            <th class="descTitleR" colspan="3">
                                ${supConInfo.scnName}&nbsp;
                            </th>
                        </tr>
                    </thead>
                	<tbody>
                    	<tr>
                            <th class="blue">对应供应商：</th>
                            <td class="blue mlink">
                            <logic:notEmpty name="supConInfo" property="salSupplier">
                                <a href="salSupplyAction.do?op=showSupplyDesc&supId=${supConInfo.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${supConInfo.salSupplier.ssuName}</a>
                            </logic:notEmpty>&nbsp;</td>
                            <th>性别：</th>
                            <td>${supConInfo.scnSex}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${supConInfo.scnRemark}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>联系方式</div></td>
                        </tr>
                    </thead>
        			<tbody>
                    	<tr>
                            <th>手机：</th>
                            <td>${supConInfo.scnPhone}&nbsp;</td>
                            <th>电子邮件：</th>
                            <td><logic:notEmpty name="supConInfo" property="scnEmail"><img src="crm/images/content/email.gif" title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('${supConInfo.scnEmail}');return false;">${supConInfo.scnEmail}</a></logic:notEmpty>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>QQ：</th>
                            <td><logic:notEmpty name="supConInfo" property="scnQq"><img src="crm/images/content/qq.gif"  title="点击开始qq对话"/><a target=_parent href="javascript:void(0)" onClick="qqTo('${supConInfo.scnQq}');return false;">${supConInfo.scnQq}</a></logic:notEmpty>&nbsp;</td>
                            <th>MSN：</th>
                            <td><logic:notEmpty name="supConInfo" property="scnMsn"><img src="crm/images/content/msn.gif"  title="点击开始msn对话"/><a href="javascript:void(0)" onClick="msnTo('${supConInfo.scnMsn}');return false;">${supConInfo.scnMsn}</a></logic:notEmpty>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>邮编：</th>
                            <td>${supConInfo.scnZipCode}&nbsp;</td>
                            <th>家庭电话：</th>
                            <td>${supConInfo.scnHomePho}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>联系地址：</th>
                            <td colspan="3">${supConInfo.scnAdd}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>其他联系方式：</th>
                            <td colspan="3">${supConInfo.scnOthLink}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>单位信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>部门：</th>
                            <td>${supConInfo.scnDep}&nbsp;</td>
                            <th>职务：</th>
                            <td>${supConInfo.scnService}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>办公电话：</th>
                            <td>${supConInfo.scnWorkPho}&nbsp;</td>
                            <th>传真：</th>
                            <td>${supConInfo.scnFex}&nbsp;</td>
                        </tr>
                    </tbody>
                </table>
                <div class="descStamp">
                    由
                    <span>${supConInfo.scnInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="supConInfo" property="scnUpdUser">，最近由
                    <span>${supConInfo.scnUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>
                <script type="text/javascript">
				removeTime("inpDate","${supConInfo.scnCreDate}",2);
                removeTime("changeDate","${supConInfo.scnModDate}",2);
              </script>
            </div>
        </div>
       </logic:notEmpty>
	    <logic:empty name="supConInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该联系人已被删除</div>
		</logic:empty>
	</div>
  </body>
</html>

