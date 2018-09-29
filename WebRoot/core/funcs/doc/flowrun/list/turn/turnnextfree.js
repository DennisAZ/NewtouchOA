var requestUrl = contextPath +moduleSrcPath+"/act/YHFreeFlowTypeAct";
var flowType = 2;//说明是自由流程，主要用于flowUserSelect页面
var prePrcsId; //用于添加预设步骤中最大的步骤号
var nowPrcsId; //用于选择框，指定当前选择的步骤
var nowPrcs;//用于标记弹出的设置框是那个步骤的
var items ;//表单的第个元素主要用于设置可写字段
function doInit(){
  skinObjectToSpan(flowrun_list_turn_turnnext);
  var url = requestUrl + "/getTurnData.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + "&isManage=" + isManage  );
  if(json.rtState == '0'){
    var data = json.rtData;
    // data完整的格式{flowName:'自由流程',isPreview:true , formItem: 'sssss,ddd'  , runName:'自由流程测试(2010-03-09 10:56:34)',prcsList:[{prcsId:1,handlerState:'<font color=blue>系统管理员[主办人](已办结)</font>'},{prcsId:2,handlerState:'<font color=red>系统管理员[主办人](办理中)</font>,<font color=red>dddd(未接收)</font>' , isNowPrcs:true}],,nextPre:'<font color=red>系统管理员[主办人]</font>,'}
    if (data.error) { 
      alert(data.error);
      return;
    }
    $('flowNameSpan').update(data.flowName + "- 转交下一步骤");//更新flowNameSpan
    $('runNameSpan').update(data.runName);//更新runNameSpan
    $('freeItemOld').value = data.formItem;
    items = data.formItem;//表单字段，主要用于设置可写字段
    for (var i = 0 ;i < data.prcsList.length ; i ++) {
      var prcsTmp = data.prcsList[i];
      addPrcs(prcsTmp);
    }
    //如果以有预设步骤的话
    if (data.isPreview && !data.nextPre ) {
      $('previewDiv').show();
    }
    //有预设步骤
    if (data.nextPre) {
      $('preSetDiv').hide();
      $('prcsText').update(data.nextPre);
    } else {
      var prcsText = getPrcsText("");
      $('prcsText').update(prcsText);
      setMenu('');
    }
    $('smsContent').value = "工作流提醒：" + data.runName;
    prePrcsId = parseInt(prcsId) + 2;
    if (data.sms2Priv) {
      $('sms2RemindSpan').show();
      if (findId(data.sms2Remind , '7')) {
        $('sms2Remind').checked = true;
      }
    }
    if (findId(data.smsRemind , '7')) {
      $('smsRemind').checked = true;
    }
    // isOnloadFinish = true;
  } 
}
function addPrcs(prcs){
  var tr = new Element("tr",{'class' : 'TableContent'});
  var tooltip = prcs.isNowPrcs ? "(当前步骤)" : "";
  var td1 = new Element("td").update("第<font color=red>" + prcs.prcsId + "</font>步: " + tooltip);
  var td2 = new Element("td").update(prcs.handlerState);
  tr.appendChild(td1);
  tr.appendChild(td2);
  $('prcsListTbody').appendChild(tr);
}

/**
 * 点击菜单执行的函数
 */
function flagMenuAction(){
  //取出当前的流程号,
  var id = arguments[2].split(':')[1];
  var flag = arguments[2].split(':')[0];
  
  $('topFlag' + id).value = flag;
  if(flag == '2'){
    $('topFLagSpan' + id).update("无主办人会签");
  }else if(flag == '1'){
    $('topFLagSpan' + id).update("先接收为主办");
  }else{
    $('topFlag' + id).value = '0';
    $('topFLagSpan' + id).update("主办人:");
  }
  //不是主办人时,要清空主办人
  if(flag != 0){
    $('prcsOpUser' + id).value = "" ; 
    $("prcsOpUserNameSpan" + id).update("");
  }
}
/**
 * 清空
 */
function cancelAll(i){
  $('prcsOpUserNameSpan' + i).update("");
  $('prcsUserNameSpan' + i).update("");

  $('prcsUser' + i).value = "";
  $('prcsOpUser' + i).value = "";
}
/**
 * 删除单个用户
 * 对应图片 , id为userId, isOp是否为主办 ,i表时点击的是那个步骤
 */
function cancelUser(img , id , isOp , i){
  if(isOp){
    $("prcsOpUser" + i).value = "";
    img.parentNode.parentNode.removeChild(img.parentNode);
    return ;
  }
  //从用户id中移出id
  var prcsUser = $F('prcsUser'+ i);
  if(id == $("prcsOpUser"+ i).value){
    $("prcsOpUser"+ i).value = "";
    $('prcsOpUserNameSpan'+ i).update("");
  }
  prcsUser = getOutofStr(prcsUser , id);
  $('prcsUser'+ i).value = prcsUser;
  img.parentNode.parentNode.removeChild(img.parentNode);  
}
/**
 * 从字符串str中移出s
 */
function getOutofStr(str, s){
  var aStr = str.split(',');
  var strTmp = "";
  var j = 0 ;//控制重名
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i] && (aStr[i] != s || j != 0)){
        strTmp += aStr[i] + ',';
    }else{
      if(aStr[i] == s){
      j = 1;
      }  
    }
  }
  return strTmp;
}
/**
 * 根据给定的名字,将数据拼成"用户名<img id="deleteOpUser_id" align="absmiddle" >"的形式
 * id表示那个步骤
 */
function setUserName(names , ids , hasRemove , id){
  var aNames = names.split(",");
  var aIds = ids.split(",");
  var result = "";
  for(var i = 0 ;i < aNames.length ;i ++){
    var tmp = aNames[i];
    if(tmp){
      if(hasRemove){
        result += "<span>" + tmp + "<img id=\"userRemove-"+ aIds[i] +"-" + id +"\" align=\"absmiddle\" onclick=\"javascript:cancelUser(this,'"+ aIds[i] +"',false ,'"+ id +"');\" src=\"" + imgPath + "/remove.png\"/>&nbsp;</span>";
      }else{
        result += "<span>" + tmp + "&nbsp;</span>"
      }
    }
  }
  return result;
}
function addUser(i){
  nowPrcsId = i;
  prcsOpUserNameSpan = $('prcsOpUserNameSpan'+ i);
  prcsUserNameSpan = $('prcsUserNameSpan'+ i);
  prcsOpUser = $('prcsOpUser'+ i);
  prcsUser = $('prcsUser'+ i);
  topFlagInput = $('topFlag'+ i);
  var chooseType = window.showModalDialog(contextPath + "/core/funcs/doc/flowrun/list/turn/userSelect/FreeFlowUserSelect.jsp",window,"dialogLeft=500px;dialogTop=350px;dialogWidth=510px;dialogHeight=400px;status=no;resizable=no"); 
}
function showItem() {
  var content = "<div align=center><table>"
    + "<tr class=TableHeader><td><img src='"+ imgPath +"/green_arrow.gif'/>请选择回退步骤：</td></tr>"
    + "<tr><td align=left id='prcsList'>"  + tmp.join("") + "</td></tr>"
    + "<tr><td align=center><input value='回退' type='button' onclick='backTo()'>&nbsp;&nbsp;<input value='关闭' type='button' onclick='winClose()'></td></tr>"
    + "</table></div>";
  alertWin('设置可写字段', content  , 300,480);
}
/**
 * 添加预设步骤
 * @return
 */
function addPre() {
  var tr = new Element("tr",{'class' : 'TableContent'});
  var tooltip = "(预设步骤)";
  var td1 = new Element("td").update("第<font color=red>" + prePrcsId + "</font>步: " + tooltip);
  var td2 = new Element("td").update(getPrcsText(prePrcsId));
  tr.appendChild(td1);
  tr.appendChild(td2);
  $('preListTbody').appendChild(tr);
  setMenu(prePrcsId);
  
  prePrcsId = parseInt(prePrcsId) + 1;
  $('delBtn').show();
}
/**
 * 设置预设步骤
 * @param i
 * @return
 */
function getPrcsText(i) {
  var tmp = new Array();
  tmp[0] = '<div style="line-height:18px;">';
  tmp[1] = '<a id="topFlagA'+ i +'" href="javascript:;"><span id="topFLagSpan'+ i +'">主办人：</span> <img align="absMiddle" src="'+ imgPath +'/menu_arrow_down.gif"/></a><input type=hidden name=topFlag'+ i +' id=topFlag'+ i +'  value=0>';
  tmp[2] = '<span id=prcsOpUserNameSpan'+ i +'></span><input type=hidden name=prcsOpUser'+ i +' id=prcsOpUser'+ i +'></div>';
  tmp[3] = '<div style="line-height:18px;">经办人：<span id=prcsUserNameSpan'+ i +'></span><input type=hidden name=prcsUser'+ i +' id=prcsUser'+ i +'></div>';
  tmp[4] = '<input type="button" value="选择人员" id="selecteUserButton" onclick="addUser(\''+ i +'\')" class="SmallButtonW"/>&nbsp;';
  tmp[5] = '<a onclick="cancelAll(\''+ i +'\')" class="orgClear" href="javascript:;">清空</a>&nbsp;&nbsp;';
  tmp[6] = '<a href="javascript:setFreeItem(\''+ i +'\')">可写字段</a><input type=hidden id="freeItem'+ i +'" name="freeItem'+ i  +'" value=""/>';
  var prcsText= tmp.join("");
  return prcsText;
}
/**
 * 设置菜单
 * @param prePrcsId
 * @return
 */
function setMenu(prePrcsId){
  var topFlagMenuData = [{name:'主办人:',action:flagMenuAction,extData:'0:' + prePrcsId}
    ,{name:'无主办人会签',action:flagMenuAction,extData:'2:' + prePrcsId}
    ,{name:'先接收为主办',action:flagMenuAction,extData:'1:' + prePrcsId}];
  $('topFlagA' + prePrcsId).onclick = function(event){
    var menu = new Menu({bindTo:$('topFlagA' + prePrcsId) , menuData:topFlagMenuData , attachCtrl:true});
    menu.show(event);
  }
}
/**
 * 删除预定义步骤
 */
function delPre(){
  var preListTbody =  $('preListTbody');
  var last = preListTbody.lastChild;
  preListTbody.removeChild(last);
  prePrcsId = parseInt(prePrcsId) - 1;
  if (prePrcsId <= parseInt(prcsId) + 2) {
  	$('delBtn').hide();
  }
}
function turnNext () {
  var prcsUserInputs = $$('#prcsTable input[id^="prcsUser"]');
  for (var i = 0 ;i < prcsUserInputs.length ; i++ ){
  	var input = prcsUserInputs[i];
  	if (!$(input).present()) {
  	  alert('请指定好所选步骤的经办人!');
  	  return ;
  	}
  }
  var prcsOpUserInputs = $$('#prcsTable input[id^="prcsOpUser"]');
  for (var i = 0 ;i < prcsOpUserInputs.length ; i++ ){
  	var input = prcsOpUserInputs[i];
  	var id = input.id.substr("prcsOpUser".length);
  	var topFlag = $("topFlag" + id).value;
  	if (topFlag == '0'
  	  && !$(input).present()) {
  	  alert('请指定好所选步骤的主办人!');
  	  return ;
  	}
  }
  var url = requestUrl + "/turnNext.act";
  var par = $('turnForm').serialize() + "&maxPrcs=" + (parseInt(prePrcsId) -1)  ;
  var json = getJsonRs (url , par);
  if (json.rtState == '0' ) {
    TurnNext_forwardPage();
  } else {
  	alert(json.rtMsrg);
  }
}
function TurnNext_forwardPage() {
  if (isManage) {
    location = contextPath + moduleContextPath + "/flowrun/manage/index.jsp?skin="+skin+"&sortId=" + sortId;
  } else {
    location = "../index.jsp?skin="+skin+"&sortId=" + sortId;
  }
}
function setFreeItem (i) {
  var contentDiv = "<div id='contentDiv'></div><div align=center>"
    	+ "<input class='SmallButtonW' type='button' value='关闭' onclick='winClose()'/></div>";
  var selectedStr = $('freeItem' + i).value;
  var selected = [];
  var disSelected = [];
  var aSelectedItem = selectedStr.split(',');
  var aItem = items.split(',');
  for(var z = 0 ;z < aItem.length ;z++){
    var itemTmp = aItem[z];
    if(itemTmp){
	  var isExist = false;
	  for(var j = 0 ; j < aSelectedItem.length;j++){
	    if(aSelectedItem[j] && aSelectedItem[j] == itemTmp){
	      isExist = true;
	      var tmp = {};
	      tmp.value = itemTmp;
	      tmp.text =  itemTmp;
	      selected.push(tmp);
	      break;
	    }
	  }
	  if(!isExist){
	    var tmp = {};
	    tmp.value = itemTmp;
	    tmp.text =  itemTmp;
	    disSelected.push(tmp);
	  }
    }
  }
  nowPrcs = i ;
  alertWin('设置可写字段', contentDiv  , 450, 300);
  new ExchangeSelectbox({containerId:'contentDiv'
	    ,selectedArray:selected
	    ,disSelectedArray:disSelected 
	    ,selectedChange:exchangeHandler
    }); 
}
function exchangeHandler (ids) {
  $('freeItem' + nowPrcs).value = ids;
}
function findId (str , s) {
  if (!str) {
    return false; 
  } else {
    var ss = str.split(",");
    for (var i = 0 ;i < ss.length ; i++) {
      var tmp = ss[i] ;
      if (tmp && tmp == s) {
        return true;
      }
    }
    return false;
  }
}