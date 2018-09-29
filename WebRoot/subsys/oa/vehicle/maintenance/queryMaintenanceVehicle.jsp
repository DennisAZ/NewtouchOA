<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>维护记录管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type ="text/javascript">
var seqId = '<%=seqId%>';
function doInit(orderType){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/selectVehicleMaintenanceByVId.act?vId="+seqId+"&orderType="+orderType; 
    var json = getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    var prcsJson = json.rtData;
    var arrowGif = "arrow_down.gif";
    if(orderType=='asc'){
      orderType = "desc";
      arrowGif = "arrow_up.gif";
 
    }else{
      orderType = "asc";
      arrowGif = "arrow_down.gif";
    }
    if(prcsJson.length>0){
      var table = new Element('table',{"class":"TableList" ,"width":"95%","align":"center"}).update("<tbody id='tbody'><tr class='TableHeader'>"
          +" <td nowrap align='center'>车牌号</td>"
          +"<td nowrap align='center'>维护类型</td>"
          +"<td nowrap align='center'>维护原因</td>"
          +"<td nowrap align='center' onclick=doInit('"+orderType+"') style='cursor:pointer;'><u>维护日期</u><img border=0 src='<%=imgPath%>/"+arrowGif+"' width='11' height='10'></td> "     
          +"<td nowrap align='center'>经办人</td>"
          +"<td nowrap align='center'>维护费用</td>"
          +"<td nowrap align='center'>备注</td>"
          +"<td nowrap align='center'>操作</td></tr><tbody>");
      $("maintenanceList").innerHTML = "";
      $("maintenanceList").appendChild(table);
      for(var i=0; i<prcsJson.length ; i ++){
         var prc = prcsJson[i];
         var vmType = prc.vmType;
         var vmTypeDesc = vmType;
         var tr = new Element('tr',{"class":"TableData"});
         $("tbody").appendChild(tr);
         tr.update("<td align='center'>" + prc.vNum + "</td>"
             +"<td align='center'>" + vmTypeDesc + "</td>"
             +"<td align='center'>" + prc.vmReason + "</td>"
             +"<td align='center'>" + prc.vmRequestDate + "</td>"
             +"<td align='center'>" + prc.vmperson + "</td>"
             +"<td align='center'>" + prc.vmFee + "</td>"
             +"<td align='center'>" + prc.vmRemark + "</td>"
             +"<td align='center'  nowrap>" 
             +"<a href='javascript:update_vehicle(" + prc.seqId + ");'>修改</a>&nbsp;"
             +"<a href='javascript:delete_vehicle(" + prc.seqId + ");'>删除</a></td>");
      }

      var tr = new Element('tr',{"class":"TableControl"});
      $("tbody").appendChild(tr);
      tr.update("<td nowrap align='center'><b>合计：</b></td>"
         //+"<td nowrap align='center' colspan='4'></td>"
         //+"<td nowrap align='right'><span id = 'feeTotal'></span> </td>"
         +"<td nowrap align='center' colspan='7'>"+json.rtMsrg+"</td>");

      var div = new Element('div' ,{"align":"center"});
      $("maintenanceList").appendChild(div);
      div.update("<br><input type='button' value='导出' class='BigButton' onclick='exportCSV(<%=seqId%>)'>&nbsp;&nbsp;"
       +"<input type='button' class='BigButton' value='打印' onclick='window.print();'>&nbsp;&nbsp;"
       +"<input type='button' value='关闭' class='BigButton' onclick='window.close();'>");
    }else{
      var table = new Element('table',{"class":"MessageBox", "align":"center" ,"width":"300"}).update("<tr>"
        +"<td class='msg info'>"
        +"<div class='content' style='font-size:12pt'>暂无车辆维护信息</div>"
        +"</td></tr>");
      $("maintenanceList").innerHTML = "";
      $("maintenanceList").appendChild(table);
    }	
}
function exportCSV(vId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/exportCSVByVId.act?vId="+vId;
  window.location.href = requestURL;
}

function delete_vehicle(seqId){
  var msg='确认要删除该维护信息吗？';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleMaintenanceAct/deleteVehicleMiantenById.act?seqId="+seqId ;
    var json = getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    } 
    //window.location.reload();
    doInit('desc');
  }
}
function update_vehicle(seqId){ 
  window.location.href = "<%=contextPath%>/subsys/oa/vehicle/maintenance/updatevehiclemainten.jsp?seqId=" + seqId;
}
</script>
</head>
<body class="" topmargin="5" onload = "doInit('desc');">
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/vehicle.gif" HEIGHT="20"><span class="big3"> 维护记录管理</span>
    </td>
  </tr>
</table>
 <br>
<div id="maintenanceList"></div> 

</body>
</html>