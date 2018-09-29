//来访类别
function getVisitType(selectObj){
  var requestURL = contextPath + "/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_VISIT_TYPE"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
//项目类别
function getActiveType(selectObj){
  var requestURL = contextPath + "/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_ACTIVE_TYPE"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
//国家
function selectCounrty(retArray,moduleId,privNoFlag,privOp){
  roleRetNameArray=retArray;
  var url=contextPath+"/subsys/oa/profsys/getCountry.jsp";
  var has=false;if(moduleId){url+="?moduleId="+moduleId;
  if(!privNoFlag){
    privNoFlag=0;
  }
  url+="&privNoFlag="+privNoFlag;has=true;}
  if(privOp){
    if(has){
      url+="&privOp="+privOp;
    }else{
      url+="?privOp="+privOp;
    }
  }
 openDialog(url,280,400);
}
//选择框
function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
}
function check_all(){
  
  var t =document.getElementsByName("email_select");
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").item(i).checked=true;
    }else{
      document.getElementsByName("email_select").item(i).checked=false;
    }
  }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").checked=true;
    }else{
      document.getElementsByName("email_select").checked=false;
    }
  }
}
function check_one(el){
   if(!el.checked)
      document.getElementsByName("allbox")[0].checked=false;
}
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  if(i==0) {
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}



function getTotal(table,pYxTotal,pTotal){
  //合计
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="9";
  mynewcell.innerHTML = "&nbsp;合计：";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align="center";
  mynewcell.innerHTML = pYxTotal;

  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align="center";
  mynewcell.innerHTML = pTotal;

  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align="center";
  mynewcell.colSpan="2";
}
function insertTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='hidden' name='pYxTotalIn' id='pYxTotalIn' value = '0'>"
    +"<input type='hidden' name='pAllTotalIn' id='pAllTotalIn' value = '0'><input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="12";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label>&nbsp"
    + "  <input type='button' value='汇总打印' class='BigButton' onclick='openPrint();' title='打印所选工作事物'></input>";

}
function insertOutTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='hidden' name='pYxTotalIn' id='pYxTotalIn' value = '0'>"
    +"<input type='hidden' name='pAllTotalIn' id='pAllTotalIn' value = '0'><input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="12";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label>&nbsp"
    + "  <input type='button' value=' 汇总打印 ' class='BigButton' onclick='printMail();' title='打印所选工作事物'></input>";

}
function insertActiveTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='hidden' name='pYxTotalIn' id='pYxTotalIn' value = '0'>"
    +"<input type='hidden' name='pAllTotalIn' id='pAllTotalIn' value = '0'><input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="11";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label>&nbsp;"
    + "  <input type='button' value='汇总打印' class='BigButton' onclick='openPrint();' title='打印所选工作事物'></input>";

}