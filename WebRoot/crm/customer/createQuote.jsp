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
    <title>新建报价记录</title>
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
				waitSubmit("save","保存中...");
				waitSubmit("doCancel");
				return $("create").submit();
			}
		 }
  	</script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<form id="create" action="cusServAction.do" method="post">
   		<input type="hidden" name="op" value="saveQuote" />
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">主题：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="quoTitle" class="inputSize2L" type="text" name="quote.quoTitle" onBlur="autoShort(this,100)"/></th>
                </tr>
            </thead>
            <tbody>
            	<logic:equal value="py" name="pInfo">
                <tr class="noBorderBot">
                    <th>对应机会：</th>
                    <td colspan="3"><span id="oppTitle" class="textOverflowL" title="${oppTitle}">${oppTitle}&nbsp;</span><input type="hidden" name="oppId" value="${oppId}"/>
                <input type="hidden" name="isIfrm" value="1"/></td>
                </tr>
                </logic:equal>
                <logic:equal value="pn" name="pInfo" >
                <tr class="noBorderBot">
                    <th>对应项目：</th>
                    <td colspan="3"><span id="proName" class="textOverflowL" title="${proName}">${proName}</span><input type="hidden" name="proId" value="${proId}"/>
                <input type="hidden" name="isIfrm" value="1"/></td>
                </tr>
                </logic:equal>
           	</tbody>
           	<thead>
           		<tr>
                	<td colspan="4"><div>基本信息</div></td>
                </tr>	
           	</thead>
            <tbody>
                <tr>
                    <th class="required">总报价：<span class='red'>*</span></th>
                    <td><input name="quote.quoPrice" id="quoPri" type="text"  onblur="checkPrice(this)" class="inputSize2" /></td>
                   	<th>报价人：</th>
                    <td>
                    	<input type="hidden" name="empId" id="seNo" value="${limUser.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="${limUser.userSeName}" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
			     	</td>
                </tr>
                <tr>
                	<th class="required">报价日期：<span class='red'>*</span></th>
                    <td colspan="3">
                        <input name="quoDate"  type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
                    </td>
                	
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3" class="longTd"><textarea class="inputSize2L" rows="3" name="quote.quoRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="save" class="butSize1" type="button" value="保存" onClick="check()" />
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
	</form>
    </div>
  </body>
</html>
