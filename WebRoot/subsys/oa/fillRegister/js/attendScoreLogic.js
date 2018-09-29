/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=62";
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData;
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  if (moblieRemindFlag == '2') {
    $(remidDiv).style.display = ''; 
    $(remind).checked = true;
    document.getElementById("smsSJ").value = "1";
  } else if(moblieRemindFlag == '1') { 
    $(remidDiv).style.display = ''; 
    $(remind).checked = false; 
  } else {
    $(remidDiv).style.display = 'none'; 
  }
}

//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=62"; 
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1") { 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind;
  if (allowRemind == '2') {
    $("smsRemindDiv").style.display = 'none';
  }else{ 
    if (defaultRemind == '1') { 
      $("smsflag2").checked = true;
      document.getElementById("smsflag").value = "1";
    }
  }
}

function checkBox2() {
  if (document.getElementById("smsflag2").checked) {
    document.getElementById("smsflag").value = "1";
  }else {
    document.getElementById("smsflag").value = "0";
  }
}
function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
    document.getElementById("smsSJ").value = "1";
  }else {
    document.getElementById("smsSJ").value = "0";
  }
}

/**
 * 是否选中
 * @param cntrlId
 * @return
 */
function checkMags(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == cntrlId && checkArray[i].checked ){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}


function attendDuty(){
  if($("beginDate").value && $("endDate").value){
    if($("beginDate").value <= $("endDate").value){
      flag = flag + 1;
      if(flag <= 1){
        $("attendDuty").style.display = '';
        getAttendDuty();
      }
    }
  }
}

function getAttendDuty(){
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendScoreAct/getAttendDutyJson.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
    .update("<tbody id='tbody'><tr class='TableData' align='center'>"
            +"<td nowrap width='5%' align='center'>选择</td>"
        +"<td nowrap width='25%' align='center'>登记次序</td>"
        +"<td nowrap width='25%' align='center'>登记类型</td>"       
        +"<td nowrap width='25%'>规定时间</td></tr><tbody>");
    $('listDiv').appendChild(table);
      var seqId = rtJson.rtData.seqId;
      var dutyTime1 = rtJson.rtData.dutyTime1;
      var dutyTime2 = rtJson.rtData.dutyTime2;
      var dutyTime3 = rtJson.rtData.dutyTime3;
      var dutyTime4 = rtJson.rtData.dutyTime4;
      var dutyTime5 = rtJson.rtData.dutyTime5;
      var dutyTime6 = rtJson.rtData.dutyTime6;
      
      var dutyType1 = rtJson.rtData.dutyType1;
      var dutyType2 = rtJson.rtData.dutyType2;
      var dutyType3 = rtJson.rtData.dutyType3;
      var dutyType4 = rtJson.rtData.dutyType4;
      var dutyType5 = rtJson.rtData.dutyType5;
      var dutyType6 = rtJson.rtData.dutyType6;
      
      if(dutyTime1){
        var num = "第1次登记";
        var dutyVal = "";
        if(dutyType1 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='1'></td><td align='center'>"
        + num + "</td><td align='center' nowrap>"     
        + dutyVal + "</td><td align='center' nowrap>"     
        + dutyTime1 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      if(dutyTime2){
        var num = "第2次登记";
        var dutyVal = "";
        if(dutyType2 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='2'></td><td align='center'>"
        + num + "</td><td align='center'>"     
        + dutyVal + "</td><td align='center'>"     
        + dutyTime2 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      if(dutyTime3){
        var num = "第3次登记";
        var dutyVal = "";
        if(dutyType3 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='3'></td><td align='center'>"
        + num + "</td><td align='center'>"     
        + dutyVal + "</td><td align='center'>"     
        + dutyTime3 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      if(dutyTime4){
        var num = "第4次登记";
        var dutyVal = "";
        if(dutyType4 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='4'></td><td align='center'>"
        + num + "</td><td align='center'>"     
        + dutyVal + "</td><td align='center'>"     
        + dutyTime4 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      if(dutyTime5){
        var num = "第5次登记";
        var dutyVal = "";
        if(dutyType5 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='5'></td><td align='center'>"
        + num + "</td><td align='center'>"     
        + dutyVal + "</td><td align='center'>"     
        + dutyTime5 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      if(dutyTime6){
        var num = "第6次登记";
        var dutyVal = "";
        if(dutyType6 == "1"){
          dutyVal = "上午登记";
        }else{
          dutyVal = "下午登记";
        }
        var tr=new Element('tr',{'class':'TableLine1'});      
        table.firstChild.appendChild(tr);
        tr.update("<td align='center'>"     
         + "<input type='checkbox'id='deleteFlag' name='deleteFlag' value='6'></td><td align='center'>"
        + num + "</td><td align='center'>"     
        + dutyVal + "</td><td align='center'>"     
        + dutyTime6 + "</td>" 
        + "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"    
       );
      }
      
  }else{
    alert(rtJson.rtMsrg); 
  }
}