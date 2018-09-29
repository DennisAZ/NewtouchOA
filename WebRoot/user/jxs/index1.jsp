<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知单</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function openProject() {
  openDialog("readNotify.jsp?flag=1",  600, 400);
}
function openPlan() {
  location.href = "repairPlan.jsp";
}
function readNotify() {
  openDialog("../zhuliyuan/readNotify.jsp",  600, 400);
}

</script>
</head>
<body>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b>  修理情况</b></td>
  </tr>
</table>

<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      通知单号
      </td>
       <td>
        装备类型
      </td>

      <td>
      装备名称
      </td>
      <td>
      开始时间

      </td>
      <td>
     修理类型
      </td>
      <td>
      修理项

      </td>
       <td>
      修理时长
      </td>
    </tr>
    <tr class="TableLine1">
        <td>
      20110123213
      </td>
       <td>
        雷达
      </td>

      <td>
      雷达1
      </td>
      <td>
      2011-03-27
      </td>
      <td>
     大修
      </td>
      <td>
         8项

      </td>
       <td>
      两天
      </td>
    </tr>
    <tr class="TableLine2">
             <td>
      20110123213
      </td>
       <td>
        雷达
      </td>

      <td>
      雷达1
      </td>
      <td>
      2011-03-27
      </td>
      <td>
     大修
      </td>
      <td>
         1项

      </td>
       <td>
      两天
      </td>
    </tr>
     <tr class="TableLine1">
             <td>
      20110123213
      </td>
       <td>
        雷达
      </td>

      <td>
      雷达1
      </td>
      <td>
      2011-03-27
      </td>
      <td>
     大修
      </td>
      <td>
      5项

      </td>
       <td>
      两天
      </td>
    </tr>
    </table>
</body>
</html>
