<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的全部工作</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<style>
.color1 {color:#FFBC18;}
.color2 {color:#50C625;}
.color3 {color:#F4A8BD;}
A:link {
  COLOR: #16008a; TEXT-DECORATION: none
}
A:visited {
  COLOR: #16008a; TEXT-DECORATION: none
}
A:hover {
  COLOR: #333333; TEXT-DECORATION: underline
}
A:active {
  COLOR: #333333; TEXT-DECORATION: underline
}
UL {
  PADDING-BOTTOM: 0px; 
  LINE-HEIGHT: 20px; 
  MARGIN: 0px 0px 0px 3px;
   PADDING-LEFT: 0px; 
   PADDING-RIGHT: 0px; PADDING-TOP: 0px
}
LI {
  PADDING-BOTTOM: 0px; 
     LIST-STYLE-TYPE: disc;
    MARGIN: 0px 0px 0px 15px;
     PADDING-LEFT: 0px;
     PADDING-RIGHT: 0px; 
     PADDING-TOP: 0px
}
LI A:link {
  COLOR: #16008a; TEXT-DECORATION: none
}
LI A:visited {
  COLOR: #16008a; TEXT-DECORATION: none
}
LI A:hover {
  COLOR: #333333; TEXT-DECORATION: underline
}
LI A:active {
  COLOR: #333333; TEXT-DECORATION: underline
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit(){ 
  skinObjectToSpan(flowrun_query_flowList);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHWorkQueryAct/getFlowList.act";
  var json = getJsonRs(url, "sortId=" + sortId) ;
  if (json.rtState == "0") {
    var sorts = json.rtData;
    for (var i = 0 ;i < sorts.length ;i ++) {
      var sort = sorts[i];
      if (sort) {
        var div = getModule(sort);
        $('div_l').innerHTML += div;
        var diff=$('div_l').offsetHeight-$('div_r').offsetHeight;
        if(diff > 100) {
          var divL = $('div_l').lastChild.previousSibling;
          if (divL != null) 
           $('div_r').appendChild(divL);
        }
        if(diff < -100) {
          var divL = $('div_r').lastChild.previousSibling;
          if (divL != null) 
            $('div_l').appendChild(divL);
        }
        var url2 = contextPath + "/yh/core/funcs/workflow/act/YHWorkQueryAct/getFlowType.act";
        getJsonRsAsyn(url2 , "sortId=" + sort.seqId, showDiv);
      }
    }
  }
}
function getDivData(rtData) {
  var div = "";
  for (var i = 0 ;i < rtData.length ;i ++) {
    var flow = rtData[i] ;
    if (flow) {
      var flowName = flow.flowName;
      var flowId = flow.flowId;
      var endWorkCount = flow.endWorkCount;
      var handlerWorkCount = flow.handlerWorkCount;
      var flowType = flow.flowType == 1 ? "固定流程" : "自由流程";
      var flowDoc = flow.flowDoc == 1 ? "是" : "否";
      var tmp = encodeURIComponent(flowName);

      var url = "query.jsp";
      if (flow.hasFormVersion) {
        url = "selectFormVersion.jsp";
      }
      div += "<li><a href=\""+url+"?skin="+ skin +"&flowId="+flowId+"&sortId="+sortId+"&flowName="+ tmp +"\" title=\"流程类型："+flowType+" &#10;允许附件："+flowDoc+"\">"+flowName+"</a>&nbsp;&nbsp;&nbsp;";

      if (handlerWorkCount > 0 ) {
        div += " <span class=\"color2\">■</span>" + handlerWorkCount;
      }
      if (endWorkCount > 0 ) {
        div += "<span class=\"color3\">■</span>" + endWorkCount;
      }
      
      div += "</li>";
    }
  }
  return div;
}
function showDiv(json) {
  var rtData = json.rtData;
  var seqId = json.rtMsrg; 
  var div = getDivData(json.rtData);
  $(seqId+"_ul").update(div);
}
function getModule(module) {
  var seqId = module.seqId;
  var name = module.sortName;
  var flows = module.flows;
  var div = "<div id=\"module_"+seqId+"\" class=\"module listColor\">";
  div += "<div class=\"head\" onclick=\"_resize("+seqId+");\" style=\"cursor:pointer\">";
  div += "<h4 id=\"module_"+seqId+"_head\" class=\"moduleHeader\">";
  div += "<img class=\"icon\" id=\"img_resize_"+seqId+"\" src=\""+ imgPath +"/expand_arrow.png\" title=\"折叠\" /><span id=\"module_"+seqId+"_text\" class=\"text\">"+name+"</span>";
  div += "</h4></div>";
  div += "<div id=\"module_"+seqId+"_body\" class=\"module_body\">";
  div += "<div id=\"module_"+seqId+"_ul\" class=\"module_div\"><ul id=\""+seqId+"_ul\">";
  div += "<img src='" + imgPath + "/dtree/loading.gif'/>";
  div += "</ul></div></div></div><div class=\"shadow\"></div>";
  return div;
}
function _resize(module_id)
{
   var module_i=$("module_"+module_id);
   var head_i=$("module_"+module_id+"_head");
   var body_i=$("module_"+module_id+"_body");
   var img_i=$("img_resize_"+module_id);
   if(body_i.style.display=="none")
   {
      module_i.className=module_i.className.substr(0,module_i.className.lastIndexOf(" "));
      head_i.className=head_i.className.substr(0,head_i.className.lastIndexOf(" "));
      body_i.style.display="block";
      img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"expand_arrow.png";
      img_i.title="折叠";
   }
   else
   {
      module_i.className=module_i.className+" listColorCollapsed";
      head_i.className=head_i.className+" moduleHeaderCollapsed";
      body_i.style.display="none";
      img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"collapse_arrow.png";
      img_i.title="展开";
   }
}
function _resizeCchild(module_id)
{
  var ul_i = $("module_"+module_id+"_ul");
  var img_i = $("img_resize_"+module_id);
  if(ul_i.style.display=="none")  
  {
    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"expand_arrow.png";
    img_i.title="折叠";
    ul_i.style.display="" ; 
  }
  else
  {
    img_i.src=img_i.src.substr(0,img_i.src.lastIndexOf("/")+1)+"collapse_arrow.png";
    img_i.title="展开";
    ul_i.style.display="none" ;     
  }
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><span class="big3" id="span1">  </span>
      &nbsp;&nbsp;
      颜色标识说明：<span class="color2">■</span>办理中 &nbsp;&nbsp;<span class="color3">■</span>已结束
      &nbsp;&nbsp;<input type="button"  value="返回" class="BigButton" onClick="location='index.jsp?skin=<%=skin %>&sortId=<%=sortId %>';">
    </td>
  </tr>
</table>

<table width="99%" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;margin-right:5px">
 <tr>
  <td width="50%" valign="top"><div id="div_l">
</div>
</td>
  <td width="50%" valign="top"><div id="div_r">
<div class="shadow"></div></div></td>
</table>

</body>
</html>