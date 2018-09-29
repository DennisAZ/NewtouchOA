<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
</head>
<body onload="">
<div class="banner_bg">
	<div class="" style="float:left;">
	  <img id="logoImg" src="<%=contextPath%>/yh/core/funcs/setdescktop/syspara/act/YHSysparaAct/queryHeaderImg.act?para=<%=Math.random() %>"/>
	</div>
	<div class="banner_overflow_x_scroll" style="float:right;">
	  <div class="banner_scroll_up">
      <a href="javascript:void(0);" onclick="shortcutMove(2);return false;" style="display:none;" id="shortcutBtnDown">
        <img onmouseover="this.src='<%=imgPath %>/banner/scroll_up_mouseon.jpg'" onmouseout="this.src='<%=imgPath %>/banner/scroll_up.jpg'" src="<%=imgPath %>/banner/scroll_up.jpg"></img>
      </a>
	  </div>
	  <div class="banner-shortcutlist-container">
		  <table id="bannerShortcut" style="position:relative;top:0px;" cellspacing="0" cellpadding="0" border="0">
		    <thead>
		    </thead>
		    <tbody id="bannerShortcutTb">
		    </tbody>
		  </table>
	  </div>
    <div class="banner_scroll_down">
      <a href="javascript:void(0);" onclick="shortcutMove(-2);return false;" style="display:none;" id="shortcutBtnUp">
        <img onmouseover="this.src='<%=imgPath %>/banner/scroll_down_mouseon.jpg'" onmouseout="this.src='<%=imgPath %>/banner/scroll_down.jpg'" src="<%=imgPath %>/banner/scroll_down.jpg"></img>
      </a>
    </div>
	</div>
	
	<div class="clear"></div>
</div>
<script type="text/javascript">
function fillShortcut(){

  Ext.Ajax.request({
    url: '<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/listShortCut.act',
    method:'post',
    failure: function(o){
      Ext.Msg.alert("提示","获取状态失败");
    },
    success: function(o){
      var json = Ext.decode(o.responseText);
      var tr = null;

      if (json.length <= 4){
        $('shortcutBtnUp').hide();
        $('shortcutBtnDown').hide();
      }
      else{
        $('shortcutBtnUp').show();
        $('shortcutBtnDown').show();
      }

      if (!(json.constructor === Array)){
        return;
      }
      
      json.each(function(e,i){
        
        var td = new Element('td',{
          'class': 'banner_shortcut_bg',
          'cellspacing': "0",
          'cellpadding': "0",
          'border': "0"
        });
        
        var alink = new Element('a',{
          'href': 'javascript:void(0)'
        }).update(e.text);

        alink.onclick = function(){
          if (e.openFlag == '1'){
            window.open(e.url);
          }
          else{
            dispParts(e.url);
          }
          return false;
        };
        
        if (!(i % 4)){
          tr = new Element('tr',{
            'cellspacing': "0",
            'cellpadding': "0",
            'border': "0"
          });
          $('bannerShortcutTb').insert(tr);
        }

        td.insert(alink);
        
        tr.insert(td);
      });
    }
  });
}

var shortcutTop = 0;

function shortcutMove(step){

  if ($('shortcutBtnUp').disabled){
    return;
  }
  
  $('shortcutBtnUp').disabled = true;
  
  var height = $('bannerShortcut').getStyle('height').replace('px','') * 1;
  
  function move(){
    if ($('bannerShortcut').getStyle('top').replace('px','') * 1 > 60 && step < 0){
      shortcutTop = height - 20;
    }
    
    shortcutTop += step;
    window.shortcutScroll = setTimeout(move, 10);
      
    $('bannerShortcut').setStyle({'top': (0 - shortcutTop) + 'px'});
    
    if (!(shortcutTop % 64)){
      clearTimeout(shortcutScroll);
      $('shortcutBtnUp').disabled = false;
    }
    
    if (height <= (shortcutTop + 20)){
      shortcutTop = -64;
      //clearTimeout(shortcutScroll);
    }

  }

  move();
}

function reloadShortcut(){
  
  var length = $('bannerShortcut').getElementsByTagName('tr').length;
  for (var i = 0;i < length; i++){
    $('bannerShortcut').deleteRow(0)
  }
  $('bannerShortcut').setStyle({'top': '0px'})
  fillShortcut();
}

fillShortcut();
</script>
</body>
</html>