/**
 * 页面跳转
 * @param url
 * @return
 */
function goToUrl(url){
  location = url;
}

function toSaveModel(){
  var url = contextPath + "/core/funcs/system/wordmodel/addModel.jsp";
  goToUrl(url);
}
/**
 * 保存套红模板
 * @return
 */
function saveWordModel(){
  var url = contextPath + "/" ;
  var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    location  = "";
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 显示日期控件
 * 
 * @return
 */
function showCalendar(cntrlId, isHaveTime, imgId) {
  var beginParameters = {
    inputId : cntrlId,
    property : {
      isHaveTime : isHaveTime
    },
    bindToBtn : imgId
  };
  new Calendar(beginParameters);
}
/**
 * 清除类容
 * 
 * @param ctrlId
 * @param ctrlIdDesc
 * @return
 */
function Clear() {
  var args = $A(arguments);
  for ( var i = 0; i < args.length; i++) {
    var cntrl = $(args[i]);
    if (cntrl) {
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML = '';
      } else {
        cntrl.value = '';
      }
    }
  }
}
/**
 * 编辑模板文件
 * @param seqId
 * @return
 */
function editLogic(seqId){
  var url = contextPath + "/core/funcs/system/wordmodel/addModel.jsp?seqId=" + seqId;
  goToUrl(url);
}
/**
 * 
 * @param seqId
 * @return
 */
function showModel(seqId){
  var url = contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/showModel.act";
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    var privStr = rtJson.rtData.privStr;
    var privs = privStr.split("|");
    var user = privs[0];
    var dept = privs[1];
    var role = privs[2];
    $('user').value = user;
    $('dept').value = dept;
    $('role').value = role;
    if(user){
      bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"} ]);
    }
    if(dept != 0 && dept != "ALL_DEPT"){
      bindDesc([ {cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"} ]);
    }else if(dept == "0" || dept == "ALL_DEPT"){
      $('deptDesc').value = "全体部门"
    }
    if(role){
      bindDesc([ {cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"} ]);
    }
    if(rtJson.rtData.attachmentName){
      attachMenuUtil("attachmentNameShow","wordModel",null,rtJson.rtData.attachmentName,rtJson.rtData.attachmentId,true);
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 删除模板文件
 * @param seqId
 * @return
 */
function deleteLogic(seqId){
  var url = contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/deleteModel.act" ;
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "0"){
    alert("删除成功");
  }else{
    alert("删除失败:" + rtJson.rtMrsg);
  }
}
/**
 *显示权限范围
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function showPriv(cellData, recordIndex, columIndex){
  var data = "";
  var result = "<div title= \"";
  var title = "";
  var privDesc = "";
  var privs = cellData.split("|");
   for(var i = 0 ; i <= privs.length ; i++){
     var privName = "";
     if(!privs[i]){
        continue;
      }
     switch(i){
       case 0 :
         privName = "人员";
         privDesc = getDesc([{codes:privs[i],desDef:"PERSON,SEQ_ID,USER_NAME"}]);
         break; 
       case 1 :
         privName = "部门";
         if(privs[i] == 0 || privs[i] == "ALL_DEPT"){
           privDesc = "全体部门";
         }else{
           privDesc = getDesc([{codes:privs[i],desDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
         }
         break; 
       case 2 : 
         privName = "角色";
         privDesc = getDesc([{codes:privs[i],desDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
         break; 
     }
     title += privName +  "：" + privDesc + "\n";
     if(privDesc.length >= 20){
       var temp = privDesc.substring(0,20).lastIndexOf(",");
       if(temp){
         privDesc = privDesc.substring(0,temp) + "...";
       }
     }
     data += "<font color=#0000FF><b>" + privName +  "：</b></font>" + privDesc+ "<br>";
   }
  result += title + "\">" + data + "</div>";
  return result;
}
function getAllDept(){
  var url = contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/getAllDept.act" ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    return "";
  }
}
/**
 * 把ID转换成名称

 * 
 * queryParam [{codes, desDef}, ...]
 */
function getDesc(paramArray) {
  var queryParam = "";
  for (var i = 0; i < paramArray.length; i++) {
    var param = paramArray[i];
    var codes = param.codes;
    if (!codes) {
      continue;
    }
    if (queryParam.length > 0) {
      queryParam += "/";
    }
    queryParam += codes + ";" + param.desDef;
  }
  if (!queryParam) {
    alert("没有输入有效的代码转换名称参数！");
    return;
  }
  var rtJson = getJsonRs(contextPath + "/yh/core/act/YHCode2DescAct/code2Desc.act", "queryParam=" + queryParam);
  var codeData = rtJson.rtData;
  var dataIndex = 0;
  for (var i = 0; i < paramArray.length; i++) {
    var param = paramArray[i];
    var codes = param.codes;
    if (!codes) {
      continue;
    }
    var cntrlData = codeData[dataIndex++];
     // 2010.1.19 解决没有value属性的控件加载问题
     return $H(cntrlData).values().toString();
  }
}

function isValidDateStrTime(str) { 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  var strArray = str.trim().split(" "); 
  if(strArray.length!=2){ 
    return false; 
  }else{ 
    if(!isValidDateStr(strArray[0])||strArray[1].match(re1) == null || strArray[1].match(re2) != null){ 
      return false; 
    } 
   } 
  }
/** 
* 判断是否为合法的日期串yyyy-MM-dd 

* 
* @str 日期串 

*/ 
function isValidDateStr(str) { 
  if (!str) { 
    return; 
   } 
   var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
   if (r == null) { 
     return false; 
    } 
   if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) { 
     return false; 
    } 
    var d = new Date(r[1], r[3]-1, r[4]); 
    return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]); 
}
function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}
function cutDate(dateStr){
  if(!dateStr){
    return;
  }
  return dateStr.substring(0,10);
}