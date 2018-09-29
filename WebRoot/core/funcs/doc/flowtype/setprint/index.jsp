<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String  flowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置流程属性</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>

<script type="text/javascript">
var isOpen = false;
var isLoad = false;
var flow_id=<%=flowId%>;

function doInit(){
	getTplList();
}

function sel_file()
{
  var obj = $("HWPostil1");
  var aa=1;
    aa= obj.LoadFile("");
 //  var aa= obj.LoadFileEx("","doc",0,0);

  isLoad = true;
}
function convert()
{
  var obj = $("HWPostil1");
  if($("T_NAME").value=="")
  {
    alert("请输入模板名称！");
    return;
  }
  if(isLoad==false)
  {
    alert("请选中模板文件！");
    return;
  }
  if(isOpen == true)
  {
    var content = obj.GetCurrFileBase64();
    //把内容上传到服务器
    var param="T_NAME="+ $("T_NAME").value+"&flow_id="+flow_id+"&t_type="+$("T_TYPE").value+"&CONTENT="+content.replace(/\+/g, '%2B');
     var url =  contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/uploadAip.act";

     var rtJson = getJsonRs(url,param);
       if (rtJson.rtState == "0") {
        var data=rtJson.rtData;
  
    	 window.location='new_tpl.jsp?seq_id='+data.id+"&flow_id="+flow_id;
    }else {
      alert(rtJson.rtMsrg);
    }
  }
  else
  {
   // alert("文档尚未转换完毕！请稍候！");
   //s convert();
  }   
}

function getTplList(){
	var rtJson = getJsonRs( contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/getTplListAct.act", "flow_id="+flow_id);
	  if (rtJson.rtState == "0") {
	    var data=rtJson.rtData.list;
	    for(var i=0;i<data.length;i++){
          var type="";
            if(data[i].tType=="1"){
                type="打印模板";
                }
            else if(data[i].tType=="2"){
                type="手写呈批单";
                }
          var edit="<a href='new_tpl.jsp?seq_id="+data[i].seqId+"&flow_id="+flow_id+"'>编辑</a>";
          var del="<a href=\"javascript:deleteTpl('"+data[i].seqId+"')\">删除</a>";
		    
	    	  var tr = new Element('tr', { "class" : "TableData" });
	    	      $('tbl_list').appendChild(tr);
	    	      tr.update("<td align='center'>" + data[i].tName+ "</td>" 
	    	    	        + "<td align='center'>"
	    	              + type + "</td>"
	    	              + "<td align='center'>" + data[i].flowPrcs + "</td>"
	    	              + "<td align='center'>" + edit+" "+del + "</td>");
	    	        }
	    
					  }else {
					    alert(rtJson.rtMsrg);
					    
					  }
}

function deleteTpl( seqId ){
if(window.confirm("确定删除该模板吗 ?")){
	var rtJson = getJsonRs( contextPath + "<%=moduleSrcPath %>/act/YHFlowPrintAct/delTplAct.act", "seq_id="+seqId);
    if (rtJson.rtState == "0") {
    	window.location.reload(); 

        
     }else{
     	 alert(rtJson.rtMsrg);
     }
	
  }
}
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyDocOpened>
isOpen = true;
</SCRIPT>





</head>
<body onLoad="doInit();" class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建打印模板</span><br>
    </td>
  </tr>
</table>

<table class="TableList" width="80%" align="center">
  <tr>
    <td class="TableContent">模板名称</td>
    <td class="TableData">
      <input type="text" class="BigInput" size=25 name="T_NAME" id="T_NAME">
    </td>
  </tr>
  <tr>
    <td class="TableContent">模板类别</td>
    <td class="TableData">
      <select name="T_TYPE" class="SmallSelect" id="T_TYPE">
       <option value="1">打印模板</option>
       <option value="2">手写呈批单</option>
      </select>
      <br>说明:在主办过程中可以直接在呈批单上进行手写签批<br>
    </td>
  </tr>
  <tr>
    <td class="TableContent">模板文件</td>
    <td class="TableData" >
      <span id="file_name"></span>
      <input type="button" value="选择模板文件" class="BigButtonC" onClick="sel_file();">（word文档）
    </td>
  </tr>
  <tr>
    <td class="TableControl" align="center" colspan=2>
      <input type="button" value="新建打印模板" class="BigButtonC" onClick="convert();">
    </td>
  </tr>
</table>
  
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/print.gif" align="absmiddle"><span class="big3"> 打印模板管理</span>
    </td>
  </tr>
</table>



<br>

<table class="TableList" width="95%"  align="center">
   <thead class="TableHeader" align='center'>
      <td align='center'  width="150">模板名称</td>
      <td align='center'  width="70">模板类别</td>
      <td align="cetner">使用步骤</td>
      <td nowrap align="center" width="70">操作</td>
   </thead>
   <tbody id="tbl_list"></tbody>

</table>


<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无打印模板</div></td>
  </tr>
  </table>
           
</div>

<div id="overlay">
    <div style="align:center;height:100%;font-size:16px"><img src="/images/loading_blue.gif" border=0><span id="msg">转化中,请稍后......</span></div>
</div>
<OBJECT id=HWPostil1 style="WIDTH:0;HEIGHT:0" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/core/cntrls/HWPostil.cab#version=3,0,7,0' >"
</OBJECT>
</body>
</html>
