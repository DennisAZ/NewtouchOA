  function initSelectState(e,value){ 
    e.childElements().each(function(e,i){ 
      if(e.value == value){ 
         e.selected = true; 
      } 
    }); 
  } 
  
  function initSelectOption(element,rtJson){ 
     rtJson.rtData.each(function(e,i){ 
     element.options.add(new Option(e.year + '年',e.year)); //year 是后台集合中的对象    }); 
  }
  // 统计下拉列表中  有多少年（在系统日志第2个标签页）  function doIniyh(){
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysLog.act';
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      initSelectOption($('YEAR'),rtJson);
     $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据'; //当选中下拉菜单时 ，按月访问数据年份跟着变   } 
  }
  function getdoInit(){
    //alert("::"+$F('YEAR'));
    // var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysLog.act?year=' + $('YEAR').value;
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysLog.act';
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
        initSelectOption($('YEAR'),rtJson);
       $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据'; //当选中下拉菜单时 ，按月访问数据年份跟着变    }  
   }
  
  /*function so(){
    $('january').innerHTML = rtJson.rtData.months[0].month;
    $('feb').innerHTML = rtJson.rtData.months[1];
    $('m').innerHTML = rtJson.rtData.months[2];
  }*/
  function remove(){  
    var test = document.getElementById("DAYS"); 
    var children = test.childNodes; 
    alert(children.length);
    for(i=0;i<children.length;i++){
      test.removeChild(children[i]);  
    }
  }
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
        var row = new Element('tr');
        row.className = 'TableData';
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
  function change_date4(){
    $('DAYS').update("");
    var year = $F('YEAR');
    var month = $F('MONTH');
    var par = "year="+ year +"&month="+month;
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysyueLog.act';
    var rtJson = getJsonRs(url,par);
    if(rtJson.rtState == "0"){
      var rtData = rtJson.rtData;
      var sum = rtData.sum;
      if(sum == 0){
        sum =1;
      }
      var j=0;  var ss = new Array();
      var yue = new Array(); 
      var s=0; var n = 0;
      var len8=0;
      var len6=0; var len4=0;
      var len2=0; var len10=0;
      rtData.days.each(function(e,i){//通过rtData.days获得后台act中的每个月各个天数。     json 串循环的一种表现形式
      var daysTable =  $('DAYS');    //通过页面中id="DAYS" 就可以循环显示出来
      var row = daysTable.insertRow(daysTable.rows.length);//表名插入行的长度
      row.className = 'TableData';                          //行的类名
      var td1 = row.insertCell(row.cells.length);          // 通过行名 插入列名
      td1.nowrap = true;                                 // 以下是列的属性
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
       var tem = Math.round(e.day/sum*100);
       td1.innerHTML = i + 1;
       td2.innerHTML = tem + "%";
       td3.innerHTML = e.day;
       
         ss[j++]=e.day;
       for(var m = 0; m < ss.length; m++){ 
         yue[n]= Math.round((ss[m]));
         if(yue[n]==0){
           yue[n]=1;
          }
         if(s<=yue[n]){
          s = yue[n];  
         }
       } 
     });
      len10 = s; //if(s==0)s=1;
      len8 = Math.round(s*0.8);
      len6 = Math.round(s*0.6);
      len4 = Math.round(s*0.4);
      len2 = Math.round(s*0.2);
      var titleTr = new Element('tr',{});
      titleTr.className = 'TableData';
      var td9= new Element('td',{colspan:'12'});
      titleTr.insert(td9);
      var table1= new Element('table',{border:0,height:'100%',cellspacing:2,cellpadding:0});
      td9.insert(table1);
      var td0 = new Element('tr',{});
      var trMonth = new Element('tr',{});
      table1.insert(td0);
      table1.insert(trMonth);
      var tdMonth0 = new Element('td',{align:'right'}).update('0');
      trMonth.insert(tdMonth0);
      var tr3 = new Element('td',{width:42,height:'0', valign:'top'})
      td0.insert(tr3);
      //style:'margin:-3px 0px;',火狐需要添加，ie不需要 
      var tr0= new Element('table',{ border:0,height:'100%',width:'100%',align:'right',cellspacing:0,cellpadding:0});
      tr3.insert(tr0); 
      tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len10)));
      tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len8)));
      tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len6)));
      tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len4)));
      tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len2)));
      
      for(var i=0; i<ss.length; i++){
        if(ss[i]==0){
          ss[i]=1;
        }  
        var hei = Math.round(ss[i]*100/s); //alert(hei);
        var td3 = new Element('td',{width:'42',height:'0',align:'center',valign:'bottom'});
        td0.insert(td3);
        var table2 = new Element('table',{border:0,width:15,cellspacing:0,cellpadding:0});
        td3.insert(table2);
        
        var tr2 = new Element('tr',{});
        if(s!=1){//如果等于1的话 1*100/100 每个图形都是满格
         var td2 = new Element('td',{width:'100%',height:Math.round(ss[i]*100/s),background:imgPath + '/column.gif',valign:'bottom'});
        }else{
         var td2 = new Element('td',{width:'100%',height:Math.round(1),background:imgPath + '/column.gif',valign:'bottom'});
        }
         tr2.insert(td2);
         table2.insert(tr2);
         var tr1 = new Element('tr',{});
         var td1 = new Element('td',{width:'100%',valign:'bottom',align:'center'}).insert(new Element('img',{border:0,src:imgPath + '/column.gif',width:15}));
         tr1.insert(td1);
         table2.insert(tr1);
         //判断等于30天的？
         if(ss.length==30 && i%2!=0){
           var tdMonth = new Element('td',{align:'center'}).update(i+1);
         }else{
           if(i==0){
             var tdMonth = new Element('td',{align:'center'}).update(i+1);
           }else{
           var tdMonth = new Element('td',{align:'center'}).update(""); 
           }
         }//除了 30天之外的
         if(ss.length!=30){// 取模 
           if((i)%3 == 0){
           //i是从0开始  27（其实27就是28）,27%3能除尽，+1变成28。29，31原理一样
             var tdMonth = new Element('td',{align:'center'}).update(i+1); 
            }else if((i == ss.length-1)){ //（特殊处理  如果是闰年2月29天）i是从0开始取得   如果i==29 显示在最后，不至于最后显示不出来最大的天数          
              var tdMonth = new Element('td',{align:'center'}).update(ss.length); 
            }else{
             var tdMonth = new Element('td',{align:'center'}).update(""); 
           }  
         }
       //  var tdMonth = new Element('td',{align:'center'}).update(i+1);
          trMonth.insert(tdMonth);
      }$('dataBodys').update("");
      //$('dataBodys').insert(titleTr);
    } 
    $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据';//当选中下拉菜单 年份 时 ，年份跟着变
    $('MONTHS').innerHTML = $F('MONTH') + '月份按日访问数据'; // 当下拉列表选中 月份时 月份跟着变 
  }
  function change_date(){
    var year = $F('YEAR');
    var month = $F('MONTH');
    var par = "year="+ year +"&month="+month;
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysYearLog.act';
    var rtJson = getJsonRs(url,par);
    if(rtJson.rtState == "0"){
       var rtData = rtJson.rtData;
       var sum = rtData.sum;
       var one = rtData.months[0].month;
       var two = rtData.months[1].month;
       var three = rtData.months[2].month;
       var four = rtData.months[3].month;
       var five = rtData.months[4].month;
       var six = rtData.months[5].month;
       var senver = rtData.months[6].month;
       var eith = rtData.months[7].month;
       var nine = rtData.months[8].month;
       var ten = rtData.months[9].month;
       var elwen = rtData.months[10].month;
       var telwen = rtData.months[11].month;
       
       var one1 = one*100/sum;
       var two1 = two*100/sum;
       var three1 = three*100/sum;
       var four1 = four*100/sum;
       var five1 = five*100/sum;
       var six1 = six*100/sum;
       var senver1 = senver*100/sum;
       var eith1 = eith*100/sum;
       var nine1 = nine*100/sum;
       var ten1 = ten*100/sum;
       var elwen1 = elwen*100/sum;
       var telwen1 = telwen*100/sum;
       var one1 = Math.round(one1);
       var two1 = Math.round(two1);
       var three1 = Math.round(three1);
       var four1 = Math.round(four1);
       
       var five1 = Math.round(five1);
       var six1 = Math.round(six1);
       var senver1 = Math.round(senver1);
       var eith1 = Math.round(eith1);
       
       var nine1 = Math.round(nine1);
       var ten1 = Math.round(ten1);
       var elwen1 = Math.round(elwen1);
       var telwen1 = Math.round(telwen1);
       $('January').innerHTML = one1+'%';
       $('February').innerHTML =two1+'%';
       $('March').innerHTML =three1+'%';
       $('April').innerHTML =four1+'%';
       $('May').innerHTML = five1+'%';
       $('June').innerHTML =six1+'%';
       $('July').innerHTML =senver1+'%';
       $('August').innerHTML = eith1+'%';
       $('September').innerHTML = nine1+'%';
       $('Octorber').innerHTML = ten1+'%';
       $('November').innerHTML =elwen1+'%';
       $('December').innerHTML = telwen1+'%';
       
       $('Jan').innerHTML = one;
       $('Feb').innerHTML =two;
       $('Mar').innerHTML =three;
       $('Ap').innerHTML =four;
       $('Ma').innerHTML = five;
       $('Jun').innerHTML =six;
       $('Jul').innerHTML =senver;
       $('Aug').innerHTML = eith;
       $('Sep').innerHTML = nine;
       $('Octor').innerHTML = ten;
       $('Nove').innerHTML =elwen;
       $('Dece').innerHTML = telwen;
    }
    $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据';//当选中下拉菜单时 ，按月访问数据年份跟着变
    
  }
  

