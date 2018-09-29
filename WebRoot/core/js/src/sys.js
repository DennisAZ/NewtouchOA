var isNS4 = (document.layers) ? 1 : 0;
var isIE4 = (document.all)? 1 : 0;
var isMoz = ((document.getElementById) && !(isIE4)) ? 1 :0;
var isTouchDevice = "ontouchstart" in window;

function MM_reloadPage(init) {  // reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
function MM_swapImgRestore() { // v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { // v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { // v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { // v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

String.prototype.trim = function(){
  return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function(){
  return this.replace(/(^\s*)/g,"");
}
String.prototype.rtrim = function(){
  return this.replace(/(\s*$)/g,"");
}

/**
 * 由键值取得本地消息


 * 
 */
function lm(msrgKey) {
  var rtStr = null;
  if (lcalMsrgMap) {
    rtStr = lcalMsrgMap[msrgKey];
  }
  if (rtStr == "" || rtStr) {
    return rtStr;
  }
  return msrgKey;
}

/**
 * 空函数，异步处理中使用


 * 
 */
function emptyFunc() {
}
function test() {
}
/**
 * 取得当前页面的URL，到主机端口部分
 * 
 * @return 例：http://192.168.0.123:8081
 */
function getUrl2Host() {
  return window.location.protocol + "//" + window.location.host;
}
/**
 * 取消事件往下传递


 * 
 */
function stopEventBuble() {
  if (event) {
    event.returnValue = false;
  }
}

function selectLast(inputCntrl) {
  if (!inputCntrl || !$(inputCntrl).visible()) {
    return;
  }
  var range = inputCntrl.createTextRange();
  range.moveStart("character", inputCntrl.value.length);
  range.select();
}

/**
 * 清除select控件的数据


 * 
 * @selectObj select控件
 */
function clearSelectData(selectObj) {
  if (!selectObj.options) {
    return;
  }
  for (var i = selectObj.options.length - 1; i >= 0; i--) {
    selectObj.remove(i);
  }
}

/**
 * 加载select控件的数据


 * 
 * @selectObj select控件
 * @data 控件需要显示的数据 数据结构Array(CodeRecord, ...)
 */
function loadSelectData(selectObj, data, selectValue) {
  clearSelectData(selectObj);
  if (!data) {
    return;
  }
  var dataSize = data.size();
  for (var i = 0; i < dataSize; i++) {
    var codeRecord = data.get(i);
    var myOption = document.createElement("option");
    myOption.value = codeRecord.code;
    myOption.text = codeRecord.desc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    if (myOption.value == selectValue) {
      myOption.selected = true;
    }
  }
}

/**
 * 输出错误信息
 * 
 * @msrg 错误信息
 */
function displayError(msrg) {
  document.body.innerHTML = msrg;
}

/**
 * 定长字符串-前面拼接
 */
function appendBefore(srcStr, outLength, appendChar) {
  if (!appendChar) {
    appendChar = " ";
  }
  srcStr = "" + srcStr;
  var srcLength = srcStr.length;
  if (srcLength >= outLength) {
    return srcStr;
  }
  var outStr = srcStr;
  for (var i = srcLength; i < outLength; i++) {
    outStr = appendChar + outStr;
  }
  return outStr;
}

/**
 * 定长字符串-前面拼接
 */
function appendAfter(srcStr, outLength, appendChar) {
  if (!appendChar) {
    appendChar = " ";
  }
  srcStr = "" + srcStr;
  var srcLength = srcStr.length;
  if (srcLength >= outLength) {
    return srcStr;
  }
  var outStr = srcStr;
  for (var i = srcLength; i < outLength; i++) {
    outStr = outStr + appendChar;
  }
  return outStr;
}

/**
 * 插入千分符, 保留指定长度的小数


 * 
 * @数字
 * @小数位数
 */
function insertKiloSplit(aValue, num) {
  aValue = "" + aValue;

  if (aValue == null || aValue == "") {
    return "";
  }
  var op = "";
  if (aValue.substring(0, 1) == "-") {
    op = "-";
    aValue = aValue.substring(1, aValue.length);
  }

  var val = parseFloat(aValue);

  if (isNaN(val)) {
    alert(lm("jsp.js.sys.js_1"));
    return "";
  }
  aValue = forDight(aValue, num);
  var value_array = ("" + aValue).split(".");

  // 格式化整数位
  start = 0;
  len = 0;
  val_int = "";

  while(1)
  {
    if((value_array[0].length - start) % 3 == 0) {
       len = 3;
    }else {
     len = value_array[0].length % 3;
    }

    if(val_int == "") {
       val_int = value_array[0].substring(start, start + len);
    }else {
       val_int = val_int + "," + value_array[0].substring(start, start+len);
    }
    start = len + start;

    if(start >= value_array[0].length) {
       break;
    }
  }
  if(val_int != "") {
    value_array[0] = val_int;
  }

  var tmpStr = "";
  // 格式化小数位
  if(value_array.length == 1) {
    for (var i = 0; i < num; i++) {
      tmpStr += "0";
    }
  }else if(value_array.length == 2) {
    tmpStr = value_array[1];
    var loopCnt = num - tmpStr.length;
    for (var i = 0; i < loopCnt; i++) {
      tmpStr += "0";
    }
  }

  return (op + value_array[0] + "." + tmpStr);
}

/**
 * 删除千分符


 * 
 */
function clearKiloSplit(aValue) {
  aValue += "";
  if (aValue == null || aValue == "") {
    return "";
  }
  var valArray = aValue.split(",");
  var rtStr = "";
  for (var i = 0; i < valArray.length; i++) {
    rtStr += valArray[i];
  }
  return rtStr;
}

String.prototype.replaceAll  = function(s1,s2){   
  return this.replace(new RegExp(s1,"gm"),s2);   
}
/**
 * 处理URI编码
 */
function toUtf8Uri(uri) {
  if (!uri) {
    return "";
  }
  uri = encodeURIComponent(uri).replaceAll("'", "%27");
  return uri;
}

/**
 * 创建随机ID号


 * 
 * @prefix 前缀
 */
function createRandomId(prefix) {
  if (!prefix) {
    prefix = "";
  }
  var nodeId = "" + new Date().getTime();
nodeId += parseInt(Math.random() * 10000);
nodeId = prefix + nodeId;

return nodeId;
}

/**
 * 设置控件的


 * 
 * @isReadOnly 状态


 * @class 风格属性


 */
function setReadOnly(cntrl, isReadOnly, styleClass) {
  if (!cntrl) {
    return;
  }
  cntrl.readOnly = isReadOnly;
  setClass(cntrl, styleClass);
}

/**
 * 设置控件的风格属性名称


 * 
 * @cntrl 控件对象
 * @styleClass 风格属性名称


 */
function setClass(cntrl, styleClass) {
  if (!cntrl) {
    return;
  }
  cntrl.className = styleClass;
}

/**
 * 判断窗口是否模态窗口


 */
function isModal(windowObj) {
  if (!windowObj) {
    windowObj = window;
  }
  return windowObj.top.dialogArguments;
}

/**
 * 取得模态窗口父级窗口引用


 */
function getOpenerModal(windowObj) {
  if (!windowObj) {
    windowObj = window;
  }
  var parentWindow = windowObj.dialogArguments;
  if (parentWindow) {
    return parentWindow;
  }
  parentWindow = windowObj.top.dialogArguments;
  if (parentWindow) {
    return parentWindow;
  }
  if (windowObj.parent) {
    parentWindow = windowObj.parent.dialogArguments;
  }
  if (parentWindow) {
    return parentWindow;
  }
}

/**
 * 取得模态窗口所在主窗口顶层引用
 */
function getMainWindow() {
  var parentWindow = getOpenerModal();
  return parentWindow.top;
}

/**
 * 取得打开该窗口的窗口
 */
function getOpenerWindow(windowObj) {
  if (!windowObj) {
    windowObj = window;
  }
 
  // 模态窗口


  var openerWindow = getOpenerModal(windowObj);
  if (openerWindow) {
    return openerWindow;
  }
  // 非模态窗口


  openerWindow = windowObj.top.opener;
  if (openerWindow) {
    return openerWindow;
  }
  
  return null;
}

/**
 * 取得顶层窗口
 */
function getRootWindow(windowObj) {
  if (!windowObj) {
    windowObj = window;
  }
  var rootWindow = getOpenerWindow(windowObj);
  if (!rootWindow) {
    return windowObj;
  }
  while (getOpenerWindow(rootWindow)) {
    rootWindow = getOpenerWindow(rootWindow);
  }
  return rootWindow;
}

/**
 * 退出


 */
function logout(msrg) {
  var windowList = new Array();
  var currWindow = window;
  var parentWindow = getOpenerWindow(currWindow);
  var topWindow = null;
  while (parentWindow) {
    if (currWindow.winManager) {
      break;
    }  
    windowList.add(currWindow);
    currWindow = parentWindow;
    parentWindow = getOpenerWindow(currWindow);
  }
  topWindow = currWindow;
  // 需要把关闭窗口的代码放置到顶级窗口执行，这样可以避免模态窗口阻塞父级窗口关闭问题


  if (!isDependsOa) {
    try {
      topWindow.top.closeAndLogout(windowList, msrg);
      return;
    }catch(e) {
    }
    try {
    topWindow.alert(msrg);
    topWindow.winManager.closeAll();
    return;
  }catch(e) {
  }
  }else {
    topWindow.alert(msrg);
    try {
      topWindow.winManager.closeAll();
    }catch(e) {
    }
  }
}

/**
 * 根窗口执行的退出，用来解决模态窗口阻塞父级窗口关闭问题


 */
function closeAndLogout(windowList, msrg) {
  for (var i = 0; i < windowList.size(); i++) {
    if (!isModal(windowList.get(i - 1))) {
      windowList.get(i).top.closeWindow();
      windowList.setAt("", i);
    }
  }  
  try {
    hiddAll();
  }catch(e) {
  }
  try {
    if (msrg) {
      alert(msrg);
    }
  }catch(e) {
  }
  try {
    doLogout();
  }catch(e) {
  }
}

/**
 * 关闭非模态窗口


 */
function closeWindow() {
  if (isModal()) {
    // window.opener = null;
    window.close();
  }else if (window.frameElement && (window.frameElement["currWindow"] || window.frameElement["isInSelfSubWindow"])) {
    windows.util.closeModal();
  }else {
    if (!isDependsOa) {
      if (window.isTopIndex) {
        return;
      }
      // window.opener = null;
    window.close();
    }else {
      try {
        // window.opener = null;
      window.close();
      }catch(e) {
      }
    }
  }
}

/**
 * 以全屏方式打开窗口
 */
function openWindow(actionUrl, title, width, height) {
  var winParam = "menubar=0,toolbar=0,status=0";
  winParam += ",scrollbars=1,resizable=1";
  if (!width) {
    width = 800;
  }
  if (!height) {
    height = 600;
  }
  var left = (screen.width - width) / 2;
  var top = (screen.height - 100 - height) / 2;
  winParam += ",width=" + width;
  winParam += ",height=" + height;
  winParam += ",top=" + top;
  winParam += ",left=" + left;
  winParam += ",location=no";
  
  return window.open(actionUrl, title, winParam);
}

/**
 * 打开模态窗口,不能改变窗口大小
 */
function openDialog(actionUrl, width, height) {
  var locX = (screen.width - width) / 2;
  var locY = (screen.height - height) / 2;
  var attrs = null;
  
  attrs = "status:no;directories:no;scroll:no;resizable:no;";
  attrs += "dialogWidth:" + width + "px;";
  attrs += "dialogHeight:" + height + "px;";
  attrs += "dialogLeft:" + locX + "px;";
  attrs += "dialogTop:" + locY + "px;";
  return window.showModalDialog(actionUrl, self, attrs);
}

/**
 * 打开模态窗口,能改变窗口大小


 */
function var supArryChild="";
function updateObject(){

	for(var i=0;i<arrMainPro.length;i++){
	for(var j=1;j<arrMainPro[i].sup.length;j++){
		if (supArryChild!= "") {
				supArryChild += ";";
		}
		supArryChild+=arrMainPro[i].pro_id+","+arrMainPro[i].pro_code+","+arrMainPro[i].pro_name+","+arrMainPro[i].typeName+","+arrMainPro[i].unitName+","+arrMainPro[i].price+","+arrMainPro[i].sup[j].supId+","+arrMainPro[i].sup[j].supName+","+arrMainPro[i].sup[j].supPrice;
	}}
}(actionUrl, width, height) {
  var locX = (screen.width - width) / 2;
  var locY = (screen.height - height) / 2;
  var attrs = null;
  
  attrs = "status:no;directories:yes;scroll:yes;Resizable=yes;";
  attrs += "dialogWidth:" + width + "px;";
  attrs += "dialogHeight:" + height + "px;";
  attrs += "dialogLeft:" + locX + "px;";
  attrs += "dialogTop:" + locY + "px;";
  return window.showModalDialog(actionUrl, self, attrs);
}

/**
 * 通过活动流水号打开活动窗口
 * 
 * @actionId 活动流水号，必须传递


 * @width 窗体宽度
 * @height 窗体高度
 * @module 模块
 */
function openActionWindowById(actionId, actionName, width, height, module) {
  if (!actionId || actionId == '0') {
    return;
  }
  if (!width) {
    width = 800;
  }
  if (!height) {
    height = 500;
  }
  
  var url = contextPath + "/openactionwindow.do";
  url += "?action_id=" + actionId;
  url += "&action_name=" + actionName;
  if (module) {
    url += "&action_module=" + module
  }

  openDialog(url, width, height);
}

/**
 * 通过活动路径打开活动窗口
 * 
 * @actionPath 活动路径，必须传递


 * @width 窗体宽度
 * @height 窗体高度
 * @module 模块
 */
function openActionWindowByPath(actionPath, actionName, width, height, module) {
  if (!actionPath) {
    alert(lm("jsp.js.sys.js_6"));
    return;
  }
  if (!width) {
    width = 800;
  }
  if (!height) {
    height = 600;
  }
  var url = contextPath + actionPath;
  
  // openDialog(url, width, height);
  // windows.util.openModal(url, actionName, width, height);
  // window.open(url, 'oa_sub_window', 'resizable=yes');
  return openWindow(url, actionName, width, height);
}

/**
 * 弹出代码选择窗口
 * 
 * @rtFieldName             返回字段名称
 * @queryFieldNameStr       查询字段名称字符串


 * @tableNo                 关联表编码


 * @filterStr               查询条件
 * @orderStr                排序字段
 * @parentFieldName         层次关系字段名称
 * @relaField               关联字段，在存在代码关联的情况下使用
 * @nonLeafSelect           是否能选择非叶子节点fase=不可以选择；true=可以选择
 * @exFunc                  返回函数，在复杂代码设置情况下使用


 * @paramNumber             返回值个数,目前在subject,acctItem中可能出现多个


 * @sort                    参数类型,如果是subject,acctItem,且为多选,则需要考虑sort=subject和acctItem时候的框架的不同处理.
 * @acctYearNow             会计年度
 * @acctItemFilter          辅助核算关联筛选条件


 */
function openCodeSelectWindow(
  rtFieldName,
  queryFieldNameStr,
  tableNo,  
  filterStr,
  orderStr,
  parentFieldName,
  relaField,
  nonLeafSelect,
  exFunc,
  paramNumber,
  sort,
  acctYearNow,
  acctItemFilter) {
  
  if (relaField && !document.all(relaField).value) {
    alert(lm("jsp.js.sys.js_2"));
    return;
  }
  
  var actionUrl = contextPath + "/common/codeselectframe.jsp";
  actionUrl += "?rt_field_name=" + (rtFieldName ? rtFieldName : "");
  actionUrl += "&q_field_name=" + (queryFieldNameStr ? queryFieldNameStr : "");
  actionUrl += "&p_field_name=" + (parentFieldName ? parentFieldName : "");
  actionUrl += "&TABLE_NO=" + (tableNo ? tableNo : "");
  actionUrl += "&filt_str=" + (filterStr ? filterStr : "");
  actionUrl += "&order_str=" + (orderStr ? orderStr : "");
  actionUrl += "&non_leaf_select=" + (nonLeafSelect ? nonLeafSelect : "");
  actionUrl += "&ex_func=" + (exFunc ? exFunc : "");
  // by cly
  actionUrl += "&paramNumber=" + (paramNumber ? paramNumber : "");
  actionUrl += "&sort=" + (sort ? sort : "");
  // 辅助核算需要,subject可以在filt_str里面设置.
  actionUrl += "&acctYearNow=" + (acctYearNow ? acctYearNow : "");

  // 海外系统人员筛选特别处理


  if (isUsedForNorinco && sort == "acctItem" && filterStr && filterStr.indexOf('\'2\'') > 0) {
    if (!acctItemFilter) {
      acctItemFilter = "";
    }
    if (acctItemFilter.indexOf("00013007:") < 0) {
      var tmpArray = acctItemFilter.split(";");
      if (!tmpArray || tmpArray.length == 1) {
        acctItemFilter += acctItemFilter + ",00013007:" + acsetOrgId;
      }else {
        acctItemFilter = tmpArray[0] + ",00013007:" + acsetOrgId
          + ";" + tmpArray[1];
      }
    }
  }
  if (acctItemFilter) {
    actionUrl += "&ITEM_FILTER=" + acctItemFilter;
  }
  
  openDialogResize(actionUrl, 600, 400);
}

/**
 * 弹出普通的日期窗口
 * 
 * @codeFieldID     填充日期的控件名称


 * @paramStr        参数字符串


 */
function openDateWindow(codeFieldId, paramStr) {
  if (!codeFieldId) {
    alert(lm("jsp.js.sys.js_3"));
    return;
  }
  var loc_x = event.clientX - 100;
  var loc_y = event.clientY + 150;
  var actionUrl = contextPath + "/common/calendar.jsp";
  actionUrl += "?FIELDNAME=" + codeFieldId;
  if (paramStr) {
    actionUrl += "&" + paramStr;
  }
  openDialog(actionUrl, 300, 210);
}

/**
 * 调整非模态窗口的大小
 */
function resizeWindow(windowWidth, windowHeight) {  
  top.moveTo((screen.width - windowWidth) / 2,
    (screen.height - windowHeight) / 2);
  top.resizeTo(windowWidth, windowHeight);
}

/**
 * 使非模态窗口最大化
 */
function maxsizeWindow() {
  top.moveTo(0, 0);
  top.resizeTo(screen.availWidth, screen.availHeight);
}
/**
 * 使非模态窗口最大化
 */
function maxsizeModalWindow() {
  resizeModalWindow(screen.availWidth, screen.availHeight);
}

/**
 * 调整非模态窗口的大小
 */ 
function resizeModalWindow(windowWidth, windowHeight) {
  top.parent.dialogLeft = ((screen.width - windowWidth) / 2) + "px";
  top.parent.dialogTop = ((screen.height - windowHeight) / 2) + "px";
  top.parent.dialogWidth = windowWidth + "px";
  top.parent.dialogHeight = windowHeight + "px";  
}
var selectID = null;
/**
 * 单选窗口中设置选择的状态


 * 
 * @obj 选中的行控件
 * @action 动作
 * @exFunc 选择后需要执行的动作，用于扩展


 */
function setPointer(obj, action, exFunc) {
  var defaultClass = "TableLine2";
  var rowIndex = null;
    
  if (action == "over") {
    obj.className = "TableLineHover";
    if (selectID && $(selectID)) {
      $(selectID).className = "TableLineSelect";
    }
  }else if (action == "out") {
    rowIndex = parseInt(obj.id.substr(3));
    if (rowIndex % 2 != 0) {
      defaultClass = "TableLine1";
    }else {
      defaultClass = "TableLine2";
    }
    obj.className = defaultClass;
    if (selectID && $(selectID)) {
      $(selectID).className = "TableLineSelect";
    }
  }else if (action == "click") {
    if (obj.id != selectID) {// 选中
      if (selectID) {
        rowIndex = parseInt(selectID.substr(3));
        if (rowIndex % 2 != 0) {
          defaultClass = "TableLine1";
        }else {
          defaultClass = "TableLine2";
        }
        if ($(selectID)) {
          $(selectID).className = defaultClass;
        }
      }
      obj.className = "TableLineSelect";
      selectID = obj.id;
    }else {// 取消选择
      if (selectID) {
        rowIndex = parseInt(selectID.substr(3));
        if (rowIndex % 2 != 0) {
          defaultClass = "TableLine1";
        }else {
          defaultClass = "TableLine2";
        }
        if ($(selectID)) {
          $(selectID).className = defaultClass;
        }
        obj.className = defaultClass;
        selectID = "";
      }      
    }
  }else if (action == "select") {
    if (selectID == obj.id) {
      return;
    }
    if (selectID) {
      var cntrl = document.all(selectID);
      rowIndex = parseInt(cntrl.id.substr(3));
      if (rowIndex % 2 != 0) {
        defaultClass = "TableLine1";
      }else {
        defaultClass = "TableLine2";
      }
      if ($(cntrl)) {
        cntrl.className = defaultClass;
      }
    }
    obj.className = "TableLineSelect";
    selectID = obj.id;
  }
}

/**
 * 四舍五入函数
 */
function forDight(value, num)
{
  var tmpVal = parseFloat(value);
  // 计算10的num次方
  bitnum = Math.pow(10, num);
  
  // 按num制定的位数四舍五入


  value = Math.round(tmpVal  * bitnum ) / bitnum;

  return value;
}

/**
 * 把数字转换为字符串，处理小数位全为0的情况，显示成xxx.00
 */
function num2Str(srcNum, scale) {
  var numStr = "" + srcNum;
  if (!scale) {
    scale = 2;
  }
  if (numStr.indexOf(".") < 0) {
    numStr += ".";
    for (var i = 0; i < scale; i++) {
      numStr += "0";
    }
  }
  return numStr;
}

/**
 * 直接去尾，由函数ForDight调用
 */
function formatnumber(value, num)
{
  var a, b, c, i;
  a = value.toString();
  b = a.indexOf('.');
  c = a.length;
  if (num == 0)
  {
    if (b != -1)
      a = a.substring(0, b);
  }
  else
  {
    if (b == -1)
    {
      a = a + ".";
      for (i = 1;i <= num; i++)
        a = a + "0";
    }
    else
    {
      a = a.substring(0,b + num + 1);
      for (i = c; i <= b + num; i++)
      a = a + "0";
    }
  }
  return a;
}

/**
 * 判断是否是数字


 * 
 * @aValue 待检查的值


 */
function isNumber(aValue) {
  var digitSrc = "0123456789";

  if (aValue == null || ("" + aValue).length == 0) {
    return false;
  }
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
  // 是数字


  return true;
}

/**
 * 判断是否是整数


 */
function isInteger(aValue) {
  if (aValue == null) {
    return false;
  }
  var reg = /^[-+]?[1-9][0-9]*$/;
  return reg.test(aValue);
}

/**
 * 判断是否是正整数
 */
function isPositivInteger(aValue) {
  if (aValue == null) {
    return false;
  }
  var reg = /^[1-9][0-9]*$/;
  return reg.test(aValue);
}

/**
 * 判断日期是否有效
 */
function isValidDate(year, month, day) {
  // alert(year + " " + month + " " + day);
  return isValidDateStr(year + "-" + month + "-" + day);
}

/**
 * 判断是否为合法的日期串


 * 
 * @str 日期串


 */
function isValidDateStr(str) {
  if (!str) {
    return;
  }
  var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
  if (r == null) {
    return false;
  }
  if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) {
    return false;
  }
  var d = new Date(r[1], r[3]-1, r[4]); 
  return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
}

/**
 * 判断是否为合法的时间串


 * @param str
 * @return
 */
function isValidTimeStr(str) {
  if (!str) {
    return;
  }
  var r = str.match(/^(\d{1,2}):(\d{1,2}):(\d{1,2})$/); 
  if (r == null) {
    return false;
  }
  if (parseInt(r[1]) > 59 || parseInt(r[1]) < 0) {
    return false;
  }
  if (parseInt(r[2]) > 59 || parseInt(r[2]) < 0) {
    return false;
  }
  if (parseInt(r[3]) > 59 || parseInt(r[3]) < 0) {
    return false;
  }
  return true;
}

/**
 * 判断是否为合法的日期时间串


 * @param str
 * @return
 */
function isValidateTimeStr(str) {
  if (!str) {
    return;
  }
  var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})\s(\d{1,2}):(\d{1,2}):(\d{1,2})$/); 
  if (r == null) {
    return false;
  }
  if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) {
    return false;
  }
  var d = new Date(r[1], r[3]-1, r[4]); 
  if (!(d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4])) {
    return false;
  }
  if (parseInt(r[5]) > 59 || parseInt(r[5]) < 0) {
    return false;
  }
  if (parseInt(r[6]) > 59 || parseInt(r[6]) < 0) {
    return false;
  }
  if (parseInt(r[7]) > 59 || parseInt(r[7]) < 0) {
    return false;
  }
  return true;
}

/**
 * 绑定校验日期时间格式事件
 * [{id, type, mustFill}] 日期：type=d; 日期时间：type=dt; 时间：type=t
 * @return
 */
function bindAssertDateTimePrcBatch(cntrls) {
  if (!cntrls || cntrls.length < 1) {
    return;
  }
  for (var i = 0; i < cntrls.length; i++) {
    var cntrlParam = cntrls[i];
    var cntrl = $(cntrls[i].id);
    var type = cntrls[i].type;
    if (!cntrl) {
      continue;
    }
    if (cntrl.tagName.toLowerCase() != "input") {
      continue;
    }
    cntrl["dtType"] = type ? type : "d";
    cntrl.observe("blur", function(event) {
      var srcElem = Event.element(event);
      if (!srcElem || !srcElem["dtType"]) {
        return;
      }
      var valueStr = srcElem.value;
      if (!valueStr) {
        return;
      }
      if (srcElem["dtType"] == "dt") {
        if (!isValidateTimeStr(valueStr)) {
          alert("请输入合法的日期时间，格式yyyy-MM-dd HH:mm:ss");
          selectLast(srcElem);
          srcElem.select();
          return;
        }else {
          return;
        }
      }else if (srcElem["dtType"] == "d") {
        if (!isValidDateStr(valueStr)) {
          alert("请输入合法的日期，格式yyyy-MM-dd");
          selectLast(srcElem);
          srcElem.select();
          return;
        }else {
          return;
        }
      }else {
        return;
      }
    });  
  }
}

/**
 * 判断是否为合法的帐务年月
 * 
 * @str 年度字符串


 */
function isValidYM(str) {
  var r = str.match(/^\d{4}-\d{2}$/);
  if (r == null) {
    return false;
  }
  return isValidDateStr(str + "-01");
}

/**
 * 删除字符串前后的空格
 * 
 * @str 需要删除前后空格的字符串


 */
function trim(str) {
  if (!str) {
    return "";
  }
  var regEmptyStr = /^(\s|\u3000)*$/;
  if (regEmptyStr.test(str)) {
    return "";
  }

  var reg = /^\s*(\S|\S[\S\s]*\S)\s*$/;
  return str.replace(reg, "$1");
}

/**
 * 非空校验
 * 
 * @tstValue 校验值


 * @cntrl 相关控件
 * @msrg 错误消息
 */
function assertFilled(tstValue, cntrl, msrg) {
  tstValue = "" + tstValue;
  if (tstValue == "") {
    var cntrlName = "";
    if (cntrl && cntrl.title) {
      cntrlName = cntrl.title;
    }
    if (!msrg) {
      msrg = lm("jsp.js.sys.js_4") + cntrlName;
    }
    alert(msrg);
    if (cntrl) {
    cntrl.focus();
    stopEventBuble();
    }
    return false;
  }
  
  return true;
}

/**
 * 数值校验


 * 
 * @tstValue 校验值


 * @cntrl 相关控件
 * @msrg 错误消息
 */
function assertNumber(tstValue, cntrl, msrg) {
  tstValue = "" + tstValue;
  if (tstValue && !isNumber(tstValue)) {
    if (!msrg) {
      msrg = lm("jsp.js.sys.js_1");
    }
    alert(msrg);
    if (cntrl) {
    cntrl.focus();
    cntrl.select();
    stopEventBuble();
    }
    return false;
  }
  return true;
}

/**
 * 大于0的数值校验


 * 
 * @tstValue 校验值


 * @cntrl 相关控件
 * @msrg 错误消息
 */
function assertGreatThan0(tstValue, cntrl, msrg) {  
  if (!assertNumber(tstValue, cntrl, msrg)) {
    return false;
  }  
  if (parseInt(tstValue) < 1) {
    if (!msrg) {
      msrg = lm("需要大于0的数值");
    }
    alert(msrg);
    if (cntrl) {
      cntrl.focus();
      cntrl.select();
      stopEventBuble();
    }
    return false;
  }
  return true;
}

/**
 * 日期校验
 * 
 * @tstValue 校验值


 * @cntrl 相关控件
 * @msrg 错误消息
 */
function assertDate(tstValue, cntrl, msrg) {
  if (tstValue && !isValidDateStr(tstValue)) {
    if (!msrg) {
      msrg = lm("jsp.js.sys.js_5");
    }
    alert(msrg);
    if (cntrl) {
    cntrl.focus();
    cntrl.select();
    stopEventBuble();
    }
    return false;
  }
  return true;
}

/**
 * 检测是否含有中文


 * 
 * @str 待测试的字符串


 */
function containsChinese(str) { 
// 如果值为空，通过校验
if (!str) {
  return false;
}

var pattern = /^.*([\u4E00-\u9FA5]|[\uFE30-\uFFA0])+.*$/; 
return pattern.test(str);
}

/**
 * 检测是否都是中文


 * 
 * @str 待测试的字符串


 */
function isAllChinese(str) { 
// 如果值为空


if (!str) {
  return false;
}

var pattern = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])+$/; 
return pattern.test(str);
}

/**
 * 判断是否是数字


 */
function isNumChar(charStr) {
  // 如果值为空


  if (!charStr) {
    return false;
  }
  var pattern = /^[0-9]$/gi;
  return pattern.test(charStr);
}

/**
 * 判断是否是字母


 */
function isEnglishChar(charStr) {
  // 如果值为空


  if (!charStr) {
    return false;
  }
  var pattern = /^[A-Za-z]$/gi;
  return pattern.test(charStr);
}

/**
 * 判断是否是中文


 */
function isChineseChar(charStr) {
  // 如果值为空


  if (!charStr) {
    return false;
  }
  var pattern = /^[\u4E00-\u9FA5\uFE30-\uFFA0]$/;
  return pattern.test(charStr);
}

/**
 * 解析Boolean型


 */
function parseBoolean(str) {
  if (str == "true") {
    return true;
  }
  return false;
}

/**
 * 生成HTML缩进
 * 
 * @identCnt
 */
function getHtmlIdent(identCnt) {
  var rtStr = "";
  
  for (var i = 0; i < identCnt; i++) {
    rtStr += "&nbsp;&nbsp;";
  }
  
  return rtStr;
}

// 字符串的替换。替换所有的old为new，并返回 ---cly
function replaceStr(str,oldStr,newStr){
  var tmpStr="";
  var pos1,pos1
  pos1=str.indexOf(oldStr);
  pos2=pos1+oldStr.length
  if(pos1<0){
  tmpStr=str;
  return tmpStr;
  }
  while(pos1>=0){
  tmpStr += str.substring(0,pos1);
  tmpStr += newStr;
  str = str.substring(pos2);
  
  pos1=str.indexOf( oldStr );
  pos2=pos1+oldStr.length;
  }
  
  tmpStr += str;
  // alert(tmpStr)
  return tmpStr;
}

/**
 * 把字符串中的空格转换为&nbsp;
 */
function toHtmlEmpty(str) {
  var reg = /\s/g;
  return str.replace(reg, "&nbsp;");
}

/**
 * 判断金额相等，由于舍入的缘故，金额相等的实际涵义并非数学上的严格相等，而是 工程意义上的差额的绝对值小于允许的最大值


 */
function isEqualAmt(amt1, amt2) {
  if (amt1 == amt2) {
    return true;
  }
  if (Math.abs(amt1 - amt2) < Math.pow(10, (0 - amtScale))) {
    return true;
  }
  return false;
}

/**
 * 创建XHttp对象
 */
function createXHttpObj() {
  var xhttp = null;

  // IE7, Mozilla ,Firefox 等浏览器内置该对象


  if(window.XMLHttpRequest){  

    xhttp = new XMLHttpRequest();    
  // IE6、IE5
  }else if(window.ActiveXObject){ 
    try{
      xhttp = new ActiveXObject("Msxml2.XMLHTTP");
    }catch (e){
    }
    if( xhttp == null) {
      try { 
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
      }catch (e){
      }
    }
  }
  
  return xhttp;
}

/**
 * 与服务器通信用客户端
 */
function XHttpClient(waitOkFunc) {
  // xmlhttp和xmldom对象
  this.xhttp = createXHttpObj();
  // post或get发送数据的键值对
  this.dataMap = new ArrayHashMap();
  // http请求头



  this.headMap = new ArrayHashMap();
  // 请求头类型


  this.rtype = 'text';
  // 异步请求返回处理函数
  this.processReturnFunc = waitOkFunc;
  this.xhttp.onreadystatechange = emptyFunc;
  
  // 增加一个POST或GET键值对
  this.addData = function(skey, svalue) {
    this.dataMap.put(skey, svalue);
  }

  // 增加一个Http请求头键值对
  this.addHeadMap = function(skey, svalue){
    this.headMap.put(skey, svalue);
  }
  // 清除当前对象的哈希表参数
  this.reset = function(){
    this.dataMap.clear();
    this.headMap.clear();
  }

  // 发送http请求头


  this.setHead = function(){
    var rkeyCount = this.headMap.size();
    // 发送用户自行设定的请求头


  for(var i = 0; i < rkeyCount; i++) {
    this.xhttp.setRequestHeader(this.headMap.getKey(i), this.headMap.getValue(i)); 
  }
  　if(this.rtype == 'binary'){
      this.xhttp.setRequestHeader("Content-Type", "multipart/form-data");
    }else{
      this.xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    }
  }
  
  /**
   * 用Post方式发送数据


   */
  this.doPost = function(purl, isAsyn){
    this.xhttp.onreadystatechange = emptyFunc;
    var pdata = "";
    
    this.state = 0;
    this.xhttp.open("POST", purl, isAsyn); 
    this.setHead();
    
    var keyCnt = this.dataMap.size();    
    for(var i = 0; i < keyCnt; i++){
      if (pdata == "") {
        pdata = this.dataMap.getKey(i) + '=' + this.dataMap.getValue(i);
      }else {
        pdata += "&" + this.dataMap.getKey(i) + '=' + this.dataMap.getValue(i);
      }
    }
    this.xhttp.send(pdata);
  }
  
  /**
   * 用GET方式发送数据


   */
  this.doGet = function(purl, isAsyn) {
    this.xhttp.onreadystatechange = this.processReturnFunc;
    var gkey = "";
    
    this.state = 0;
    var keyCnt = this.dataMap.size();
    for(var i = 0; i < keyCnt; i++){
      if(gkey == "") {
        gkey = this.dataMap.getKey(i) + '=' + this.dataMap.getValue(i);
      }else {
        gkey += "&" + this.dataMap.getKey(i) + '=' + this.dataMap.getValue(i);
      }
    }
    if(purl.indexOf('?') < 0) {
      purl = purl + '?' + gkey;
    }else {
      purl = purl + '&' + gkey;
    }  
    this.xhttp.open("GET", purl, isAsyn);
    this.setHead();
    this.xhttp.send();
  }

  /**
   * 异步用Post方式发送数据


   */
  this.sendPostAsyn = function(purl) {
    this.doPost(purl, true);
  }

  /**
   * 同步用Post方式发送数据


   */
  this.sendPost = function(purl) {
    this.doPost(purl, false);
  }
  
  /**
   * 异步用GET方式发送数据


   */
  this.sendGetAsyn = function(purl) {
    this.doGet(purl, true);
  };

  /**
   * 同步用GET方式发送数据


   */
  this.sendGet = function(purl) {
    this.doGet(purl, false);
  };
  
  /**
   * 服务器响应是否正常


   */
  this.isReturnOk = function() {
    return (this.xhttp && this.xhttp.readyState == 4 && this.xhttp.status == 200);
  }
  
  /**
   * 取得响应状态码
   */
  this.getResponseStatus = function() {
    if (this.xhttp) {
      return this.xhttp.status;
    }
    return "";
  }
  
  /**
   * 取得相应文本
   */
  this.getResponseTxt = function() {
    if (this.isReturnOk()) {
      return this.xhttp.responseText;
    }
    return "";
  }
  
  /**
   * 取得相应XML
   */
  this.getResponseXml = function() {
    if (this.isReturnOk()) {
      return this.xhttp.responseXml;
    }
    return "";
  }
  
  /**
   * 取得相应XML
   */
  this.getJSON = function() {
    if (this.isReturnOk()) {
      eval("var rtObj = " + this.xhttp.responseText);
      return rtObj;
    }
    return undefined;
  }
}

/**
 * 时间记录类，用于性能调优
 */
function TimeLogger() {
  // 时间记录列表
  this.timeArray = new Array();
  // 消息列表
  this.msrgArray = new Array();
  
  /**
   * 记录信息
   */
  this.log = function(msrg) {
    this.timeArray.add((new Date()).getTime());    
    if (msrg) {
      this.msrgArray.add(msrg);
    }else {
      this.msrgArray.add("no log");
    }
  }
  
  /**
   * 取得信息
   */
  this.getLogTime = function(index) {
    return this.timeArray.get(index);
  }
  
  /**
   * 取得消息
   */
  this.getMsrg = function(index) {
    return this.msrgArray.get(index);
  }
  
  /**
   * 取得消息之间的毫秒间隔


   */
  this.getInterval = function(indexFrom, indexTo) {
    return this.timeArray.get(indexTo) - this.timeArray.get(indexFrom);
  }
  
  /**
   * 转换为字符串
   */
  this.toString = function() {
    var rtStr = "";
    
    if (this.timeArray.size() < 2) {
      return rtStr;
    }
    rtStr += "[total]=" + this.getInterval(0, this.timeArray.size() - 1) + ";";
    
    for (var i = 1; i < this.timeArray.size(); i++) {
      rtStr += "[" + this.getMsrg(i - 1) + "-" + this.getMsrg(i) + "]" + "=" + this.getInterval(i - 1, i) + ";";
    }
    return rtStr;
  }
}

/**
 * 构造XMLDOM对象
 */
function createXmlDom() {
  // 通过对象/属性检测法，判断是IE来是Mozilla
  if (window.ActiveXObject) {
    var arrSignatures = ["MSXML2.DOMDocument.5.0", "MSXML2.DOMDocument.4.0",
                         "MSXML2.DOMDocument.3.0", "MSXML2.DOMDocument",
                         "Microsoft.XmlDom"];
                     
    for (var i=0; i < arrSignatures.length; i++) {
      try {
  
        var oXmlDom = new ActiveXObject(arrSignatures[i]);        
        return oXmlDom;  
      } catch (oError) {
          // ignore
      }
    }          

    throw new Error("MSXML is not installed on your system."); 
         
  // 同上
  } else if (document.implementation && document.implementation.createDocument) {
      
    var oXmlDom = document.implementation.createDocument("","",null);

    // 创建Mozilla版本的parseError对象
    oXmlDom.parseError = {
        valueOf: function () { return this.errorCode; },
        toString: function () { return this.errorCode.toString() }
    };
    
    // 初始化parseError对象
    oXmlDom.__initError__();
            
    
    oXmlDom.addEventListener("load", function () {
        this.__checkForErrors__();
        this.__changeReadyState__(4);
    }, false);

    return oXmlDom;        
      
  } else {
    throw new Error("Your browser doesn't support an XML DOM object.");
  }
}

/**
 * 取得滚动的上坐标
 * 
 * @obj 触发事件对象
 */
function getLockTop(obj) {
  return obj["container"].scrollTop;
}
/**
 * 取得滚动的左坐标
 * 
 * @obj 触发事件对象
 */
function getLockLeft(obj) {
  return obj["container"].scrollLeft;
}

/**
 * 取得滚动的上坐标
 * 
 * @container 容器
 */
function getScrollTop(container) {
  if (!container) {
    container = window.dataContainer;
  }
  return container.scrollTop;
}

/**
 * 取得滚动的左坐标
 * 
 * @container 容器
 */
function getScrollLeft(container) {
  if (!container) {
    container = window.dataContainer;
  }
  return container.scrollLeft;
}

/**
 * 处理滚动条


 */
function processDataTable() {
  var dataListaTable = document.getElementById("dataListaTable");

  if (dataListaTable && dataContainer) {
    var tableObj = dataContainer.removeChild(dataListaTable);
    if (window.reportTable) {
      reportTable.display(dataContainer);
    }else {
      dataContainer.appendChild(tableObj);
    }
  }
}
/**
 * 锁定表头
 * 
 * @tableObj 数据表


 * @container DIV
 * @rowIndex 需要锁定最大的行坐标，从0开始计数


 * @colIndex 需要锁定最大的列坐标，从0开始计数


 */
function lockCell(tableObj, container, rowIndex, colIndex) {
  if (rowIndex == undefined || rowIndex == null || rowIndex < 0) {
    return;
  }
  if (!container) {
    container = dataContainer;
  }
  for (var i = 0; i <= rowIndex; i++) {
    var tr = tableObj.rows[i];
    tr.className = "lock_row";
    tr["container"] = container;
  }
  if (colIndex == undefined || colIndex == null || colIndex < 0) {
    return;
  }
  var rowCnt = tableObj.rows.length;
  for (var i = 0; i < rowCnt; i++) {
    var tr = tableObj.rows[i];
    for (var j = 0; j <= colIndex; j++) {
    var td = tr.cells[j];
    td.className = "lock_col";
    td["container"] = container;
    }
  }
}

var loopCnt = 0;
/**
 * 把符合正则表达式的单词分离成数组
 * 
 * @str 数据库


 * @reg 正则表达式


 */
function getMatchedWordArray(str, reg, regIndex) {
  if (regIndex == undefined) {
    regIndex = 0;
  }
  var rtArray = [];
  var regObj = null;
  while ((regObj = reg.exec(str)) != null) {
    loopCnt++;
    if (loopCnt > 100000) {
      alert("exists dead loop");
      return [];
    }
    rtArray.push(regObj[regIndex]);
  }
  return rtArray;
}

/**
 * 取得递归调用函数
 * 
 * @arguments[0] 函数引用
 * @arguments[1] 函数执行的上下文关联对象
 * @arguments[>1] 函数执行时传入的参数
 */
function bindFunc() {
  var args = [];
  for (var i = 0, cnt = arguments.length; i < cnt; i++) {
    args[i] = arguments[i];
  }
  var __method = args.shift();
  var object = args.shift();
  return (
    function(){
      var argsInner = [];
      for (var i = 0, cnt = arguments.length; i < cnt; i++) {
        argsInner[i] = arguments[i];
      }
      return __method.apply(object, args.concat(argsInner));
    });
}

/**
 * 从度量字符串中解析数值


 * 
 * @numStr 例如：100px
 */
function parseNum(numStr) {
  if (!numStr) {
    return 0;
  }
  var reg = /\d+/;
  var num = reg.exec(numStr);
  return parseInt(num[0]);
}

/**
 * 窗口用工具方法


 */
var windows = {  
  util: {
    currModalWin: null,
    currFrameSet: null,
    /**
     * 创建一个屏蔽


     */
    createShim: function(codePopWindow, shimDoc, shimContainer) {
      if (!codePopWindow) {
        // return null;
      }
        if (!shimDoc) {
        shimDoc = document;
      }
      if (!shimContainer) {
        shimContainer = shimDoc.body;
      }

      /**
       * var shim = shimDoc.createElement("iframe scrolling='no' frameborder='0'" + "
       * src='" + contextPath + "/core/inc/emptyshim.html'" + "
       * style='position:absolute;filter:alpha(opacity=40);" + "display:none'");
       */
      var shim = shimDoc.createElement("iframe");
      shim.scrolling = "no";
      shim.frameborder = "0"; 
      shim.src = contextPath + "/core/inc/emptyshim.html";
      shim.style.position = "absolute";
      shim.style.filter = "alpha(opacity=40)";
      shim.style.opacity = 0.4;
      shim.style.display = "none";
      shim.name = windows.util.getShimId(codePopWindow);
      shim.id = windows.util.getShimId(codePopWindow);
      shimContainer.appendChild(shim); 
    
      return shim;
    },
    /**
     * 取得屏蔽窗口的ID
     */
    getShimId: function(codePopWindow) {
      if (!codePopWindow || !codePopWindow.id) {
        return "__shim";
      }
      return "__shim" + codePopWindow.id;
    },
    /**
     * 取得屏蔽窗口
     */
    getShim: function(codePopWindow, shimDoc) {
      if (!shimDoc) {
      shimDoc = document;
    }
      return shimDoc.getElementById(windows.util.getShimId(codePopWindow));
    },
    /**
     * 打开窗口的垫板，解决遮盖Select窗口控件的问题


     * 
     * @author YZQ
     * @date 2006-09-24
     */
    openWindowShim: function(windowObj, isShimWindowOnly, shimDoc, shimContainer) {
      if (!shimDoc) {
        shimDoc = document;
      }
      var shim = windows.util.getShim(windowObj, shimDoc);
      if (!shim) {
        shim = windows.util.createShim(windowObj, shimDoc, shimContainer);
      }
      if (!shimContainer) {
        shimContainer = shimDoc.body;
      }
      if (!isShimWindowOnly) {
        shim.style.left = 0;
        shim.style.top = 0;
  // shim.style.setExpression("width", shimContainer.clientWidth);
  // shim.style.setExpression("height", shimContainer.clientHeight);
      // shim.style.setExpression("width", Ext.lib.Dom.getViewportWidth());
  // shim.style.setExpression("height", Ext.lib.Dom.getViewportHeight());
        var shimWidth = Ext.lib.Dom.getViewportWidth();
        var shimHeight = Ext.lib.Dom.getViewportHeight();
        if (!Ext.isIE) {
          shimWidth = shimWidth - 25;
          shimHeight = shimHeight - 10;
        }
        shim.width = shimWidth;
        shim.height = shimHeight;
      }else {
        var top = windowObj.style.top;
        var left = windowObj.style.left;
        var region = Ext.lib.Dom.getRegion(windowObj);
        // var shimWidth = windowObj.offsetWidth ? windowObj.offsetWidth :
        // windowObj.width;
        // var shimHeight = windowObj.offsetHeight ? windowObj.offsetHeight :
        // windowObj.height;
        var shimWidth = region.right - region.left - 5;
        var shimHeight = region.bottom - region.top - 5;
        // alert("shimWidth" + shimWidth);
        // alert("shimHeight" + shimHeight);
        shim.width = shimWidth;
        shim.height = shimHeight;
        shim.style.top = top;
        shim.style.left = left;
      }
      shim.style.zIndex = 100;
      shim.style.backgroundColor = "#BDE2FF";
      shim.style.position = "absolute";
      shim.style.display = "block";
    },
    /**
     * 取得鼠标相对所在窗口左上顶点的位置坐标
     * 
     * @author YZQ
     * @date 2006-09-14
     */
    getMouseCoords: function(ev){
      if(ev.pageX || ev.pageY){
        return {x:ev.pageX, y:ev.pageY};
      }
      return {
        x: (ev.clientX + document.body.scrollLeft - document.body.clientLeft),
        y: (ev.clientY + document.body.scrollTop  - document.body.clientTop)
      };
    },
    /**
     * 取得目标对象相对所在窗口左上顶点是坐标原点的位置坐标


     * 
     * @author YZQ
     * @date 2006-09-14
     */
    getPosition: function(trget){
      var left = 0;
      var top  = 0;
      
      while (trget.offsetParent){
      left += trget.offsetLeft;
      top += trget.offsetTop;
      trget = trget.offsetParent;
      }
      
      left += trget.offsetLeft;
      top += trget.offsetTop;
      
      return {
        x:left, y:top
      };
    },
    /**
     * 取得鼠标相对所指向的对象的偏移位置
     * 
     * @author YZQ
     * @date 2006-09-14
     */
    getMouseOffset: function(target, ev){
      ev = ev || window.event;
      
      var docPos = windows.util.getPosition(target);
      var mousePos = windows.util.getMouseCoords(ev);
      return {
        x: (mousePos.x - docPos.x),
        y: (mousePos.y - docPos.y)
      };
    },
    findOppener: function() {
      var topFameWindow = null;
      try {
        if (window.frameElement && window.frameElement["isInSelfSubWindow"]) {
          topFameWindow = window;
        }
        if (!topFameWindow) {
          topFameWindow = window.frameElement.topFameWindow;
        }
        if (!topFameWindow) {
          topFameWindow = window.parent.window.frameElement.topFameWindow;
        }    
      }catch(e) {
      }
      return topFameWindow ? topFameWindow : window;
    },
    findTopFameDocument: function() {
      return windows.util.findOppener().document;
    },
    /**
     * 在本窗口打开莫太窗口
     */
    createModalInCurrWin: function(url, winTitle, width, height, id) {
        var modalWindow = new TDJsSubWindow();
      modalWindow.setConfig("isShimAll", true);
      modalWindow.setConfig("actionPath", url);
      modalWindow.setConfig("titleText", winTitle);
      modalWindow.setConfig("width", width ? width : 400);
      modalWindow.setConfig("height", height ? height : 300);
      modalWindow.setConfig("id", id ? id : 0);
      // viewPortArear是dom对象，不是参数


      var viewPortArear = document.getElementById("viewPortArear");
      if (viewPortArear) {
        modalWindow.setConfig("container", viewPortArear);
      }
      modalWindow.registAfterClose(windows.util._handleCloseModal);
      windows.util.currModalWin = modalWindow;
      return modalWindow;
    },
    /**
     * 交换最后两个Frame的显示空间


     */
    swapShimState: function() {
      var toolBarArea = this.all("toolBarArea");
      var queryArea = this.all("queryArea");
      
      var shimDoc = toolBarArea.document
      var shim = windows.util.getShim(null, shimDoc);
      if (!shim || shim.style.display != "block") {
        windows.util.openWindowShim(null, false, shimDoc);
      }else {
        shim.style.display = "none"
      }
      
      shimDoc = queryArea.document
      shim = windows.util.getShim(null, shimDoc);
      if (shim) {
        alert(shim.style.display);
      }
      windows.util.openWindowShim(null, false, shimDoc);
      if (!shim) {
        windows.util.openWindowShim(null, false, shimDoc);
      }else {
        shim.style.display = "none"
      }
    },
    /**
     * 打开模态窗口


     */
    openModal: function(url, winTitle, width, height) {  
      var topFrameWindow = windows.util.findOppener();
      var currModal = null;
      if (topFrameWindow.winManager) {  
        currModal = topFrameWindow.winManager.showModal(url, winTitle, width, height);
      }  
      if (!currModal) {
        topFrameWindow = parent.windows.util.findOppener();
        if (topFrameWindow.winManager) {
          currModal = topFrameWindow.winManager.showModal(url, winTitle, width, height);
        }
      }
          if (!currModal) {
        currModal = topFrameWindow.windows.util.createModalInCurrWin(url, winTitle, width, height, 0)
        currModal.show();
      }
      currModal.contentFrame["opennerWindow"] = window;
    },
    /**
     * 处理窗口关闭事件
     */
    _handleCloseModal: function() {
      windows.util.currModalWin = null;
    },
    
    /**
     * 关闭模态窗口


     */
    closeModal: function() {
      var currWindow = window.frameElement["currWindow"];
      if (currWindow) {
        try {
          if (currWindow.winManager) {
            currWindow.winManager.activeWindow.closeCurrModal();
          }else if (currWindow.windows.util.currModalWin) {
            currWindow.windows.util.currModalWin.close();
          }
        }catch(e) {      
        }    
      }
    },
    /**
     * 取得打开窗口
     */
    getOpennerWindow: function(windowObj) {
      if (!windowObj) {
        windowObj = window;
      }
      var openerWindow = getOpenerModal(windowObj);
      if (openerWindow) {
        return openerWindow;
      }
      openerWindow = windowObj.opener;
      if (openerWindow) {
        return openerWindow;
      }
      if (window.frameElement) {
        return window.frameElement["opennerWindow"];
      }
    }
  }
}

/**
 * 显示调试信息
 */
function showLog(str) {
  var divWindow = document.createElement("<div id=\"logDivWindow\" style=\"position:absolute;top:10px;left:10px;width:500px;height:300px;overflow:auto;z-index:10000;border:1px solid blue;background-color:#BBBBBB;\" \>");
  divWindow.innerHTML = str;
  divWindow.ondblclick = function(){divWindow.removeNode(true);};
  document.body.appendChild(divWindow);
}

/**
 * 菜单项目
 */
function TDJsMenuItem(itemName, action, iconChecked, iconUnChecked) {
  this.menu = null;
  this.itemName = null;
  this.action = null;
  this.actionIndex = null;
  // 左侧图片
  this.icon = null;
  // 悬浮图片
  this.iconOver = null;
  this.isChecked = undefined;
  this.iconChecked = null;  
  this.iconUnChecked = null;
  this.iconCheckedDomObj = null;  
  this.iconUnCheckedDomObj = null; 
  this.itemList = [];
  this.listeners = [];
  this.domNode = null;
  /**
   * 构造函数


   */
  this.initialize = function() {
    this.itemName = itemName;
    this.action = action;
    this.iconChecked = iconChecked;
    this.iconUnChecked = iconUnChecked;
  }
  
  /**
   * 处理鼠标悬停事件
   */
  this._handleMouseEnter = function() {
    this.domNode.className = "x-menu-item-over";
  }
  /**
   * 处理鼠标离开事件
   */
  this._handleMouseLeave = function() {
    this.initState();
  }
  /**
   * 菜单关闭，菜单项目状态复原


   */
  this.initState = function() {
    this.domNode.className = "x-menu-item";
  }
  /**
   * 处理鼠标单击事件
   */
  this._handleMouseClick = function() {
    if (this.action) {
    this.action(this.actionIndex);
    this.menu.hide();
    }
  }
  
  /**
   * 创建Dom节点
   */
  this.createDomNode = function() {
    // var itemDomObj = document.createElement("<div
    // style=\"height:21px;cursor:pointer;\"/>");
    var itemDomObj = document.createElement("div");
    itemDomObj.style.height = "21px";
    itemDomObj.style.cursor = "hand";
    this.domNode = itemDomObj;
    this.domNode["itemObj"] = this;

    if (this.iconChecked && this.iconUnChecked) {
      this.isChecked = false;
      this.setChecked(this.isChecked);     
    }
    itemDomObj = $(itemDomObj);
    itemDomObj.appendChild(document.createTextNode(" " + this.itemName));
    itemDomObj.observe("mouseover", this._handleMouseEnter.bindAsEventListener(this));
    itemDomObj.observe("mouseout", this._handleMouseLeave.bindAsEventListener(this));
    itemDomObj.observe("click", this._handleMouseClick.bindAsEventListener(this));
    if (!this.action) {
      itemDomObj.style.color = "#8F8F8F";
    }
        
    return itemDomObj;
  }
  /**
   * 设置选择状态


   */
  this.setChecked = function(isChecked) {
    if (this.isChecked === undefined) {
      return;
    }
    if (!this.iconCheckedDomObj) {
      // this.iconCheckedDomObj = document.createElement("<img src=\"" +
      // this.iconChecked + "\" style=\"vertical-align:middle\">");
      this.iconCheckedDomObj = document.createElement("img");
      this.iconCheckedDomObj.src = this.iconChecked;
      this.iconCheckedDomObj.verticalAlign = "middle"
    }
    if (!this.iconUnCheckedDomObj) {
      // this.iconUnCheckedDomObj = document.createElement("<img src=\"" +
      // this.iconUnChecked + "\" style=\"vertical-align:middle\">");
      this.iconUnCheckedDomObj = document.createElement("img");
      this.iconUnCheckedDomObj.src = this.iconUnChecked;
      this.iconUnCheckedDomObj.verticalAlign = "middle";
    }
    this.isChecked = isChecked;
    var iconObj = this.isChecked ? this.iconCheckedDomObj : this.iconUnCheckedDomObj;
    var childNodes = this.domNode.childNodes;
    if (childNodes.length < 1) {
      this.domNode.appendChild(iconObj);
    }else if (childNodes.length == 1) {
      this.domNode.insertBefore(iconObj, childNodes.item(0));
    }else if (childNodes.length == 2) {
      this.domNode.removeChild(childNodes.item(0));
      this.domNode.insertBefore(iconObj, childNodes.item(0));
    }
  }
  /**
   * 设置粗体字


   */
  this.setTextDecoration = function(isActive) {
    if (isActive) {
      this.domNode.style.textDecoration = "underline";
      this.domNode.style.fontWeight = "bolder";
    }else {
      this.domNode.style.textDecoration = "none";
      this.domNode.style.fontWeight = "normal";
    }
  }
  /**
   * 释放资源
   */
  this._dispose = function() {
  this.menu = null;
  this.id = null;
  this.itemName = null;
  this.action = null;
  this.icon = null;
  this.iconOver = null;
  this.isChecked = false;
  this.iconChecked = null;  
  this.iconUnChecked = null;
  this.iconCheckedDomObj = null;  
  this.iconUnCheckedDomObj = null;
  this.itemList = null;
  this.listeners = null;
  this.domNode = null;
  }
  /**
   * 关闭项目
   */
  this.close = function() {
    this._dispose();
  }
  
  this.initialize();
};

/**
 * 菜单
 */
function TDJsMenu(width, height, container) {
  this.container = null;
  this.width = 0;
  this.height = 0;
  this.isDisp = false;
  this.domNode = null;
  this.mouseEntered = false;
  this.autoHideTimeId = null;
  this.onShowBefore = null;
  this.onHideAfter = null;
  this.shimFrame = null;
  this.shimMenuOnly = false;
  /**
   * 构造方法


   * 
   * @width 菜单宽度
   * @height 菜单高度
   */
  this.initialize = function() {
    this.width = width;
    this.height = height;    
    this.container = container ? container : document.body;
    this.createDomNode();
  }  
  /**
   * 如果较长时间用户没有选择菜单，菜单自动关闭


   */
  this._autoHide = function() {
    if (this.autoHideTimeId) {
      clearTimeout(this.autoHideTimeId);
    }
    if (this.mouseEntered) {
      return;
    }
    this.hide();
  }
  /**
   * 处理鼠标进入事件
   */
  this._handleMouseEnter = function() {
    this.mouseEntered = true;
  }
  /**
   * 处理鼠标离开事件
   */
  this._handleMouseLeave = function() {
    this.hide();
  }
  /**
   * 创建结点
   */
  this.createDomNode = function() {
    var dimension = "position:absolute;"
    + "width:" + this.width + "px;"
    + "height:" + this.height + "px;"
    + "overflow-y:auto;overflow-x:hidden;";
  var nodeId = createRandomId("popMenuWindow_");
  // var nodeText = "<div id=\"" + nodeId + "\" class=\"x-menu\" style=\"" +
  // dimension + "\">";
  // alert(nodeText);
  this.domNode = document.createElement("div");
  this.domNode.id = nodeId;
  this.domNode.className = "x-menu";
  this.domNode.style.position = "absolute";
  this.domNode.style.width = this.width + "px";
  this.domNode.style.height = this.height + "px";
  this.domNode.style.overflowY = "auto";
  this.domNode.style.overflowX = "hidden";
  this.domNode.onmouseleave = this._handleMouseLeave.bind(this);
  this.domNode.onmouseenter = this._handleMouseEnter.bind(this);
  
  this.shimFrame = windows.util.getShim(this.domNode, document);
  if (!this.shimFrame) {
    this.shimFrame = windows.util.createShim(this.domNode, document, this.container);
  }
  }
  /**
   * 添加菜单项目
   */
  this.addItem = function(item) {
    item.menu = this;
    if (item instanceof  Array) {
      for (var itemObj in item) {
        this.domNode.appendChild(itemObj.createDomNode());
      }
    }else {
      this.domNode.appendChild(item.createDomNode());
    }
  }
  /**
   * 删除菜单项目
   */
  this.removeItem = function(item) {
    if (!item) {
      return;
    }
    this.domNode.removeChild(item.domNode);
    item.close();
  }
  /**
   * 显示
   */
  this.show = function(event) {
    if (this.isDisp) {
      return;
    }
    this.isDisp = true;
    
    // 处理菜单项目状态


    var childItems = this.domNode.childNodes;
    for (var i = 0, cnt = childItems.length; i < cnt; i++) {
      childItems[i].itemObj.initState();
    }
    var event = arguments[0] || window.event;
    var srcElem = Event.element(event);
    var elemRegion = D.getRegion(srcElem);
    // alert(pos[0] + "\t" + pos[1]);
    // 显示菜单
    /**
     * var tmpInt = event.clientX + this.width; var left = event.clientX; if
     * (tmpInt > this.container.clientWidth) { left = event.clientX -
     * this.width; } if (left < 5) { left = 5; } this.domNode.style.left = left +
     * "px"; this.domNode.style.top = (event.clientY + 3) + "px";
     */
    var tmpInt = elemRegion.left + this.width;
    var left = elemRegion.left;
    if (tmpInt > this.container.clientWidth) {
      left = elemRegion.left - this.width;
    }
    if (left < 5) {
      left = 5;
    }
    this.domNode.style.left = left + "px";
    this.domNode.style.top = elemRegion.bottom + "px";
    
    if (this.onShowBefore) {
      this.onShowBefore();
    }
    
    this.container.appendChild(this.domNode);
    this.container.appendChild(this.shimFrame);
    this.domNode.style.display = "block";
    windows.util.openWindowShim(this.domNode, this.shimMenuOnly, document, this.container);
    
    // 设置鼠标进入状态，自动关闭使用
    this.mouseEntered = false;
    this.autoHideTimeId = setTimeout(this._autoHide.bind(this), 2000);
  }
  /**
   * 隐藏
   */
  this.hide = function() {
    if (!this.isDisp) {
      return;
    }
    this.isDisp = false;
    if (this.domNode) {
      windows.util.getShim(this.domNode).style.display = "none";
      this.domNode.style.display = "none";
      this.container.removeChild(this.domNode);
      this.container.removeChild(this.shimFrame);
      
      if (this.onHideAfter) {
      this.onHideAfter();
    }
    }
  }
  
  this.initialize();
}

/**
 * 弹出子窗口


 */
function TDJsPopWindow(width, height, container) {
  this.container = null;
  this.width = 0;
  this.height = 0;
  this.isDisp = false;
  this.domNode = null;
  this.mouseEntered = false;
  this.autoHideTimeId = null;
  this.onShowBefore = null;
  this.onHideAfter = null;
  this.shimFrame = null;
  this.shimWindowOnly = true;
  this.content = null;
  /**
   * 构造方法


   * 
   * @width 菜单宽度
   * @height 菜单高度
   */
  this.initialize = function() {
    this.width = width ? width : 300;
    this.height = height ? height : 30;    
    this.container = container ? container : document.body;
    this.createDomNode();
  }  
  /**
   * 如果较长时间用户没有选择菜单，菜单自动关闭


   */
  this._autoHide = function() {
    if (this.autoHideTimeId) {
      clearTimeout(this.autoHideTimeId);
    }
    if (this.mouseEntered) {
      return;
    }
    this.hide();
  }
  /**
   * 处理鼠标进入事件
   */
  this._handleMouseEnter = function() {
    this.mouseEntered = true;
  }
  /**
   * 处理鼠标离开事件
   */
  this._handleMouseLeave = function() {
    this.hide();
  }
  /**
   * 创建结点
   */
  this.createDomNode = function() {
    var dimension = "position:absolute;"
    + "width:" + this.width + "px;"
    + "height:" + this.height + "px;"
    + "overflow:auto;";
  var nodeId = createRandomId("popMessageWindow_");
  // this.domNode = document.createElement("<div id=\"" + nodeId + "\"
  // class=\"x-window\" style=\"" + dimension + "\">");
  this.domNode = document.createElement("div");
  this.domNode.id = nodeId;
  this.domNode.className = "x-window";
  this.domNode.style.position = "absolute";
  this.domNode.style.width = this.width + "px";
  this.domNode.style.height = this.height + "px";
  this.domNode.style.overflow = "auto";
  this.domNode.onmouseleave = this._handleMouseLeave.bind(this);
  this.domNode.onmouseenter = this._handleMouseEnter.bind(this);
  
  this.shimFrame = windows.util.getShim(this.domNode, document);
  if (!this.shimFrame) {
    this.shimFrame = windows.util.createShim(this.domNode, document, this.container);
  }
  }
  /**
   * 添加菜单项目
   */
  this.setContent = function(content) {
    if (!content) {
      return;
    }
    this.content = content;
    if (typeof content == "string") {
      this.domNode.innerHTML = content;
    }else {
      if (this.domNode.childNodes.length > 0) {
        this.domNode.removeChild(this.domNode.childNodes[0]);
      }
      try {
        this.domNode.append(content.createDomNode());
      }catch(e) {
      }
    }
  }
  /**
   * 显示
   */
  this.show = function(content, width, height) {
    if (this.isDisp) {
      if (typeof content == "string" && this.content == content) {        
        return;
      }else if (typeof content == "object") {
        return;
      }
      this.hide();
    }
    this.isDisp = true;
    
    this.width = width && typeof width == "number" ? width : 300;
    this.height = height && typeof height == "number" ? height : 30;
    this.domNode.style.width = this.width + "px";
    this.domNode.style.height = this.height + "px";
    this.setContent(content);
    // 显示菜单
    var tmpInt = event.clientX + this.width;
    var left = event.clientX;
    if (tmpInt > this.container.clientWidth) {
      left = event.clientX - this.width;
    }
    if (left < 5) {
      left = 5;
    }
    this.domNode.style.left = left + "px";
    this.domNode.style.top = (event.clientY + 3) + "px";    
    
    if (this.onShowBefore) {
      this.onShowBefore();
    }
    
    this.container.appendChild(this.domNode);
    this.container.appendChild(this.shimFrame);
    this.domNode.style.display = "block";
    windows.util.openWindowShim(this.domNode, this.shimWindowOnly, document, this.container);
    
    // 设置鼠标进入状态，自动关闭使用
    this.mouseEntered = false;
    this.autoHideTimeId = setTimeout(this._autoHide.bind(this), 2000);
  }
  /**
   * 隐藏
   */
  this.hide = function() {
    if (!this.isDisp) {
      return;
    }
    this.isDisp = false;
    if (this.domNode) {
      windows.util.getShim(this.domNode).style.display = "none";
      this.domNode.style.display = "none";
      this.container.removeChild(this.domNode);
      this.container.removeChild(this.shimFrame);
      
      if (this.onHideAfter) {
      this.onHideAfter();
    }
    }
  }
  /**
   * 绑定到控件


   * 
   * @cntrl 控件
   * @showEvent 显示事件
   */
  this.bind2Cntrl = function(cntrl, content, width, height, showEvent) {
    cntrl = $(cntrl);
    if (!showEvent) {
      showEvent = "onmouseenter";
    }
    cntrl.observe(showEvent, this.show.bindAsEventListener(this, content, width, height));
  }
  
  this.initialize();
};

/**
 * 弹出菜单管理器


 */
function TDJsPopMenuManager() {
  /**
   * 菜单列表
   */
  this.menuMap = new ArrayHashMap();
  
  /**
   * 新增加菜单


   * 
   * @menuName 菜单名称
   * @menuItemList 菜单属性列表，结构[[actionName, actionHandler, iconChecked,
   *               iconUnChecked], ...]
   */
  this.addMenu = function(menuName, menuItemList, width, height) {
    if (!menuName || !menuItemList) {
      return;
    }
    if (!width) {
      width = 150;
    }
    if (!height) {
      height = 200;
    }
    var menuObj = new TDJsMenu(width, height);
    menuObj.shimMenuOnly = true;
    this.menuMap.put(menuName, menuObj);
    for (var i = 0, cnt = menuItemList.size(); i < cnt; i++) {
      var itemDef = menuItemList.get(i);
      var itemName = itemDef[0];
      var itemAction = itemDef[1];
      var iconChecked = itemDef[2] ? itemDef[2] : imgPath + "/views/empty.gif";
      var iconUnChecked = itemDef[3] ? itemDef[3] : imgPath + "/views/empty.gif";
      var menuItem = new TDJsMenuItem(itemName, itemAction, iconChecked, iconUnChecked);
      menuItem.actionIndex = i;
      menuObj.addItem(menuItem);
    }
  }
  /**
   * 弹出菜单绑定到控件


   * 
   * @cntrl 控件名称，必须传递


   * @menuName 菜单名称，可选，如果没传递则是第一个菜单


   * @eventName 绑定的事件，可选，如果没传递，则显示是onmouseenter
   */
  this.bindMenuToCntrl = function(cntrl, menuName, showEvent) {
    if (!showEvent) {
      showEvent = "click";
    }
    cntrl = $(cntrl);
    cntrl.observe(showEvent, this.showMenu.bindAsEventListener(this, menuName));
  }
  /**
   * 查找菜单对象
   * 
   * @menuName 菜单名称
   */
  this._findMenu = function(menuName) {
    if (this.menuMap.size() < 1) {
      return null;
    }
    var menu = null;
    if (!menuName) {
      menu = this.menuMap.getValue(0);
    }else {
      menu = this.menuMap.get(menuName);
    }
    return menu;
  }
  /**
   * 显示菜单
   * 
   * @menuName 菜单名称
   */
  this.showMenu = function() {
  var eventObj = arguments[0];
  var menuName = arguments[1];
    var menu = this._findMenu(menuName);
    if (!menu) {
      return;
    }
    this.currMenu = menu;
    menu.show(eventObj);
  }
}
/**
 * 自使用高度和宽度
 * 
 * @param cntrl
 * @param container
 * @return
 */
function autoFit(cntrl, container, diffWidth, diffHeight) {
  this.cntrl = $(cntrl);
  if (!container) {
    container = document.body;
  }
  this.container = $(container);
  this.diffWidth = diffWidth ? diffWidth : 0;
  this.diffHeight = diffHeight ? diffHeight : 0;
  /**
   * 取得高度和宽度


   */
  this.getWH = function() {
    var width = 0;
    var height = 0;
    if (container.tagName.toLowerCase() == "body") {
      width = Ext.lib.Dom.getViewportWidth();
      height = Ext.lib.Dom.getViewportHeight();
    }else {
      var region = Ext.lib.Region.getRegion(container);
      width = region.right - region.left;
      height = region.bottom - region.top;
    }
    // alert(width + "\t" + height);
    width = width - this.diffWidth;
    height = height - this.diffHeight;
    return {width: width, height: height};
  }
  this.handleResize = function() {
    var wh = this.getWH();
    if (this.cntrl.tagName.toLowerCase() == "iframe") {
      // alert("iframe");
      this.cntrl.width = wh.width;
      this.cntrl.height = wh.height;
    }else {
      this.cntrl.style.width = wh.width + "px";
      this.cntrl.style.height = wh.height + "px";      
    }
  }
  this.handleResize();
  window.onresize = this.handleResize.bind(this);
}
/**
 * 把Json数据绑定到控件


 */
function bindJson2Cntrl(json, filters) {
  for (var property in json) {
    if (filters) {
      if (Object.isString(filters) && filters.indexOf(",") > 0) {
        var filterArray = filters.split(",");
        if (!filterArray.contains(property)) {
          continue;
        }
      }else if (Object.isString(filters)) {
        var ancestor = $(filters);
        var elem = $(property);
        if (ancestor && elem && !Element.descendantOf(elem, ancestor)) {
          continue;
        }
      }else if (Object.isArray(filters)) {
        if (!filters.contains(property)) {
          continue;
        }
      }else if (Object.isElement(filters)) {
        var elem = $(property);
        if (elem && !Element.descendantOf(elem, ancestor)) {
          continue;
        }
      }
    }
    var value = json[property];  
    var cntrlArray = document.getElementsByName(property);    
    var cntrlCnt = cntrlArray.length;
    if (!cntrlArray || cntrlCnt < 1) {
      if (document.getElementById(property)) {
        cntrlArray = [document.getElementById(property)];
        cntrlCnt = 1;
      }else {
        continue;
      }
    }
    if (cntrlCnt == 1) {
      var cntrl = cntrlArray[0];
      if (cntrl.tagName.toLowerCase() == "input" && cntrl.type.toLowerCase() == "checkbox") {
        if (cntrl.value == value) {
          cntrl.checked = true;
        }else {
          cntrl.checked = false;
        }
      }else if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML = value;
      } else if (cntrl.tagName.toLowerCase() == 'select') {
        for (var i = 0; i < cntrl.childNodes.length; i++) {
          if (cntrl.childNodes[i].value == value) {
            cntrl.childNodes[i].setAttribute("selected", "selected");
            break;
          }
        }
      }else {
        cntrl.value = value;
      }
    }else {
      for (var i = 0; i < cntrlCnt; i++) {
        var cntrl = cntrlArray[i];
        if (cntrl.value == value) {
          cntrl.checked = true;
        }else {
          cntrl.checked = false;
        }
      }
    }
  }
}
/**
 * 把ID转换成名称


 * 
 * queryParam [{cntrlId, desDef}, ...]
 */
function bindDesc(paramArray) {
  var queryParam = "";
  for (var i = 0; i < paramArray.length; i++) {
    var param = paramArray[i];
    var cntrl = $(param.cntrlId);
    if (!cntrl || !cntrl.value) {
      continue;
    }
    var cntrlDesc = $(param.cntrlId + "Desc");

    if (!cntrlDesc) {
      continue;
    }
    if (queryParam.length > 0) {
      queryParam += "/";
    }
    queryParam += cntrl.value + ";" + param.dsDef;
  }
  if (!queryParam) {
    //alert("没有输入有效的代码转换名称参数！");
    return;
  }
  var rtJson = getJsonRs(contextPath + "/yh/core/act/YHCode2DescAct/code2Desc.act", "queryParam=" + queryParam);
  if(!rtJson || rtJson.rtState == "1"){
    return;
  }
  var codeData = rtJson.rtData;
  var dataIndex = 0;
  for (var i = 0; i < paramArray.length; i++) {
    var param = paramArray[i];
    var cntrl = $(param.cntrlId);
    if (!cntrl || !cntrl.value) {
      continue;
    }
    cntrl = $(param.cntrlId + "Desc");
    if (!cntrl) {
      continue;
    }
    var cntrlData = codeData[dataIndex++];
     // 2010.1.19 解决没有value属性的控件加载问题
     if (cntrl.tagName.toLowerCase() == "td"
       || cntrl.tagName.toLowerCase() == "div"
       || cntrl.tagName.toLowerCase() == "span") {

      cntrl.innerHTML =  $H(cntrlData).values().toString();
    }else{
      cntrl.value = $H(cntrlData).values().toString();
    }
  }
}

/**
 * 打开Ext窗口
 * @param actionUrl
 * @param title
 * @param width
 * @param height
 * @param isMaxSize 是否可以最大化
 * @param hideEvent 当点击关闭按钮时触发事件
 * @return
 */

function showWindow(actionUrl, title, width, height, isMaxSize, animate, callback){
  window.onunload = function(){
    hideWindow();
  };

  hideWindow();

  if(!callback) {
    callback = function(){};
  }
  if(!top.win){
    top.win = new top.YH.Window({
      src: actionUrl,
      closeAction: 'close',
      title: title,
      width: width || 800,
      height: height || 600,
      listeners:{ 
        'close': function(t){
          callback();
          top.win = null;
        }
      }
    });
  }
  
  top.win.show();
}

/**
 * 隐藏窗口
 * @return
 */
function hideWindow(){
  if (top.win){
    top.win.close();
  }
}

/**
 * 显示模态窗口

 * @param actionUrl
 * @param title
 * @param width
 * @param height
 * @param isMaxSize
 * @param callback
 * @return
 */
function showModalWindow(actionUrl, title, id, width, height, isMaxSize, callback){
  if (!callback) {
    callback = function(){};
  }
  var modalWin = new top.YH.Window({
    closeAction: 'close',
    id: id,
    src: actionUrl,
    title: title,
    width: width ? width : 800,
    height: height ? height : 600,
    modal: true,
    listeners:{ 
      'close': function(t){
        callback();
        top.win = null;
      }
    }
  }).show();
}

/**
 * 关闭模态窗口


 * @param id
 * @return
 */
function closeModalWindow(id){
  top.YH.getCmp(id).close();
}

/**
 * 比较大小，内部使用

 * @param src1
 * @param src2
 * @return
 */
function _compare(src1, src2) {
  if (src1 > src2) {
    return 1;
  }else if (src1 < src2) {
    return -1;
  }else {
    return 0;
  }
}
/**
 * 比较两个对象的大小


 * @param obj1
 * @param obj2
 * @param key
 * @param type         1=整型;2=浮点性;其他字符串


 * @return
 */
function compare(obj1, obj2, key, type) {
  var c1 = obj1[key];
  var c2 = obj2[key];
  if (type == 1) {
    if (!c1) {
      c1 = 0;
    }
    if (!c2) {
      c2 = 0;
    }
    c1 = parseInt(c1);
    c2 = parseInt(c2);
  }else if (type == 2) {
    if (!c1) {
      c1 = 0;
    }
    if (!c2) {
      c2 = 0;
    }
    c1 = parseFloat(c1);
    c2 = parseFloat(c2);
  }else {
    if (!c1) {
      c1 = "";
    }
    if (!c2) {
      c2 = "";
    }
    try {
      return c1.localeCompare(c2);
    }catch(e) {
    }
  }
  return _compare(c1, c2);
}
/**
 * 批量下载
 * @param attachmentName
 * @param attachmentId
 * @param module
 * @return
 */
function batchDownload(attachmentName,attachmentId,module,name){
  var param = "attachmentName=" + encodeURIComponent(attachmentName) + "&attachmentId=" + attachmentId + "&module=" + module + "&name=" + name;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/batchDownload.act?";
  url += param;
  location = url ;
}
 /**
 * 批量下载
 */
 function batchDownloadFnet(fileNames,paths,name){
 var param = "fileNames=" + encodeURIComponent(fileNames) + "&paths=" + encodeURIComponent(paths) + "&name=" + encodeURIComponent(name) ;
 var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/batchDownloadByLocal.act?";
 url += param;
 location = url ;
 }

/**
 * 金额增加千分符号
 */
function formatAmt(srcStr, scale) {
  if (!srcStr) {
    return "";
  }
  if (!isNumber(srcStr)) {
    return "";
  }
  if (!scale) {
    scale = 2;
  }
  srcStr = num2Str(resetAmt(srcStr), scale);
  var re = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
  srcStr = srcStr.replace(re, "$1,");
  return srcStr;
}
/**
 * 清理千分符号，恢复金额原值


 */
function resetAmt(srcStr) {
  if (!srcStr) {
    return "";
  }
  var re = /,/g;
  srcStr = srcStr.replace(re, "");
  return srcStr;
}
/**
 * 添加处理千分符的能力
 * @cntrl              相关控件
 * @formCntrl          cntrl所在的Form
 * @scale              精度，缺省是2
 */
function bindKiloSplitPrc(cntrl, formCntrl, scale) {  
  if (!cntrl || !$(cntrl)) {
    return;
  }
  if (!scale) {
    scale = 2;
  }
  cntrl = $(cntrl);
  if (cntrl["prcKiloSplit"] === true) {
    return;
  }
  cntrl.observe("focus", function(event){
    var srcElem = Event.element(event);
    var re = /,/g;
    srcElem.value = srcElem.value.replace(re, "");
    selectLast(srcElem);
    srcElem.select();
  });  
  cntrl.observe("blur", function(event){
    var srcElem = Event.element(event);
    if (srcElem.value && !isNumber(srcElem.value)) {
      alert("请输入数值");
      selectLast(srcElem);
      srcElem.select();
      return;
    }
    srcElem.value = formatAmt(srcElem.value);
  });
  cntrl["prcKiloSplit"] = true;
  formCntrl = $(formCntrl);
  if (formCntrl) {
    var kiloSplitHidden = $("kiloSplitCntrlIds");
    if (!kiloSplitHidden) {
      kiloSplitHidden = new Element("input", {type:"hidden", name:"kiloSplitCntrlIds", id:"kiloSplitCntrlIds", value:""});
      formCntrl.appendChild(kiloSplitHidden);
    }
    if (kiloSplitHidden.value.indexOf(cntrl.id + ",") < 0) {
      kiloSplitHidden.value = kiloSplitHidden.value + cntrl.id + ","
    }
  }
}
/**
 * 批量处理千分符号
 */
function bindKiloSplitPrcBatch(cntrls, formCntrl, scale) {
  var cntrlArray = null;
  if (Object.isArray(cntrls)) {
    cntrlArray = cntrls;
  }else if (Object.isString(cntrls)) {
    cntrlArray = cntrls.split(",");
    for (var i = 0; i < cntrlArray.length; i++) {
      cntrlArray[i] = cntrlArray[i].strip();
    }
  }
  for (var i = 0; i < cntrlArray.length; i++) {
    var cntrl = $(cntrlArray[i]);
    if (cntrl) {
      bindKiloSplitPrc(cntrl, formCntrl, scale);
    }
  }
}

/**
 * 处理frameset,左边是菜单,右边是功能区,菜单不随着功能区滚动的问题
 * @param menuId
 * @param mainId
 * @param minHeight
 * @param url
 * @return
 */
function doFramesetScroll(menuId, mainId, minHeight, url) {
  try {
    var main = document.getElementById(mainId);
    var menu = document.getElementById(menuId);
    menu.scrolling = "no";
    menu.contentWindow.document.body.style.height = "2000px";
    menu.contentWindow.document.attachEvent && menu.contentWindow.document.attachEvent("onmousewheel", function(e) {
      var top = (main.contentWindow.document.documentElement || main.contentWindow.document.body).scrollTop;
      if (e.wheelDelta >= 120) {
        main.contentWindow.scrollTo(0, top - 50);
      }
      else {
        main.contentWindow.scrollTo(0, top + 50);
      }
    });
    main.onload = main.onreadystatechange = function() {
      if (!this.readyState || this.readyState == "complete") {
        menu.contentWindow.scrollTo(0, 0);
        main.contentWindow.document.body.style.minHeight = parseInt(minHeight) + "px";
        if (main.contentWindow.attachEvent) {
          main.contentWindow.attachEvent("onscroll", function(e) {
            menu.contentWindow.scrollTo(0, main.contentWindow.document.documentElement.scrollTop);
            return false;
          });
        }
        else if (main.contentWindow.addEventListener) {
          main.contentWindow.addEventListener("scroll", function(e) {
            menu.contentWindow.scrollTo(0, main.contentWindow.document.documentElement.scrollTop);
            return false;
          }, false);
        }
      }
    };
    main.src = url;
  } catch (e) {
    
  } finally {
    
  }
}

function fitIE9() {
  Date.prototype.getYear = Date.prototype.getFullYear;
}

if (isTouchDevice) {
  if (top.YH && top.YH.version) {
    window.showModalDialog = function () {
      top.YH && top.showModalDialog.apply(this, arguments);
    }
    
    window.open = function () {
      top.YH && top.open.apply(this, arguments);
    }
  }
  function simMouseEvt(type, target, pos) {
    var evt = document.createEvent('MouseEvents');
    evt.initMouseEvent(type, true, true, document, 
        0, pos.screenX, pos.screenY, pos.clientX, pos.clientY, 
        false, false, false, false, 
        0, target);
    return target.dispatchEvent(evt);
  }
  document.addEventListener("touchend", function(e) {
    var timer;
    if (e.target instanceof HTMLElement) {
      if (e.target.getAttribute("yhdbclick")) {
        clearTimeout(timer);
        e.target.removeAttribute("yhdbclick");
        simMouseEvt("dblclick", e.target, e.changedTouches[0]) || e.preventDefault();
      }
      else {
        e.target.setAttribute("yhdbclick", "dbclick");
        timer = setTimeout(function() {
          e.target.removeAttribute("yhdbclick");
        }, 1000);
      }
    }
  });
}

fitIE9();

/*!
 * iScroll v4.1.9 ~ Copyright (c) 2011 Matteo Spinelli, http://cubiq.org
 * Released under MIT license, http://cubiq.org/license
 */
(function(){
var m = Math,
  mround = function (r) { return r >> 0; },
  vendor = (/webkit/i).test(navigator.appVersion) ? 'webkit' :
    (/firefox/i).test(navigator.userAgent) ? 'Moz' :
    (/trident/i).test(navigator.userAgent) ? 'ms' :
    'opera' in window ? 'O' : '',

    // Browser capabilities
    isAndroid = (/android/gi).test(navigator.appVersion),
    isIDevice = (/iphone|ipad/gi).test(navigator.appVersion),
    isPlaybook = (/playbook/gi).test(navigator.appVersion),
    isTouchPad = (/hp-tablet/gi).test(navigator.appVersion),

    has3d = 'WebKitCSSMatrix' in window && 'm11' in new WebKitCSSMatrix(),
    hasTouch = 'ontouchstart' in window && !isTouchPad,
    hasTransform = vendor + 'Transform' in document.documentElement.style,
    hasTransitionEnd = isIDevice || isPlaybook,

  nextFrame = (function() {
      return window.requestAnimationFrame
      || window.webkitRequestAnimationFrame
      || window.mozRequestAnimationFrame
      || window.oRequestAnimationFrame
      || window.msRequestAnimationFrame
      || function(callback) { return setTimeout(callback, 1); };
  })(),
  cancelFrame = (function () {
      return window.cancelRequestAnimationFrame
      || window.webkitCancelAnimationFrame
      || window.webkitCancelRequestAnimationFrame
      || window.mozCancelRequestAnimationFrame
      || window.oCancelRequestAnimationFrame
      || window.msCancelRequestAnimationFrame
      || clearTimeout;
  })(),

  // Events
  RESIZE_EV = 'onorientationchange' in window ? 'orientationchange' : 'resize',
  START_EV = hasTouch ? 'touchstart' : 'mousedown',
  MOVE_EV = hasTouch ? 'touchmove' : 'mousemove',
  END_EV = hasTouch ? 'touchend' : 'mouseup',
  CANCEL_EV = hasTouch ? 'touchcancel' : 'mouseup',
  WHEEL_EV = vendor == 'Moz' ? 'DOMMouseScroll' : 'mousewheel',

  // Helpers
  trnOpen = 'translate' + (has3d ? '3d(' : '('),
  trnClose = has3d ? ',0)' : ')',

  // Constructor
  iScroll = function (el, options) {
    var that = this,
      doc = document,
      i;

    that.wrapper = typeof el == 'object' ? el : doc.getElementById(el);
    that.wrapper.style.overflow = 'hidden';
    that.scroller = that.wrapper.children[0];

    // Default options
    that.options = {
      hScroll: true,
      vScroll: true,
      x: 0,
      y: 0,
      bounce: true,
      bounceLock: false,
      momentum: true,
      lockDirection: true,
      useTransform: true,
      useTransition: false,
      topOffset: 0,
      checkDOMChanges: false,   // Experimental

      // Scrollbar
      hScrollbar: true,
      vScrollbar: true,
      fixedScrollbar: isAndroid,
      hideScrollbar: isIDevice,
      fadeScrollbar: isIDevice && has3d,
      scrollbarClass: '',

      // Zoom
      zoom: false,
      zoomMin: 1,
      zoomMax: 4,
      doubleTapZoom: 2,
      wheelAction: 'scroll',

      // Snap
      snap: false,
      snapThreshold: 1,

      // Events
      onRefresh: null,
      onBeforeScrollStart: function (e) {
        var target = e.targetElement || e.target;
        if (target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA') {
          e.preventDefault(); 
        }
      },
      onScrollStart: null,
      onBeforeScrollMove: null,
      onScrollMove: null,
      onBeforeScrollEnd: null,
      onScrollEnd: null,
      onTouchEnd: null,
      onDestroy: null,
      onZoomStart: null,
      onZoom: null,
      onZoomEnd: null
    };

    // User defined options
    for (i in options) that.options[i] = options[i];
    
    // Set starting position
    that.x = that.options.x;
    that.y = that.options.y;

    // Normalize options
    that.options.useTransform = hasTransform ? that.options.useTransform : false;
    that.options.hScrollbar = that.options.hScroll && that.options.hScrollbar;
    that.options.vScrollbar = that.options.vScroll && that.options.vScrollbar;
    that.options.zoom = that.options.useTransform && that.options.zoom;
    that.options.useTransition = hasTransitionEnd && that.options.useTransition;

    // Helpers FIX ANDROID BUG!
    // translate3d and scale doesn't work together! 
    // Ignoring 3d ONLY WHEN YOU SET that.options.zoom
    if ( that.options.zoom && isAndroid ){
      trnOpen = 'translate(';
      trnClose = ')';
    }
    
    // Set some default styles
    that.scroller.style[vendor + 'TransitionProperty'] = that.options.useTransform ? '-' + vendor.toLowerCase() + '-transform' : 'top left';
    that.scroller.style[vendor + 'TransitionDuration'] = '0';
    that.scroller.style[vendor + 'TransformOrigin'] = '0 0';
    if (that.options.useTransition) that.scroller.style[vendor + 'TransitionTimingFunction'] = 'cubic-bezier(0.33,0.66,0.66,1)';
    
    if (that.options.useTransform) that.scroller.style[vendor + 'Transform'] = trnOpen + that.x + 'px,' + that.y + 'px' + trnClose;
    else that.scroller.style.cssText += ';position:absolute;top:' + that.y + 'px;left:' + that.x + 'px';

    if (that.options.useTransition) that.options.fixedScrollbar = true;

    that.refresh();

    that._bind(RESIZE_EV, window);
    that._bind(START_EV);
    if (!hasTouch) {
      that._bind('mouseout', that.wrapper);
      if (that.options.wheelAction != 'none')
        that._bind(WHEEL_EV);
    }

    if (that.options.checkDOMChanges) that.checkDOMTime = setInterval(function () {
      that._checkDOMChanges();
    }, 500);
  };

// Prototype
iScroll.prototype = {
  enabled: true,
  x: 0,
  y: 0,
  steps: [],
  scale: 1,
  currPageX: 0, currPageY: 0,
  pagesX: [], pagesY: [],
  aniTime: null,
  wheelZoomCount: 0,
  
  handleEvent: function (e) {
    var that = this;
    switch(e.type) {
      case START_EV:
        if (!hasTouch && e.button !== 0) return;
        that._start(e);
        break;
      case MOVE_EV: that._move(e); break;
      case END_EV:
      case CANCEL_EV: that._end(e); break;
      case RESIZE_EV: that._resize(); break;
      case WHEEL_EV: that._wheel(e); break;
      case 'mouseout': that._mouseout(e); break;
      case 'webkitTransitionEnd': that._transitionEnd(e); break;
    }
  },
  
  _checkDOMChanges: function () {
    if (this.moved || this.zoomed || this.animating ||
      (this.scrollerW == this.scroller.offsetWidth * this.scale && this.scrollerH == this.scroller.offsetHeight * this.scale)) return;

    this.refresh();
  },
  
  _scrollbar: function (dir) {
    var that = this,
      doc = document,
      bar;

    if (!that[dir + 'Scrollbar']) {
      if (that[dir + 'ScrollbarWrapper']) {
        if (hasTransform) that[dir + 'ScrollbarIndicator'].style[vendor + 'Transform'] = '';
        that[dir + 'ScrollbarWrapper'].parentNode.removeChild(that[dir + 'ScrollbarWrapper']);
        that[dir + 'ScrollbarWrapper'] = null;
        that[dir + 'ScrollbarIndicator'] = null;
      }

      return;
    }

    if (!that[dir + 'ScrollbarWrapper']) {
      // Create the scrollbar wrapper
      bar = doc.createElement('div');

      if (that.options.scrollbarClass) bar.className = that.options.scrollbarClass + dir.toUpperCase();
      else bar.style.cssText = 'position:absolute;z-index:100;' + (dir == 'h' ? 'height:7px;bottom:1px;left:2px;right:' + (that.vScrollbar ? '7' : '2') + 'px' : 'width:7px;bottom:' + (that.hScrollbar ? '7' : '2') + 'px;top:2px;right:1px');

      bar.style.cssText += ';pointer-events:none;-' + vendor + '-transition-property:opacity;-' + vendor + '-transition-duration:' + (that.options.fadeScrollbar ? '350ms' : '0') + ';overflow:hidden;opacity:' + (that.options.hideScrollbar ? '0' : '1');

      that.wrapper.appendChild(bar);
      that[dir + 'ScrollbarWrapper'] = bar;

      // Create the scrollbar indicator
      bar = doc.createElement('div');
      if (!that.options.scrollbarClass) {
        bar.style.cssText = 'position:absolute;z-index:100;background:rgba(0,0,0,0.5);border:1px solid rgba(255,255,255,0.9);-' + vendor + '-background-clip:padding-box;-' + vendor + '-box-sizing:border-box;' + (dir == 'h' ? 'height:100%' : 'width:100%') + ';-' + vendor + '-border-radius:3px;border-radius:3px';
      }
      bar.style.cssText += ';pointer-events:none;-' + vendor + '-transition-property:-' + vendor + '-transform;-' + vendor + '-transition-timing-function:cubic-bezier(0.33,0.66,0.66,1);-' + vendor + '-transition-duration:0;-' + vendor + '-transform:' + trnOpen + '0,0' + trnClose;
      if (that.options.useTransition) bar.style.cssText += ';-' + vendor + '-transition-timing-function:cubic-bezier(0.33,0.66,0.66,1)';

      that[dir + 'ScrollbarWrapper'].appendChild(bar);
      that[dir + 'ScrollbarIndicator'] = bar;
    }

    if (dir == 'h') {
      that.hScrollbarSize = that.hScrollbarWrapper.clientWidth;
      that.hScrollbarIndicatorSize = m.max(mround(that.hScrollbarSize * that.hScrollbarSize / that.scrollerW), 8);
      that.hScrollbarIndicator.style.width = that.hScrollbarIndicatorSize + 'px';
      that.hScrollbarMaxScroll = that.hScrollbarSize - that.hScrollbarIndicatorSize;
      that.hScrollbarProp = that.hScrollbarMaxScroll / that.maxScrollX;
    } else {
      that.vScrollbarSize = that.vScrollbarWrapper.clientHeight;
      that.vScrollbarIndicatorSize = m.max(mround(that.vScrollbarSize * that.vScrollbarSize / that.scrollerH), 8);
      that.vScrollbarIndicator.style.height = that.vScrollbarIndicatorSize + 'px';
      that.vScrollbarMaxScroll = that.vScrollbarSize - that.vScrollbarIndicatorSize;
      that.vScrollbarProp = that.vScrollbarMaxScroll / that.maxScrollY;
    }

    // Reset position
    that._scrollbarPos(dir, true);
  },
  
  _resize: function () {
    var that = this;
    setTimeout(function () { that.refresh(); }, isAndroid ? 200 : 0);
  },
  
  _pos: function (x, y) {
    x = this.hScroll ? x : 0;
    y = this.vScroll ? y : 0;

    if (this.options.useTransform) {
      this.scroller.style[vendor + 'Transform'] = trnOpen + x + 'px,' + y + 'px' + trnClose + ' scale(' + this.scale + ')';
    } else {
      x = mround(x);
      y = mround(y);
      this.scroller.style.left = x + 'px';
      this.scroller.style.top = y + 'px';
    }

    this.x = x;
    this.y = y;

    this._scrollbarPos('h');
    this._scrollbarPos('v');
  },

  _scrollbarPos: function (dir, hidden) {
    var that = this,
      pos = dir == 'h' ? that.x : that.y,
      size;

    if (!that[dir + 'Scrollbar']) return;

    pos = that[dir + 'ScrollbarProp'] * pos;

    if (pos < 0) {
      if (!that.options.fixedScrollbar) {
        size = that[dir + 'ScrollbarIndicatorSize'] + mround(pos * 3);
        if (size < 8) size = 8;
        that[dir + 'ScrollbarIndicator'].style[dir == 'h' ? 'width' : 'height'] = size + 'px';
      }
      pos = 0;
    } else if (pos > that[dir + 'ScrollbarMaxScroll']) {
      if (!that.options.fixedScrollbar) {
        size = that[dir + 'ScrollbarIndicatorSize'] - mround((pos - that[dir + 'ScrollbarMaxScroll']) * 3);
        if (size < 8) size = 8;
        that[dir + 'ScrollbarIndicator'].style[dir == 'h' ? 'width' : 'height'] = size + 'px';
        pos = that[dir + 'ScrollbarMaxScroll'] + (that[dir + 'ScrollbarIndicatorSize'] - size);
      } else {
        pos = that[dir + 'ScrollbarMaxScroll'];
      }
    }

    that[dir + 'ScrollbarWrapper'].style[vendor + 'TransitionDelay'] = '0';
    that[dir + 'ScrollbarWrapper'].style.opacity = hidden && that.options.hideScrollbar ? '0' : '1';
    that[dir + 'ScrollbarIndicator'].style[vendor + 'Transform'] = trnOpen + (dir == 'h' ? pos + 'px,0' : '0,' + pos + 'px') + trnClose;
  },
  
  _start: function (e) {
    var that = this,
      point = hasTouch ? e.touches[0] : e,
      matrix, x, y,
      c1, c2;

    if (!that.enabled) return;

    if (that.options.onBeforeScrollStart) that.options.onBeforeScrollStart.call(that, e);

    if (that.options.useTransition || that.options.zoom) that._transitionTime(0);

    that.moved = false;
    that.animating = false;
    that.zoomed = false;
    that.distX = 0;
    that.distY = 0;
    that.absDistX = 0;
    that.absDistY = 0;
    that.dirX = 0;
    that.dirY = 0;

    // Gesture start
    if (that.options.zoom && hasTouch && e.touches.length > 1) {
      c1 = m.abs(e.touches[0].pageX-e.touches[1].pageX);
      c2 = m.abs(e.touches[0].pageY-e.touches[1].pageY);
      that.touchesDistStart = m.sqrt(c1 * c1 + c2 * c2);

      that.originX = m.abs(e.touches[0].pageX + e.touches[1].pageX - that.wrapperOffsetLeft * 2) / 2 - that.x;
      that.originY = m.abs(e.touches[0].pageY + e.touches[1].pageY - that.wrapperOffsetTop * 2) / 2 - that.y;

      if (that.options.onZoomStart) that.options.onZoomStart.call(that, e);
    }

    if (that.options.momentum) {
      if (that.options.useTransform) {
        // Very lame general purpose alternative to CSSMatrix
        matrix = getComputedStyle(that.scroller, null)[vendor + 'Transform'].replace(/[^0-9-.,]/g, '').split(',');
        x = matrix[4] * 1;
        y = matrix[5] * 1;
      } else {
        x = getComputedStyle(that.scroller, null).left.replace(/[^0-9-]/g, '') * 1;
        y = getComputedStyle(that.scroller, null).top.replace(/[^0-9-]/g, '') * 1;
      }
      
      if (x != that.x || y != that.y) {
        if (that.options.useTransition) that._unbind('webkitTransitionEnd');
        else cancelFrame(that.aniTime);
        that.steps = [];
        that._pos(x, y);
      }
    }

    that.absStartX = that.x;  // Needed by snap threshold
    that.absStartY = that.y;

    that.startX = that.x;
    that.startY = that.y;
    that.pointX = point.pageX;
    that.pointY = point.pageY;

    that.startTime = e.timeStamp || Date.now();

    if (that.options.onScrollStart) that.options.onScrollStart.call(that, e);

    that._bind(MOVE_EV);
    that._bind(END_EV);
    that._bind(CANCEL_EV);
  },
  
  _move: function (e) {
    var that = this,
      point = hasTouch ? e.touches[0] : e,
      deltaX = point.pageX - that.pointX,
      deltaY = point.pageY - that.pointY,
      newX = that.x + deltaX,
      newY = that.y + deltaY,
      c1, c2, scale,
      timestamp = e.timeStamp || Date.now();

    if (that.options.onBeforeScrollMove) that.options.onBeforeScrollMove.call(that, e);

    // Zoom
    if (that.options.zoom && hasTouch && e.touches.length > 1) {
      c1 = m.abs(e.touches[0].pageX - e.touches[1].pageX);
      c2 = m.abs(e.touches[0].pageY - e.touches[1].pageY);
      that.touchesDist = m.sqrt(c1*c1+c2*c2);

      that.zoomed = true;

      scale = 1 / that.touchesDistStart * that.touchesDist * this.scale;

      if (scale < that.options.zoomMin) scale = 0.5 * that.options.zoomMin * Math.pow(2.0, scale / that.options.zoomMin);
      else if (scale > that.options.zoomMax) scale = 2.0 * that.options.zoomMax * Math.pow(0.5, that.options.zoomMax / scale);

      that.lastScale = scale / this.scale;

      newX = this.originX - this.originX * that.lastScale + this.x,
      newY = this.originY - this.originY * that.lastScale + this.y;

      this.scroller.style[vendor + 'Transform'] = trnOpen + newX + 'px,' + newY + 'px' + trnClose + ' scale(' + scale + ')';

      if (that.options.onZoom) that.options.onZoom.call(that, e);
      return;
    }

    that.pointX = point.pageX;
    that.pointY = point.pageY;

    // Slow down if outside of the boundaries
    if (newX > 0 || newX < that.maxScrollX) {
      newX = that.options.bounce ? that.x + (deltaX / 2) : newX >= 0 || that.maxScrollX >= 0 ? 0 : that.maxScrollX;
    }
    if (newY > that.minScrollY || newY < that.maxScrollY) { 
      newY = that.options.bounce ? that.y + (deltaY / 2) : newY >= that.minScrollY || that.maxScrollY >= 0 ? that.minScrollY : that.maxScrollY;
    }

    that.distX += deltaX;
    that.distY += deltaY;
    that.absDistX = m.abs(that.distX);
    that.absDistY = m.abs(that.distY);

    if (that.absDistX < 6 && that.absDistY < 6) {
      return;
    }

    // Lock direction
    if (that.options.lockDirection) {
      if (that.absDistX > that.absDistY + 5) {
        newY = that.y;
        deltaY = 0;
      } else if (that.absDistY > that.absDistX + 5) {
        newX = that.x;
        deltaX = 0;
      }
    }

    that.moved = true;
    that._pos(newX, newY);
    that.dirX = deltaX > 0 ? -1 : deltaX < 0 ? 1 : 0;
    that.dirY = deltaY > 0 ? -1 : deltaY < 0 ? 1 : 0;

    if (timestamp - that.startTime > 300) {
      that.startTime = timestamp;
      that.startX = that.x;
      that.startY = that.y;
    }
    
    if (that.options.onScrollMove) that.options.onScrollMove.call(that, e);
  },
  
  _end: function (e) {
    if (hasTouch && e.touches.length != 0) return;

    var that = this,
      point = hasTouch ? e.changedTouches[0] : e,
      target, ev,
      momentumX = { dist:0, time:0 },
      momentumY = { dist:0, time:0 },
      duration = (e.timeStamp || Date.now()) - that.startTime,
      newPosX = that.x,
      newPosY = that.y,
      distX, distY,
      newDuration,
      snap,
      scale;

    that._unbind(MOVE_EV);
    that._unbind(END_EV);
    that._unbind(CANCEL_EV);

    if (that.options.onBeforeScrollEnd) that.options.onBeforeScrollEnd.call(that, e);

    if (that.zoomed) {
      scale = that.scale * that.lastScale;
      scale = Math.max(that.options.zoomMin, scale);
      scale = Math.min(that.options.zoomMax, scale);
      that.lastScale = scale / that.scale;
      that.scale = scale;

      that.x = that.originX - that.originX * that.lastScale + that.x;
      that.y = that.originY - that.originY * that.lastScale + that.y;
      
      that.scroller.style[vendor + 'TransitionDuration'] = '200ms';
      that.scroller.style[vendor + 'Transform'] = trnOpen + that.x + 'px,' + that.y + 'px' + trnClose + ' scale(' + that.scale + ')';
      
      that.zoomed = false;
      that.refresh();

      if (that.options.onZoomEnd) that.options.onZoomEnd.call(that, e);
      return;
    }

    if (!that.moved) {
      if (hasTouch) {
        if (that.doubleTapTimer && that.options.zoom) {
          // Double tapped
          clearTimeout(that.doubleTapTimer);
          that.doubleTapTimer = null;
          if (that.options.onZoomStart) that.options.onZoomStart.call(that, e);
          that.zoom(that.pointX, that.pointY, that.scale == 1 ? that.options.doubleTapZoom : 1);
          if (that.options.onZoomEnd) {
            setTimeout(function() {
              that.options.onZoomEnd.call(that, e);
            }, 200); // 200 is default zoom duration
          }
        } else {
          that.doubleTapTimer = setTimeout(function () {
            that.doubleTapTimer = null;

            // Find the last touched element
            target = point.target;
            while (target.nodeType != 1) target = target.parentNode;

            if (target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA') {
              ev = document.createEvent('MouseEvents');
              ev.initMouseEvent('click', true, true, e.view, 1,
                point.screenX, point.screenY, point.clientX, point.clientY,
                e.ctrlKey, e.altKey, e.shiftKey, e.metaKey,
                0, null);
              ev._fake = true;
              target.dispatchEvent(ev);
            }
          }, that.options.zoom ? 250 : 0);
        }
      }

      that._resetPos(200);

      if (that.options.onTouchEnd) that.options.onTouchEnd.call(that, e);
      return;
    }

    if (duration < 300 && that.options.momentum) {
      momentumX = newPosX ? that._momentum(newPosX - that.startX, duration, -that.x, that.scrollerW - that.wrapperW + that.x, that.options.bounce ? that.wrapperW : 0) : momentumX;
      momentumY = newPosY ? that._momentum(newPosY - that.startY, duration, -that.y, (that.maxScrollY < 0 ? that.scrollerH - that.wrapperH + that.y - that.minScrollY : 0), that.options.bounce ? that.wrapperH : 0) : momentumY;

      newPosX = that.x + momentumX.dist;
      newPosY = that.y + momentumY.dist;

      if ((that.x > 0 && newPosX > 0) || (that.x < that.maxScrollX && newPosX < that.maxScrollX)) momentumX = { dist:0, time:0 };
      if ((that.y > that.minScrollY && newPosY > that.minScrollY) || (that.y < that.maxScrollY && newPosY < that.maxScrollY)) momentumY = { dist:0, time:0 };
    }

    if (momentumX.dist || momentumY.dist) {
      newDuration = m.max(m.max(momentumX.time, momentumY.time), 10);

      // Do we need to snap?
      if (that.options.snap) {
        distX = newPosX - that.absStartX;
        distY = newPosY - that.absStartY;
        if (m.abs(distX) < that.options.snapThreshold && m.abs(distY) < that.options.snapThreshold) { that.scrollTo(that.absStartX, that.absStartY, 200); }
        else {
          snap = that._snap(newPosX, newPosY);
          newPosX = snap.x;
          newPosY = snap.y;
          newDuration = m.max(snap.time, newDuration);
        }
      }

      that.scrollTo(mround(newPosX), mround(newPosY), newDuration);

      if (that.options.onTouchEnd) that.options.onTouchEnd.call(that, e);
      return;
    }

    // Do we need to snap?
    if (that.options.snap) {
      distX = newPosX - that.absStartX;
      distY = newPosY - that.absStartY;
      if (m.abs(distX) < that.options.snapThreshold && m.abs(distY) < that.options.snapThreshold) that.scrollTo(that.absStartX, that.absStartY, 200);
      else {
        snap = that._snap(that.x, that.y);
        if (snap.x != that.x || snap.y != that.y) that.scrollTo(snap.x, snap.y, snap.time);
      }

      if (that.options.onTouchEnd) that.options.onTouchEnd.call(that, e);
      return;
    }

    that._resetPos(200);
    if (that.options.onTouchEnd) that.options.onTouchEnd.call(that, e);
  },
  
  _resetPos: function (time) {
    var that = this,
      resetX = that.x >= 0 ? 0 : that.x < that.maxScrollX ? that.maxScrollX : that.x,
      resetY = that.y >= that.minScrollY || that.maxScrollY > 0 ? that.minScrollY : that.y < that.maxScrollY ? that.maxScrollY : that.y;

    if (resetX == that.x && resetY == that.y) {
      if (that.moved) {
        that.moved = false;
        if (that.options.onScrollEnd) that.options.onScrollEnd.call(that);    // Execute custom code on scroll end
      }

      if (that.hScrollbar && that.options.hideScrollbar) {
        if (vendor == 'webkit') that.hScrollbarWrapper.style[vendor + 'TransitionDelay'] = '300ms';
        that.hScrollbarWrapper.style.opacity = '0';
      }
      if (that.vScrollbar && that.options.hideScrollbar) {
        if (vendor == 'webkit') that.vScrollbarWrapper.style[vendor + 'TransitionDelay'] = '300ms';
        that.vScrollbarWrapper.style.opacity = '0';
      }

      return;
    }

    that.scrollTo(resetX, resetY, time || 0);
  },

  _wheel: function (e) {
    var that = this,
      wheelDeltaX, wheelDeltaY,
      deltaX, deltaY,
      deltaScale;

    if ('wheelDeltaX' in e) {
      wheelDeltaX = e.wheelDeltaX / 12;
      wheelDeltaY = e.wheelDeltaY / 12;
    } else if('wheelDelta' in e) {
      wheelDeltaX = wheelDeltaY = e.wheelDelta / 12;
    } else if ('detail' in e) {
      wheelDeltaX = wheelDeltaY = -e.detail * 3;
    } else {
      return;
    }
    
    if (that.options.wheelAction == 'zoom') {
      deltaScale = that.scale * Math.pow(2, 1/3 * (wheelDeltaY ? wheelDeltaY / Math.abs(wheelDeltaY) : 0));
      if (deltaScale < that.options.zoomMin) deltaScale = that.options.zoomMin;
      if (deltaScale > that.options.zoomMax) deltaScale = that.options.zoomMax;
      
      if (deltaScale != that.scale) {
        if (!that.wheelZoomCount && that.options.onZoomStart) that.options.onZoomStart.call(that, e);
        that.wheelZoomCount++;
        
        that.zoom(e.pageX, e.pageY, deltaScale, 400);
        
        setTimeout(function() {
          that.wheelZoomCount--;
          if (!that.wheelZoomCount && that.options.onZoomEnd) that.options.onZoomEnd.call(that, e);
        }, 400);
      }
      
      return;
    }
    
    deltaX = that.x + wheelDeltaX;
    deltaY = that.y + wheelDeltaY;

    if (deltaX > 0) deltaX = 0;
    else if (deltaX < that.maxScrollX) deltaX = that.maxScrollX;

    if (deltaY > that.minScrollY) deltaY = that.minScrollY;
    else if (deltaY < that.maxScrollY) deltaY = that.maxScrollY;

    that.scrollTo(deltaX, deltaY, 0);
  },
  
  _mouseout: function (e) {
    var t = e.relatedTarget;

    if (!t) {
      this._end(e);
      return;
    }

    while (t = t.parentNode) if (t == this.wrapper) return;
    
    this._end(e);
  },

  _transitionEnd: function (e) {
    var that = this;

    if (e.target != that.scroller) return;

    that._unbind('webkitTransitionEnd');
    
    that._startAni();
  },


  /**
   *
   * Utilities
   *
   */
  _startAni: function () {
    var that = this,
      startX = that.x, startY = that.y,
      startTime = Date.now(),
      step, easeOut,
      animate;

    if (that.animating) return;
    
    if (!that.steps.length) {
      that._resetPos(400);
      return;
    }
    
    step = that.steps.shift();
    
    if (step.x == startX && step.y == startY) step.time = 0;

    that.animating = true;
    that.moved = true;
    
    if (that.options.useTransition) {
      that._transitionTime(step.time);
      that._pos(step.x, step.y);
      that.animating = false;
      if (step.time) that._bind('webkitTransitionEnd');
      else that._resetPos(0);
      return;
    }

    animate = function () {
      var now = Date.now(),
        newX, newY;

      if (now >= startTime + step.time) {
        that._pos(step.x, step.y);
        that.animating = false;
        if (that.options.onAnimationEnd) that.options.onAnimationEnd.call(that);      // Execute custom code on animation end
        that._startAni();
        return;
      }

      now = (now - startTime) / step.time - 1;
      easeOut = m.sqrt(1 - now * now);
      newX = (step.x - startX) * easeOut + startX;
      newY = (step.y - startY) * easeOut + startY;
      that._pos(newX, newY);
      if (that.animating) that.aniTime = nextFrame(animate);
    };

    animate();
  },

  _transitionTime: function (time) {
    time += 'ms';
    this.scroller.style[vendor + 'TransitionDuration'] = time;
    if (this.hScrollbar) this.hScrollbarIndicator.style[vendor + 'TransitionDuration'] = time;
    if (this.vScrollbar) this.vScrollbarIndicator.style[vendor + 'TransitionDuration'] = time;
  },

  _momentum: function (dist, time, maxDistUpper, maxDistLower, size) {
    var deceleration = 0.0006,
      speed = m.abs(dist) / time,
      newDist = (speed * speed) / (2 * deceleration),
      newTime = 0, outsideDist = 0;

    // Proportinally reduce speed if we are outside of the boundaries 
    if (dist > 0 && newDist > maxDistUpper) {
      outsideDist = size / (6 / (newDist / speed * deceleration));
      maxDistUpper = maxDistUpper + outsideDist;
      speed = speed * maxDistUpper / newDist;
      newDist = maxDistUpper;
    } else if (dist < 0 && newDist > maxDistLower) {
      outsideDist = size / (6 / (newDist / speed * deceleration));
      maxDistLower = maxDistLower + outsideDist;
      speed = speed * maxDistLower / newDist;
      newDist = maxDistLower;
    }

    newDist = newDist * (dist < 0 ? -1 : 1);
    newTime = speed / deceleration;

    return { dist: newDist, time: mround(newTime) };
  },

  _offset: function (el) {
    var left = -el.offsetLeft,
      top = -el.offsetTop;
      
    while (el = el.offsetParent) {
      left -= el.offsetLeft;
      top -= el.offsetTop;
    }
    
    if (el != this.wrapper) {
      left *= this.scale;
      top *= this.scale;
    }

    return { left: left, top: top };
  },

  _snap: function (x, y) {
    var that = this,
      i, l,
      page, time,
      sizeX, sizeY;

    // Check page X
    page = that.pagesX.length - 1;
    for (i=0, l=that.pagesX.length; i<l; i++) {
      if (x >= that.pagesX[i]) {
        page = i;
        break;
      }
    }
    if (page == that.currPageX && page > 0 && that.dirX < 0) page--;
    x = that.pagesX[page];
    sizeX = m.abs(x - that.pagesX[that.currPageX]);
    sizeX = sizeX ? m.abs(that.x - x) / sizeX * 500 : 0;
    that.currPageX = page;

    // Check page Y
    page = that.pagesY.length-1;
    for (i=0; i<page; i++) {
      if (y >= that.pagesY[i]) {
        page = i;
        break;
      }
    }
    if (page == that.currPageY && page > 0 && that.dirY < 0) page--;
    y = that.pagesY[page];
    sizeY = m.abs(y - that.pagesY[that.currPageY]);
    sizeY = sizeY ? m.abs(that.y - y) / sizeY * 500 : 0;
    that.currPageY = page;

    // Snap with constant speed (proportional duration)
    time = mround(m.max(sizeX, sizeY)) || 200;

    return { x: x, y: y, time: time };
  },

  _bind: function (type, el, bubble) {
    (el || this.scroller).addEventListener(type, this, !!bubble);
  },

  _unbind: function (type, el, bubble) {
    (el || this.scroller).removeEventListener(type, this, !!bubble);
  },


  /**
   *
   * Public methods
   *
   */
  destroy: function () {
    var that = this;

    that.scroller.style[vendor + 'Transform'] = '';

    // Remove the scrollbars
    that.hScrollbar = false;
    that.vScrollbar = false;
    that._scrollbar('h');
    that._scrollbar('v');

    // Remove the event listeners
    that._unbind(RESIZE_EV, window);
    that._unbind(START_EV);
    that._unbind(MOVE_EV);
    that._unbind(END_EV);
    that._unbind(CANCEL_EV);
    
    if (!that.options.hasTouch) {
      that._unbind('mouseout', that.wrapper);
      that._unbind(WHEEL_EV);
    }
    
    if (that.options.useTransition) that._unbind('webkitTransitionEnd');
    
    if (that.options.checkDOMChanges) clearInterval(that.checkDOMTime);
    
    if (that.options.onDestroy) that.options.onDestroy.call(that);
  },

  refresh: function () {
    var that = this,
      offset,
      i, l,
      els,
      pos = 0,
      page = 0;

    if (that.scale < that.options.zoomMin) that.scale = that.options.zoomMin;
    that.wrapperW = that.wrapper.clientWidth || 1;
    that.wrapperH = that.wrapper.clientHeight || 1;

    that.minScrollY = -that.options.topOffset || 0;
    that.scrollerW = mround(that.scroller.offsetWidth * that.scale);
    that.scrollerH = mround((that.scroller.offsetHeight + that.minScrollY) * that.scale);
    that.maxScrollX = that.wrapperW - that.scrollerW;
    that.maxScrollY = that.wrapperH - that.scrollerH + that.minScrollY;
    that.dirX = 0;
    that.dirY = 0;

    if (that.options.onRefresh) that.options.onRefresh.call(that);

    that.hScroll = that.options.hScroll && that.maxScrollX < 0;
    that.vScroll = that.options.vScroll && (!that.options.bounceLock && !that.hScroll || that.scrollerH > that.wrapperH);

    that.hScrollbar = that.hScroll && that.options.hScrollbar;
    that.vScrollbar = that.vScroll && that.options.vScrollbar && that.scrollerH > that.wrapperH;

    offset = that._offset(that.wrapper);
    that.wrapperOffsetLeft = -offset.left;
    that.wrapperOffsetTop = -offset.top;

    // Prepare snap
    if (typeof that.options.snap == 'string') {
      that.pagesX = [];
      that.pagesY = [];
      els = that.scroller.querySelectorAll(that.options.snap);
      for (i=0, l=els.length; i<l; i++) {
        pos = that._offset(els[i]);
        pos.left += that.wrapperOffsetLeft;
        pos.top += that.wrapperOffsetTop;
        that.pagesX[i] = pos.left < that.maxScrollX ? that.maxScrollX : pos.left * that.scale;
        that.pagesY[i] = pos.top < that.maxScrollY ? that.maxScrollY : pos.top * that.scale;
      }
    } else if (that.options.snap) {
      that.pagesX = [];
      while (pos >= that.maxScrollX) {
        that.pagesX[page] = pos;
        pos = pos - that.wrapperW;
        page++;
      }
      if (that.maxScrollX%that.wrapperW) that.pagesX[that.pagesX.length] = that.maxScrollX - that.pagesX[that.pagesX.length-1] + that.pagesX[that.pagesX.length-1];

      pos = 0;
      page = 0;
      that.pagesY = [];
      while (pos >= that.maxScrollY) {
        that.pagesY[page] = pos;
        pos = pos - that.wrapperH;
        page++;
      }
      if (that.maxScrollY%that.wrapperH) that.pagesY[that.pagesY.length] = that.maxScrollY - that.pagesY[that.pagesY.length-1] + that.pagesY[that.pagesY.length-1];
    }

    // Prepare the scrollbars
    that._scrollbar('h');
    that._scrollbar('v');

    if (!that.zoomed) {
      that.scroller.style[vendor + 'TransitionDuration'] = '0';
      that._resetPos(200);
    }
  },

  scrollTo: function (x, y, time, relative) {
    var that = this,
      step = x,
      i, l;

    that.stop();

    if (!step.length) step = [{ x: x, y: y, time: time, relative: relative }];
    
    for (i=0, l=step.length; i<l; i++) {
      if (step[i].relative) { step[i].x = that.x - step[i].x; step[i].y = that.y - step[i].y; }
      that.steps.push({ x: step[i].x, y: step[i].y, time: step[i].time || 0 });
    }

    that._startAni();
  },

  scrollToElement: function (el, time) {
    var that = this, pos;
    el = el.nodeType ? el : that.scroller.querySelector(el);
    if (!el) return;

    pos = that._offset(el);
    pos.left += that.wrapperOffsetLeft;
    pos.top += that.wrapperOffsetTop;

    pos.left = pos.left > 0 ? 0 : pos.left < that.maxScrollX ? that.maxScrollX : pos.left;
    pos.top = pos.top > that.minScrollY ? that.minScrollY : pos.top < that.maxScrollY ? that.maxScrollY : pos.top;
    time = time === undefined ? m.max(m.abs(pos.left)*2, m.abs(pos.top)*2) : time;

    that.scrollTo(pos.left, pos.top, time);
  },

  scrollToPage: function (pageX, pageY, time) {
    var that = this, x, y;
    
    time = time === undefined ? 400 : time;

    if (that.options.onScrollStart) that.options.onScrollStart.call(that);

    if (that.options.snap) {
      pageX = pageX == 'next' ? that.currPageX+1 : pageX == 'prev' ? that.currPageX-1 : pageX;
      pageY = pageY == 'next' ? that.currPageY+1 : pageY == 'prev' ? that.currPageY-1 : pageY;

      pageX = pageX < 0 ? 0 : pageX > that.pagesX.length-1 ? that.pagesX.length-1 : pageX;
      pageY = pageY < 0 ? 0 : pageY > that.pagesY.length-1 ? that.pagesY.length-1 : pageY;

      that.currPageX = pageX;
      that.currPageY = pageY;
      x = that.pagesX[pageX];
      y = that.pagesY[pageY];
    } else {
      x = -that.wrapperW * pageX;
      y = -that.wrapperH * pageY;
      if (x < that.maxScrollX) x = that.maxScrollX;
      if (y < that.maxScrollY) y = that.maxScrollY;
    }

    that.scrollTo(x, y, time);
  },

  disable: function () {
    this.stop();
    this._resetPos(0);
    this.enabled = false;

    // If disabled after touchstart we make sure that there are no left over events
    this._unbind(MOVE_EV);
    this._unbind(END_EV);
    this._unbind(CANCEL_EV);
  },
  
  enable: function () {
    this.enabled = true;
  },
  
  stop: function () {
    if (this.options.useTransition) this._unbind('webkitTransitionEnd');
    else cancelFrame(this.aniTime);
    this.steps = [];
    this.moved = false;
    this.animating = false;
  },
  
  zoom: function (x, y, scale, time) {
    var that = this,
      relScale = scale / that.scale;

    if (!that.options.useTransform) return;

    that.zoomed = true;
    time = time === undefined ? 200 : time;
    x = x - that.wrapperOffsetLeft - that.x;
    y = y - that.wrapperOffsetTop - that.y;
    that.x = x - x * relScale + that.x;
    that.y = y - y * relScale + that.y;

    that.scale = scale;
    that.refresh();

    that.x = that.x > 0 ? 0 : that.x < that.maxScrollX ? that.maxScrollX : that.x;
    that.y = that.y > that.minScrollY ? that.minScrollY : that.y < that.maxScrollY ? that.maxScrollY : that.y;

    that.scroller.style[vendor + 'TransitionDuration'] = time + 'ms';
    that.scroller.style[vendor + 'Transform'] = trnOpen + that.x + 'px,' + that.y + 'px' + trnClose + ' scale(' + scale + ')';
    that.zoomed = false;
  },
  
  isReady: function () {
    return !this.moved && !this.zoomed && !this.animating;
  }
};

if (typeof exports !== 'undefined') exports.iScroll = iScroll;
else window.iScroll = iScroll;

})();

if ('ontouchstart' in window) {
  document.addEventListener('DOMContentLoaded', function() {
    setTimeout(function() {
      try {
        if (document.body.tagName != "BODY" || window.dialogArguments || window.opener) {
          return;
        }
        var height = top.getWorkspaceHeigt && top.getWorkspaceHeigt() - 10;
        document.body.style.height = "100%";
        document.documentElement.style.height = "100%";
        var ctn = document.createElement("DIV");
        var scroller = document.createElement("DIV");
        scroller.style.height = height + "px";
        scroller.style.overflow = "auto";
        scroller.style.position = "relative";
        ctn.style.overflow = "auto";
        document.body.style.overflow = "auto";
        for (var i = 0; i < document.body.childNodes.length; i++) {
          var child = document.body.childNodes[i];
          if (child.nodeType == 1) {
            ctn.appendChild(child);
          }
        }
        var bottom = document.createElement("DIV");
        bottom.style.height = "90px";
        ctn.appendChild(bottom);
        document.body.appendChild(scroller);
        scroller.appendChild(ctn);
        document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
        var s = new iScroll(scroller);
        setTimeout(function() {
          s.refresh();
        }, 2000);
      } catch (e) {
        alert(e);
      }
    }, 10);
  }, false);
}