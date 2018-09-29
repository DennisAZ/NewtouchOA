<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String userId = request.getParameter("perSeqId")== null ? "" : YHUtility.encodeSpecial(request.getParameter("perSeqId"));
  String tPlanNo = request.getParameter("tPlanNo")== null ? "" : YHUtility.encodeSpecial(request.getParameter("tPlanNo"));
  String tInstitutionName = request.getParameter("tInstitutionName") == null ? "" : YHUtility.encodeSpecial(request.getParameter("tInstitutionName"));
  String trainningCost = request.getParameter("trainningCost") == null ? "" : YHUtility.encodeSpecial(request.getParameter("trainningCost"));
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>培训计划查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingrecordlogic.js"></script>
<script type="text/javascript">
var loginUserId = <%=loginUser.getSeqId()%>;
var pageMgr = null;
function doInit(){
  var param = "";
  param =  "userId="+encodeURI('<%=userId%>')+"&tPlanNo="+encodeURI('<%=tPlanNo%>')+"&tInstitutionName="+encodeURI('<%=tInstitutionName%>')+"&trainningCost="; 
  param +=  encodeURI('<%=trainningCost%>')+"";
  var url =  contextPath + "/yh/subsys/oa/training/act/YHTrainingRecordAct/getTrainingRecordSearchList.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
               {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
               {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
               {type:"data", name:"tPlanName",  width: '20%', text:"培训计划名称", render:recordCenterFunc},       
               {type:"data", name:"staffUserId",  width: '20%', text:"受训人", render:staffUserIdFunc},
               {type:"data", name:"trainningCost",  width: '20%', text:"培训费用", render:trainingCostFunc},
               {type:"data", name:"tInstitutionName",  width: '20%', text:"培训机构", render:recordCenterFunc},
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
      WarningMsrg('无符合条件的培训记录', 'msrg');
      //showCntrl('delOpt');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>

<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选培训记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选培训记录</a>&nbsp;
      </td>
 </tr>
</table>
<br>
</div>
<div align="center">
<input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/training/record/query.jsp';">&nbsp;&nbsp;
</div>
</form>
</body>
</html>