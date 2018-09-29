<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>

<div id="deskNews" class="" style="overflow:hidden;position:relative;width:100%;">
	<div id="deskNews_ul" class="module_div" style="width:100%;">
		<ul id="deskNews_ul1" style="float:left;text-align:left;width:100%;">
		</ul>
	  <div style="clear:both;"></div>
	</div>
</div>
<script type="text/javascript">
window.deskNews = function (seqId){
    var img = document.getElementById("img_"+seqId+"");
    if(img){
      document.getElementById("img_"+seqId+"").style.display="none";
    }
    var URL = "<%=contextPath%>/core/funcs/news/show/readNews.jsp?seqId="+seqId;
    myleft=(screen.availWidth-500)/2;
    window.open(URL,"deskNews","height=600,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");    
  }

window.openURL = function (seqId){ 
  var img = document.getElementById("img_"+seqId+"");
  if(img){
    document.getElementById("img_"+seqId+"").style.display="none";
  }
  var url = "<%=contextPath%>/core/funcs/news/show/reNews.jsp?seqId="+ seqId+"&manage=1"
  myleft=(screen.availWidth-500)/2;
  window.open(url,"","height=600,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");    
}

window.doInitNews = function (){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/news/act/YHNewsShowAct/getDeskNewsAllList.act");  
  if (rtJson.rtState == "0") {     
    var records = Math.round(rtJson.rtData.length);
    var lines = ${param.lines};
    var scroll = ${param.scroll};
    
    //填充ul
    rtJson.rtData.each(function(e,i){

      
//      var img = new Element('img',{src: e.icon,style: 'width:16px;height:16px',align:'absmiddle',alt:e.text});
      var a = new Element('a',{href: "javascript:deskNews("+e.seqId+")"});
      //+" ("+e.clickCount+") "+" ("+e.newsTIme+")"
      a.insert(e.subject);
      
      var li = new Element('li',{'style': 'height:20px;'}).insert(a);
      var co = "("+e.clickCount+")";
      li.insert(co); 
      var b = "<a href='javascript:void(0);' onclick='javascript:openURL("+e.seqId +");return false;'>&nbsp;&nbsp;&nbsp;评论</a>"+"("+ e.commentCount+")" +"&nbsp;&nbsp;&nbsp;("+ e.newsTIme +")";
      if(e.iread =="no"){
        b += "<img id='img_"+e.seqId+"'src='<%=imgPath%>/email_new.gif' align='absMiddle' border='0'>";
      }
      li.insert(b);
      $('deskNews_ul1').insert(li);
    });
   
    //设置
    $('deskNews').setStyle({height: 20 * lines + 'px'});
    $('deskNews_ul').setStyle({position: 'relative'});
   
    cfgModule({
      records: records,
      lines: lines,
      name: '新闻',
      showPage:  function(i){
        $('deskNews_ul').setStyle({'top': (- i * lines * 20) + 'px'});
      }
    });
    
    if (scroll){
      Marquee('deskNews_ul',80,1);
    }
    
  }else {
    
  }
}
doInitNews();



</script>
</body>
</html>