var parentWindowObj = window.dialogArguments;
var config = null;
var pageMgr = null;
var valueField = "";
var dataSql = "";
var findStr = "";


function doInit() {
  var selectDataA = parentWindowObj.$(ctrlId);
  
  var dataSrc=selectDataA.getAttribute("DATA_TABLE");
  var dataField=selectDataA.getAttribute("DATA_FIELD");
  var dataFieldName=selectDataA.getAttribute("DATA_FLD_NAME");
  var dataQuery=selectDataA.getAttribute("DATA_QUERY");
  
  
   dataSql = "select ";
   var ss = dataField.split("`");
  for (var i = 0 ;i < ss.length ;i++) {
    var s = ss[i];
    dataSql += s + ",";
  }
  if (ss.length > 0) {
    dataSql = dataSql.substring(0 ,dataSql.length - 2);
  }
  dataSql += " from " + dataSrc + " where 1=1 ";
  //设置sql语句
  //设置列表列  config = this.getConfig(dataFieldName);
  //设置查询的表单  setForm(dataQuery , dataFieldName , dataField);
  //回填数据
  valueField = itemStr;
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

function setForm(str , str2 , str3) {
  if (!str || str.indexOf("1") == -1) {
    $('form').hide();
    $('noForm').show();
    return ;
  }
  var s = str.split("`");
  var s2 = str2.split("`");
  var s3 = str3.split("`");
  var td = "";
  var count = 0;
  for (var i = 0 ;i < s.length ;i ++) {
    var tmp = s[i];
    if (tmp && tmp == "1") {
      var input = createField(s2[i] , s3[i]);
      td += "<td align=left>" + input + "</td>";
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
function createField(str , str2){
  var input = "";
  if (str) {
    input = "<span>" + str + ":</span>";
    input += "<input name='" + str2 + "' type='text'/>" ;
    findStr += str2 + ",";
  }
  return input;
}

function getConfig(str) {
  var url =  contextPath +moduleSrcPath+"/act/YHFormDataSelect/selectData.act";
  var cfgs = {
    dataAction: url,
    container: "container",
    paramFunc: getParameter,
    pageSize:10
  };
  var colums = new Array();
  var s = str.split("`");
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
  var queryParam = $("form2").serialize() + "&sqlId=" + dataSql.trim() + "&findStr=" + findStr.trim();
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
  var input = "<div align=center><input value='添加数据' class='SmallButtonW2' onclick='setFieldValue(\""+ value +"\")' type='button'/></div>";
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
      var id =  field[i];
      var node = parentWindowObj.$(id);
      if (node && (i < size)) {
        node.value = s[i];
      }
    }
  }
  window.close();
}