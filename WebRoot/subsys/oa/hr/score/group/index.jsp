<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核指标集明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreItemLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreItemAct/getScoreItem.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  var flag = 0;
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"70%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader'>"
				+"<td nowrap width='' align='center'>考核项目</td>"
				+"<td nowrap width='' align='center'>分值范围</td>"				
				+"<td nowrap width='' align='center'>操作</td></tr><tbody>");
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
			tr.update("<td align='center'><input type='text' name='itemName_"+seqId+"' id='itemName_"+seqId+"' class='SmallInput' size='20' value='"+itemName+"'></td><td align='center'><input type='text' name='min_"+seqId+"' id='min_"+seqId+"' class='SmallInput' size='3' maxlength='5' value='"+min+"'>&nbsp;～&nbsp;<input type='text' name='max_"+seqId+"' id='max_"+seqId+"' size='3' class='SmallInput' maxlength='5' value='"+max+"'>"					
				+ "</td><td align='center'>"		
				+ "<a href="
				+ "javascript:doEditItem('" + seqId + "','"+groupId+"');"
                + ">修改</a>&nbsp;&nbsp;"
				+ "<a href="	
				+ "javascript:deleteItem('" + seqId + "');"
				+ ">删除</a>&nbsp;&nbsp;"
				+ "</td>"					
			);
  	}
  	getScoreFlowFlag();
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function getScoreFlowFlag(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getScoreFlowFlag.act?groupId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    if(data == "1"){
      $("scoreFlag").style.display = '';
    }else{
      $("scoreFlag").style.display = 'none';
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  if($("itemName").value.trim() == ""){ 
    alert("考核项目不能空！！！");
    $("itemName").focus();
    $("itemName").select();
    return false;
  }
  if($("min").value.trim() == ""){ 
    alert("最小值不能空！！！");
    $("min").focus();
    $("min").select();
    return false;
  }
  if($("max").value.trim() == ""){ 
    alert("最大值不能空！！！");
    $("max").focus();
    $("max").select();
    return false;
  }
  if($("min").value){
    if(!isNumbers($("min").value)){
     alert("分值范围,应形如  100.00");
     $("min").focus();
     $('min').select();  
     return false;
    }
  }
  if($("max").value){
    if(!isNumbers($("max").value)){
     alert("分值范围,应形如  100.00");
     $("max").focus();
     $('max').select();  
     return false;
    }
  }
  if($("min").value >= $("max").value){
    alert("分值范围输入不正确！！！");
    $("min").focus();
    $('min').select(); 
    return false;
  }
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreItemAct/addScoreItem.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" align="absmiddle" width=22 height=20><span class="big3"> 考核指标集明细</span>
    </td>
  </tr>
</table>

 <div id="listDiv" align="center"></div>
    <form method="post" name="form1" id="form1" action="" onSubmit="">
    <table width="70%" align="center" class="TableList">
 <tfoot class="TableFooter">
    <td colspan="4">考核项目:
     <INPUT type="text"name="itemName" id="itemName" class=SmallInput size="20">
    &nbsp;
     分值范围:
     <INPUT type="text" name="min" id="min" class=SmallInput size="3" maxlength="5">&nbsp;～
     <INPUT type="text" name="max" id="max" class=SmallInput size="3" maxlength="5">
   &nbsp;&nbsp;
   <input type="hidden" name="seqId" id="seqId">  
     <input type="button" name="submit" class="SmallButton" value="添加" onclick="doSubmit();"></td>
  </tfoot>
  </table>
  </form>
<div align="center">
   <br>
   <input type="button" class="BigButton" value="返回" onClick="location='<%=contextPath%>/subsys/oa/hr/score/index.jsp';">
</div>
<div id="scoreFlag" style="display:none">
<table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">该考核指标集已经被应用，如果对该考核指标明细进行修改则会影响到已应用的考核任务！！！</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>