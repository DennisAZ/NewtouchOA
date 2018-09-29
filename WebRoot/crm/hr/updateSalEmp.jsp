<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>员工资料修改</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
		.grayTab {
			margin-left:5px;
			margin-right:5px;
			margin-bottom:2px;
			border:#fff 1px solid;
			border-bottom:#cdcdcd 1px solid;
			text-align:left;
			padding:6px;
			padding-top:15px;
			padding-bottom:15px;
		}
		.grayTab th{
			color:#999;
			width:8%;
		}
		.grayTab td{
			height:25px;
		}
		
		/* 橙色框 */
		.orangeBoxBorder {
			border:#fcb224 1px solid;
			background-color:#fefae6;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/hr.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript">
		 if('${msg}'!=null&&'${msg}'!=""){
	   		alert('${msg}');
			if(window.opener!=null){
				try{
	   				window.opener.loadList();
				}catch(error){}
			}
			window.opener=null;
		    window.open("",'_self',""); 
			window.close();
	   	}
	  
	   function showIndex()
	   {
		   var sex='${salEmp.seSex}';
		   var seIsovertime='${salEmp.seIsovertime}';//是否允许打卡加班
		   var marry='${salEmp.seMarry}';
		   var seRap='${salEmp.seRap}';//在职状态
		   if(sex=="男")
		   {
			$("sex1").checked=true; 
		   }
		   else if(sex=="女")
		   {
			 $("sex2").checked=true;
		   }
		   if(marry=="已婚")
		   {
			$("marry1").checked=true;
		   }
		   else if(marry=="未婚")
		   {
			 $("marry2").checked=true;
		   }
		   if(seRap=="在职")
		   {
			 $("seRap1").checked=true;
		   } 
		   else if(seRap=="离职")
		   {
			 $("seRap2").checked=true;
		   }
		   else if(seRap=="休假")
		   {
			 $("seRap3").checked=true;
		   }
		   if(seIsovertime=="否")
		   {
			 $("overtime1").checked=true;
		   } 
		   else if(seIsovertime=="是")
		   {
			 $("overtime2").checked=true;
		   }
		   $("seType").value='${salEmp.seType}';//员工类型
		   $("poliStatus").value='<c:out value="${salEmp.sePoliStatus}"/>';//政治面貌
		   $("jobLev").value='<c:out value="${salEmp.limRole.rolId}"/>';//职位
		   $("jobCate").value='<c:out value="${salEmp.seJobCate}"/>';//职类
		   $("houReg").value='<c:out value="${salEmp.seHouReg}"/>';//户籍性质
		   $("attendance").value='${salEmp.seAttendance}'//考勤方式
		   $("soCode").value='${salEmp.salOrg.soCode}';//所属部门
	   }
	   
	   	function checkEmpNum(obj){
			if(obj != undefined){
				autoShort(obj,25);
				checkIsRepeat(obj,'empAction.do?op=checkSecode&seCode=','<c:out value="${salEmp.seCode}"/>');
			}
			else{
				autoShort($('seCode'),25);
				checkRepeatForm(new Array('empAction.do?op=checkSecode&seCode=','seCode','<c:out value="${salEmp.seCode}"/>'));
			}
	   	}
		
		createProgressBar();
		window.onload=function(){
			if('${salEmp}'!=null&&'${salEmp}'!=''){
				showIndex();
				$("jobDate").value='${salEmp.seJobDate}'.substring(0,10);
				$("seBirth").value='${salEmp.seBirth}'.substring(0,10);
				$("endDate").value='${salEmp.seEndDate}'.substring(0,10);
			}
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<logic:notEmpty name="salEmp">
    <div id="errDiv" class="errorDiv redWarn" style=" display:none;">&nbsp;<img class="imgAlign" src="images/content/fail.gif" alt="警告">&nbsp;此员工号在系统(包括回收站)中已存在</div>
  	<div id="mainbox">
    	<div id="contentbox">
            <div class="descInf" style="width:680px">
            	<div id="descTop">修改员工信息</div>
            	<form action="empAction.do" id="creSalEmp" method="post" style="margin:0">
                    <input type="hidden" name="op" value="updEmp">
                    <input type="hidden" name="seNo" value="${salEmp.seNo}">
                    <input type="hidden" id="isRepeat" />
                    <div style="margin-left:5px;margin-right:5px;padding:6px;" class="orangeBack">
                        <table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        		<tr><td style="text-align:center"><input class="butSize3" type="button" id="sub1" onClick="checkRepeatForm(new Array('empAction.do?op=checkSecode&seCode=','seCode','<c:out value="${salEmp.seCode}"/>'))" value="保存员工信息"/></td></tr>
                        </table>
                    </div>
                    
                    <div class="grayTab" style="text-align:center" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                    	<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;基本信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right"><span class='red'>*</span>姓名：</div></td>
                                <td ><input class="inputSize2"  type="text" id="seName" name="salEmp.seName" value="<c:out value="${salEmp.seName}"/>" onBlur="autoShort(this,25)"/></td>
                                <td colspan="3" rowspan="8">&nbsp;&nbsp;&nbsp;&nbsp;
                                	<span class="orgBorder" style="width:206px">
                                        <div class="orgBorderTop" style="text-align:center">员工照片</div>
                                        <iframe frameborder="0" scrolling=no  height="205px" width="200px" src="fileAction.do?op=showImg&id=${salEmp.seNo}&type=empPic"></iframe>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td width="8%"><div align="right"><span class='red'>*</span>员工号：</div></td>
                                <td ><input class="inputSize2"  type="text" id="seCode" name="salEmp.seCode" value="<c:out value="${salEmp.seCode}"/>" onBlur="checkEmpNum(this)"/></td>
                                
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right"><span class='red'>*</span>所属部门：</div></td>
                                <td>
                                    <select name="soCode" id="soCode"  class="inputSize2">
                                        <option value="">请选择</option>
                                        <logic:notEmpty name="salOrg">	
                                        <logic:iterate id="so" name="salOrg" scope="request">
                                            <option value="${so.soCode}">${so.soName}</option>						
                                        </logic:iterate>
                                        </logic:notEmpty>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right"><span class='red'>*</span>职位：</div></td>
                                <td>
                                    <select id="jobLev" name="seJobLev"  class="inputSize2">
                                        <option value="">请选择</option>
                                        <logic:notEmpty name="roleList">
                                           <logic:iterate id="roleList" name="roleList">
                                           <option value="${roleList.rolId}">${roleList.rolName}</option>
                                           </logic:iterate>
                                        </logic:notEmpty>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right"><span class='red'>*</span>在职情况：</div></td>
                                <td>
                                    <div align="left">
                                    <input type="radio" name="salEmp.seRap" id="seRap1" value="在职" checked><label for="seRap1">在职&nbsp;</label>
                                    <input type="radio" name="salEmp.seRap" id="seRap2" value="离职"><label for="seRap2">离职&nbsp;</label>
                                    </div>
                                </td>
                           </tr>
                           <tr>
                           		<td>&nbsp;</td>
                                <td><div align="right">性别：</div></td>
                                <td><input  type="radio" name="salEmp.seSex" id="sex1" value="男" checked><label for="sex1">男&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    <input  type="radio" name="salEmp.seSex" id="sex2" value="女"><label for="sex2">女&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">成本中心：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seCostCenter" value="<c:out value="${salEmp.seCostCenter}"/>" onBlur="autoShort(this,25)"/></td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">最高学历：</div></td>
                                <td><input class="inputSize2"   type="text" name="salEmp.seEdu" value="<c:out value="${salEmp.seEdu}"/>"onblur="autoShort(this,50)"></td>
                            </tr>
                   		</table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;公司信息&nbsp;&nbsp;</div></th>
                                <td width="10%"><div align="right">员工类型：</div></td>
                                <td >
                                    <select name="salEmp.seType" id="seType"  class="inputSize2">
                                        <option value="">请选择</option>
                                        <option value="兼职">兼职</option>
                                        <option value="试用">试用</option>
                                        <option value="正式">正式</option>
                                        <option value="实习">实习</option>
                                        <option value="培训">培训</option>
                                    </select>
                                </td>	
                                <td width="9%"><div align="right">职类：</div></td>
                                <td >
                                    <select name="salEmp.seJobCate" id="jobCate"  class="inputSize2">
                                        <option value="">请选择</option>
                                        <option value="管理类">管理类</option>
                                        <option value="销售类">销售类</option>
                                        <option value="技术类">技术类</option>
                                        <option value="职能类">职能类</option>
                                        <option value="生产类">生产类</option>
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">职称：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seJobTitle" value="<c:out value="${salEmp.seJobTitle}"/>"  onblur="autoShort(this,50)"/></td>
                                <td><div align="right">招聘来源：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seRecSource" value="<c:out value="${salEmp.seRecSource}"/>" onBlur="autoShort(this,100)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">入职日期：</div></td>
                                <td><input class="inputSize2 Wdate" style="cursor:pointer" type="text"  name="jobDate" id="jobDate" readonly="readonly" ondblClick="clearInput(this)"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                                <td><div align="right">试用期(月)：</div></td>
                                <td><input class="inputSize2"  type="text" id="prob" name="salEmp.seProb" value="<c:out value="${salEmp.seProb}"/>" onBlur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">离职日期：</div></td>
                                <td><input class="inputSize2 Wdate" style="cursor:pointer" type="text"  name="endDate" id="endDate" readonly="readonly" ondblClick="clearInput(this)"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;薪酬信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right">本公司年资：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seYearPay"    value="<c:out value="${salEmp.seYearPay}"/>" onBlur="autoShort(this,50)"/></td>
                                <td width="8%"><div align="right">社保号：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seSocialCode" value="<c:out value="${salEmp.seSocialCode}"/>" onBlur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                          	</tr>
                            <tr>
                            	<td>&nbsp;</td>
								<td><div align="right">公积金号：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seProvFund" value="<c:out value="${salEmp.seProvFund}"/>" onBlur="autoShort(this,50)"/></td>
                                <td><div align="right">其他福利：</div></td>
                                <td><input class="inputSize2"  type="text" name="salEmp.seWealAddress" value="<c:out value="${salEmp.seWealAddress}"/>" onBlur="autoShort(this,50)"/></td>	
                                <td>&nbsp;</td>
                           </tr>
                           <tr>
                           		<td>&nbsp;</td>
                                <td><div align="right">开户银行：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seBankName" value="<c:out value="${salEmp.seBankName}"/>" onBlur="autoShort(this,50)"/></td>
                                <td><div align="right">银行账号：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seBankCard" value="<c:out value="${salEmp.seBankCard}"/>" onBlur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;个人信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right">身份证号：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seIdeCode" value="<c:out value="${salEmp.seIdeCode}"/>"  onblur="autoShort(this,20)"/></td>
                                <td width="8%" ><div align="right">出生日期：</div></td>
                                <td ><input class="inputSize2 Wdate" style="cursor:pointer" id="seBirth" type="text"  name="salEmp.seBirth"  readonly="readonly" ondblClick="clearInput(this)"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">婚姻状况：</div></td>
                                <td>
                                    <input  type="radio" name="salEmp.seMarry" id="marry1" value="已婚"><label for="marry1">已婚&nbsp;</label>
                                    <input  type="radio" name="salEmp.seMarry" id="marry2" value="未婚" checked><label for="marry2">未婚</label>
                                </td>
                                <td><div align="right">政治面貌：</div></td>
                                <td>
                                    <select id="poliStatus" name="salEmp.sePoliStatus"  class="inputSize2">
                                    <option value="">请选择</option>
                                    <option value="党员">党员</option>
                                    <option value="团员">团员</option>
                                    <option value="预备党员">预备党员</option>
                                    <option value="其他">其他</option>
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">民族：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seNation" value="<c:out value="${salEmp.seNation}"/>" onBlur="autoShort(this,25)"/></td>
                                <td><div align="right">籍贯：</div></td>
                                <td><input class="inputSize2"  type="text" name="salEmp.seBirPlace" value="<c:out value="${salEmp.seBirPlace}"/>"  onblur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                            </tr>
							<tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">户口所在地：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seAccPlace" value="<c:out value="${salEmp.seAccPlace}"/>"  onblur="autoShort(this,100)"/></td>
                                <td><div align="right">户籍性质：</div></td>
                                <td>
                                    <select id="houReg" name="salEmp.seHouReg"  class="inputSize2">
                                    <option value="">请选择</option>
                                    <option value="本市城镇">本市城镇</option>
                                    <option value="本市农村">本市农村</option>
                                    <option value="非本市城镇">非本市城镇</option>
                                    <option value="非本市农村">非本市农村</option>
                                    </select>
                                </td>	
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<th>&nbsp;</th>
                                <td align="right" valign="top"><div align="right">教育背景：</div></td>
                                <td colspan="3"><textarea rows="4" cols="30" style="width:444px" name="salEmp.seEdcBac" onBlur="autoShort(this,4000)">${salEmp.seEdcBac}</textarea></td>
                            </tr>
                            <tr><tr/>
                            <tr>
	                           	<th>&nbsp;</th>
	                            <td align="right" valign="top"><div align="right">工作经历：</div></td>
	                            <td colspan="3"><textarea rows="4" cols="30" style="width:444px" name="salEmp.seWorkEx" onBlur="autoShort(this,4000)">${salEmp.seWorkEx}</textarea></td>
                           </tr>
                        </table>
                    </div>
                    
                    <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;联系方式&nbsp;&nbsp;</div></th>
                                <td width="12%"><div align="right">手机：</div></td>
                                <td ><input  class="inputSize2" type="text" name="salEmp.sePhone" value="<c:out value="${salEmp.sePhone}"/>"  onblur="autoShort(this,25)"/></td>
                                <td width="11%"><div align="right">电话：</div></td>
                                <td ><input  class="inputSize2" type="text"  name="salEmp.seTel" value="<c:out value="${salEmp.seTel}"/>"  onblur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                         	<tr> 
                            	<td>&nbsp;</td>
                                <td><div align="right">QQ：</div></td>
                                <td><input  class="inputSize2" type="text"  name="salEmp.seQq" value="<c:out value="${salEmp.seQq}"/>" onBlur="autoShort(this,25)"/></td>
                                <td ><div align="right">MSN：</div></td>
                                <td><input  class="inputSize2" type="text" name="salEmp.seMsn" value="<c:out value="${salEmp.seMsn}"/>"  onblur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">电子邮件：</div></td>
                                <td><input  class="inputSize2" type="text"  name="salEmp.seEmail" value="<c:out value="${salEmp.seEmail}"/>"  onblur="autoShort(this,50)"/></td>
                                <td ><div align="right">居住地址：</div></td>
                                <td><input  class="inputSize2" type="text" name="salEmp.seAddress" value="<c:out value="${salEmp.seAddress}"/>" onBlur="autoShort(this,500)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            
                        </table>
                   </div>

                  
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;考勤信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right">考勤方式：</div></td>
                                <td >
                                	<select id="attendance" name="salEmp.seAttendance"  class="inputSize2">
                                 		<option value="">请选择</option>
                                 		<option value="正常打卡">正常打卡</option>
                                 		<option value="免卡人员">免卡人员</option>
                                 		<option value="弹性考勤">弹性考勤</option>
                                 	</select>
                                </td>
                                <td width="8%"><div align="right">卡号：</div></td>
                                <td ><input  class="inputSize2" type="text"  name="salEmp.seCardNum" value="<c:out value="${salEmp.seCardNum}"/>"  onblur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">允许打卡加班：</div></td>
                                <td><input  type="radio" name="salEmp.seIsovertime" id="overtime1" value="否" checked><label for="overtime1">否&nbsp;</label>&nbsp;&nbsp;<input  type="radio" name="salEmp.seIsovertime" id="overtime2" value="是"><label for="overtime2">是</label></td>
                                <td colspan="3">&nbsp;</td>
                             </tr>
                       	</table>
                    </div>
                    <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                    	<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th style="vertical-align:top;"><div align="center">&nbsp;&nbsp;其他信息&nbsp;&nbsp;</div></th>
                                <td style="width:12%;vertical-align:top;"><div align="right">备&nbsp;&nbsp;注：</div></td>
                                <td colspan="3"><textarea rows="4" cols="30" style="width:444px" name="salEmp.seRemark" onBlur="autoShort(this,4000)">${salEmp.seRemark }</textarea></td>
                            </tr>
                        </table>
                    </div>
                    <div style="margin-left:5px;margin-right:5px;padding:6px;" class="orangeBack">
                        <table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        		<tr><td style="text-align:center"><input class="butSize3" type="button" id="sub2" onClick="checkEmpNum()" value="保存员工信息"/></td></tr>
                        </table>
                    </div>
                    <div class="tipsLayer" style="margin:5px 0px 0px 5px; width:675px;">
                        <ul>
                            <li>1.员工照片上传成功后即已保存，无须点击保存按钮。</li>
                            <li>2.如员工已分配了使用账号则职位修改范围只能在大于上级账号职级小于等于当前使用账号职级之间，如需其它修改要先修改当前使用账号。</li>
                        </ul>
                    </div>
                </form>
          </div>
        </div>
    </div>
    </logic:notEmpty>
	<logic:empty name="salEmp">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该员工资料已被删除</div>
	</logic:empty>
  </body>
</html>
