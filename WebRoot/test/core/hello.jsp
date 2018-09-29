<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<head>
<title>SWFUpload Demos - Simple Demo</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript"><!--
 /*
function doLogin() {
  var queryParam = $("form1").serialize();
  queryParam = mergeQueryString(queryParam,{a:'ww', b:'bb'}, "ee=qq");// 这个方法可以传无限参数
  //alert(queryParam);
  var rtJson = getJsonRs("<%=contextPath %>/test/core/act/YHTestLoginAct/doLogin.act", queryParam);
  //alert(rtJson);
  if (rtJson.rtState == "0") {
    alert(rtJson.rtData.name);
  }else {
    alert(rtJson.rtMsrg);
  }
}
function doInit(){
 var jehe = new Array();
 var psersonName = [{zs,81,0(4)},{zs,82,0(3)},{ls,80,0(2)},{ls,79,0(1)},{ww,78,0(0)},{ww,77,0(1)}];
 //alert(psersonName); 
 for(var i = 0; i<personName.length; i++){
   jehe = personName.get(i); 
                            
   }
}*/

function doInit1(){
  alert("dd");
 var f = [{name:'a',value:1,b:'bz70'},{name:'b',value:5,b:'bz71'},{name:'a',value:2,b:'72'},{name:'b',value:4,b:'bz73'}];
 var temp = new Object();
 f.each(function(e,i){
 if(!temp[e.name]){   //如果名字不相同 就赋值于0 
   temp[e.name] = new Object();
   alert(temp[e.name]);
   }
 alert(temp[e.name]+"aaa");// 
 temp[e.name] += e.value; //如果遇到(temp[e.a])名字相同的 就让值相加
 alert(e.name+","+e.value+","+e.b);
 var as= e.name+","+e.value+","+e.b
 alert(as);
if(temp[e.name]){
  alert(as+","+e.value+","+e.b);
}
      });
}

/*
function doInit2(){
 // alert();
  var t = [{name:'a',value:1,b:'b81'},{name:'b',value:1,b:'b82'},{name:'a',value:2,b:'b83'}]; 
  var temp = new Object(); 
  t.each(function(e,i){ 
  if(!temp[e.name]){ 
  temp[e.name] = 0; 
  } 
  temp[e.name] += e.value; 
  alert(temp[e.name]);
  });
  
}*/

function doInit3(){
var t = [{name:'a',value:1,b:'b81'},{name:'b',value:1,b:'b82'},{name:'a',value:2,b:'b83'}]; 
var temp = new Object(); 
t.each(function(e,i){ 
if(!temp[e.name]){ 
temp[e.name] = function(){} 
 // var temp = new Object();
} 
temp[e.name][e.b] = e.value; 
}); 
alert("bbb");
for(var i in temp){ 
if(!this.hasOwnProperty(i)){ 
continue; 
}alert("ccc"); 
alert(temp[i]);
}
}
function doInits(){
  
  var ts = [{name:'a',value:1,b:'bz81'},{name:'b',value:1,b:'b82'},{name:'a',value:2,b:'b83'}];
  var temp = new Object();
  ts.each(function(e,i){
    
    alert(!temp[e.name]);//
   
  if(!temp[e.name]){//名字不相同為true 相同為false
    temp[e.name] = new Object(); //new 個空間給temp[e.name]
    }
 // alert(temp[e.name][e.b]);
    temp[e.name][e.b] = e.value;
    alert(temp[e.name][e.b]);
    });
}


function doInit4(){
  var tt =[{name:'a',value:1,b:'b81'},{name:'b',value:1,b:'b82'},{name:'a',value:2,b:'b83'},{name:'b',value:4,b:'b89'}];
  var temp = new Object();
  tt.each(function (e, i){
  if(!temp[e.name]){
   temp[e.name] = new Object();
    }
  temp[e.name][e.b] = e.value;
    });
for (var i in temp){
  if(!this.hasOwnProperty(i)){
   continue;
    }
  alert(temp[i]);
}
}

function doInit2(){ //驗證用 這個是正常， 用断点跟
var t = [{name:'a',value:1,b:'b81'},{name:'b',value:1,b:'b82'},{name:'a',value:2,b:'b83'},{name:'b',value:4,b:'b89'}]; 
var temp = new Object(); 

t.each(function(e,i){//e就相当于[{}]数组中的每个{}花括号 的内容， i 就是相当于数组中有多少个花括号 
if(!temp[e.name]){   //名字不相同為true相同為false主要为不相同的分配空间（隐含意思 每次第一个人名不相同就分配空间，{相同了就不走这里，但已有了内存空间}）
temp[e.name] = new Object(); 
} 
//var ss = temp[e.name][e.b] = e.value; //
 temp[e.name][e.b] = e.value; //有[e.b]目的是显示相同人名的不同步骤（如果没有 就e.name 容易混淆） 如：a b81 1 b83 2 跟断点容易发现
});
alert(temp);
}

--></script>
</head>
<body onload="doInit2()">
<form name="form1" id="form1">


 <table id="flow_table" width="100%" class="TableList" style="table-layout:fixed;">
   <!--  <tbody id="dataBody"></tbody>  -->

   <tbody id="dataBody"> </tbody>
</table>
</form>
</body>
</html>