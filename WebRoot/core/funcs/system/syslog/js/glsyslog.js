  
  function doInit3(queryParam){
    alert(queryParam); 
    var temp = queryParam;
    var url = contextPaths+'/yh/core/funcs/system/syslog/act/YHGlSyslogAct/getMyGlSysLog.act';
    var rtJson = getJsonRs(url,temp);
    if(rtJson.rtState == "0"){
      alert("cheng gongle");
      alert(rsText);
      var rtData = rtJson.rtData;
      var listData = rtData.listData;
      if(listData.length > 0){
         // $('dataBody').update("");
          for(var i = 0 ;i < listData.length ;i ++){
            var data = listData[i];
            addRow(data, i);
           }
       }else{
          alert("检索数据为空");
          } 
      }
  }
  function addRow(tmp,i){
    alert("jin ru addRow");
    var td = "<td nowrap align=center><input type=checkbox name=run_select onclick=check_select() value='"+ tmp.seqId +"'></td>"
             + "<td nowrap align=center>" + tmp.userId +"</td>"
             + "<td nowrap align=center>" + tmp.date +"</td>"
             + "<td nowrap align=center>" + tmp.Ip +"</td>";
             + "<td nowrap align=center>" + tmp.types +"</td>"
             + "<td nowrap align=center>" + tmp.remarks +"</td>";
  
    var className = "TableLine2";    
    if(i%2 == 0){
      className = "TableLine1" ;
    }
    var tr = new Element("tr" , {"class" : className});
    $('dataBody').appendChild(tr);  
    tr.update(td);
  }
