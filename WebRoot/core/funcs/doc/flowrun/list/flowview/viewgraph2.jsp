<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.Map,java.util.List" %>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
String flowId = request.getParameter("flowId");
String runId = request.getParameter("runId");
Map map = (Map)request.getAttribute("map");
String runName = (String)map.get("runName");
String flowName = (String)map.get("flowName");
String timeToId =  (String)map.get("timeToId");
if (timeToId == null) {
  timeToId = "";
}
List<Map> prcsList =  (List<Map>)map.get("prcsList");
%>
<%
String script = "";
String content = "";
for (Map p : prcsList) {
  List<Map> list = (List<Map>)p.get("list");
  for (Map p2 : list) {
    Integer state = (Integer)p2.get("state");
    String fillcolor = "";
    if ( state == 1) {
      fillcolor ="#FFBC18";
    } else if ( state == 2) {
      fillcolor ="#FFBC18";
    } else if ( state == 3) {
      fillcolor = "#F4A8BD";
    } else if ( state == 4) {
      fillcolor = "#F4A8BD";
    } else if ( state == 5) {
      fillcolor = "#D7D7D7";
    }
    String title = "<b>" + p2.get("prcsName") + "</b><br>";
    List<Map> userList  = (List<Map>)p2.get("user");
    for (Map m : userList) {
      String prcsUserName = "";
      boolean isOp = (Boolean)m.get("isOp");
      String userName = (String)m.get("userName");
      if (isOp) {
        prcsUserName = "<span style='text-decoration:underline;font-weight:bold;color:red'>"+ userName +" 主办</span>";
      } else {
        prcsUserName = "<span style='text-decoration:underline;font-weight:bold;'>"+ userName +"</span>";
      }
      String state2 = (String)m.get("state");
      if ("1".equals(state2)) {
        title += "<img src='"+ imgPath +"/email_close.gif'  align='absmiddle'/>" + prcsUserName + "[<font color=green>未接收办理</font>]";
      } else if ("2".equals(state2)) {
        String timeUsed = (String)m.get("timeUsed");
        if (timeUsed == null) {
          timeUsed = "";
        }
         title += "<img src='"+ imgPath +"/email_open.gif'  align='absmiddle'/>" + prcsUserName + "[<font color=green>办理中,已用时：" + timeUsed + "</font>]";
        String timeOutFlag = (String)m.get("timeOutFlag");
        if(!"".equals(timeOutFlag)){
          String timeOut = (String)m.get("timeOut");
          if (timeOut  == null) {
            timeOut = "0";
          }
          title += "<br> <span style='color:red'>限时" + timeOut +"小时," + timeUsed + "</span>";
        }
        String beginTime = (String)m.get("beginTime");
        title += "<br> 开始于：" + beginTime;
      } else if("3".equals(state2)){
        String timeUsed = (String)m.get("timeUsed");
        if (timeUsed == null) {
          timeUsed = "";
        }
        String beginTime = (String)m.get("beginTime");
        String deliverTime = (String)m.get("deliverTime");
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>"+ prcsUserName +"[<font color=green>已转交下步,用时：" + timeUsed + "</font>]";
        title += "<br> 开始于：" + beginTime;
        if(!"".equals(deliverTime)){
          title += "<br> 结束于：" + deliverTime;
        }
        //已办结
      }else if("4".equals(state2)){
        String beginTime = (String)m.get("beginTime");
        String timeUsed = (String)m.get("timeUsed");
        if (timeUsed == null) {
          timeUsed = "";
        }
        String deliverTime = (String)m.get("deliverTime");
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>"+ prcsUserName +"[<font color=green>已办结,用时：" + timeUsed + "</font>]";
        title += "<br> 开始于：" + beginTime;
        if(!"".equals(deliverTime)){
          title += "<br> 结束于：" + deliverTime;
        }
      }else if("5".equals(state2)){
        title += prcsUserName + "[预设经办人]";
      }
      title += "<br><br>";
    }
    String childRun = (String)p2.get("childRun");
    String childFlowId = (String)p2.get("childFlowId");
    String dbclick = "";
    if (childRun != null 
        && !"0".equals(childRun) 
        && !"0".equals(childFlowId)) {
      dbclick = "flowView("+childRun+" , "+childFlowId+")";
    }
    String sourceId = (String)p2.get("sourceId");
    String flowType = (String)p2.get("flowType");
    String prcsTitle = (String)p2.get("prcsTitle");
    int leftVml = (Integer)p2.get("leftVml");
    int topVml = (Integer)p2.get("topVml");
    String sRoundrect = "<v:roundrect inset='2pt,2pt,2pt,2pt' id='"
      + sourceId +  "' flowType='"
      + flowType + "' passCount='0' flowTitle='"
      + prcsTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
      + fillcolor + "' onDblClick='"+dbclick+"' style='LEFT:"
      + leftVml + "; TOP:"
      + topVml +"; WIDTH: 100; POSITION: absolute; HEIGHT: 50;vertical-align:middle;CURSOR:hand;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title=''>";
    String sShadow = "<v:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
    String sTextbox = "<v:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'>"+prcsTitle+"</v:textbox>";
    sRoundrect += sShadow;
    sRoundrect += sTextbox;
    sRoundrect += "</v:roundrect>";
    script += "new Tooltip(document.getElementById('"+sourceId+"'),{width:300,backgroundColor:'#FAFCFD',";
    script += "borderColor:'#000', textColor:'#000', maxWidth:250,mouseFollow:true}, \""+ title +"\");";
    content += sRoundrect;
    List<String> prcsTo =(List<String>) p2.get("prcsTo");
    for (int b=0;b<prcsTo.size();b++) {
      String ss = prcsTo.get(b);
      if (!"".equals(ss)){
        String sLine = "<v:line mfrID='"+ ss + "' title='' source='"+ sourceId + "' object='"+ ss 
          + "' from=\"0,0\" to=\"0,0\" style='position:absolute;display:none;z-index:2' arcsize='4321f' coordsize='21600,21600'>";
        String sStroke = "<v:stroke endarrow='block'></v:stroke>";
        sLine += sStroke;
        sLine += "</v:line>";
        content += sLine;
      }
    }
  }
}

%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<title>流程图-<%=flowName %>-(<%=runId %>)-<%=runName %></title>
<OBJECT id="VML" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<style>
v\:oval {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shadow {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:textbox {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:roundrect {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shapetype {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:stroke {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:path {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shape {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:line {FONT-SIZE: 12px;behavior: url(#default#VML);}
#tooltip {z-index:65535;position:absolute;border:1px solid #333;background:#f7f5d1;padding:2px 5px;color:#333;display:none;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Tooltip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script language="JavaScript" src="<%=contextPath %>/core/funcs/workflow/flowrun/list/flowview/set_main2.js"></script>
<script language="JavaScript">
var flowId = '<%=flowId%>';
var runId = '<%=runId%>';
var contextPath = '<%=contextPath%>';
var imgPath = '<%=imgPath%>';
function init(){
<%=script %>
}
</script>
</HEAD>
<BODY onload="createVml()">
<div id=content>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/workflow.gif" align="absmiddle"><span id="runMsg" class="big3"> <%=runName %> 流水号：<%=runId %></span>&nbsp;
    </td>
  </tr>
</table>
<div>
颜色标识说明：<span style="color:#FFBC18;">■</span>未接收
&nbsp;&nbsp;<span style="color:#50C625;">■</span>办理中
&nbsp;&nbsp;<span style="color:#F4A8BD;">■</span>办理完毕
&nbsp;&nbsp;<span style="color:#D7D7D7;">■</span>预设步骤
<!-- &nbsp;&nbsp;<span style="color:#70A0DD;">■</span>子流程&nbsp;&nbsp;注：子流程可双击步骤查看流程图&nbsp;&nbsp;-->
</div>

<div id="canvas">
<%=content %>
</div>
</div>
<input type="hidden" value="<%=timeToId %>" name="timeToId" id="timeToId">

</BODY>
</HTML>