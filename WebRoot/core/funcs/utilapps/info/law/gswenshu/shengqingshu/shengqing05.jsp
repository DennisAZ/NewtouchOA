<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>法律法规查询</TITLE>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<style type="text/css">
<!--a            { text-decoration: none; font-size: 9pt; color: black; font-family: 宋体 }
.text        { font-size: 9pt; font-family: 宋体 }
.text1       { color: #0000A0; font-size: 11pt; font-family: 宋体 }
.text2       { color: #008080; font-size: 9pt; font-family: 宋体 }
.text3       { color: #0F8A91; font-size: 11pt; font-family: 宋体 }
.l100        { line-height: 14pt; font-size: 9pt }
td           { font-family: 宋体; font-size: 9pt; line-height: 13pt }
input        { font-size: 9pt; font-family: 宋体 }
p            { font-size: 9pt; font-family: 宋体 }
-->
</STYLE>

</HEAD>
<BODY class="bodycolor" topmargin="0">
<BR>
<table width="500" border="0" cellspacing="1" cellpadding="3" align="center" class="TableBlock">
  <tr> 
	<td height="27" class="TableHeader"> 
	  <div align="center">发明专利请求书</font></div>
	</td>
  </tr>
  <tr> 
	<td height="54" valign="top" class="TableData"> 
	  <p><br>
		　　请按照本表背面“填表注意事项”正确填写本表各栏&nbsp;&nbsp;&nbsp; 此框内容由专利局填写</p>
	  <table width="600" border="0" cellspacing="0" cellpadding="0" height="22" align="center">
		<tr> 
		  <td width="379">&nbsp;</td>
		  <td width="221"> 
			<p style='line-height:14.0pt;'>←收件人地址</p>
			<p style='line-height:14.0pt;'>←收件人姓名</p>
		  </td>
		</tr>
	  </table>
	  <table border=1 cellspacing=0 cellpadding=0 width=599 align="center">
		<tr> 
		  <td width=79 rowspan=2 > 
			<p >⑤发明</p>
			<p >名称</p>
		  </td>
		  <td width=311 colspan=4 rowspan=2 valign=top >&nbsp; </td>
		  <td width=209 colspan=3 valign=top > 
			<p >①申请号（发明）</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=209 colspan=3 valign=top > 
			<p >②分案提交日</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=2 > 
			<p >⑥发明人</p>
		  </td>
		  <td width=311 colspan=4 rowspan=2 valign=top >&nbsp; </td>
		  <td width=209 colspan=3 valign=top > 
			<p >③申请日</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=209 colspan=3 valign=top > 
			<p >④</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=6 > 
			<p >⑦申请人</p>
		  </td>
		  <td width=520 colspan=7 valign=top > 
			<p >姓名或名称（代表）&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  国籍或所在地国家</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >邮政编码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 地址&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  联系人姓名</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >姓名或名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  国籍或所在地国家</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >邮政编码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 地址</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >姓名或名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  &nbsp;&nbsp;&nbsp;国籍或所在地国家</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >邮政编码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 地址</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=3 > 
			<p >⑧专利代</p>
			<p >理机构</p>
		  </td>
		  <td width=520 colspan=7 valign=top > 
			<p >名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  代理机构代码</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >邮政编码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 地址</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >代理人姓名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  代理人证书号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 电话</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=2 > 
			<p >⑨菌种</p>
			<p >保藏</p>
		  </td>
		  <td width=520 colspan=7 valign=top > 
			<p >保藏单位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  地址</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=520 colspan=7 valign=top > 
			<p >保藏日期&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  保藏编号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			  分类命名</p>
		  </td>
		  <td width=0 height=34 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=2 valign=top > 
			<p >⑩分案</p>
			<p >申请</p>
		  </td>
		  <td width=311 colspan=4 valign=top > 
			<p >原案申请号</p>
		  </td>
		  <td width=74 rowspan=4 > 
			<p >⑿</p>
			<p >不丧失</p>
			<p >新颖性</p>
			<p >宽限期</p>
			<p >声明</p>
		  </td>
		  <td width=134 rowspan=4 > 
			<p  style='line-height:15.0pt;'><span
  style='font-family:宋体;"Times New Roman"'>□已在中国政府主办或承认的国际展览会上首次展出</span></p>
			<p  style='line-height:15.0pt;'><span
  style='font-family:宋体;"Times New Roman"'>□已在规定的学术会议或技术会议上首次发表</span></p>
			<p style='line-height:15.0pt;'>□</p>
		  </td>
		  <td width=0 >&nbsp; </td>
		</tr>
		<tr> 
		  <td width=311 colspan=4 valign=top > 
			<p >原案申请日</p>
		  </td>
		  <td width=0 height=29 ></td>
		</tr>
		<tr> 
		  <td width=79 rowspan=4 valign=top > 
			<p >⑾要求</p>
			<p >优先</p>
			<p >权声</p>
			<p >明</p>
		  </td>
		  <td width=96 valign=top > 
			<p >在先申请日</p>
		  </td>
		  <td width=103 valign=top > 
			<p >在先申请号</p>
		  </td>
		  <td width=112 valign=top > 
			<p >在先申请国别</p>
		  </td>
		  <td width=0 height=29 ></td>
		</tr>
		<tr> 
		  <td width=96 rowspan=3 valign=top >&nbsp; </td>
		  <td width=103 rowspan=3 valign=top >&nbsp; </td>
		  <td width=112 rowspan=3 valign=top >&nbsp; </td>
		  <td width=0 height=29 ></td>
		</tr>
		<tr> 
		  <td width=74 > 
			<p >⒀保密</p>
			<p >请求</p>
		  </td>
		  <td width=134 > 
			<p style='line-height:16.0pt;'>□本专利申请可能涉及国家重大利益，请求保密处理</p>
		  </td>
		  <td width=0 height=95 ></td>
		</tr>
		<tr> 
		  <td width=209 colspan=3 valign=top > 
			<p >⒁权利要求项数每份&nbsp;&nbsp; 项</p>
			<p >说明书&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 每份&nbsp;&nbsp;&nbsp; 项</p>
			<p >说明书附图&nbsp;&nbsp; 每份&nbsp;&nbsp;&nbsp; 项</p>
		  </td>
		  <td width=0 height=30 ></td>
		</tr>
		<tr> 
		  <td width=79 valign=top > 
			<p >⒂发明</p>
			<p >名称</p>
		  </td>
		  <td width=520 colspan=7 valign=top >&nbsp; </td>
		  <td width=0 height=30 ></td>
		</tr>
		<tr> 
		  <td width=247 colspan=3 valign=top >&nbsp; </td>
		  <td width=199 colspan=3 valign=top >&nbsp; </td>
		  <td width=153 valign=top >&nbsp; </td>
		  <td width=0 height=8 ></td>
		</tr>
		<tr> 
		  <td width=247 colspan=3 valign=top > 
			<p >⒃</p>
			<p >申请文件清单</p>
			<p >1、请求书&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
			<p >2、权利要求书&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
			<p >3、说明书&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
			<p >4、说明书附图&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
			<p >5、说明书摘要&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
			<p >6、摘要附图&nbsp;&nbsp;&nbsp; 份&nbsp; 每份&nbsp;&nbsp; 页</p>
		  </td>
		  <td width=199 colspan=3 valign=top > 
			<p >⒄</p>
			<p >附加文件清单</p>
			<p >□专利代理委托书</p>
			<p >□费用减缓请求书</p>
			<p >□在先申请文件副本</p>
			<p >□</p>
			<p >□</p>
			<p >□</p>
		  </td>
		  <td width=153 valign=top > 
			<p >⒅</p>
			<p >专利局审批</p>
		  </td>
		  <td width=0 height=30 ></td>
		</tr>
		<tr> 
		  <td width=599 colspan=8 valign=top > 
			<p >⒆&nbsp; 申请人或代理机构签章</p>
		  </td>
		  <td width=0 height=30 ></td>
		</tr>
		<tr> 
		  <td width=599 colspan=8 valign=top > 
			<p >⒇&nbsp; 收件人地址、姓名</p>
			<table cellpadding=0 cellspacing=0 align=left>
			  <tr> 
				<td width=0 height=9 ></td>
				<td width=207 ></td>
				<td width=9 ></td>
				<td width=146 ></td>
			  </tr>
			  <tr> 
				<td height=116 ></td>
				<td rowspan=2 width=207 height=117 align=left valign=top bgcolor=white > 
				  <table cellpadding=0 cellspacing=0 width="100%">
					<tr> 
					  <td > 
						<p>&nbsp; □□□□□□</p>
					  </td>
					</tr>
				  </table>
				</td>
				<td ></td>
				<td width=146 height=116 align=left valign=top bgcolor=white >&nbsp;</td>
			  </tr>
			  <tr> 
				<td height=1 ></td>
			  </tr>
			</table>
			  </td>
		  <td width=0 height=30 ></td>
		</tr>
		<tr height=0> 
		  <td width=79 ></td>
		  <td width=96 ></td>
		  <td width=72 ></td>
		  <td width=31 ></td>
		  <td width=112 ></td>
		  <td width=56 ></td>
		  <td width=18 ></td>
		  <td width=134 ></td>
		  <td width=0 height=30 ></td>
		</tr>
	  </table>
	</td>
  </tr>
</table>
<br><center>
<input type="button" class="BigButton" value="回上一层" onclick="history.go(-1)">
<input type="button" class="BigButton" value="回主目录" onclick="location='../../index.jsp';">
</center><br>
</html>
