<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.manage.staffLaborSkills.data.*"%> 
<html>
<head>
<%
	YHPage pages = (YHPage)request.getAttribute("page");
  List<YHHrStaffLaborSkills> laborSkills = (List<YHHrStaffLaborSkills>)request.getAttribute("findLaborSkills");
  String issueDate1 = (String)request.getAttribute("issueDate1");
  String issueDate2 = (String)request.getAttribute("issueDate2");
  String expireDate1 = (String)request.getAttribute("expireDate1");
  String expireDate2 = (String)request.getAttribute("expireDate2");
  YHHrStaffLaborSkills laborSkill = (YHHrStaffLaborSkills)request.getAttribute("laborSkill");
  
%>
<title>劳动技能管理 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script Language="JavaScript">
   function laborSkillsInfo(laborSkillSeqId){
    var url  = contextPath + "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/findlaborSkillsXxInfo.act?skillId="+ laborSkillSeqId;
    window.location.href=url;
   
   }

   function gotoPage(currNo){
     $("currNo").value = currNo;
    // var url  = contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/findBooks.act?currNo="+ currNo;   
    // window.location.href= url;
     $("form1").submit();
   }

   function jumpToPage(){
     var start = 1;
     var end = <%=pages.getTotalPageNum()%>;
     var pageNo = $("currentNo").value;
     if(!isInteger(pageNo)&&pageNo!=0){
       alert("请填写整数");
       $("currentNo").focus();
       return false;
     }
     else if(pageNo > end || pageNo <start){
       alert("页号应在"+start+"-"+end +"之间");
       $("currentNo").focus();
       return false;
     }else{
       gotoPage(pageNo);
     }
   }

  function deleteLaborSkillInfo(deleteSeqId){
   var msg ="确定要删除该类别吗？";
   if(!window.confirm(msg)){
   }else{
     var noHiddenId = document.getElementById("noHiddenId").value;
     if(noHiddenId && noHiddenId!="null"){ 
       window.location.href = contextPath + "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/delLaborSkillInfo.act?HiddenId="+deleteSeqId;  
       return true;
      }
    }
   
  }
  //全选
  function checkAll(field){
	   var allSelect = document.getElementsByName("bookSelect");
	   for(var i = 0; i<allSelect.length; i++){
	      allSelect[i].checked = field.checked;
	   }
  }
  //单选
  function checkOne(one){
    if(!one.checked){   
	     document.all("allbox").checked=false;
	   }
  }
  // 删除所选劳动技能信息
  function deleteCheckLaborSkill(){
    var allSelect = document.getElementsByName("bookSelect");
    var deleteStr = "";
    for(var i = 0; i<allSelect.length; i++){
       if(allSelect[i].checked){
         deleteStr += allSelect[i].value +",";
       }
    }  
    if(deleteStr == ""){
       alert("要删除劳动技能信息，请至少选择其中一条");
       return ;
    }
    msg = '确定要删除所选劳动技能信息吗?';
    if(window.confirm(msg)){
       var par = 'deleteStr='+deleteStr;
       var url = contextPath + '/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/deleteLaborSkillInfo.act';
       var json = getJsonRs(url,par);
       if (json.rtState == "0"){//删除后 重新提交查询页面
				 $("form1").submit();
				 return true;
		  } else{
         alert(json.rtMsrg);
      }
      }
   }
 function updateLaborSkill(laborSkillId){
	 var url  = contextPath + "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/upLaborSkillInfo.act?laborSkillId="+ laborSkillId;
	  window.location.href=url;
 }
  
</script>
</head>

<body class="bodycolor" topmargin="5" >
<%
  if(pages.getTotalRowNum() > 0){%>    

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">劳动技能管理 </span><br>
    </td>
    <td valign="bottom" align="right">
    <span class="small1">当前为第<b><%=pages.getFirstResult()+1 %></b>至<b><%=pages.getLastResult()%></b>条 (第<%=pages.getCurrentPageIndex()%>页，共<%=pages.getTotalPageNum()%>页，每页最多<%=pages.getPageSize() %>条)</small>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
    </tr>
</table>

<table class="TableList"  width="95%" align="center">
<form method="post" name="from2" id ="from2" action="#">
  <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">单位员工</td>
      <td nowrap align="center">技能名称</td>
      <td nowrap align="center">级别</td>
      <td nowrap align="center">发证日期</td>
      <td nowrap align="center">有效期</td>
      <td nowrap align="center">操作</td>
  </tr>
 <%
 	for(int i=0; i<laborSkills.size()&&laborSkills.size()>0; i++){%>
 	  <tr class="TableLine1">
 	  <td nowrap align="center"><input type="checkbox" id="bookSelect" name="bookSelect" value="<%=laborSkills.get(i).getSeqId() %>" onClick="checkOne(this);">&nbsp;</td>
    <td nowrap align="center"><%=laborSkills.get(i).getStaffName() ==null?"":laborSkills.get(i).getStaffName() %>&nbsp;</td>
    <td nowrap align="center"><%=laborSkills.get(i).getAbilityName()==null?"":laborSkills.get(i).getAbilityName() %>&nbsp;</td>
    <td nowrap align="center"><%=laborSkills.get(i).getSkillsLevel()==null?"":laborSkills.get(i).getSkillsLevel() %>&nbsp;</td>
    <td nowrap align="center"><%=laborSkills.get(i).getIssueDate()==null?"":laborSkills.get(i).getIssueDate() %>&nbsp;</td>
    <td nowrap align="center"><%=laborSkills.get(i).getExpires() ==null?"":laborSkills.get(i).getExpires() %>&nbsp;</td>
    <td nowrap align="center" width="80">
    	    <a href="javascript:void(0);" onclick="laborSkillsInfo(<%=laborSkills.get(i).getSeqId() %>);return false;">详细信息 </a> 
    	     <a href="javascript:void(0);" onclick="updateLaborSkill(<%=laborSkills.get(i).getSeqId() %>);return false;">修改</a>
    	     <a href="javascript:void(0);" onclick="deleteLaborSkillInfo(<%=laborSkills.get(i).getSeqId() %>);return false;">删除 </a>	
  <input type="hidden" value="<%= laborSkills.get(i).getSeqId()%>" id="noHiddenId" name="noHiddenId"/>
    </td>
  </tr>
 <%} %> 
 <%
   if(pages.getTotalPageNum() >0){%>
     <tr class="TableControl">
			<td colspan="2" align="left">
				<input type="checkbox" name="allbox" id="allbox" onClick="checkAll(this);"><label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label>&nbsp;
        <a href="javascript:deleteCheckLaborSkill();" title="删除所选劳动技能信息"><img src="<%=imgPath %>/delete.gif">删除</a>
	    	 </td>
			 <td colspan="5" align="right">
			   <%if(pages.getCurrentPageIndex() >1){%>
			      <input type="button"  value="首页" class="BigButton"   onclick="gotoPage(1)"> &nbsp;&nbsp;
			   	  <input type="button"  value="上一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()-1 %>)"> &nbsp;&nbsp;
			   	<%}%>
			   <%if(pages.getCurrentPageIndex() < pages.getTotalPageNum()){%>
			   	  <input type="button"  value="下一页" class="BigButton"  onclick="gotoPage(<%=pages.getCurrentPageIndex()+1 %>)"> &nbsp;&nbsp;
			   	  <input type="button"  value="末页" class="BigButton"   onclick="gotoPage(<%=pages.getTotalPageNum() %>)"> &nbsp;&nbsp;
			   	<%}%>
			   页数
			   <input type="text" name="currentNo" id="currentNo" value="<%=pages.getCurrentPageIndex()%>" class="SmallInput" size="2"> <input type="button"  value="转到" class="SmallButton" onclick="jumpToPage();" title="转到指定的页面">&nbsp;&nbsp;
			</td>
   </tr>
  <%}%> 
  </form>
</table>
<%} else{%>
 <table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">没有符合条件的劳动技能信息！</div>
				    </td>
          </tr>
        </tbody>
      </table>  
<%}%>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/queryLaborSkillInfo.act" method="get" name="form1" id="form1">
 <%if(laborSkill !=null){ %>
 
 <input type="hidden" name="userId"  value="<%=laborSkill.getStaffName()==null?"":laborSkill.getStaffName() %>"/> 
 <input type="hidden" name="ABILITY_NAME"  value="<%=laborSkill.getAbilityName()==null?"":laborSkill.getAbilityName() %>"/> 
 <input type="hidden" name="ISSUING_AUTHORITY"  value="<%=laborSkill.getIssuingAuthority()==null?"":laborSkill.getIssuingAuthority() %>"/> 
 
  <input type="hidden" name="ISSUE_DATE1"  value="<%=issueDate1 %>"/> 
  <input type="hidden" name="ISSUE_DATE2"  value="<%=issueDate2 %>"/> 
  <input type="hidden" name="EXPIRE_DATE1"  value="<%=expireDate1 %>"/> 
   <input type="hidden" name="EXPIRE_DATE2"  value="<%=expireDate2 %>"/> 
   
   
 <%} %>
  <input type="hidden" name="currNo" id="currNo" value=""/> 
</form>
</body>
</html>
