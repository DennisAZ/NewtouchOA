<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
	int count1=0;
	int count2=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示供应商详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript" src="crm/js/sup.js"></script>
	<script type="text/javascript">     
		
		createProgressBar();
		window.onload=function(){
			if('${supplyInfo}'!=null&&'${supplyInfo}'!=''){
				//表格内容省略
				loadXpTabSel(3);
			}
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  <div id="mainbox">
         <logic:notEmpty name="supplyInfo">
    	<div id="contentbox">
        	<div id="title">采购管理 > 供应商资料 > 供应商详细信息</div>
            <div class="descInf">
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">供应商名称：</th>
                            <th class="descTitleR" colspan="3">${supplyInfo.ssuName}&nbsp;</th>
                        </tr>
                    </thead>
                	<tbody>
                        <tr>
                            <th>供应商编号：</th>
                            <td>${supplyInfo.ssuCode}&nbsp;</td>
                            <th>供应商类别：</th>
                            <td>${supplyInfo.typeList.typName}&nbsp;</td>
                        </tr>
                       	<tr>
                            <th>供应产品：</th>
                            <td colspan="3">${supplyInfo.ssuPrd}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${supplyInfo.ssuRemark}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                        <tr>
                            <td colspan="4"><div>联系方式</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>所在地区：</th>
                            <td colspan="3">
                            <logic:equal value="1" name="supplyInfo" property="country.areId">&nbsp;</logic:equal>
                            <logic:notEqual value="1" name="supplyInfo" property="country.areId">${supplyInfo.country.areName}&nbsp;</logic:notEqual>
                            <logic:equal value="1" name="supplyInfo" property="province.prvId">&nbsp;</logic:equal>
                            <logic:notEqual value="1" name="supplyInfo" property="province.prvId">${supplyInfo.province.prvName}&nbsp;</logic:notEqual>
                            <logic:equal value="1" name="supplyInfo" property="city.cityId">&nbsp;</logic:equal>
                            <logic:notEqual value="1" name="supplyInfo" property="city.cityId">${supplyInfo.city.cityName}</logic:notEqual>
                            </td>
                        </tr>
                        <tr>
                            <th>电话：</th>
                            <td>${supplyInfo.ssuPhone}&nbsp;</td>
                            <th>传真：</th>
                            <td>${supplyInfo.ssuFex}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>邮编：</th>
                            <td>${supplyInfo.ssuZipCode}&nbsp;</td>
                            <th>邮箱：</th>
                            <td><logic:notEmpty name="supplyInfo" property="ssuEmail"><img src="crm/images/content/email.gif"  title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('${supplyInfo.ssuEmail}');return false;">${supplyInfo.ssuEmail}</a></logic:notEmpty>&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>地址：</th>
                            <td>${supplyInfo.ssuAdd}&nbsp;</td>
                            <th>网址：</th>
                            <td><logic:notEmpty name="supplyInfo" property="ssuNet"><img src="crm/images/content/url.gif" title="点击打开网站"/><a href="//${supplyInfo.ssuNet}" target="_blank">${supplyInfo.ssuNet}</a></logic:notEmpty>&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                        <tr>
                            <td colspan="4"><div>付款信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                        	<th>开户银行：</th>
                            <td>${supplyInfo.ssuBank}&nbsp;</td>
                            <th>开户账号：</th>
                            <td>${supplyInfo.ssuBankCode}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>开户名称：</th>
                            <td colspan="3">${supplyInfo.ssuBankName}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                        <tr>
                            <td colspan="4"><div>关联数据</div></td>
                        </tr>
                    </thead>
                 </table>
               
                <div class="xpTab">
                  <span id="xpTab1" class="xpTabSelected" onClick="swapTab(1,3,'salSupplyAction.do?op=getSupCon&id=${supplyInfo.ssuId}&name=${supplyInfo.ssuName}')">
                    &nbsp;联系人&nbsp;
                  </span>
                  <span id="xpTab2" class="xpTabGray" onClick="swapTab(2,3,'salSupplyAction.do?op=getSupSpo&id=${supplyInfo.ssuId}')">
                    &nbsp;采购单&nbsp;
                  </span>
                  <span id="xpTab3" class="xpTabGray" onClick="swapTab(3,3,'salSupplyAction.do?op=getSupInq&id=${supplyInfo.ssuId}&name=${supplyInfo.ssuName}')">
                    &nbsp;询价记录&nbsp;
                  </span>
                </div>
                <div class="HackBox"></div>
                 <div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                 </div>
                <div class="descStamp">
                    由
                    <span>${supplyInfo.ssuInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="supplyInfo" property="ssuAltUser">，最近由
                    <span>${supplyInfo.ssuAltUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>  
                <script type="text/javascript">
					removeTime("inpDate","${supplyInfo.ssuCreDate}",2);
					removeTime("changeDate","${supplyInfo.ssuAltDate}",2);
				</script>
            </div>
        </div>
  	     </logic:notEmpty>
		 <logic:empty name="supplyInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该供应商已被删除</div>
		</logic:empty>
	</div>
  </body>
  
</html>
