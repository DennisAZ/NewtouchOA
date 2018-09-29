<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>标题</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit() {
  var url =  contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/list.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    colums: [
       {type:"hidden", name:"seqId", dataType:"int"},
       {type:"data",name:"createUserId",text:"createUserId" },
       {type:"data",name:"createDeptId",text:"createDeptId" },
       {type:"data",name:"expertId",text:"expertId" },
       {type:"data",name:"employeeName",text:"employeeName" },
       {type:"data",name:"planNo",text:"planNo" },
       {type:"data",name:"position",text:"position" },
       {type:"data",name:"employeeMajor",text:"employeeMajor" },
       {type:"data",name:"employeePhone",text:"employeePhone" },
       {type:"data",name:"transactorStep",text:"transactorStep" },
       {type:"data",name:"stepFlag",text:"stepFlag" },
       {type:"data",name:"endFlag",text:"endFlag" },
       {type:"data",name:"nextDateTime",text:"nextDateTime" },
       {type:"data",name:"nextTransaStep",text:"nextTransaStep" },
       {type:"data",name:"filterMethod1",text:"filterMethod1" },
       {type:"data",name:"filterDateTime1",text:"filterDateTime1" },
       {type:"data",name:"firstContent1",text:"firstContent1" },
       {type:"data",name:"firstView1",text:"firstView1" },
       {type:"data",name:"transactorStep1",text:"transactorStep1" },
       {type:"data",name:"passOrNot1",text:"passOrNot1" },
       {type:"data",name:"nextTransaStep1",text:"nextTransaStep1" },
       {type:"data",name:"nextDateTime1",text:"nextDateTime1" },
       {type:"data",name:"filterMethod2",text:"filterMethod2" },
       {type:"data",name:"filterDateTime2",text:"filterDateTime2" },
       {type:"data",name:"firstContent2",text:"firstContent2" },
       {type:"data",name:"firstView2",text:"firstView2" },
       {type:"data",name:"transactorStep2",text:"transactorStep2" },
       {type:"data",name:"passOrNot2",text:"passOrNot2" },
       {type:"data",name:"nextTransaStep2",text:"nextTransaStep2" },
       {type:"data",name:"nextDateTime2",text:"nextDateTime2" },
       {type:"data",name:"filterMethod3",text:"filterMethod3" },
       {type:"data",name:"filterDateTime3",text:"filterDateTime3" },
       {type:"data",name:"firstContent3",text:"firstContent3" },
       {type:"data",name:"firstView3",text:"firstView3" },
       {type:"data",name:"transactorStep3",text:"transactorStep3" },
       {type:"data",name:"passOrNot3",text:"passOrNot3" },
       {type:"data",name:"nextTransaStep3",text:"nextTransaStep3" },
       {type:"data",name:"nextDateTime3",text:"nextDateTime3" },
       {type:"data",name:"filterMethod4",text:"filterMethod4" },
       {type:"data",name:"filterDateTime4",text:"filterDateTime4" },
       {type:"data",name:"firstContent4",text:"firstContent4" },
       {type:"data",name:"firstView4",text:"firstView4" },
       {type:"data",name:"transactorStep4",text:"transactorStep4" },
       {type:"data",name:"passOrNot4",text:"passOrNot4" },
        {type:"selfdef", width: "13%",text:"操作", render:optRender}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('listContainer').style.display = "";
  }else{
    WarningMsrg("无数据记�?","msrg");
  }
}
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"360\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
function optRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var html = "";
  html = "<A href=\"javascript:updateOpt(" + seqId + ")\">修改</A>&nbsp;"
       + "<A href=\"javascript:deleteOpt(" + seqId + ")\">删除</A>&nbsp;";
  return html;
}
function updateOpt(id){
  var url = "input.jsp?seqId=" + id;
  location = url;
}

function deleteOpt(id){
  if(!window.confirm("是否删除此条数据!")){
    return;
  }
  var url =  contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/deleteField.act?seqId=" + id;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
    location.reload();
  }else{
    alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="listContainer" style="display:none;width:98%;"></div>
<div id="msrg"></div>
</body>