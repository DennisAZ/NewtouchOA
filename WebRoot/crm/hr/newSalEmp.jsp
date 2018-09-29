<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>员工资料录入</title>
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
	  
	  	function checkEmpNum(obj){
			if(obj != undefined){
				autoShort(obj,25);
				checkIsRepeat(obj,'empAction.do?op=checkSecode&seCode=','${salEmp.seCode}');
			}
	  		else{
				autoShort($('seCode'),25);
				checkRepeatForm(new Array('empAction.do?op=checkSecode&seCode=','seCode'));
			}
	 	}
	  	
		createProgressBar();
   		window.onload=function(){
			closeProgressBar();
  		}
  	</script>
  </head>
  
  <body>
  <div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="images/content/fail.gif" alt="警告">&nbsp;此员工号在系统(包括回收站)中已存在</div>
  	<div id="mainbox">
    	<div id="contentbox">
            <div class="descInf" style="width:680px">
            	<div id="descTop">员工信息录入</div>
            	<form action="empAction.do" id="creSalEmp" method="post" style="margin:0px">
                	<input type="hidden" name="op" value="newSalEmp">
                    <input type="hidden" id="isRepeat" />
                    <div style="margin-left:5px;margin-right:5px;padding:6px;" class="orangeBack">
                        <table class="normal noBr" width="100%">
                        		<tr><td style="text-align:center"><input class="butSize3" type="button" id="sub1" onClick="checkRepeatForm(new Array('empAction.do?op=checkSecode&seCode=','seCode'))" value="保存员工信息"/></td></tr>
                        </table>
                    </div>
                    <div class="grayTab" style="text-align:center" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                    	<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;基本信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right"><span class='red'>*</span>姓名：</div></td>
                                <td><input class="inputSize2" type="text" id="seName" name="salEmp.seName" onBlur="autoShort(this,25)"/></td>
                                <td width="8%"><div align="right"><span class='red'>*</span>员工号：</div></td>
                                <td><input class="inputSize2" type="text" id="seCode" name="salEmp.seCode" onBlur="checkEmpNum(this)"/></td>
                                <td>&nbsp;</td>
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
                            	<td><div align="right"><span class='red'>*</span>职位：</div></td>
                                <td>
                                    <select id="jobLev" name="seJobLev" class="inputSize2" >
                                        <option value="">请选择</option>
                                        <logic:notEmpty name="roleList">
                                           <logic:iterate id="roleList" name="roleList">
                                           <option value="${roleList.rolId}">${roleList.rolName}</option>
                                           </logic:iterate>
                                        </logic:notEmpty>
                                    </select>
                                </td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right"><span class='red'>*</span>在职情况：</div></td>
                                <td>
                                    <div align="left">
                                    <input type="radio" name="salEmp.seRap" id="rap1" value="在职" checked><label for="rap1">在职&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    <input type="radio" name="salEmp.seRap" id="rap2" value="离职"><label for="rap2">离职&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    </div>
                                </td>
                                <td><div align="right">性别：</div></td>
                                <td><input  type="radio" name="salEmp.seSex" id="sex1" value="男" checked><label for="sex1">男&nbsp;&nbsp;&nbsp;&nbsp;</label>
                                    <input  type="radio" name="salEmp.seSex" id="sex2" value="女"><label for="sex2">女&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">成本中心：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seCostCenter" onBlur="autoShort(this,25)"/></td>
                                <td><div align="right">最高学历：</div></td>
                                <td><input class="inputSize2"   type="text" name="salEmp.seEdu" onBlur="autoShort(this,50)"></td>
                                <td>&nbsp;</td>
                            </tr>
                   		</table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;公司信息&nbsp;&nbsp;</div></th>
                                <td width="10%"><div align="right">员工类型：</div></td>
                                <td >
                                    <select name="salEmp.seType" class="inputSize2" >
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
                                    <select name="salEmp.seJobCate"  class="inputSize2">
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
                                <td><input class="inputSize2"  type="text"  name="salEmp.seJobTitle" onBlur="autoShort(this,50)"/></td>
                                <td><div align="right">招聘来源：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seRecSource" onBlur="autoShort(this,100)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">入职日期：</div></td>
                                <td><input class="inputSize2 Wdate" style="cursor:pointer" type="text"  name="jobDate" readonly="readonly"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                                <td><div align="right">试用期(月)：</div></td>
                                <td><input class="inputSize2"  type="text" id="prob" name="salEmp.seProb" onBlur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">离职日期：</div></td>
                                <td><input class="inputSize2 Wdate" style="cursor:pointer" type="text"  name="endDate" readonly="readonly"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                            </tr>
                        </table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;薪酬信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right">本公司年资：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seYearPay" onBlur="autoShort(this,50)"/></td>
                                <td width="8%"><div align="right">社保号：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seSocialCode" onBlur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                          	</tr>
                            <tr>
                            	<td>&nbsp;</td>
								<td><div align="right">公积金号：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seProvFund" onBlur="autoShort(this,50)"/></td>
                                <td><div align="right">其他福利：</div></td>
                                <td><input class="inputSize2"  type="text" name="salEmp.seWealAddress" onBlur="autoShort(this,50)"/></td>	
                                <td>&nbsp;</td>
                           </tr>
                           <tr>
                           		<td>&nbsp;</td>
                                <td><div align="right">开户银行：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seBankName" onBlur="autoShort(this,50)"/></td>
                                <td><div align="right">银行账号：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seBankCard" onBlur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                        </table>
                   </div>
                   
                   <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;个人信息&nbsp;&nbsp;</div></th>
                                <td width="8%"><div align="right">身份证号：</div></td>
                                <td ><input class="inputSize2"  type="text"  name="salEmp.seIdeCode" onBlur="autoShort(this,20)" /></td>
                                <td width="8%" ><div align="right">出生日期：</div></td>
                                <td ><input class="inputSize2 Wdate" style="cursor:pointer" type="text"  name="salEmp.seBirth"  readonly="readonly"  onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">婚姻状况：</div></td>
                                <td>
                                    <input  type="radio" name="salEmp.seMarry" id="mar1" value="已婚"><label for="mar1">已婚&nbsp;</label>
                                    <input  type="radio" name="salEmp.seMarry" id="mar2" value="未婚" checked><label for="mar2">未婚</label>
                                </td>
                                <td><div align="right">政治面貌：</div></td>
                                <td>
                                    <select name="salEmp.sePoliStatus" class="inputSize2" >
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
                                <td><input class="inputSize2"  type="text"  name="salEmp.seNation" onBlur="autoShort(this,25)" /></td>
                                <td><div align="right">籍贯：</div></td>
                                <td><input class="inputSize2"  type="text" name="salEmp.seBirPlace" onBlur="autoShort(this,50)"/></td>
                                <td>&nbsp;</td>
                            </tr>
							<tr>
                            	<td>&nbsp;</td>
                            	<td><div align="right">户口所在地：</div></td>
                                <td><input class="inputSize2"  type="text"  name="salEmp.seAccPlace" onBlur="autoShort(this,100)"/></td>
                                <td><div align="right">户籍性质：</div></td>
                                <td>
                                    <select name="salEmp.seHouReg"  class="inputSize2">
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
                                <td colspan="3"><textarea rows="4" cols="30"  style="width:444px" name="salEmp.seEdcBac" onBlur="autoShort(this,4000)"></textarea></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr><tr/>
                            <tr>
	                           	<th>&nbsp;</th>
	                            <td align="right" valign="top"><div align="right">工作经历：</div></td>
	                            <td colspan="3"><textarea rows="4" cols="30" style="width:444px" name="salEmp.seWorkEx" onBlur="autoShort(this,4000)"></textarea></td>
	                            <td>&nbsp;</td>
                           </tr>
                        </table>
                    </div>
                    
                    <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                   		<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th valign="top"><div align="center">&nbsp;&nbsp;联系方式&nbsp;&nbsp;</div></th>
                                <td width="11%"><div align="right">手机：</div></td>
                                <td ><input  class="inputSize2" type="text" name="salEmp.sePhone" onBlur="autoShort(this,25)"></td>
                                <td width="11%"><div align="right">电话：</div></td>
                                <td ><input  class="inputSize2" type="text"  name="salEmp.seTel" onBlur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                         	<tr> 
                            	<td>&nbsp;</td>
                                <td width="11%"><div align="right">QQ：</div></td>
                                <td><input  class="inputSize2" type="text"  name="salEmp.seQq"  onblur="autoShort(this,25)"/></td>
                                <td width="11%"><div align="right">MSN：</div></td>
                                <td><input  class="inputSize2" type="text" name="salEmp.seMsn" onBlur="autoShort(this,50)"></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td width="11%"><div align="right">电子邮件：</div></td>
                                <td><input  class="inputSize2" type="text"  name="salEmp.seEmail" onBlur="autoShort(this,50)"/></td>
                                <td width="11%"><div align="right">居住地址：</div></td>
                                <td><input  class="inputSize2" type="text" name="salEmp.seAddress" onBlur="autoShort(this,500)"/></td>
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
                                	<select name="salEmp.seAttendance" class="inputSize2">
                                 		<option value="">请选择</option>
                                 		<option value="正常打卡">正常打卡</option>
                                 		<option value="免卡人员">免卡人员</option>
                                 		<option value="弹性考勤">弹性考勤</option>
                                 	</select>
                                </td>
                                <td width="8%"><div align="right">卡号：</div></td>
                                <td ><input  class="inputSize2" type="text"  name="salEmp.seCardNum" onBlur="autoShort(this,25)"/></td>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                            	<td>&nbsp;</td>
                                <td><div align="right">允许打卡加班：</div></td>
                                <td><input  type="radio" name="salEmp.seIsovertime" id="isOver1" value="否" checked><label for="isOver1">否&nbsp;&nbsp;</label>&nbsp;<input  type="radio" name="salEmp.seIsovertime" id="isOver2" value="是"><label for="isOver2">是</label></td>
                                <td colspan="3">&nbsp;</td>
                             </tr>
                       	</table>
                    </div>
                    <div class="grayTab" onMouseOver="this.className='orangeBoxBorder grayTab'" onMouseOut="this.className='grayTab'">
                    	<table class="normal noBr" width="100%" border="0" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th style="vertical-align:top;"><div align="center">&nbsp;&nbsp;其他信息&nbsp;&nbsp;</div></th>
                                <td style="width:12%;vertical-align:top;"><div align="right">备&nbsp;&nbsp;注：</div></td>
                                <td colspan="3"><textarea rows="4" cols="30" style="width:444px" name="salEmp.seRemark" onBlur="autoShort(this,4000)"></textarea></td>
                                <td>&nbsp;</td>
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
                            <li>保存成功后，可在编辑员工资料里上传员工照片。</li>
                        </ul>
                    </div>
                </form>
          </div>
        </div>
    </div>
  </body>
</html>
