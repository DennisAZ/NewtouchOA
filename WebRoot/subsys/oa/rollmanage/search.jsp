<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String rollCode = request.getParameter("rollCode")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("rollCode"));
  String rollName = request.getParameter("rollName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("rollName"));
  String roomId = request.getParameter("roomId") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("roomId"));
  String years = request.getParameter("years") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("years"));
  String beginDate0 = request.getParameter("beginDate0") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("beginDate0"));
  String beginDate1 = request.getParameter("beginDate1") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("beginDate1"));
  String endDate0 = request.getParameter("endDate0") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("endDate0"));
  String endDate1 = request.getParameter("endDate1") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("endDate1"));
  String secret = request.getParameter("secret") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("secret"));
  String deadline0 = request.getParameter("deadline0") == null ? "" : YHUtility.encodeSpecial(request.getParameter("deadline0")) ;
  String deadline1 = request.getParameter("deadline1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("deadline1")) ;
  String categoryNo = request.getParameter("categoryNo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("categoryNo")) ;
  String catalogNo = request.getParameter("catalogNo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("catalogNo")) ;
  String archiveNo = request.getParameter("archiveNo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("archiveNo"));
  String boxNo = request.getParameter("boxNo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("boxNo"));
  String microNo = request.getParameter("microNo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("microNo"));
  String certificateKind = request.getParameter("certificateKind") == null ? "" : YHUtility.encodeSpecial(request.getParameter("certificateKind"));
  String certificateStart0 = request.getParameter("certificateStart0") == null ? "" : YHUtility.encodeSpecial(request.getParameter("certificateStart0"));
  String certificateStart1 = request.getParameter("certificateStart1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("certificateStart1"));
  String certificateEnd0 = request.getParameter("certificateEnd0") == null ? "" : YHUtility.encodeSpecial(request.getParameter("certificateEnd0"));
  String certificateEnd1 = request.getParameter("certificateEnd1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("certificateEnd1"));
  String rollPage0 = request.getParameter("rollPage0") == null ? "" : YHUtility.encodeSpecial(request.getParameter("rollPage0"));
  String rollPage1 = request.getParameter("rollPage1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("rollPage1"));
  String deptId = request.getParameter("deptId") == null ? "" : YHUtility.encodeSpecial(request.getParameter("deptId"));
  String remark = request.getParameter("remark") == null ? "" : YHUtility.encodeSpecial(request.getParameter("remark"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>案卷查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rmsrolllogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var param = "";
  param =  "rollCode="+encodeURI('<%=rollCode%>')+"&rollName="+encodeURI('<%=rollName%>')+"&roomId="+encodeURI('<%=roomId%>')+"&years="+encodeURI('<%=years%>')+"&beginDate0=<%=beginDate0%>&beginDate1=<%=beginDate1%>&endDate0="; 
  param +=  "<%=endDate0%>&endDate1=<%=endDate1%>&secret="+encodeURI('<%=secret%>')+"&deadline0="+encodeURI('<%=deadline0%>')+"&deadline1="+encodeURI('<%=deadline1%>')+"&categoryNo="+encodeURI('<%=categoryNo%>')+"&catalogNo=";
  param +=  encodeURI('<%=catalogNo%>')+"&archiveNo="+encodeURI('<%=archiveNo%>')+"&boxNo="+encodeURI('<%=boxNo%>')+"&microNo="+encodeURI('<%=microNo%>')+"&certificateKind="+encodeURI('<%=certificateKind%>')+"&certificateStart0=";
  param +=  encodeURI('<%=certificateStart0%>')+"&certificateStart1="+encodeURI('<%=certificateStart1%>')+"&rollPage0="+encodeURI('<%=rollPage0%>')+"&rollPage1="+encodeURI('<%=rollPage1%>')+"&deptId="+encodeURI('<%=deptId%>')+"&remark="
  param +=  encodeURI('<%=remark%>')+"&certificateEnd0="+encodeURI('<%=certificateEnd0%>')+"&certificateEnd1="+encodeURI('<%=certificateEnd1%>')+"";
  var url =  contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getSearchRmsRoll.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"rollCode",  width: '10%', text:"案卷号", render:rollCodeFunc},       
         {type:"data", name:"rollName",  width: '10%', text:"案卷名称", render:rollCenterFunc},
         {type:"data", name:"roomId",  width: '10%', text:"所属卷库", render:getRmsRollRoomNameFunc},
         {type:"data", name:"categoryNo",  width: '10%', text:"全宗号", render:rollCenterFunc},
         {type:"data", name:"certificateKind",  width: '10%', text:"凭证类别", render:getCodeNameKindFunc},
         {type:"data", name:"secret",  width: '10%', text:"案卷密级", render:getCodeNameSecretFunc},
         {type:"data", name:"status",  width: '15%', text:"案卷状态",render:rollStatusFunc}, 
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的案卷', 'msrg');
    }
    if(!total){
      $("delOpt").style.display = "none";
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 案卷查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/rollmanage/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>

</form>
</body>
</html>