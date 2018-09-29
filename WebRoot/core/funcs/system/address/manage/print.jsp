<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String groupId = request.getParameter("groupId");
  if(groupId == null) {
    groupId = "";
  }

%>
<head>
<STYLE type=text/css>.HdrPaddingTop {
 PADDING-TOP: 5px
}
.HdrPaddingBttm {
 PADDING-BOTTOM: 5px
}
BODY {
 MARGIN-TOP: 2px; MARGIN-LEFT: 0px; COLOR: #000000; MARGIN-RIGHT: 0px
}
A {
 FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
BODY {
 FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
 FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TD {
 FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TR {
 FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
 BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px
}
.FF {
 COLOR: #000000
}
</STYLE>
<title>通讯簿打印</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/private/js/privateUtil.js"></script>
<script type="text/javascript">
var groupId = "<%=groupId%>";

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/getPrintList.act";
  var rtJson = getJsonRs(url, "groupId="+groupId);
  var flag = 0;
  if(rtJson.rtState == "0"){
    var table = table = new Element('table',{"width":"640","align":"center","cellSpacing":"0", "cellPadding":"0", "border":"0"})
    .update("<tbody id='tbody'>"
      + "<tbody>");
    $('listDiv').appendChild(table);
    var tr = null;
    for(var i = 0; i < rtJson.rtData.length; i++){
     flag++;
     var seqId = rtJson.rtData[i].seqId;
     var psnName = rtJson.rtData[i].psnName;
     var addDept = rtJson.rtData[i].addDept;
     var postNoDept = rtJson.rtData[i].postNoDept;
     var telNoDept = rtJson.rtData[i].telNoDept;
     var telNoHome = rtJson.rtData[i].telNoHome;
     var mobilNo = rtJson.rtData[i].mobilNo;
     var faxNoDept = rtJson.rtData[i].faxNoDept;
     var email = rtJson.rtData[i].email;
     if(flag % 2 == 1){
       tr = new Element('tr');			
       table.firstChild.appendChild(tr);
       tr.insert("<td class='HdrPaddingBttm' width='49%'><table cellSpacing='0' cellPadding='0' width='100%' align='center' border='0'><tr><td height='20' valign='BOTTOM' style='font-size:15;font-weight:bold;background:#CDCDCD;'>"	
           + psnName +"</td></tr><tr><td height='3'></td></tr><tr><td align='left'>" 
           + addDept +"</td></tr><tr><td align='left'>"
           + postNoDept + "</td></tr><tr><td align='left'>商务电话：&nbsp;&nbsp;"  
           + telNoDept + "</td></tr><tr><td align='left'>住宅电话：&nbsp;&nbsp;"
           + telNoHome + "</td></tr><tr><td align='left'>移动电话：&nbsp;&nbsp;"  
           + mobilNo + "</td></tr><tr><td align='left'>商务传真：&nbsp;&nbsp;"
           + faxNoDept + "</td></tr><tr><td align='left'>电子邮件：&nbsp;&nbsp;"
           + email + "</td></tr>"   
           + "</table></td><td class='HdrPaddingBttm' width='2%'></td>");
     }else {
       tr.insert("<td class='HdrPaddingBttm' width='49%'><table cellSpacing='0' cellPadding='0' width='100%' align='center' border='0'><tr><td height='20' valign='BOTTOM' style='font-size:15;font-weight:bold;background:#CDCDCD;'>" 
           + psnName +"</td></tr><tr><td height='3'></td></tr><tr><td align='left'>" 
           + addDept +"</td></tr><tr><td align='left'>"
           + postNoDept + "</td></tr><tr><td align='left'>商务电话：&nbsp;&nbsp;"  
           + telNoDept + "</td></tr><tr><td align='left'>住宅电话：&nbsp;&nbsp;"
           + telNoHome + "</td></tr><tr><td align='left'>移动电话：&nbsp;&nbsp;"  
           + mobilNo + "</td></tr><tr><td align='left'>商务传真：&nbsp;&nbsp;"
           + faxNoDept + "</td></tr><tr><td align='left'>电子邮件：&nbsp;&nbsp;"
           + email + "</td></tr>"   
           + "</table></td><td class='HdrPaddingBttm' width='2%'></td>");
     }
     //<tr><td class='HdrPaddingBttm' colspan='3' height='10'></td></tr>
    }
    if(flag <= 0){
      WarningMsrg('无打印信息', 'msrg');
      $("listDiv").style.display = 'none';
    }
  }else{
   alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit();">
<div id="listDiv" align="center"></div>
<br>
<div id="msrg"></div>
</body>
</html>