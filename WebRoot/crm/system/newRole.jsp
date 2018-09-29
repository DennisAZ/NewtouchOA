<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base> 
    <title>新建职位</title>
    <link rel="shortcut icon" href="favicon.ico"/>
  	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/js/sys.js"></script>
	<script type="text/javascript">
		function check(){
			var errStr = "";
			if(isEmpty("rolName")){
				errStr+="- 未填写职位名称！\n";
			}
			else if(checkLength("rolName",50)){
				errStr+="- 职位名称不能超过50个字！\n";
			}
			if($("isRepeat").value==1){
				errStr+="- 此职位名称已存在！\n";
			}
			if(isEmpty("rolLev")){   
				errStr+="- 未选择职级！\n"; 
			}
			
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","保存中...");
				waitSubmit("cancel1");
				return $("register").submit();
			}		  
		 }
		  function showIndex(){
		  	 var xmlDoc;
			 if(window.ActiveXObject)
				{
				 //获得操作的xml文件的对象
					xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
					xmlDoc.async = false;
					xmlDoc.load("system/role.xml");
					if(xmlDoc == null)
					{
						alert('您的浏览器不支持xml文件读取,于是本页面禁止您的操作,推荐使用IE5.0以上可以解决此问题!');	
						return;
					 }
				}
		 //解析xml文件，判断是否出错
			if(xmlDoc.parseError.errorCode != 0)
			{
			   alert(xmlDoc.parseError.reason);
			   return;
			}
			var level=xmlDoc.getElementsByTagName("num");//读取XML里面的级别数
			var n=level[0].childNodes[0].nodeValue;
			var m=$("rolLev");
			m.options.length=1;
			for(var i=1;i<=n;i++){
				var text=i+'级';
				m.add(new Option(text,i));
				}
			}
		
		function checkRoleName(obj){
			if(obj != undefined){
				autoShort(obj,50);
				checkIsRepeat(obj,'userAction.do?op=checkRoleName&rolName=','<c:out value="${limRole.rolName}"/>');
			}
			else{
				autoShort($('rolName'),50);
				checkRepeatForm(new Array('userAction.do?op=checkRoleName&rolName=','rolName','<c:out value="${limRole.rolName}"/>'));
			}
		}
		
		window.onload=function(){
			showIndex();
		}
  </script> 
</head>
  
  <body>
     <div class="inputDiv">
  		<form action="userAction.do" method="post" id="register">
  			<input type="hidden" name="op" value="saveRole"/>
			<input type="hidden" id="isRepeat" />
            <div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此职位名已存在</div>
            <table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th style="width:80px;"><span class='red'>*</span>职位名称：</th>
                        <td><input name="limRole.rolName" id="rolName" type="text" style="width:260px;" value="<c:out value="${limRole.rolName}"/>" class="inputSize2" onBlur="checkRoleName(this)">
                        </td>
                    </tr>
                    <tr>
                        <th><span class='red'>*</span>职级：</th>
                        <td>
                            <select  name="limRole.rolLev" id="rolLev" style="width:260px;">
                                <option value="">请选择</option>
                            </select>
                        </td>
                    </tr>	
                    <tr>
                        <th style="vertical-align:top">职位描述：</th>
                        <td><textarea rows="3" cols="30" name="limRole.roleDesc" onBlur="autoShort(this,100)">${limRole.roleDesc}</textarea></td>
                    </tr>			
                    <tr>
                        <td colspan="2" style="border:0px; text-align:center">
                        <input type="button" class="butSize1" value="保存" id="Submit" onClick="checkRoleName()">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="cancel1" type="button" class="butSize1" value="取消" onClick="cancel()"></td>
                    </tr>
					<tr>
						<td colspan="2" style="border-bottom:0px">
							<div class="tipsLayer">
								<ul>
									<li>添加职级中系统默认一级为顶级</li>
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
