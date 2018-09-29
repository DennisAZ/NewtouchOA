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
    <title>新建项目日志</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
     <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <link rel="stylesheet" type="text/css" href="css/style.css"/>
     <script type="text/javascript" src="js/prototype.js"></script>
	 <script type="text/javascript" src="js/sal.js"></script>
	 <script type="text/javascript" src="js/common.js"></script>
	 <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
     <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">
     function findSubPro() 
	   {     
	         var proId=$("proId").value;
		     var url ="projectAction.do";
			 var pars = "op=loadSubPro&proId="+proId+"&ran=" + Math.random();
			 new Ajax.Request(url,{
			 	method:'get',
				parameters: pars,
				onSuccess:function(transport){
					var docXml = transport.responseXML;
				   	if(docXml!=null||docXml!=""){    
						var sel=$("staId");//获得select对象
						sel.options.length=0; 
						sel.add(new Option("",""));
						var n=docXml.getElementsByTagName("option").length;
						for(var i=0;i<n;i++)
						{
								var selOption=docXml.getElementsByTagName("option")[i];
								if(selOption!=null){
									var opValue=selOption.getAttribute("value");
									var opTxt=selOption.childNodes[0].nodeValue;
									var item = new Option(opTxt,opValue); //通过Option()构造函数创建option对象      
										sel.options.add(item); //添加到options集合中
								}
						}
				   	}
				},
				onFailure:function(transport){
					if (transport.status == 404)
						alert("您访问的url地址不存在！");
					else
						alert("Error: status code is " + transport.status);
				}
			 });

	   }

			function check(){
				var errStr = "";
				
				if(isEmpty("prtaTitle")){   
					errStr+="- 未填写项目日志主题！\n"; 
				}
				else if(checkLength("prtaTitle",300)){
					errStr+="- 项目日志主题不能超过300个字！\n";
				}
				if(isEmpty("proTitle")){   
					errStr+="- 未选择对应项目！\n"; 
				}
				if(errStr!=""){
					errStr+="\n请返回修改...";
					alert(errStr);
					return false;
				}
				else{
					waitSubmit("dosend","保存中...");
					waitSubmit("doCancel");
					return $("create").submit();	
				}		  
			}
  </script> 
</head>
  <body>
  <div class="inputDiv">
  	<form action="projectAction.do" method="post" name="create">
  	<input type="hidden" name="op" value="saveHisProTask">
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<th class="descTitleL">日志主题：<span class='red'>*</span></th>
					<th class="descTitleR" colspan="3"><input name="proTask.prtaTitle" id="prtaTitle" type="text" class="inputSize2L" onBlur="autoShort(this,300)"/></th>				
				</tr>
			</thead>
			<tbody>
			<logic:notEqual value="n" name="showSelPro" >
				<tr>
					<th class="required">对应项目：<span class='red'>*</span></th>
					<td colspan="3">
                        <input name="proTitle" id="proTitle" value="${proTitle}" type="text" ondblClick="clearInput(this,'proId')" class="inputSize2S lockBack" title="此处文本无法编辑，双击可清空" readonly>
                        <button class="butSize2" onClick="parent.addDivBrow(6)">选择</button>
                        <input type="hidden" name="proId" value="${proId}" id="proId"/>
					</td>				
				</tr>
			</logic:notEqual>
			<logic:equal value="n" name="showSelPro" >
				<tr>
					<th>对应项目：</th>
					<td colspan="3"><span class="textOverflowL" title="${project.proTitle}">${project.proTitle}</span>
					<input name="proTitle" id="proTitle" value="${project.proTitle}" type="hidden">
					<input type="hidden" name="proId" value="${project.proId}" id="proId"/>
					<input type="hidden" name="isIfrm" value="1"/>
					</td>				
				</tr>
			</logic:equal>
			<tr>
			    <th>子项目：</th>
			    <td colspan="3">
                    <select name="proTask.prtaStaName" class="inputSize2L">
                        <option value="" ></option>
                        <logic:notEmpty name="project" property="proStages">
                           <logic:iterate id="pStage" name="project" property="proStages">
                           <logic:equal value="1" name="pStage" property="staIsdel">
                           <option value="${pStage.staTitle}">${pStage.staTitle}</option>
                           </logic:equal>
                           </logic:iterate>
                        </logic:notEmpty>
                    </select>
				</td>				
			</tr>
			<tr>
				<th>日志内容：</th>
				<td colspan="3"><textarea class="inputSize2L" rows="6" onBlur="autoShort(this,4000)" name="proTask.prtaDesc"></textarea></td>
			</tr>
            <tr class="noBorderBot">
				<th>提交人：</th>
				<td>${limUser.userSeName}&nbsp;</td>
                <th>提交时间：</th>
                <td>${curTime}</td>
			</tr>
			<tr class="submitTr">
				<td colspan="4">
				<input type="button" class="butSize1" id="dosend" value="保存" onClick="check()">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="doCancel" type="button" class="butSize1" value="取消" onClick="cancel()"></td>
			</tr>
			</tbody>			
	  </table>
	</form>
  </div>
  </body>
</html>
