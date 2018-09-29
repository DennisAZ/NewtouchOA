<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.officeProduct.officeType.data.*"%> 
<%@ page  import="java.util.List"%>
<html>
<head>
<%
YHOfficeDepository officeDep = (YHOfficeDepository)request.getAttribute("officeDepositoryInfo");
List<YHOfficeType> officeType = (List<YHOfficeType>)request.getAttribute("officeType");

String deptNames = (String)request.getAttribute("deptNames");
if(!YHUtility.isNullorEmpty(officeDep.getDeptId())){
	if(officeDep!=null && officeDep.getDeptId().equals("0")){
		deptNames ="全体部门";
	}
}
String managerNames = (String)request.getAttribute("managerNames");
String keeperNames = (String)request.getAttribute("keeperNames");

%>
<title>修改办公用品库</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>
<script type="text/javascript">
var fckContentStr = "";
/*****************附件上传开始*****************************/
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
/*****************附件上传结束*****************************/
 /** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function doSubmit(){
	var depositoryName = $("DEPOSITORY_NAME").value;
	if(depositoryName.replaceAll(" ","") == "" || depositoryName == "null"){
	      alert("库名称不能为空");
	      return false;
 }
	$("form1").submit();
}
//返回
function gotos(){
	window.location.href= contextPath + "/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/findOfficeDepositoryInfo.act";
}
/*
function doOnload(){
var depts	= $("dept").value;
$("dept1").value = depts;
}
*/
</script>
</head>
<body topmargin="5" class="bodycolor" >
	<script src="/inc/js/module.js"></script>
	<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
		<tbody>
		   <tr>
			   <td class="Big"><img align="absmiddle" src="<%=imgPath %>/vote.gif"><span class="big3"> 修改办公用品库 </span></td>
		   </tr>
		</tbody>
	</table>
<form action="<%=contextPath%>/yh/subsys/oa/officeProduct/officeType/act/YHOfficeDepositoryAct/updateOfficeDepositoryInfo.act"  method="post" name="form1" id="form1" onsubmit="">
		<table width="40%" align="center" name="1" class="TableList">
		  <tbody>
			  <tr>
			      <td nowrap="" align="right" class="TableData">库名称：</td>
			      <td class="TableData"><input type="text" name="DEPOSITORY_NAME" id="DEPOSITORY_NAME" size="30" value="<%=officeDep.getDepositoryName()==null?"":officeDep.getDepositoryName() %>"></td>
			      <input type="hidden" name="seqid" id = "seqid" value="<%=officeDep.getSeqId() %>"></input>
			  </tr>
			  <tr>
				  <td align="right" class="TableData">所属部门：</td>
				  <td class="TableData" id="td_dept">
          <input type="hidden" id ="dept" name="dept" value="<%=officeDep.getDeptId()==null?"":officeDep.getDeptId() %>">
           <textarea cols=20 name="deptDesc" id="deptDesc" rows="3" class="BigStatic" wrap="yes" readonly><%=deptNames==null?"":deptNames %> </textarea>
          <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['dept','deptDesc']);">添加</a>
          <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';");">清空</a>
      </td>
			  </tr>
			<tr>
			  <td nowrap="" align="right" class="TableData">库管理员：</td>
			  <td class="TableData" id="td_user">
	        <input type="hidden" name="user" id ="user" value="<%=officeDep.getManager()==null?"":officeDep.getManager() %>">
	        <textarea cols=20 name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes" readonly><%=managerNames==null?"":managerNames %></textarea>
	        <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userDesc']);">添加</a>
	        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
       </td>
      
			</tr>
			<tr>
			  <td nowrap="" align="right" class="TableData">物品调度员：</td>
			   <td class="TableData" id="td_user">
	        <input type="hidden" name="user1" id ="user1" value="<%=officeDep.getProKeeper()==null?"":officeDep.getProKeeper() %>">
	        <textarea cols=20 name="userDesc1" id="userDesc1" rows="2" class="BigStatic" wrap="yes" readonly><%=keeperNames==null?"":keeperNames %></textarea>
	        <a href="javascript:;" class="orgAdd" onClick="selectUser(['user1', 'userDesc1']);">添加</a>
	        <a href="javascript:;" class="orgClear" onClick="$('user1').value='';$('userDesc1').value='';">清空</a>
        </td>
			</tr>
		 <tr>
        <td nowrap="" align="right" class="TableContent">专属类别：</td>
        <td class="TableData">
        <%if(officeDep!=null && officeDep.getSeqId()!=1 && officeType!=null){//if($ID!='1')oa不等于1
	        	 for(int i=0; i<officeType.size()&&officeType.size()>0; i++){
		        	  int  typeId =officeType.get(i).getSeqId();
		        	  String typeName = officeType.get(i).getTypeName();
		        	  if(!YHUtility.isNullorEmpty(officeDep.getOfficeTypeId())&& officeDep.getOfficeTypeId().contains(String.valueOf(typeId))){
				     %>
					      <input type="checkbox" checked="" id="typeName" name="typeName" value="<%=typeId%>"><%=typeName%>
		         <%}else{ %>
		        	  <input type="checkbox"  id="typeName" name="typeName" value="<%=typeId%>"><%=typeName%>
		        <%}
	         }
       }%>
      </td>
    </tr> 
		</tbody>
	</table>    
	</form>
	<div align="center">
		 <input type="button" value="修改" class="BigButton" name="button" onclick="doSubmit();">
		<input type="button" onclick="gotos();" value="返回" class="BigButton">
	</div>
	<div align="center" id="returnDiv" style="">
 <table class="MessageBox" align="center" width="430" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">
        <span> 1、所属部门：指可以使用该库的部门；<br> </span> 
        <span> &nbsp;&nbsp;2、库管理员：有审批权限的用户，设置后在办公用品信息<br> </span>
        <span> &nbsp;&nbsp;3、物品调度员：负责物品采购和发放的用户，只在审批通过后收到短信提醒，不参与审批；<br> </span>
       </div>
    </td>
  </tr>
</table>
 </div>
 </body>
</html>