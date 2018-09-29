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
    
    <title>修改库存盘点</title>
    <link rel="shortcut icon" href="favicon.ico"/> 
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">       
		function check(){
			var errStr = "";
			if(isEmpty("pname")){
				errStr+="- 未填写盘点主题！\n";
			}
			else if(checkLength("pname",300)){
				errStr+="- 盘点主题不能超过300个字！\n";
			}
			if(isEmpty("ws")){
				errStr+="- 未选择仓库！\n";
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
		function showIndex(){
			var obj=$("ws")
			if(obj!=null){
				if('${wmsCode}'!=null&&'${wmsCode}'!=""){
						obj.value='${wmsCode}';
						$("wsTxt").innerText=obj.options[obj.selectedIndex].text;
						obj.style.display="none";
				}else{
					var wmsName = '${wmsCheck.wmsStro.wmsCode}'; 
						obj.value=wmsName;	    		
				}
			}
		}
		
		window.onload=function(){
			if("${wmsCheck}"!=null&&"${wmsCheck}"!=""){
				showIndex();
			}
		}
  </script> 
  </head>
  
  <body>
  <logic:notEmpty name="wmsCheck">
  <div class="inputDiv">
  	<form action="wwoAction.do" method="post" id="register">
		<input type="hidden" name="wmcId" value="${wmsCheck.wmcId}">
  		<input type="hidden" name="op" value="updateWmc">
		<logic:notEmpty name="wmsCode">
			<input type="hidden" name="wst" value="${wmsCode}">
		</logic:notEmpty>
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="sysCodeL">盘点单号：</th>
                    <th colspan="3" class="sysCodeR">${wmsCheck.wmcCode}&nbsp;</th>
                </tr>
            </thead>
        	<tbody>
            	<tr>
                    <th class="required">主题：<span class='red'>*</span></th>
                    <td colspan="3"><input name="wmcTitle" id="pname" type="text" class="inputSize2L" value="${wmsCheck.wmcTitle}" onBlur="autoShort(this,300)"/></td>				
                </tr>
                <tr>
                    <th class="required">仓库：<span class='red'>*</span></th>
                    <td>
                        <select name="wst" id="ws" class="inputSize2">
                            <option>&nbsp;</option>
                            <logic:notEmpty name="wmsStro">
                            <logic:iterate id="ws" name="wmsStro" scope="request">
                            <option value="${ws.wmsCode}">${ws.wmsName}</option>
                            </logic:iterate>
                            </logic:notEmpty>
                        </select>
                        <span id="wsTxt"></span>
                    </td>
                    <th>填单人：</th>
                    <td>${wmsCheck.wmcInpName}&nbsp;</td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="3" name="wmcRemark" onBlur="autoShort(this,4000)">${wmsCheck.wmcRemark}</textarea>&nbsp;</td>
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
                                <li>&nbsp;请打开盘点单详情编辑盘点差异。</li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </tbody>
	  	</table>
	</form>
  </div>
  <script type="text/javascript">		
		removeTime("wmcDate","${wmsCheck.wmcDate}",1);
		removeTime("wmcAppDate","${wmsCheck.wmcAppDate}",2);
	</script>
    </logic:notEmpty>
  <logic:empty name="wmsCheck">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该盘点单已被删除</div>
	</logic:empty>
  </body>
</html>
