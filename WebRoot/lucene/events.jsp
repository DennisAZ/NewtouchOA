<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<% 
  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
  Date today=new Date();
  String todayStr=format.format(today);
  today=format.parse(todayStr);
  Date thatDay=format.parse("2011-11-6");
  long days=thatDay.getTime()-today.getTime();
  days=days/(1000*24*60*60);
%>
<style type="text/css">
#eventevent {
	text-align: center;
	margin-left: 30px;
	margin-right: 30px;
}


.current {
	width: 25px;
	height: 24px;
	background: url(/yh/show/css/images/daysel.png) no-repeat;
}
#calendar {
	clear:both;
	width: 632px;
	height: 220px;
	background: url(/yh/show/css/images/calendar-bg.png) no-repeat;
}
#ccccl {
  float:left;
  margin: 0px;
  padding-left: 25px;
  width : 280px;
  display: inline;
  overflow: hidden;
}
#ccccl ul {
  float:left;
  margin: 0;
  padding-left: 0px;
  margin-top: 8px;
  width : 240px;
}
#ccccl li {
  color: #00258f;
  height: 42px;
  width: 310px;
  text-align: left;
  font: 16px/42px "微软雅黑","宋体";
  padding-left: 20px;
  border-bottom: #eaeaea 1px solid;
}

#ccccr {
  float:right;
  margin: 0px;
  padding-right: 45px;
  width : 280px;
  display: inline;
	overflow: hidden;
}
#ccccr ul {
  margin: 0px;
  padding-left: 0px;
  margin-top: 8px;
}
#ccccr li {
  color: #00258f;
  height: 42px;
  width: 300px;
  text-align: left;

  font: 16px/42px "微软雅黑","宋体";
  padding-left: 20px;
  border-bottom: #eaeaea 1px solid;
}
.monthl {
  float:left;
  margin: 0px;
  padding-left: 25px;
  width : 280px;
}
.monthl table {
   width: 280px;
   display: inline;
   float: left;
   text-align: center;
}
.selectDay {
	background: url(/yh/show/css/images/daysel.png) no-repeat center center;
	width: 25px;
	height: 24px;
}
.selectDay a:link {
	color: #fff;
}
.selectDay a {
  color: #fff;
}
.monthl table th, .monthr table th {
	font: bold 16px/28px "微软雅黑","宋体";
	height: 40px;
}
.monthl table th a, .monthr table th a {
	height: 40px;
	line-height: 40px;
}
.monthl table td, .monthr table td {
	font: 14px/24px "微软雅黑","宋体";
	width: 40px;
	height: 24px;
}
.monthr {
  float:right;
  margin: 0px;
  padding-right: 25px;
  width: 280px;
  text-align: center;
}
.monthr table{
   width: 280px;  
   display: inline;
   float: left;
}
#holiday {
	margin-top: 10px;
	width: 635px;
	height: 238px;
	background:url(/yh/show/css/images/table-bg.png) no-repeat;
}
.prev {
	width: 23px;
	height: 28px;
	float: left;
	margin-left: 10px;
	background:url(/yh/show/css/images/prev.png) no-repeat left top;
}
.next {
	width: 23px;
	height: 28px;
	float: right;
	margin-right: 10px;
	background:url(/yh/show/css/images/next.png) no-repeat left top;
}
</style>
<link rel="stylesheet" href = "/yh/core/styles/style5/css/style.css">
<script type="text/javascript" src="/yh/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="/yh/show/js/getKeyInfo.js"></script>
<script type="text/javascript" src="/yh/core/js/jquery/yh/core/js/jquery/jquery.min1.6.2.js"></script>
<script type="text/javascript" src="/yh/core/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="/yh/core/js/sys.js"></script>
<script type="text/javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/javascript" src="/yh/show/js/cand.js"></script>
<script type="text/javascript" >
 

//setTimeout("setDays()",500);/
var days="<%=days%>";
  
function setDays(){
	  // initKey();   //识别key的开始入口       A0200000001921
	
	 if(Number(days)>0){
	$("count-down").innerHTML="<span>"+days+"</span>";
  //  changecount();
	 }else{
	  $("count-down").style.display="none";
		}
	 var  currentDate = new Date();
   currentDate = currentDate.format('yyyy-MM-dd');
	 init(currentDate);
	 getData(currentDate);
	 
   $("foot").innerHTML="<p>公安部南方研究所   版权所有</p>";
}

var  flag=1;
function changecount(){
	 if(flag==0){
	    $("count-down").innerHTML="";
	    $("count-down").style.background="url(css/images/count_down1.png) no-repeat";
	    
      flag=1;
	 }else{
		  $("count-down").innerHTML="<span>"+days+"</span>";
      $("count-down").style.background="url(css/images/count_down.png) no-repeat";
		 flag=0;
		 }
	 setTimeout("changecount()",5000);
}


function showMemo(){
	 jQuery.blockUI({ message: jQuery('#eventevent'), css: { width: '700px',top:'60px', heigth: '700px',  cursor:'default',left:'250px'} }); 	
	
		 var  currentDate = new Date();
		 sysLog("20090005",currentDate+"浏览了大事记")
	  
}
function hideMemo(){
	  jQuery.unblockUI();	
	  $("memo").innerHTML="<img onclick=\"showMemo()\" style=\"cursor:pointer\" src=\"/yh/show/css/images/memo.png\" />"; 
	  }

function init(year){
    var urls =contextPath+"/yh/show/event/act/YHEventsAct/getCanlendarAct.act";
    var rtJsons = getJsonRs(urls,"year="+year);
    if(rtJsons.rtState == '0'){
    	 var data=rtJsons.rtData.data;
    	 $("calendar").innerHTML=data;
      }else{
          alert(rtJsons.rtMsrg);
      }
	 
}

function setFontColor(day){
	day=day.replace("-","").replace("-","");
	jQuery("#calendar table td ").attr('class','');
   var str=""+day;
  
	 //jQuery(str).attr('class','selectDay');
	// document.getElementById(day+"").className="selectDay";
	$(str).className="selectDay";
}


function goBefore(year){
	 init(year+"-01");
	 getCaInfo(year+"-01");
	
}
function goNext(year){
	init(year+"-01");
	 getCaInfo(year+"-01");

	}

function getCaInfo(year){
	
     getData(year);
	   setFontColor(year);
}

function getData(year){
	var  urls =contextPath+"/yh/show/event/act/YHEventsAct/getEventsAct.act";
	   var    rtJsons = getJsonRs(urls,"year="+year);
	        var text1="";
	        var text2="";
	        var text="";
	        var x=0;
	        var y=0;
	        if(rtJsons.rtState == '0'){
	           var data=rtJsons.rtData.data;
	            for(var i=0;i<data.length;i++){
	             if(i%2==0){
	               text1+="<li>"+data[i].calTime+" &nbsp;&nbsp;"+data[i].content+"</li>";
	               x++;
	             }else{
	               text2+="<li>"+data[i].calTime+" &nbsp;&nbsp;"+data[i].content+"</li>";
	               y++
	                 }
	             }

	            for(var i=0;i<(5-x);i++){
	                 text1+="<li> &nbsp;&nbsp;</li>";
	                }

	            for(var i=0;i<(5-y);i++){
	                text2+="<li> &nbsp;&nbsp;</li>";
	               }
	       
	            if(rtJsons.rtData.data.length>0){
	             $("memo").innerHTML="<img onclick=\"showMemo()\" style=\"cursor:pointer\" src=\"/yh/show/css/images/memo1.gif\" />"; 
	                }        

	            text1="<ul>"+text1+"</ul>";
	            text2="<ul>"+text2+"</ul>";
	            $("nodataresult").innerHTML="";
	            $("ccccl").innerHTML=text1;
	            $("ccccr").innerHTML=text2;
	           
	        }else{
	          alert(rtJsons.rtMsrg);
	            }
}

</script>

<script language="javascript">
// define a couple of global variables;
var userid = null;
var keyid = null;

function initKey() {

	  <% //判断是否已经登陆，是则不出处理
	    YHPerson loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
	    if(loginUser!=null){
	   %>
	      return ; 
	  <% } %>
	  //否  读取key信息并验证
    var pkiCtrl=document.getElementById("WebCtlObj");
    var _tmp = getKeyInfo(pkiCtrl);

  //  var _tmp = "admin|A10999222291";
    
    var keyid = "";
    var userid = "";
    var cert = "";
    var Keyflag=_tmp.split("|")[0];  
  // alert(Keyflag);
                          
  if(Keyflag == "X") {    //判断key是否正常识别，不能识别跳转到error.jsp页面。
    alert("初始化pki的key设备失败！");
    location.href="error.jsp";
    return ;
  } else {
    keyid = _tmp.split("|")[0];         //keyid
    userid = _tmp.split("|")[1];        //身份证号码
    cert = _tmp.split("|")[2];
  }
    
   getUserInfoByAjax(keyid, userid);

 }
function getRightFromPMI(cert) {
    var isOK = false;
    
    jQuery.ajax({
      type: "POST",
    url: "pki/getUserRight.do",
    async: false,
    data: "cert=" + cert,
    success: function(result) {
        if ("OK" != result)
                alert("无法获得用户权限，请与管理员联系！");
            else
                isOK = true;
    },
    error: function(content) {
        alert("从PMI获取权限发生异常，请与管理员联系！");
    }
  });

  return isOK;
}

//识别用户KEY函数 
function getUserInfoByAjax(user_key_id, userid) {
    var param = 'user_key_id='+user_key_id+'&userid=' + userid + "&from=wiki";
   //  alert(param);
    var urls =contextPath+"/yh/show/inte/act/YHInteAct/getNameAct.act";
  //   alert(urls);
    var rtJsons = getJsonRs(urls,param);  //判断系统中是否存在
      if(rtJsons.rtState == '0'){
        
        var data=rtJsons.rtData;
        if(data.result == "0") {   //0 未注册   -1  未激活   其他  正确
         //   alert("没有用户名！，去注册！");
             location.href = contextPath+"/show/inte/register.jsp?"+param;
             } else if(data.result == "-1"){
                 // 账号未激活
            	  location.href = contextPath+"/show/active.jsp";
              
        }
    }else{
     alert(rtJsons.rtMsrg);
    }
}

//记录日志js函数
function sysLog(type,remark){
	var param="type="+type+"&remark="+remark;
	var urls =contextPath+"/yh/core/funcs/system/syslog/act/YHSysLogAct/addLog.act";
	var rtJsons = getJsonRs(urls,param);
	if(rtJsons.rtState != '0'){
		  alert(rtJsons.rtMsrg);
	}

	
}


// 根据模块id判断   是否有使用的权限
/** code 表
 ZDXQ 重点选区浏览权限 
 CYZL 参阅资料浏览权限 
 GZDT 工作动态浏览权限 
 GXFW 个性服务使用权限 
 */
 
function modulePriv(code){
  var param="typeId="+code;
  var urls =contextPath+"/yh/show/clasinfo/act/YHClasInfoAct/havePriv.act";
  var rtJsons = getJsonRs(urls,param);
  if(rtJsons.rtState == '0'){
	   var data = rtJsons.rtData.result;
     if(data!="1"){
       location.href="nopriv.jsp";
     }
  }

  
}

</script>


<div id="eventevent" class="event" style="display:none;">
<div align="right"><a href="javascript:hideMemo()">关闭</a></div>
<br>
<div id="calendar">

</div>
<div id="holiday">
	<div id="ccccl" class="cccc" ></div>
	<div id="ccccr" class="cccc" ></div>
</div>
<div id="nodataresult"></div>
<br/>
<br/>
<br/>
<OBJECT ID="WebCtlObj" CLASSID="CLSID:63AD4021-FF1F-47CA-A88B-DB9DC1BD75ED" style="VISIBILITY:hidden" ></OBJECT>
</div>