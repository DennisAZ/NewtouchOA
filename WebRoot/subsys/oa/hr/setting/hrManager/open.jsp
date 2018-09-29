<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">



</script>
</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/images/send.gif" width="18" HEIGHT="18"><span class="big3"> 设置公开字段</span>
    	<span class="small1"> 注意：字段设置为公开后，在查看在线人员列表时，点击姓名即可看到，请谨慎设置！</span>
    </td>
  </tr>
</table>
<form action="open_submit.php" method="post" name="form1">
  	<table width="200" align="center" border="1" cellspacing="0" cellpadding="3"  bordercolorlight="#000000" bordercolordark="#FFFFFF" class="TableBlock">
      <tr bgcolor="#CCCCCC">
     <td align="center">排序</td>
    <td align="center"><b>公开字段</b></td>
    <td align="center">&nbsp;</td>
    <td align="center" valign="top"><b>可选字段</b></td>
  </tr>
  <tr>
  	<td align="center" bgcolor="#999999">
      <input type="button" class="SmallInput" value=" ↑ " onClick="func_up();">
      <br><br>
      <input type="button" class="SmallInput" value=" ↓ " onClick="func_down();">
    </td>
    <td valign="top" align="center" bgcolor="#CCCCCC">
    <select  name="select1" ondblclick="func_delete();" MULTIPLE style="width:200;height:320">
<option value="STAFF_POLITICAL_STATUS">政治面貌</option>    </select>
    <input type="button" value=" 全 选 " onClick="func_select_all1();" class="SmallInput">
    </td>
    <td align="center" bgcolor="#999999">
      <input type="button" class="SmallInput" value=" ← " onClick="func_insert();">
      <br><br>
      <input type="button" class="SmallInput" value=" → " onClick="func_delete();">
    </td>
    <td align="center" valign="top" bgcolor="#CCCCCC">
    <select  name="select2" ondblclick="func_insert();" MULTIPLE style="width:200;height:320">
<option value="STAFF_E_NAME">英文名</option><option value="STAFF_SEX">性别</option><option value="STAFF_NO">编号</option><option value="WORK_NO">工号</option><option value="STAFF_CARD_NO">身份证号码</option><option value="STAFF_BIRTH">出生日期</option><option value="STAFF_AGE">年龄</option><option value="STAFF_NATIVE_PLACE">籍贯</option><option value="STAFF_NATIONALITY">民族</option><option value="STAFF_MARITAL_STATUS">婚姻状况</option><option value="WORK_STATUS">在职状态</option><option value="JOIN_PARTY_TIME">入党时间</option><option value="STAFF_PHONE">联系电话</option><option value="STAFF_MOBILE">手机号码</option><option value="STAFF_LITTLE_SMART">小灵通</option><option value="STAFF_MSN">MSN</option><option value="STAFF_QQ">QQ</option><option value="STAFF_EMAIL">电子邮件</option><option value="HOME_ADDRESS">家庭地址</option><option value="JOB_BEGINNING">参加工作时间</option><option value="OTHER_CONTACT">其他联系方式</option><option value="WORK_AGE">总工龄</option><option value="STAFF_HEALTH">健康状况</option><option value="STAFF_DOMICILE_PLACE">户口所在地</option><option value="STAFF_TYPE">户口类别</option><option value="DATES_EMPLOYED">入职时间</option><option value="STAFF_HIGHEST_SCHOOL">学历</option><option value="STAFF_HIGHEST_DEGREE">学位</option><option value="GRADUATION_DATE">毕业时间</option><option value="STAFF_MAJOR">专业</option><option value="GRADUATION_SCHOOL">毕业院校</option><option value="COMPUTER_LEVEL">计算机水平</option><option value="FOREIGN_LANGUAGE1">外语语种1</option><option value="FOREIGN_LANGUAGE2">外语语种2</option><option value="FOREIGN_LANGUAGE3">外语语种3</option><option value="FOREIGN_LEVEL1">外语水平1</option><option value="FOREIGN_LEVEL2">外语水平2</option><option value="FOREIGN_LEVEL3">外语水平3</option><option value="STAFF_SKILLS">特长</option><option value="WORK_TYPE">工种</option><option value="ADMINISTRATION_LEVEL">行政级别</option><option value="STAFF_OCCUPATION">员工类型</option><option value="JOB_POSITION">职务</option><option value="PRESENT_POSITION">职称</option><option value="JOB_AGE">本单位工龄</option><option value="BEGIN_SALSRY_TIME">起薪时间</option><option value="LEAVE_TYPE">年休假</option><option value="RESUME">简历</option><option value="SURETY">担保记录</option><option value="CERTIFICATE">职务情况</option><option value="INSURE">社保缴纳情况</option><option value="BODY_EXAMIM">体检记录</option><option value="PHOTO_NAME">照片</option><option value="REMARK">备 注</option>    </select>
    <input type="button" value=" 全 选 " onClick="func_select_all2();" class="SmallInput">
    </td>
  </tr>
    <tfoot align="center" class="TableFooter">
      <td nowrap colspan="4" align="center">
        <input type="button" value="确定" class="BigButton" onClick="exreport()">
        <input type="hidden" name="FIELDMSG">
        <input type="hidden" name="FIELDMSGNAME">
      </td>
    </tfoot>
    </table>
</form>



</body>
</html>