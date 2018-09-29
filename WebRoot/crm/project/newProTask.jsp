<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>新建项目工作(未使用)</title>
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
     <script type="text/javascript" src="js/formCheck.js"></script>
	 <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
   		function findSubPro(){     
	         var proId=$("proId").value;
		     var url ="projectAction.do";
			 var pars = "op=loadStageActor&proId="+proId+"&ran=" + Math.random();
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
			        	for(var i=0;i<n;i++){
						    var selOption=docXml.getElementsByTagName("option")[i];
							if(selOption!=null){
							    var opValue=selOption.getAttribute("value");
							    var opTxt=selOption.childNodes[0].nodeValue;
								var item = new Option(opTxt,opValue); //通过Option()构造函数创建option对象      
		          			        sel.options.add(item); //添加到options集合中
							}
						}
						var spanElement=$("actorList");
						//spanElement.appendChild(document.createElement("nobr"));
						spanElement.innerHTML="&nbsp;";//清空
						var m=docXml.getElementsByTagName("actor").length;
				   		for(var j=0;j<m;j++){
						    var chk=docXml.getElementsByTagName("actor")[j];
							if(chk!=null){
							    var chkValue=chk.getAttribute("value");
							    var chkTxt=chk.childNodes[0].nodeValue;
							    var str="<input type='checkbox' name='actor' id='actor"+j+"' value='"+chkValue+"'><label for='actor"+j+"'>"+chkTxt+
							    "</label><input type='hidden' name='"+chkValue+"' value='"+chkTxt+"'>";
							    spanElement.innerHTML+=str;
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
			var flag=false;
			var array= $N("actor");
			var errStr = "";
			if(isEmpty("prtaTitle")){   
				errStr+="- 未填写工作名称！\n"; 
			}
			else if(checkLength("prtaTitle",300)){
				errStr+="- 工作名称不能超过300个字！\n";
			}
			if(isEmpty("proTitle")){   
				errStr+="- 未选择对应项目！\n"; 
			}
			for(var i=0;i<array.length;i++){ 
				if(array[i].checked==true){ 
						flag=true; 
				  }
			} 
			if(!flag){
				 errStr+="- 未选择工作执行人！\n";
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
  		<input type="hidden" name="op" value="saveProTask">
		<table class="normal dashTab" cellpadding="0" cellspacing="0" style="width:98%">
			<thead>
				<tr>
					<th class="descTitleL" style="vertical-align:top; width:100px;"><nobr><span class='red'>*</span>工作名称</nobr></th>
					<th class="descTitleR" colspan="3"><input name="proTask.prtaTitle" id="prtaTitle" type="text" class="inputSize2" style="width:480px" onBlur="autoShort(this,300)"/></th>				
				</tr>
			</thead>
			<tbody>
			<logic:notEqual value="n" name="showSelPro" >
			<tr>
					<th><nobr><span class='red'>*</span>对应项目：</nobr></th>
					<td colspan="3">
					<input name="proTitle" id="proTitle" value="${proTitle}" type="text" class="inputSize2 lockBack" style="width:401px" ondblClick="clearInput(this,'proId')" title="此处文本无法编辑，双击可清空文本" readonly>&nbsp;
                    <button class="butSize2" onClick="parent.addDivBrow(6)">点击选择</button>
					<input type="hidden" name="proId" value="${proId}" id="proId"/>
					</td>				
			</tr>
			</logic:notEqual>
			<logic:equal value="n" name="showSelPro" >
			<tr>
					<th><nobr>对应项目：</nobr></th>
					<td colspan="3">
                    <span class="textOverflow" style="width:480px" title="${project.proTitle}">${project.proTitle}</span>
					<input name="proTitle" id="proTitle" value="${project.proTitle}" type="hidden">
					<input type="hidden" name="proId" value="${project.proId}" id="proId"/>
                    <input type="hidden" name="isIfrm" value="1"/>
					</td>				
			</tr>
			</logic:equal>
			<tr>
					<th><nobr>子项目：</nobr></th>
					<td colspan="3">
					 <logic:notEqual value="y" name="proStage">
						<select id="staId" name="staId" class="inputSize2" style="width:480px">
	                     <option value="" ></option>
	                     </select>
                    </logic:notEqual>
                    <logic:equal value="y" name="proStage" >
						<select name="staId" class="inputSize2" style="width:480px">
	                    <option value="" ></option>
	                    <logic:notEmpty name="project" property="proStages">
	                           <logic:iterate id="pStage" name="project" property="proStages">
	                           <logic:equal value="1" name="pStage" property="staIsdel">
	                           <option value="${pStage.staId}">${pStage.staTitle}</option>
	                           </logic:equal>
	                           </logic:iterate>
	                    </logic:notEmpty>
	                    </select>
				  </logic:equal>
					</td>				
			</tr>
			
			<tr>
					<th>工作状态：</th>
					<td>
					<input type="radio" name="proTask.prtaState" id="sta1" value="0" checked="checked"><label for="sta1">执行中&nbsp;&nbsp;</label>
					<input type="radio" name="proTask.prtaState" id="sta2" value="1"><label for="sta2">已完成&nbsp;&nbsp;</label>
					<input type="radio" name="proTask.prtaState" id="sta3" value="2"><label for="sta3">取消</label>
					</td>
                    <th>优先级：</th>
					<td>
					<input type="radio" name="proTask.prtaLev" id="lev1" value="0" checked="checked"><label for="lev1">低&nbsp;&nbsp;</label>
					<input type="radio" name="proTask.prtaLev" id="lev2" value="1"><label for="lev2">中&nbsp;&nbsp;</label>
					<input type="radio" name="proTask.prtaLev" id="lev3" value="2"><label for="lev3">高</label></td>
			</tr>
			<tr>
            	<th><nobr>完成期限：</nobr></th>
				<td colspan="3">
				<input name="prtaFinDate" type="text"  readonly="readonly" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="cursor:hand;" ondblClick="clearInput(this)"/></td>
					
			</tr>

			<tr>
                    <th style="vertical-align:top"><span class="red">*</span>执行人：</th>
                    <td colspan="3">
<!--                        <div onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" style="cursor:pointer; padding:1px" onClick="showHide('userDiv','showmore')">&nbsp;<img id="showmore" src="images/content/showmore.gif" class="imgAlign" alt="点击展开"/>&nbsp;展开勾选工作执行人</div>-->
                        <div id="userDiv" style="background-color:#fff;width:480px">
                         <logic:notEqual value="y" name="proStage">
                          <span id="actorList"></span>
                         </logic:notEqual>
                         <logic:equal value="y" name="proStage" >
						 <logic:notEmpty name="project" property="proActors">
                          <logic:iterate id="proActor" name="project" property="proActors">
                          <logic:equal value="1" name="proActor" property="actIsdel">
							<span><nobr><input type="checkbox" name="actor" id="ac<%=count%>" value="${proActor.salEmp.seNo}" ><label for="ac<%=count%>">${proActor.salEmp.seName}</label><input type="hidden" name="${proActor.salEmp.seNo}" value="${proActor.salEmp.seName}">&nbsp;&nbsp;</nobr></span>
                          </logic:equal>
                          <% count++; %>
                          </logic:iterate>
                        </logic:notEmpty>
						</logic:equal>
                       </div>
                    </td>
                </tr>
			<tr>
				<th style="vertical-align:top">工作描述：</th>
				<td colspan="3"><textarea rows="6" onBlur="autoShort(this,4000)" style="width:480px;" name="proTask.prtaDesc"></textarea></td>
			</tr>
<!--			<tr>
				<th style="vertical-align:top"> 备注：</th>
				<td colspan="3"><textarea rows="3" onblur="autoShort(this,4000)" style="width:480px;" name="proTask.prtaRemark"></textarea></td>
			</tr>-->
			<tr>
				<td colspan="4" align="center" style="border:0px">
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
