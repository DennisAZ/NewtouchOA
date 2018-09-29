<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>表单验证</title>
<script type="text/javascript" src="<%=jsPath%>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/uni-form-validation.jquery.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
  <link href="<%=cssPath%>/ui/uniform/css/uni-form.css" media="screen" rel="stylesheet"/>
  <link href="<%=cssPath%>/ui/uniform/css/default.uni-form.css" title="Default Style" media="screen" rel="stylesheet"/>
  
  <!--[if lte ie 7]>
    <style type="text/css" media="screen">
      /* Move these to your IE6/7 specific stylesheet if possible */
      .uniForm, .uniForm fieldset, .uniForm .ctrlHolder, .uniForm .formHint, .uniForm .buttonHolder, .uniForm .ctrlHolder ul{ zoom:1; }
    </style>
  <![endif]-->
<style>
#demo {
  padding: 10px;
}
div.valid {
  background: url("img/ok.png") no-repeat scroll 100% 2.75em transparent;
}
</style>
</head>
<body>
  <div id="demo">
    <form action="#" class="uniForm">
            
      <fieldset class="inlineLabels">
        <div class="ctrlHolder">
          <label for="code"><em>*</em> 代码</label>
          <input name="code" id="code" size="35" maxlength="50" type="text" class="textInput required validateInteger"/>
          <p class="formHint">请输入数字</p>
        </div>
        
        <div class="ctrlHolder">
          <label for="name"><em>*</em> 名称</label>
          <input name="name" id="name" data-default-value="" size="35" maxlength="50" type="text" class="textInput required"/>
          <p class="formHint"></p>
        </div>
      
        <div class="ctrlHolder">
          <label for="type"><em>*</em> 类别</label>
          <select name="type" id="type">
            <option>现金</option>
            <option>银行存款</option>
          </select>
        </div>
        <div class="ctrlHolder">
          <label for="help"><em>*</em> 辅助核算</label>
          <select name="help" id="help">
            <option>无</option>
            <option>客户</option>
            <option>职员</option>
          </select>
          <p class="formHint"></p>
        </div>
        
        <div class="ctrlHolder">
          <label for=""><em>*</em> 外币核算</label>
          <select name="help" id="help">
            <option>不核算</option>
            <option>所有类别</option>
          </select>
          <p class="formHint"></p>
        </div>
        
        <div class="ctrlHolder">
          <p class="label"><em>*</em> 余额方向</p>
          <p>
	          <label for=""><input type="radio" name="color" class="required"> 借</label>
	          <label style="margin:0" for=""><input type="radio" name="color" class="required"> 贷</label>
          </p>
        </div>
      </fieldset>
    </form>
  </div>
  <script>
    $(function(){
      $('form.uniForm').uniform({
        prevent_submit : true
      });
    });
  </script>
  </body>
</html>