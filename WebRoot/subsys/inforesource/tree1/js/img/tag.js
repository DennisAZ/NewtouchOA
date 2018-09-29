$("document").ready(function(){  
   
 });
 /**
  * 返回第一层tag
  */
  function ajax1(){
    $.ajax({
      url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/firstLevelTag.act",
      type:"post",      
      success:function(data){
      var temp = eval("("+data+")");
      insertData(temp);
      }
    });  
  }
  
  /**
   * 插入tag云图
   */
    function insertData(data){ 
      if(data){ 
          if(data.rtState == '0'){
            $("#leftPic").empty();
            var rtdata = data.rtData;           
            var cont = "";  
            var contname = "";
            if(rtdata.length > 0){  
              var arr = new Array();
              for(var j=0; j<rtdata.length; j++){
               var intnums = rtdata[j].nTime;
               if(!intnums){
                  intnums =  rtdata[j].nTimes;  
                }            
                var number = parseInt(intnums);
                arr.push(number);
              }
              arr = arr.sort(sortNumber); 
                  
              var count=60; 
              var original=new Array;//原始数组 
              for (var i=0;i<count;i++){ 
              original[i]=i+1; 
              } 
              original.sort(function(){ 
              return 0.5 - Math.random();//随机排序 的一种


              });     
            for(var i=0; i<rtdata.length && i<60; i++){         
              contname = rtdata[i].Keyword; 
              var intnum = rtdata[i].nTime;
              if( !intnum ){
                intnum =  rtdata[i].nTimes;  
               }           
              var number = parseInt(intnum);             
              cont += "<a id=\"font"+original[i]+"\" ";              
              cont += " style=\"cursor:pointer;\">";
              cont += "<font size=\""+returnFont(arr, intnum)+"\">"+rtdata[i].Keyword+"</font>";
              cont += "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";           
            }
          }else{
            cont +="<br><br>"
             cont += "<div style=\"margin-top:-5px;\"><font size=3 color=\"#001199\">暂</font><font color=\"#00fffa\">无</font><font size=3 color=\"#22FF22\">TAG</font><font color=\"#00fffa\">云</font><font  size=3 color=\"#22FF22\">图</font><font color=\"#FF2222\">标</font>签<div>";
          }
              $("#leftPic").append(cont);
        }
      }
    }
    
    /**
     * 返回改权值下的字体
     */
     function returnFont(arr, ntime){
       var max = arr[arr.length-1];
       var min = arr[0];
       var MAXFONT = 4;//设置最大字体为5
       var flag = parseFloat((max - min)/MAXFONT);
       for(var i=0; i<MAXFONT; i++){
         if(ntime >= (flag*i) && ntime < (flag*(i+1))){    
           if(i==0){i=1;}   
            return (i+1);
         }
       } 
       return MAXFONT; 
     }
     
     /**
      * 数组进行排序
      */
      function sortNumber(a, b){
        return a-b;
      }