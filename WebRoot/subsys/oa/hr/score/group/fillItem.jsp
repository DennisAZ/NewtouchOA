<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核指标集明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreAnswerLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
var total = 0;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreAnswerAct/getScoreAnswerList.act?seqId=${param.seqId}";
    var cfgs = {
        dataAction: url,
        container: "listContainer",
        sortIndex: 1,
        sortDirect: "desc",
        colums: [
           {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
           {type:"data", name:"itemName",  width: '20%', text:"考核项目名称 "},       
           {type:"selfdef", text:"操作", width: '20%',render:opts}]
      };
      pageMgr = new YHJsPage(cfgs);
      pageMgr.show();
      total = pageMgr.pageInfo.totalRecord;
      if(total){
        $("numDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
        showCntrl('listContainer');
        var mrs = " 共 " + total + " 条记录 ！";
      }else{
        WarningMsrg('尚未创建考核项', 'msrg');
      }
}

function doSubmit(){

}

function reloadFunc(){
  //parent.navigateFrame.location.reload();
  var seqId = ${param.seqId};
  if(total == 0){
    location = contextPath + "/subsys/oa/hr/score/group/fillItem.jsp?seqId="+seqId;
  }else{
    pageMgr.refreshAll();
  }
}

function getItemDetail(){
  var URL = contextPath + "/subsys/oa/hr/score/group/detailItem.jsp?seqId=${param.seqId}";
  newItemWindow(URL,'600','400');
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle" width=22 height=20><span class="big3"> 考核指标集明细</span>
    </td>
  </tr>
</table>

<br>
 <div align="center"><input type="button" value="新增考核项" class="BigButton" onclick="getItemDetail();"></div>
<br>
 <div align="center" id="numDiv"> </div>
<div id="listContainer" style="display:none;width:100;">
</div>
 
<div id="msrg">
</div>

<br>
<div align="center">
   <input type="button" class="BigButton" value="返回" onClick="location='/yh/subsys/oa/hr/score/index.jsp';">
</div>

</body>
</html>