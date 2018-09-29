<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>销售机会详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript">
		createProgressBar();
		window.onload=function(){
			if('${salOpp}'!=null&&'${salOpp}'!=''){
				loadXpTabSel();
			}
			if($("staLog")!=null){
				//推进历史收缩层
				if(shortDiv("staLog",150)){
					$("moreInfButton").show();
				}
			}
			closeProgressBar();
		}
		function edit(){
			cusPopDiv(31,'${salOpp.oppId}','1');
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
    <logic:notEmpty name="salOpp">
		<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">
            	<span class="descOp">[<a href="javascript:void(0);" onClick="edit();return false;" title="编辑">编辑</a>]</span>
                销售机会详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
					<thead>
                    	<tr>
                            <th class="descTitleL">机会主题：</th>
                            <th class="descTitleR" colspan="3">
                                ${salOpp.oppTitle}&nbsp;
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr class="noBorderBot">
                          	<th class="blue">对应客户：</th>
                          	<td class="blue mlink">
                                <span id="cusName">
                                    <logic:notEmpty name="salOpp" property="cusCorCus">
                                        <a href="customAction.do?op=showCompanyCusDesc&corCode=${salOpp.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${salOpp.cusCorCus.corName}</a>
                                    </logic:notEmpty>&nbsp;
                                </span>
                            </td>
                            <th>负责人：</th>
                            <td>${salOpp.salEmp1.seName}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>基本信息</div></td>
                        </tr>
                    </thead>
                	<tbody>
                    	<tr>
                            <th>发现日期：</th>
                            <td><span id="findDate"></span>&nbsp;</td>
							<th>发现人：</th>
                   			<td>${salOpp.salEmp.seName}</td>
						</tr>
                        <tr>
                            <th>关注日期：</th>
                            <td><span id="exeDate"></span>&nbsp;</td>
                            <th>机会热度：</th>
                            <td>${salOpp.oppLev}&nbsp;</td>
                        </tr>    
                        <tr class="noBorderBot">
                            <th>机会描述：</th>
                            <td colspan="6">${salOpp.oppDes}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                        	<td colspan="4"><div>当前状态</div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>预计签单日期：</th>
                            <td><span id="signDate"></span>&nbsp;</td>
                            <th>预计金额：</th>
                            <td>￥<bean:write name="salOpp" property="oppMoney" format="###,##0.00"/>&nbsp;</td>
                        </tr>
                    	<tr>
                        	<th class="orangeBack">机会状态：</th>
                        	<td colspan="3">${salOpp.oppState}&nbsp;</td>
                        </tr>
                        <tr>
                            <th class="orangeBack">阶段：</th>
                            <td>
                                <logic:notEmpty name="salOpp" property="oppStage">
                                    ${salOpp.oppStage.typName}&nbsp;
                                </logic:notEmpty>
                                <logic:equal value="跟踪" name="salOpp" property="oppState">
                                <span class="gray" style="font-weight:500" title="阶段停留">已停留${salOpp.oppDayCount}天</span>
                                </logic:equal>
                                <logic:notEqual value="跟踪" name="salOpp" property="oppState">
                                &nbsp;
                                </logic:notEqual>&nbsp;
                            </td>
                            <th class="orangeBack">可能性：</th>
                            <td><logic:notEmpty name="salOpp" property="oppPossible">${salOpp.oppPossible}%</logic:notEmpty>&nbsp;</td>
                        </tr>
                        <tr>
                            <th class="orangeBack">阶段备注：</th>
                            <td colspan="3">${salOpp.oppStaRemark}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>推进历史：</th>
                            <td colspan="3" style="font-weight:normal;">
                            <logic:notEmpty name="salOpp" property="oppStaLog">
							<div id="allLog" style="display:none;">
                            	${salOpp.oppStaLog}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someLog','allLog')">-&nbsp;点击收起</a>
							</div>
							<div id="someLog">
                                <div id="staLog">${salOpp.oppStaLog}</div>
                                <a id="moreInfButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allLog','someLog')">+&nbsp;点击展开</a>
                            </div>
                            </logic:notEmpty>
                            <logic:empty name="salOpp" property="oppStaLog">&nbsp;</logic:empty>
							</td>
                        </tr>	
                    </tbody>
                </table>
                <input type="hidden" id="cusId" value="${salOpp.cusCorCus.corCode}" />
                <input type="hidden" id="corName" value="<c:out value="${salOpp.cusCorCus.corName}"/>" />
                <input type="hidden" id="oppId" value="${salOpp.oppId}" />
                <input type="hidden" id="oppTil" value="<c:out value="${salOpp.oppTitle}"/>" />
                <div class="descStamp">
                    由
                    <span>${salOpp.oppInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="salOpp" property="oppUpdUser">，最近由
                    <span>${salOpp.oppUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>
                <script type="text/javascript">
                    removeTime("findDate","${salOpp.oppFindDate}",1);
                    removeTime("exeDate","${salOpp.oppExeDate}",1);
                    removeTime("signDate","${salOpp.oppSignDate}",1);
                    removeTime("inpDate","${salOpp.oppInsDate}",2);
                    removeTime("changeDate","${salOpp.oppUpdDate}",2);
                </script>
            </div>
            
       </div>
     </logic:notEmpty>
	 <logic:empty name="salOpp">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该销售机会已被删除</div>
	</logic:empty>
	 </div>
  </body>
</html>
