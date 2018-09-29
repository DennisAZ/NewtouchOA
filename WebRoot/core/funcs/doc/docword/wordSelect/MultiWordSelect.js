var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";
var Word;
var menu = null;
var tree =  null;
var defaultIndex = "";

function getAccord(){
  var data = {
  panel:'left',
  data:[{title:'主题词选择', action:getTree}]
   };
  menu = new MenuList(data);
  defaultIndex = menu.getContainerId(1);
  menu.showItem(this,{},1);
  getTree();
}
function getTree(){
  $(defaultIndex).update("");
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHWordSelectAct/getTree.act?id=";
  var config = {bindToContainerId:defaultIndex
      , requestUrl:url
      , isOnceLoad:false
      , checkboxPara:{isHaveCheckbox:false}
      , linkPara:{clickFunc:treeNodeClick}
      , isUserModule:true
    };
  tree = new DTree(config);
  tree.show(); 
}

function doInit(){
  getAccord();
  var parentWindowObj = window.dialogArguments;
  var wordRetNameArray = parentWindowObj["wordRetNameArray"];
  if (wordRetNameArray) {
    var wordCntrl = wordRetNameArray;
    Word = parentWindowObj.$(wordCntrl);
  }else {
	Word = parentWindowObj.$("word");
  }  if (Word.value) {
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
  wordDiv.onclick = function(){    if(this.isSelected){
      if (!isSingle) { 
        selectUser(wordName);
      } else {
        Word.value = "";
      }
      this.isSelected = false;
      this.className = "item";
    }else{
      if (!isSingle) { 
        disSelectUser(wordName);
        this.isSelected = true;
        this.className = "item select";
      } else {
        close();
      }
    }
  }
  return wordDiv;
}
function selectUser(wordName) {
  var wordDescStr = "";
  if (Word.tagName == "INPUT") {
	  wordDescStr = Word.value;
    Word.value = getOutofStr(wordDescStr , wordName);
  } else {
	  wordDescStr = Word.innerHTML.trim();
    Word.innerHTML = getOutofStr(wordDescStr , wordName);
  }
}
function disSelectUser(wordName) {
  var wordDescStr = "";
  if (Word.tagName == "INPUT") {
	  wordDescStr = Word.value;
  } else {
	  wordDescStr = Word.innerHTML.trim();
  }
  if (Word.tagName == "INPUT") {
    if (Word.value) {
    	Word.value += splitStr + wordName;
    } else {
    	Word.value =  wordName;
    }
  } else {
    if(Word.innerHTML){
    	Word.innerHTML += splitStr + wordName;
    }else{
    	Word.innerHTML = wordName;
    }
  }
}
function selectedAll(i){
  var divs = new Array();
  if (i != undefined) {
    divs = $('list-' + i).getElementsByTagName('div');
  } else {
    var divs1 = $('right').getElementsByTagName('div');
    for (var i = 0 ;i < divs1.length ;i ++) {
      var tmp = divs1[i];
      if ( tmp.id 
          && tmp.id.indexOf('list-') != -1) {
        var div2 = tmp.getElementsByTagName('div');
        for(var j = 0 ; j < div2.length ;j ++) {
          divs.push(div2[j]);
        }
      }
    }
  }
  var wordStr = Word.value;
  if (wordStr) {
	  wordStr += splitStr;
  }
  for(var i = 0;i < divs.length ;i++){
    var div = divs[i];
    if(!div.isSelected){
      var wordId = div.id.substr(4);
      div.isSelected = true;
      div.className = "item select";
      wordStr += wordId + splitStr;
    }
  }
  if (wordStr && wordStr.lastIndexOf(splitStr) == wordStr.length - 1) {
	  wordStr = wordStr.substring(0, wordStr.length - 1);
  }
  Word.value = wordStr;
}

function disSelectedAll(i){
  var divs = new Array();
  if (i != undefined) {
    divs = $('list-' + i).getElementsByTagName('div');
  } else {
    var divs1 = $('right').getElementsByTagName('div');
    for (var i = 0 ;i < divs1.length ;i ++) {
      var tmp = divs1[i];
      if ( tmp.id 
          && tmp.id.indexOf('list-') != -1) {
        var div2 = tmp.getElementsByTagName('div');
        for(var j = 0 ; j < div2.length ;j ++) {
          divs.push(div2[j]);
        }
      }
    }
  }
  var wordStr = Word.value;
  for(var i = 0 ; i< divs.length ;i++){
    var div = divs[i];
    if(div.isSelected){
      var wordId = div.id.substr(4);
      wordStr = getOutofStr(wordStr , wordId);
      div.isSelected = false;
      div.className = "item";
    }
  }
  Word.value = wordStr;
 
}

function getOutofStr(str, s){
  var aStr = str.split(splitStr);
  var strTmp = "";
  var j = 0 ;
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i] && (aStr[i] != s || j != 0)){
      strTmp += aStr[i] + splitStr;
    }else{
      if(aStr[i] == s){
        j = 1;
      }  
    }
  }
  if (strTmp && strTmp.lastIndexOf(splitStr) == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}

function getRightPal(title , isHaveAll , hasData , i , tip){
  var right = $('right');
  var tDiv;
  if (i == 0) {
    tDiv = new Element("table", {
      "class": "TableTop",
      "width": "100%"
    });
    
    var tr = tDiv.insertRow(0);
    var tdl = tr.insertCell(0);
    var td = tr.insertCell(1);
    var tdr = tr.insertCell(2);
    tdl.className = "left";
    td.className = "center";
    tdr.className = "right";
    td.innerHTML = title;
  }
  else {
    tDiv = new Element("div" , {'class' : 'header'}).update(title);
  }
  tDiv.id = "title" + i;
  right.appendChild(tDiv);
  if (hasData) {
    var hasData = new Element("div", {"class": "list"});
    hasData.id = "hasData-" + i;  
    right.appendChild(hasData);
    if (isHaveAll && !isSingle) {
      var addAll = new Element("input" , {type: "button", 'class' : 'BigButtonB', value: "全部添加"});
      addAll.onclick = function () {
        selectedAll(i);
      }
      var disAll = new Element("input" , {type: "button", 'class' : 'BigButtonB', value: "全部删除"});
      disAll.onclick = function () {
        disSelectedAll(i);
      }
      var d = new Element("div", {"class": "op"});
      hasData.appendChild(d.insert(addAll).insert(disAll));
    }
    var list = new Element("div");
    list.id = "list-" + i;
    hasData.appendChild(list);
  } else {
     if (!tip) {
       tip = "未定义主题词";
     }
     var noData = new Element("div" , {'color' : 'red'}).update(tip);
     noData.style.fontSize = "10pt";
     noData.style.paddingTop = "5px";
     right.appendChild(noData);
  }
}

function doSearch(value) {
  $('right').update("");
  var url = contextPath+"/yh/core/funcs/doc/send/act/YHWordSelectAct/getWordBySearch.act";
  var json = getJsonRs(url , "userName=" + encodeURI(value));
  tip = "未查询到所需主题词";
  if (json.rtState == '0') {
    var us = json.rtData ;
    if (us.length > 0) {
      getRightPal("主题词查询" , true , true , 0 , tip);
      addWordDiv(us , 0);
      setSelected(Word.value.split(','));
    } else {
      getRightPal("主题词查询" , true , false , 0 ,tip );
    }
  }
}
