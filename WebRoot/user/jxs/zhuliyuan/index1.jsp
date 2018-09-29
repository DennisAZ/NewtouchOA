<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检测记录</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/javascript">
function writeRecord() {
  formView(49, 570);
}
function openProject() {
  formView(46 , 569);
}
function createLianSou(number , name , dept , isNew) {
  if (isNew) {
    var par = "维修通知单号=" + number + "&装备名称=" + name + "&承修部门="  + dept;
    createNewWork('装备验收流程'  , par , true );
  } else {
    location.href = contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId=50&flowId=570&prcsId=1&flowPrcs=1&sortId=&skin=&isWriteLog=0"
  }
  
}

</script>
</head>
<body>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 修理计划</b></td>
  </tr>
</table>
<table  width="100%" class="TableList">
    <tr class="TableHeader">
      <td>
      送修通知单号
      </td>
       <td>
          送修时间
      </td>
      <td>
      装备名称
      </td>
      <td>
      承修部门
      </td>
      <td>状态</td>
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
      <td>未拟定</td>
      <td>
      <a href="javascript:void(0)" onClick="openProject()">查看修理计划</a>&nbsp;
      <a href="#" onClick="createLianSou('20110321001' , '东风汽车' , '部门3',true)">拟定验收方案</a>
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
      <td><a style="color:#FFFF00" >审批中</a></td>
      <td>
      <a href="javascript:void(0)" onClick="openProject()">查看修理计划</a>&nbsp;
      <a href="#" onClick="writeRecord()">查看验收方案</a>
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
      战车
      </td>
      <td> 部门1
      </td>
	   <td>未拟定</td>
      <td>
      <a href="javascript:void(0)" onClick="openProject()">查看修理计划</a>&nbsp;
      <a href="#" onClick="createLianSou('20110321001' , '东风汽车' , '部门3')">拟定验收方案</a>
      </td>
    </tr>
    </table>
</body>
</html>