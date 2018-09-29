<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关系搜索</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="touchGraph.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/keymap.js"></script>
<script type="text/javascript">
var url = contextPath + "/yh/subsys/inforesouce/act/YHTouchGraphAct/getArray.act?id=";
function clickFunTest(event, id, childWindow) {
  touchGraphUrl = url + id;
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      childWindow.selectedNode = ids;
    }
  } else {
    childWindow.location.reload();
  }
}
function dbClickFunTest(event, id, childWindow) {
 // touchGraphUrl = url + id;
 // childWindow.location.reload();
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      //childWindow.selectedNode = ids;
    }
    touchGraphUrl = url + ids;
    childWindow.location.reload();
  }
}
function cDbClickFunTest(event, id, childWindow) {
  
}
function cClickFunTest(event, id, childWindow) {
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      childWindow.selectedNode = ids;
    }
  }
}
/**
 * 查看后缀ext是否在exts中

 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}
function doInit() {
  showTouchGrap(url 
      ,'clickFunTest'
      ,'cClickFunTest'
      ,'dbClickFunTest'
      ,'cDbClickFunTest' , true , this.main);
  
  
}
function clickFunTest2(event, subject, childWindow) {
  var id = main.centerNode.id;
  var url = contextPath + "/core/module/touchgraph/right.jsp?&subject=" + encodeURIComponent(subject) + "&id=" + id;
  right.location = url;
}
</script>
</head>
<frameset cols="*"  frameborder="no" border="0"  rows="*,100"  framespacing="0" onload="doInit()">
  <frameset cols="200,*,200" rows="*"  framespacing="0" onload="doInit()">
  <frame src="left.jsp" name="left"  id="left" title="主题词" />
  <frame name="main"  scrolling="auto" id="main" title="touchgraph"/>
  <frame src="" name="right" scrolling="auto"  id="right" title="相关文章列表" />
  </frameset>
  <frame src="time.jsp" name="bottom" scrolling="auto"  id="bottom" title="时间轴" />
</frameset>
</html>