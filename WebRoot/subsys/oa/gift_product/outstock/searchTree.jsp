<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<html class="hiddenRoll">
<head>
<title>模糊选择</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
 <script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script language=javascript>
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
function getOpenner(){
   if(is_moz){
      return parent.opener;
   }else{
      return parent.dialogArguments;
   }
}
var parent_window = getOpenner();

var giftIdField = null;
var tree = null;
function doInit(){	
  parent_window = getOpenner();
  giftIdField = parent_window.$(parent_window.giftIdField);//初始化对象
  tree =  new DTree({bindToContainerId:'xtree' 
  ,requestUrl:contextPath + '/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getGiftTypeTree.act?GIFT_PROTYPE=GIFT_PROTYPE&id='
  ,isOnceLoad:false
  ,checkboxPara:{isHaveCheckbox:false}
  , treeStructure:{isNoTree:false}
	,linkPara:{clickFunc:test}
	,treeStructure:{isNoTree:false}	
});

tree.show();
}
function test(id,temp){  
//var node=tree.getNode(id);//node得到的是你点击的结点的一个js对象
//var parentId=node.parentId;
//alert("curNode:"+node+"  parentId>>>:"+parentId);
//alert(id);
   if(id){
     if(id.split(",").length>1){
  	 //id.split(",")[0];
  	 toGiftId(id.split(",")[0]);
     }
   }
}
function toGiftId(seqId){
  //上级页面更新数据
  parent_window.getInstock2();
  giftIdField.value = seqId;
  
  close();
}
function CheckFrom(){
  var giftName = $("giftName").value;
  if(giftName==''){
    alert("请输入查询名称！");
    return;
  }else{
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectGiftInstockByName.act?giftName="+encodeURI(giftName);
    var rtJson = getJsonRs(requestUrl);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var prcs = rtJson.rtData;
    if(prcs.length>0){
      var table = new Element('table',{ "border":"0", "cellspacing":"1" ,"width":"95%" ,"class":"small" ,"bgcolor":"#000000" ,"cellpadding":"3" ,"align":"center", "title":"请点击选择"}).update("<tbody id='giftTbody'>"
          +"<tr class='TableHeader'>"
          +"<td nowrap align='center'>编号</td>"
          +"<td nowrap align='center'>名称</td>"
          + "</tr></tbody>");
      $("giftQuery").update(table);
      for(var i = 0;i< prcs.length;i++){
        var prc = prcs[i];
        var tr = new Element('tr',{"class":"TableLine1"});
        $("giftTbody").appendChild(tr);
        tr.update("<td nowrap align='center'>"
            +"<a href='#' onClick='toGiftId("+prc.seqId+");'>"+prc.seqId+"</a></td>"
           + "<td nowrap align='center'>"
            +"<a href='#' onClick='toGiftId("+prc.seqId+");'>"+prc.giftName+"</a></td>"
            )
       
      }
    }else{
      alert("没有满足条件的礼品！");   
      window.location.reload();
    }
  }
}
</script>
</head>
<body leftmargin="3" topmargin="5" onload="doInit()">
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" align="absmiddle"><span class="big3"> 模糊查询礼品</span>
    </td>
  </tr>
  <tr><td align="center">请输入物品名称：<input type="text" id="giftName" name="giftName" class="BigInput"></td>
  </tr>
  <tr>
  	<td align="center"><input type="button" value="模糊查询" class="SmallButtonW" onClick="CheckFrom();">
	&nbsp;<input type="button" value="取消" class="SmallButton" onClick="window.close();"></td>
  </tr>
</table>
</form>
<div align="center" id="giftQuery" style="padding: 10px;">
</div>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" align="absmiddle"><span class="big3"> 请选择礼品</span>
    </td>
  </tr>
</table>
<dir id="xtree"></dir>
</body>
</html>