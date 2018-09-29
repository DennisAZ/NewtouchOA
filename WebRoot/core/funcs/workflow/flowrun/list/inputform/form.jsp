<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<div id="formDiv" align=center>
</div>
<div id="attachDiv" align=center>
<table width="90%" cellspacing="0" cellpadding="3" bordercolor="#b8d1e2" border="1" align="center" class="TableList" style="border-collapse: collapse;">
    <tbody><tr class="TableHeader">
      <td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> 公共附件区</div></td>
    </tr>
<tr class="TableData"><td align="center">无附件</td></tr><tr height="25">
      <td class="TableData">
         新建附件：
         <input type="radio" id="NEW_TYPE1" name="DOC_TYPE" onclick="document.all('NEW_TYPE').value='doc'"/><label for="NEW_TYPE1">Word文档</label>
         <input type="radio" id="NEW_TYPE2" name="DOC_TYPE" onclick="document.all('NEW_TYPE').value='xls'"/><label for="NEW_TYPE2">Excel文档</label>
         <input type="radio" id="NEW_TYPE3" name="DOC_TYPE" onclick="document.all('NEW_TYPE').value='ppt'"/><label for="NEW_TYPE3">PPT文档</label>  
         <b>附件名：</b><input type="text" value="新建文档" class="SmallInput" size="20" name="NEW_NAME"/>
         <input type="button" onclick="new_attach();" value="新建附件" class="SmallButtonW"/>
         <input type="hidden" name="NEW_TYPE" value=""/>
      </td>
    </tr>

<tr>
   <td class="TableContent">
     <script></script><span id="ATTACHMENT_div"/><span style="display: none;" id="ATTACHMENT_upload_div"/><div id="SelFileDiv"/><a href="javascript:;" class="addfile">添加附件<input type="file" onchange="AddFile();" hidefocus="true" size="1" id="ATTACHMENT_0" name="ATTACHMENT_0" class="addfile"/></a> | <a class="selfile" onclick="sel_attach('SelFileDiv','ATTACH_DIR','ATTACH_NAME','DISK_ID');" href="#">从文件柜和网络硬盘选择附件</a><input type="hidden" name="ATTACH_NAME" value=""/><input type="hidden" name="ATTACH_DIR" value=""/><input type="hidden" name="DISK_ID" value=""/>
     <input type="hidden" value="1" name="ATTACH_PRIV"/>
     <input type="hidden" value="" name="ATTACHMENT_ID_OLD"/>
     <input type="hidden" value="" name="ATTACHMENT_NAME_OLD"/>
   </td>
 </tr>
</tbody></table>

</div>
<div id="feedBackDiv" align=center>
<table width="90%" cellspacing="0" cellpadding="3" bordercolor="#b8d1e2" border="1" align="center" class="TableList" style="border-collapse: collapse;">
    <tbody><tr class="TableHeader">
      <td><div style="float: left;"><img align="absmiddle" src="<%=imgPath %>/green_arrow.gif"/> 会签意见区</div></td>
    </tr>
    <tr class="TableContent">
      <td id="SIGN_INFO_POS">
        <b>我的意见：</b> <input type="checkbox" onclick="set_sign_cookie()" id="flow_sign_flag"/><label for="flow_sign_flag">启用会签手写签章功能</label>
        <div>        	
<input type="hidden" value="" id="CONTENT" name="CONTENT"/>       </div>
      </td>
    </tr>

        <tr class="TableData">
        	<td nowrap="">
           <script></script><span id="ATTACHMENT1_div"/><span style="display: none;" id="ATTACHMENT1_upload_div"/><div id="SelFileDiv1"/><a href="javascript:;" class="addfile">添加附件<input type="file" onchange="AddFile();" hidefocus="true" size="1" id="ATTACHMENT1_0" name="ATTACHMENT1_0" class="addfile"/></a> | <a class="selfile" onclick="sel_attach('SelFileDiv1','ATTACH_DIR1','ATTACH_NAME1','DISK_ID1');" href="#">从文件柜和网络硬盘选择附件</a><input type="hidden" name="ATTACH_NAME1" value=""/><input type="hidden" name="ATTACH_DIR1" value=""/><input type="hidden" name="DISK_ID1" value=""/>
          </td>
        </tr>
   <tr class="TableControl">
   	<td>
        <div align="center">
        <input type="button" onclick="CheckForm('3');" value="保存" class="SmallButton"/> 
        <input type="button" onclick="SelectSign()" value="快捷输入" class="SmallButtonW"/> 
        </div>
      </td>
    </tr>
</tbody></table>

</div>