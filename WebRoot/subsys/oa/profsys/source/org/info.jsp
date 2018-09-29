<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看组织信息</title>
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var  selfdefMenu = {
  	office:["downFile","dump","read"], 
    img:["downFile","dump","play"],  
    music:["downFile","dump","play"],  
    video:["downFile","dump","play"], 
    others:["downFile","dump"]
	}

function doOnloadFile(seqId){
  var attr = $("attr");
  attachMenuSelfUtil(attr,"profsys",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
function doOnload(){
  var seqId = '<%=seqId%>';
  getOrg(seqId);
}
function getOrg(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/source/org/act/YHSourceOrgAct/selectOrgById.act?seqId="+seqId; 
  var json=getJsonRs(requestURL); 
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
   // $("seqId").value = seqId;
    $("orgNum").innerHTML = prc.orgNum;
    $("orgName").innerHTML = prc.orgName;
    $("orgNation").innerHTML = prc.orgNation;
    $("orgLeader").innerHTML = prc.orgLeader;
    $("orgScale").innerHTML = prc.orgScale;
    $("orgPublication").innerHTML = prc.orgPublication;
    $("orgEstablishTime").innerHTML = prc.orgEstablishTime.substr(0,10);
    $("orgActive").innerHTML = prc.orgActive;
    $("orgContact").innerHTML = prc.orgContact;
    $("orgNote").innerHTML = prc.orgNote;   
    $("attachmentId").value = prc.attachmentId;
    $("attachmentName").value = prc.attachmentName;
    doOnloadFile(seqId);
  }
}
</script>
</head>
<body  onLoad="doOnload();" topmargin="5">
&nbsp;
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 组织信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1">
<table class="TableBlock" width="80%" align="center">
    <tr>
     <td nowrap  class="TableControl" colspan="6" align="left">
      基本信息
     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">组织编号：</td>
      <td nowrap class="TableData" id="orgNum">
           </td>       
      <td nowrap class="TableContent" width="90">组织名称：</td>
      <td nowrap class="TableData" id="orgName">     </td>       
    </tr>
    <tr>
      <td nowrap class="TableContent">国别：</td>
      <td nowrap class="TableData" id="orgNation">    </td>
      <td nowrap class="TableContent" >领导人：</td>
      <td nowrap class="TableData" id="orgLeader">  </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent">规模：</td>
      <td nowrap class="TableData" id="orgScale">    </td>
      <td nowrap class="TableContent">成立时间：</td>
      <td nowrap class="TableData" id="orgEstablishTime">  </td>
    </tr>
    <tr>
     <td nowrap class="TableContent">发行刊物：</td>
      <td class="TableData" colspan="3" id="orgPublication"> </td>     
   </tr>
   <tr>
      <td nowrap class="TableContent">主要从事活动：</td>
      <td class="TableData" colspan="3" id="orgActive">
    </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">交往情况：</td>
      <td class="TableData" colspan="3" id="orgContact"> </td>
    </tr>   
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="orgNote">     </td>
    </tr>
     <tr>
     <td nowrap  class="TableControl" colspan="6" align="left">
      其他信息
     </td>
    </tr>
    <tr> 
      <td nowrap class="TableData" colspan="6">
      <input type="hidden" id="attachmentName" name="attachmentName"></input>
       <input type="hidden" id="attachmentId" name="attachmentId"></input>
       <span id="attr">无附件 </span>
     </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="parent.window.close();">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>