<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body onload=""> 
<div class="banner_bg">
	<div class="" style="float:left;">
	  <img id="logoImg" src="<%=contextPath%>/yh/core/funcs/setdescktop/syspara/act/YHSysparaAct/queryHeaderImg.act?para=<%=Math.random() %>"/>
	</div>
	<div class="banner_overflow_x_scroll" style="float:right;">
	  <div class="banner_scroll_up">
      <a href="javascript:void(0);" onclick="return false;" style="display:none;" id="shortcutBtnDown">
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
      <a href="javascript:void(0);" onclick="return false;" style="display:none;" id="shortcutBtnUp">
        <img onmouseover="this.src='<%=imgPath %>/banner/scroll_down_mouseon.jpg'" onmouseout="this.src='<%=imgPath %>/banner/scroll_down.jpg'" src="<%=imgPath %>/banner/scroll_down.jpg"></img>
      </a>
    </div>
	</div>
	
	<div class="clear"></div>
</div>
<script type="text/javascript">
var shortcutTop = -64;

function fillShortcut(){
  var url = '<%=contextPath %>/yh/core/frame/act/YHClassicInterfaceAct/listShortCut.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (!$.isArray(json)) {
        return;
      }

      $('#bannerShortcutTb').empty();
      
      if (json.length > 4) {
        $('#shortcutBtnDown').show();
        $('#shortcutBtnUp').show();

        $('#shortcutBtnDown').bind('click', function() {
          if ( -shortcutTop >= $('#bannerShortcut').outerHeight()) {
            shortcutTop = 0;
            $('#bannerShortcut').css({'top': '64px'});
          }
          $('#bannerShortcut').animate({
	            'top': shortcutTop
	          }, 'normal',
	          function() {
	            shortcutTop += -64;
	          }
	        );
        });

        $('#shortcutBtnUp').bind('click', function() {
          shortcutTop += 64 * 2;
          if (shortcutTop > 0) {
            shortcutTop = -$('#bannerShortcut').outerHeight() + 64;
            $('#bannerShortcut').css({'top': '-' + $('#bannerShortcut').outerHeight()});
          }
          $('#bannerShortcut').animate({
	            'top': shortcutTop
	          }, 'normal',
	          function() {
	            shortcutTop += -64;
	          }
	        );
        });
      }

      var tr = null;
      
      $.each(json, function(i, e) {
        var td = $('<td class="banner_shortcut_bg" cellspacing="0" cellpadding="0" border="0"></td>');
        var a = $('<a href="javascript:void(0)"></a>');
        td.append(a);
        a.html(e.text);
        a.bind('click', function() {
          dispParts(e.url);
        });

        if (!(i % 4)){
          tr = $('<tr></tr>');
          $('#bannerShortcutTb').append(tr);
        }

        tr.append(td);
      });
    }
  });
}

function reloadShortcut(){
  fillShortcut();
}

fillShortcut();
</script>
</body>
</html>