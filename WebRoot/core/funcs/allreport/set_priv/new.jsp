<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String rId=request.getParameter("rid");
String fId = request.getParameter("fId");
%>
<html>
<head>
<title>设置报表权限</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowreport/js/report.js"></script>

<script Language="JavaScript">

function CheckForm()
{
   if(document.form1.COPY_TO_ID.value=="")
   { alert("人员不能为空！");
     return (false);
   }

   /*if(document.form1.DEPT.value=="OTHER" && document.form1.TO_ID.value=="")
   { alert("所管部门不能为空！");
     return (false);
   }*/
   $("userId").value=document.form1.COPY_TO_ID.value;
   /*var dept=document.form1.DEPT.value;
   if(dept=="OTHER"){
      $("deptvalue").value=document.form1.TO_ID.value;
   }else {
	 $("deptvalue").value=dept;
   }*/
   return (true)
}

function change_dept(val)
{
  if(val == "OTHER")
  {
    document.getElementById("sel_dept").style.display = "";
    document.form1.TO_ID.value="";
    document.form1.TO_NAME.value="";
  }
  else 
  {
    document.getElementById("sel_dept").style.display = "none";
  }
 // parent.frm_resize();
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 设置报表权限</span>
    </td>
  </tr>
</table>

<form action="<%=contextPath %>/yh/core/funcs/allreport/act/YHReportPrivAct/addReportPrivAct.act?rid=<%=rId%>" enctype="multipart/form-data"  method="post" name="form1" onSubmit="return CheckForm();">
<table class="TableBlock"  width="500" align="center" >
   <!--tr>
     <td nowrap class="TableData">部门范围</td>
     <td class="TableData">
    <select name="DEPT" class="BigSelect" id="DEPT" onChange="change_dept(this.value)">
      <option value="ALL_DEPT" >全体部门</option>
      <option value="DEPT" >本部门</option>
      <option value="OTHER">指定部门</option>
    </select> 
     </td>
   </tr-->
   <!--tr id='sel_dept' style="display:none;">
     <td nowrap class="TableData">部门：</td>
     <td class="TableData">
       <input type="hidden" id="TO_ID" name="TO_ID" value="">
     <textarea cols=30 name="TO_NAME"  id="TO_NAME"  rows=4 class="BigStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectDept(['TO_ID','TO_NAME'])">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('TO_ID').value='' , $('TO_NAME').innerHTML = ''">清空</a>
     </td>
   </tr-->
   <tr>
      <td nowrap class="TableData">人员：</td>
      <td class="TableData">
        <input type="hidden" name="COPY_TO_ID" id="COPY_TO_ID" value="">
        <textarea cols=30 name="COPY_TO_NAME" id="COPY_TO_NAME" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['COPY_TO_ID' , 'COPY_TO_NAME'])">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('COPY_TO_ID').value='' , $('COPY_TO_NAME').innerHTML = ''">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" name="userId" id="userId" value=""/>
      <input type="hidden" name="rid" id="rid"  value="<%=rId%>"/>
      <!--input type="hidden" name="deptvalue"  id="deptvalue" value=""/-->
        <input type="submit" value="确定" class="BigButton" name="button"/>&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location.href = 'index.jsp?rId=<%=rId %>'"/>
    </td>
   </tr>
  
</table>
</form>
</body>
</html>
