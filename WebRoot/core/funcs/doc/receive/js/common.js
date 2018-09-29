/**
 * 把数字转化为保密级别
 * @param num
 * @return
 */

function confLevel(num){
  if('1' == num){
    return "绝密";
  }else if('2' == num){
    return "机密";
  }else{
    return "一般";
  }
}
/**
 * 弹出窗口
 * @param url
 * @return
 */
function openWin(url){  
  var width = (screen.availWidth-12)
  var heigth = (screen.availHeight-38)
  var myleft=(screen.availWidth-500)/2;
  window.open(url, "window","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=10,top=10,resizable=yes,height="+ heigth +", width="+ width +"");
}

var imgUp = imgPath + "/arrow_up.gif";
var imgDown = imgPath + "/arrow_down.gif";