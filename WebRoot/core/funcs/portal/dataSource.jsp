<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/mytable.css"/>
<link  rel="stylesheet"  href="<%=cssPath%>/style.css">
<link type="text/css" href="<%=cssPath%>/cmp/tab.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<style>
  body {
    overflow:auto;
  }
  ul {
    margin:0;
    padding:0;
  }
  li {
    line-height: 20px;
  }
  
  .jq-Container-tabheader a {
    font-size: 12px;
  }
  .column {
    height: 100%;
    padding-bottom: 150px;
  }
  
  .yh-imgbox {
    position:relative;
    height: 100px;
    overflow: hidden;
  }
  .yh-imgbox-container {
    position:absolute;
    height: 100px;
    overflow:hidden;
    width:100%;
  }
  
  .yh-imgbox-imgs {
    position:absolute;
  }
  
  .yh-imgbox-texts {
    position:absolute;
    top:0px;
    left:180px;
  }
  
  .yh-imgbox-imgs img {
    height: 100px;
    width: 150px;
    display:block;
  }
  
  .yh-imgbox-texts span {
    cursor:pointer;
    display:block;
    line-height: 20px;
  }
</style>
<script type="text/javascript">
var win;
function newRule() {
  if (!win) {
    $('#createRule').show();
    win = new YH.Window({
      title: '新建规则',
      tbar: [{
        id: 'close',
        preventDefault: true,
        handler: function(e, t, w) {
          w.hide();
        }
      }],
      width: 600,
      items: [
        YH.packCmp({
          el: $('#createRule')
        })
      ]
    });
  }
  win.show();
}
</script>
</head>
<body>
  <br>
  <br><br>
  <div id="createRule" style="display:none;">
    <input type="radio" name="radio" id="radio0"><label for="radio0">列表规则</label>
    <input type="radio" name="radio" id="radio1"><label for="radio1">图标列表规则</label>
    <table style="width:100%;margin: 0 auto;" class="TableBlock">
      <tbody>
       <tr class="TableData">
        <td>
        图片路径
        </td>
        <td>
        <input type="text"></input>
        </td>
       </tr>
       <tr class="TableData">
        <td>
        显示文本
        </td>
        <td>
        <input type="text"></input>
        </td>
       </tr>
       <tr class="TableData">
        <td>
       链接地址
        </td>
        <td>
        <input type="text"></input>
        </td>
       </tr>
              <tr class="TableData">
        <td>
       自定义点击事件
        </td>
        <td>
        <textarea style="width: 90%; height: 100%;"></textarea>
        </td>
       </tr>
    </tbody>
  </table>
  </div>
  <div>
  <table style="width:600px;margin: 0 auto;" class="TableBlock">
    <tbody>
      <tr>
        <td class="TableHeader" colspan="2">
          自定义SQL配置数据:
        </td>
      </tr>
      <tr class="TableData">
        <td>
           SQL语句:
          </td>
        <td colspan="1">
        <textarea style="width: 90%; height: 100px;"></textarea>
        </td>
      </tr>
      <tr class="TableData">
        <td>
          数据组合规则:
        </td>
         <td>
          <input type="button" class="BigButton" value="新建规则" onclick="newRule()" />
        </td>
      </tr>
        <tr>
         <td nowrap  class="TableControl" colspan="2" align="center">
           <input type="hidden" name="portId" id="portId" value="">
           <input type="button" value="确定" onclick="location.href='cfgPortal.jsp'" class="BigButton">&nbsp;&nbsp;
           <input type="button" value="返回" class="BigButton" onclick="">
         </td>
       </tr>
    </tbody>
  </table>
  <br/>
  <br/>
  <table style="width:600px;margin: 0 auto;" class="TableBlock">
    <tbody>
      <tr>
        <td class="TableHeader" colspan="2">
          自定义模块地址:
        </td>
      </tr>
      <tr class="TableData">
        <td>
           模块内容url:
          </td>
         <td>
          <input size="40" type="text" />
        </td>
      </tr>
        <tr>
         <td nowrap  class="TableControl" colspan="2" align="center">
           <input type="hidden" name="portId" id="portId" value="">
           <input type="button" value="确定" onclick="location.href='cfgPortal.jsp'" class="BigButton">&nbsp;&nbsp;
           <input type="button" value="返回" class="BigButton" onclick="">
         </td>
       </tr>
    </tbody>
  </table>
 </div>
</body>
</html>