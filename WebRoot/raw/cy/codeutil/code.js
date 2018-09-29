function loadData( tabId, url, tableNo){
  var table = $(tabId);
  //alert(tableNo);
  var rtJson = getJsonRs( url, "tableNo= " + tableNo);
  bind2Table(table,rtJson.rtData);
  //alert(getFieldConfig(tabId));
}
function bind2Table(tab,Json){
  var index = tab.rows.length;
  for(var i = 0 ; i < Json.length ; i++ ){
    var row = tab.insertRow(index + i);
    if(!Json[i]){
      continue;
    }
    var record = Json[i];
    for(var property in record){
      //alert( record[property]);
      var cell = row.insertCell(0) ;
      cell.innerHTML = record[property];
      cell.id = "fieldCell_" + (index + i );
      var nameCell = row.insertCell(1) ;
      nameCell.ch = "1";
      nameCell.innerHTML = "<input type='text' class='SmallInput' id='nameCell_" + (index + i ) + "' value='" + cell.innerHTML + "'>";
    }
    //
    var widthCell = row.insertCell(2) ;
    widthCell.ch = "1";
    widthCell.innerHTML = "<input type='text' class='SmallInput' id='widthCell_" + (index + i ) + "' value=''>";
    //
    var isShowCell = row.insertCell(3) ;
    isShowCell.ch = "1";
    isShowCell.innerHTML = "<input type='checkbox'  id='isShowCell_" + (index + i ) + "' value='1'>";
    //
    var isMustCell = row.insertCell(4) ;
    isMustCell.ch = "1";
    isMustCell.innerHTML = "<input type='checkbox' id='isMustCell_" + (index + i ) + "' value='1'>";
  }
}
function getFieldConfig(dom){
  var table = $(dom);
  var result = "" ;
  for(var i = 1 ; i < table.rows.length ; i++) {
    if(result != ""){
      result += "/";
    }
    var fieldCell = $('fieldCell_' + i);
    var nameCell = $('nameCell_' + i);
    var widthCell = $('widthCell_' + i);
    var isShowCell = $('isShowCell_' + i);
    var isMustCell = $('isMustCell_' + i);
    result += fieldCell.innerHTML + ","
           + nameCell.value + ","
           + widthCell.value + ",";
    if(isShowCell.checked){
      result += "1" + ",";
    }else {
      result += "0" + ",";
    }
    if(isMustCell.checked){
      result += "1";
    }else {
      result += "0"
    }
  }
  return result;
}
function inputs(dom1,dom2){
  $(dom1).value = getFieldConfig(dom2);
}
function onSelectTable(){
  var curr = document.getElementById("pre3").value; 
  //selectCode ({sort:1,tableNo:"",codeField:"",nameField:"",filterField:"",filterValue:"",currValue:"",orderBy:""});
  selectCode ({sort:"1",tableNo:"99999",codeField:"代码",nameField:"tableNo",codeFieldNo:"编号",nameFieldNo:"tableName",filterField:"tableName",filterValue:"",currValue:curr,orderBy:""});
  $('tempTabNo').value = document.getElementById("pre3").value; 
}
function onSelectPackage(el){
  var baseUrl;
  if($(el).value){
   baseUrl = $(el).value;
   
  }else{
    alert("请先选择工程！");
  }
}