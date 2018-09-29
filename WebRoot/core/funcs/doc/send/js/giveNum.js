var years = [2010,2020];

var docStr = "";
function doInit() {
  createYears();
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getDocNum.act";
  var json = getJsonRs(url , "runId=" + runId);
  if (json.rtState == "0") {
    var data = json.rtData;
    var docwords = data.docWords;
   // $('docWord').value = data.docWord;
    createDocWords(docwords , data.docWordSeqId);
    $('docYear').value = data.docYear;
    $('docWordSeqId').value = data.docWordSeqId;
    $('docNum').value = data.docNum;
    docStr = data.indexStyle;
    setDoc(data.docWord , data.docYear, data.docNum)
  }
}
function createDocWords(docwords , now) {
  var docWord = $('docWord');
  for (var i = 0 ;i < docwords.length ; i++) {
    var op = new Element("option");
    op.value = docwords[i].seqId;
    op.update(docwords[i].name);
    if (now == docwords[i].seqId) {
      op.selected = true;
    }
    docWord.appendChild(op);
  }
}
function createYears() {
  var date = new Date();
  var nowYear = date.getFullYear();
  var docYear = $('docYear');
  for (var i = years[0] ;i <= years[1] ; i++) {
    var op = new Element("option");
    op.value = i;
    op.update(i);
    if (nowYear == i) {
      op.selected = true;
    }
    docYear.appendChild(op);
  }
}
function setDoc(docWord , docYear,docNum) {
  doc1 = docStr.replace("${文件字}",docWord);
  doc1 = doc1.replace("${年号}",docYear);
  doc1 = doc1.replace("${文号}",docNum);
  $('doc').value = doc1;
}
function getNum(docYear) {
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getNum.act";
  var json = getJsonRs(url , "docWord=" + $('docWordSeqId').value + "&docYear=" + docYear);
  if (json.rtState == "0") {
    var data = json.rtData;
    $('docNum').value = data;
    setDoc($('docWord').value , docYear, data)
  }
}
function getWordText() {
  var options = $("docWord").options;
  var selectValue = "";
  for (var i = 0 ;i < options.length;i++) {
    var op = options[i];
    if (op.selected) {
      selectValue = op.innerHTML;
    }
  }
  return selectValue;
}
function getWordNum(docWordSeqId) {
  var selectValue = getWordText();
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getNum.act";
  var json = getJsonRs(url , "docWord=" + docWordSeqId + "&docYear=" + $('docYear').value);
  if (json.rtState == "0") {
    var data = json.rtData;
    $('docNum').value = data;
    setDoc(selectValue , $('docYear').value, data)
  }
}
function sendNum() {
  var docNum = $('docNum').value;
  if (!isNumber(docNum)) {
    alert("编号只能为数字！");
    return;
  }
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/sendNum.act";
  var json = getJsonRs(url , $('form1').serialize());
  if (json.rtState == "0") {
    if (json.rtData) {
      alert("文号重复，请重新分配文号！");
    } else {
      alert("文号分配成功！");
      if (top && top.win) {
        top.win.closeGiveNumPage();
      } else {
        window.returnValue = '1';
        window.close();
      }
    }
  } 
}
function setDocNum(value) {
  var selectValue = getWordText();
  setDoc(selectValue , $('docYear').value, value)
}