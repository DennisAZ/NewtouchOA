<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/sso.js" ></script>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript">
var json = null;
var userId = "<%=request.getParameter("userId")%>";
function doInit(){
  rsSSO(ssoUrlGPower + "/userJson.jsp");
  if (!json) {
    setTimeout(doInit, 30);
    return;
  }
  $('userId').value = userId;
  
  //window.json = getJsonRs('userJsonData.jsp');
  

  if (!json instanceof Array){
    return;
  }
  
  json.each(function(e, i){
    var radio = new Element('input', {
      'type': 'radio',
      'value': i,
      'id': 'id' + e.userID,
      'name': 'radioGroup'
    });

    var label =  new Element('label', {
      'for': 'id' + e.userID
    }).update(e.userID + '&nbsp;&nbsp;' + e.userDesc);

    var td = new Element('td',{
      'class': 'TableData',
      'nowrap': 'true'
    });

    var tr = new Element('tr',{
    });
    
    $('bindRadio').insert(tr);
    tr.insert(td);
    td.insert(radio);
    td.insert(label);
  });

  
  if (json.size() > 0){

    var td = new Element('td',{
      'class': 'TableContent',
      'nowrap': 'true',
      'style': 'text-align:center'
    });

    var tr = new Element('tr',{
    });
    
    var btn = new Element('input', {
      'type': 'button',
      'value': '确定'
    });

    btn.onclick = submitForm;
    
    $('bindRadio').insert(tr);
    tr.insert(td);
    td.insert(btn);
  }
  else{
    var td = new Element('td',{
      'class': 'TableData',
      'nowrap': 'true',
      'style': 'text-align:center'
    });
    
    var tdBtn = new Element('td',{
      'class': 'TableContent',
      'nowrap': 'true',
      'style': 'text-align:center'
    });

    var tr = new Element('tr',{
    });
    
    var trBtn = new Element('tr',{
    });
    
    var btn = new Element('input', {
      'type': 'button',
      'value': '关闭'
    });

    btn.onclick = function(){
      window.close()
    };
    
    $('bindRadio').insert(tr);
    $('bindRadio').insert(trBtn);
    
    tr.insert(td);
    td.update("无数据!");
    
    trBtn.insert(tdBtn);
    tdBtn.insert(btn);
  }
}

function submitForm(){
  
  var index = -1;    
  var radioArray = document.getElementsByName("radioGroup");
  for (var i = 0; i < radioArray.length; i++){
    if (radioArray[i].checked){
      index = i;
      break;
    }
  }

  if (index < 0){
    alert('请选择绑定用户');
    return;
  }

  $('userDescOther').value= window.json[index].userDesc;
  $('userIdOther').value= window.json[index].userID;
  
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHBindUsersAct/bindUser.act";

  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    dialogArguments.setBindInfo();
    window.close();
  }else{
    alert("绑定失败");
  }
}

</script>
</head>
<body onload="doInit()">
<form method="post" id="form1" name="form1">
<table id="dispBindUser" class="TableBlock" width="95%" align="center">
  <thead>
      <h3>选择绑定用户</h3>
  </thead>
  <tbody id="bindRadio">
   </tbody>
  </table>
  
  <input type="hidden" id="userDescOther" name="userDescOther">
  <input type="hidden" id="userIdOther" name="userIdOther">
  <input type="hidden" id="userId" name="userId">
</form>
</body>
</html>