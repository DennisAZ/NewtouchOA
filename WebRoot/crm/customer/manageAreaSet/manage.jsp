<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String loginUserPriv = person.getUserPriv();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员管辖区域设置</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/grid.js"></script>
<script type="text/javascript" src="<%=contextPath %>/crm/customer/manageAreaSet/js/userPrivUtil.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/grid.css" />
<style type="text/css">  
.align-center{   
     margin:0 auto;      /* 居中 这个是必须的，，其它的属性非必须 */   
     width:500px;        /* 给个宽度 顶到浏览器的两边就看不出居中效果了 */   
}   
</style>
<script type="text/javascript">
function doInit() {
  var url =  contextPath + "/yh/core/funcs/person/act/YHUserPrivAct/getUserPrivList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data",width: 80, name:"privNo", text:"角色排序号", render:roleStyle},   
       {type:"data", width: 150,name:"privName", text:"角色名称", render:roleNameStyle},   
       //{type:"data", width: 220,name:"seqId", text:"总用户数/禁止登录数/辅助角色数", render:getCountFunc},   
       {type:"selfdef", text:"操作", width: 200,render:opts}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    //var mrs = " 共 "+ total + " 个符合条件且可管理的用户";
    //WarningMsrg(mrs, 'msrg');
  }else{
    WarningMsrg('无管理角色', 'msrg');
  }
}  

/**
 * 自定义操作

 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var privNo = this.getCellData(recordIndex,"privNo");
  var privName = this.getCellData(recordIndex,"privName");
  if(privName=="未分配角色"){
	    return "";
	  }
	  return "<center><a href=\"javascript:doPriv("+seqId+","+privNo+",'"+privName+"');\">设置管辖区域</a></center>";
}

function getCountFunc(cellData, recordIndex, columIndex){
  var count = "";
  var count1 = "";
  var count2 = "";
  var count3 = "";
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath %>/yh/core/funcs/person/act/YHUserPrivAct/getAllUsers.act";
  var rtJsonAll = getJsonRs(url, "seqId=" + seqId);
  if (rtJsonAll.rtState == "0") {
    count1 = rtJsonAll.rtData ;
  }
  var url = "<%=contextPath %>/yh/core/funcs/person/act/YHUserPrivAct/getNotLoginUser.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    count2 = rtJson.rtData ;
  }
  var url = "<%=contextPath %>/yh/core/funcs/person/act/YHUserPrivAct/getOtherUser.act";
  var rtJsonOther = getJsonRs(url, "seqId=" + seqId);
  if (rtJsonOther.rtState == "0") {
    count3 = rtJsonOther.rtData ;
  }
  count = count1+"/"+count2+"/"+count3
  return "<center>" + count + "</center>";
}

function doDetail(seqId, privName){
  var URL = "/yh/core/funcs/userpriv/showUsers.jsp?seqId="+seqId+"&privName="+encodeURIComponent(privName);
  //openDialogResize(URL,'800', '500');
  var myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

/**
 * 编辑
 */
function doEdit(seqId){
  location.href = "<%=contextPath %>/core/funcs/userpriv/edit.jsp?seqId=" + seqId;
}

function confirmDel() {
  if(confirm("确认删除！")){
    return true;
  }else{
    return false;
  }
}

/**
 * 删除
 */
function doDelete(seqId){
  if(!confirmDel()){
	  return ;
  }

  var url = "<%=contextPath %>/yh/core/funcs/person/act/YHUserPrivAct/getUserPrivNo.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    if(rtJson.rtData > "0"){
      location = "<%=contextPath %>/core/funcs/userpriv/deletepriv.jsp";
     }else{
       var urls = "<%=contextPath %>/yh/core/funcs/person/act/YHUserPrivAct/deleteUserPriv.act";
       var rtJsons = getJsonRs(urls, "seqId=" + seqId);
       if (rtJsons.rtState == "0") {
         window.location.reload();
       }else{
         alert(rtJsons.rtMsrg); 
       }
     }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 设置权限
 */
function doPriv(seqId,privNo,privName){
  location.href = "<%=contextPath%>/core/funcs/userpriv/privtree.jsp?seqId=" + seqId + "&privNo=" + privNo + "&privName=" + encodeURIComponent(privName);
}

/**
 * 克隆
 */
function doClone(seqId){
  var num = 1 ;
  window.location.href = "<%=contextPath %>/core/funcs/userpriv/edit.jsp?seqId=" + seqId
  + "&num=" + num;
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
      <span class="big3">人员管辖区域设置</span>
      <!-- 
      &nbsp;&nbsp;<span class="small" style="cursor:pointer;" title="点击打开操作提示与技巧窗口" onclick="javascript:window.open('/module/iask?FUNC=sys_user_priv','iask','top=0,left=0,width=600,height=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes')">&nbsp;<img src="<%=imgPath%>/iask_small.gif" align="absmiddle"></span>    </td>
      -->
  </tr>
</table>
<div id="userpriv">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHUserPriv"/>
</div>

<div id="listContainer" style="display:none;vertical-align:middle;" class="align-center">
</div>
<div id="msrg">
</div>

</body>
</html>