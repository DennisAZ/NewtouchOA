
/**
 * 打开新窗口  newWindow(URL,'800', '600','windosName');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height,nameStr){
  var defaultName = "newWindos";
  if(nameStr){
    defaultName = nameStr;
  }
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, defaultName, 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

//选择框


function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";

}


function toDate(cellData, recordIndex, columInde){
  return cellData.substr(0,19);
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

/**
 * 获取参数
 * @param paraName
 * @return
 */
function getPara(paraName){
  var url = contextPath + "/yh/subsys/servicehall/setting/act/YHSysParaHallAct/selectObj.act?paraName=" + paraName;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  return json.rtData;
}


/**
 * 获取时间差
 * @param interval :相差时间规则 D：天 H:小时 M：分钟 S:秒
 * @param date1
 * @param date2
 * @returns
 */
function dateDiff(interval, date1, date2)
{
   var objInterval = {'D':1000 * 60 * 60 * 24,'H':1000 * 60 * 60,'M':1000 * 60,'S':1000,'T':1};
   interval = interval.toUpperCase();
   var dt1 = new Date(Date.parse(date1.replace(/-/g, '/')));
   var dt2 = new Date(Date.parse(date2.replace(/-/g, '/')));
   try
   {
     // alert(dt2.getTime() - dt1.getTime());
      //alert(eval_r('objInterval.'+interval));
      //alert((dt2.getTime() - dt1.getTime()) / eval_r('objInterval.'+interval));
      return Math.round((dt2.getTime() - dt1.getTime())/ eval('objInterval.'+interval) );
    }
    catch (e)
    {
      return e.message;
    }
    
    
}

function toNotifyTitle(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='javascript: selectInfo("+seqId+ ")' >" + cellData+  "</a>";
}

/**
 * 查看收文详情
 * @param paraName
 * @return
 */
function selectInfo(seqId){
  var url = contextPath + "/subsys/jtgwjh/notifyManage/reciveDetail.jsp?seqId=" + seqId;
  newWindow(url,700,450,"info");
}

function toPrint(seqId){
  var url = contextPath + "/subsys/jtgwjh/receiveDoc/recvprint.jsp?seqId=" + seqId;
  newWindow(url,900,600,"info");
}

/**
 * 
 * @param obj : 公文对象Id
 * @param printCount ： 打印份数
 * 编号：startNo,endNo
 */
function addSelLogPrint(seqId,printCount,startNo,endNo){
  var url = contextPath + "/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/addSelLogPrint.act";
  var json = getJsonRs(url,{seqId:seqId,printCount:printCount,startNo:startNo ,endNo:endNo});
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  return json.rtData;
}

/**
 * 
 * @param obj : 公文对象Id
 * @param printCount ： 打印份数
 * 编号：startNo,endNo
 */
function addSelLog(seqId,status){
  var url = contextPath + "/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/addSelLog.act";
  var json = getJsonRs(url,{seqId:seqId,status:status});
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  return json.rtData;
}


