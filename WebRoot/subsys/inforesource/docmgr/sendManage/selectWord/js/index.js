var parentWindowObj = window.dialogArguments;
var selectedCtrl = null;
function doInit() {
  selectedCtrl = parentWindowObj.selectedCtrl;
  getWords("");
}

function getWords (queryStr) {
  $("rolesDiv").update("");
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/getWord.act";
  var json = getJsonRs(url,"wordKey=" + queryStr);
  if (json.rtState == '0') {
    addDiv(json.rtData);
  }
  setSelected(selectedCtrl.value.split(","))
}
function createField(str){
  var input = "";
  if (str) {
    var fieldVal = str.split(":");
    input = "<span>" + fieldVal[0] + ":</span>";
    input += "<input name='" + fieldVal[1] + "' type='text'/>" ;
    findStr += fieldVal[1] + ",";
  }
  return input;
}
function doQuery() {
  getWords ($('keyword').value);
}
function setSelected(selectedDept){
  for(var i = 0 ;i < selectedDept.length ;i++){
    var selectedDiv = $("Div-" + selectedDept[i]);
    if(selectedDiv){
      selectedDiv.isChecked = true;
      selectedDiv.className = "item select";
    }
  }
}
function addDiv(roles){
  var divs = $("rolesDiv");
  if(roles.length > 0 ){
    for(var i = 0 ; i < roles.length ; i++){
      var dept = roles[i];
      var div = createDiv(dept);
      divs.appendChild(div);
    }
  }else{
    $('hasRole').hide();
  $('noRole').show();
  }
}
function createDiv(role){
  var div = new Element('div',{'class':'item'}).update(role)
  div.id = "Div-" + role ;
  div.onmouseout = function(){
    if(!this.isChecked){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  div.onmouseover = function(){
    if(!this.isChecked){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  div.onclick = function(){
    var roleNameStr = selectedCtrl.value.trim();

    var roleName = this.innerHTML.trim();
    if(this.isChecked){
      selectedCtrl.value = getOutofStr(roleNameStr , roleName);
      this.isChecked = false;
      this.className = "item";
    }else{
      if(selectedCtrl.value){
        selectedCtrl.value += "," + roleName;
      }else{
        selectedCtrl.value = roleName;
      }
      this.isChecked = true;
      this.className = "item select";
    }
  }
  return div;
}
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
  if (strTmp && strTmp.lastIndexOf(",") == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}
