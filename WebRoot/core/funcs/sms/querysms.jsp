<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信查询</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript">
function send(){
  var opType = $('operationType').value;
  if(checkDateTime("startDate") == false){
    $("startDate").focus();
    $("startDate").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  if(checkDateTime("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形如：2010-10-10 12:12:12");
    return;
  }
  if( !opType ||  opType == 1 ){
      //查询
      doSearch('form1');
   }else if(opType == 2){
     doExport('form1');
   } else if(opType == 3){
     alert('删除功能正在完善中！');
     return;
   }
}
/**
 * 查询
 */
function doSearch(form){
  var userType = $('userType').value;
  var param = "";
  var url = null;
  if(form){
    param = $(form).serialize();
  }
  if(userType == 1){//1代表收件箱查询，2代表发件箱查询
    url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/acceptedSms.act?pageNo=0&pageSize=100&queryType=1&";
  }else {
    url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsTestAct/sentSmsList.act?pageNo=0&pageSize=100&queryType=1&";
  }
  location = url + param;
}
function doExport(form){
  var userType = $('userType').value;
  var param = "";
  if(form){
    param = $(form).serialize();
  }
  var  url = contextPath + "/yh/core/funcs/sms/act/YHSmsTestAct/exportExcel.act?";
  location = url + param;
}
function setOpType(type){
  $('operationType').value = type;
}
function doInit(){
  showCalendar("startDate", true, "startDateImg");
  showCalendar("endDate", true, "endDateImg");
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/infofind.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3"> 提醒查询</span>
    </td>
  </tr>
</table>
 <form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">
    <tr>
      <td nowrap class="TableData">
         <select id="userType" name="userType">
            <option value="1">发送人</option>
            <option value="2">收信人</option>
         </select>
      </td>
      <td class="TableData">
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols=23 name="userIdDesc" id="userIdDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('userId','userIdDesc')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">类型：</td>
      <td class="TableData">
      <select name="smsType">
         <option value="">所有类型</option>
         <option value="0">个人短信</option>
         <option value="1">公告通知</option>
         <option value="2">内部邮件</option>
         <option value="3">网络会议</option>
         <option value="4">工资上报</option>
         <option value="5">日程安排</option>
         <option value="6">考勤批示</option>
         <option value="7">工作流:提醒下一步经办人</option>
         <option value="40">工作流:提醒流程发起人</option>
         <option value="41">工作流:提醒流程所有人员</option>
         <option value="8">会议申请</option>
         <option value="9">车辆申请</option>
         <option value="10">手机短信</option>
         <option value="11">投票提醒</option>
         <option value="12">工作计划</option>
         <option value="13">工作日志</option>
         <option value="14">新闻</option>
         <option value="15">考核</option>
         <option value="16">公共文件柜</option>
         <option value="17">网络硬盘</option>
         <option value="18">内部讨论区</option>
         <option value="19">工资条</option>
         <option value="20">个人文件柜</option>
         <option value="22">审核提醒</option>
         <option value="23">即时通讯离线消息</option>
         <option value="24">上线提醒</option>
         <option value="30">培训课程</option>
         <option value="31">课程报名</option>
         <option value="32">培训调查</option>
         <option value="33">培训信息</option>
         <option value="35">销售合同提醒</option>
         <option value="34">效果评估</option>
         <option value="42">项目管理</option>
         <option value="37">档案管理</option>
         <option value="43">办公用品审批</option>
         <option value="44">网络传真</option>
         <option value="45">日程安排-周期性事务</option>
         <option value="a0">报表提示</option>
       </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">起始日期：</td>
      <td class="TableData"><input type="text" name="startDate" id="startDate" size="20" maxlength="20" class="BigInput" value="">
          <img src="<%=imgPath%>/calendar.gif" id="startDateImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">截止日期：</td>
      <td class="TableData"><input type="text" name="endDate" id="endDate" size="20" maxlength="20" class="BigInput" value="">
          <img src="<%=imgPath%>/calendar.gif" id="endDateImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols=33 name="content" rows="2" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">排序字段：</td>
      <td class="TableData">
        <select name="orderBy">
            <option value="SMS_TYPE">类型</option>
            <option value="SEND_TIME" selected>发送时间</option>
         </select>
         <select name="orderBySeq">
            <option value="DESC">降序</option>
            <option value="ASC">升序</option>
         </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">操作：</td>
      <td class="TableData">
        <input type="hidden" value="" id="operationType"></input>
        <input type="radio" name="operation"  value="1" checked onclick="setOpType(1);"><label for="OPERATION1">查询</label>&nbsp;
        <input type="radio" name="operation"  value="2" onclick="setOpType(2);"><label for="OPERATION2">导出</label>&nbsp;
       <%--<input type="radio" name="operation"  value="3" onclick="setOpType(3);"><label for="OPERATION3">删除</label> --%> 
      </td>
    </tr>
    <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="确定" class="BigButton" onclick="send()">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>