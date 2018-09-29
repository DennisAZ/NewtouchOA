<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="pgTopPanel2">
<div class="pgPanel">
  <div>
             每页<select id="selectPageSize2"></select>条
  </div>
  <div class="separator"></div>
  <div id="btnPgFirst2" title="第一页" class="pgBtn pgFirst pgFirstDisabled"></div>
  <div id="btnPgPre2" title="前一页" class="pgBtn pgPrev pgPrevDisabled"></div>
  <div class="separator"></div>
  <div>
           第<input name="pageIndex" id="pageIndex2" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"  type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共
    <span id="pageCount2" class="pgTotalPage"></span> 页
  </div>
  <div class="separator"></div>
  <div title="下一页" id="btPgNext2" class="pgBtn pgNext pgNextDisabled"></div>
  <div title="最后一页" id="btnPgLast2" class="pgBtn pgLast pgLastDisabled"></div>
  <div class="separator"></div>
  <div id="btnRefresh2" title="刷新" class="pgBtn pgRefresh"></div>
  <div class="separator"></div>
  <div id="pgSearchInfo2" class="pgSearchInfo"></div>
</div>
<table id="dataTable2" border="0" class="TableList pgTable" style="display:none">
  <tr id="dataHeader2" class=TableTr>
  </tr>
</table>
</div>
<table id="pgMsrgPanel2" class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
              没有查找到数据！
    </td>
  </tr>
</table>