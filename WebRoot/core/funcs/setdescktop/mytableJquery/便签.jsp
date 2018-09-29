<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div id="noteContainer" class="" style="overflow: auto; overflow-x: hidden; position:relative;width:100%;">
  <div id="noteScroll" class="module_div" style="margin: 0 auto;width:98%;height: auto;">
    <textarea id="moduleNote" style="margin: 0; padding: 0;background-color:#FFFFCC;position:relative;line-height:20px;height: 300px; width:100%;border:0px;overflow:hidden;"></textarea>
  </div>
</div>
<script type="text/javascript">
(function() {
  var lines = ${param.lines};
  var scroll = ${param.scroll};
	function resizeNoteScroll() {
	  var records = 0;
	  var notes = $('moduleNote').value.match(/\n/gi);
	  if (notes){
	    records = notes.length + 1;
	  }
	
	  $('moduleNote').setStyle({
	    'height': (((records > lines ? records : lines) * 20) - 5) + 'px'
	  });
	}
	
	function doInitNote(){
	  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/notes/act/YHNotesAct/getNotes.act");
	  
	  if (rtJson.rtState == "0") {

	    $('moduleNote').value = rtJson.rtData.replace(/&#13;&#10;/g,'\r\n');
	
	    $('noteContainer').setStyle({'height': (lines * 20) + 'px'});
	
	    var notes = $('moduleNote').value.match(/\n/gi);
	
	    var records = 0;
	    
	    if (notes){
	      records = notes.length + 1;
	    }
	    $('moduleNote').setStyle({'height': ((records > lines ? records : lines) * 20 - 5) + 'px'});
	    //$('noteContainer').setStyle({'height': (recoreds > lines ? recoreds : lines) * 20 + 'px'});
	    
	    cfgModule({
	      records: lines,
	      lines: lines,
	      name: '便签',
	      showPage:  function(i){
	        $('moduleNote').setStyle({'top': (- i * lines * 20) + 'px'});
	      }
	    });
	
	    if (scroll){
	      $('moduleNote').setStyle({'position': 'static'});
	      Marquee('noteScroll',80,1);
	    }
	  }else {
	    
	  }
	}
	
	function saveNote(){
	  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/notes/act/YHNotesAct/saveNote.act";
	  var para = 'note=' + $('moduleNote').value;  
	  var rtJson = getJsonRs(url, para);
	  if (rtJson.rtState == "0") {
	    
	  }
	  else{
	    alert('保存失败');
	  }
	}
	
	$('moduleNote').onblur = saveNote;
	$('moduleNote').onfocus = function() {
  }
	$('moduleNote').onkeyup = resizeNoteScroll;
	
	doInitNote();
}) ();

</script>
</body>
</html>