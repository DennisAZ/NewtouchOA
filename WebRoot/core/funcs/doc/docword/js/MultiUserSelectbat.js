
function doInit(){
  getAccord();
  var parentWindowObj = window.dialogArguments;
  var wordRetNameArray = parentWindowObj["wordRetNameArray"];
  if (wordRetNameArray) {
    var wordCntrl = wordRetNameArray;
    Word = parentWindowObj.$(wordCntrl);
  }else {
	Word = parentWindowObj.$("word");
  }
  if (Word.value) {
    var strs = Word.value.split(splitStr);
    	getRightPal( '已选主题词', true , true , 0 );
        addWordDiv(strs , 0);///
        setSelected(Word.value.split(splitStr));
  } else{
	  getRightPal('已选主题词' , true , false , 0, "未选择主题词");
  }
}

function treeNodeClick(id){
	var url=contextPath+"/yh/core/funcs/doc/send/act/YHWordSelectAct/getWordBySort.act";
  var node = tree.getNode(id);
  if (node && node.extData) {
    var json = getJsonRs(url , 'deptId=' + id);
    if(json.rtState == '0'){
      $("right").update("");
      var users = json.rtData;
      var name = json.rtMsrg;
      if (users.length > 0) {
        getRightPal(name , true , true , 0 );
        addWordDiv(users , 0);
        setSelected(Word.value.split(splitStr));
      } else {
        getRightPal(name , true , false , 0 );
      }
    }
  }
}


function setSelected(selectedWord){
  for(var i = 0 ;i < selectedWord.length ;i++){
    var selectedDiv = $("Div-" + selectedWord[i]);
    if(selectedDiv){
      selectedDiv.isSelected = true;
      selectedDiv.className = "item select";
    }
  }
}

function addWordDiv(words , i){
  var divs = $("list-" + i);
  if(words.length > 0){
    for(var i = 0 ; i < words.length ; i++){
      var word = words[i];
      var div = createDiv(word);
      divs.appendChild(div);
    }
  }
}

function createDiv(word){
  var wordName = word
  tmp = wordName;
  var wordDiv = new Element('div',{'class':'item'}).update(tmp);
  wordDiv.id = "Div-" + wordName ;
  wordDiv.isSelected = false;
  wordDiv.onmouseout = function(){
    if(!this.isSelected){
      this.className = "item";
    }else {
      this.className = "item select";
    }
  }
  wordDiv.onmouseover = function(){
    if(!this.isSelected){
      this.className = "item hover";
    }else {
      this.className = "item select";
    }
  }
  wordDiv.onclick = function(){
    checkAllDept();
   
    var deptStr = Word.value;
    var deptDescStr = "";
    if (Word.tagName == 'INPUT') {
      deptDescStr =  Word.value; 
    } else {
      deptDescStr = Word.innerHTML.trim();
    }

    if(this.isChecked){
       Word.value = getOutofStr(deptStr , deptId);
       if (Word.tagName == 'INPUT') {
      	 Word.value = getOutofStr(deptDescStr , deptName);
       } else {
      	 Word.innerHTML = getOutofStr(deptDescStr , deptName);
       }

	    this.isChecked = false;
  	  //this.style.backgroundColor = '';
  	  this.className = "item";
    }else{
      if (DeptId.value.length > 0) {
        DeptId.value += "," + deptId;
      }else {
        DeptId.value = deptId;
      }
      if (DeptName.tagName == 'INPUT') {
        if(DeptName.value){
          DeptName.value += "," + deptName;
        }else{
          DeptName.value = deptName;
        }
      } else {
        if(DeptName.innerHTML){
          DeptName.innerHTML += "," + deptName;
        }else{
          DeptName.innerHTML = deptName;
        }
      }
      this.isChecked = true;
      this.className = "item select";
    }
	
  }
  return div;
}
function removeDiv(){
  var divs = $("deptsDiv");
  var divList = divs.getElementsByTagName('div');
  for (i=divList.length-1; i>=0; i--) {
    divs.removeChild(divList[i]);
  }
}
function selectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  if (DeptName.tagName == 'INPUT') {
    if(DeptName.value){
      var deptNameStr = DeptName.value;
    }else{
      var deptNameStr = "";
    }
  } else {
    if(DeptName.innerHTML){
      var deptNameStr = DeptName.innerHTML.trim();
    }else{
      var deptNameStr = "";
    }
  }
  if (deptNameStr) {
    deptNameStr += ",";
  }
  if (deptIdStr) {
    deptIdStr += ",";
  }
  for(var i = 0 ;i < divs.length ;i++){
		var div = divs[i];
		if(!div.isChecked){
		  var deptId = div.id.substr(4);
	 	  var deptName = div.innerHTML.trim().split("├")[1];
	 	  div.isChecked = true;
	    //div.style.backgroundColor = selectedColor;
	    div.className = "item select";  
	    deptIdStr += deptId + ',';
	    deptNameStr += deptName + ',';
		}
  }
  if (deptIdStr && deptIdStr.lastIndexOf(",") == deptIdStr.length - 1) {
    deptIdStr = deptIdStr.substring(0, deptIdStr.length - 1);
  }
  if (deptNameStr && deptNameStr.lastIndexOf(",") == deptNameStr.length - 1) {
    deptNameStr = deptNameStr.substring(0, deptNameStr.length - 1);
  }
  DeptId.value = deptIdStr;
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.innerHTML = deptNameStr;
  }
}
function disSelectedAll(){
  var divs = $('deptsDiv').getElementsByTagName('div');
  var deptIdStr = DeptId.value;
  if (DeptName.tagName == 'INPUT') {
    if(!DeptName.value){
      return ;
    }
  } else {
    if(!DeptName.innerHTML){
      return ;
    }
  }
  var deptNameStr = "" ;
  if (DeptName.tagName == 'INPUT') {
    deptNameStr = DeptName.value;
  } else {
    deptNameStr = DeptName.innerHTML.trim();
  }
  
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
  	if(div.isChecked){
  	  var deptId = div.id.substr(4);
   	  var deptName = div.innerHTML.trim().split("├")[1];
   	  deptIdStr = getOutofStr(deptIdStr , deptId);
      
   	  deptNameStr = getOutofStr(deptNameStr , deptName);
   	  div.isChecked = false;
   	  div.className = "item";
	}
  }
  DeptId.value = deptIdStr;
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = deptNameStr;
  } else {
    DeptName.innerHTML = deptNameStr;
  }
  
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

function comeBack(){
  url = contextPath + "/yh/core/funcs/orgselect/act/YHDeptSelectAct/getDeptIndent.act";
  var json = getJsonRs(url);
   if(json.rtState == "0"){
    $('title').update('全体部门');
    $('title').onclick = function() {
      
      selectedAllDept();
    }
    $('title').className = "clickable";
    removeDiv();
    var deptList = json.rtData;
    if (deptList.size() > 0 ) {
      $('addAll').show();
      $('disAll').show();
    }
  	addDiv(deptList);
    setSelected(DeptId.value.split(','));
  }
}
function selectedAllDept(){
  <% if (hasAllDept) {%>
  DeptId.value = 0;
  if (DeptName.tagName == 'INPUT') {
    DeptName.value = "全体部门";
  } else {
    DeptName.innerHTML = "全体部门";
  }
  window.close();
  <% }%>
}
function checkAllDept(){
  if(DeptId.value == 0 || DeptId.value == "ALL_DEPT"){
    DeptId.value = "";
    if (DeptName.tagName == 'INPUT') {
      DeptName.value = "";
    } else {
      DeptName.innerHTML = "";
    }
  }
}
