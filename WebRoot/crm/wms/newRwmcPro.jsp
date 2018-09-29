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
    
    <title>录入产品盘点</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/choosePro.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">
		function check(){
		 	var num=$("relNum").value;
			var num2=$("rspProNum").innerText.replace(/,/g,"");
			var errStr = "";
			if(isEmpty("wprName")){   
		 		errStr+="- 未选择产品！\n"; 
			}
			if(isEmpty("relNum")){
				errStr+="- 未填写实际盘点数量！\n"; 
			}
			else if(isNaN(num)){
				errStr+="- 实际盘点数量请填写数字！\n";
			}
			if(parseFloat(num2)==0){
				errStr+="- 该产品没有库存！\n";
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
	</script>
  </head>
  <body>
  <div class="inputDiv">
  		<form action="wwoAction.do" method="post" id="register">
		<input type="hidden" name="op" value="newRwmcPro">
  		<input type="hidden" name="wprCode" id="wprCode" value="">
  		<input type="hidden" name="wmcId" value="${wmcId}">
		<input type="hidden" name="rwmsChange.rwcDifferent" id="rspNum"  value="">
		<input type="hidden" name="rwmsChange.rmcWmsCount" id="num" value="">
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<tbody>
                <tr>
                    <th>盘点仓库：</th>
                    <td><span class="textOverflow" style="width:150px" title="${wmsCheck.wmsStro.wmsName}">${wmsCheck.wmsStro.wmsName}</span></td>			
                </tr>
                <tr>
                    <th class="required">盘点产品：<span class='red'>*</span></th>
                    <td>
                    <input name="wprName" readonly="readonly" id="wprName" type="text" class="inputSize2S lockBack" ondblClick="clearInput(this,'wprCode')" title="此处文本无法编辑，双击可清空文本"/>&nbsp;<input type="button" onClick="forwardToChoose(4,'${wmsCheck.wmsStro.wmsCode}')" class="butSize2" value="选择">
                    </td>			
                </tr>
                <tr>
                    <th>账面数量：</th>
                    <td><span id="rspProNum"></span>&nbsp;</td>
                </tr>
                <tr>
                    <th class="required">实际盘点数量：<span class='red'>*</span></th>
                    <td><input class="inputSize2" name="rwmsChange.rmcRealNum" onBlur="checkIsNum(this)" id="relNum" type="text" value="">&nbsp;</td>
                </tr>
                <tr>
                    <th>差异数量：</th>
                    <td><span id="dif"></span>&nbsp;</td>
                </tr>
                <tr>
                    <th>模式：</th>
                    <td>
                    <select name="rwmsChange.rmcType" class="inputSize2">
                        <option value="">&nbsp;</option>
                        <option value="0">不计成本</option>
                        <option value="1">计算成本</option>
                    </select>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea name="rwmsChange.rmcRemark" class="inputSize2" onBlur="autoShort(this,500)"></textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="button" id="dosave" class="butSize1" value="保存" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>
           	</tbody>			
	  	</table>
	</form>
  </div>
  </body>
  <script type="text/javascript">       
    $("relNum").onkeyup=function(){
        if($("rspProNum")!=null&&$("num").value!=""){
			checkIsNum($("relNum"));
            $("rspNum").value=parseFloat($("relNum").value)-parseFloat($("num").value);
            $("dif").innerText=Math.round((parseFloat($("relNum").value)-parseFloat($("num").value))*100)/100;
        }
    }
  </script> 
</html>
