<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>待执行的来往记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
		#tipsTab td{
			padding-left:2px;
			padding-right:2px;
		}
		#todayTips ,#lastTips {
			height:221px;
		}
		#todayTips ul,#lastTips ul {
			padding:0px;
			margin:0px;
		}
		#todayTips li,#lastTips li {
			padding:2px;
			border-bottom:#999999 1px dotted;
		}
		#todayTips span,#lastTips span{
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
  </head>
  
  <body>
  	<table id="tipsTab" class="normal" cellpadding="0" cellspacing="0" style="width:98%">
    	<tr>
    		<td style="width:50%">
          		<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendarT.gif" alt="来往记录提醒" class="imgAlign" />&nbsp;<span class="bigger">今天</span>&nbsp;有&nbsp;<span class="bigger">${num}</span>&nbsp;个来往记录待执行
                    </div>
                    <div id="todayTips" class="divWithScroll2">
                        <logic:notEmpty name="curPraList">			
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="salPras" name="curPraList" indexId="curPraI">
                                    <li>
                                    	<img src="crm/images/content/blueBlock.gif"/>&nbsp;
                                    	<logic:notEmpty name="salPras" property="praType">
                                        	<span class="brown">[${salPras.praType}]</span>
                                        </logic:notEmpty><a href="cusServAction.do?op=showSalPraInfo&id=${salPras.praId}" target="_blank">${salPras.praRemark}</a>
                                        <span class="mLink"><nobr>
                                            <logic:notEmpty name="salPras" property="cusCorCus">
                                            	<a title="对应客户" href="customAction.do?op=showCompanyCusDesc&corCode=${salPras.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${salPras.cusCorCus.corName}</a>
                                            </logic:notEmpty>
                                        </nobr></span>
                                        <span id="exePraLayer${curPraI}" style="display:none">
                                            <span id="${salPras.praId}"><nobr>&nbsp;<logic:equal value="${limUser.salEmp.seNo}" name="salPras" property="salEmp.seNo"><img class="imgAlign" onClick="parent.cusPopDiv(41,['${salPras.praId}','1'])" style="cursor:pointer;" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;</logic:equal><img class="imgAlign"  src="crm/images/content/execute.gif" style="cursor:pointer;" onClick="modifyPraState('${salPras.praId}','${salPras.praExeDate}','${salPras.cusCorCus.corCode}')" border="0" alt="立即执行"/></nobr></span>
                                            <span id="y${salPras.praId}" style="display:none"><img class="imgAlign" src="crm/images/content/suc.gif" alt="已执行"/></span>
                            			</span>
                                        <script type="text/javascript">displayLimAllow("r015","exePraLayer${curPraI}");</script>
                                    </li>
                                    
                                  </logic:iterate>
                            </ul>
                        </logic:notEmpty>  
                        <logic:empty name="curPraList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                今天没有待执行的来往记录
                            </div>
                        </logic:empty>
                    </div>
                </div>
            </td>
            
            <td style="width:50%">
            	<div class="tipBox">
                    <div class="tipTitle">
                        &nbsp;<img src="crm/images/content/calendar7.gif" alt="来往记录提醒" class="imgAlign" />&nbsp;<span class="bigger">七天内</span>&nbsp;有&nbsp;<span class="bigger">${num2}</span>&nbsp;个来往记录待执行
                    </div>
                    <div id="lastTips" class="divWithScroll2">
                        <logic:notEmpty name="lastPraList">		
                            <ul class="listtxt" style="word-break:break-all;">
                                <logic:iterate id="salPras2" name="lastPraList" indexId="lastPraI">
                                    <li>
                                    	<img src="crm/images/content/blueBlock.gif"/>&nbsp;
                                    	<logic:notEmpty name="salPras2" property="praType">
                                        	<span class="brown aAlign">[${salPras2.praType}]</span>
                                        </logic:notEmpty>
                                    	<a href="cusServAction.do?op=showSalPraInfo&id=${salPras2.praId}" target="_blank">${salPras2.praRemark}</a>
                                        <span class="mLink"><nobr>
                                            <logic:notEmpty name="salPras2" property="cusCorCus">
                                            	<a title="对应客户" href="customAction.do?op=showCompanyCusDesc&corCode=${salPras2.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${salPras2.cusCorCus.corName}</a>
                                            </logic:notEmpty>
                                        </nobr></span>
                                        <nobr>
                                        <span class="gray" title="计划执行日期">
                                        	<label id="jhexe${lastPraI}"></label>
                                        </span>
                                        <span id="modPraLayer${lastPraI}" style="display:none">[<a href="javascript:void(0)" onClick="parent.cusPopDiv(42,['${salPras2.praId}','${salPras2.praExeDate}']);return false;">修改</a>]&nbsp;</span>
                                        <script type="text/javascript">displayLimAllow("r015","modPraLayer${lastPraI}");</script>  
                                        </nobr>
                                    </li>
                                    <script type="text/javascript">
										dateFormat('jhexe','${salPras2.praExeDate}','${lastPraI}');
									</script>
                                </logic:iterate>
                            </ul>
                        </logic:notEmpty>
                        <logic:empty name="lastPraList">
                            <div align="center" class="gray" style="padding-top:30px">	
                                七天内没有待执行的来往记录
                            </div>
                        </logic:empty>
                    </div>
               </div>
            </td>
        </tr>
   	</table>
  </body>
</html>
