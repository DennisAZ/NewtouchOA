<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>

<div id="shortcut" class="" style="overflow:hidden;position:relative;width:100%;">
	<div id="shortcut_ul" class="module_div" style="width:100%;">
		<ul id="shortcut_ul1" style="float:left;text-align:left;width:48%;">
		</ul>
		<ul id="shortcut_ul2" style="float:right;text-align:left;width:48%;">
		</ul>
	  <div style="clear:both;"></div>
	</div>
</div>
<script type="text/javascript">

window.doInitShortcut = function (){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/getModule.act");
  
  if (rtJson.rtState == "0") {
    
    var records = Math.round(rtJson.rtData.length / 2);
    var lines = ${param.lines};
    var scroll = ${param.scroll};
    
    //填充ul
    rtJson.rtData.each(function(e,i){
      
      var img = new Element('img',{src: e.icon,style: 'width:16px;height:16px',align:'absmiddle'});
      var a = new Element('a',{href: "javascript:void(0)"});
      a.onclick = function() {
        top.dispParts(e.url);
        return false;
      }
      a.insert(img);
      a.insert(e.text);
      
      var li = new Element('li',{
        'style': 'height:20px'
      }).update(a);
      
      if (i & 1){
        $('shortcut_ul2').insert(li);
      }
      else{
        $('shortcut_ul1').insert(li);
      }
    });

    //设置
    $('shortcut').setStyle({height: 20 * lines + 'px'});
    $('shortcut_ul').setStyle({position: 'relative'});
    
    cfgModule({
      records: records,
      lines: lines,
      name: '菜单快捷组',
      showPage:  function(i){
        $('shortcut_ul').setStyle({'top': (- i * lines * 20) + 'px'});
      }
    });
    
    if (scroll){
      Marquee('shortcut_ul',80,1);
    }
    
  }else {
    
  }
}

doInitShortcut();

</script>
</body>
</html>