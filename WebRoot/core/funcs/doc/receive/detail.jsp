<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<style media=print>
.Noprint {
	display: none;
}

.PageNext {
	page-break-after: always;
}
</style>
<script>
function userPrint(num){	 
  try{ 
    var wb = document.getElementById("wb");
    wb.Execwb(num,1);//打印预览
  }catch(e){
    alert("如不能打印或打印预览，请调整安全级别！具体方法如下：\n根据当前页面所属安全区域，自定义安全设置，找到设置项“对未标记为可安全执行的脚本的Activex控件进行初始化并执行脚本”，将其设置为“提示”即可。");
  }
}
</script>
</head>

<body style="heigth: 100%;">
<DIV align=center>
<DIV style="WIDTH: 850px" align=center>
<DIV align=right>
<TABLE
	style="BORDER-BOTTOM: medium none; BORDER-LEFT: medium none; PADDING-LEFT: 500px; BORDER-COLLAPSE: collapse; BORDER-TOP: medium none"
	border=1 cellSpacing=0 cellPadding=0 width=214>
	<TBODY>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; WIDTH: 160.2pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=214>
			<DIV align=center><SPAN style="COLOR: red; FONT-SIZE: 14pt">市外办收文</SPAN></DIV>
			</TD>
		</TR>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; WIDTH: 160.2pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=214>
			<DIV align=center><SPAN style="COLOR: red; FONT-SIZE: 14pt"><INPUT
				style="TEXT-ALIGN: left" id=DATA_1 title=文号 align=left name=DATA_1>号</SPAN></DIV>
			</TD>
		</TR>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; WIDTH: 160.2pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=214>
			<DIV style="TEXT-ALIGN: center; TEXT-INDENT: 7pt" align=center><SPAN
				style="COLOR: red; FONT-SIZE: 14pt"><INPUT id=DATA_2
				class=AUTO title=年月日 dataFld=SYS_DATE_CN value="" name=DATA_2
				classname="AUTO">&nbsp;</SPAN></DIV>
			</TD>
		</TR>
	</TBODY>
</TABLE>
<center class="Noprint">
<div id=MyWebBrowser><OBJECT id=wb
	classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0
	VIEWASTEXT></OBJECT></div>
<span><input title="关闭" class="BigButton"
	onclick="userPrint('45');" type="button" value="关闭 " /></span> <span><input
	title="打印" class="BigButton" onclick="userPrint('6');" type="button"
	value="打印" /></span> <span><input title="打印预览" class="BigButton"
	onclick="userPrint('7');" type="button" value="打印预览 " /></span></center>
<BR>
<DIV align=center><B><SPAN style="COLOR: red; FONT-SIZE: 26pt">北京市人民政府外事办公室公文批办单</SPAN></B></DIV>
<DIV align=center>
<TABLE
	style="BORDER-BOTTOM: medium none; BORDER-LEFT: medium none; BORDER-COLLAPSE: collapse; BORDER-TOP: medium none; BORDER-RIGHT: medium none"
	border=1 cellSpacing=0 cellPadding=0 width=850>
	<TBODY>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=55>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">等级</SPAN></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=107>
			<DIV align=left>&nbsp;<INPUT
				style="TEXT-ALIGN: left; WIDTH: 80px; HEIGHT: 22px" id=DATA_14
				title=等级 align=left name=DATA_14></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=70>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">密级</SPAN></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=103>
			<DIV align=left>&nbsp;<INPUT
				style="TEXT-ALIGN: left; WIDTH: 80px; HEIGHT: 22px" id=DATA_13
				title=密级 align=left name=DATA_13></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=100>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">收文日期</SPAN></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				width=134>
			<DIV align=center><INPUT
				style="TEXT-ALIGN: left; WIDTH: 100px; HEIGHT: 22px" id=DATA_11
				title=收文日期 align=left name=DATA_11></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=78>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">承办人</SPAN></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: red 1pt solid; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=114>
			<DIV align=left>&nbsp;<INPUT
				style="TEXT-ALIGN: left; WIDTH: 100px; HEIGHT: 22px" id=DATA_12
				title=承办人 align=left name=DATA_12></DIV>
			</TD>
		</TR>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=335 colSpan=4>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">来文单位：<INPUT
				style="TEXT-ALIGN: left; WIDTH: 200px; HEIGHT: 22px" id=DATA_9
				title=来文单位 align=left name=DATA_9></SPAN></DIV>
			</TD>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: #f0f0f0; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=426 colSpan=4>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">原文编号：<INPUT
				style="TEXT-ALIGN: left; WIDTH: 200px; HEIGHT: 22px" id=DATA_10
				title=原文编号 align=left name=DATA_10></SPAN></DIV>
			</TD>
		</TR>
		<TR>
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=761 colSpan=8>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">标题：<INPUT
				style="TEXT-ALIGN: left; WIDTH: 543px; HEIGHT: 22px" id=DATA_8
				title=标题 align=left name=DATA_8></SPAN></DIV>
			</TD>
		</TR>
		<TR style="HEIGHT: 218.6pt">
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; HEIGHT: 218.6pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=761 colSpan=8>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">领导批示：<BR>
			<TEXTAREA style="WIDTH: 830px; HEIGHT: 300px" id=DATA_5 title=领导批示
				name=DATA_5
				src="/yh/core/funcs/doc/flowform/editor/plugins/NTextArea/textarea.gif"></TEXTAREA></SPAN></DIV>
			</TD>
		</TR>
		<TR style="HEIGHT: 113pt">
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; HEIGHT: 113pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=761 colSpan=8>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">拟办意见：<BR>
			<TEXTAREA style="WIDTH: 830px; HEIGHT: 200px" id=DATA_6 title=拟办意见
				name=DATA_6
				src="/yh/core/funcs/doc/flowform/editor/plugins/NTextArea/textarea.gif"></TEXTAREA></SPAN></DIV>
			</TD>
		</TR>
		<TR style="HEIGHT: 48.85pt">
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; HEIGHT: 48.85pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=761 colSpan=8>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">附注：<BR>
			<TEXTAREA style="WIDTH: 830px; HEIGHT: 50px" id=DATA_7 title=附注
				name=DATA_7
				src="/yh/core/funcs/doc/flowform/editor/plugins/NTextArea/textarea.gif"></TEXTAREA></SPAN></DIV>
			</TD>
		</TR>
		<TR style="HEIGHT: 113pt">
			<TD
				style="BORDER-BOTTOM: red 1pt solid; BORDER-LEFT: red 1pt solid; PADDING-BOTTOM: 0cm; BACKGROUND-COLOR: transparent; PADDING-LEFT: 5.4pt; PADDING-RIGHT: 5.4pt; HEIGHT: 113pt; BORDER-TOP: #f0f0f0; BORDER-RIGHT: red 1pt solid; PADDING-TOP: 0cm"
				vAlign=top width=761 colSpan=8>
			<DIV align=left><SPAN style="COLOR: red; FONT-SIZE: 16pt">处室意见：<BR>
			<TEXTAREA style="WIDTH: 830px; HEIGHT: 200px" id=DATA_7 title=处室意见
				name=DATA_6
				src="/yh/core/funcs/doc/flowform/editor/plugins/NTextArea/textarea.gif"></TEXTAREA></SPAN></DIV>
			</TD>
		</TR>
	</TBODY>
</TABLE>
</DIV>
<P><SPAN style="COLOR: red; FONT-SIZE: 14pt">&nbsp;&nbsp;<SPAN
	style="FONT-FAMILY: 宋体; COLOR: red; FONT-SIZE: 16pt; mso-ascii-font-family: Calibri; mso-ascii-theme-font: minor-latin; mso-fareast-theme-font: minor-fareast; mso-hansi-font-family: Calibri; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: ZH-CN; mso-bidi-language: AR-SA">联系人：<INPUT
	style="TEXT-ALIGN: left" id=DATA_3 title=联系人 align=left name=DATA_3></SPAN>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
&nbsp; <SPAN style="FONT-SIZE: large"><SPAN
	style="FONT-FAMILY: 宋体; COLOR: red; FONT-SIZE: 16pt; mso-ascii-font-family: Calibri; mso-ascii-theme-font: minor-latin; mso-fareast-theme-font: minor-fareast; mso-hansi-font-family: Calibri; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: ZH-CN; mso-bidi-language: AR-SA">联系电话：</SPAN></SPAN><INPUT
	style="TEXT-ALIGN: left" id=DATA_4 title=联系电话 align=left name=DATA_4>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</SPAN><SPAN style="FONT-SIZE: large"><SPAN style="COLOR: red"><SPAN
	style="FONT-FAMILY: 宋体; COLOR: red; FONT-SIZE: 16pt; mso-ascii-font-family: Calibri; mso-ascii-theme-font: minor-latin; mso-fareast-theme-font: minor-fareast; mso-hansi-font-family: Calibri; mso-hansi-theme-font: minor-latin; mso-bidi-font-family: 'Times New Roman'; mso-bidi-theme-font: minor-bidi; mso-ansi-language: EN-US; mso-fareast-language: ZH-CN; mso-bidi-language: AR-SA" title="部门">市外办秘书处</SPAN></SPAN></SPAN></P>
</DIV>
</DIV>
</body>
</html>