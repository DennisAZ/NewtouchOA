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
    <title>编辑项目工作(未使用)</title>
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
					waitSubmit("dosend","提交中...");
					waitSubmit("doCancel");
					return $("change").submit();	
				}		  
			}
		function showState()
		{
			$("staId").value='${proTask.proStage.staId}'
			var prtaState='${proTask.prtaState}';
			var prtaLev='${proTask.prtaLev}';
			if(prtaState=="0")
			   $("prtaState0").checked=true;
			if(prtaState=="1")
			   $("prtaState1").checked=true;
			if(prtaState=="2")
			   $("prtaState2").checked=true;
			if(prtaLev=="0")
			   $("prtaLev0").checked=true;
			if(prtaLev=="1")
			   $("prtaLev1").checked=true;
			if(prtaLev=="2")
			   $("prtaLev2").checked=true;
			
		}	
		function findSubPro()
		{
		  var proTitle=$("proTitle").value;
		  var proId=$("proId").value;
		  window.location.href="projectAction.do?op=getStageActor&proTitle="+encodeURIComponent(proTitle)+"&proId="+proId;
		} 
		window.onload=function(){
			if('${proTask}'!=null&&'${proTask}'!=''){
				if('${proTask.prtaState}'!=""&&'${proTask.prtaState}'!=1)
			    	showState();
			}
		
		}
  </script> 
</head>
  <body>
  <div class="inputDiv">
  <logic:notEmpty name="proTask">
    <logic:notEqual name="proTask" property="prtaState" value="1">
  	<form action="projectAction.do" method="post" name="change">
  	<input type="hidden" name="op" value="modProTask">
  	<input type="hidden" name="prtaId" value="${proTask.prtaId}">
		<table class="normal dashTab" cellpadding="0" cellspacing="0" style="width:98%">
			<thead>
				<tr>
                    <th class="descTitleL" style="vertical-align:top; width:100px;"><nobr><span class='red'>*</span>工作名称</nobr></th>
                    <th class="descTitleR" colspan="3"><input name="proTask.prtaTitle" id="prtaTitle" value="${proTask.prtaTitle}" type="text" class="inputSize2" style="width:480px" onBlur="autoShort(this,300)"></th>				
				</tr>
			</thead>
			<tbody>
			<tr>
                <th><nobr>对应项目：</nobr></th>
                <td colspan="3"><span class="textOverflow" style="width:480px" title="${project.proTitle}">${project.proTitle}</span>&nbsp;</td>				
			</tr>
			<tr>
                <th><nobr>子项目：</nobr></th>
                <td colspan="3">
                <select name="staId" id="staId" class="inputSize2" style="width:480px">
                <option value="" ></option>
                <logic:equal value="y" name="proStage" >
                <logic:notEmpty name="project" property="proStages">
                       <logic:iterate id="pStage" name="project" property="proStages">
                       <logic:equal value="1" name="pStage" property="staIsdel">
                       <option value="${pStage.staId}">${pStage.staTitle}</option>
                       </logic:equal>
                       </logic:iterate>
                </logic:notEmpty>
                </logic:equal>
              </select>
                </td>				
			</tr>
			
			<tr>
                <th>工作状态：</th>
                <td>
                <input type="radio" name="proTask.prtaState" id="prtaState0" value="0" ><label for="prtaState0">执行中&nbsp;&nbsp;</label>
                <input type="radio" name="proTask.prtaState" id="prtaState1" value="1"><label for="prtaState1">已完成&nbsp;&nbsp;</label>
                <input type="radio" name="proTask.prtaState" id="prtaState2" value="2"><label for="prtaState2">取消</label>
                </td>
                <th>优先级：</th>
                <td>
                <input type="radio" name="proTask.prtaLev" id="prtaLev0" value="0" checked="checked"><label for="prtaLev0">低&nbsp;&nbsp;</label>
                <input type="radio" name="proTask.prtaLev" id="prtaLev1" value="1"><label for="prtaLev1">中&nbsp;&nbsp;</label>
                <input type="radio" name="proTask.prtaLev" id="prtaLev2" value="2"><label for="prtaLev2">高</label></td>
			</tr>
			<tr>
            	<th><nobr>完成期限：</nobr></th>
				<td colspan="3"><input name="prtaFinDate" type="text" id="prtaFinDate" readonly="readonly"  class="Wdate" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd HH:mm'})" ondblClick="clearInput(this)" style="cursor:hand"/></td>
			</tr>
			<tr>
                <th style="vertical-align:top"><div align="right"><span class="red">*</span>执行人：</div></th>
                <td colspan="3">
<!--                    <div onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" style="cursor:pointer; padding:1px" onClick="showHide('userDiv','showmore')">&nbsp;<img id="showmore" src="images/content/showmore.gif" class="imgAlign" alt="点击展开"/>&nbsp;展开勾选工作执行人</div>-->
                    <div id="userDiv" style="background-color:#fff; width:480px">
                     <logic:equal value="y" name="proStage" >
                     <logic:notEmpty name="project" property="proActors">
                      <logic:iterate id="proActor" name="project" property="proActors">
                      <span><nobr><input type="checkbox" name="actor" id="actor<%=count%>" value="${proActor.salEmp.seNo}" ><label for="actor<%=count%>">${proActor.salEmp.seName}<input type="hidden" name="${proActor.salEmp.seNo}" value="${proActor.salEmp.seName}">&nbsp;&nbsp;</label></nobr></span>
                      <logic:iterate id="pTaskLim" name="proTask" property="proTaskLims">
                            <logic:equal value="1" name="pTaskLim" property="ptlIsdel">
                                <script type="text/javascript">
                                    var code='${pTaskLim.salEmp.seNo}';
                                    var code1=$("actor"+<%=count%>).value;
                                    if(code==code1){
                                            $("actor"+<%=count%>).checked=true;
                                            
                                    }
                                </script>
                            </logic:equal>
                            <logic:equal value="0" name="pTaskLim" property="ptlIsdel">
                                <script type="text/javascript">
                                    var code='${pTaskLim.salEmp.seNo}';
                                    var code1=$("actor"+<%=count%>).value;
                                    if(code==code1){
                                            $("actor"+<%=count%>).checked=false;
                                            
                                    }
                                </script>
                           </logic:equal>
                    </logic:iterate>
                       <%count++;%>
                      </logic:iterate>
                    </logic:notEmpty>
                    </logic:equal>
                   </div>
                </td>
            </tr>
			<tr>
				<th style="vertical-align:top">工作描述：</th>
				<td colspan="3"><textarea rows="6" onBlur="autoShort(this,4000)" style="width:480px;" name="proTask.prtaDesc">${proTask.prtaDesc}</textarea></td>
			</tr>
<!--			<tr>
				<th style="vertical-align:top"> 备注：</th>
				<td colspan="3"><textarea rows="3" onblur="autoShort(this,4000)" style="width:480px;" name="proTask.prtaRemark">${proTask.prtaRemark}</textarea></td>
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
	<script type="text/javascript">
	   removeTime("prtaFinDate","${proTask.prtaFinDate}",2);			 
   </script>
   </logic:notEqual>
   <logic:equal name="proTask" property="prtaState" value="1">
    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该工作已结束，不可再编辑！</div>
   </logic:equal>
  </logic:notEmpty>
  <logic:empty name="proTask">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目工作已被删除</div>
	  </logic:empty>
  </div>
  </body>
</html>
