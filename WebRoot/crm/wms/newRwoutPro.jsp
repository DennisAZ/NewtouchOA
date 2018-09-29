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
    
    <title>新建出库产品</title>
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
			var num=$("rspProNum").innerText.replace(/,/g,"");
			var num2=$("rspNum").value;
			 
			var errStr = "";
			if(isEmpty("wprName")){   
		 		errStr+="- 未选择产品！\n"; 
			}
			if(isEmpty("rspNum")){
				errStr+="- 未填写出库数量！\n"; 
			}
			else if(isNaN(num2)){
				errStr+="- 出库数量请填写数字！\n";
			}
			if(parseFloat(num)==0){
				errStr+="- 该产品没有库存！\n";
			}
			if(parseFloat(num2)>parseFloat(num)){
				errStr+="- 出库数量大于库存，无法出库！\n";	
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
		<input type="hidden" name="op" value="newWwoPro">
  		<input type="hidden" name="wprId" id="wprCode" value="">
  		<input type="hidden" name="wwoId" value="${wwoId}">
		<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th>调出仓库：</th>
                    <td><span class="textOverflow" style="width:150px" title="${wmsWarOut.wmsStro.wmsName}">${wmsWarOut.wmsStro.wmsName}</span></td>
                </tr>
                <tr>
                    <th class="required">出库产品：<span class='red'>*</span></th>
                    <td>
                    <input name="wprName" readonly="readonly" id="wprName" type="text" class="inputSize2S lockBack" ondblClick="clearInput(this,'wprCode')" title="此处文本无法编辑，双击可清空文本"/>&nbsp;<input type="button" onClick="forwardToChoose(4,'${wmsWarOut.wmsStro.wmsCode}')" class="butSize2" value="选择">
                    </td>				
                </tr>
                <tr>
                    <th>库存量：</th>
                    <td><span id="rspProNum"></span>&nbsp;</td>
                </tr>
                <tr>
                    <th class="required">出库量：<span class='red'>*</span></th>
                    <td><input name="rwoWoutNum" id="rspNum" type="text"  onblur="checkIsNum(this)" class="inputSize2"></td>
              	</tr>
              	<tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea name="rwoRemark" class="inputSize2" onBlur="autoShort(this,500)"></textarea></td>
              	</tr>
                <tr class="submitTr">
                    <td  colspan="2">
                    <input type="button" id="dosave" value="保存" class="butSize1" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                </tr>	
            </tbody>
	  	</table>
	</form>
  </div>
  </body>
</html>
