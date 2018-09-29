<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择印章</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src ="<%=contextPath  %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath %>/cmp/grid.css"/>
<script type="text/javascript">

var obj;
var win;
var data;

function ShowDialog(id,vTopOffset){
  if(typeof arguments[1] == "undefined")
    vTopOffset = 90;
  var bb = document.documentElement || document.body;

  $(id).style.display = 'block';

  $(id).style.left = ((bb.offsetWidth - 400)/2)+"px";
  $(id).style.top  = (bb.offsetHeight - 480)/2 + "px";
}

function HideDialog(id)
{
   $(id).style.display = 'none';
}

function doInit(){
  var d = document;
  loadGrid();
}

function loadGrid(){
  var hd =[
           [
             {header:"序号",name:"sealId",width:"20%"},
             {header:"文件",name:"sealName",width:"20%"},
             {header:"显示属性",name:"userStr",width:"25%"},
             {header:"显示属性",name:"createTime",width:"15%"},
             {name:"seqId",hidden:true},
             {name:"sealData",hidden:true}
           ],
           {
             header:"操作",
             oprates:[
                  new YHOprate('<span style="font-size:14px;color:blue;">修改密码<span>',true,function(record,index){
                    if(!data)
                      data = record.getField('sealData').value;
                    modifyPass(record.getField('sealId').value,record.getField('sealName').value);
                      })]
          }
  ];
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/sealpass/act/YHSealPassAct/getSealInfo.act?";
  var grid = new YHGrid(hd,url,null);
  grid.rendTo('grid');
}

function modifyPass(seal_id,seal_name){
   obj = $("DMakeSealV61");
  if(!obj){
    alert("尚未安装控件");
    return false;
  }

  clearSeal();//清空界面上一些控件的值，包括activex对象及五个html控件

  if(show_info(seal_id,data)){//显示印章信息（activex控件会先弹出一个密码输入窗口）
    ShowDialog("seal_pass");
  }
}
function clearSeal()
{
  var vID = 0;
  do{
      vID = obj.GetNextSeal(vID);
      if(!vID)
        break;
     obj.DelSeal(vID);
  }while(vID);

  $("SID").value="";//印章的自增ID，对应SEAL表的ID字段
  $("seal_id").innerHTML="";//印章ID，对应SEAL表的SEAL_ID字段
  $("seal_name").innerHTML=""; //印章名称，对应SEAL表的SEAL_NAME字段
  $("PASS1").value="";//新密码第一次
  $("PASS2").value="";//新密码第二次
}
function show_info(ID){
  if(data!=""){
	  //判断界面上是否已经有控件
	  var obj = document.getElementById("DMakeSealV61");
	  if(!obj){
	     alert("控件加载失败!");
	     return false;
	  }
      
	  //加载印章图片以及印章ID、名称等信息
    if(0 == obj.LoadData(data)){//执行这句代码时，首先弹出密码输入窗口（activex的密码窗口，即加载印章数据前，该控件都会先弹出密码输入窗口）
       var vID = 0;
       //获取印章的vID
       vID = obj.GetNextSeal(0);
       if(!vID){
         return true;
       }

       if(obj.SelectSeal(vID))//根据vID选中base64中的印章对象
         return false;

       var vSealID = obj.strSealID;//印章ID，对应SEAL表的SEAL_ID字段
       var vSealName = obj.strSealName;//印章名称，对应SEAL表的SEAL_NAME字段

       $("seal_id").innerHTML=vSealID;//印章ID
       $("seal_name").innerHTML=vSealName;//印章名称
       $("SID").value=ID;//ID：印章的自增ID，对应SEAL表的ID字段
       return true;
    }
    else{
      alert("读取印章数据失败");
      return false;
    }
  }
	else{
	  alert("无印章信息!");
	  return false;
	}
}
/**
 * 点击“确定”按钮对应的函数
 */
function mysubmit(){
  if($("PASS1").value != $("PASS2").value)
  {
    alert("两次密码不一致，请重新输入！");
    return;
  }
  //把新密码赋值给activex控件
  obj.strOpenPwd = $("PASS1").value;

   //生成新的base64格式的印章数据，保存到activex控件中，同时赋值给页面的隐藏域SEAL_DATA
  $("SEAL_DATA").value = obj.SaveData();

  new Ajax.Request('<%=contextPath %>/yh/core/funcs/setdescktop/sealpass/act/YHSealPassAct/updateSealData.act', {
    method: 'post',
    parameters:{
	    data:$("SEAL_DATA").value,
	    seqId:$("SID").value,
	    pass:$("PASS1").value,
	    sealName:$('seal_name').value
    },
    onSuccess: function(transport) {
      alert('密码修改成功');
      data = $("SEAL_DATA").value;
      HideDialog('seal_pass');
    },onFailure:function(){
      alert('密码修改失败');
    }
  });
}
</script>
</head>

<body topmargin="5" onload="doInit()">
<div class="PageHeader">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/seal.gif" align="absmiddle"><span class="big3"> 我的印章</span>&nbsp;
    </td>
  </tr>
</table>
</div>
<div id="grid" style="width:100%;text-align: center;"></div>
<div id="seal_pass" class="ModalDialog" style="width:400px;height: 480px;">
	<div class="header">
    <span id="title" class="title"> 印章密码修改</span>
    <a class="operation" href="javascript:HideDialog('seal_pass');">
      <img src="<%=imgPath%>/close.png"/>
    </a>
  </div>
  <div id="seal_pass_body" class="body bodycolor">
    <table class="TableBlock" width="100%" align="center">
      <tr>
        <td class="TableData" colspan=2 align="center">
          <OBJECT 
              id=DMakeSealV61 
              style="left: 0px; top: 0px" 
              classid="clsid:3F1A0364-AD32-4E2F-B550-14B878E2ECB1" 
              VIEWASTEXT 
              width=200
              height=200
              codebase='<%=contextPath%>/core/funcs/system/sealmanage/sealmaker/MakeSealV6.ocx#version=1,0,2,8'>
            <PARAM NAME="_Version" VALUE="65536">
            <PARAM NAME="_ExtentX" VALUE="2646">
            <PARAM NAME="_ExtentY" VALUE="1323">
            <PARAM NAME="_StockProps" VALUE="0">
          </OBJECT>
        </td>
      </tr>
      <tr>
        <td class="TableContent" width=80>印章ID</td>
        <td class="TableData"><span id="seal_id"></span></td>
      </tr>
      <tr>
        <td class="TableContent">印章名称</td>
        <td class="TableData"><span id="seal_name"></span></td>
      </tr>         
      <tr>
        <td class="TableContent" >新密码：</td>
        <td class="TableData" >
          <input type="password" id="PASS1"  class="BigInput" size="20">
        </td>
      </tr>
     
      <tr>
        <td class="TableContent" >确认新密码：</td>
        <td class="TableData" >
          <input type="password" id="PASS2"  class="BigInput" size="20">
        </td>
      </tr>   
      <tr class="TableControl">
        <td class="TableData" colspan=2 align=center>
        <input type="hidden" id="SID" value=""><!--印章的自增ID，对应SEAL表的ID字段-->
        <input type="hidden" id="SEAL_DATA" value=""><!--印章的base64格式的数据-->
        <input type="button" onclick="mysubmit()" class=BigButton value="确认">
        </td>
      </tr> 
    </table>        
  </div>
</div>
</body>
</html>
