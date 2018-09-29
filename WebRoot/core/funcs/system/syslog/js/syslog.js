  function doInit3(){
    
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysLogSearchAct/getMySysLog.act';
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
       var rtData = rtJson.rtData;
       var listData = rtData.listData;
       $(' str').innerHTML = '&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str;
       $(' str1').innerHTML ='&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str1;
       $(' str2').innerHTML ='&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str2;
       $(' str3').innerHTML ='&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str3;
       $(' str4').innerHTML ='&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str4;
       $(' str5').innerHTML ='&nbsp;&nbsp;&nbsp;'+ rtJson.rtData.str5;
       Newten();
      }
  }
  var k = 0;
  function Newten(){
    var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysLogTenAct/getMySysLog.act';
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
        var rtData = rtJson.rtData;
        var listData = rtData.listData;
        for(var i = 0 ; i < listData.length ;  i ++ ){
        var data = listData[i];
        SysLogTen(data, i);
        k++;
      }
     for(var j = 0 ; j < k ; j++){  // 把表中的数字 转换成名字 如：1 换成 登陆日志  别忘了k的作用  日志类型的转换
        //alert($("fromId_" + j).value); bindDesc 是写好的函数，type_ 对应上面的<input id=\"type_ 数据库中的表名
        bindDesc([{cntrlId:"type_" +j, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SYS_LOG"}]); 
     }
     for(var m = 0 ; m < k ; m++){  // 把表中的数字 转换成名字 如：233 换成 系统管理员  别忘了k的作用
        //alert($("fromId_" + j).value);
       var userName = listData[m].userName;
       if(!userName){
         bindDesc([{cntrlId:"fromId_" + m, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
       }else{
         $("fromId_" + m + "Desc").update(userName);
       }

     }
    }else{
      alert("检索数据为空");
    }
  }
  function SysLogTen(tmp,i){
    // 把表中的数字 转换成名字 如：233 换成 系统管理员  别忘了k的作用
      var tenTime = tmp.time;
        // tenTime = tenTime.substring(0,tenTime.indexOf(".0"));
         if(tmp.useId=='0'){
            var td = "<td nowrap align=center>" + tmp.useId + "</td>"
        }else{
  		      var td = "<td nowrap align=center> <input id=\"fromId_"+k+"\" type=hidden value=" + tmp.useId + "><span id=\"fromId_"+k+"Desc\"></span></td>"
        }
             td+= "<td nowrap align=center>" + tenTime + "</td>"
             td+= "<td nowrap align=center>" + tmp.ip + "</td>"
             td+= "<td nowrap align=center><input id=\"type_"+k+"\" type=hidden value=\""+tmp.type +"\"><span id=\"type_"+k+"Desc\"></span></td>"
             td+= "<td nowrap align=center>" + tmp.remark.replace('null','') + "</td>"
  		var className = "TableLine2" ;    
  	  if(i%2 == 0){
  	     className = "TableLine1" ;
  	  }
  	  var tr = new Element("tr" , {"class" : className});
  	  $('dataBody').appendChild(tr);  
  	  tr.update(td);
  }
  function SysLogRow(tmp,i){
    var td = "<td nowrap class=TableData width=100>总统计天数:</td>"
            + "<td class=TableData align=left>" + tmp.countDays +"</td>";
    var className = "TableLine2" ;    
    if(i%2 == 0){
       className = "TableLine1" ;
    }
    var tr = new Element("tr" , {"class" : className});
    $('dataBody').appendChild(tr);  
    tr.update(td)
  } 

