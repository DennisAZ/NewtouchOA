
/**
 * 把字符串的IP转化为一个数值(用来比较IP大小)
 */
function ip2Number(ip)  {
  var ipArray = ip.split(".");
  var lvl = [255*255*255 , 255*255 ,255 , 1];
  var value = 0;
  ipArray .each(function(e, i) {
     value += e * lvl[i];
  });
  return value;
}

function check(){
  var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/;
  var beginIp = document.getElementById("beginIp");
  var endIp = document.getElementById("endIp");
  if (!reg.test(beginIp.value)) {
    alert("起始IP格式不合法");
    beginIp.focus();
    beginIp.select();
    return false;
  }
  if (!reg.test(endIp.value)) {
    alert("结束IP格式不合法");
    endIp.focus();
    endIp.select();
    return false;
  }
  if ((beginIp.value.length == 0)) {
    alert("起始IP不能为空！");
    beginIp.focus();
    return false;
  }
  if ((endIp.value.length == 0)) {
    alert("结束IP不能为空！");
    endIp.focus();
    return false;
  }
  var beginIpStr = beginIp.value;
  var endIpStr = endIp.value;

  if(ip2Number(beginIpStr) > ip2Number(endIpStr)){
    alert("结束IP应大于起始IP");
    beginIp.focus();
    beginIp.select();
    return false;
  }
  return true;
}