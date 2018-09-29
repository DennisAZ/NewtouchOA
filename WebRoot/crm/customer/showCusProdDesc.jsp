<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
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
    <title>客户产品详情</title>
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
	<script type="text/javascript">
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
    <logic:notEmpty name="cusProd">
    	<div id="contentbox">
        	<div id="title">客户管理 > 客户产品 > 客户产品详细信息</div>
              <div class="descInf">
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    </thead>
                	<tbody>
                    	<tr>
                            <th class="blue">对应客户：</th>
                            <td class="blue mlink" colspan="3">
                                <span id="cusName">
                                    <logic:notEmpty name="cusProd" property="cusCorCus">
                                        <a href="customAction.do?op=showCompanyCusDesc&corCode=${cusProd.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${cusProd.cusCorCus.corName}</a>
                                    </logic:notEmpty>&nbsp;
                                </span>
                            </td>
                        </tr>
                      	<tr>
		                    <th>对应产品：</th>
                            <td class="blue mlink" colspan="3">
                                <span id="wprName">
                                    <logic:notEmpty name="cusProd" property="cusCorCus">
                                        <a href="prodAction.do?op=wmsProDesc&wprId=${cusProd.wmsProduct.wprId}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看产品详情" class="imgAlign" style="border:0px;">${cusProd.wmsProduct.wprName}</a>
                                    </logic:notEmpty>&nbsp;
                                </span>
                            </td>
                		</tr>
                    </tbody>
                	<thead>
                    </thead>
                    <tbody>
                        <tr>
		                   <th>客户价格：</th>
		                   <td>￥<bean:write name="cusProd" property="cpPrice" format="###,##0.00"/></td>
		                </tr>
		                <tr class="noBorderBot">
		                    <th>备注：</th>
		                    <td colspan="3">${cusProd.cpRemark}&nbsp;</td>
		                </tr>
                    </tbody>
                </table>
                <div class="descStamp">
                    由
                    <span>${cusProd.cpCreUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="cusProd" property="cpUpdUser">，最近由
                    <span>${cusProd.cpUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
               </div>
               <script type="text/javascript">
					removeTime("inpDate","${cusProd.cpCreDate}",2);
					removeTime("changeDate","${cusProd.cpUpdDate}",2);
				</script>
            </div>
         </div>
      </logic:notEmpty>
	 <logic:empty name="cusProd">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该客户产品已被删除</div>
	</logic:empty>
     </div>
  </body>
</html>
