<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>录入库间调拨单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    
    <script type="text/javascript">       
		function check(){
			var errStr = "";
			if(isEmpty("pname")){
				errStr+="- 未填写库间调拨单主题！\n";
			}
			else if(checkLength("pname",300)){
				errStr+="- 库间调拨单主题不能超过300个字！\n";
			}
			if(isEmpty("owms")){
				errStr+="- 未选择调出仓库！\n";
			}
			if(isEmpty("iwms")){
				errStr+="- 未选择调入仓库！\n";
			}
			if($("iwms").value==$("owms").value){
				errStr+="- 请选择不同的仓库进行调拨！\n";
			}
			
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("register").submit();
			}				  
		}
		
		window.onload=function(){
			if('${wmsCode}'!=null&&'${wmsCode}'!=""&&$("owms")!=null){
				var sel=$("owms");
				sel.value='${wmsCode}';
				$("woTxt").innerText=sel.options[sel.selectedIndex].text;
				sel.style.display="none";
			}
		}
  </script> 
  </head>
  
  <body>
  <div class="inputDiv">
  		<form action="wwoAction.do" method="post" id="register">
  		<input type="hidden" name="op" value="newWmsChange" />
        <input type="hidden" name="wmsChange.wchState" value="0" />
		<logic:notEmpty name="wmsCode">
			<input type="hidden" name="outWms" value="${wmsCode}" />
		</logic:notEmpty>
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="sysCodeL">调拨单号：</th>
                    <th class="sysCodeRG" colspan="3">系统自动生成</th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th class="required">主题：<span class='red'>*</span></th>
                    <td colspan="3"><input name="wmsChange.wchTitle" id="pname" type="text" class="inputSize2L" onBlur="autoShort(this,300)" /></td>				
                </tr>
                <tr>
                    <th class="required">调出仓库：<span class='red'>*</span></th>
                    <td>
                        <select name="outWms" id="owms" class="inputSize2">
                            <option>&nbsp;</option>
                            <logic:notEmpty name="wmsStro">
                            <logic:iterate id="ws" name="wmsStro" scope="request">
                            <option value="${ws.wmsCode}">${ws.wmsName}</option>
                            </logic:iterate>
                            </logic:notEmpty>
                        </select>
                        <span id="woTxt"></span>
                    </td>
                    <th>预计调出日期：</th>
                    <td><input type="text" name="wchOutDate" class="Wdate inputSize2" readonly="readonly" id="pid1" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                   
                </tr>
                <tr>
                    <th class="required">调入仓库：<span class='red'>*</span></th>
                    <td>
                        <select name="inWms" id="iwms" class="inputSize2">
                            <option>&nbsp;</option>
                            <logic:notEmpty name="wmsStro">
                                <logic:iterate id="ws" name="wmsStro" scope="request">
                                <option value="${ws.wmsCode}">${ws.wmsName}</option>
                                </logic:iterate>
                            </logic:notEmpty>
                        </select>
                        <span id="wiTxt"></span>
                    </td>
                    <th>预计调入日期：</th>
                    <td>
                    <input type="text" name="wchInDate" class="Wdate inputSize2" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                </tr>
                <tr>
                	<th>填单人：</th>
                    <td colspan="3">${limUser.userSeName}&nbsp;</td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="3" name="wmsChange.wchRemark" onBlur="autoShort(this,4000)" ></textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input type="Button" class="butSize1" id="dosave" value="保存" onClick="check()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>
                <tr>
                    <td class="tipsTd" colspan="4">
                        <div class="tipsLayer">
                            <ul>
                                <li>&nbsp;保存后请打开调拨单编辑调拨明细。</li>
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