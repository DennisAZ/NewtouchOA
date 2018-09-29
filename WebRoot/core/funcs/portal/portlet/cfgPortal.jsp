<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>   

<%
  String pageNo = request.getParameter("pageNo");
  String pId = request.getParameter("id");
  boolean isEdit = false;
  if (!YHUtility.isNullorEmpty(pId)) {
    isEdit = true;
  }
  if ( YHUtility.isNullorEmpty(pageNo)) {
    pageNo = "0";
  }
  boolean isPublish = false;
  String publicPath = request.getParameter("publicPath");
  if (publicPath == null) {
    publicPath = "";
  }
  if (!YHUtility.isNullorEmpty(publicPath)) {
    pageNo = "1";
    isPublish = true;
  }
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑模块</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="js/cfgPortal.js"></script>
<script type="text/javascript">
var pageNo = <%=pageNo %>;
var isEdit = <%=isEdit %>;
var pId =  "<%=pId %>";
var publishData = {
    picName: '${param.picName}',
    publicPath: '${param.publicPath}',
    type: '${param.type}'
};
</script>
</head>
<body onload="doInit(<%=isPublish %>)">
<table  border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 编辑模块</span><br>
    </td>
  </tr>
</table>
<iframe name="preview" frameborder="0" style="display:none;" id="preview" width="100%" height="200px" src="preview.jsp"></iframe>
<hr />
<input value="<%=pId %>" type="hidden" name="pId" id="pId">
<input value="" type="hidden" name="oldName" id="oldName">
<div id="div-0" style="display:none">
 <div id="defineSqlDiv">
  <table style="width:600px;margin: 0 auto;" class="TableBlock">
    <tbody>
      <tr>
        <td class="TableHeader" colspan="2">自定义SQL配置数据:</td>
      </tr>
      <tr class="TableData">
        <td>SQL语句:<span style="color:red">*</span></td>
        <td colspan="1">
        <textarea style="width: 90%; height: 100px;" name="sql" id="sql"></textarea>
        </td>
      </tr>
      <tr class="TableData">
        <td>
          数据组合规则:<span style="color:red">*</span>
        </td>
         <td>
         <a href="javascript:void(0)" onclick="addRule()"><img src='img/add.gif'  title='新建规则'/>新建规则</a>
         
          <div id="ruleList"  style="display:none">
          <table class="TableBlock" style="margin: 0px auto;width:100%">
          <tr><td>类型</td><td>显示文本</td><td>图片</td><td>地址</td><td>操作</td></tr>
          <tbody id="ruleListTbody"></tbody>
          </table>
          </div>
          <div id="ruleDiv" style="border-top:1px solid #ccc;padding-top:5px;display:none">
          <form action="" id="ruleForm">
             <input type="radio" name="ruleType" id="gridRuleType" onclick="gridRuleTypeClick(this)" value="grid" checked><label for="radio0">列表规则</label>
    <input type="radio" name="ruleType" id="imgRuleType" onclick="imgRuleTypeClick(this)" value="img"><label for="radio1">图标列表规则</label>
          <input  class="SmallButtonW" type="button" value="保存规则" onclick="saveRuleData()">
          <input type="button" value="关闭" class="SmallButtonW" onclick="clearRuleData()">
    <table style="width:100%;margin: 0 auto;" class="TableBlock">
      <tbody>
       <tr id="imageAddTr" class="TableData" style="display:none">
        <td align="right">
        图片路径:<span style="color:red">*</span>
        </td>
        <td>
        <input type="text" name="imageAddress" id="imageAddress"/>
        </td>
       </tr>
       <tr class="TableData">
        <td  align="right">
        显示文本:<span style="color:red">*</span>
        </td>
        <td>
        <input type="text" name="showText" id="showText"/>
        </td>
       </tr>
       <tr class="TableData">
        <td  align="right">
       链接地址:
        </td>
        <td>
        <input type="text" name="linkAddress" id="linkAddress"/>
        </td>
       </tr>
              <tr class="TableData">
        <td  align="right">
       自定义点击事件:
        </td>
        <td>
        <textarea style="width: 90%; height: 100%;" name="linkEvent" id="linkEvent"></textarea>
        </td>
       </tr>
      </tbody>
      </table>
      </form>
      </div>
      </td></tr>
        <tr>
         <td  class="TableControl" colspan="2" align="center">
           <input type="button" value="自定义模块地址" class="BigButtonC" onclick="$('defineSqlDiv').hide();$('defineUrlDiv').show();"/>
           <% if (!isEdit) { %>
           <input type="button" value="下一步" onclick="setDefDate()" class="BigButton">&nbsp;&nbsp;
          <% } else {%>
              <input type="button" value="修改" onclick="updateModData()" class="BigButton">&nbsp;&nbsp;
          <input type="button" value="返回" onclick="location.href = 'portletlist.jsp'" class="BigButton">&nbsp;&nbsp;
          <% } %>
         </td>
       </tr>
    </tbody>
  </table>
  </div>
  <div id="defineUrlDiv" style="display:none">
  <table style="width:600px;margin: 0 auto;" class="TableBlock">
    <tbody>
      <tr>
        <td class="TableHeader" colspan="2"> 自定义模块地址:</td>
      </tr>
      <tr class="TableData">
      <td>模块内容url:<span style="color:red">*</span></td>
         <td><input size="40" type="text" name="url" id="url"/></td>
      </tr>
        <tr>
         <td class="TableControl" colspan="2" align="center">
           <input type="button" value="自定义sql数据"  class="BigButtonC" onclick="$('defineUrlDiv').hide();$('defineSqlDiv').show();"/>
           <% if (!isEdit) { %>
           <input type="button" value="下一步" onclick="setDefDate()" class="BigButton">&nbsp;&nbsp;
          <% } else {%>
              <input type="button" value="修改" onclick="updateModData()" class="BigButton">&nbsp;&nbsp;
          <input type="button" value="返回" onclick="location.href = 'portletlist.jsp'" class="BigButton">&nbsp;&nbsp;
          <% } %>
         </td>
       </tr>
    </tbody>
  </table>
  </div>
</div>
<div id="div-1"  style="display:none">
<table style="width:600px;margin: 0 auto;" class="TableBlock">
        <tbody>
          <tr>
            <td class="TableHeader" colspan="2">
              配置内容样式:
            </td>
          </tr>
          <tr class="TableData">
            <td>
            模块名称设置:
            </td>
            <td>
            <input type="text" id="name" id="name" onblur="titleBlur(this.value)"/>
           <span id="nameTip" style="color:red;font-size:14px"></span>    
            </td>
          </tr>
          <tr class="TableData">
            <td>
              内容模板选择:
            </td>
             <td>
              <input type="radio" name="styleType" checked id="styleType0"  value="grid" onclick="cfgContentType(this)"/><label for="radio0">列表类型</label>
              <input style="display:none" type="radio" name="styleType" id="styleType1" value="img"  onclick="cfgContentType(this)"/><label for="radio1">图片类型</label>
              <input type="radio" name="styleType" id="styleType2" value="imgbox" onclick="cfgContentType(this)"/><label for="radio2">图片新闻</label>
              
            </td>
          </tr>
          <tr class="TableData">
            <td>
              操作
            </td>
            <td>
             <% if (!isEdit) { %>
             <% if (!isPublish) { %>
            <input type="button" value="上一步" onclick="nextPage(0)" class="BigButton">&nbsp;&nbsp;
              <% } %>
              <input type="button" class="BigButton" value="保存模块" onclick="save()">
              <input type="button" value="下一步" onclick="checkNameNotEmpty()" class="BigButton">&nbsp;&nbsp;    
              <% } else { %>
               <input type="button" value="返回" onclick="location.href = 'portletlist.jsp'" class="BigButton">&nbsp;&nbsp;
              <input type="button" value="修改" onclick="updateModData()" class="BigButton">&nbsp;&nbsp;
              <%  } %>
            </td>
          </tr>
        </tbody>
      </table>

</div>
<div id="div-2"  style="display:none">
<table style="width:600px;margin: 0 auto;" class="TableBlock">
        <tbody>
          <tr>
            <td class="TableHeader" colspan="2">
              配置边框样式:
            </td>
          </tr>
          <tr class="TableData">
            <td>样式一</td>
            <td class="TableData">
              <div style="text-align: center;padding: 2px;">
                <img src="img/panel_1.jpg"/>
                <br>
                <input name="sel" id="sel1" value="1" onclick="cfgBorderStyle(this);" checked type="radio"/>
              </div>
            </tr>
            <tr class="TableData">
              <td>样式二</td>
              <td>
              <div style="text-align: center;padding: 2px;">
                <img src="img/panel_2.jpg"/><br>
                <input name="sel" id="sel2" value="2"  onclick="cfgBorderStyle(this);"  type="radio"/>
              </div>
              </td>
            </tr>
            <tr class="TableData">
              <td>样式三</td>
              <td>
                <div style="text-align: center;padding: 2px;">
                  <img src="img/panel_3.jpg"/><br>
                  <input name="sel" type="radio" value="3"  onclick="cfgBorderStyle(this);"  id="sel3"/>
                </div>
              </td>
            </tr>
            <tr class="TableData">
              <td>其他选项</td>
              <td>
                <input type="checkbox" onclick="cfgToolButAutoHide(this)" name="toolButAutoHide"  id="toolButAutoHide"/><label for="check0">工具按钮是否自动隐藏</label>
                <input type="checkbox" name="borderAutoHide"  onclick="cfgBorderAutoHide(this)"  id="borderAutoHide"/><label for="check1">边框是否自动隐藏</label>
              </td>
            </tr>
            <tr>
              <td   class="TableControl" colspan="2" align="center">
              <% if (!isEdit) { %>
              <input type="button" value="上一步" onclick="nextPage(1)" class="BigButton">&nbsp;&nbsp;
                <input type="button" class="BigButton" value="保存模块" onclick="save()">
              <input type="button" value="下一步" onclick="nextPage(3)" class="BigButton">&nbsp;&nbsp;    
              <% } else { %>
               <input type="button" value="返回" onclick="location.href = 'portletlist.jsp'" class="BigButton">&nbsp;&nbsp;
              <input type="button" value="修改" onclick="updateModData()" class="BigButton">&nbsp;&nbsp;
              <%  } %>
              </td>
          </tr>
        </tbody>
      </table>

</div>
<div id="div-3"  style="display:none">
<table class="TableBlock" width="600" height="100%" align="center">
        <tr>
          <td class="TableHeader" colspan="2">
            配置权限:
          </td>
        </tr>
        <tr>
          <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
          <td class="TableData">
            <input type="hidden" name="dept" id="dept" value="">
            <textarea cols=40 name="deptDesc" id="deptDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td  class="TableContent"" align="center">授权范围：<br>（角色）</td>
          <td class="TableData">
            <input type="hidden" name="role" id="role" value="">
            <textarea cols="40" name="roleDesc" id="roleDesc" rows="8" class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td  class="TableContent"" align="center">授权范围：<br>（人员）</td>
          <td class="TableData">
            <input type="hidden" name="user" id="user" value="">
            <textarea cols="40" name="userDesc" id="userDesc" rows="8" class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td   class="TableControl" colspan="2" align="center">
          <% if (!isEdit) { %>
          <input type="button" value="上一步" onclick="nextPage(2)" class="BigButton">&nbsp;&nbsp;
            <input type="button" value="保存模块" onclick="save()" class="BigButton">&nbsp;&nbsp;
          <% } else { %>
               <input type="button" value="返回" onclick="location.href = 'portletlist.jsp'" class="BigButton">&nbsp;&nbsp;
              <input type="button" value="修改" onclick="updateModData()" class="BigButton">&nbsp;&nbsp;
              <%  } %>
          </td>
        </tr>
      </table>
</div>
<div id="noData" align="center" style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">此模块数据不能配置！</td>
        </tr>
    </tbody>
</table>
<div align="center"><input type="button" class="BigButton" value="返回" onclick="location.href='portletlist.jsp'"/></div>
</div>
</body>
</html>