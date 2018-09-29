function parseObj(docType, id){
    if(docType){
      var tt = eval("("+docType+")");
      var sel = document.getElementById(id);
      if(tt && sel){
        for(var i=0; i<tt.length; i++ ){         
          sel.options.add(new Option(tt[i], i+1));
        }
      }
    }
  }
  function initDate(date){
    if(date < 10){
      return "0"+date;
    }
    return date;
  }

  function doSubmit(saveType){
    var url = $("form1").action;
    $("ftype").value = saveType;
    if(jugeFile() && checkVal()){ //有没有上传的文件
      $("formFile").submit();     //如果有没上传的文件，先上传
      isUploadBackFun = true;
      //$("form1").submit();
      return;
    }
    if(checkVal()){ 
      $("form1").submit();
    }
    return false;
  }

  function clearValue(){
    $("recipient").value = "";
    $("recipientId").value = "";
  }

  function clearUser(){
   $("user").value = "";
   $("userId").value ="";
  }

  function checkVal(){
    var docNo = $("docNo").value;
    var fromUnits = $("fromUnits").value;
    var confLevel = $("confLevel").value;
    var oppDocNo = $("oppDocNo").value;
    var title = $("title").value;
    var copies = $("copies").value;
   // var startTime = $("startTime").value;
   // var sponsor = $("sponsor").value;
    var docType = $("docType").value;
    //var recipient = $("recipient").value;
    var user = $("user").value;
    var flag  = true;
    if(docNo=="" || docNo==null || docNo.replaceAll(" ","")=="" ){
        $("docNoMsg").innerHTML = "*文号为必填项";
        flag = false;
    }else{
      $("docNoMsg").innerHTML = "";
    }
    if(fromUnits=="" || fromUnits==null || fromUnits.replaceAll(" ","")=="" ){
      $("fromUnitsMsg").innerHTML = "*来文单位为必填项";
       flag = false;
    }else{
      $("fromUnitsMsg").innerHTML = "";
    }
    
    if(oppDocNo=="" || oppDocNo==null || oppDocNo.replaceAll(" ","")==""){
      $("oppDocNoMsg").innerHTML = "*对方文号为必填项";
      flag = false;
    }else{
      $("oppDocNoMsg").innerHTML = "";
    }
    
    if(title=="" || title==null || title.replaceAll(" ","")==""){
      $("titleMsg").innerHTML = "*对方文号为必填项";
      flag = false;
    }else{
      $("titleMsg").innerHTML = "";
    }
    
    if(copies=="" || copies==null || copies.replaceAll(" ","")==""){
      $("copiesMsg").innerHTML = "*份数为必填项";
      flag = false;
    }else if(copies==0){
      $("copiesMsg").innerHTML = "*份数必须大于0";
      flag = false;
    }else if(!isInteger(copies)){
      $("copiesMsg").innerHTML = "*份数必须为整数";
      flag = false;
    }else{
      $("copiesMsg").innerHTML = "";
    }

    /*if(startTime=="" || startTime==null || startTime.replaceAll(" ","")==""){
      $("startTimeMsg").innerHTML = "*时间为必填项";
      flag = false;
    }else if(!isValidDateStr(startTime)){
      $("startTimeMsg").innerHTML = "*时间格式不正确,应如1999-10-10";
      flag = false;
    }else{
      $("startTimeMsg").innerHTML = "";
    }
    if(recipient=="" || recipient==null || recipient.replaceAll(" ","")==""){
      $("recipientMsg").innerHTML = "*签收人为必填项";
      flag = false;
    }else{
      $("recipientMsg").innerHTML = "";
    }*/
    if(user=="" || user==null || user.replaceAll(" ","")==""){
      $("userMsg").innerHTML = "*登记人为必填项";
      flag = false;
    }else{
      $("userMsg").innerHTML = "";
    }
    if(seqId){//id不为空是修改状态
      var deptVal = $("deptId").value;
      if(deptVal=="" || deptVal==null || deptVal.replaceAll(" ","")==""){
        $("sponsorMsg").innerHTML = "*承办处室为必填项";
        flag = false;
      }else{
        $("sponsorMsg").innerHTML = "";
      }
    }
    return flag;
  }

  function test(){
    alert($("alarm").value);
  }

  /**
   * 处理附件的显示
   * @param cntrlId
   * @return
   */
  function showAttach(attrIds,attrNames,cntrId){
    var reStr = "<div id='attrDiv'>";
    var ym = "";
    var attrId = ""
    var attrIdArrays = attrIds.split(",");
    var attrNameArrays = attrNames.split("*");
    for(var i = 0 ; i <= attrIdArrays.length; i++){
      if(!attrIdArrays[i]){
        continue;
      }
      var key = attrIdArrays[i];
      var attrName = attrNameArrays[i];
      var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
      attachMenuUtil("showAtt","<%=YHDocConst.MODULE%>",null,$('attachmentName').value ,$('attachmentId').value,false);
    }
    reStr += "</div>";
    if(cntrId){
      $(cntrId).innerHTML = reStr;
    }else{
      document.write(reStr);
    }
  }
 

  /**
   * 处理文件上传
   */

  function handleSingleUpload(rtState, rtMsrg, rtData) {
      var data = rtData.evalJSON(); 
      $('attachmentId').value +=  data.attrId;
      $('attachmentName').value +=  data.attrName; 
      attachMenuUtil("showAtt","doc",null,$('attachmentName').value ,$('attachmentId').value,false,"","","",true)
      removeAllFile();
      if(isUploadBackFun==true){
        $("form1").submit();       //如果文件上传上去了，则保存
        isUploadBackFun = false;
      }
 }

  function upload_attach()
  {
      $("btnFormFile").click();
  }

  /**
   * 判断有没有上传的附件
   */
  function  jugeFile(){
    var formDom  = document.getElementById("formFile");
    var inputDoms  = formDom.getElementsByTagName("input"); 
    for(var i=0; i<inputDoms.length; i++){
      var idval = inputDoms[i].id;
      if(idval.indexOf("ATTACHMENT")!=-1){
        return true;
      }
    } 
    return false; 
  }

//浮动菜单文件的删除 
  function deleteAttachBackHand(attachName,attachId,attrchIndex){ 
  var url= contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocReceiveAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName ; 
  if (seqId) {
    url += "&seqId=" + seqId;
  }
  var json=getJsonRs(encodeURI(url)); 
  if(json.rtState =='1'){ 
   alert(json.rtMsrg); 
   return false; 
  }else{ 
   prcsJson=json.rtData; 
   var updateFlag=prcsJson.updateFlag; 
   if(updateFlag){ 
     var ids = $('attachmentId').value ;
     if (!ids) {
       ids = ""; 
     }
     var names =$('attachmentName').value;
     if (!names) {
       names = ""; 
     }
     var idss = ids.split(",");
     var namess = names.split("*");
     
     var newId = getStr(idss , attachId , ",");
     var newname = getStr(namess , attachName , "*");  
     
     $('attachmentId').value = newId;
     $('attachmentName').value = newname;
     return true; 
   }else{ 
     return false; 
   }  
  } 
  }
  
  function getStr(ids , id , split) {
    var str = "";
    for (var i = 0 ; i< ids.length ;i ++){
      var tmp = ids[i];
      if (tmp) {
        if (tmp != id) {
          str += tmp + split;
        }
      }
    }
    return str;
  }
  
  function onselChange(){
    var typeId = $("docType").value;
    getMaxOrderNo(typeId);
  }
  /**
   * 获得最大的收文编号
   * @return
   */
  function getMaxOrderNo(typeId){
    var url = contextPath +"/yh/subsys/inforesouce/docmgr/act/YHDocReceiveAct/getMaxOrderNo.act?typeId=" + typeId;
    var rtJson = getJsonRs(url); 
    if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
    $("docNo").value = (rtJson.rtData + 1);
  }
  
  function initDept(){
    var requestUrl = contextPath + "/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToAttendance.act?deptId=0";
    var rtJson = getJsonRs(requestUrl);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var userId_priv = rtJson.rtMsrg;
    var userId = userId_priv.split(",")[0];
    var priv = userId_priv.split(",")[1];
    var prcs = rtJson.rtData;
    var selects = document.getElementById("deptId");
    
      var optionAll = document.createElement("option"); 
      optionAll.value = ''; 
      optionAll.innerHTML = '请选择部室'; 
      selects.appendChild(optionAll);
    
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var option = document.createElement("option"); 
      option.value = prc.value; 
      option.innerHTML = prc.text; 
      selects.appendChild(option);
    }
  }
  