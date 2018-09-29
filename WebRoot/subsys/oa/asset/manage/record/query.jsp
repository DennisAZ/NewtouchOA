<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String cpreFlag = request.getParameter("CPRE_FLAG") == null ? "" :  request.getParameter("CPRE_FLAG");
String cptlName = request.getParameter("CPTL_NAME") == null ? "" :  request.getParameter("CPTL_NAME");
String cptlSpec = request.getParameter("CPTL_SPEC") == null ? "" :  request.getParameter("CPTL_SPEC");
String deptId2 = request.getParameter("DEPT_ID") == null ? "" :  request.getParameter("DEPT_ID");
String typeId = request.getParameter("TYPE_ID") == null ? "" :  request.getParameter("TYPE_ID");
String useFor = request.getParameter("USE_FOR") == null ? "" :  request.getParameter("USE_FOR");
String useState = request.getParameter("USE_STATE") == null ? "" :  request.getParameter("USE_STATE");
String useUser = request.getParameter("USE_USER") == null ? "" :  request.getParameter("USE_USER");
%>
<html>
<head>
<title>固定资产查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var cpreFlag2 = <%=cpreFlag%>;
var pageMgr = null;
var cfgs = null;
var name = "固定资产申请领用";
if (cpreFlag2 == '2') {
  name = "固定资产返库";
}
function doInit(){
  var param ="CPRE_FLAG=<%=cpreFlag%>&CPTL_NAME=<%=cptlName%>&CPTL_SPEC=<%=cptlSpec%>&DEPT_ID=<%=deptId2%>"
    + "&TYPE_ID=<%=typeId%>&USE_FOR=<%=useFor%>&USE_STATE=<%=useState%>&USE_USER=<%=useUser%>";
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/listSelect.act?" + param;
    cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"cptlNo", text:"编号",align:"center", width:"10%"},
       {type:"text", name:"cptlName", text:"固定资产名称",align:"center", width:"10%"},
       {type:"text", name:"cptlSpec", text:"规格型号", width: "10%",align:"center"},
       {type:"text", name:"cptlQty", text:"数量", width: "3%",align:"center"},
       {type:"text", name:"useDept", text:"使用地点", width: "6%",align:"center"},
       {type:"text", name:"cpreUser", text:"使用人签字", width: "6%",align:"center",render:toUser},
       {type:"text", name:"keeper", text:"专管员签字 ", width: "6%",align:"center",render:toKeeper},
       {type:"text", name:"CpreMemo", text:"备注 ", width: "6%",align:"center"},
       {type:"hidden", name:"runId", text:"工作流ID", width: "2%",align:"center"},
       {type:"text", name:"", text:"操作",align:"center", width: "5%",render:toCao}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的固定资产</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
}
//返回操作 
function toCao(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var runId =  this.getCellData(recordIndex,"runId");
  if (runId == "" || runId == null || runId == '0') {
    return "<a href=javascript:updateCptl('" + seqId + "');>修改</a>&nbsp;<a href=javascript:delDetail('" + seqId + "');>删除</a>";
  }
  if (runId != null && runId != "" && runId != '0') {
    return "<a href=javascript:formViewByName('" + runId + "','" + name +"')>工作流详情</a>";
  }
}
function updateCptl(seqId) {
  var URL = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/getAssetId.act?seqId=" + seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=500,width=600,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
 }


//删除资产
function delDetail(seqId) {
  if (window.confirm("确认要删除该资产使用记录吗？")) {
    var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/getDelete.act?seqId=" + seqId;
    var rtJson = getJsonRs(url);
    queryGift();
  }
 }
//查询
function queryGift(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的固定资产 </div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}
//使用人
function toUser(cellData,recordIndex,columIndex){
  var cpreUser = this.getCellData(recordIndex,"cpreUser");
  if (cpreUser != "" || cpreUser != null) {
    var seqId = "?seqId=" + cpreUser;
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/userName.act" + seqId;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return userList;
  } else {
    return "";
  }
}
//专管员
function toKeeper(cellData,recordIndex,columIndex){
  var keeper = this.getCellData(recordIndex,"keeper");
  if (keeper != "" || keeper != null) {
    var seqId = "?seqId=" + keeper;
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/userName.act" + seqId;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return userList;
  } else {
    return "";
  }
}
//使用地点
function toDept(cellData,recordIndex,columIndex){
  var useDept = this.getCellData(recordIndex,"useDept");
  if (useDept != "" || useDept != null) {
    var seqId = "?seqId=" + useDept;
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/userDept.act" + seqId;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    return userList;
  } else {
    return "";
  }
}
</script>
</head>
<body onLoad="doInit();" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3">
  固定资产使用明细表<%if(cpreFlag.equals("1")){%>(使用部室)<%} %>
  <%if(cpreFlag.equals("2")){%>(物业部)
  <%} %>
  </span><br>
    </td>
    </tr>
    </table>
<br>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull">
</div>
<br>
<div align="center">
<input type="button"  value="返回" class="BigButton" onClick="history.back();">
</div>
</body>
</html>