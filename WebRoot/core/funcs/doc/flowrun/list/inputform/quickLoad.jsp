<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String itemId = request.getParameter("itemId");
String itemData = request.getParameter("itemData");
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择历史数据进行快速录入</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%><%=moduleContextPath %>/workflowUtility/utility.js" ></script>
<script type="text/javascript">
var runId = "<%=runId%>";
var flowId = "<%=flowId%>";
var itemId = "<%=itemId%>";
var itemData = "<%=itemData %>";
function doInit1() {
  var item = itemId.substr(5);
  var url = contextPath + "<%=moduleSrcPath %>/act/YHQuickLoadAct/getQuickLoad.act";
  var json = getJsonRs(url , "runId=" + runId + "&flowId=" + flowId + "&itemId=" + item + "&selectedItem=" + encodeURIComponent(itemData));
   if (json.rtState == "0") {
    var data = json.rtData;
    if (data.length > 0) {
      $('thead').show();
      $('tbody').show();
      $('tfoot').hide();
      $('count').update(data.length);
      $('tbody1').show();
      for (var i = 0 ;i < data.length ;i++) {
        var tmp = data[i];
        addRow1(tmp , i);
      }
    } else {
      $('thead').hide();
      $('tbody').hide();
      $('tfoot').show();
    }
  }
}
function addRow1(data , i) {
  var runId = data.runId;
  var runName = data.runName;
  var itemData = data.itemData;
  var itemDataAll = data.itemDataAll;
  var flowId = data.flowId;

  
  var className1 = "TableLine" + (i % 2 + 1);
  var tr = new Element("tr", {'class':className1});
  $('tbody').appendChild(tr);
  var td = new Element("td");
  tr.appendChild(td);
  td.noWrap = true;
  var tdStr = runId + "<a href=\"javascript:formView("+runId+","+flowId+")\">"+ runName +"</a>";
  td.update(tdStr);
  
  var td2 = new Element("td");
  tr.insert(td2);
  td2.update(itemData);
  var td3 = new Element("td");
 
  tr.insert(td3);
  
  td3.noWrap = true;
  var td3Str = "<input type=\"hidden\" id=\"CONTENT_"+i+"\" name=\"CONTENT_"+i+"\" value=\""+itemData+"\"/>"
    + "<input type=\"hidden\" id=\"CONTENT_ALL_"+i+"\" name=\"CONTENT_ALL_"+i+"\" value=\""+itemDataAll+"\">"
    + "<input type=\"button\" class=\"SmallButtonW\" onclick=\"javascript:clickSign('CONTENT_"+i+"')\" value=\"回填\">"
    + "<input type=\"button\" class=\"SmallButtonW\" onclick=\"javascript:clickSignAll('CONTENT_ALL_"+i+"')\" value=\"回填整表\">";
  td3.update(td3Str);
}
function clickSign(tdId) {
  var input = $(tdId).value;
  try {
    opener.$(itemId).value = input;
  } catch (e) {
    opener.document.getElementsByName(itemId)[0].value = input;
  }
  window.close();
}
function clickSignAll(ID)
{
  item_all = $(ID).value;
  while((pos=item_all.indexOf("[@#@]"))>=0)
  {
    pos_tmp=item_all.indexOf(":");
    item_id=item_all.substring(0,pos_tmp);
    item_data=item_all.substring(pos_tmp+1,pos);
    item_all=item_all.substring(pos+5);
    try {
      obj=opener.document.getElementsByName("DATA_"+item_id)[0];
      if(obj)
      {
        if(!obj.readOnly) {
          obj.value=item_data;
        }
      }
    } catch(e){}
  }
  window.close();
}
</script>
</head>
<body onload="doInit1()">
<table border="0" class="Small" cellspacing="0" width="100%" cellpadding="3">
<thead id="thead" style="display:none">
<tr class="TableHeader">
  <td bgcolor="#d6e7ef" align="center" colspan=3><b>选择历史数据进行快速录入(显示前100条)</b></td>
</tr>
<tr class="TableHeader" >
  <td nowrap>流水号/名称(点击名称查看表单)</td>
  <td nowrap>历史记录</td>
  <td nowrap>操作</td>
</tr>
</thead>

<tbody id="tbody"  style="display:none"></tbody>
<tr id="tbody1" class="TableContent" align=center style="display:none">
  <td colspan=3>共<span id="count"></span> 条匹配记录</td>
</tr>
<tfoot id="tfoot"  style="display:none"> 
<tr><td colspan=3 align=center><TABLE class=MessageBox width=250>
<TBODY>
<TR>
<TD id=msgInfo class="msg info">没有匹配的历史数据</TD></TR></TBODY></TABLE>
</td></tr>

</tfoot>
</table>
<br>
<center><input type="button" class="BigButton" value="关闭" onclick="window.close();"></center>
</body>
</html>