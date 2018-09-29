<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议详情</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingRoomAct";
function doInit(){
	var url = requestURL + "/getRoomDetail.act";
	var json = getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson = json.rtData;
	if(prcsJson.length > 0){
		var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='20%' align='center'>名称</td>"
			+ "<td nowrap width='20%' align='center'>可容纳人数</td>"				
			+ "<td nowrap width='20%' align='center'>设备情况</td>"				
			+ "<td nowrap width='20%' align='center'>所在地点</td>"		
			+ "<td nowrap width='20%' align='center'>会议室描述</td>"	
			+ "</tr><tbody>";	

		table.update(strTable);
		$('roomDetailDiv').update(table);
		for(var i = 0;i<prcsJson.length;i++){
			var prcs = prcsJson[i];
			var mroomId = prcs.seqId;
			var mrName = prcs.mrName;
			var mrCapacity = prcs.mrCapacity;
			var mrDevice = prcs.mrDevice;
			var mrDesc = prcs.mrDesc;
			var mrPlace = prcs.mrPlace;

			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  										
			var tr=new Element('tr',{'width':'90%','class': className ,'font-size':'10pt'});		
			table.firstChild.appendChild(tr);	
			var str = "<td align='center'>"			
				+ mrName + "</td><td align='center'>"					
				+ mrCapacity + "</td><td align='center'>"					
				+ mrDevice + "</td><td align='center'>"					
				+ mrPlace + "</td><td align='center'>"
				+ mrDesc + "</td>";
				tr.update(str);
		}
		$("closeDiv").show();
	}else{
		$("noInfoDiv1").show();
	}
}
</script>
</head>
<body onload=" doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" HEIGHT="20" width="20" align="absmiddle"><span class="big3"> 会议室详情</span>
    </td>
  </tr>
</table>
<br>
<div id="roomDetailDiv"></div>
<br>

<div align="center" id="closeDiv" style="display: none">
  <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</div>

<div id="noInfoDiv1" style="display:none">
<table class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无会议室信息</div>
    </td>
  </tr>
</table>
<div align="center">
	<input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</div>
</div>
</body>
</html>