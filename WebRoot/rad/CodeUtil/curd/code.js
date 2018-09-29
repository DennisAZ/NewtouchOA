var selectHashMap = new Hash();
var selectShowHashMap = new Hash();
var radioHashMap = new Hash();
var radioShowHashMap = new Hash();
function loadData(tabId, url, tableNo) {
  var table = $(tabId);
 // alert(tableNo);
  var rtJson = getJsonRs(url, "tableNo= " + tableNo);
  bind2Table(table, rtJson.rtData);
}
function bind2Table(tab, Json) {
  var index = tab.rows.length;
  for ( var i = 0; i < Json.length; i++) {
    var valR = "";
    var row = tab.insertRow(index + i);
    if (!Json[i]) {
      continue;
    }
    var record = Json[i];
    for ( var property in record) {
      if(property == 'fieldName'){
        var cell = row.insertCell(0);
        cell.innerHTML = record[property];
        valR = record[property];
        cell.id = "fieldCell_" + (index + i);
      }
      if(property == 'fieldDesc'){
        var nameCell = row.insertCell(1);
        nameCell.ch = "1";
        nameCell.innerHTML = "<input type='text' class='SmallInput' id='nameCell_"
            + (index + i) + "' value='" + record[property] + "'>";
      }
    }
    //
    var widthCell = row.insertCell(2);
    widthCell.ch = "1";
    widthCell.innerHTML = "<input type='text' class='SmallInput' id='widthCell_"
        + (index + i) + "' value='100'>";
    //
    var isShowCell = row.insertCell(3);
    isShowCell.ch = "1";
    isShowCell.innerHTML = "<input type='checkbox'  id='isShowCell_"
        + (index + i) + "' value='1' checked >";
    //
    var isMustCell = row.insertCell(4);
    isMustCell.ch = "1";
    isMustCell.innerHTML = "<input type='checkbox' id='isMustCell_"
        + (index + i) + "' value='1'>";

    var domCell = row.insertCell(5);
    var sel = $C("select");
    sel.id = "domCell_" + (index + i);
    var opt1 = $C("option");
    opt1.value = "input";
    opt1.text = "Input";
    var opt2 = $C("option");
    opt2.value = "select";
    opt2.text = "Select";
    var opt3 = $C("option");
    opt3.value = "radio";
    opt3.text = "Radio";
    Event.observe(sel, 'change', changes.bind(sel, valR));

    try {
      sel.add(opt1, null); // standards compliant
      sel.add(opt2, null); // standards compliant
      sel.add(opt3, null); // standards compliant
    } catch (ex) {
      sel.add(opt1); // IE only
      sel.add(opt2); // IE only
      sel.add(opt3); // IE only
    }
    domCell.appendChild(sel);
  }
}

function $C(tag) {
  return document.createElement(tag);
}
function changes(value) {
  var el = this;
  //alert(this);
  var tval = el.options[el.options.selectedIndex].text;
  var methodN = "save" + tval + "Div(\'" + value + "\')";
 // alert(methodN);
 // eval(methodN);
}
function saveInputDiv(value) {
  var res = value.split("_");
  var reslut = "";
  for ( var i = 0; i < res.length; i++) {
    if (res[i]) {
      reslut += res[i].substr(0, 1).toUpperCase()
          + res[i].substr(1).toLowerCase();
    }
  }
  reslut = reslut.substr(0, 1).toLowerCase() + reslut.substr(1);
  selectShowHashMap.unset(reslut);
  selectHashMap.unset(reslut);
  radioShowHashMap.unset(reslut);
  radioHashMap.unset(reslut);
}

function toSelQueryString() {
  var param = "";
  var rec = selectHashMap.values();
  for ( var index = 0; index < rec.length; index++) {
    param += "&selectFilter=" + rec[index];
  }
  var rec2 = radioHashMap.values();
  for ( var index = 0; index < rec2.length; index++) {
    param += "&radioFilter=" + rec2[index];
  }
  return param;
}
function saveSelectDiv(value) {
  var Url = "/yh/rad/CodeUtil/curd/selectField.jsp"
  var text = getTextRs(Url);
  alertWin('select配置',text,500,400);
  $('field_name').focus();
  //htmlEditor();
  var res = value.split("_");
  var reslut = "";
  for ( var i = 0; i < res.length; i++) {
    if (res[i]) {
      reslut += res[i].substr(0, 1).toUpperCase()
          + res[i].substr(1).toLowerCase();
    }
  }
  reslut = reslut.substr(0, 1).toLowerCase() + reslut.substr(1);
  var json = selectShowHashMap.get(reslut);
  if (json) {
    bindJson2Cntrl(json);
  } else {
    $('field_name').value = reslut;
  }
}
function saveRadioDiv(value) {
  //createIframe();
  //  alert(value);
 // htmlEditor("/test/rad/client/codeautogener/rawtest/div2.jsp");
  var Url = "/yh/rad/CodeUtil/curd/radioField.jsp"
  var text = getTextRs(Url);
  alertWin('radio配置',text,500,400);
  var res = value.split("_");
  var reslut = "";
  for ( var i = 0; i < res.length; i++) {
    if (res[i]) {
      reslut += res[i].substr(0, 1).toUpperCase()
          + res[i].substr(1).toLowerCase();
    }
  }
  reslut = reslut.substr(0, 1).toLowerCase() + reslut.substr(1);
  var json = radioShowHashMap.get(reslut);
  if (json) {
    bindJson2Cntrl(json);
  } else {
    $('field_name').value = reslut;
  }
  $('field_name').focus();
}
function getFieldConfig(dom) {
  var table = $(dom);
  var result = "";
  for ( var i = 1; i < table.rows.length; i++) {
    if (result != "") {
      result += "/";
    }
    var fieldCell = $('fieldCell_' + i);
    var nameCell = $('nameCell_' + i);
    var widthCell = $('widthCell_' + i);
    var isShowCell = $('isShowCell_' + i);
    var isMustCell = $('isMustCell_' + i);
    var domCell = $('domCell_' + i);
    result += fieldCell.innerHTML + "," + nameCell.value + ","
        + widthCell.value + ",";
    if (isShowCell.checked) {
      result += "1" + ",";
    } else {
      result += "0" + ",";
    }
    if (isMustCell.checked) {
      result += "1" + ",";
    } else {
      result += "0" + ",";
    }
    result += domCell.value;
  }
 // alert(result);
  return result;
}
function inputs(dom1, dom2) {
  $(dom1).value = getFieldConfig(dom2);
}
function onSelectTable() {
  var record = newWindow("tableList.jsp");
//  var curr = document.getElementById("pre3").value;
  //selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
//  selectCode( {
//    sort : "1",
//    tableNo : "99999",
//    codeField : "代码",
//    nameField : "tableNo",
//    codeFieldNo : "编号",
//    nameFieldNo : "tableName",
//    filterField : "tableName",
//    filterValue : "",
//    currValue : curr,
//    orderBy : ""
//  });
  if(record){
    document.getElementById('pre3').value = record.tableNo;
    document.getElementById('tempTabNo').value = record.tableNo;
  }
}

function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  child = window.showModalDialog(url,
      '',
      'dialogWidth:523px;scroll:auto;dialogHeight:290px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;');
  return child;
}

function onSelectPackage(el) {
  var baseUrl;
  if ($(el).value) {
    baseUrl = $(el).value;
  } else {
    alert("请先选择工程！");
  }
}
function createIframe() {
  var newMask = document.createElement("div");
  newMask.id = "mDiv";
  newMask.style.position = "absolute";
  newMask.style.zIndex = "2";
  _scrollWidth = Math.max(document.body.scrollWidth,
      document.documentElement.scrollWidth);
  _scrollHeight = Math.max(document.body.scrollHeight,
      document.documentElement.scrollHeight);
  // _scrollHeight = Math.max(document.body.offsetHeight,document.documentElement.scrollHeight); 
  newMask.style.width = _scrollWidth + "px";
  newMask.style.height = _scrollHeight + "px";
  newMask.style.top = "0px";
  newMask.style.left = "0px";
  newMask.style.background = "#33393C";
  //newMask.style.background = "#FFFFFF"; 
  newMask.style.filter = "alpha(opacity=40)";
  newMask.style.opacity = "0.40";
  newMask.style.display = 'none';
  var objDiv = document.createElement("DIV");
  objDiv.id = "div1";
  objDiv.name = "div1";
  objDiv.style.width = "480px";
  objDiv.style.height = "200px";
  objDiv.style.left = (_scrollWidth - 480) / 2 + "px";
  objDiv.style.top = (200) / 2 + "px";
  objDiv.style.position = "absolute";
  objDiv.style.zIndex = "3";
  objDiv.style.display = "none";

  objDiv.style.border = "solid #0033FF 3px;";
  var frm = document.createElement("div");
  frm.id = "ifrm";
  frm.name = "ifrm";
  frm.style.position = "absolute";
  frm.style.width = "100%";
  frm.style.height = 180;
  frm.style.top = 20;
  frm.style.display = '';
  frm.frameborder = 0;
  objDiv.appendChild(frm);
  document.body.appendChild(newMask);
  newMask.appendChild(objDiv);
  //document.body.appendChild(objDiv);
}
function htmlEditor(Url) {
  var frm = document.getElementById("ifrm");
  var objDiv = document.getElementById("div1");
  var mDiv = document.getElementById("mDiv");
  mDiv.style.display = '';
  var text = getTextRs(Url);
  frm.innerHTML = text;
  objDiv.style.display = "";
}
function HideIframe(mDiv, objDiv) {
  mDiv.style.display = 'none';
  objDiv.style.display = "none";
}
function Hidediv() {
  var json = $('selectFilter').serialize(true);
  var res = "";
  for ( var i in json) {
    var val = ""
    if (json[i]) {
      val = json[i];
    }
    if (res) {
      res += ",\'" + i + "\':\'" + val + "\'";
    } else {
      res += "\'" + i + "\':\'" + val + "\'";
    }
  }
  selectHashMap.set(json["cntrlId"], "{" + res + "}");
  selectShowHashMap.set(json["cntrlId"], json);
//  alert( "select" + selectHashMap.inspect());
//  alert(selectShowHashMap.inspect());
  winClose();
  //HideIframe(document.getElementById('mDiv'), document.getElementById('div1'));
}
function Hidediv2() {
  var json = $('radioFilter').serialize(true);
  var res = "";
  for ( var i in json) {
    var val = ""
    if (json[i]) {
      val = json[i];
    }
    if (res) {
      res += ",\'" + i + "\':\'" + val + "\'";
    } else {
      res += "\'" + i + "\':\'" + val + "\'";
    }
  }
  radioHashMap.set(json["cntrlId"], "{" + res + "}");
  radioShowHashMap.set(json["cntrlId"], json);
 // alert( " radio : " + radioHashMap.inspect());
 // alert(radioShowHashMap.inspect());
  winClose();
 // HideIframe(document.getElementById('mDiv'), document.getElementById('div1'));
}