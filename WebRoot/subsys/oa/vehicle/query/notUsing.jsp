<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>未使用车辆</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectUseingVehicle.act"; 
  var json=getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcsJson=json.rtData; 
  if(prcsJson.length>0){ 
    var table=new Element('table',{ "width":"95%","cellspacing":"0","cellpadding":"3","class":"TableList"}).update(""
      +"<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
      +"<td align='center'>厂牌型号</td>"
      +"<td align='center'>车牌号</td>"
      +"<td align='center'>司机</td>"
      +"<td align='center'>类型</td>"
      +"<td align='center'>购置日期</td>"
      +"<td align='center'>状态</td>"
      +"<td align='center'>操作</td></tr><tbody>"); 
    $('listDiv').appendChild(table); 
    for(var i=0;i<prcsJson.length;i++){ 
      var prc=prcsJson[i]; 
      var seqId=prc.seqId; 
      var vModel=prc.vModel; 
      var vNum=prc.vNum; 
      var vDriver=prc.vDriver; 
      var classDesc=prc.classDesc; 
      var vDate=prc.vDate; 
      if(vDate!=''){
        vDate = vDate.substr(0,10);
      }
      var vStatus = prc.vStatus;
      var vcStatus = "正常";
      if(vStatus=="0"){
        vcStatus = "正常";
      }
      if(vStatus=="1"){
        vcStatus = "损坏";
      }
      if(vStatus=="2"){
        vcStatus = "维修中";
      }
      if(vStatus=="3"){
        vcStatus = "报废";
      }
      var tr=new Element('tr',{'width':'95%','font-size':'10pt',"class": "TableData"}); 
      $("tbody").appendChild(tr); 
      tr.update("<td align='center'>" 
      + vModel + "</td><td align='center'>" 
      + vNum + "</td><td align='center'>" 
      + prc.vDriverName + "</td><td align='center'>" 
      + classDesc + "</td><td align='center'>" 
      + vDate + "</td><td align='center'>" 
      + vcStatus + "</td><td align='center'>" 
      + "<a href='#' onclick='query_Vehicle(\""+ seqId +"\")'>详细信息 </a>&nbsp;" 
      + "<a href='#' onclick='openOrder(\""+ seqId +"\")'>预定情况</a>&nbsp;</td>");
    } 
  }else{
    var table=new Element('table',{ "width":"260","class":"MessageBox","align":"center"}) 
    .update("<tr>" 
    +"<td class='msg info'  align='center'><div class='content' style='font-size:12pt'>暂无车辆信息</div></td>" 
    +"</tr>"); 
    $('listDiv').appendChild(table);
  }
}
function query_Vehicle(seqId) { 
  var requestURL= "<%=contextPath%>/subsys/oa/vehicle/manage/queryVehicle.jsp?seqId=" + seqId;
  openDialogResize(requestURL , 600, 450); 
}
function openOrder(seqId) {
  window.open('<%=contextPath%>/subsys/oa/vehicle/orderDetail.jsp?seqId=' + seqId,'','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=280,top=150,resizable=yes');
}
</script>
</head>
<body topmargin="5" onload="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vehicle.gif" HEIGHT="20"><span class="big3"> 未使用车辆</span>
    </td>
  </tr>
</table>
<div id="listDiv">
</div>
 
</body>
 
</html>

