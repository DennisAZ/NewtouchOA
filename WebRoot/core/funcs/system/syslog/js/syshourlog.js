  function doInit5(){
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysHourLog.act';
    var rtJson = getJsonRs(url);
    var sum = 0;
    if(rtJson.rtState == "0"){
        var rtData = rtJson.rtData;
        rtData.each(function(e,i){
          sum += e.value;
          sum = parseInt(sum); 
          if(sum == 0){
            sum = 1;
          }
        });
        
        rtData.each(function(e,i){
          var hoursTable = $('HOURS');
          var row = hoursTable.insertRow(hoursTable.rows.length);
          row.className = 'TableData';
          var td1 = row.insertCell(row.cells.length);
          td1.nowrap = true;
          td1.align = 'center';
          td1.width = '80';
         var td2 = row.insertCell(row.cells.length);
          td2.nowrap = true;
          td2.align = 'center';
          td2.width = '80';
          var td3 = row.insertCell(row.cells.length);
          td3.nowrap = true;
          td3.align = 'center';
          td3.width = '80';
          var tem = Math.round(e.value/sum*100);
          td1.innerHTML = e.hour;
          td2.innerHTML = tem +"%";
          td3.innerHTML = e.value;
        });
          rtData.each(function(e,i){
          var row = new Element('tr',{class:'TableData'});
          var td1 = new Element('td',{nowrap:true,align:'center',width:'80'});
          var td2 = new Element('td',{nowrap:true,align:'center',width:'80'});
          var td3 = new Element('td',{nowrap:true,align:'center',width:'80'});
          alert(e.value);
          var tem = Math.round(e.value/sum*100);
         
          td1.innerHTML = e.hour;
          td2.innerHTML = tem +"%";
          td3.innerHTML = e.value;
          row.insert(td1);
          row.insert(td2);
          row.insert(td3);
          $('HOURS').insert(row);
         
        });
    }
  }
