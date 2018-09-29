<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>员工详细资料</title>
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
			white-space:nowrap;  
		}
		.grayTab td{
			height:25px;
			word-break : break-all;/*自动换行*/	
		}
		
		.grayBack {
			border:#a4b0c1 1px solid;
			background-color:#e9eff6;
		}
		
		.descKey{
			font-weight:600;
			color:#20438b;
			background:none;
			border-top:0px;
			border-bottom:#ccc 1px dotted;
		}
    </style>
     <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/hr.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript">
		function initemp(){
			//压缩图片
			reSizePic("empPic",195,200);
			$("jobDate").innerHTML='${salEmp.seJobDate}'.substring(0,10);
			$("seBirth").innerHTML='${salEmp.seBirth}'.substring(0,10);
            $("endDate").innerHTML='${salEmp.seEndDate}'.substring(0,10);
		}
	
		
		createProgressBar();
		window.onload=function(){
			if('${salEmp}'!=null&&'${salEmp}'!=''){
				initemp();
			}
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<logic:notEmpty name="salEmp">
  	<div id="mainbox">
    	<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">员工详细资料&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
                <div class="grayTab" style="text-align:center" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal" width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;基本信息&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right">姓名：</div></td>
                            <td width="35%" class="descKey">${salEmp.seName}&nbsp;</td>
                            <td colspan="3" rowspan="8" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;
                                <logic:notEmpty name="salEmp" property="sePic">
                                    <img id="empPic" src="${salEmp.sePic}" alt="点击查看原图" style="cursor:pointer;border:#999999 2px solid;" onClick="window.open('${salEmp.sePic}')"/>
                                </logic:notEmpty>
                                <logic:empty name="salEmp" property="sePic">
                                    <img id="empPic" src="images/content/noImg.png"/>
                                </logic:empty>
                                    
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="8%"><div align="right">员工号：</div></td>
                            <td width="35%" class="descKey">${salEmp.seCode}&nbsp;</td>
                            
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right"><nobr>所属部门：</nobr></div></td>
                            <td class="descKey">${salEmp.salOrg.soName}&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">职位：</div></td>
                            <td class="descKey">${salEmp.limRole.rolName}&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">在职情况：</div></td>
                            <td class="descKey">${salEmp.seRap}&nbsp;</td>
                       </tr>
                       <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">性别：</div></td>
                            <td class="descKey">${salEmp.seSex}&nbsp;</td>
                            
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">成本中心：</div></td>
                            <td class="descKey">${salEmp.seCostCenter}&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">最高学历：</div></td>
                            <td class="descKey">${salEmp.seEdu}&nbsp;</td>
                        </tr>
                    </table>
               </div>
               <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal" width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;公司信息&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right"><nobr>员工类型：</nobr></div></td>
                            <td width="35%" class="descKey">${salEmp.seType}&nbsp;</td>	
                            <td width="8%"><div align="right">职类：</div></td>
                            <td width="35%" class="descKey">${salEmp.seJobCate}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">职称：</div></td>
                            <td class="descKey">${salEmp.seJobTitle}&nbsp;</td>
                            <td><div align="right">招聘来源：</div></td>
                            <td class="descKey">${salEmp.seRecSource}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">入职日期：</div></td>
                            <td class="descKey"><span id="jobDate"></span>&nbsp;</td>
                            <td><div align="right"><nobr>试用期(月)：</nobr></div></td>
                            <td class="descKey">${salEmp.seProb}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">离职日期：</div></td>
                            <td class="descKey"><span id="endDate"></span>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
               </div>
               <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal " width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;薪酬信息&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right"><nobr>本公司年资：</nobr></div></td>
                            <td width="35%" class="descKey">${salEmp.seYearPay}&nbsp;</td>
                            <td width="8%"><div align="right">社保号：</div></td>
                            <td width="35%" class="descKey">${salEmp.seSocialCode}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">公积金号：</div></td>
                            <td class="descKey">${salEmp.seProvFund}&nbsp;</td>
                            <td><div align="right"><nobr>其他福利：</nobr></div></td>
                            <td class="descKey">${salEmp.seWealAddress}&nbsp;</td>	
                            <td>&nbsp;</td>
                       </tr>
                       <tr>
                            <td>&nbsp;</td>
                            <td><div align="right">开户银行：</div></td>
                            <td class="descKey">${salEmp.seBankName }&nbsp;</td>
                            <td><div align="right">银行账号：</div></td>
                            <td class="descKey">${salEmp.seBankCard }&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
               </div>
               <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal " width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;个人信息&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right">身份证号：</div></td>
                            <td width="35%" class="descKey">${salEmp.seIdeCode}&nbsp;</td>
                            <td width="8%"><div align="right"><nobr>出生日期：</nobr></div></td>
                            <td width="35%" class="descKey"><span id="seBirth"></span>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="8%"><div align="right">婚姻状况：</div></td>
                            <td width="35%" class="descKey">${salEmp.seMarry}&nbsp;</td>
                            <td width="8%"><div align="right">政治面貌：</div></td>
                            <td width="35%" class="descKey">${salEmp.sePoliStatus}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="8%"><div align="right">民族：</div></td>
                            <td width="35%" class="descKey">${salEmp.seNation}&nbsp;</td>
                            <td width="8%"><div align="right">籍贯：</div></td>
                            <td width="35%" class="descKey">${salEmp.seBirPlace}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td width="8%"><div align="right"><nobr>户口所在地：</nobr></div></td>
                            <td width="35%" class="descKey">${salEmp.seAccPlace}&nbsp;</td>
                            <td width="8%"><div align="right">户籍性质：</div></td>
                            <td width="35%" class="descKey">${salEmp.seHouReg}&nbsp;</td>	
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td width="8%"><div align="right"><nobr>教育背景：</nobr></div></td>
                            <td colspan = "3" class="descKey">${salEmp.seEdcBac}&nbsp;</td>
                            <td></td>
                        </tr>
                        <tr>
                            <th>&nbsp;</th>
                            <td width="8%"><div align="right"><nobr>工作经历：</nobr></div></td>
                            <td colspan="3" class="descKey">${salEmp.seWorkEx}&nbsp;</td>
                            <td></td>
                        </tr>
                    </table>
                </div>
                <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal " width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;联系方式&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right">手机：</div></td>
                            <td width="35%" class="descKey">${salEmp.sePhone}&nbsp;</td>
                            <td width="8%"><div align="right">电话：</div></td>
                            <td width="35%" class="descKey">${salEmp.seTel}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr> 
                            <td>&nbsp;</td>
                            <td><div align="right">QQ：</div></td>
                            <td class="descKey"><logic:notEmpty name="salEmp" property="seQq"><img src="images/content/qq.gif"  title="点击开始qq对话"/><a href="javascript:void(0)" onClick="qqTo('<c:out value="${salEmp.seQq}"/>');return false;">${salEmp.seQq}</a></logic:notEmpty>&nbsp;</td>
                            <td><div align="right">MSN：</div></td>
                            <td class="descKey"><logic:notEmpty name="salEmp" property="seMsn"><img src="images/content/msn.gif"  title="点击开始msn对话"/><a href="javascript:void(0)" onClick="msnTo('<c:out value="${salEmp.seMsn}"/>');return false;">${salEmp.seMsn}</a></logic:notEmpty>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right"><nobr>电子邮件：</nobr></div></td>
                            <td class="descKey"><logic:notEmpty name="salEmp" property="seEmail"><img src="images/content/email.gif" title="点击发送电子邮件"/><a href="javascript:void(0)" onClick="mailTo('<c:out value="${salEmp.seEmail}"/>');return false;">${salEmp.seEmail}</a></logic:notEmpty>&nbsp;</td>
                            <td><div align="right"><nobr>居住地址：</nobr></div></td>
                            <td class="descKey">${salEmp.seAddress }&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        
                    </table>
               </div>
               <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal " width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;考勤信息&nbsp;&nbsp;</div></th>
                            <td width="8%"><div align="right">考勤方式：</div></td>
                            <td width="35%" class="descKey">${salEmp.seAttendance}&nbsp;</td>
                            <td width="8%"><div align="right"><nobr>卡号：</nobr></div></td>
                            <td width="35%" class="descKey">${salEmp.seCardNum}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><div align="right"><nobr>允许打卡加班：</nobr></div></td>
                            <td class="descKey">${salEmp.seIsovertime}&nbsp;</td>
                            <td colspan="3">&nbsp;</td>
                         </tr>
                    </table>
                </div>
                <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal " width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center">&nbsp;&nbsp;其他信息&nbsp;&nbsp;</div></th>
                            <td width="8%" valign="top"><div align="right"><nobr>备&nbsp;&nbsp;注：</nobr></div></td>
                            <td width="78%" class="descKey">${salEmp.seRemark }&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>
                 <div class="grayTab" onMouseOver="this.className='grayBack grayTab'" onMouseOut="this.className='grayTab'">
                    <table class="normal" width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <th valign="top"><div align="center" class="orange">&nbsp;&nbsp;账号信息&nbsp;&nbsp;</div></th>
                            
                            <td width="8%"><div align="right" class="blue"><nobr>当前账号：</nobr></div></td>
                            <td width="78%" class="descKey blue">${salEmp.seUserCode}&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td style="vertical-align:top"><div align="right" class="blue">使用记录：</div></td>
                            <td colspan="2" class="descKey gray">${salEmp.seLog}&nbsp;</td>
                        </tr>
                    </table>
               </div>
                <div class="descStamp">
                    &nbsp;&nbsp;由
                    <span>${salEmp.seInserUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入
                    <logic:notEmpty name="salEmp" property="seAltUser">，最近由
                    <span>${salEmp.seAltUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;修改
                    </logic:notEmpty>
               </div>
			   <script type="text/javascript">
                     removeTime('inpDate','${salEmp.seInserDate}',2);
                     removeTime("changeDate","${salEmp.seAltDate}",2);
                </script>
          </div>
        </div>
    </div>
    </logic:notEmpty>
	<logic:empty name="salEmp">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该员工资料已被删除</div>
	</logic:empty>
  </body>
</html>
