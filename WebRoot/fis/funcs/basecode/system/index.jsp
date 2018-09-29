<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>系统设置</title>
<script type="text/javascript" src="<%=jsPath %>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath %>/ui/jquery-ui-patch.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
</head>
<body>
  <div class="page-header">
    <h2>系统设置</h2>
  </div>
  <div>
    <table class="table-block">
      <tr class="table-head">
        <td width="60">类别</td>
        <td width="140">名称</td>
        <td width="300">取值</td>
        <td width="200">备注</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>数量</td>
        <td><input type="text" class="small" id="amount" name="amount" value="4"/></td>
        <td>小数位数有效取值范围为0-8位</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>单价</td>
        <td><input type="text" class="small" id="price" name="price" value="4"/></td>
        <td>小数位数有效取值范围为0-8位</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>汇率</td>
        <td><input type="text" class="small" id="rate" name="rate" value="4"/></td>
        <td>小数位数有效取值范围为0-8位</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>增值税纳税人身份</td>
        <td>
          <select class="small" id="" name="">
            <option value="0">一般纳税人</option>
          </select>
        </td>
        <td></td>
      </tr>
      <tr>
        <td>公共</td>
        <td>税率</td>
        <td><input type="text" class="small" id="" name="" value="17"/>%</td>
        <td></td>
      </tr>
      <tr>
        <td>公共</td>
        <td>制单人打印控制</td>
        <td>
          <label><input type="checkbox" checked/>单据上打印制单人姓名</label>
          <label><input type="checkbox" checked/>账表上打印制表人姓名</label>
        </td>
        <td></td>
      </tr>
      <tr>
        <td>公共</td>
        <td>财务报表签名栏打印选项</td>
        <td>
          <label><input type="checkbox" checked/>单位负责人</label>
          <label><input type="checkbox" checked/>复核</label>
          <label><input type="checkbox" checked/>制表</label>
        </td>
        <td>报表将显示勾选项的签名栏</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>反结账控制</td>
        <td>
          <label><input type="checkbox" checked/>允许反结账</label>
        </td>
        <td>取消勾选将隐藏反结账功能</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>反结束初始化控制</td>
        <td>
          <label><input type="checkbox" checked/>允许反结束初始化</label>
        </td>
        <td>取消勾选将隐藏反结束初始化功能</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>期末结账发送短信控制</td>
        <td>
          <label><input type="checkbox"/>允许发送短信</label>
        </td>
        <td>勾选后期末结账（反结账）成功，发送短信通知，取消勾选则不发短信。 </td>
      </tr>
      <tr>
        <td>公共</td>
        <td>期末处理凭证审核控制</td>
        <td>
          <label><input type="checkbox"/>凭证审核后才能期末处理</label>
        </td>
        <td>勾选后期末结账当前期间所有凭证必须审核通过</td>
      </tr>
      <tr>
        <td>公共</td>
        <td>科目级数</td>
        <td><input type="text" value="3" name="" id=""/></td>
        <td></td>
      </tr>
      <tr>
        <td>公共</td>
        <td>编码长度</td>
        <td><div id="code"></div></td>
        <td></td>
      </tr>
      <tr>
        <td>公共</td>
        <td>优惠折扣科目</td>
        <td><input type="text"/></td>
        <td></td>
      </tr>
    </table>
  </div>
</body>
</html>