var baseContentUrl = contextPath +  "/core/funcs/workflow/flowhook"
var requestUrl = contextPath +  "/yh/core/funcs/workflow/act/YHFlowHookAct";
var hookData = null;

function doInit(){
  var jso = [{title:"基本属性", onload:showInfo.bind(window, "basic")
               , useTextContent:true
               ,imgUrl:imgPath + "/edit.gif"
               , contentUrl: baseContentUrl + "/basicSet.jsp" , useIframe:false}
               ,{title:"数据映射", onload:showInfo.bind(window, "data")
              , useTextContent:true
              ,imgUrl:imgPath + "/node_user.gif"
              , contentUrl: baseContentUrl + "/dataSet.jsp", useIframe:false}
            ,{title:"流程插件", onload:showInfo.bind(window, "plugin"), 
              useTextContent:true 
              ,imgUrl:imgPath + "/edit.gif"
              ,contentUrl:baseContentUrl + "/pluginSet.jsp", useIframe:false}
            ];
  tabs = buildTab(jso, 'contentDiv');
  var url = requestUrl + "/getHook.act";
  var editJson = getJsonRs(url, 'hid=' + $F('hid'));
  if (editJson.rtState == "0") {
    setDataToCtrl(editJson.rtData);
    $('system').value = editJson.rtData.system;
    hookData = editJson;
  }
}
function showInfo() {
  var id = arguments[0];
  if(id == 'data'){
    $('openeddata').value = 1;
    setOptions($("flows"),hookData.flows,hookData.rtData.flowId);
    $('mapIn').value = hookData.rtData.map;
    $('mapName').value = hookData.rtData.map;
    var module = hookData.module;
    var el = $('MODULE_STRING');
    for (var i in module) {
      var option = new Element('option').update(module[i]);
      option.value = i;
      el.appendChild(option);
    }
    var formItem = hookData.formItem;
    var childItem = formItem.split(",");
    var childItemTemp = [];
    var el = $('fldChild').getElementsByTagName('optgroup')[0];
    for(var j = 0 ;j < childItem.length ; j ++){
      if(childItem[j]){
        var option = new Element('option').update(childItem[j]);
        option.value = childItem[j];
        el.appendChild(option);
      }
    }
    $('field_sub').show();
    setRelationInDiv(hookData.rtData.map , module)
  }
  if(id == 'plugin'){
    $('openedplugin').value = 1;
    if (hookData.rtData.system == "1") {
      $('pluginDiv').hide();
    }
    $('plugin').value = hookData.rtData.plugin;
    var formItem = hookData.formItem;
    var childItem = formItem.split(",");
    var childItemTemp = [];
    var el = $('formItemSelect');
    for(var j = 0 ;j < childItem.length ; j ++){
      if(childItem[j]){
        var option = new Element('option').update(childItem[j]);
        option.value = childItem[j];
        el.appendChild(option);
      }
    }
    $('prcsInSet').value  = hookData.rtData.conditionSet;
    var prcsInSet = hookData.rtData.condition.split(',');
    
    for(var i = 0 ;i < prcsInSet.length;i++){
      var tmp = prcsInSet[i];
      if(tmp){
        createTr(1 , tmp);
      }
    }
  }
}
function setRelationInDiv(map , module){
  var mapArray = map.split(",");
  var count = 0;
  var newMap = "";
  var mapName = "";
  for (var i = 0 ;i < mapArray.length ;i++) {
    if (mapArray[i]) {
      var newArray = mapArray[i].split('=>');
      var name = newArray[0];
      var newName = module[name];
      newMap += "<span>" + newName + "=>" + newArray[1] 
                  + "<a href='javascript:;' onclick=\"map_del('" + count + "')\"><img src='"+imgPath+"/remove.png' title='删除映射关系' border='0' align='absmiddle'></a>,</span>";
      mapName += newName + "=>" + newArray[1] + ",";
      count++;
    }
  }
  $("mapName").value = mapName;
  $('relationIn').innerHTML = newMap;
}

function setDataToCtrl(data) {
  $('hmodule').value = data.module;
  if (data.system == "1") {
    $('hmodule').readOnly = true;
  }
  if (data.status == '1') {
    $('status1').checked = true;
  } else if (data.status == '2') {
    $('status2').checked = true;
  } else {
    $('status0').checked = true;
  }
  $('hname').value  = data.name;
  $('hdesc').value = data.desc;
  
  //$('flowId').value = data.flowId;
  //$('map').value = data.map;
  //$('condition').value = data.condition;
  //$('conditionSet').value  = data.conditionSet;
  //$('system').value  = data.system;
  
  
}
function checkForm(){
  if(!document.getElementById("status0").checked)
  { 
    if ($F("openedplugin") == '1') {
      var tab_in=$("prcsInTab");
      for(var i=1;i<tab_in.rows.length;i++)
      {
        $("prcsIn").value+=tab_in.rows[i].cells[1].innerText+ ",";
      } 
    }
    if ($F("openeddata") == '1') {
      var flag=0;
      var strs= new Array(); 
      strs=$("mapIn").value.split(","); 
      for (i=0;i<strs.length ;i++ )    
      {    
         var strs1= new Array(); 
         strs1=strs[i].split("=>"); 
         if(strs1[0]=="KEY")
            flag=1;
      } 
      if(flag==0)
      {
         alert("数据映射关系必须包含主键！"); 
         return (false);  
      }
    }
  }
  var url = requestUrl + "/updateHook.act";
  var json = getJsonRs(url, $('hookForm').serialize());
  if (json.rtState == '0') {
    alert("修改成功!");
    window.close();
  }
}
function setOptions(el,aOption,value){
  for(var i = 0 ;i< aOption.length ;i++){
    var option = new Element('option').update(aOption[i].flowName);
    if (value && aOption[i].flowId == value) {
      option.selected = true;
    }
    option.value = aOption[i].flowId;
    el.appendChild(option);
  }
}
function changeFlow(value){
  if(value == ''){
    $('field_sub').hide();
    $('fldChild').getElementsByTagName('optgroup')[0].innerHTML = "";
    $('relationIn').innerHTML = "";
    $('mapIn').value = "";
    $('mapName').value = "";
    //去掉map
  }else{
    //取得子流程formItem
    var requestUrl1 = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";
    var url = requestUrl1 + "/getFormItem.act";
    var json = getJsonRs(url , "flowId=" + value);
    var childItem = [];
    if(json.rtState != "0"){
      return ; 
    }    
    var sChildItem = json.rtData;
    hookData.formItem = sChildItem;
    var tmp = sChildItem.split(",");
    for(var i = 0 ;i < tmp.length ;i ++){
      if(tmp){
        var obj = {};
        obj.value = obj.text = tmp[i];
        childItem.push(obj);
      }
    }
    var group = $('fldChild').getElementsByTagName('optgroup')[0];
    group.innerHTML = "";
    for(var i = 0 ;i< childItem.length ;i++){
      
      var option = new Element('option').update(childItem[i].text);
      option.value = childItem[i].value;
      group.appendChild(option);
    }
    var el2 = $('formItemSelect');
    if (el2) {
      el2.update("");
      for(var j = 0 ;j < childItem.length ; j ++){
        if(childItem[j]){
          var option = new Element('option').update(childItem[j].text);
          option.value = childItem[j].value;
          el2.appendChild(option);
        }
      }
    }
    $('field_sub').show();
    $('relationIn').innerHTML = "";
    $('mapIn').value = "";
    $('mapName').value = "";
  }
}
function addRelation(){
  var option1 = "",option2 = "";
  var oFldParent = $('fldParent');
  var oFldChild = $('fldChild'); 
  if(oFldParent.selectedIndex  != -1 && oFldChild.selectedIndex != -1){
    option1 = oFldParent.options[oFldParent.selectedIndex].value;
    option2 = oFldChild.options[oFldChild.selectedIndex].value;
    mapadd(option1 + " => " + option2 + " , ");
  }else {
    alert("请选择子流程的字段");
  }    
}

function map_relation()
{
  var option1,option2,note;
  var oFLD_PARENT = $("FLD_PARENT");
  var oFLD_SUB = $("fldChild");
  if(oFLD_PARENT.selectedIndex!=-1&&oFLD_SUB.selectedIndex!=-1)
  {
    option1=oFLD_PARENT.options[oFLD_PARENT.selectedIndex].value;
    option2=oFLD_SUB.options[oFLD_SUB.selectedIndex].value;   
    note=oFLD_PARENT.options[oFLD_PARENT.selectedIndex].innerHTML;
    
    var data = option1+"=>"+option2+",";
      
    var notes = note+"=>"+option2+",";
    map_add(data,notes);
    }
  else {
      alert("请选择业务流程的字段");
  }

}
function map_add(data,notes)
{
  var oMap = $("mapIn");
  var aMap = $("mapName");
  if(arguments.length>0)
  {
      oMap.value+=data;
      aMap.value+=notes;
  }
  rel_array=oMap.value.split(",");
  name_array=aMap.value.split(",");
  $("relationIn").innerHTML="";
  for(i=0;i<rel_array.length-1;i++)
  {
    $("relationIn").innerHTML+="<span>"+name_array[i]+"<a href=\"javascript:\" onclick=\"map_del('"+i+"')\">"+
    "<img src=\""+imgPath +"/remove.png\" title=\"删除映射关系\" border=\"0\" align=\"absmiddle\"></a>,</span>";
  }
}
function map_del(index)
{
  var span = window.event.srcElement.parentNode.parentNode;
    span.parentNode.removeChild(span);
  var new_value="";
  var oMap = $("mapIn");
  rel_array=oMap.value.split(",");
  rel_array[index]="";
  for(i=0;i<rel_array.length;i++)
  { 
    if(rel_array[i]!="")
        new_value+=rel_array[i]+",";
  }
  oMap.value=new_value;
}
function changeCondition(value){
  if (value == "=" || value == "<>") {
  $('divCheck').style.display = "inline";
  if ($('checkType').checked) {
    $('divType').style.display =  "inline";
    $('divValue').style.display = "none";
  } else {
    $('divType').style.display = "none";
    $('divValue').style.display =  "inline";
  }
  } else {
    $('divCheck').hide();
    if ($('divType').style.display == "inline"){
      $('divType').hide();
      $('divValue').style.display = "inline";
    }
  }
}
function createSelected(select , items){
  
  for(var i = 0 ;i < items.length ;i++){
    var item = items[i];
    if (item) {
      var option  = new Element('option').update(item);
      option.value = item;
      $(select).appendChild(option);
    }
  } 
}
function changeType(checkbox){
  if(checkbox.checked){
    $('divValue').hide();
    $('divType').style.display = 'inline';
  }else{
    $('divType').hide();
    $('divValue').style.display = 'inline';
  }
}

function delRule(obj, flag){
  var tab;
  if(flag == 1){
    tab = $('prcsInTab');
  }else{
    tab = $('prcsOutTab');
  }
  var tr=obj.parentNode.parentNode;
  var no=tr.rowIndex;
  tab.deleteRow(tr.rowIndex);
  for(var i=no;i<tab.rows.length;i++){
    tab.rows[i].cells[0].innerText="[" + (tab.rows[i].rowIndex) + "]";
  }
}

function edit1(obj, flag){
  var td = obj.parentNode.parentNode.cells[1];
  var obj_edit = $("edit");
  var content = td.innerText;
  obj_edit.value = content;
  td.innerHTML = "";
  td.appendChild(obj_edit);
  obj_edit.style.display="";
  obj_edit.select()
  obj_edit.focus();
}

function updateEdit(edit)
{
  edit.style.display ="none";
  var td = edit.parentNode;
  document.body.appendChild(edit);
  td.innerText = edit.value;
} 
function addCondition(flag)
{
  var vITEM_NAME = $('formItemSelect').value;
  var vCONDITION = $('conditionSelect').value;
  var vITEM_VALUE = $('itemValue').value;

  if(vITEM_VALUE.indexOf("'")>=0)
  {
    alert("值中不能含有'号");
    return;
  }

  if(($('conditionSelect').value == "=" 
    || $('conditionSelect').value == "<>" ) 
    && $('checkType').checked==true)
  {
     if(vCONDITION == "=")
       vCONDITION = "==";
     else
       vCONDITION = "!==";

     vITEM_VALUE = $('itemType').value;
  }
  var str="'"+ vITEM_NAME + "'" + vCONDITION + "'" + vITEM_VALUE +"'";
  createTr(flag , str);
}
function createTr(flag ,str ){
  var tab;
  var img;
  var trClass;
  if(flag == 1){
    tab = $('prcsInTab');
    img = '<image style="cursor:pointer" src="'+imgPath+'/edit.gif" align="absmiddle" onclick="edit1(this,1)"> <image style="cursor:pointer" src="'+imgPath+'/delete.gif" align="absmiddle" onclick="delRule(this,1)">';
  } else {
    tab = $('prcsOutTab');
    img = '<image style="cursor:pointer" src="'+imgPath+'/edit.gif" align="absmiddle" onclick="edit1(this,0)"> <image style="cursor:pointer" src="'+imgPath+'/delete.gif" align="absmiddle" onclick="delRule(this,0)">';
  }
  var trs = tab.getElementsByTagName('tr');
  for(var i=1;i<trs.length;i++){
    var tabValue = trs[i].innerText;
    if(tabValue.indexOf(str) >= 0){
      alert("条件重复！");
      return;
    }
  }

  if(tab.rows.length%2 == 1 ) {
    trClass="TableLine1";
  }else{
    trClass="TableLine2";
  } 
  var newRow = tab.insertRow(-1);
  newRow.setAttribute("className" , trClass);
  var first = newRow.insertCell(-1);
  first.setAttribute("align" , "center");
  $(first).update("[" + (tab.rows.length-1) + "]");
  var second = newRow.insertCell(-1);
  $(second).update( str );
  var third = newRow.insertCell(-1);
  third.setAttribute("align","center");
  $(third).update(img);
}
function remove_plugin()
{
   $("plugin").value="";
}

function selPlugin(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHPluginAct/getPluginListTwo.act" ;
  loc_y=loc_x=200;
  loc_x=document.body.scrollLeft+event.clientX-event.offsetX;
  loc_y=document.body.scrollTop+event.clientY-event.offsetY;
  LoadDialogWindow(url,self,loc_x, loc_y, 380, 350);
}
function LoadDialogWindow(URL, parent, loc_x, loc_y, width, height){ 
  window.showModalDialog(URL,parent,"edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px",true);
}
