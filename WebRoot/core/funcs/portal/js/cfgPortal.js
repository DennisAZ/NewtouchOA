//var modData = {dataDef:{type:"url|sql" , url:"" , sql:""} , contentStyle{type:"grid|img|imgbox" , title:"新建模块"} , borderStyle:{style:"borderStyle-1" , toolButAutoHide:false ,borderAutoHide:false }};
//{ruleType:"" , imageAddress:"" , showText:"" , linkAddress:"" ,linkEvent:"" },{ruleType:"" , imageAddress:"" , showText:"" , linkAddress:"" ,linkEvent:"" },{ruleType:"" , imageAddress:"" , showText:"" , linkAddress:"" ,linkEvent:"" }
var modData = {dataDef:{},contentStyle:{type:"grid" , title:""} , borderStyle:{style:"1" , toolButAutoHide:false ,borderAutoHide:false }  , priv:{dept:"0" , user:"", role:"" }};
var ruleObjList = [];
var nowEditRule = -1;
var pageLength = 4;
var hasName = false;//有这个模块
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct"
var previewWindow = null;
function doInit(flag){
  previewWindow = preview.window;
  if (!isEdit) {
    $('dept').value = '0';
    $('deptDesc').update("全体部门");
  }
  if (flag) {
    previewWindow.refresh();
  } 
  if (isEdit) {
    var url = requestUrl + "/getModData.act";
    var json = getJsonRs(url , "id=" + pId);
    if (json.rtState == "0") {
      var script = json.rtData.script;
      var defData = json.rtData.defData;
      
      convertToModData(script , defData);
      if (!script.iframeSrc && !defData.sql && pageNo == 0) {
        $('noData').show();
        return ;
      }
      modData.priv = json.rtData.priv;
      setDataToCtrl();
      previewWindow.refresh();
    }
  } 
  $("div-" + pageNo).style.display = "";
}

function convertToModData(script , defData){
  if (defData.sql) {
    modData.dataDef = {type:"sql"  , sql:defData.sql};
    ruleObjList = defData.ruleList;
  } else {
    if (script.iframeSrc) {
      modData.dataDef = {type:"url"  , url:script.iframeSrc};
    } else {
      //modData.dataDef.type = "";
    }
  }
  
  modData.contentStyle.title = script.title;
  //其它项
  modData.borderStyle.style = script.cls ;
  modData.borderStyle.toolButAutoHide = script.autoHideBorders;
  modData.borderStyle.borderAutoHide = script.autoHideTools;
  //还有一项
  
  modData.contentStyle.type = "grid";
  if (script.items && script.items.length > 0 ) {
    modData.contentStyle.type = script.items[0].xtype;
    if (script.items[0].loader.param) {
      publishData = script.items[0].loader.param;
    }
  }
}
function setDataToCtrl() {
  //page-1
  if (modData.dataDef.type == "sql"){
    $('sql').update(modData.dataDef.sql);
    showList();
  } else {
    $('url').value = modData.dataDef.url;
    $('defineSqlDiv').hide();
    $('defineUrlDiv').show();
  }
  //page-2
  $('name').value = modData.contentStyle.title;
  $('oldName').value = modData.contentStyle.title;
  var styleType = modData.contentStyle.type;
  if (styleType == "grid") {
    $('styleType0').checked = true;
  } else if (styleType == "") {
    $('styleType1').checked = true;
  } else {
    $('styleType2').checked = true;
  }
  //page-3
  var borderStyle = modData.borderStyle.style;
  if (borderStyle == "1") {
    $('sel1').checked = true;
  } else if (borderStyle == "2") {
    $('sel2').checked = true;
  } else {
    $('sel3').checked = true;
  }
  if (modData.borderStyle.toolButAutoHide) {
    $("toolButAutoHide").checked = true;
  }
  if (modData.borderStyle.borderAutoHide) {
    $("borderAutoHide").checked = true;
  }
  //page-4
  var priv = modData.priv;
  $('user').value = priv.user;
  $('userDesc').update(priv.userDesc);
  $('dept').value = priv.dept;
  $('deptDesc').update(priv.deptDesc);
  $('role').value = priv.role;
  $('roleDesc').update(priv.roleDesc);
}
function updateModData() {
  if (!checkDefData()) {
    return ;
  }
  if (!checkNameNotEmpty(true)) {
    return ;
  }
  setPriv();
  previewWindow.save($F('pId'));
}
function checkDefData() {
  if ($("defineUrlDiv").style.display == "") {
    var value = $("url").value;
    if (!value) {
      alert("地址不能为空！");
      return false;
    }
    modData.dataDef = {type:"url" , url:value};
  } else {
    var value = $("sql").value;
    if (!value) {
      alert("sql语句不能为空！");
      return false;
    }
    if (ruleObjList.length < 1) {
      alert("请设置规则！");
      return false;
    }
    modData.dataDef = {type:"sql" , sql:value};
  }
  return true;
}
function setDefDate() {
  if (checkDefData()) {
    nextPage(1  , true);
  }
}
function checkNameNotEmpty(isOnlyCheck){
  var value = $("name").value;
  if (!value) {
    alert("模块名称不能为空！");
    if (!isOnlyCheck) {
      $("name").focus();
    }
    return false;
  }
  if (!isOnlyCheck) {
    nextPage(2);
  }
  return true;
}
function nextPage(pageNo , isReloadData) {
  for (var i = 0 ;i < pageLength; i++) {
  	var div = $("div-" + i);
  	if (pageNo == i) {
  	  div.style.display = "";
  	} else {
  	  div.style.display = "none";
  	}
  }
  //刷新预览
  previewWindow.refresh(isReloadData);
}
function titleBlur(name) {
  if (!name) {
    return ;
  }
  if (isEdit) {
    if (name == $F('oldName')) {
      return ;
    }
  }
  var url = requestUrl + "/checkName.act";
  var json = getJsonRs(url , "title=" + name);
  if (json.rtState == '0') {
    $("nameTip").update("");
    hasName = false;
  }  else {
    $("nameTip").update("此模块名已存在！");
    hasName = true;
  }
  //var radio =  getRadioValue("styleType"); 
  modData.contentStyle.title = name;
//刷新预览
  previewWindow.refresh();
}
function getRadioValue(radioName) {
  var radios = document.getElementsByTagName("input");
  for (var i= 0 ;i < radios.length; i++) {
    var r = radios[i];
    if (r.name==radioName && r.type == "radio") {
      return r.value;
    }
  }
}
function cfgContentType(radio){
  if (!radio.checked) {
    return ;
  }
  modData.contentStyle.type = radio.value;
  //刷新预览
  previewWindow.refresh();
}

function cfgBorderStyle(radio){
  if (!radio.checked) {
    return ;
  }
  modData.borderStyle.style = radio.value;
  
  //刷新预览
  previewWindow.refresh();
}
function cfgToolButAutoHide(checkbox) {
  modData.borderStyle.toolButAutoHide = checkbox.checked;
  previewWindow.refresh();
}
                                       
function cfgBorderAutoHide(checkbox) {
  modData.borderStyle.borderAutoHide = checkbox.checked;
  previewWindow.refresh();
}
function addRule(){
  clearRuleData();
  $('ruleDiv').show();
  nowEditRule = -1;
}
function gridRuleTypeClick(radio){
  if (radio.checked) {
    $('imageAddTr').hide();
  }
}
function imgRuleTypeClick(radio){
  if (radio.checked) {
    $('imageAddTr').show();
  }
}
function clearRuleData() {
  $("ruleDiv").hide();
  $('imageAddress').value = "";
  $("linkAddress").value = "";
  $('showText').value = "";
  $('linkEvent').innerHTML = "";
}
function saveRuleData() {
  //新建
  var ruleType1 = "grid";
  var showText1 = $("showText").value;
  var imageAddress1 = $("imageAddress").value ;
  if (!showText1) {
    alert("显示文本不能为空！");
    return ;
  }
  if ($("imgRuleType").checked) {
    ruleType1 = "img";
    if (!imageAddress1) {
      alert("图片地址不能为空！");
      return ;
    }
  }
  var linkAddress1 = $("linkAddress").value;
  var linkEvent1 =  $("linkEvent").value;
  var rule = {ruleType:ruleType1 , imageAddress:imageAddress1, showText:showText1 , linkAddress:linkAddress1 ,linkEvent:linkEvent1 };
  if (nowEditRule == -1) {
    ruleObjList.push(rule);
  //修改
  } else {
    ruleObjList[nowEditRule] = rule;
  }
  showList();
  clearRuleData();
}
function showList() {
  $('ruleListTbody').update("");
  if (ruleObjList.length < 1) {
    $('ruleList').hide();
    return;
  } 
  $('ruleList').show();
  for (var i = 0 ;i < ruleObjList.length; i++) {
    var rule = ruleObjList[i];
    addRuleTr(rule , i);
  }
}
function addRuleTr(rule , i) {
  var className = (i % 2 == 0) ? "TableLine1" :"TableLine2" ;
  var tr = new Element("tr",{"class":className});
  $('ruleListTbody').appendChild(tr);
  var td = new Element("td");
  var typeStr = (rule.ruleType == "grid") ? "列表规则":"图片列表规则";
  tr.appendChild(td);
  td.update(typeStr);
  td = new Element("td");
  tr.appendChild(td);
  td.update(rule.showText);
  td = new Element("td");
  var imageStr = (rule.ruleType == "grid")  ? "": rule.imageAddress ;
  tr.appendChild(td);
  td.update(imageStr);
  td = new Element("td");
  tr.appendChild(td);
  td.update(rule.linkAddress);
  td = new Element("td");
  tr.appendChild(td);
  var op = "&nbsp;<img src='img/edit.gif' onclick='editRule("+i+")' title='编辑'/>&nbsp;"
      + "<img src='img/delete.gif' onclick='delRule("+i+")' title='删除'/>"
  td.update(op);
}

function editRule(i) {
  nowEditRule = i;
  var rule = ruleObjList[i];
  if (rule.ruleType == "grid") {
    $('gridRuleType').checked = true;
  } else {
    $('imgRuleType').checked = true;
  }
  $('imageAddress').value = rule.imageAddress;
  $("linkAddress").value = rule.linkAddress;
  $('showText').value = rule.showText;
  $('linkEvent').update( rule.linkEvent);
  $("ruleDiv").show();
}
function delRule(ii) {
  if (confirm("确认删除此规则吗？")) {
    var newList = [];
    for (var i = 0 ;i < ruleObjList.length; i++) {
      if (i != ii) {
        newList.push(ruleObjList[i]);
      }
    }
    ruleObjList = newList;
    showList();
  }
}
function setPriv() {
  modData.priv.user = $F("user");
  modData.priv.priv = $F("role");
  modData.priv.dept = $F("dept");
}
function save() {
  setPriv();
  previewWindow.save();
}
