<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page  import="yh.subsys.oa.officeProduct.officeType.data.*"%> 
<%
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
  List<YHOfficeDepository> officeDep = (List<YHOfficeDepository>)request.getAttribute("findOfficeDepS");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript">
var module = "<%=module%>";
function doInit(){
	setDate();
	getOfficeDepository();
}

//获取全体部门列表
function getOfficeDepository(){
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/report/act/YHReportAnalysisAct/getOfficeDepository.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("officeDepository");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
}

function doSubmit(){
  var chose = "";
  if($('mapType')){
	  var mapTypeStr = document.getElementsByName("mapType");
	  for(var i = 0; i < mapTypeStr.length; i++){
	    if(mapTypeStr[i].checked)
	      chose = mapTypeStr[i].value;
	  } 
	}
  var form1 = $('form1').serialize();
  parent.tumain.location = "<%=contextPath%>/subsys/oa/officeProduct/report/analysis.jsp?chose="+chose+"&"+form1+"&module="+module;
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'careDate1',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
     inputId:'careDate2',
     property:{isHaveTime:false}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}

//点击办公用品库 显示办公类型信息
function depositoryOfType(id){
	depositoryOfProducts('');
	if(id.trim() == ""){
	  $("officeProtype").innerHTML="";
	  var officeType = $('officeProtype');
	  officeType.options.add(new Option("请选择", ""));
		return;
	}
  $("officeProtype").innerHTML="";
  var officeType = $('officeProtype');
  officeType.options.add(new Option("请选择", ""));
  var options = "";
  var par = "officeId="+id;
  var url = contextPath+'/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeType.act';
  var rtJson = getJsonRs(url,par);
  if(rtJson.rtState == "0"){
    var rtData = rtJson.rtData;
    var listData = rtData.listData;
    for(var i = 0 ; i < listData.length ;  i ++ ){
      officeType.options.add(new Option(listData[i].typeName, listData[i].seqId));
    }
  }
}

//点击办公用品类型 显示办公用品信息
function depositoryOfProducts(id){
	if(id.trim() == ""){
	  $("product").innerHTML="";
	  var proId = $('product');
	  proId.options.add(new Option("请选择", ""));
		return;
	}
  $("product").innerHTML="";
  var proId = $('product');
  proId.options.add(new Option("请选择", ""));
  var options ="";
  var par = "officeId="+id;
  var url = contextPath + '/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeProducts.act';
  var rtJson = getJsonRs(url,par);
  if(rtJson.rtState =="0"){
    var rtData = rtJson.rtData;
    var listData = rtData.listData;
    for(var i =0; i<listData.length; i++){
      proId.options.add(new Option(listData[i].proName, listData[i].seqId));
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<form action="" method="post" name="form1" id="form1" target="tu_main">
<table align="center" width="100%" class="TableBlock">
<% if("OFFICE_CGWP".equals(module) || "OFFICE_LYWP".equals(module)) {%>
    <tr>
      <td nowrap class="TableContent">日期：</td>
      <td class="TableData" colspan="3">
         &nbsp;&nbsp;<input type="text" name="careDate1" id="careDate1" size="11" maxlength="10"  class="BigInput" value="" readonly>
                     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;&nbsp;至&nbsp;
                     <input type="text" name="careDate2" id="careDate2" size="11" maxlength="10"  class="BigInput" value="" readonly>
                     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableContent">统计图：</td>
      <td class="TableData" >
        <input type="radio" name='mapType' value="1" checked><label for="mapType1">饼图</label>
        <input type="radio" name='mapType' value="2"><label for="mapType2">柱状图</label>
        <input type="radio" name='mapType' value=""><label for="mapType3">数据表</label>
      </td>
    </tr> 
<%}else{ %>   
    <tr>
      <td nowrap class="TableContent" width="60">日期：</td>
      <td class="TableData" colspan="5">
         &nbsp;&nbsp;<input type="text" name="careDate1" id="careDate1" size="11" maxlength="10"  class="BigInput" value="" readonly>
                     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;&nbsp;至&nbsp;
                     <input type="text" name="careDate2" id="careDate2" size="11" maxlength="10"  class="BigInput" value="" readonly>
                     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>    
<%} %> 
    <tr>
      <td nowrap class="TableContent" width="80" >办公用品库：</td>
      <td class="TableData" nowra>
         &nbsp;&nbsp;<select id="officeDepository" name="officeDepository" style="width:150px" onchange="depositoryOfType(this.value);">
           <option value="">请选择</option>
         </select>
      </td>
      <td nowrap class="TableContent" width="80" >办公用品类别：</td>
      <td class="TableData" nowrap>
         &nbsp;&nbsp;<select id="officeProtype" name="officeProtype" style="width:150px" onchange="depositoryOfProducts(this.value);">
           <option value="">请选择</option>
         </select>
      </td>
      <td nowrap class="TableContent" width="80" >办公用品：</td>
      <td class="TableData" nowrap >
         &nbsp;&nbsp;<select id="product" name="product" style="width:150px">
           <option value="">请选择</option>
         </select>
      </td>
    </tr>
    <tr>
      <td class="TableData" colspan="6" align="center">
         <input type="button" value="查询" onclick="doSubmit()">
      </td>
    </tr> 
  </table>
</form>
</body>
</html>
