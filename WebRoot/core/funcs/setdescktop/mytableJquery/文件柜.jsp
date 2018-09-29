<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>文件柜</title>
</head>
<body>
<div class="module_body">
<div class="moduleTypeLink">
<a href="javascript:getFile(1);" id="newContent">最新文件</a> | <a href="javascript:getFile(2);" id="folder">文件柜</a> 
</div>
<div id="folderDiv" class="" style="position:relative;overflow:hidden;width:100%;">

	<div id="folder_ul" class="module_div" style="width:100%;">
	
		<ul id="folder_li" style="float:left;text-align:left;width:100%;" type="disc">
		</ul>

	  <div style="clear:both;"></div>
	</div>
</div>
</div>
<script type="text/javascript">

window.getFile = function (index){
  var lines = <%=request.getParameter("lines")%>;
  $('folderDiv').setStyle({height:20 * lines + 'px'});
  var URL = "<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileContentAct/selectFolderInfoToDisk.act?index="+index;
  var rtJson = getJsonRs(URL);
  //alert(rsText);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  //alert(prcs.length);
  if(prcs.length>0){
    var contentList="";
    
    for(var i = 0; i<prcs.length;i++){
     var prc = prcs[i];
     var returnContentId=prc.contentId;
     var returnSortId=prc.sortId;
     var subject=prc.subject;
     var sendTime=prc.sendTime;
     var sortName=prc.sortName;
     var limitSubject="";
     if(subject.length>=20){
       limitSubject=subject.substring(0,20) +" ...";
      }else{
        limitSubject=subject;
      }

     if(index == '1'){
       contentList = contentList + "<li>&nbsp;" + sortName + "&nbsp;<a href='javascript:top.dispParts(\"<%=contextPath%>/core/funcs/filefolder/index.jsp?showFlag=CONTENT&sortId="+ returnSortId +"&contentId="+ returnContentId + "\" )'  title='" + subject +  "'>" + limitSubject +"</a>&nbsp;" + "(" + sendTime + ")</li>";
      }

      if(index == '2'){
        contentList = contentList + "<li>&nbsp;<a href='javascript:top.dispParts(\"<%=contextPath%>/core/funcs/filefolder/index.jsp?sortId="+ returnSortId + "\") '  title='" + subject +  "'>" + sortName +"</a>&nbsp;</li>";
      }
     
     
    }
    $("folder_li").update(contentList);
    doInitPlan(prcs.length );
    
  }else{
    if(index=='1'){
      $("folder_li").update("<li>暂无最新文件<li>");
    }
    if(index=='2'){
      $("folder_li").update("<li>暂无文件夹<li>");
    }
  }
}

window.doInitPlan = function (records){
  var lines = <%=request.getParameter("lines")%>;
  $('folder_ul').setStyle({position: 'relative'});

  $('folder_ul').setStyle({'top': '0px'});
  cfgModule({
     records: records,
     lines: lines,
     name: '文件柜',
     showPage:  function(i){
       $('folder_ul').setStyle({'top': (- i * lines * 20) + 'px'});
     }
  });

}
getFile(1);
var scroll = <%=request.getParameter("scroll")%>;
if (scroll){
  Marquee('folder_ul',80,1);
}

</script>
</body>
</html>