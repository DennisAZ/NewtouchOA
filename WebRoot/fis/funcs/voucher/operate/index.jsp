<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>凭证操作</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/zTree/jquery.ztree.all-3.1.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<link rel="stylesheet" type="text/css" href="style/voucher.css"/>
<script type="text/javascript" src="js/operate.js"></script>
</head>
<body>
  <div class="page-header">
    <h2>凭证新增</h2>
  </div>
  <div id="tools">
  </div>
  <div class="voucher-center">
	  <div>
	    <table>
	      <tr>
	        <td>
	          日期:
	        </td>
	        <td>
	          <input id="date" name="date" type="text" class="small"/>
	        </td>
	      </tr>
	      <tr>
	        <td>
	        凭证字号:
	        </td>
	        <td>
	          <select class="small">
	            <option>记</option>
	          </select>
	          -
	          <input type="text" class="small">
	          附单据张数:
	          <input type="text" class="small">
	        </td>
	      </tr>
	    </table>
	  </div>
	  <div class="voucher-operate">
	    <div class="voucher-header">
	      <span class="voucher-title">凭证分录:新增</span>
	      <div class="opt" id="headerOpt"></div>
	    </div>
	    <div class="opt-range">
	      <div>
		      <label for="name"><em>*</em>摘要</label>
		      <input name="summary" id="summary" type="text" class="big textInput required"/>
		      <a class="toolbutton" id="dailySummary" href="javascript: void(0)"><span>维护日常摘要</span></a>
        </div>
        <div>
		      <label for="name"><em>*</em><a id="subjectLink" href="javascript: void(0)">科目</a></label>
		      <input name="subject" id="subject" type="text" class="small textInput required resettable"/><a id="subjectSel" href="javascript:void(0)"><span>选择科目</span></a>
		      <span id="subjectText" class="resettable"></span>
        </div>
        <div>
		      <label for="currency"><em>*</em>币别</label>
		      <select name="currency" id="currency" class="small resettable"></select>
		      <label for="rate"><em>*</em>汇率</label>
		      <input name="rate" id="rate" type="text" disabled class="small readonly resettable"/>
        </div>
        <div class="" id="itemsOpt"></div>
        <div>
          <label for="name"><em>*</em>原币</label>
          <input name="amount" id="amount" type="text" class="small textInput required resettable"/>
        </div>
        <div>
          <label for="name"><em>*</em>借方金额</label>
          <input name="debit" id="debit" type="text" class="small textInput required resettable"/>
          <label for="name"><em>*</em>贷方金额</label>
          <input name="credit" id="credit" type="text" class="small textInput required resettable"/>
        </div>
        <div>
          <input type="hidden" name="itemId" id="itemId" class="resettable"/>
          <a id="saveLine" href="javascript:void(0)" onclick="nextRow()"><span>下一行</span></a>
        </div>
	    </div>
	    <table class="voucher-view" id="grid">
	      <thead>
	         <td class="opt">
	         操作
	         </td>
	         <td class="subject">
	         科目
	         </td>
	         <td class="summary">
	         摘要
	         </td>
	         <td class="">
	         原币金额
	         </td>
	         <td class="rate">
	         币别/汇率
	         </td>
	         <td class="">
	         借方金额
	         </td>
	         <td class="">
	         贷方金额
	         </td>
	        </thead>
	        <tbody>
			      <tr class="active">
			        <td class="opt">
			        </td>
			        <td class="subject"></td><td class="summary"></td><td class="amount"></td><td class="rate"></td><td class="debit"></td><td class="credit"></td>
	          </tr>
			      <tr>
			        <td class="opt"></td><td class="subject"></td><td class="summary"></td><td class="amount"></td><td class="rate"></td><td class="debit"></td><td class="credit"></td>
	          </tr>
			      <tr>
			        <td class="opt"></td><td class="subject"></td><td class="summary"></td><td class="amount"></td><td class="rate"></td><td class="debit"></td><td class="credit"></td>
	          </tr>
			      <tr>
			        <td class="opt"></td><td class="subject"></td><td class="summary"></td><td class="amount"></td><td class="rate"></td><td class="debit"></td><td class="credit"></td>
	          </tr>
			      <tr>
			        <td class="opt"></td><td class="subject"></td><td class="summary"></td><td class="amount"></td><td class="rate"></td><td class="debit"></td><td class="credit"></td>
	          </tr>
	        </tbody>
		      <tfoot>
		      </tfoot>
	      </table>
	  </div>
  </div>
</body>
</html>