<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
    <title>部门管理</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
		body {
			background-color:#fff;
		}
		.blueBorderTop span{
			padding:4px;
			cursor:pointer;
		}
		#descTab td{
			height:31px;
		}
		.smallBut {
			width:50px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/JsOrgStruct.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/hr.js"></script>
	 <script type="text/jscript">
			
	 if('${msg}'!=null&&'${msg}'!=""){
	    alert('${msg}');
		parent.history.go(0);
	}
	 function showDiv(obj){
	 	switch(obj){
			//显示部门详情
			case 0:
				$("descDiv").style.display="block";
		 		$("modDiv").style.display="none";
				break;
			
			//显示部门修改
			case 1:
				$("descDiv").style.display="none";
		 		$("modDiv").style.display="block";
				break;
			
			//显示公司详情
			case 2:
				$("defaultDiv").style.display="block";
				$("modComDiv").style.display="none";
				$("orgStruts").style.display="none";
				break;
			
			//显示公司修改
			case 3:
				$("defaultDiv").style.display="none";
				$("modComDiv").style.display="block";
				break;
				
			//显示部门架构图
			case 4:
				$("strutsIframe").innerHTML="<iframe frameborder='0' scrolling='auto' height='380px' width='100%' src='empAction.do?op=getAllOrg&mark=orgStruts'></iframe>";
				$("defaultDiv").style.display="none";
				$("orgStruts").style.display="block";
				break;
				
				
		}
	 }

	  function check(){
	  	var errStr = "";
		if(isEmpty("modSoCode")){
			errStr+="- 未填写部门编号！\n";
		}
		else if(checkLength("modSoCode",25)){
			errStr+="- 部门编号不能超过25位！\n";
		}
		if(isEmpty("modSoName")){   
			errStr+="- 未填写部门名称！\n"; 
		}
		else if(checkLength("modSoName",50)){
			errStr+="- 部门名称不能超过50个字！\n";
		}
/*		if($("modNum").value!=""&&!isint($("modNum").value)){
			errStr+="- 部门编制数请填写数字！\n";
		}*/
		 
		if($("isRepeat1").value==1){
			errStr+="- 此部门编号已存在！\n";
		}
		if($("isRepeat2").value==1){
			errStr+="- 此部门名称已存在！\n";
		}
		if($("isRepeat3").value==1){
			errStr+="- 不能把下级部门设置为上级！\n";
		} 
		if(errStr!=""){
			errStr+="\n请返回修改...";
			alert(errStr);
			return false;
		}
		else{
			waitSubmit("altButton1","保存中...");
			waitSubmit("altButton2","保存中...");
			waitSubmit("calButton1");
			waitSubmit("calButton2");
	     	$("modSalOrg").submit();
		}
	 }
	 
	 //公司信息修改验证
	  function modCheck2(){
	  		var errStr = "";
			if(isEmpty("modComName")){
				errStr+="- 未填写公司名称！\n";
			}
			else if(checkLength("modComName",50)){
				errStr+="- 公司名称不能超过50个字！\n";
			}
			 
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("altButton1","保存中...");
				waitSubmit("altButton2","保存中...");
				waitSubmit("calButton1");
				waitSubmit("calButton2");
				$("modCom").submit();
			}
	 }
	 function delSalOrg(code)
	 {
	 	if(!confirm("确定要删除该部门吗？"))
	 		return false;
	   else
	 	 window.location.href="empAction.do?op=delSalOrg&soCode="+code
	 }
	 
	 //载入部门信息及层的显示
	 function loadOrg(){
	 	//载入公司性质下拉
		$("comType").value="${comOrg.soOrgNature}";
		
	 	if('${salOrg.salOrg.soCode}'!="")
	     {
	     	$("upOrg").value='${salOrg.salOrg.soCode}';
	     }
	    if('${showButton}'==1||'${isMod}'==1)
	      {
	        if('${salOrg.salOrg.soCode}'=="")
	      	{
	      	   $("upOrg").value="";
	     	}
	      }
	   if('${newSalOrg.salOrg.soCode}'!="")
	     {
	     	$("upOrg").value='${newSalOrg.salOrg.soCode}';//部门编号名称填写重复返回显示下拉框的值
	     }
	   $("orgNature").value='${salOrg.soOrgNature}';

	   if('${showButton}'==1)
	   {
	   		$("descDiv").style.display="";
	   		$("defaultDiv").style.display="none";//查看详情返回控制层的显示与隐藏
	   }
	   if('${isMod}'==1)
	   {
	        $("defaultDiv").style.display="none";
	   		$("modDiv").style.display="";//修改后部门名称，编号出现重复返回
	   }
	   if('${isDel}'==1)
	   {
	        $("defaultDiv").style.display="none";
	   		$("descDiv").style.display="";//删除部门不成功返回
	   }
	 }
	 
	 
	 function checkOrgNum(obj){
	 	autoShort(obj,25);
		checkIsRepeat(obj,'empAction.do?op=checkOrgCode&orgCode=','${salOrg.soOrgCode}',1);
	 }
	 
	 function checkOrgName(obj){
	 	autoShort(obj,50);
		checkIsRepeat(obj,'empAction.do?op=checkOrgName&orgName=','<c:out value="${salOrg.soName}"/>',2)
	 }
	 
	 function checkFormRep(){
	 	autoShort($('modSoCode'),25);
		autoShort($('modSoName'),50);
	 	var orgCheck = new Array('empAction.do?op=checkOrgCode&orgCode=','modSoCode','<c:out value="${salOrg.soOrgCode}"/>',1);
		var nameCheck = new Array('empAction.do?op=checkOrgName&orgName=','modSoName','<c:out value="${salOrg.soName}"/>',2);
		var upCheck = new Array('empAction.do?op=checkUpOrgCode&oldSoCode='+ encodeURIComponent('${salOrg.soCode}') +'&modUpSoCode=','upOrg','${salOrg.salOrg.soCode}',3);
	 	checkRepeatForm(orgCheck,nameCheck,upCheck);
	 }
	 
	 window.onload=function(){
	 	$("defaultDiv").style.display="block";
	 	//初始化页面
		loadOrg();
		
	 }
	</script>
</head>
  <body style="text-align:left">
        <div id="defaultDiv" class="blueBorder" style="display:none;width:550px;">
            <div class="blueBorderTop" style="height:26px" >
                <table class="normal white" style="width:98%" cellspacing="0" cellpadding="0">
                    <tr>
                        <td class="middle"><nobr>&nbsp;公司信息</nobr></td>
                        <td width="120px"><nobr><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="showDiv(4)" ><img style="vertical-align:middle" src="crm/images/content/whiteTree.gif" alt="查看部门结构图"/>&nbsp;查看部门结构图</span></nobr></td>
                        <td width="110px"><nobr><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="showDiv(3)"><img style="vertical-align:middle" src="crm/images/content/whiteEdit.gif" alt="修改公司信息"/>&nbsp;修改公司信息</span></nobr></td>
                    </tr>
                </table>
            </div>
            <table id="descTab" class="normal dashTab" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th style="width:15%;"><nobr>公司名称：</nobr></th>
                        <td style="width:85%;">${comOrg.soName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>公司性质：</th>
                        <td>${comOrg.soOrgNature}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>企业法人：</th>
                        <td>${comOrg.soUserCode}&nbsp;</td>	
                    </tr>
                    <tr>
                        <th>员工人数：</th>
                        <td>${comOrg.soEmpNum}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>公司地址：</th>
                        <td>${comOrg.soLoc}&nbsp;</td>
                    </tr>
                    <tr>
                        <th style="border:0px">主营行业：</th>
                        <td style="border:0px">${comOrg.soResp}&nbsp;</td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <div id="modComDiv" class="blueBorder" style="display:none; width:550px;">
            <div class="blueBorderTop" style="padding:0px">
                <table class="normal white" style="width:98%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="middle"><nobr>&nbsp;修改公司信息</nobr></td>
                        <td width="80px"><nobr><button id="altButton1" class="smallBut" onClick="modCheck2()">保存</button></nobr></td>
                        <td width="55px"><nobr><button id="calButton1" class="smallBut" onClick="showDiv(2)" >取消</button></nobr></td>
                    </tr>
                </table>
            </div>
            <form action="empAction.do" method="post" id="modCom" style=" padding:0px; margin:0px">
                <input type="hidden" name="op" value="updateSalOrg">
                <input type="hidden" name="oldSoCode" value="${orgTopCode}">
                <table id="editTab" class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
                	<tbody>
                    	<tr>
                            <th style="width:15%"><nobr><span class='red'>*</span>公司名称：</nobr></th>
                            <td style="width:85%"><input name="comOrg.soName" id="modComName" type="text" value="<c:out value="${comOrg.soName}"/>" class="inputSize2" style="width:90%" onBlur="autoShort(this,50)"></td>
                        </tr>
                        <tr>
                            <th>公司性质：</th>
                            <td>
                                <select id="comType" name="comOrg.soOrgNature" style="width:90%">
                                    <option value="">请选择</option>
                                    <option value="企业单位">企业单位</option>
                                    <option value="事业单位或社会团体">事业单位或社会团体</option>
                                    <option value="个体经营">个体经营</option>
                                    <option value="个人">个人</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>企业法人：</th>
                            <td><input name="comOrg.soUserCode" type="text" value="<c:out value="${comOrg.soUserCode}"/>" class="inputSize2 inputBoxAlign" style="width:90%"  onblur="autoShort(this,25)"/>
                            </td>  
                        </tr>
                        <tr>
                            <th>员工人数：</th>
                            <td><input type="text" name="comOrg.soEmpNum" value="<c:out value="${comOrg.soEmpNum}"/>" class="inputSize2" style="width:90%"  onblur="autoShort(this,25)"/></td>
                        </tr>
                        <tr>
                            <th>公司地址：</th>
                            <td><input name="comOrg.soLoc"  type="text" value="<c:out value="${comOrg.soLoc}"/>" class="inputSize2" style="width:90%" onBlur="autoShort(this,500)"></td>
                        </tr>
                        <tr>
                            <th style="border:0px; vertical-align:top;">主营行业：</th>
                            <td style="border:0px">
                                <textarea name="comOrg.soResp" rows="5" style="width:90%"  onblur="autoShort(this,2000)">${comOrg.soResp}</textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
        
        <div id="orgStruts" class="blueBorder" style="display:none;width:550px;">
            <div class="blueBorderTop">
                <table class="normal white" width="98%">
                    <tr>
                        <td class="middle"><nobr>&nbsp;部门结构图</nobr></td>
                        <td width="110px"><nobr><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="showDiv(2)" ><img style="vertical-align:middle" src="crm/images/content/whiteClose.gif" alt="返回公司信息"/>&nbsp;返回公司信息</span></nobr></td>
                    </tr>
                </table>
            </div>
            <div id="strutsIframe" class="normal gray" style="text-align:center"><img src="crm/images/gif/uploading.gif"/>&nbsp;数据加载中...</div>
        </div>
        
        <div id="descDiv" class="blueBorder" style="display:none;width:550px;">
            <div class="blueBorderTop" style="height:26px">
                <table class="normal white" style="width:98%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="middle"><nobr>&nbsp;部门信息</nobr></td>
                        <td width="110px"><nobr><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="parent.addDivNew(2,'${salOrg.salOrg.soCode}')" ><img style="vertical-align:middle" src="crm/images/content/whiteAdd.gif" alt="添加同级部门"/>&nbsp;添加同级部门</span></nobr></td>
                        <td width="60px"><nobr><span  id="fixDep" onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="showDiv(1)"><img style="vertical-align:middle" src="crm/images/content/whiteEdit.gif" alt="修改"/>&nbsp;修改</span></nobr></td>
                        <td width="55px"><nobr><span id="delDep" onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="delSalOrg('${salOrg.soCode}')" ><img style="vertical-align:middle" src="crm/images/content/whiteClose.gif" alt="删除"/>&nbsp;删除</span></nobr></td>
                        <script type="text/javascript">displayLimAllow("sy008|sy009",new Array("fixDep","delDep"))</script>
                    </tr>
                </table>
            </div>
            <table id="descTab" class="normal dashTab" style="width:98%" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                        <th style="width:15%"><nobr>部门编号：</nobr></th>
                        <td style="width:85%">${salOrg.soOrgCode}&nbsp;</td>	
                    </tr>
                    <tr>
                        <th>部门名称：</th>
                        <td>${salOrg.soName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>上级部门：</th>
                        <td>${salOrg.salOrg.soName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>机构性质：</th>
                        <td>${salOrg.soOrgNature}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>成本中心：</th>
                        <td>${salOrg.soCostCenter}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>负责人：</th>
                        <td>${salOrg.soUserCode}&nbsp;</td>	
                    </tr>
                    <tr>
                        <th>编制数：</th>
                        <td>${salOrg.soEmpNum}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>办公地点：</th>
                        <td>${salOrg.soLoc}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>掌管区域：</th>
                        <td>${salOrg.soConArea}&nbsp;</td>
                    </tr>
                    <tr>
                        <th style="border:0px">部门职责：</th>
                        <td style="border:0px">${salOrg.soResp}&nbsp;</td>
                    </tr>
                </tbody>
            </table>
        </div>
        
        <div id="modDiv" class="blueBorder" style="display:none;width:550px;">
            <div class="blueBorderTop" style="padding:0px">
                <table class="normal white" style="width:98%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="middle"><nobr>&nbsp;修改部门信息</nobr></td>
                        <td width="80px"><nobr><button id="altButton2" class="smallBut inputBoxAlign" onClick="checkFormRep()">保存</button></nobr></td>
                        <td width="55px"><nobr><button id="calButton2" class="smallBut inputBoxAlign" onClick="showDiv(0)" >取消</button></nobr></td>
                    </tr>
                </table>
            </div>
            <form action="empAction.do" method="post" id="modSalOrg" style=" padding:0px; margin:0px">
                <input type="hidden" name="op" value="updateSalOrg"/>
                <input type="hidden" name="oldSoCode" value="${salOrg.soCode}"/>
                <input type="hidden" id="isRepeat1" />
                <input type="hidden" id="isRepeat2" />
                <input type="hidden" id="isRepeat3" />
                <div id="errDiv1" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此部门编号已存在</div>
                <div id="errDiv2" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此部门名称已存在</div>
                <table id="editTab" class="normal dashTab" style="width:98%" cellpadding="0" cellspacing="0">
                	<tbody>
                    	<tr>
                            <th style="vertical-align:top; width:15%;"><nobr><span class='red'>*</span>部门编号：</nobr></th>
                            <td style="width:85%"><input name="salOrg.soOrgCode" id="modSoCode" type="text" value="<c:out value="${salOrg.soOrgCode}"/>" class="inputSize2" style="width:90%;" onBlur="checkOrgNum(this)"/>
                            </td>
                        </tr>
                        <tr>
                            <th style="vertical-align:top"><span class='red'>*</span>部门名称：</th>
                            <td><input name="salOrg.soName" id="modSoName" type="text" value="<c:out value="${salOrg.soName}"/>" class="inputSize2" style="width:90%;" onBlur="checkOrgName(this)"/>
                            </td>
                        </tr>
                        <tr>   
                            <th>上级部门：</th>
                            <td>
                                <select id="upOrg" name="modUpSoCode" style="width:90%;" onChange="checkIsRepeat(this,'empAction.do?op=checkUpOrgCode&oldSoCode='+ encodeURIComponent('${salOrg.soCode}') +'&modUpSoCode=','${salOrg.salOrg.soCode}',3)">
                                   <option value=""></option>
                                    <logic:iterate id="sorg" name="orgList">
                                     <option value="${sorg.soCode}">${sorg.soName}</option>
                                    </logic:iterate>
                                </select>
                            <div>
                            <span id="errDiv3" class="redWarn" style="display:none">&nbsp;<img src="crm/images/content/fail.gif" class="imgAlign" alt="警告"/>&nbsp;不能选择当前部门的下级部门</span>
                           </div>
                            </td>
                         
                       </tr>
                       <tr>
                            <th>机构性质：</th>
                            <td>
                                <select id="orgNature" name="salOrg.soOrgNature" style="width:90%;">
                                    <option value="">请选择</option>
                                    <option value="总部">总部</option>
                                    <option value="分公司">分公司</option>
                                    <option value="子公司">子公司</option>
                                    <option value="部门">部门</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th>成本中心：</th>
                            <td><input name="salOrg.soCostCenter"  type="text" value="<c:out value="${salOrg.soCostCenter}"/>" class="inputSize2" style="width:90%;" onBlur="autoShort(this,100)"/></td>
                        </tr>
                        <tr>
                            <th>负责人：</th>
                            <td>
                     	<input id="modSoUserName" name="salOrg.soUserCode" class="inputSize2" style="width:80%;" type="text" value="<c:out value="${salOrg.soUserCode}"/>" onBlur="autoShort(this,25)"/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                            </td>  
                        </tr>
                        <tr>
                            <th>编制数：</th>
                            <td><input type="text" id="modNum" name="salOrg.soEmpNum" value="<c:out value="${salOrg.soEmpNum}"/>" class="inputSize2" style="width:90%;" onBlur="autoShort(this,25)"/></td>
                        <tr>
                            <th>办公地点：</th>
                            <td><input name="salOrg.soLoc"  type="text" value="<c:out value="${salOrg.soLoc}"/>" class="inputSize2" style="width:90%;" onBlur="autoShort(this,500)"/></td>
                        </tr>
                        <tr>
                            <th>掌管区域：</th>
                            <td><input name="salOrg.soConArea" type="text" value="<c:out value="${salOrg.soConArea}"/>" class="inputSize2" style="width:90%;" onBlur="autoShort(this,500)"/></td>    
                        </tr>   
                        <tr>
                            <th style="border:0px; vertical-align:top">部门职责：</th>
                            <td style="border:0px">
                                <textarea name="salOrg.soResp" cols="50" rows="5" style="width:90%;" onBlur="autoShort(this,1000)">${salOrg.soResp}</textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>

  </body>
</html>
