<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
    <script type="text/javascript" src="js/prototype.js"><script>
    <script type="text/javascript" src="js/ExchanageSelectbox.js"><script>
 	<script type="text/javascript">
 	alert('d');
 	function test(){

 	}
/*
 	function func_find(select_obj,option_text)
{
 pos=option_text.indexOf("] ")+1;
 option_text=option_text.substr(0,pos);

 for (j=0; j<select_obj.options.length; j++)
 {
   str=select_obj.options[j].text;
   if(str.indexOf(option_text)>=0)
      return j;
 }//for

 return j;
}

function func_color(select_obj)
{
 font_color="red";
 option_text="";
 for (j=0; j<select_obj.options.length; j++)
 {
   str=select_obj.options[j].text;
   if(str.indexOf(option_text)<0)
   {
      if(font_color=="red")
         font_color="blue";
      else
         font_color="red";
   }
   select_obj.options[j].style.color=font_color;

   pos=str.indexOf("] ")+1;
   option_text=str.substr(0,pos);
 }//for

 return j;
}

function func_insert()
{
 for (i=form1.select2.options.length-1; i>=0; i--)
 {
   if(form1.select2.options[i].selected)
   {
     option_text=form1.select2.options[i].text;
     option_value=form1.select2.options[i].value;
     option_style_color=form1.select2.options[i].style.color;

     var my_option = document.createElement("OPTION");
     my_option.text=option_text;
     my_option.value=option_value;
     my_option.style.color=option_style_color;

     pos=func_find(form1.select1,option_text);
     form1.select1.options.add(my_option,pos);
     form1.select2.remove(i);
  }
 }//for

 func_init();
}

function func_delete()
{
 for (i=form1.select1.options.length-1; i>=0; i--)
 {
   if(form1.select1.options[i].selected)
   {
     option_text=form1.select1.options[i].text;
     option_value=form1.select1.options[i].value;

     var my_option = document.createElement("OPTION");
     my_option.text=option_text;
     my_option.value=option_value;

     pos=func_find(form1.select2,option_text);
     form1.select2.options.add(my_option,pos);
     form1.select1.remove(i);
  }
 }//for

 func_init();
}

function func_select_all1()
{
 for (i=form1.select1.options.length-1; i>=0; i--)
   form1.select1.options[i].selected=true;
}

function func_select_all2()
{
 for (i=form1.select2.options.length-1; i>=0; i--)
   form1.select2.options[i].selected=true;
}*/
function _init()
{
     var selected = [{id:'1',name:'公告通知'},{id:'2',name:'内部邮件'},{id:'3',name:'日程安排'}];
      var disselected = [{id:'4',name:'网络会议'},{id:'6',name:'工资上报'},{id:'5',name:'车辆申请'},{id:'7',name:'工作计划'}];
 	  new ExchanageSelectbox('parent',selected,disselected);
}
</script>
</head>

<body class="bodycolor" topmargin="5" onLoad="test();">
<div id="parent"></div>

<div>
<div>
    <select  name="select1" ondblclick="func_delete();" MULTIPLE style="width:200;height:280">
             <option value="1">公告通知</option>
              <option value="2">内部邮件</option>
              <option value="5">日程安排</option>
              <option value="7">工作流:提醒下一步经办人</option>
           </select>
    <input type="button" value=" 全 选 " onClick="func_select_all1();" class="SmallInput">
   </div>
   <div>
      <input type="button" class="SmallInput" value=" ← " onClick="func_insert();">
      <input type="button" class="SmallInput" value=" → " onClick="func_delete();">
    </div>
   <div>
    <select  name="select2" ondblclick="func_insert();" MULTIPLE style="width:200;height:280">
             <option value="3">网络会议</option>
              <option value="4">工资上报</option>
              <option value="6">考勤批示</option>
              <option value="8">会议申请</option>
              <option value="9">车辆申请</option>
              <option value="11">投票提醒</option>
              <option value="12">工作计划</option>
              <option value="13">工作日志</option>
              <option value="14">新闻</option>
              <option value="15">考核</option>
              <option value="16">公共文件柜</option>
              <option value="17">网络硬盘</option>
              <option value="18">内部讨论区</option>
              <option value="19">工资条</option>
              <option value="20">个人文件柜</option>
              <option value="35">销售合同提醒</option>
              <option value="40">工作流:提醒流程发起人</option>
              <option value="41">工作流:提醒流程所有人员</option>
              <option value="42">项目管理</option>
              <option value="22">审核提醒</option>
              <option value="30">培训课程</option>
              <option value="31">课程报名</option>
              <option value="32">培训调查</option>
              <option value="33">培训信息</option>
              <option value="34">效果评估</option>
              <option value="43">办公用品审批</option>
              <option value="23">即时通讯离线消息</option>
              <option value="24">上线提醒</option>
              <option value="44">网络传真</option>
              <option value="45">日程安排-周期性事务</option>
              <option value="37">档案管理</option>
              <option value="a0">报表提示</option>
           </select>
    <input type="button" value=" 全 选 " onClick="func_select_all2();" class="SmallInput">
  </div>
   </div>
</body>
</html>