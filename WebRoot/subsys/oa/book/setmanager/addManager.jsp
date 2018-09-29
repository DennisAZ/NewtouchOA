<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<%
  YHBookManager manager = (YHBookManager)request.getAttribute("manager");
  boolean flag = false;
  String names = "";
  String manageIds = "";
  String deptNames = "";
  String deptIds = "";
  int seqId = 0;
  if(manager != null){
    flag = true;
    names = manager.getManagerNames();
    manageIds = manager.getManagerId();
    deptNames = manager.getDeptNames();
    deptIds = manager.getManageDeptId();
    seqId = manager.getSeqId();
  }
%>
<html>
<head>
<title>设置管理员</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script Language="JavaScript"> 
	function clearValue(){
	  $('manage').value = "";
	  $('managernames').value = "";
	}

	function doInit(){
    var flag = <%=flag%>; 
    if(flag == true){
      var managerNames = "<%=names%>";
      var manageIds = "<%=manageIds%>";
      var deptIds = "<%=deptIds%>";
      var deptNames = "<%=deptNames%>";
      var seqId = "<%=seqId%>";
      $("managernames").value = managerNames;      
      $("manage").value = manageIds;
      $("dept").value = deptIds;
      $("deptDesc").value = deptNames;
      $("seqId").value= seqId;
      $("form1").action = contextPath + "/yh/subsys/oa/book/act/YHSetBookManagerAct/updateManager.act";
    }  
	}

	function clearDept(){
	  $('dept').value = "";
	  $('deptDesc').value = "";
	}

	function goBack(){
   window.location.href= contextPath + "/yh/subsys/oa/book/act/YHSetBookManagerAct/index.act";
	}

	function check(){
   var manageIds = $("manage").value;
   var deptIds = $("dept").value;
   if(!manageIds){
     alert("管理员不能为空！");
     return false;
   }else if(!deptIds){
     alert("所管部门不能为空！");
     return false;
   }else{
     $("form1").submit();
   }
  }
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%= imgPath %>/edit.gif"><span class="big3"> 编辑管理员</span>
	    </td>
	  </tr>
	</table>
	<form action="<%=contextPath%>/yh/subsys/oa/book/act/YHSetBookManagerAct/addManager.act"  method="post" name="form1" id="form1">
<table class="TableBlock"  width="500" align="center" >
  
   <tr>
      <td nowrap class="TableData">管理员：</td>
      <td class="TableData">
        <input type="hidden" id="manage" name="manage" value="">
        <textarea  name="managernames" id="managernames" rows="4"   cols=30 class="BigStatic" wrap="yes" readonly></textarea>       
        <a href="javascript:void(0);" class="orgAdd" onclick="selectUser(['manage', 'managernames'])">添加</a>      
        <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearValue();">清空</a><br>	
      </td>      
   </tr>
    
   <tr>
     <td nowrap class="TableData">所管部门：</td>
     <td class="TableData">
       <input type="hidden" id="dept" name="dept" value="">       
       <textarea cols=30 id="deptDesc" name="deptDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onclick="javascript:selectDept(['dept','deptDesc'] );">添加</a>
       <a href="javascript:;" class="orgClear" onclick="clearDept()">清空</a>
     </td>
   </tr> 
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
    	  <input type="hidden" name="AUTO_ID" value="">
        <input type="button" onclick="check();return false;" value="确定" class="BigButton" name="button">&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="goBack();return false;">
    </td>
   </tr>
   <input type="hidden" name="seqId"  id="seqId" value=""/>
 
</table>
  </form>
</body>
</html>

