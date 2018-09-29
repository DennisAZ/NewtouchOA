var mateRetNameArray = null;
function selectMate(retArray) {
  mateRetNameArray = retArray;
  var url = contextPath + "/subsys/inforesource/matetree.jsp";  
  openDialog(url, 470, 400);
}