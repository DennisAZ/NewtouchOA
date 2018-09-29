<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>模块管理</title>
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script  type="text/javascript">
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct"
function doInit(){
  var url = requestUrl + "/listAllPorts.act";
  var json = getJsonRs(url); 
  if (json.rtState == "0") {
    var modList = json.rtData.records;
    if (modList.length > 0) {
      $("modListTable").show();
      for( var i = 0 ;i < modList.length ; i++) {
        var mod = modList[i];
        addModTr(mod , i);
      }
    }else {
      $('noData').show();
    }
  } 
}
function addModTr(mod , i) {
  var className = (i % 2 == 0) ? "TableLine1" :"TableLine2" ;
  var tr = new Element("tr",{"class":className , "id":"tr-" + mod.id});
  $('modListTbody').appendChild(tr);
  
  var checkbox = new Element('input', {type: 'checkbox', value: mod.id});
  var checkboxTd = new Element("td").update(checkbox);
  tr.insert(checkboxTd);
  
  var td = new Element("td");
  tr.appendChild(td);
  var str = mod.file.replace(".js" , "");
  td.update(str);
  td = new Element("td");
  tr.appendChild(td);
  var op = "&nbsp;<a href='javascript:editModule("+mod.id+" , 0)'>配置数据</a>&nbsp;&nbsp;"
    //+ "<a href='javascript:editModule("+mod.id+" , 1)'>配置内容样式</a>&nbsp;&nbsp;"
    //+ "<a href='javascript:editModule("+mod.id+" , 2)'>配置边框样式</a>&nbsp;&nbsp;"
    + "<a href='javascript:editModule("+mod.id+" , 3)'>设置权限</a>&nbsp;&nbsp;"
    +"<a href='javascript:delMod("+mod.id+")'>删除</a>"
  td.update(op);
}
function editModule(id , pageNo) {
  location.href = "cfgPortal.jsp?pageNo=" + pageNo + "&id=" + id;
}
function delMod(id) {
  if (!confirm("确认删除此模块吗？")){
    return;
  }
  var url = requestUrl + "/delPort.act";
  var json = getJsonRs(url , "id=" + id); 
  if (json.rtState == "0") {
    var tr = $("tr-" + id);
    $('modListTbody').removeChild(tr);
  }
}

function importPorts() {
  $('importForm').action = contextPath + "/yh/core/funcs/portal/act/YHPortAct/importPorts.act";
  $('importForm').submit();
}

function showImport(id) {
  $('importForm').show();
  if (id) {
    $(id).insert($('importForm'));
  }
}

function exportPorts() {
  var ports = $$('#modListTable .TableLine1 input').concat($$('#modListTable .TableLine2 input'));
  var portsStr = '';
  ports.each(function(e, i) {
    if (e.checked) {
      portsStr += e.value + ',';
    }
  });
  if (!portsStr) {
    alert("请选择模块导出");
    return;
  }
  $('portsStr').value = portsStr;
  $('form1').action = contextPath + "/yh/core/funcs/portal/act/YHPortAct/exportPorts.act";
  $('form1').submit();
}


function checkAll(o) {
  var checked = o.checked
  $$('#modListTable .TableLine1 input').each(function(e, i) {
    e.checked = checked;
  });
  $$('#modListTable .TableLine2 input').each(function(e, i) {
    e.checked = checked;
  });
}
</script>
</head>

<body onLoad="doInit()">
<table  border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 模块管理</span><br>
    </td>
  </tr>
</table>
<br/>
<br/>
<table  id="modListTable"  style="width:600px;margin: 0 auto;display:none" class="TableList">
      <tr>
        <td class="TableHeader" colspan="">
        </td>
        <td class="TableHeader" colspan="">
        模块名称
        </td>
        <td class="TableHeader" colspan="">
        操作
        </td>
      </tr>
     <tbody id="modListTbody">
    </tbody>
    <tr class="TableControl">
        <td colspan="3" class="TableControl">
         <input type="checkbox" onclick="checkAll(this)">全选&nbsp;&nbsp;
         操作&nbsp;
         <input type="button" class="BigButton" value="新增" onclick="location.href='cfgPortal.jsp'"/>
         &nbsp;<input type="button" class="BigButtonC" value="导出选中模块" onclick="exportPorts()"/>
         &nbsp;<input type="button" class="BigButtonC" value="导入桌面模块" onclick="showImport()"/>
         <form style="display:none;" method="post" id="importForm" name="importForm" enctype="multipart/form-data">
           &nbsp;<input type="file" id="file" name="file" class="" value=""/>
         &nbsp;<input type="button" class="BigButton" value="导入" onclick="importPorts()"/>
         </form>
        </td>
      </tr>
  </table>
  <div id="noData" align="center" style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">尚未定义模块！</td>
        </tr>
    </tbody>
</table>
<div align="center" id="emptyOpts"><input type="button" class="BigButton" value="新增" onclick="location.href='cfgPortal.jsp'"/>
&nbsp;<input type="button" class="BigButtonC" value="导入桌面模块" onclick="showImport('emptyOpts')"/>
&nbsp;
</div>
</div>
<form action="" id="form1" name="form1">
<input type="hidden" id="portsStr" name="portsStr">
</form>

</body>
</html>