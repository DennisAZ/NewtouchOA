<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报表</title>
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
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/messages.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
function doInit(){
	var flag=true;
  skinObjectToSpan(flowrun_list_allList);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowReportAct/getReportListAct.act";
  var json = getJsonRs(url) ;
  if (json.rtState == "0") {
    var sorts = json.rtData.data;
    for (var i = 0 ;i < sorts.length ;i ++) {
      var sort = sorts[i];
      if (sort.list.length>0) {
        flag=false;
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
      }
    }

    if(flag==true){
         $("showNoData").style.display="block";
     }
    
  }
}
function getModule(module) {
  var seqId = module.seqId;
  var name = module.sortName;
  var lists = module.list;
  var div = "<div id=\"module_"+seqId+"\" class=\"module listColor\">";
  div += "<div class=\"head\" onclick=\"_resize("+seqId+");\" style=\"cursor:pointer\">";
  div += "<h4 id=\"module_"+seqId+"_head\" class=\"moduleHeader\">";
  div += "<img class=\"icon\" id=\"img_resize_"+seqId+"\" src=\""+ imgPath +"/expand_arrow.png\" title=\"折叠\" /><span id=\"module_"+seqId+"_text\" class=\"text\">"+name+"</span>";
  div += "</h4></div>";
  div += "<div id=\"module_"+seqId+"_body\" class=\"module_body\">";
  div += "<div id=\"module_"+seqId+"_ul\" class=\"module_div\"><ul>";
  for (var i = 0 ;i < lists.length ;i ++) {
    var list = lists[i] ;
    if (list) {
      var listName = list.name;
      var listId = list.seqId;
      var hasVersion = list.hasFormVersion;
      var url = "show_report.jsp?1=1";
      if (hasVersion) {
        var flowId = list.flowId;
        url = "selectFormVersion.jsp?flowId="+flowId+"&flowName=" + encodeURIComponent(listName);
      }
      div += "<li><a href=\""+url+"&rId="+listId+"\" title=\"点击打开报表\">"+listName+"</a>&nbsp;&nbsp;&nbsp;";
      div += "</li>";
    }
  }
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
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><span class="big3" id="span"> 统计报表 </span>
     
    </td>
  </tr>
</table>

<table width="99%" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;margin-right:5px">
<tr>
  <td width="50%" valign="top"><div id="div_l"></div></td>
  <td width="50%" valign="top"><div id="div_r"><div class="shadow"></div></div></td>
</tr>
</table>

<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无数据报表</div></td>
  </tr>
  </table>
           
</div>

</body>
</html>