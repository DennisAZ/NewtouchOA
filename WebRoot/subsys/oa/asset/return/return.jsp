<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的固定资产</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
function doInit(){
  var timestamps = new Date().valueOf();
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/cpcpSelect.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"资产编号",align:"center", width:"1%"},
       {type:"text", name:"cptlNo", text:"资产编号",align:"center", width:"8%"},
       {type:"text", name:"cptlName", text:"资产名称", width: "10%",align:"center",render:toName},
       {type:"selfdef", name:"jiLu", text:"资产使用记录", width: "5%",align:"center",render:toJiLu},
       {type:"text", name:"cptlVal", text:"资产值", width: "5%",align:"center",render:toVal},
       {type:"hidden", name:"cptlQty", text:"数量", width: "5%",align:"center"},
       {type:"text", name:"cptlSpec", text:"资产类型 ", width: "8%",align:"center"},
       {type:"text", name:"listDate", text:"单据日期",align:"center", width: "8%",render:toTime,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"caozuo", text:"操作",align:"center", width: "5%",render:toCao}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无我的固定资产</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
}
//返回操作名称
function toName(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var cptlName =  this.getCellData(recordIndex,"cptlName");
  return "<a href=javascript:cptlDetail('" + seqId +"')>"+ cptlName +"</a>";
}

//返回操作时间
function toTime(cellData, recordIndex,columInde){
  var listDate =  this.getCellData(recordIndex,"listDate");
  return listDate.substr(0,10);
}
//返回操作 项点击查看明细
function toJiLu(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var cptlName =  this.getCellData(recordIndex,"cptlName");
  return "<a href=javascript:cpreDetail('" + seqId +"')>点击查看明细</a>";
}
//返回操作 项资产值
function toVal(cellData, recordIndex,columInde){
  var cptlVal =  this.getCellData(recordIndex,"cptlVal");
  var cptlQty =  this.getCellData(recordIndex,"cptlQty");
  return insertKiloSplit(cptlVal,2) + "*";
}
//返回操作 
function toCao(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var cptlName =  this.getCellData(recordIndex,"cptlName");
  var cptlNo =  this.getCellData(recordIndex,"cptlNo");
  var re1 = /\'/gi;
  
  cptlNo = cptlNo.replace(re1,"&lsquo;");
  cptlNo = encodeURIComponent(cptlNo);
  
  cptlName = cptlName.replace(re1,"&lsquo;");
  cptlName = encodeURIComponent(cptlName);
  
  return "<a href=\"#\" onclick=createNewWork('固定资产返库','固定资产名称=" + seqId
  + "&固定资产名=" + cptlName + "&编号=" + cptlNo +"')>退回</a>";
}
function cptlDetail(seqId) {
  myleft = (screen.availWidth-500)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/getSelect.act?seqId=" + seqId,"read","height=570,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
function cpreDetail(seqId) {
  myleft = (screen.availWidth-500)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/getSelectID.act?seqId=" + seqId,"read","height=370,width=750,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=150,resizable=yes");
}
</script>
</head>
<body onLoad="doInit();" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3"> 我的固定资产列表</span>
    </td>
    </tr>
    </table>
<br>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull">
</div>
<br>
<div align="center">
<!-- 
<input type="button"  value="返回" class="BigButton" onClick="history.back();">
 -->
</div>
</body>
</html>