<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <title>报价记录详情</title>
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
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
	    function heji(n){
			var allPrice=0;
			for(var i=0;i<n;i++){
				allPrice+=parseFloat($F("allprice"+i));	
			}
			$("ap").innerHTML="￥"+changeTwoDecimal(allPrice);
		}
		//编辑明细样式
		function loadEditRop(){
			var obj=$("editRop");
			obj.style.cursor="pointer";
			//obj.className="blueBg";
				
			obj.onmouseover=function(){
				obj.className="grayBack blue";
			}
			
			obj.onmouseout=function() {
				obj.className="blackblue";
			}
			
			obj.onclick=function() {
				window.open("cusServAction.do?op=goRup&quoId=${quoteInfo.quoId}");
			}
		}
		createProgressBar();
		window.onload=function(){
			loadEditRop();
			//明细表格内容省略
			loadTabShort("tab");
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
	<div id="mainbox">
   	<logic:notEmpty name="quoteInfo">
    	<div id="contentbox">
        	<div id="title">客户管理 > 报价记录详细信息</div>
            <div class="descInf">
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">报价主题：</th>
                            <th class="descTitleR" colspan="3">
                                ${quoteInfo.quoTitle}&nbsp;
                            </th>
                        </tr>
                    </thead>
                	<tbody>
                    	<tr class="noBorderBot">
                            <th class="blue">对应机会：</th>
                            <td class="blue">
                                <span id="cusName">${quoteInfo.salOpp.oppTitle}&nbsp;</span>
                            </td>
                            <th class="blue">对应项目：</th>
                            <td class="blue">${quoteInfo.project.proTitle}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                        	<td colspan="4"><div>基本信息</div></td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>总报价：</th>
                            <td>￥<bean:write name="quoteInfo" property="quoPrice" format="###,###0.00"/></td>
                            <th>报价人：</th>
                            <td>${quoteInfo.salEmp.seName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>报价日期：</th>
                            <td colspan="3"><span id="quoDate"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>备注：</th>
                            <td colspan="3">${quoteInfo.quoRemark }&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                    <logic:notEmpty name="quoteInfo" property="attachments">
                                        <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${quoteInfo.quoId}','quo','reload')"/>
                                     </logic:notEmpty>
                                     <logic:empty name="quoteInfo" property="attachments">
                                        <img style="vertical-align:middle; cursor:pointer;" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${quoteInfo.quoId}','quo','reload')"/>
                                     </logic:empty>
                                    附件：
                            </th>
                            <td colspan="3">
                                 <logic:iterate id="attList" name="quoteInfo" property="attachments">
                                    <span id="fileDLLi<%=count%>"></span>
                    				<script type="text/javascript">createFileToDL(<%=count%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                                    <% count++;%>
                                </logic:iterate>
                            </td>
                        </tr>
                    </tbody>
                	<thead>
                    	<tr>
                            <td colspan="4"><div>报价明细&nbsp;&nbsp;
                                <span id="editRop" class="blackblue" style="padding:4px;">
                                <img class="imgAlign" src="crm/images/content/edit.gif" alt="编辑明细"/>
                                </span></div></td>
                        </tr>
                    </thead>
					<tbody>
                    	<tr>
                            <td colspan="4" class="subTab">
                                <table id="tab" class="rowstable"  cellpadding="0" cellspacing="0" style="width:100%">
                                    <tr>
                                        <th style="width:30%">产品名称/型号</th>
                                        <th style="width:10%">数量[单位]</th>
                                        <th style="width:10%">单价</th>
                                        <th style="width:10%">总价</th>
                                        <th style="width:10%; border-right:0px">备注</th>
                                    </tr>
                                    <logic:notEmpty name="quoteInfo" property="rquoPros">
                                    <logic:iterate id="rpp" name="quoteInfo" property="rquoPros">
                                    <tr id="ropTr<%=count1%>" onMouseOver="focusTr('ropTr',<%= count1%>,1)" onMouseOut="focusTr('ropTr',<%= count1%>,0)">
                                        <td>&nbsp;<logic:equal value="1" name="rpp" property="wmsProduct.wprIscount"><img src="crm/images/content/database.gif" alt="计算库存" class="imgAlign"/></logic:equal>${rpp.wmsProduct.wprName}<logic:notEmpty name="rpp" property="wmsProduct.wprModel">/${rpp.wmsProduct.wprModel}</logic:notEmpty></td>
                                        <td><bean:write name="rpp" property="rupNum" format="###,##0.00"/><logic:notEmpty name="rpp" property="wmsProduct.typeList">[${rpp.wmsProduct.typeList.typName}]</logic:notEmpty></td>
                                        <td>￥<bean:write name="rpp" property="rupPrice" format="###,##0.00"/></td>
                                        <td>￥<bean:write name="rpp" property="rupAllPrice" format="###,##0.00"/><input type="hidden" id="allprice<%=count1%>" value="${rpp.rupAllPrice}"/></td>
                                        <td>${rpp.rupRemark}&nbsp;</td>			
                                    </tr>
                                    <script type="text/javascript">	
                                        rowsBg('ropTr',<%=count1%>);
                                    </script>
                                    <%count1++;%>
                                    </logic:iterate>
                                    
                                     </logic:notEmpty>	
                                     <logic:empty name="quoteInfo" property="rquoPros">
                                        <tr>
                                            <td class="noDataTd" colspan="5">未添加</td>
                                        </tr>
                                     </logic:empty>
                                    <tr class="lightBlueBg bold">
                                        <td>合计</td>
                                        <td>&nbsp;</td>
                                        <td>——</td>
                                        <td><span id="ap">&nbsp;</span></td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                        <script type="text/javascript">heji(<%=count1%>);</script>	
                                </table>   
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div class="descStamp">
                    由
                    <span>${quoteInfo.quoInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="quoteInfo" property="quoUpdUser">，最近由
                    <span>${quoteInfo.quoUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>
                <script type="text/javascript">
					removeTime("quoDate","${quoteInfo.quoDate}",1)
					removeTime("inpDate","${quoteInfo.quoInsDate}",2);
					removeTime("changeDate","${quoteInfo.quoUpdDate}",2);
				</script>
            </div>
         </div>
      </logic:notEmpty>
	 <logic:empty name="quoteInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该报价记录已被删除</div>
	</logic:empty>
     </div>
  </body>
</html>
