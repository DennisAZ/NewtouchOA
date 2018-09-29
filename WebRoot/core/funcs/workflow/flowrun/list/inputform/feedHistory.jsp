<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的会签意见历史</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function click_sign(ID)
{ 
 
  var targetelement=document.getElementsByName(ID)[0];
  var editor = window.opener.FCKeditorAPI.GetInstance('FCKeditor1') ;
  editor.SetData(targetelement.value);
  window.close();
}
function doInit() {
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFeedbackAct/getRecFeedback.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var data = json.rtData;
    if (data.length > 0 ) {
      for (var i = 0 ;i< data.length ;i++) {
        var d = data[i];
        createTr (d , i ) ;
      }
    } else {
      $('feedbackTable').hide();
      $('no').show();
    }
  }
}
function createTr(data , i ) {
  var className = "TableLine1";
  if (i % 2 == 1) {
    className = "TableLine2";
  }
  var contentVi = data.contentView;
  var content = data.content;
  var input1 = contentVi 
      + "<input type=\"hidden\" name=\"CONTENT_" + i + "\" value=\""+content+"\">";
  var tr = new Element("tr" , {"class":className});
  $('feedbackTbody').appendChild(tr);
  var td = new Element("td");
  register(td , i);
  tr.appendChild(td);
  td.style.cursor = 'hand';
  td.title = content;
  td.update(input1);
}
function register(td , i) {
  td.onclick = function() {
    click_sign("CONTENT_" + i );
  } 
}
</script>
</head>
<body onload="doInit()" class="bodycolor" topmargin="5">
<table id="feedbackTable" border="1" cellspacing="0" width="100%" class="TableBlock" cellpadding="3"  bordercolorlight="#000000" bordercolordark="#FFFFFF"  onMouseover="borderize_on(event)" onMouseout="borderize_off(event)">
<tr class="TableHeader">
  <td bgcolor="#d6e7ef" align="center"><b>选择会签意见（最近的50条）</b></td>
</tr>
<tbody id="feedbackTbody"></tbody>
</table>

<div id="no" style='display:none'>
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info"><span id="span3">尚没有进行过会签操作，无会签历史记录可供选择!</span></td>
        </tr>
    </tbody>
</table></div>
</div>
</body>
</html>