<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div id="deskNotify" class="" style="overflow:hidden;position:relative;width:100%;">
	<div id="deskNotify_ul" class="module_div" style="width:100%;">
		<ul id="deskNotify_ul1" style="float:left;text-align:left;width:100%;">
		</ul>
	  <div style="clear:both;"></div>
	</div>
</div>
<script>
window.deskNotify = function (seqId){
  var img = document.getElementById("img_"+seqId+"");
  if(img){
    document.getElementById("img_"+seqId+"").style.display="none";
  }
    var URL = "<%=contextPath%>/core/funcs/notify/show/readNotify.jsp?seqId="+seqId;
    myleft=(screen.availWidth-500)/2;
    window.open(URL,"deskNotify","height=600,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");    
   
  }

window.doInitNotice = function (){
var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyShowAct/getdeskNotifyAllList.act");
if (rtJson.rtState == "0") {
  
  var records = Math.round(rtJson.rtData.length);
  var lines = ${param.lines};
  var scroll = ${param.scroll};
  
  //填充ul
  rtJson.rtData.each(function(e,i){

    
//    var img = new Element('img',{src: e.icon,style: 'width:16px;height:16px',align:'absmiddle',alt:e.text});
    var a = new Element('a',{href: "javascript:deskNotify("+e.seqId+")"});   
    if(e.iread == "no"){
      a.insert(e.subject+" ("+e.readerCount+") "+" ("+e.sendTime+")" +"<img id='img_"+e.seqId+"'src='<%=imgPath%>/email_new.gif' align='absMiddle' border='0'>");
    }else{
      a.insert(e.subject+" ("+e.readerCount+") "+" ("+e.sendTime+")");
    }
    
    var li = new Element('li',{'style': 'height:20px;'}).update(a);
    $('deskNotify_ul1').insert(li);
  });
 
  //设置
  $('deskNotify').setStyle({height: 20 * lines + 'px'});
  $('deskNotify_ul').setStyle({position: 'relative'});
 
  cfgModule({
    records: records,
    lines: lines,
    name: '公告通知',
    showPage:  function(i){
      $('deskNotify_ul').setStyle({'top': (- i * lines * 20) + 'px'});
    }
  });
  
  if (scroll){
      Marquee('deskNotify_ul',80,1);
  }
  
  }else {
  
  }
}
doInitNotice();
</script>
</body>
</html>