<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="yh.core.funcs.modulepriv.data.YHModulePriv"%>
<% 
 String id = request.getParameter("id");
 if(id==null) {
   id = "1";
 }
 String uid = request.getParameter("uid");
 String userName = request.getParameter("userName");
 String dept_priv = request.getParameter("dept_priv");
 String role_priv = request.getParameter("role_priv");
 String dept_id = request.getParameter("dept_id");
 String priv_id = request.getParameter("priv_id");
 String user_id = request.getParameter("user_id");
 //userName = new String(userName.getBytes("iso-8859-1"), "utf-8");
 YHModulePriv modulePriv = (YHModulePriv)request.getAttribute("modulePriv");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按模块设置管理范围</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/swfupload.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script type="text/javascript">
  var userName = '<%=userName%>';
  var moduleArray = [['1','在线人员',userName+'可以看到所选范围的所有在线人员，为空则不限制'],
                     ['2','全部人员',userName+'可以看到所选范围的所有人员，为空则不限制'],
                     ['3','日程安排查询',userName+'可以看到所选范围内人员的日程安排，为空则只能看到自己管理范围内的比自己角色低的用户的日程安排'],
                     ['4','工作日志查询',userName+'可以看到所选范围内人员的工作日志，为空则只能看到自己管理范围内的比自己角色低的用户的工作日志'],
                     ['5','公告通知发布',userName+'可以向所选范围内的用户发布公告，为空则不限制'],
                     ['6','新闻发布',userName+'可以向所选范围内的用户发布新闻，为空则不限制'],
                     ['7','投票发布',userName+'可以向所选范围内的用户发布投票，为空则不限制'],
                     ['8','管理简报',userName+'可以统计所选范围内的用户的日志数量等工作情况'],
                     ['9','人事档案管理',userName+'可以管理所选范围内的用户的人事档案信息'],
                     ['10','人事档案查询',userName+'可以查询统计所选范围内的用户的人事档案信息'],
                     ['11','邮件发送范围',userName+'可以向所选范围内的用户发送邮件，为空则不限制'],
                     ['12','短信发送范围',userName+'可以向所选范围内的用户发送短信，为空则不限制'],
                     ['13','人力资源统计范围',userName+'可以统计所选范围内的部门人力资源情况，为空则不限制'],
                     ['14','薪酬统计范围',userName+'可以统计所选范围内的部门的薪酬情况，为空则不限制']];

  function onInit() {
       var id = "<%=id%>";
       var selectOtherAll = "";
       var count = 0;
	     for(var i=0;i < moduleArray.length;i++) {
	         if(id==moduleArray[i][0]) {
	        	 continue;
			 }
	         temp = '<input type="checkbox" id="APPLY_TO_MODULE_'+moduleArray[i][0]+'" value="'+moduleArray[i][0]+'" onclick="AddModule(this.id);"/><label for="APPLY_TO_MODULE_'
	            +moduleArray[i][0]+'">'+moduleArray[i][1]+'</label>';
	         count ++;
	         if(count%3==0) {
	           temp += '<br/>'
	         }
	         selectOtherAll +=temp;
	     }
//       alert(selectOtherAll);
       document.getElementById("tableHeader").innerHTML = userName+' - '+moduleArray[id-1][1];
       document.getElementById("moduleDiscripe").innerHTML = moduleArray[id-1][2];
       document.getElementById("selectOther").innerHTML = selectOtherAll;

       var url = contextPath + "/yh/core/funcs/modulepriv/act/YHModuleprivAct/getJson.act?id="+id+"&uid="+<%=uid%>;
		var json = getJsonRs(url);
		 if(json.rtState == "0"){ 
			 bindJson2Cntrl(json.rtData);
			 var rtData = json.rtData;
			 var deptId = rtData.deptId;
			 var userId = rtData.userId;
			 var privId = rtData.privId;
			 
			 if(deptId!=""||userId!=""||privId!=""){
			 bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
		 		,{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}
		 		,{cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}
		     	]);
			 }
			 var deptPriv = rtData.deptPriv;
			 var rolePriv = rtData.rolePriv;
			 if(deptPriv=='2'){
				 document.getElementById("dept_tr").style.display = '';
			}
		     if(deptPriv == '3'){
		    	 document.getElementById("user_tr").style.display = '';
			 }
		     if(rolePriv == '3'){
		    	 document.getElementById("priv_tr").style.display = '';
			 }
		 }
   }

  function commitModulePriv(){
	  var dept_priv = document.form1.deptPriv.value;//获取到DEPT_PRIV信息 
	  var role_priv = document.form1.rolePriv.value;//获取到ROLE_PRIV信息 
	  var dept_id = document.form1.deptId.value;//获取到 DEPT_ID 信息 
	  var user_id = document.form1.userId.value;//获取到USER_ID信息 
	  var priv_id = document.form1.privId.value;//获取到PRIV_ID信息 
	  var apply_to_dept = document.form1.apply_to_dept.value;//获取到APPLY_TO_DEPT信息 
	  var apply_to_priv = document.form1.apply_to_priv.value;//获取到APPLY_TO_PRIV信息 
//	  alert(dept_priv+'-----'+role_priv+'------'+apply_to_dept+'------'+apply_to_priv);
	  if(dept_priv=="" && role_priv!="")
	   {
	      //alert("请选择部门范围");
	      //return false;
	      msg='您未指定部门范围，将恢复默认设置。';
	      if(window.confirm(msg))
	    	  role_priv="";
	      else
	      	 return false;
	      
	   }
	   if(role_priv=="" && dept_priv!="" && dept_priv!="4")
	   {
	      alert("请选择角色范围");
	      return false;
	   }
	   if(dept_priv=="2" && dept_id=="")
	   {
	      alert("请选择指定部门");
	      return false;
	   }
	   if(dept_priv=="3" && user_id=="")
	   {
	      alert("请选择指定人员");
	      return false;
	   }
	   if(dept_priv=="4")
	   {
		   role_priv="1";
	   }
	   if(role_priv=="3" && priv_id=="")
	   {
	      alert("请选择指定角色");
	      return false;
	   }
	   if(apply_to_dept=="" && apply_to_priv!="")
	   {
	      alert("请选择应用到的部门");
	      return false;
	   }
	   if(apply_to_dept!="" && apply_to_priv=="")
	   {
	      alert("请选择应用到的角色");
	      return false;
	   }
	   document.getElementById("userSeqId").value="<%=uid%>";
	   document.getElementById("moduleId").value="<%=id%>";
	  var url = "<%=contextPath%>/yh/core/funcs/modulepriv/act/YHModuleprivAct/updateModulepriv.act";
	  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
	  alert(rtJson.rtMsrg);
	}

  function AddModule(id)
  {
     var obj = document.getElementById(id);
     var apply_to_str = document.form1.apply_to_module.value;
     if(obj.checked)
     {
        apply_to_str+=obj.value+",";
     }
     else
     {
        if(apply_to_str.indexOf(obj.value+",")==0)
           apply_to_str=apply_to_str.replace(obj.value+",","");
        else if(apply_to_str.indexOf(","+obj.value+",")>0)
           apply_to_str=apply_to_str.replace(","+obj.value+",",",");
     }
 
     document.form1.apply_to_module.value = apply_to_str;
  }
   
  function show_obj(obj) {
    if(document.getElementById(obj).style.display == "none"){
      document.getElementById(obj).style.display="";
    }else{
      document.getElementById(obj).style.display="none";
    }
    
  }

  function select_dept(obj)  //指定部门和指定人员 
  {
     var dept_i=document.getElementById("dept_tr");
     var user_i=document.getElementById("user_tr");
     if (obj.value=="2")
     {
         dept_i.style.display='';
         user_i.style.display='none';
     }
     else if (obj.value=="3")
     {
         dept_i.style.display="none";
         user_i.style.display='';
     }
     else
     {
         dept_i.style.display="none";
         user_i.style.display='none';
     }
  }

  function select_priv(obj) //指定人员的角色 
  {
     var priv_i=document.getElementById("priv_tr");
     if(obj.value=="3")
         priv_i.style.display="";
     else
         priv_i.style.display="none";
  }

  function addDept() {
	 	var URL="<%=contextPath %>/core/funcs/orgselect/MultiDeptSelect1.jsp";
	  	openDialog(URL,'520', '400');
	}

  function ClearUser(TO_ID, TO_NAME){
	  if(TO_ID=="" || TO_ID=="undefined" || TO_ID== null){
		TO_ID="TO_ID";
		TO_NAME="TO_NAME";
	  }
	  document.getElementsByName(TO_ID)[0].value="";
	  document.getElementsByName(TO_NAME)[0].value="";
  }

  function SelectUser(TO_ID, TO_NAME){ //指定角色 
	  URL=contextPath + "/core/funcs/dept/userselect.jsp?&TO_ID="+TO_ID+"&TO_NAME="+TO_NAME;
	  openDialog(URL,'400', '350');
  }

  function addPrivtemp(){
     
  }
</script>
</head>
<body onload="onInit();">

<form action=""  method="post" name="form1" id="form1" onSubmit="">
  <table class="TableBlock" width="95%" align="center">
   <tr>
    <td class="TableHeader" colspan="2"><div id="tableHeader"></div></td>
   </tr>
   <tr>
    <td class="TableContent" width="100">人员范围：</td>
    <td class="TableData">
      <select name="deptPriv" class="BigSelect" OnChange="select_dept(this)">
          <option value=""></option>
          <option value="0">本部门</option>
          <option value="1">全体</option>
          <option value="2">指定部门</option>
          <option value="3">指定人员</option>
      </select>
    </td>
   </tr>
   <tr id="dept_tr" style="display:none">
    <td class="TableContent" width="100">指定部门：</td>
    <td class="TableData">
        <input type="hidden" name="deptId" id="dept" value="">
        <textarea cols=20 name="deptName" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc'],'','1');">添加</a>
        <a href="javascript:;" class="orgClear" onClick="javascript:ClearUser('deptId', 'deptName');">清空</a>
    </td>
   </tr>
   <tr id="user_tr" style="display:none">
    <td class="TableContent" width="100">指定人员：</td>
    <td class="TableData">
        <input type="hidden" name="userId" id="user" value="">
        <textarea cols=20 name="userName" id="userDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="javascript:ClearUser('userId', 'userName');">清空</a>
    </td>
   </tr>
   <tr>
    <td class="TableContent" width="100">人员角色：</td>
    <td class="TableData">
      <select name="rolePriv" class="BigSelect" OnChange="select_priv(this)">
          <option value=""></option>
     <%if ( !"5".equals(id) && !"6".equals(id) ) {%>
          <option value="0">低角色的用户</option>
          <option value="1">同角色和低角色的用户</option>
             <% } %>
          <option value="2">所有角色的用户</option>
           <%if ( !"5".equals(id) && !"6".equals(id) ) {%>
          <option value="3">指定角色的用户</option>
           <% } %>
      </select>
    </td>
   </tr>
   <tr id="priv_tr" style="display:none">
    <td class="TableContent" width="100">指定角色：</td>
    <td class="TableData">
        <input type="hidden" id="role" name="privId" value="">
        <textarea cols=18 id="roleDesc" name="privName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole(['role','roleDesc'],'','','1');">添加</a>
        <a href="javascript:;" class="orgClear" onClick="javascript:ClearUser('privId', 'privName');">清空</a>
    </td>
   </tr>
   <tr>
    <td class="TableContent" width="100">说明：</td>
    <td class="TableData"><div id="moduleDiscripe"></div></td>
   </tr>
   <tr onClick="show_obj('APPLAY_TBODY');" style="cursor:pointer;" title="点击选择应用到的模块和人员范围">
    <td class="TableHeader" colspan="2">以上设置应用到其它模块、其他用户 &gt;&gt;</td>
   </tr>
   <tbody id="APPLAY_TBODY" style="display:none;">
   <tr>
    <td class="TableContent" width="100">应用到其它模块：</td>
    <td class="TableData">
    <div id="selectOther"></div>
    </td>
   </tr>
   <tr>
    <td class="TableHeader" colspan="2">应用到其他用户（必须同时满足以下的部门、角色限制）</td>
   </tr>
   <tr>
    <td class="TableContent" width="100">所在部门：</td>
    <td class="TableData">
        <input type="hidden" name="apply_to_dept" id="apply_to_dept" value="">
        <textarea cols=19 name="apply_to_dept_name" id="apply_to_dept_name" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['apply_to_dept','apply_to_dept_name']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="javascript:ClearUser('apply_to_dept', 'apply_to_dept_name');">清空</a>
    </td>
   </tr>
   <tr>
    <td class="TableContent" width="100">所属角色：</td>
    <td class="TableData">
        <input type="hidden" name="apply_to_priv" id="privApply" value="">
        <textarea cols=19 name="apply_to_priv_name" id="privApplyDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRolePriv(['privApply','privApplyDesc'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="javascript:ClearUser('apply_to_priv', 'apply_to_priv_name')">清空</a>
    </td>
   </tr>
   </tbody>
   <tr>
    <td nowrap  class="TableControl" colspan="3" align="center">
        <input type="hidden" value="" name="userSeqId" id="userSeqId">
        <input type="hidden" value="" name="apply_to_module">
        <input type="hidden" value="" name="moduleId" id="moduleId">
        <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.modulepriv.data.YHModulePriv"/>
        <input type="button" value="保存" class="BigButton" name="button" onclick="commitModulePriv();">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="parent.close();">
    </td>
  </tr>
</table>
 </form>
</body>
</html>
