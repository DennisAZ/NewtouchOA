/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}

/**
 * 点击查看信息后并把信息绑定到指定页面
 * @param seqId
 * @return
 */

function showInfoStr(result){
  var obj = document.getElementById("DMakeSealV61");
  if(!obj){
     alert("控件加载失败!");
    return false;
  }
  if(0 == obj.LoadData(result)){
    var vID = 0; 
    vID = obj.GetNextSeal(0);
    if(!vID){
     return true;
    }
    if(obj.SelectSeal(vID)) return false;
    var vSealID = obj.strSealID;
    var vSealName = obj.strSealName;
    var vSealWidth = obj.fSealWidthMM
    var vSealHeight = obj.fSealHeightMM;
    var vCertCtrlNum = 0;
    var vID = 0 ;
    var vSignInfo = "无签名" ;
    var vCertInfo = "" ;
    var vTempFilePath = "" ;
    while(vID = obj.GetNextCtrlCert(vID)){
     //obj.GetCtrlCert(vID,""); 
     //alert(0);
     vTempFilePath = obj.GetTempFilePath(); 
     if(0 == obj.GetCtrlCert(vID,vTempFilePath)){
      if(0 == obj.CertGetInfo(vTempFilePath,"CERTDATAFILE")){
        vCertInfo += "用户："+ obj.SubjectName+ "  序列号:" + obj.SerialNumber+"<br>";
      }
      obj.DelLocalFile(vTempFilePath);
     }
     vCertCtrlNum ++;
    }
    if(vCertCtrlNum>0)
      vCertInfo = "绑定的证书数量:"+vCertCtrlNum+"<br>"+vCertInfo;
    else
     vCertInfo = "无绑定证书";
    
    vTempFilePath = obj.GetSignCert(0); 
    if("" != vTempFilePath){
     if(0 == obj.CertGetInfo(vTempFilePath,"CERTDATAFILE") ){
       vSignInfo +="用户:" + obj.SubjectName+"  序列号:" + obj.SerialNumber+"<br>";
     }
     obj.DelLocalFile(vTempFilePath);
    }
      $("seal_id").innerHTML=vSealID;
      $("seal_name").innerHTML=vSealName;
      $("seal_size").innerHTML="宽度："+vSealWidth+" mm<br>高度："+vSealHeight+" mm"; 
      $("seal_sign").innerHTML=vSealID;
      $("seal_cert").innerHTML=vCertInfo;
      $("seal_sign").innerHTML=vSignInfo;    
     ShowDialog();
  }else{
    alert("读取印章数据失败");
  }
}
/**
 * 查看页面格局css操作
 * @return
 */
function ShowDialog(){    //id是传入后台的最佳答案的id 
  $("apply").style.left = (parseInt(document.body.clientWidth) - parseInt($("apply").style.width))/2;
  $("apply").style.top  = 150;
  $("overlay").style.width  =  document.body.clientWidth;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight) )
     $("overlay").style.height  =  document.body.clientHeight;
  else
   $("overlay").style.height  =  document.body.scrollHeight;
  $("overlay").style.display = 'block';
  $("apply").style.display = 'block';
 window.scroll(0,0);
}

function sealIdFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>"
}

function creatTimeFunc(cellData, recordIndex, columIndex){
  var value = cellData.substr(0, cellData.length - 2);
  return "<center>" + value + "</center>"
}


function logTimeFunc(cellData, recordIndex, columIndex){
  var value = cellData.substr(0, cellData.length - 2);
  return "<center>" + value + "</center>";
}

function resultFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function ipAddFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function sealNameFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

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

function Test(leaveDate1,leaveDate2){ 
//var leaveDate1 = document.getElementById("leaveDate1"); 
//var leaveDate2 = document.getElementById("leaveDate2"); 
  var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  var leaveDate2Array = leaveDate2.value.trim().split(" "); 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  
  if(leaveDate1.value){
    if(leaveDate1Array.length != 2){ 
      alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始时间格式不对，应形如 1999-01-02 14:55:20"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
        alert("错误,截止时间格式不对，应形如 1999-01-02 14:55:20"); 
        leaveDate2.focus(); 
        leaveDate2.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate1.value && leaveDate2.value){
    if(leaveDate1.value > leaveDate2.value){
      alert("错误 起始时间不能大于截至时间！");
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false;
    }
  }
  return true;
}
