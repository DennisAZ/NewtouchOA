var requestUrl = contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct"

function doInit(){
  if(flowId){
    //yh/yh/core/funcs/workflow/act/YHFlowTypeAct/getEditMessage.act?flowId=1
    //设置显示，取分类，flow json对象，所属表单对象 ,以及部门名称， 下面是工作数量，已删除工作数量，决定是否取表单列表
    $('bodyDiv').style.overflow = 'auto';
    resizeDiv();
    document.body.onresize = resizeDiv;
    $('optionTitle').update("编辑流程基本属性 (该流程下共有<span id='workCount' style=''></span>个工作,其中<span id='delCount' style=''></span>个已删除)");
    $('flowIdSpan').update('(内部Id号：' + flowId + ")");
    var url = requestUrl + "/getEditMessage.act";
    var json = getJsonRs(url , "flowId=" + flowId);
    if(json.rtState == '0'){
      var flowType = json.rtData.flowType;
      
      setOptions($('flowSort') , json.rtData.sortList , flowType.flowSort);
      setOptions($('formId') , json.rtData.formList , flowType.formId);
      
      if (isAdmin) {
        setOptions($('deptId') , json.rtData.deptList , flowType.deptId);
      } else {
        $('deptId').value = uDeptId;
      }
      $('flowNo').value = flowType.flowNo;
      $('flowName').value = flowType.flowName;
      
      $('flowType').value = flowType.flowType;
      if($('flowType').value == '2'){
        type_change();
        $('freePreset').value = flowType.freePreset;
        $('isOperDesign').innerHTML = '<a href="setNewPriv.jsp?flowId='+ flowId +'&sortId='+ flowType.flowSort +'"><img src="'+ imgPath + '/node_user.gif"/>新建权限</a>';
      }
      
      
      
      $('freePreset').value = flowType.freePreset;
      $('freeOther').value = flowType.freeOther;
      $('flowDoc').value = flowType.flowDoc;
      $('flowDesc').innerHTML = flowType.flowDesc;
      $('autoName').value = flowType.autoName;
      $('autoNum').value = flowType.autoNum;
      $('autoLen').value = flowType.autoLen;
      $('autoEdit').value = flowType.autoEdit;
      $('listFldsStr').value = flowType.listFldsStr;
      var workCount = json.rtData.workCount;
      $('workCount').update(workCount);
      $('delCount').update(json.rtData.delCount);
      $('flowId').value = flowId;
      
      if(workCount > 0){
        $('flowType').disabled = true;
        $('formId').disabled = true;
      }
      var allFormItems = json.rtData.formItem.split(",");
      var listFlds = flowType.listFldsStr.split(",");
      createExchangeSelect(allFormItems , listFlds);
      $('wpiroot').show();
      $('flowNo').focus();
      var isSystem = flowType.isSystem;
      if (isSystem == '1') {
        $('delA').hide();
      }
    }
  }else{
    
    $('optionTitle').update("新建流程");
    var url = requestUrl + "/getAddMessage.act";
    var json = getJsonRs(url);
    if(json.rtState == '0'){
      //取分类，表单列表
      setOptions($('flowSort') , json.rtData.sortList);
      setOptions($('formId') , json.rtData.formList);
      
      if (isAdmin) {
        setOptions($('deptId') , json.rtData.deptList);
      } else {
        $('deptId').value = uDeptId;
      }
      $('flowNo').focus();
      $('formItemTr').hide();
    }
  }
}
function createExchangeSelect(allFormItems , listFilds){
  var selected = new Array();
  var disSelected = new Array();
  for(var i = 0 ;i < allFormItems.length ;i++){
    var itemTmp = allFormItems[i];
		var isExist = false;
  	for(var j = 0 ; j < listFilds.length;j++){
      if(listFilds[j] == itemTmp){
      	isExist = true;
      	var tmp = {};
      	tmp.value = itemTmp;
      	tmp.text = itemTmp;
      	selected.push(tmp);
     	 break;
    	}
  	}
  	if(!isExist){
      var tmp = {};
      tmp.value = itemTmp;
      tmp.text = itemTmp;
      disSelected.push(tmp);
  	}
	}
  new ExchangeSelectbox({containerId:'exchangeDiv', selectedArray:selected, disSelectedArray:disSelected ,isSort:false ,selectName:'selectedName',selectedChange:exchangeHandler}); 
} 
function exchangeHandler(ids){
  $('listFldsStr').value = ids;
}
function delOptions(el){
  el.innerHTML = "";
}
function setOptions(el,aOption,value){
  for(var i = 0 ;i< aOption.length ;i++){
    var option = new Element('option').update(aOption[i].text);
    if (aOption[i].value == value) {
      option.selected = true;
    }
    option.value = aOption[i].value;
    el.appendChild(option);
  }
}
function type_change(){
  var obj = $("freeOther");
  if($("flowType").value == 1){
    $("FREE_SET").hide();
    if(obj.options[1].value!=3){
       var myoption = document.createElement("option");
       myoption.value="3"  ;
       myoption.text="按步骤设置的经办权限委托";
       obj.options.add(myoption,1);
    }
  }else{
    $("FREE_SET").show();
    if(obj.options[1].value==3){
      obj.options.remove(1);
    }
  }
}
function showOrHide(id){
  if($(id).style.display == 'none'){
    $(id).style.display = '';
  }else{
    $(id).style.display = 'none';
  }  
}
function commit(){
  var url ;
  if (!$('flowSort').value) {
    myAlert('流程分类不能为空！' , 200 ,20);
    return ;
  }
  if (!$('formId').value) {
    myAlert('表单不能为空！' , 200 ,20);
    return ;
  }
  if (!$('flowName').present()) {
    myAlert('流程名不能为空！' , 200 ,20);
    $('flowName').focus();
    return;
  }
  var reg = /['"]/g;
  if ($('flowName').value.match(reg)) {
    alert("流程名称不能有\"'\"和\"\"\"字符！");
    $('flowName').focus();
    return false;
  }  
  if ($('flowDesc').value.match(reg)) {
    alert("流程说明不能有\"'\"和\"\"\"字符！");
    $('flowDesc').focus();
    return false;
  } 
  if ($('autoName').value.match(reg)) {
    alert("自动文号表达式不能有\"'\"和\"\"\"字符！");
    $('autoName').focus();
    return false;
  } 
  
  
  var reg1 = /[^\d]/g;
  var flowNo = $("flowNo");
  var str = flowNo.value;
  if (!str && str.match(reg1)) {
    alert("流程排序号只能为数字！");
    flowNo.focus();
    return false;
  } 
  var autoNum = $("autoNum");
   str = autoNum.value;
  if (!str && str.match(reg1)) {
    alert("自动编号计数器只能为数字！");
    autoNum.focus();
    return false;
  } 
  var autoLen = $("autoLen");
   str = autoLen.value;
  if (!str && str.match(reg1)) {
    alert("自动编号显示长度只能为数字！");
    autoLen.focus();
    return false;
  } 
  
  
  if(flowId){
    //修改
    url = requestUrl + "/updateFlowType.act";
  }else{
    //保存
    url = requestUrl + "/saveFlowType.act";
  }
  var json = getJsonRs(url , $('editForm').serialize());
  if(json.rtState == '0'){
    //alert(json.rtMsrg);
    myAlert(json.rtMsrg , 100 ,20);
    if(flowId){
      //修改
      parent.parent.leftFrame.location.reload();
    }else{
      //保存
      $('editForm').reset();
      parent.leftFrame.location.reload();
    }
  }
  
}

function delFlowType(){
  if(confirm("确定删除吗？\n这将删除以下数据：\n1,流程描述与步骤设置。\n2,依托于该流程的所有工作。")){
    var url =  contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct/delFlowType.act";
    var json = getJsonRs(url, "flowId=" + flowId);
    if(json.rtState == '0'){
     // alert(json.rtMsrg);
      myAlert(json.rtMsrg , 150 ,20);
      parent.parent.leftFrame.location.reload();
      history.back();
    //转到列表页
    }else{
    myAlert(json.rtMsrg , 150 ,20);
      //alert(json.rtMsrg);
    }
  }
}
function clear(){
  if(confirm("确定清空依托于该流程的所有工作数据吗?")) {
    var url =  contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct/empty.act";
    var json = getJsonRs(url, "flowId=" + flowId);
    if(json.rtState == '0'){
      //alert(json.rtMsrg);
      myAlert(json.rtMsrg , 150 ,20);
      location.reload();
      parent.parent.leftFrame.location.reload();
      //history.back();
    }else{
      alert(json.rtMsrg);
    }
  }
}
function clone() {
  var title = "流程克隆";
  var url = contextPath + "/core/funcs/workflow/flowtype/clone/index.jsp?flowId="+flowId;
  showModalWindow(url , title , "clone" ,500,300 , false , function () {parent.parent.leftFrame.location.reload();});
}
function myAlert(text , width , height) {
  var div = new Element("div").update(text);
  with (div.style) {
    backgroundColor = '#F08080';
    color = '#FFF';
    fontWeight = 'bold';
    position = 'absolute';
    textAlign = 'center';
    marginTop = '5px';
    fontSize = '12pt';
    border = '2px dotted #A020F0'
    zIndex = 99;
  }
  div.style.width = width  + "px" ;
  div.style.height = height  + "px" ;
  div.style.left = document.viewport.getDimensions().width/2 - width + "px";
  div.style.top  =  document.viewport.getDimensions().height/2 - height + "px"; 
  
  iframe = new Element("iframe");
  iframe.scrolling = "no";
  iframe.frameborder = "0"; 
  iframe.src = contextPath + "/core/inc/emptyshim.html";
  iframe.style.position = "absolute";
  iframe.style.filter = "alpha(opacity=40)";
  iframe.style.opacity = 0.4;
  iframe.style.position = "absolute";
  iframe.style.display = "block";
  iframe.style.zIndex = 10;
  
  iframe.width = width  + "px" ;
  iframe.height = height  + "px" ;
  iframe.style.left = (document.viewport.getDimensions().width/2 - width)  + "px";
  iframe.style.top  =  (document.viewport.getDimensions().height/2 - height) + 5 + "px"; 
  document.body.appendChild(iframe);
  document.body.appendChild(div);
  setTimeout(getOutDiv.bind(this,div , iframe) , 1500);
}
function getOutDiv() {
  var obj = arguments[0];
  var iframe  = arguments[1];
  document.body.removeChild(obj);
  document.body.removeChild(iframe);
}

