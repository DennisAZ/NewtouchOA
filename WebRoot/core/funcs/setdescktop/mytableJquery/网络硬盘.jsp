<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body >
<%--<div id="netdiskFolder" style="height: 100px;"></div> --%>

<div id="netdiskDiv" class="" style="position:relative;overflow:hidden;width:100%;">

	<div id="netdisk_ul" class="module_div" style="width:100%;">
	
		<ul id="netdisk_li" style="float:left;text-align:left;width:100%;" type="disc"></ul>

	  <div style="clear:both;"></div>
	</div>
</div>

<script type="text/javascript">
var lines = <%=request.getParameter("lines")%>;
var scroll = <%=request.getParameter("scroll")%>;

function doInitDisk(){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/netdisk/act/YHNetDiskAct/getTreebyFileSystem.act?DISK_ID=");
  if(rtJson.rtState == "0"){
    //alert("length>>" + rtJson.rtData.length);
    //alert(rsText);  
    var prcJson = rtJson.rtData;
    var records = Math.round(prcJson.length / 2);
    var showDiskFolder = $("netdiskFolder");

    var dataStr = "";
    
    if(prcJson.length>0){
      var contentList="";
      //alert("showDiskFolder>>"+showDiskFolder);
      for(var i=0;i<prcJson.length;i++){
        var prcs = prcJson[i];
        var diskName = prcs.name;
        var diskId = prcs.nodeId;
        var extData = prcs.extData;

        contentList = contentList + "<li>&nbsp;<a href=javascript:top.dispParts(\"<%=contextPath%>/core/funcs/netdisk/index.jsp?seqId=" + extData + "&filePath=" +diskId + "\")> " + diskName + "</a>";
      }
      
      $("netdisk_li").update(contentList);
      //alert(prcJson.length);
      var records = prcJson.length;

      $('netdiskDiv').setStyle({height:20 * lines + 'px'});
      $('netdisk_ul').setStyle({position: 'relative'});

      //$('folder_ul').setStyle({'top': '0px'});
      cfgModule({
        records: records,
        lines: lines,
        name: '网络硬盘',
        showPage:  function(i){
          $('netdisk_ul').setStyle({'top': (- i * lines * 20) + 'px'});
        }
      });

      if (scroll){
        Marquee('netdisk_ul',80,1);
      }
    }
    
  }else{
    
  }
  
}

doInitDisk();

</script>

</body>

</html>