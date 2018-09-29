<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="yh.core.global.YHSysProps" %>
    <%@ page import="java.util.List" %>
    <%@ page import="java.util.Map" %>
    <%@ page import="yh.core.util.YHUtility" %>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
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
String  useInfoResSubsys = YHSysProps.getProp("useInfoResSubsys");

List<Map> prcsList = (List<Map>)request.getAttribute("prcsList");
boolean isSetPriv = (Boolean)request.getAttribute("isSetPriv");
int firstPrcsSeqId = (Integer)request.getAttribute("firstPrcsSeqId");
request.getAttribute("flowName");
%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<title>流程设计</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<OBJECT id="VML" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E"></OBJECT>
<STYLE>
v\:oval {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shadow {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:textbox {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:roundrect {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shapetype {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:stroke {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:path {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:shape {FONT-SIZE: 12px;behavior: url(#default#VML);}
v\:line {FONT-SIZE: 12px;behavior: url(#default#VML);}
</STYLE>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script language="JavaScript" src="<%=contextPath %>/core/funcs/doc/flowdesign/flowdesigner2.js"></script>
<script language="JavaScript" src="<%=contextPath %>/core/funcs/doc/flowdesign/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script language="JavaScript">
var flowId = '<%=flowId%>';
var contextPath = '<%=contextPath%>';
var useInfoResSubsys = '<%=useInfoResSubsys%>';
var isSetPriv = <%=isSetPriv %>;
var firstPrcsSeqId =  <%=firstPrcsSeqId %>;
</script>
</HEAD>
<BODY  style="height:600px" onload="createVml()"  onmousedown="DoRightClick();" oncontextmenu="nocontextmenu();">
<%
for(Map map : prcsList){
  String flowType = (String)map.get("flowType");
  String prcsTo = YHUtility.null2Empty((String)map.get("prcsTo"));
  int prcsId = (Integer)map.get("prcsId");
  int tableId = (Integer)map.get("tableId");
  String flowTitle = (String)map.get("flowTitle");
  String fillcolor = (String)map.get("fillcolor");
  int leftVml = (Integer)map.get("leftVml");
  int topVml = (Integer)map.get("topVml");
  String prcsName = (String)map.get("prcsName");
  
  if ("start".equals(flowType) ||"end".equals(flowType)  ) {
    String prcsTo2 = prcsTo.replace(",0",",结束");
    if("0".equals(prcsTo2) 
        || YHUtility.isNullorEmpty(prcsTo2) 
        || "0,".equals(prcsTo2)){
      prcsTo2 = "结束";
    }
    String prcsToTitle = "·下一步骤:" + prcsTo2;
    String sOval = "<v:oval id="
      + prcsId +" table_id="
      + tableId + " flowType='"
      + flowType + "'  passCount='0'  onDblClick='Edit_Process("+ tableId +")'  flowTitle='"
      + flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
      + fillcolor + "' onDblClick='' style='LEFT:" 
      + leftVml + "; TOP:" 
      + topVml + ";WIDTH: 120; POSITION: absolute; HEIGHT: 60;vertical-align:middle;CURSOR:hand;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
      + flowTitle + "\n" + prcsToTitle + "'>"; 
    sOval += "<v:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
    sOval += "<v:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'><b>" + prcsId + "</b><br>" + prcsName + "</v:textbox>";
    sOval += "</v:oval>";
    out.println(sOval);
  } else if ("child".equals(flowType)) {
    String prcsTo2 = prcsTo.replace(",0",",结束");
    if("0".equals(prcsTo2)){
      prcsTo2 = "结束";
    }
    String prcsToTitle = "·下一步骤:" + prcsTo2;
    String sRoundrect = "<v:roundrect inset='2pt,2pt,2pt,2pt' id='"
      + prcsId +  "' table_id='" 
      + tableId + "' flowType='"
      + flowType + "' passCount='0' onDblClick='Edit_Process("+ tableId +")' flowTitle='"
      + flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
      + fillcolor + "' onDblClick='' style='LEFT:"
      + leftVml + "; TOP:"
      + topVml +"; WIDTH: 100; POSITION: absolute; HEIGHT: 50;vertical-align:middle;CURSOR:hand;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
      + flowTitle + "\n" + prcsToTitle + "'>";
      sRoundrect += "<v:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
      sRoundrect += "<v:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'><b>" + prcsId + "</b><br>" + prcsName + "</v:textbox>";
      sRoundrect += "</v:roundrect>";
      out.println(sRoundrect);
  } else if (flowType == null || "".equals(flowType)) {
    String condition = (String)map.get("condition");
    if (!YHUtility.isNullorEmpty(condition)) {
      String prcsTo2 = prcsTo.replace(",0",",结束");
      if("0".equals(prcsTo2)){
        prcsTo2 = "结束";
      }
      String prcsToTitle = "·下一步骤:" + prcsTo2;
      String sShapetype = "<v:shapetype   onDblClick='Edit_Process("+ tableId +")'  id='type" + prcsId + "' coordsize='21600,21600' o:spt='4' path='m10800,l,10800,10800,21600,21600,10800xe'>";
      String sStroke = "<v:stroke joinstyle='miter'/>";
      String sPath = "<v:path gradientshapeok='t' connecttype='rect' textboxrect='5400,5400,16200,16200'/>";
        
      
      String sShape = "<v:shape type='#type" + prcsId + "' id='" + prcsId + "' table_id='" 
        + tableId + "' onDblClick='Edit_Process("+ tableId +")'  flowType='"
        + flowType + "' passCount='0'  flowTitle='"
        + flowTitle + "' flowFlag='0'  readOnly='0' fillcolor='"
        + fillcolor + "' onDblClick='' style='LEFT: "+ leftVml +"; TOP:"
        + topVml +"; WIDTH: 140; POSITION: absolute; HEIGHT: 80;z-index:1;vertical-align:middle;CURSOR:hand;TEXT-ALIGN:center;' title=\"▲"+ flowTitle + "\n" + prcsToTitle  + condition.replace("<br>","\n").replace("<BR>" ,"\n") + "\">";
     
      String sShadow = "<v:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
      String sTextbox = "<v:textbox inset='0pt,0pt,0pt,0pt' onselectstart='return false;'><b>" + prcsId + "</b><br>" + prcsName+"</v:textbox>";
       
      sShapetype += sStroke;
      sShapetype += sPath;
      sShapetype += "</v:shapetype>";
      out.println(sShapetype);
      
      sShape += sShadow;
      sShape += sTextbox;
      sShape += "</v:shape>";
      out.println(sShape);
    } else {
      String prcsTo2 = prcsTo.replace(",0",",结束");
      if("0".equals(prcsTo2)){
        prcsTo2 = "结束";
      }
      String prcsToTitle = "·下一步骤:" + prcsTo2;
      String sRoundrect = "<v:roundrect inset='2pt,2pt,2pt,2pt' id='"
        + prcsId +  "' table_id='" 
        + tableId + "' flowType='"
        + flowType + "' passCount='0' onDblClick='Edit_Process("+ tableId +")' flowTitle='"
        + flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
        + fillcolor + "' onDblClick='' style='LEFT:"
        + leftVml + "; TOP:"
        + topVml +"; WIDTH: 100; POSITION: absolute; HEIGHT: 50;vertical-align:middle;CURSOR:hand;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
        + flowTitle + "\n" + prcsToTitle + "'>";
        sRoundrect += "<v:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
        sRoundrect += "<v:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'><b>" + prcsId + "</b><br>" + prcsName + "</v:textbox>";
        sRoundrect += "</v:roundrect>";
        out.println(sRoundrect);
    }
  }
  String[] aPrcsTo = prcsTo.split(",");
  for (String pto : aPrcsTo) {
    if (!YHUtility.isNullorEmpty(pto)) {
      String sLine = "<v:line  mfrID='"+pto+"' title='' source='"+prcsId+ "' object='"+ pto +"' from=\"0,0\" to=\"0,0\" style='position:absolute;display:none;z-index:2' arcsize='4321f' coordsize='21600,21600'>";
      String sStroke = "<v:stroke endarrow='block'></stroke>";
      //String sShadow = "<v:shadow on='T' type='single' color='#b3b3b3' offset='1,1'/>";
      sLine += sStroke;
     // sLine += sShadow;
      sLine += "</v:line>";
      out.println(sLine);
    }
  }
}
%>
</BODY>
</HTML>