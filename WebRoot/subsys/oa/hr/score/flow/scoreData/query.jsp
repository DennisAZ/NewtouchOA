<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核数据</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var count = 0;
function doInit() {
  var str = "";
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/getItemName.act?groupId=${param.groupId}";
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  //alert(rsText);
  var prcs = json.rtData;
  var strTd = "";
  var itemNameStr = "";
  if (prcs.length > 0) {
    var table = new Element('table',{"class":"TableList","width":"90%","align":"center","cellspacing":"0","cellpadding":"3" });
    var strTable = "<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
    for(var i = 0; i < prcs.length; i++) {
      count++;
      var itemName = prcs[i].itemName;
      strTable += "<td align='center'>" + itemName+ "</td>"
      if(i == "1"){
        itemNameStr = "评分人姓名";
      }else{
        itemNameStr = itemName;
      }
      strTd += "<td align='center'><b>" + itemNameStr + "</b></td>"
     }
     strTable += "<td align='center'>总分</td></tr><tbody>";
     table.update(strTable);
    $('listDiv').appendChild(table); 
    scoreDataList(table,strTd);
  } else {
    var table = new Element('table',{"class":"TableList","align":"center","cellspacing":"0","cellpadding":"3" });
    var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
    for(var i = 0; i < prcs.length; i++) {
      var itemName = prcs[i].itemName;
      strTable += "<td align='center'>" + itemName+"</td>"
    }
    strTable += "</tr><tbody>";
    table.update(strTable);
    $('listDiv').appendChild(table); 
  }
}

//获取被考核人员值
function scoreDataList(table,strTd){
  var anonmity = ${param.anonmity};
  var flowId = ${param.flowId};
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/getScoreDataList.act?flowId=${param.flowId}";
  var rtJson = getJsonRs(url);
  //alert(rsText);
  var flag = 0;
  var scoreStr = "";
  var memoStr = "";
  var num = 0;
  var memoVal = "";
  if(rtJson.rtState == "0"){
  	for(var i = 0; i < rtJson.rtData.length; i++){
      var userName = rtJson.rtData[i].userName;
      //alert(userName);
      var deptName = rtJson.rtData[i].deptName;
      var userPriv = rtJson.rtData[i].userPriv; 
      var rankMan = rtJson.rtData[i].rankMan; 
      var memo = rtJson.rtData[i].memo; 
      var privName = rtJson.rtData[i].privName;
      var score = rtJson.rtData[i].score;
      memoStr = memo.split(',');
      scoreStr = score.split(',');
      var tr = new Element('tr',{"style":"cursor:pointer","onClick":"tdDetail(" + i + ")" ,"align":"center","title":"单击查看评分明细 ","class":"TableData"});
      $('tbody').appendChild(tr);
          var strTable = "<td>" + deptName + "</td><td>" + userName + "</td><td>" + userPriv + "</td>"
          //alert(strTable);
          tr.insert(new Element('td').insert(deptName));
          tr.insert(new Element('td').insert(userName));
          tr.insert(new Element('td').insert(userPriv));
          var detailTdStr = "";
          //alert(count);
          for(var j = 0; j < count - 3; j++){ 
            var td = new Element('td', {}).insert(scoreStr[j].trim());
            tr.insert(td);
            var scoreTdStr = "";
            if(!isNaN(scoreStr[j].trim())){
            	num = num + parseFloat(scoreStr[j]);
            	scoreTdStr = scoreStr[j];
            }
            memoVal = "";
            if(memoStr[j].trim()){
            	memoVal = "<span class='big4'>(批注：" + memoStr[j] + ")</span>";
            }
            detailTdStr += "<td nowrap align='center'> " + scoreTdStr + "<br>" + memoVal + "</td>"
          }
          var td = new Element('td', {}).insert(num);
          tr.insert(td);
          num = 0;
          $('tbody').appendChild(tr);
 		  $('tbody').insert(tr);
 		  if(anonmity !=0){
 			deptName = "****";
 			rankMan = "****";
 			privName = "****";
 		  }
      for(var x = 1; x <= count; x++){
      	//strTd += "<td nowrap align='center'> 考核项目" + x + " </td>";
      }
 		  var detailTable = "<table border='1' width='60%' cellspacing ='1' bgcolor='#000000' class='TableList'>"
 			+ "<tr class='TableData' align='center' style='font-size:10pt'>"
 			
 			+ strTd
 			+ "</rt>"
 			+ "<tr class='TableData' align='center' style='font-size:10pt'>"
 			+ "<td nowrap align='center'> " + deptName + " </td>"
 			+ "<td nowrap align='center'>" + rankMan + " </td>"
 			+ "<td nowrap align='center'> " + privName + "</td>"
 			+ detailTdStr
 			+ "</rt>"
 			+ "</table>"; 
 		  var tr1 = new Element('tr',{"style":"display:none","onDblClick":"tdDetail(" + i + ")", "id":"trId_"+i ,"align":"center","title":"双击关闭子窗口 ","class":"TableData"});
 		  var td1 = "<td align = 'left' colspan='" + (count + 1) + "'><br>"
 			+ detailTable
 			+ "<br></td>";
 		  tr1.insert(td1);
 		  $('tbody').insert(tr1);
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function tdDetail(i){
	if($("trId_" + i).style.display == "none"){
		$("trId_" + i).style.display = "";
		$("trId_" + i).style.cursor = "hand";
	}else{
		$("trId_" + i).style.display = "none";
	}
}

</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/hrms.gif" align="middle"><span class="big3">&nbsp;被考核人员</span>
    &nbsp; &nbsp;<input class="BigButton" type="button" value="关闭" onClick="window.close();">
    </td>
    <td class="Small" ><b>点击条目显示详细打分情况</b></td>
    </tr>
</table>

<div id="listDiv"></div>
</body>
</html>