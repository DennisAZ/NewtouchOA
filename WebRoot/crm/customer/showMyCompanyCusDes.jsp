<%@ page language="java" pageEncoding="UTF-8"%>
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
    <title>显示客户详情</title>
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
		function edit(){
			cusPopDiv(11,'${cusCorCusInfo.corCode}');
		}
		
		function initPage(){
			if("${cusCorCusInfo}"!=""){
				loadXpTabSel();
				if("${cusCorCusInfo.corStateRecord}"!=""){
					//修改记录收缩层
					if(shortDiv("appDesc",150)){
						$("appMoreButton").show();
					}
				}
			}
			removeTime("onDate","${cusCorCusInfo.corOnDate}",1);
			removeTime("recDate","${cusCorCusInfo.corRecDate}",1);
			$("cusState").innerHTML = getCusTxtValue("t_state","${cusCorCusInfo.corState}");
			$("cNameLayer").className = getCusColorClass("${cusCorCusInfo.corColor}");
			$("cusLev").innerHTML = getCusLev("${cusCorCusInfo.corHot}");
			$("cusCardType").innerHTML = getCusTxtValue("t_cardType","${cusCorCusInfo.corCardType}");
			if("${cusCorCusInfo.corRecvAmt}"!=""&&"${cusCorCusInfo.corRecvMax}"!=""&&parseFloat("${cusCorCusInfo.corRecvAmt}")>parseFloat("${cusCorCusInfo.corRecvMax}")){ $("cusToPaid").className = "red"; }
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
  	<c:if test="${!empty cusCorCusInfo}">
    	<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">
            	<!--  
                <span class="descOp">[<a href="javascript:void(0);" onClick="edit();return false;" title="编辑">编辑</a>]</span>
                -->
                客户详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">客户名称：</th>
                            <th class="descTitleR" colspan="3">
                                <span id="cNameLayer">${cusCorCusInfo.corName}</span>
                                <c:if test="${cusCorCusInfo.corTempTag==1}">
                                    <img class="imgAlign" src="crm/images/content/markRed.gif" alt="已标记"/>
                                </c:if>  
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                        	<th>助记简称：</th>
                            <td>${cusCorCusInfo.corMne}&nbsp;</td>
                        	<th>客户编号：</th>
                            <td>${cusCorCusInfo.corNum}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    </thead>
                	<tbody>
                	    <tr>
                        	<th>客户状态：</th>
                            <td><span id="cusState">&nbsp;</span></td>
                            <th>负责人：</th>
                            <td>${cusCorCusInfo.person.userName}&nbsp;&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>客户类型：</th>
                            <td>${cusCorCusInfo.cusType.typName}&nbsp;</td>
                            <th>客户级别：</th>
                            <td><span id="cusLev"></span>&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>关系等级：</th>
                            <td>${cusCorCusInfo.corRelation}&nbsp;</td>
                            <th>发货期初余额：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corBeginBal}">￥<fmt:formatNumber value="${cusCorCusInfo.corBeginBal}" pattern="#,##0.00"/></c:if>&nbsp;&nbsp;
                        </tr>
                        <tr>
                        	<th>应收发货余额：</th>
                            <td id="cusToPaid"><c:if test="${!empty cusCorCusInfo.corRecvAmt}">￥<fmt:formatNumber value="${cusCorCusInfo.corRecvAmt}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        	<th>开票期初余额：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corTicketBal}">￥<fmt:formatNumber value="${cusCorCusInfo.corTicketBal}" pattern="#,##0.00"/></c:if>&nbsp;&nbsp;
                        </tr>
                        <tr>
                        	<th>应收开票余额：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corTicketNum}">￥<fmt:formatNumber value="${cusCorCusInfo.corTicketNum}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                       		<th>最高余额：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corRecvMax}">￥<fmt:formatNumber value="${cusCorCusInfo.corRecvMax}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>最低月销售额：</th>
							<td>￥<fmt:formatNumber value="${cusCorCusInfo.corSaleNum}" pattern="#,##0.00"/>&nbsp;&nbsp;
                            <th>账龄（天）：</th>
                            <td>${cusCorCusInfo.corAging}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>到期日期：</th>
                            <td><span id="onDate"></span>&nbsp;</td>
                            <th>收款日期：</th>
                            <td><span id="recDate"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>证件类型：</th>
                            <td><span id="cusCardType"></span>&nbsp;</td>
                            <th>证件号码：</th>
                            <td>${cusCorCusInfo.corCardNum}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>客户简介：</th>
                            <td colspan="3">${cusCorCusInfo.corComInf}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>联系方式</div></td>
                        </tr>
                    </thead>
                	<tbody>
                    	<tr>
                            <th class="orangeBack" style="border:0">最近联系时间：</th>
                            <td colspan="3"><c:if test="${empty cusCorCusInfo.corLastDate}">未联系</c:if><c:if test="${!empty cusCorCusInfo.corLastDate}"><label id="lastConTime">${cusCorCusInfo.corLastDate}</label></c:if></td>
                        </tr>
                        <tr>
                            <th>所在地区：</th>
                            <td>
                            <c:if test="${cusCorCusInfo.cusArea.areId!=1}">${cusCorCusInfo.cusArea.areName}</c:if>
                            <c:if test="${cusCorCusInfo.cusProvince.prvId!=1}">${cusCorCusInfo.cusProvince.prvName}&nbsp;</c:if>
                            <c:if test="${cusCorCusInfo.cusCity.cityId!=1}">${cusCorCusInfo.cusCity.cityName}</c:if>&nbsp;
                            </td>
                            <th>地址：</th>
                            <td>${cusCorCusInfo.corAddress}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>电话：</th>
                            <td>${cusCorCusInfo.corPhone}&nbsp;</td>
                            <th>手机：</th>
                            <td>${cusCorCusInfo.corCellPhone}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>传真：</th>
                            <td>${cusCorCusInfo.corFex}&nbsp;</td>
                            <th>邮编：</th>
                            <td>${cusCorCusInfo.corZipCode}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>网址：</th>
                            <td colspan="3"><c:if test="${!empty cusCorCusInfo.corNet}"><img src="crm/images/content/url.gif" title="点击打开网站"/><a href="//<c:out value="${cusCorCusInfo.corNet}"/>" target="_blank">${cusCorCusInfo.corNet}</a></c:if>&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>电子邮件：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corEmail}"><img src="crm/images/content/email.gif"  title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('<c:out value="${cusCorCusInfo.corEmail}"/>');return false;">${cusCorCusInfo.corEmail}</a></c:if>&nbsp;</td>
                            <th>QQ：</th>
                            <td><c:if test="${!empty cusCorCusInfo.corQq}"><img src="crm/images/content/qq.gif"  title="点击开始qq对话"/><a target=_parent href="javascript:void(0)" onClick="qqTo('<c:out value="${cusCorCusInfo.corQq}"/>');return false;">${cusCorCusInfo.corQq}</a></c:if>&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>辅助信息</div></td>
                        </tr>
                    </thead>
					<tbody>
						<tr>
                        	<th>行业：</th>
                            <td>${cusCorCusInfo.cusIndustry.typName}&nbsp;</td>
						    <th>来源：</th>
                            <td>${cusCorCusInfo.corSou.typName}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>人员规模：</th>
                            <td>${cusCorCusInfo.corPerSize}&nbsp;</td>
                            <th>常用发货方式：</th>
                        	<td>${cusCorCusInfo.corSendType}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>税号：</th>
                        	<td>${cusCorCusInfo.corTaxNum}&nbsp;</td>
                        	<th>开户行及账号：</th>
                        	<td>${cusCorCusInfo.corBankCode}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>备注：</th>
                            <td colspan="3">${cusCorCusInfo.corRemark}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                         	<th>
                            	<c:if test="${!empty cusCorCusInfo.attachments}">
                                    <img id="${cusCorCusInfo.corCode}y" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusCorCusInfo.corCode}','allCus','reload')">                            
                                 </c:if>
                                 <c:if test="${empty cusCorCusInfo.attachments}">
                                    <img id="${cusCorCusInfo.corCode}n" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${cusCorCusInfo.corCode}','allCus','reload')">                               
                                 </c:if>
                                 附件：                     	 
                            </th>
                            <td colspan="3" style="border-bottom:0px">
                            <c:forEach var="attList" items="${cusCorCusInfo.attachments}" varStatus="idx">
                                <span id="fileDLLi${idx.index}"></span>
                    			<script type="text/javascript">createFileToDL("${idx.index}","${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                            </c:forEach>
                        	</td>
                       </tr>
                    </tbody>
                    <thead>
                    	<tr>
                    		<td colspan="4"><div>分配情况</div></td>
                    	</tr>
                    </thead>
                    <tbody>
                    	<tr>
							<th>最后分配日期：</th>
							<td colspan="3"><span id="assignDate"></span>&nbsp;</td>
						</tr>
                    	<tr class="noBorderBot">
                            <th>分配情况：</th>
                            <td colspan="3" style="font-weight:normal;">
                            <c:if test="${!empty cusCorCusInfo.corAssignCont}">
							<div id="allCont" style="display:none;">
                            	${cusCorCusInfo.corAssignCont}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someCont','allCont')">-&nbsp;点击收起</a>
							</div>
							<div id="someCont">
                                <div id="assignCont">${cusCorCusInfo.corAssignCont}</div>
                                <a id="contMoreButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allCont','someCont')">+&nbsp;点击展开</a>
                            </div>
                            </c:if>&nbsp;
							</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                    		<td colspan="4"><div>修改记录</div></td>
                    	</tr>
                    </thead>
                    <tbody>
                    	<tr class="noBorderBot">
                            <th>修改记录：</th>
                            <td colspan="3" style="font-weight:normal;">
                            <c:if test="${!empty cusCorCusInfo.corStateRecord}">
							<div id="allAppDesc" style="display:none;">
                            	${cusCorCusInfo.corStateRecord}
								<a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someAppDesc','allAppDesc')">-&nbsp;点击收起</a>
							</div>
							<div id="someAppDesc">
                                <div id="appDesc">${cusCorCusInfo.corStateRecord}</div>
                                <a id="appMoreButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allAppDesc','someAppDesc')">+&nbsp;点击展开</a>
                            </div>
                            </c:if>
                            <c:if test="${empty cusCorCusInfo.corStateRecord}">&nbsp;</c:if>
							</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>关联数据</div></td>
                        </tr>
                    </thead>
                 </table>
                 <input type="hidden" id="corCode" value="${cusCorCusInfo.corCode}" />
                 <input type="hidden" id="cusName" value="${cusCorCusInfo.corName}" />
                <div class="xpTab"> 
                  <span id="xpTab1" class="xpTabGray" onClick="swapTab(1,10,'customAction.do?op=toListCusCon&cusId=${cusCorCusInfo.corCode}')">&nbsp;联系人&nbsp;</span>
                  <span id="xpTab2" class="xpTabGray" onClick="swapTab(2,10,'customAction.do?op=toListCusAddr&cusId=${cusCorCusInfo.corCode}')">&nbsp;收货地址&nbsp;</span>
                  <span id="xpTab3" class="xpTabGray" onClick="swapTab(3,10,'messageAction.do?op=toListCusSch&cusId=${cusCorCusInfo.corCode}')">&nbsp;日程安排&nbsp;</span>
                  <span id="xpTab4" class="xpTabGray" onClick="swapTab(4,10,'cusServAction.do?op=toListCusPra&cusId=${cusCorCusInfo.corCode}')">&nbsp;来往记录&nbsp;</span>
                  <!--<span id="xpTab4" class="xpTabGray" onClick="swapTab(4,10,'cusServAction.do?op=toListCusOpp&cusId=${cusCorCusInfo.corCode}')">&nbsp;销售机会&nbsp;</span>-->
                  <span id="xpTab5" class="xpTabGray" onClick="swapTab(5,10,'customAction.do?op=toListCusOrder&showInfo=comCusOrder&cusId=${cusCorCusInfo.corCode}&cusName=${cusCorCusInfo.corName}')">&nbsp;订单合同&nbsp;</span>
                  <!--<span id="xpTab6" class="xpTabGray" onClick="swapTab(6,10,'paidAction.do?op=toListCusPaidPast&cusId=${cusCorCusInfo.corCode}')">&nbsp;回款记录&nbsp;</span>-->
                  <span id="xpTab7" class="xpTabGray" onClick="swapTab(7,10,'customAction.do?op=toListCusProd&cusId=${cusCorCusInfo.corCode}')">&nbsp;产品策略&nbsp;</span>
                  <!--<span id="xpTab8" class="xpTabGray" onClick="swapTab(8,10,'orderAction.do?op=toListCusPro&cusId=${cusCorCusInfo.corCode}')">&nbsp;产品历史&nbsp;</span>-->
                  <span id="xpTab9" class="xpTabGray" onClick="swapTab(9,10,'customAction.do?op=toListCusSalInvoice&cusId=${cusCorCusInfo.corCode}')">&nbsp;开票记录&nbsp;</span>
                  <span id="xpTab10" class="xpTabGray" onClick="swapTab(10,10,'orderAction.do?op=toListProdShipmentByCusId&cusId=${cusCorCusInfo.corCode}')">&nbsp;发货记录&nbsp;</span>
                  <!--<span id="xpTab9" class="xpTabGray" onClick="swapTab(8,8,'customAction.do?op=toCusSalStat&cusId=${cusCorCusInfo.corCode}')">&nbsp;销售分析&nbsp;</span>-->
                </div>
                <div class="HackBox"></div>
				<div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                </div> 
                <div class="descStamp">
                    由
                    <span>${cusCorCusInfo.corInsUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<c:if test="${!empty cusCorCusInfo.corUpdUser}">，最近由
                    <span>${cusCorCusInfo.corUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </c:if>
                </div>  
                <script type="text/javascript">
					removeTime("lastConTime","${cusCorCusInfo.corLastDate}",1);
					removeTime("assignDate","${cusCorCusInfo.corAssignDate}",1);
					removeTime("inpDate","${cusCorCusInfo.corCreatDate}",2);
					removeTime("changeDate","${cusCorCusInfo.corUpdDate}",2);
				</script>
            </div>
        </div>
        </c:if>
        <c:if test="${empty cusCorCusInfo}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该客户已被删除</div>
		</c:if>
	</div>
  </body>
  
</html>
