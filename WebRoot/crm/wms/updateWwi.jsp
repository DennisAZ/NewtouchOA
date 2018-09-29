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
    <title>编辑入库单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body{
			background-color:#fff;
		}
	</style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">       
		function check(){
			var errStr = "";
			if(isEmpty("pname")){
				errStr+="- 未填写入库单主题！\n";
			}
			else if(checkLength("pname",300)){
				errStr+="- 入库单主题不能超过300个字！\n";
			}
			if(isEmpty("wms")){
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
		
		function initForm(){
			var obj=$("wms")
			if(obj!=null){
				if('${wmsCode}'!=null&&'${wmsCode}'!=""){
						obj.value='${wmsCode}';
						$("wsTxt").innerText=obj.options[obj.selectedIndex].text;
						obj.style.display="none";
				}else{
					var wmsName = '${wmsWarIn.wmsStro.wmsCode}'; 
						obj.value=wmsName;	    		
				}
			}
		}
		
		window.onload=function(){
			if("${wmsWarIn}"!=null&&"${wmsWarIn}"!=""){
				initForm();
			}
		}
  </script>
  </head>
  <body>
  <div class="inputDiv">
  	<logic:notEmpty name="wmsWarIn">
  		<form action="wmsManageAction.do" method="post" id="register">
	  		<input type="hidden" name="op" value="updateWwi">
	  		<input type="hidden" name="wwiId" value="${wmsWarIn.wwiId}">
      		<logic:notEmpty name="wmsWarIn" property="salPurOrd">
                <input type="hidden" name="isIfrm" value="1"/>
            </logic:notEmpty>
    		<logic:notEmpty name="wmsCode">
				<input type="hidden" name="wmsCode" value="${wmsCode}">
			</logic:notEmpty>
            <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
                <thead>
                    <tr>
                        <th class="sysCodeL">入库单号：</th>
                        <th class="sysCodeR" colspan="3">${wmsWarIn.wwiCode}&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th class="required">主题：<span class='red'>*</span></th>
                        <td colspan="3"><input name="wmsWarIn.wwiTitle" id="pname" type="text" class="inputSize2L" value="${wmsWarIn.wwiTitle}" onBlur="autoShort(this,300)"/></td>				
                    </tr>
                    <tr>
                        <th class="required">仓库：<span class='red'>*</span></th>
                        <td>
                            <select name="wmsCode" id="wms" class="inputSize2">
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
                        <td>${wmsWarIn.wwiInpName}&nbsp;</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="wmsWarIn.wwiRemark" onBlur="autoShort(this,4000)">${wmsWarIn.wwiRemark}</textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input type="Button" id="dosave" class="butSize1" value="保存" onClick="check()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                    </tr>
                    <tr>
                        <td class="tipsTd" colspan="4">
                            <div class="tipsLayer">
                                <ul>
                                    <li>&nbsp;请打开入库详情编辑入库明细。</li>
                                </ul>
                            </div>
                        </td>
                    </tr> 	
                </tbody>		
            </table> 
    </form>
     </logic:notEmpty>
     <logic:empty name="wmsWarIn">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该入库单已被删除</div>
	</logic:empty>
  </div>
  
  </body>
</html>
