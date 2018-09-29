<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>

<%@page import="yh.subsys.oa.finance.data.YHChargeExpense"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
List<YHChargeExpense> list = (List<YHChargeExpense> )request.getAttribute("list");
YHChargeExpense expense = new YHChargeExpense();
%>
<html>
<head>
<title>报销单</title>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}
.tdShu {
  border-top-style: none;
  border-right-style: none;
  border-bottom-style: none;
}
 </style>  
<script language="JavaScript"> 
var hkey_root,hkey_path,hkey_key
hkey_root="HKEY_CURRENT_USER"
hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"
//设置网页打印的页眉页脚为空
function pagesetupNull(){
try{
var RegWsh = new ActiveXObject("WScript.Shell")
hkey_key="header" 
RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
hkey_key="footer"
RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"")
}catch(e){}
}
</script>
</head>
<body style="margin:0;padding:0;" onload="pagesetupNull()">
 <center  class = "Noprint">  
 <OBJECT id = WebBrowser classid = CLSID:8856F961 - 340A - 11D0 - A96B - 00C04FD705A2 height = 0  width = 0>  
 </OBJECT>  
 <font color='red'>打印此界面时请先打印预览->页面设置->页眉页脚全设置为空（此行文字不会显示在打印效果中）</font>  
 <hr align = "center"  width = "90%"  size = "1"  noshade>  
 </center>
 <%if(list.size() > 0){
  for (int i = 0 ; i < list.size(); i++) {
    expense = list.get(i);
%>
<div SCROLL=no align='center'>
<table width='80%' >
<tr>
<td align='center' colspan = '2'>
<div style="TEXT-ALIGN: center; COLOR: blank"><SPAN style="FONT-SIZE: 30px"><b>费&nbsp;&nbsp;用&nbsp;&nbsp;报&nbsp;&nbsp;销&nbsp;&nbsp;单</b></SPAN></div>
</td>
</tr>
<tr>
<td align='left'><SPAN style="FONT-SIZE: 17px"> 部室 ：<%=expense.getDeptId()%></SPAN></td>
<td align='right'><SPAN style="FONT-SIZE: 17px"><%=expense.getChargeDate().toString().substring(0,4)%> 年 <%=expense.getChargeDate().toString().substring(5,7)%>月 <%=expense.getChargeDate().toString().substring(8,10)%>日</SPAN></td>
</tr>
<tr>
<td align='left'><SPAN style="FONT-SIZE: 17px">  团名：  <%if(expense.getChargeMemo()!= null) {%><%=expense.getChargeMemo()%><%} %></SPAN></td>
<td align='right'><SPAN style="FONT-SIZE: 17px">申请人：<%=expense.getChargeUser()%></SPAN></td>
</tr>
</table>

  <table width="86%" style="border:1px #000 solid;line-height:20px;border-collapse:collapse;padding:3px;">
  <tr height=47 class="tdShu">
  <td nowrap width="20%" align=center style="border-top:1px #000 solid; border-right:1px #000 solid;font-weight:bold;line-height:25px;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;项&nbsp;&nbsp;&nbsp;目&nbsp;&nbsp;</span></td>
  <td nowrap width="50%" align=center style="border-top:1px #000 solid; border-right:1px #000 solid;font-weight:bold;line-height:25px;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;&nbsp;摘&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;要&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
  <td nowrap width="7%" align=center style="border-top:1px #000 solid; border-right:1px #000 solid;font-weight:bold;line-height:25px;"><SPAN style="FONT-SIZE: 17px">支票</span></td>
  <td nowrap width="23%" align=center style="border-top:1px #000 solid; border-right:1px #000 solid;font-weight:bold;line-height:25px;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;金&nbsp;&nbsp;&nbsp;额&nbsp;&nbsp;</span></td>
  <td nowrap rowspan="100">
<span style="writing-mode: tb-rl; text-align: center"><b>
  原&nbsp;始&nbsp;凭&nbsp;证&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张</b></span>
</td>
  </tr>
 <% 
 double sunMoney = 0;
 double bol = 0;
 boolean flage = false;
 if(expense.getChargeItem()!=null&&!expense.getChargeItem().equals("")) {
           String detailContent = expense.getChargeItem();
           String dcs[] = detailContent.split("\n");
           for(int j = 0; j < dcs.length;j++){
             String dc= dcs[j];
             String[] temp = dc.split("`");
             String item = "";
             String person = "";
             String days = "";
             String price = "";
              for(int k = 0; k<temp.length;k++){
                if(k==0){
                  item = temp[0];
                }
                if(k==1){
                  person = temp[1];
                }
                if(k==2){
                  days = temp[2];
                }
                if(k==3){
                  price = temp[3];
                  try {
                    bol = Double.parseDouble(price);
                    flage = true;
                  } catch(Exception ex){
                    flage = false;
                  }
                  if (flage) {
                    sunMoney = sunMoney + Double.parseDouble(price);
                  }
                }
              }
             %> 
           <tr height=47>
            <td nowrap width="20%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px"><%=item %></SPAN></td>
            <td nowrap width="50%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px"><%=person%></SPAN></td>
            <td nowrap width="7%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px"><%=days%></SPAN></td>
            <td nowrap width="23%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">￥<%=YHUtility.getFormatedStr(price,2)%></SPAN></td>
         </tr>
         <%}
         } %>
    <tr height=47>
  <td nowrap width="20%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
  <td nowrap width="50%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
  <td nowrap width="7%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;</span></td>
  <td nowrap width="23%" align=left style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;</span></td>
  </tr>
      <tr height=47>
  <td nowrap width="20%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
  <td nowrap width="50%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>
  <td nowrap width="7%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;</span></td>
  <td nowrap width="23%" align=left style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;&nbsp;&nbsp;</span></td>
  </tr>
  <%
  String money = "";
  String s = String.valueOf(sunMoney);
  // 将这个数转换成 double 类型，并对其进行四舍五入操作 
  try {
    double d = Double.parseDouble(s);  
    if(d < 0) {    
      return ;   
    }
    // 如果传入的是空串则继续返回空串   
    if("".equals(s)) {   
      return ;   
    }   
    // 此操作作用在小数点后两位上   
    d = (d * 100 + 0.5) / 100;   
    // 将 d 进行格式化   
    s = new java.text.DecimalFormat("##0.000").format(d);   
    // 以小数点为界分割这个字符串   
    int index = s.indexOf(".");   
    // 这个数的整数部分   
    String intOnly = s.substring(0, index);   
    // 规定数值的最大长度只能到万亿单位，否则返回 "0"   
    if(intOnly.length() > 13) {  
      return ;   
    }   
    // 这个数的小数部分   
    String smallOnly = s.substring(index + 1);   
    // 如果小数部分大于两位，只截取小数点后两位   
    if(smallOnly.length() > 2) {   
      String roundSmall = smallOnly.substring(0, 2);   
      // 把整数部分和新截取的小数部分重新拼凑这个字符串   
      s = intOnly + "." + roundSmall;   
    }  

    int flag = 1;
    int flag2 = 2;
    // 用来存放转换后的新字符串   
    String sfNews = "";
    String sfNews2 = "";  
    // 以小数点为界分割这个字符串   
    int index2 = s.indexOf(".");   
    // 截取并转换这个数的整数部分   
    String intOnly2 = s.substring(0,index2);
    // 截取并转换这个数的小数部分   
    String smallOnly2 = s.substring(index2 + 1);
    
    if (flag == 1) {
      int sLength = intOnly2.length();   
      // 货币大写形式   
      String bigLetter[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};   
      // 货币单位   
      String unit[] = {"元", "拾", "佰", "仟", "万",    
          // 拾万位到仟万位   
          "拾", "佰", "仟",   
          // 亿位到万亿位   
          "亿", "拾", "佰", "仟", "万"};     
      // 逐位替换为中文大写形式   
      for(int m = 0; m < sLength; m ++) {   
        if(flag == 1) {   
          // 转换整数部分为中文大写形式（带单位）   
          sfNews = sfNews + bigLetter[intOnly2.charAt(m) - 48] + unit[sLength - m - 1];   
        }
      }  
    }
    String part12 = sfNews;
    if (flag2 == 2) {
      int sLength = smallOnly2.length();   
      // 货币大写形式   
      String bigLetter[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};  
      String small[] = {"分", "角"};    
      // 逐位替换为中文大写形式   
      for(int m = 0; m < sLength; m ++) {
        if(flag == 2) {   
          // 转换小数部分（带单位）   
          sfNews2 = sfNews2 + bigLetter[smallOnly2.charAt(m) - 48] + small[sLength - m - 1];   
        }   
      }   
    }
    String part2 = sfNews2;   
    // 把转换好了的整数部分和小数部分重新拼凑一个新的字符串   
    String newS = part12 + part2;     

    // 如果用户开始输入了很多 0 去掉字符串前面多余的'零'，使其看上去更符合习惯   
    while(newS.charAt(0) == '零') {   
      // 将字符串中的 "零" 和它对应的单位去掉   
      newS = newS.substring(2);   
      // 如果用户当初输入的时候只输入了 0，则只返回一个 "零"   
      if(newS.length() == 0) {   
        newS = "零";   
      }   
    }   
    // 字符串中存在多个'零'在一起的时候只读出一个'零'，并省略多余的单位   
    /* 由于本人对算法的研究太菜了，只能用4个正则表达式去转换了，各位大虾别介意哈... */  
    String regex1[] = {"零仟", "零佰", "零拾"};   
    String regex2[] = {"零亿", "零万", "零元"};   
    String regex3[] = {"亿", "万", "元"};   
    String regex4[] = {"零角", "零分"};   
    // 第一轮转换把 "零仟", 零佰","零拾"等字符串替换成一个"零"   
    for(int m = 0; m < 3; m ++) {   
      newS = newS.replaceAll(regex1[m], "零");   
    }   
    // 第二轮转换考虑 "零亿","零万","零元"等情况   
    // "亿","万","元"这些单位有些情况是不能省的，需要保留下来   
    for(int m = 0; m < 3; m ++) {   
      // 当第一轮转换过后有可能有很多个零叠在一起   
      // 要把很多个重复的零变成一个零   
      newS = newS.replaceAll("零零零", "零");   
      newS = newS.replaceAll("零零", "零");   
      newS = newS.replaceAll(regex2[m], regex3[m]);   
    }   
    // 第三轮转换把"零角","零分"字符串省略   
    for(int m = 0; m < 2; m ++) {   
      newS = newS.replaceAll(regex4[m], "");   
    }   
    // 当"万"到"亿"之间全部是"零"的时候，忽略"亿万"单位，只保留一个"亿"   
    newS = newS.replaceAll("亿万", "亿");
    money = newS;
    //return newS;
  }catch (Exception e) {
    money = "";
  }
  %>
      <tr height=47>
  <td nowrap width="20%" align=center style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">合   计  </span></td>
  <td nowrap width="50%" align=left style="border-top:1px #000 solid;   border-right:1px #000 solid;">
<span style="FONT-SIZE: 18px">&nbsp;人民币&nbsp;<br>（大写）&nbsp;&nbsp;</span>
  <span style="FONT-SIZE: 18px"><%if(!YHUtility.isNullorEmpty(money)){%><%=money%><%}else{%>零<%}%></span></td>
  <td nowrap width="7%" colspan="2" align=left style="border-top:1px #000 solid;   border-right:1px #000 solid;"><SPAN style="FONT-SIZE: 17px">&nbsp;</span>
  <span style="FONT-SIZE: 18px">&nbsp;人民币&nbsp;<br>（小写）&nbsp;&nbsp;</span>
  <span style="FONT-SIZE: 18px">￥<%=YHUtility.getFormatedStr(sunMoney,2)%> </span>
  </td>
  </tr>
</table>
<table width="73%">
<tr>
  <td width=35%><SPAN style="FONT-SIZE: 18px">&nbsp;负责人：&nbsp;&nbsp;</span>
  </td>
  <td width=35%><SPAN style="FONT-SIZE: 18px">&nbsp;经手人：&nbsp;&nbsp;</span>
  </td>
  <td width=30%><SPAN style="FONT-SIZE: 18px">&nbsp;验收人：&nbsp;&nbsp;</span>
  </td>
</tr>
</table>
</div>
<br>
<br>
<%} }%>
</body>
</html>