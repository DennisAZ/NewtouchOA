<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.data.YHAuthKeys" %>
<%
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String maxSum = String.valueOf(YHRegistProps.getInt(YHAuthKeys.USER_CNT + ".yh"));
  if (maxSum == null) {
    maxSum = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>离职人员</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script type="text/javascript">
loginUserId = <%=loginUser.getSeqId()%>;
var isAdmin = <%=loginUser.isAdmin()%>;
var deptId = "<%=deptId%>";
var maxSum = "<%=maxSum%>";
var total = 0;
function doInit() {
  var url =  contextPath + "/yh/core/funcs/person/act/YHPersonAct/getManagePersonList.act?deptId=" + deptId;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int", render:subjectRender},
       {type:"data", name:"userId",  width: '20%', text:"用户名", render:subjectRender},       
       {type:"data", name:"userName",  width: '20%', text:"真实姓名", render:subjectRender},
       {type:"data", name:"deptId",  width: '20%', text:"部门", render:getDeptName},
       {type:"data", name:"userPriv",  width: '20%', text:"角色", render:getRoleName},
       {type:"hidden", name:"notLogin", text:"是否登录", dataType:"int"},
       {type:"hidden", name:"password", text:"密码", dataType:"int"},
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  window.pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  total = pageMgr.pageInfo.totalRecord;
  $('numUser').innerHTML = total;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('暂无可管理的用户！', 'msrg');
  }
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
  var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
  if(deptId == "0"){
    document.getElementById("newUser").innerHTML = "离职人员/外部人员";
    document.getElementById("manageUser").innerHTML = "离职人员/外部人员";
  }
}  

function reloadFunc(){
  parent.navigateFrame.location.reload();
  if(total == 0){
    location = "<%=contextPath%>/core/funcs/person/usernew.jsp?deptId="+deptId;
  }else{
    pageMgr.refreshAll();
  }
}

function getStyle(cellData, recordIndex, columIndex) {
  var password = this.getCellData(recordIndex,"isEmptyPass");
  if (password == "1") {
    return {color: "#FF0000"};
  }else {
    return {color: "#00FF00"};
  }
}

/**
 * 自定义操作
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
    return "<center><a href=\"javascript:setPersonManageArea("+seqId+",'"+userName+"');\">设置管辖区域</a></center>";
}

/**
 * 设置管辖区域
 */
function setPersonManageArea(seqId,userName){
  location.href = "<%=contextPath%>/crm/customer/manageAreaSet/privtree.jsp?seqId=" + seqId + "&userName=" + encodeURIComponent(userName);
}

/**
 * subject 描画
 */
function subjectRender(cellData, recordIndex, columIndex) {
  var deptId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
  var notLogin = this.getCellData(recordIndex,"notLogin");
  var password = this.getCellData(recordIndex,"isEmptyPass");
    if(notLogin == "1"){
      return "<div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">" + cellData + "</div >";
    }else if(password == "1"){
      return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=black>" + cellData + "</font ></div >";
      }else{
      return "<div align=\"center\" title=\""+userName+"\"><font color=black>" + cellData + "</font ></div >";
    }
}
/**
 * 获得部门名称及相对应的颜色：密码为空用户显示为红色，禁止登录用户显示为灰色 
 */
function getDeptName(cellData, recordIndex, columIndex){
  var deptId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
  var notLogin = this.getCellData(recordIndex,"notLogin");
  var password = this.getCellData(recordIndex,"isEmptyPass");
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  cellData);
    if(rtJsons.rtState == '0'){
      if(notLogin == "1"){
        return "<center><div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">" +rtJsons.rtData+ "</div >";
      }else if(password == "1"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=black>" +rtJsons.rtData+ "</font ></div >";
      }else{
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>" +rtJsons.rtData+ "</font ></div >";
      }
    }
}
/**
 * 获得角色名称及相对应的颜色：密码为空用户显示为红色，禁止登录用户显示为灰色 
 */
function getRoleName(cellData, recordIndex, columIndex){
  var deptId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
  var notLogin = this.getCellData(recordIndex,"notLogin");
  var password = this.getCellData(recordIndex,"isEmptyPass");
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getRoleName.act";
    var rtJsons = getJsonRs(urls , "roleId=" +  cellData);
    if(rtJsons.rtState == '0'){
      if(notLogin == "1"){
        return "<center><div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">" +rtJsons.rtData+ "</div ></center>";
      }else if(password == "1"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=black>" +rtJsons.rtData+ "</font ></div >";
      }else{
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>" +rtJsons.rtData+ "</font ></div >";
      }
    }
}
/**
 * 获得管理范围名称及相对应的颜色：密码为空用户显示为红色，禁止登录用户显示为灰色 
 */
function getPostPriv(cellData, recordIndex, columIndex){
  var deptId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
  var notLogin = this.getCellData(recordIndex,"notLogin");
  var password = this.getCellData(recordIndex,"isEmptyPass");
    if(notLogin == "1"){
      if(cellData == "0"){
        return "<div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">本部门</div >";
      }else if(cellData == "1"){
        return "<div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">全体</div >";
      }else if(cellData == "2"){
        return "<div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">指定部门</div >";
      }
    }else if(password == "1"){
      if(cellData == "0"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=red>本部门</font ></div >";
      }else if(cellData == "1"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=red>全体</font ></div >";
      }else if(cellData == "2"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=red>指定部门</font ></div >";
      }
    }else{
      if(cellData == "0"){
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>本部门</font ></div >";
      }else if(cellData == "1"){
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>全体</font ></div >";
      }else if(cellData == "2"){
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>指定部门</font ></div >";
      }
    }
}


function getDutyType(cellData, recordIndex, columIndex){
  var deptId = this.getCellData(recordIndex,"seqId");
  var userName = this.getCellData(recordIndex,"userName");
  var notLogin = this.getCellData(recordIndex,"notLogin");
  var password = this.getCellData(recordIndex,"isEmptyPass");
    var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDutyType.act";
    var rtJsons = getJsonRs(urls , "dutyId=" +  cellData);
    if(rtJsons.rtState == '0'){
      if(notLogin == "1"){
        return "<div style=color:#C0C0C0 align=\"center\" title=\""+userName+" 禁止登录OA\">" +rtJsons.rtData+ "</div >";
      }else if(password == "1"){
        return "<div align=\"center\" title=\""+userName+" 登录密码为空\"><font color=red>" +rtJsons.rtData+ "</font ></div >";
      }else{
        return "<div align=\"center\" title=\""+userName+"\"><font color=black>" +rtJsons.rtData+ "</font ></div >";
      }
    }
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  var userId = this.getCellData(recordIndex,"userId");
  if(userId != "admin"){
    return "<input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" >";
  }else{
    return "";
  }
    
}

</script>
</head>
<body onload="doInit()">
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;管理用户（<span id="manageUser"></span>）－ <span id="numUser"></span>人</span>
    </td>
  </tr>
</table>
<br/>
<div id="listContainer" style="display:none;">
</div>
<div id="delOpt" style="display:none">
</div>
<div id="msrg">
</div>
</body>
</html>