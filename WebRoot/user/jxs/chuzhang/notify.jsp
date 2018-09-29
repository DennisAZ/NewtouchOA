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
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b>  待批通知单</b></td>
  </tr>
</table>

<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      通知单号
      </td>
       <td>
          拟制时间
      </td>

      <td>
      装备名称
      </td>
      <td>
      承修部门
      </td>
      <td>
      操作
      </td>
    </tr>
    <tr class="TableLine1">
      <td>
      20110321001
      </td>
      <td>
      2011年03月21日
      </td>
      <td>
      东风汽车
      </td>
      <td>部门3</td>
      <td>
      <a href="javascript:void(0)" onclick="openProject()">审批</a>&nbsp;
      <a href="javascript:void(0)" onclick="readNotify()">查看通知单</a>
      </td>
    </tr>
    <tr class="TableLine2">
      <td>
      20110317002
      </td>
      <td>
      2011年03月17日
      </td>
      <td>
       坦克
      </td>
      <td>
     部门2
      </td>
      <td>
      <a href="javascript:void(0)" onclick="openProject()">审批</a>&nbsp;
      <a href="javascript:void(0)" onclick="readNotify()">查看通知单</a>&nbsp;
      </td>
    </tr>
     <tr class="TableLine1">
      <td>
      20110316001
      </td>
      <td>
      2011年03月16日
      </td>
      <td>
      步兵战车
      </td>
      <td> 部门1
      </td>
      <td>
      <a href="javascript:void(0)" onclick="openProject()">审批</a>&nbsp;
      <a href="javascript:void(0)" onclick="readNotify()">查看通知单</a>
      </td>
    </tr>
    </table>
</body>
</html>