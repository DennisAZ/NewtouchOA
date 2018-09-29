<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核项目</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreFlowLogic.js"></script>
<script type="text/javascript">
function doInit() {
  var seqId = ${param.seqId};
  var groupFlag = "${param.groupFlag}";
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getScoreFlowData.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url, "groupFlag="+groupFlag);
  var flag = 0;
  var table = "";
  if(rtJson.rtState == "0"){
    if(groupFlag == "0"){
      table = new Element('table',{ "width":"70%","class":"TableList","align":"center"})
            .update("<tbody id='tbody'><tr class='TableHeader'>"
            +"<td nowrap width='' align='center'>考核项目</td>"
            +"<td nowrap width='' align='center'>分值范围</td></tr><tbody>");
      $('listDiv').appendChild(table);
      for(var i = 0; i < rtJson.rtData.length; i++){
        flag++;
        var seqId = rtJson.rtData[i].seqId;
        var itemName = rtJson.rtData[i].itemName;
        var groupId = rtJson.rtData[i].groupId;
        var min = rtJson.rtData[i].min.trim();
        var max = rtJson.rtData[i].max.trim();
        var re1 = /\'/gi; 
        itemName = itemName.replace(re1,"&lsquo;"); 
        var tr=new Element('tr',{'class':'TableLine1'});   
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"+itemName+"</td><td align='center'>"+min+"～"+max+""     
          + "</td>"     
        );
      }
    }else{
      table = new Element('table',{ "width":"70%","class":"TableList","align":"center"})
            .update("<tbody id='tbody'><tr class='TableHeader'>"
            +"<td nowrap width='' align='center'>考核项目</td></tr><tbody>");
            $('listDiv').appendChild(table);
      for(var i = 0; i < rtJson.rtData.length; i++){
        flag++;
        //var seqId = rtJson.rtData[i].seqId;
        var itemName = rtJson.rtData[i].itemName;
        var groupId = rtJson.rtData[i].groupId;
        var re1 = /\'/gi; 
        itemName = itemName.replace(re1,"&lsquo;"); 
        var tr=new Element('tr',{'class':'TableLine1'});   
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"+itemName+"</td>"     
        );
      }
     }
  }else{
  	alert(rtJson.rtMsrg); 
  }
  var groupDesc = getGroupName(seqId);
  var tr=new Element('tr',{'class':'TableLine1'});			
  table.firstChild.appendChild(tr);
  var temp = "";
  if (groupFlag == "0") {
    temp = " colspan='2'";
  }
  tr.update("<td "+temp+">描述："+groupDesc+"</td>"					
  );
}

</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/workflow.gif" align="absmiddle"><span class="big3">&nbsp;考核项目</span><br>
    </td>
    </tr>
</table>
<br>
<div id="listDiv"></div>
<div align="center">
   <br>
   <input type="button" class="BigButton" value="关闭" onClick="window.close();">
</div>

</body>
</html>