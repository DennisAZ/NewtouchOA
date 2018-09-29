/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件


 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"290\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function WarningMsrgLong(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"410\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=" <h4 class=\"title\">信息</h4>"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}

/**
 * 判断小数位后２位
 * @param aValue
 * @return
 */
function isNumbers(aValue) { 
  var digitSrc = "0123456789"; 
  aValue = "" + aValue; 
  if (aValue.substr(0, 1) == "-") { 
    aValue = aValue.substr(1, aValue.length - 1); 
  } 
  var strArray = aValue.split("."); 
  // 含有多个“.” 
  if (strArray.length > 2) { 
    return false; 
  } 
  var tmpStr = ""; 
  for (var i = 0; i < strArray.length; i++) { 
    tmpStr += strArray[i]; 
  } 
  for (var i = 0; i < tmpStr.length; i++) { 
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i)); 
    if (tmpIndex < 0) { 
  // 有字符不是数字 
      return false; 
    } 
  } 
  if(aValue.indexOf(".") != -1){
    var str = aValue.substr(aValue.indexOf(".")+1, aValue.length-1);
    if(str.length > 2){
      return false;
    }
    if(str.length == 0){
      return false;
    }
  }
  // 是数字 
  return true;
}


/**
 * 自动补全0
 * printCount:总打印份数
 * startNumNo：所有开始编号
 * endNumNo : 所有的结束编号
 * reciveDeptValue:单位字符串、已逗号分隔
 */
function autoCompletionStr(printCount,startNo,endNo,reciveDeptValue){
  var cType = "1";
  if(!startNo){
    startNo = "1";
    cType = "0"
  }
  if(!endNo){
    endNo = "";
  }
  var NextNo = startNo;
  
  var realPrintCount = 0;
  if(reciveDeptValue){//如果存在单位
    var reciveDeptArray = reciveDeptValue.split(',');//接收单位转化成数组
    for(var i = 0; i < reciveDeptArray.length ; i++){//循环单位数组
      if($("print_count_" + i)){//判断是否有此单位打印份数字段
        var printCountI = $("print_count_" + i).value;
        if(isInteger(printCountI)){
          realPrintCount = realPrintCount + parseInt(printCountI,10);
          if(realPrintCount> parseInt(printCount,10)){
            alert("实际总打印份数不能超过总打印" + printCount + "份数！");
            break;
          }
          var NextNoTemp = getPrintNo(printCountI,NextNo,endNo,cType,i);
          if(!NextNoTemp){//如果返回0或者空，则跳出
            break;
          }
          NextNo = NextNoTemp;
        }else{
          alert("请输入正确的数字");
           $("print_count_" + i).select();
           $("print_count_" + i).focus();
          break;
        }
      }else{
        alert("没有此打印份数对象！");
        break;
      }

    }
  }
 return NextNo;
   
}
/**
 * 
 * @param printCount : 打印份数
 * @param startNumNo :开始编号
 * @param endNumNo:结束编号
 * 
 * cType:控制状态：0:不控制，1：控制
 * index:索引
 */
function getPrintNo(printCount,startNumNo,endNumNo,cType,index){
  var startNum = parseInt(startNumNo,10);
  var printCountInt = parseInt(printCount,10);
  var startNo = startNum;
  var endNoInt = printCountInt + startNum-1;
  var endNo = printCountInt + startNum -1;
  for(var i=1;i<=3;i++){
    if(startNum.toString().length<i){
      startNo = "0" + startNo;
    }
  }

  for(var i=1;i<=3;i++){
    if(endNoInt.toString().length<i){
      endNo = "0" + endNo;
    }
  }
  if(cType == '1' ){//需要控制
    if(parseInt(endNumNo,10)< parseInt(startNo,10)){//最大编号小于开始编号
      alert("此正文已没有可打印的编号!");
      return 0;
    }
    if(parseInt(endNumNo,10)< parseInt(endNoInt,10)){//最大编号小于结束编号
     // alert("最大编号不能小于结束编号");
      alert("此正文已没有可打印的编号!");
      return 0;
    }
  }
  $("print_no_start_" + index).value = startNo;
  $("print_no_end_" + index).value = endNo;
 // alert(startNo + ":"+ endNo);
  return parseInt(endNo,10) + 1;
}

//判断正整数  
function checkRate(input){ 
  var re = /^[0-9]+[0-9]*]*$/;
  if(!re.test(input)) {  
    return false;  
  }  
  return true;
}  

function replaceAll(str,s1,s2){   
  return str.replace(new RegExp(s1,"gm"),s2);   
}  

function isOnline(){
  var url =  contextPath + "/yh/core/esb/client/act/YHEsbConfigAct/isOnline.act";
  var rtJson = getJsonRsAsyn(url , null , updateState);
}

function updateState(rtJson) {
  var isline = false;
  if(rtJson.rtState == "0"){
    if (rtJson.rtData) {
      isline = true;
    } 
  } else {
    isline = false;
  }
  if (isline) {
    $('state').update("<img  src=\"../images/a1.gif\" align=\"absmiddle\">已连接");
  } else {
    $('state').update("<img  src=\"../images/a0.gif\" align=\"absmiddle\">未连接");
  }
}

function isOnline1(){
  var url =  contextPath + "/yh/core/esb/client/act/YHEsbConfigAct/isOnline.act";
  var rtJson = getJsonRsAsyn(url , null , updateState1);
}

function updateState1(rtJson) {
  var isline = false;
  if(rtJson.rtState == "0"){
    if (rtJson.rtData) {
      isline = true;
    } 
  } else {
    isline = false;
  }
  if (isline) {
    $('state').update("<img  src=\"../../images/a1.gif\" align=\"absmiddle\">已连接");
  } else {
    $('state').update("<img  src=\"../../images/a0.gif\" align=\"absmiddle\">未连接");
  }
}

//清空
function clearDept(){
  $('reciveDept').value='';
  $('reciveDeptDesc').value='';
  $('reciveDeptFlag').value='';
  var inputs = $('selectTr').getElementsByTagName('input');
  for(var i = 0; i < inputs.length; i++){
    if(inputs[i].type == "checkbox"){
      inputs[i].checked = false;
    }
  }
  printTable();
}