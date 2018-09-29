var parentWindowObj = window.dialogArguments;
var config = null;
var pageMgr = null;
var valueField = "";
var dataSql = "";
var findStr = "";


function doInit() {
  var selectDataA = parentWindowObj.$('selectData');
  var flowId =  parentWindowObj.flowId;
  var formId = "";
  if (!flowId) {
    formId = parentWindowObj.formId;
    flowId = "";
  }
  //设置sql语句
  dataSql = selectDataA.sqlid;
  //设置列表列
  var columnName = selectDataA.columnname;
  config = this.getConfig(columnName);
  //设置查询的表单
  var findItem = selectDataA.finditem;
  setForm(findItem);
  //回填数据
  valueField = selectDataA.comeback;
  
  var url =  contextPath +moduleSrcPath+"/act/YHPluginAct/getField.act";
  var json = getJsonRs(url , "valueField=" + valueField + "&flowId=" + flowId + "&formId=" + formId) ;
  if (json.rtState == '0') {
    valueField = json.rtData;
  } 
  doQuery();
}
function doQuery() {
  if(!pageMgr){
    pageMgr = new YHJsPage(config);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('container').style.display = "";
    $('msrg').style.display = "none";
  }else{
    WarningMsrg('没有检索到数据!', 'msrg','info');
    $('msrg').style.display = "";
    $('container').style.display = "none";
  }
}

/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"260\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr> <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function setForm(str) {
  if (!str) {
    $('form').hide();
    $('noForm').show();
    return ;
  }
  var s = str.split(",");
  var td = "";
  var count = 0;
  for (var i = 0 ;i < s.length ;i ++) {
    var tmp = s[i];
    if (tmp) {
      var input = createField(tmp);
      td += "<td>" + input + "</td>";
      count++ ;
    } 
  }
  $('fiTr').insert(td);
  
  var queryBut = new Element("input" , {'type':'button','value':"查询" , 'class':'SmallButton'});
  queryBut.onclick = doQuery;
  var td1 = new Element("td");
  td1.align = 'right';
  td1.appendChild(queryBut);
  $('opTr').colspan = count;
  $('opTr').appendChild(td1);
  $('form').show();
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

function getConfig(str) {
  var url =  contextPath +moduleSrcPath+"/act/YHPluginAct/selectData.act";
  var cfgs = {
    dataAction: url,
    container: "container",
    paramFunc: getParameter
  };
  var colums = new Array();
  if (!str) {
    str = "";
  }
  var s = str.split(",");
  for (var i = 0 ;i < s.length ;i ++) {
    var tmp = s[i];
    if (tmp) {
      var col = {type:"data", name:'col' + i ,width:150, text:tmp , render:centerRender};
      colums.push(col);
    }
  }
  var operate =  {type:"selfdef", text:"操作",width:100, render:opRender};
  colums.push(operate);
  cfgs.colums = colums;
  return cfgs;
}

function getParameter(){
  var tmp1 = dataSql;
  var tmp2 = findStr;
  if (!tmp1) {
    tmp1 = "";
  }
  if (!tmp2) {
    tmp2 = "";
  }
  var queryParam = $("form2").serialize() + "&sqlId=" + tmp1.trim() + "&findStr=" + tmp2.trim();
  return queryParam;
}
/**
 * 操作描画器
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opRender(cellData, recordIndex, columIndex){
  var value = this.getCellData(recordIndex,"value");
  var re1 = /\'/gi;
  value = value.replace(re1,"&lsquo;");
  var input = "<div align=center><input value='选择数据' class='SmallButton' onclick='setFieldValue(\""+ value +"\")' type='button'/></div>";
  return input;
}
function centerRender(cellData, recordIndex, columIndex){
  cellData = "<div align=center>" + cellData + "</div>";
  return cellData;
}
function setFieldValue(str) {
  var field = valueField.split(",");
  var s = str.split("++");
  var size = s.length;
  for (var i = 0 ;i < field.length ;i ++) {
    if (field[i]) {
      var id = 'DATA_' + field[i];
      var node = parentWindowObj.$(id);
      if (node && (i < size)) {
        node.value = s[i];
      }
    }
  }
  close();
}