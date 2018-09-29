<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <title>编辑报价记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
	<script type="text/javascript" >
    	function check(){
			var errStr = "";
			if(isEmpty("quoTitle")){
				errStr+="- 未填写主题！\n";
			}
			else if(checkLength("quoTitle",100)){
				errStr+="- 主题不能超过100个字！\n";
			}
			if(isEmpty("quoPri")){
				errStr+="- 未填写总报价！\n";
			}
			 
			if(isEmpty("pid")){
				errStr+="- 未填写报价日期！\n";
			}

			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
		 	else {
			 	waitSubmit("dosave","保存中...");
			 	waitSubmit("doCancel");
			 	return $("edit").submit();
			}
		}
  	</script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<logic:notEmpty name="quoteInfo">
  	<form id="edit" action="cusServAction.do" method="post">
   		<input type="hidden" name="op" value="changeQuote" />
        <input type="hidden" name="quoId" value="${quoteInfo.quoId}"/>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">主题：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="quoTitle" class="inputSize2L" type="text" name="quote.quoTitle" value="${quoteInfo.quoTitle}" onBlur="autoShort(this,100)"/></th>
                </tr>
            </thead>
            <tbody>
            	<logic:notEmpty name="quoteInfo" property="salOpp" >
                    <tr class="noBorderBot">
                        <th>对应机会：</th>
                        <td colspan="3"><span id="oppTitle" class="textOverflowL" title="${quoteInfo.salOpp.oppTitle}">${quoteInfo.salOpp.oppTitle}</span><input type="hidden" name="isIfrm" value="1"/></td>
                    </tr>
                </logic:notEmpty>
                <logic:notEmpty name="quoteInfo" property="project" >
                    <tr class="noBorderBot">
                        <th>对应项目：</th>
                        <td colspan="3"><span class="textOverflowL" title="${quoteInfo.project.proTitle}">${quoteInfo.project.proTitle}</span><input type="hidden" name="isIfrm" value="1"/></td>
                    </tr>
                </logic:notEmpty>
            </tbody>
            <thead>
            	<tr>
                	<td colspan="4"><div>基本信息</div></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th class="required">总报价：<span class='red'>*</span></th>
                    <td>
                    <input class="inputSize2" name="quote.quoPrice" id="quoPri" type="text" value="<bean:write name='quoteInfo' property='quoPrice' format='0.00'/>" onBlur="checkPrice(this)"/></td>
                    <th>报价人：</th>
                    <td>
                    	<input type="hidden" name="empId" id="seNo" value="${quoteInfo.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" ondblClick="clearInput(this,'seNo')" value="${quoteInfo.salEmp.seName}" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
			     	</td>
                </tr>
                <tr>
                	<th class="required">报价日期：<span class='red'>*</span></th>
                    <td colspan="3">
                        <input name="quoDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3" class="longTd"><textarea class="inputSize2L" rows="3" name="quote.quoRemark" onBlur="autoShort(this,4000)">${quoteInfo.quoRemark}</textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                    </td>
                </tr>
                <tr>
                    <td class="tipsTd" colspan="4">
                        <div class="tipsLayer">
                            <ul>
                                <li>&nbsp;保存后可在报价详情中编辑报价明细。</li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <script type="text/javascript">
             document.getElementById("pid").value='${quoteInfo.quoDate}'.substring(0,10);
         </script>
	</form>
    </logic:notEmpty>
    <logic:empty name="quoteInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该报价记录已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>
