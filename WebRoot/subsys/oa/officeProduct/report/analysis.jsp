<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String careDate1 = request.getParameter("careDate1") == null ? "" :  request.getParameter("careDate1");
  String careDate2 = request.getParameter("careDate2") == null ? "" :  request.getParameter("careDate2");
  String mapType = request.getParameter("mapType") == null ? "" :  request.getParameter("mapType");
  String officeDepository = request.getParameter("officeDepository") == null ? "" :  request.getParameter("officeDepository");
  String officeProtype = request.getParameter("officeProtype") == null ? "" :  request.getParameter("officeProtype");
  String product = request.getParameter("product") == null ? "" :  request.getParameter("product");
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
.pgPanel {
  display:none;
}

.TableBlock td.TableData,  .TableBlock .TableData td{
  padding: 1px;
  text-align:center;
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript">
var module = "<%=module%>";
var mapType = "<%=mapType%>";
var strXML = "";
var moduleName = "";
var choseStr = "";
var param = "";
param = "careDate1=" + encodeURIComponent("<%=careDate1%>");
param += "&careDate2=" + encodeURIComponent("<%=careDate2%>");
param += "&officeDepository=" + encodeURIComponent("<%=officeDepository%>");
param += "&officeProtype=" + encodeURIComponent("<%=officeProtype%>");
param += "&product=" + encodeURIComponent("<%=product%>");
param += "&module=" + encodeURIComponent("<%=module%>");
param += "&mapType=" + encodeURIComponent("<%=mapType%>");
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/report/act/YHReportAnalysisAct/getAnalysis.act?"+param;
  if(module == "OFFICE_WPZB") {
	  moduleName = "物品总表";
	  var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"hidden", name:"seqId",     text:"顺序号",dataType:"int"},
	       {type:"data", name:"proName",     width: '10%', text:"办公用品名称" ,align: 'center'},
	       {type:"data", name:"proUnit",     width: '10%', text:"计量单位" ,align: 'center' },
	       {type:"data", name:"proStock",    width: '10%', text:"当前库存" ,align: 'center' },
	       {type:"data", name:"bugNum",      width: '10%', text:"采购量" ,align: 'center' },
	       {type:"data", name:"getNum",      width: '10%', text:"领用量" ,align: 'center' },
	       {type:"data", name:"borrowNum",   width: '10%', text:"借出量" ,align: 'center' },
	       {type:"data", name:"returnNum",   width: '10%', text:"归还量" ,align: 'center' },
	       {type:"data", name:"unreturnNum", width: '10%', text:"未归还量" ,align: 'center' ,render: unreturnNum},
	       {type:"data", name:"dumping",     width: '10%', text:"报废量" ,align: 'center' }
	       ]
	  };
	}
  else if(module == "OFFICE_LYWP"){
    if(mapType != ""){
      var rtJson = getJsonRs(url);
      if(rtJson.rtState == "0"){
        strXML = rtJson.rtData.data;
      }
      else{
        alert(rtJson.rtMsrg); 
      }
    }
    else{
      moduleName = "领用物品报表";
      $('titleName').innerHTML = moduleName;
   	  var requestUrl ="<%=contextPath%>/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/selectDeptToAttendance.act";
   	  var rtJson = getJsonRs(requestUrl);
   	  if(rtJson.rtState == "1"){
   	    alert(rtJson.rtMsrg); 
   	    return ;
   	  }
   	  var userId = rtJson.rtMsrg;
   	  var prcs = rtJson.rtData;
   	  if(prcs.length>0){
   	    var table = new Element('table',{"bordercolor":"#000000", "width":"95%","style":"'border-collapse:'collapse'" ,"border":"1", "cellspacing":"0","cellpadding":"2" ,"bordercolor":"#000000" ,"class":"TableBlock" ,"align":"center"}).update("<tbody id='tbody'></tbody>");
   	    $("bodyDiv").update(table);
   	    for(var i=0;i<prcs.length;i++){
   	      var prc = prcs[i];
   	      var deptId =  prc.value;
   	      var deptIdDesc = prc.text;
   	      var giftOutPrcs = getGiftOutByDeptId(deptId);
   	      var giftOutStr = "";
   	      if(giftOutPrcs.length>0){ 
   	        giftOutStr = giftOutStr + " <table style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2 bordercolor='#000000' class=TableBlock cellpadding=3 align=left>"
   	        +"<tr class='TableControl' >"
   	        +"<td nowrap align='center' width='20%'><b>领用人</b></td>"
   	        +"<td nowrap align='center' width='20%'><b>办公用品名称</b></td>"
   	        +"<td nowrap align='center' width='10%'><b>领用总量</b></td>"
   	        +"<td nowrap align='center' width='20%'><b>领用日期</b></td>"
   	        +"<td nowrap align='center' width='10%'><b>单价</b></td>"
   	        +"<td nowrap align='center' width='20%'><b>总价</b></td></tr>"
   	        var total = 0;
   	        for(var j = 0; j<giftOutPrcs.length;j++){
   	          var giftOut = giftOutPrcs[j];
   	          var transTotal = giftOut.transTotal;
   	          if(giftOut.transDate.length > 10){
   	        	  giftOut.transDate = giftOut.transDate.substr(0,10);
   	          }
   	          giftOutStr = giftOutStr +  "<tr class=TableData>"
   	          +"<td>"+giftOut.userName+"</td>"
   	          +"<td>"+giftOut.proName+"</td>"
   	          +"<td>"+giftOut.transQty+"</td>"
   	          +"<td>"+giftOut.transDate+"</td>"  
   	          +"<td>"+giftOut.price+"</td>"
   	          +"<td>"+giftOut.transQty*giftOut.price+"</td></tr>";
   	        }
   	        giftOutStr = giftOutStr + "</table>";
   	      }
   	      newTrElement(table,[],2,{className:"TableData"},[deptIdDesc,giftOutStr])
   	    }  
   	  } 
    }
    
    return;
  }
  else if(module == "OFFICE_WHJL"){
    moduleName = "维护记录报表";
    var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId",  text:"顺序号", dataType:"int"},
         {type:"data", name:"proName",  width: '10%', text:"办公用品名称" ,align: 'center'},
         {type:"data", name:"transDate",width: '10%', text:"维护时间" ,align: 'center' ,render:splitDateFunc},
         {type:"data", name:"operator", width: '10%', text:"操作员" ,align: 'center' },
         {type:"data", name:"remark",   width: '10%', text:"备注" ,align: 'center' }
         ]
    };
	}
  else if(module == "OFFICE_WGHWP"){
     moduleName = "未归还物品报表";
     var cfgs = {
       dataAction: url,
       container: "listContainer",
       sortIndex: 1,
       sortDirect: "desc",
       colums: [
          {type:"hidden", name:"seqId",     text:"顺序号", dataType:"int"},
          {type:"data", name:"proName",     width: '10%',  text:"办公用品名称" ,align: 'center'},
          {type:"data", name:"noreturnNum", width: '10%',  text:"未归还数量" ,align: 'center' ,render:unreturnNum},
          {type:"hidden", name:"borrowNum", text:"借用数", dataType:"int"},
          {type:"hidden", name:"returnNum", text:"归还数", dataType:"int"},
          {type:"data", name:"userName",    width: '10%',  text:"借用人" ,align: 'center' },
          {type:"data", name:"transDate",   width: '10%',  text:"借用日期" ,align: 'center' ,render:splitDateFunc}
          ]
     };
  }
  else if(module == "OFFICE_TZ"){
     moduleName = "台帐报表";
     var cfgs = {
       dataAction: url,
       container: "listContainer",
       sortIndex: 1,
       sortDirect: "desc",
       colums: [
          {type:"data", name:"transDate", width: '10%', text:"发生日期" ,align: 'center',render:splitDateFunc},
          {type:"data", name:"proName",   width: '10%', text:"办公用品名称" ,align: 'center' },
          {type:"data", name:"transFlag", width: '10%', text:"登记类型",align: 'center' ,render:transFlag},
          {type:"data", name:"num",       width: '10%', text:"数量",align: 'center', dataType:"int", render: renderFlag},
          {type:"data", name:"proUnit",   width: '10%', text:"计量单位" ,align: 'center' },
          {type:"data", name:"proPrice",  width: '10%', text:"单价" ,align: 'center' ,render:amountFunc},
          {type:"data", name:"operator",  width: '10%', text:"操作员" ,align: 'center' }
          ]
     };
  }
  else {
	  var showText = "";
	  if(module == "OFFICE_CGWP"){
		  if(mapType != ""){
			  var rtJson = getJsonRs(url);
			  if(rtJson.rtState == "0"){
				  strXML = rtJson.rtData.data;
			  }
			  else{
          alert(rtJson.rtMsrg); 
			  }
			  return;
		  }
      moduleName = "采购物品报表";
      showText = "单价";
	  }
	  
	  else if(module == "OFFICE_JYWP"){
		  moduleName = "借用物品报表";
		  showText = "借用人";
		}
	  else if(module == "OFFICE_GHWP"){
      moduleName = "归还物品报表";
      showText = "归还人";
    }

    else if(module == "OFFICE_BFWP"){
      moduleName = "报废物品报表";
      showText = "单价";
    }
    var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"proName",    width: '10%', text:"办公用品名称" ,align: 'center'},
         {type:"data", name:"transFlag",  width: '10%', text:"登记类型" ,align: 'center' ,render:transFlag},
         {type:"data", name:"transQty",   width: '10%', text:"数量" ,align: 'center' },
         {type:'data', name:'username',   width: '10%', text:showText ,align: 'center' ,render:amountFunc},
         {type:"data", name:"transDate",  width: '10%', text:"登记日期" ,align: 'center' ,render:splitDateFunc},
         {type:"data", name:"operator",   width: '10%', text:"登记员" ,align: 'center' },
         {type:"data", name:"remark",     width: '10%', text:"备注"  }
         ]
    };
  }
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
	  $('print').style.display = 'none';
	  $('export').style.display = 'none';
    WarningMsrg('无'+ moduleName +'信息！', 'msrg');
  }
  $('titleName').innerHTML = moduleName;
}

function unreturnNum(cellData, recordIndex, columIndex){
	var borrowNum = this.getCellData(recordIndex,"borrowNum");
	var returnNum = this.getCellData(recordIndex,"returnNum");
	return borrowNum - returnNum;
}

function renderFlag(cellData, recordIndex, columIndex){
  var transFlag = this.getCellData(recordIndex,"transFlag");
  if(transFlag == 1 || transFlag == 2 || transFlag == 4)
    cellData = cellData*(-1);
  return cellData;
}

/**
 * 截取时间
 * @param cellData
 * @param recordIndex
 * @param columInde
 * @return
 */
function splitDateFunc(cellData, recordIndex,columInde) {
  var str = "";
  if(cellData){
    str = cellData.substr(0,10);
  }
  return "<center>" + str + "</center>";
}

function transFlag(cellData, recordIndex,columInde) {
  var str = "";
  switch(cellData){
    case '0':str = "采购";break;
    case '1':str = "领用";break;
    case '2':str = "借用";break;
    case '3':str = "归还";break;
    case '4':str = "报废";break;
    case '5':str = "维护";break;
  }
  return "<center>" + str + "</center>";
}

function amountFunc(cellData, recordIndex, columIndex) {
  var str = cellData;
  if(!isNaN(cellData)){
    str = insertKiloSplit(cellData,2);
  }
  return "<center>" + str + "</center>";
}


function getGiftOutByDeptId(deptId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/officeProduct/report/act/YHReportAnalysisAct/getAnalysis.act?"+param+"&deptId="+deptId;
  var json = getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcs = json.rtData;
  return prcs;
}
function newTrElement(tbObj,tbAttri,index,tdAttri,config){
  var currentRows = tbObj.rows.length;//原来的行数

  var mynewrow = tbObj.insertRow(currentRows);
   //tr给属性赋值

  if(tbAttri.className){
    mynewrow.className = tbAttri.className;
  }
  if(tbAttri.width){
    mynewrow.width = tbAttri.width;
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.nowrap = true;
    //td给属性赋值

    if(tdAttri.className){
      mynewcell.className = tdAttri.className;
    }
    if(tdAttri.width){
      mynewcell.width = tdAttri.width;
    }
    if(tdAttri.colSpan){
      mynewcell.colSpan = tdAttri.colSpan;
    }
    if(tdAttri.align){
      mynewcell.align = tdAttri.align;
    }
    mynewcell.innerHTML = "<div align='left'>" + config[i] + "</div>";
  }
}

function printExcel(){

  if(module == "OFFICE_LYWP"){
    $('table').value = $('bodyDiv').innerHTML;
    $('form1').action = "<%=contextPath%>/subsys/oa/officeProduct/report/menuTest.jsp";
    $('form1').submit();
    return;
  }
	
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/report/act/YHReportAnalysisAct/printExcel.act?"+param;
  var param2 = "";
  if(module == "OFFICE_WPZB") {
    param2 = encodeURIComponent("办公用品名称,计量单位,当前库存,采购量,领用量,借出量,归还量,未归还量,报废量");
  }
  else if(module == "OFFICE_CGWP"){
    param2 = encodeURIComponent("办公用品名称,登记类型,数量,单价,登记日期,登记员,备注");
  }
  else if(module == "OFFICE_JYWP"){
    param2 = encodeURIComponent("办公用品名称,登记类型,数量,借用人,登记日期,登记员,备注");
  }
  else if(module == "OFFICE_GHWP"){
    param2 = encodeURIComponent("办公用品名称,登记类型,数量,归还人,登记日期,登记员,备注");
  }
  else if(module == "OFFICE_WGHWP"){
    param2 = encodeURIComponent("办公用品名称,未归还数量,借用数,归还数,借用人,借用日期");
  }
  else if(module == "OFFICE_BFWP"){
	  param2 = encodeURIComponent("办公用品名称,登记类型,数量,单价,登记日期,登记员,备注");
  }
  else if(module == "OFFICE_WHJL"){
    param2 = encodeURIComponent("办公用品名称,维护时间,操作员,备注");
  }
  else if(module == "OFFICE_TZ"){
    param2 = encodeURIComponent("发生日期,办公用品名称,登记类型,数量,计量单位,单价,操作员");
  }
	
  $('form1').action = url+"&title="+param2;
  $('form1').submit();
}
</script>
</head>
<body>
<% if(YHUtility.isNullorEmpty(mapType)) {%>
<table border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3" id="titleName"></span>
    </td>
    <td align="right">
     <input id='print' type="button" class="SmallButton" value="打印" onClick="window.print();">&nbsp;&nbsp;&nbsp;&nbsp;
     <input id='export' type="button" class="SmallButton" value="导出" onClick="printExcel();">
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:98%;">
</div>
<div id="msrg">
</div>
<div id="bodyDiv" align="center"></div>

<form action="" id="form1" name="form1" method="post">
<input type="hidden" id="table" name="table">
</form>
<script type="text/javascript"> 
doInit();
</script>
<%}else{ %>
<center>
  <!-- START Script Block for Chart FactorySum -->
  <div id="FactorySumDiv" align="center">
    <br>
    <div id="chart1div" align="center">
    Chart.
    </div>
  <script type="text/javascript"> 
      //Instantiate the Chart 
    doInit();
    if(mapType == "1"){
      choseStr = "Pie3D.swf";
    }
    else if(mapType == "2"){
      choseStr = "Column3D.swf";
    }
    var chart1 = new FusionCharts("<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/"+choseStr+"", "chart1Id", "600","300", "0", "0");
    chart1.setTransparent("false");
    chart1.setDataXML(strXML);
    chart1.render("chart1div");
  </script>
  <form name='frmUpdate' id="frmUpdate" >
  </form>
  </div>
</center>
<%} %>
</body>
</html>
