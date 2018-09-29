window.TurnNext_forwardPage = turnNextForwardPage2;
function turnNextForwardPage2(){
  var obj = this;
  var parentObj = obj.parent;
  if (parentObj.isParent) {
    obj = parentObj;
  }
  if (isManage) {
    obj.location = contextPath + "/core/funcs/doc/flowrun/manage/index.jsp?skin="+ skin +"&sortId=" + sortId;
  } else {
    obj.location = "../index.jsp?skin="+skin+"&sortId=" + sortId;
  }
}