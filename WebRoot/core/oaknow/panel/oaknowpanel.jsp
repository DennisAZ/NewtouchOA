
<%@ include file="/core/inc/header.jsp" %>
<frameset rows="30,*" cols="*" frameborder="0" framespacing="0" id="frame1" name="frame1" class="frameboder">
    <frame name="banner" id="banner" scrolling="no" noresize src="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/topPanel.act" frameborder="0" framespacing="1" style="border-bottom: 1px solid #BBBBBB;">
    <frameset rows="*"  cols="180,*" frameborder="0" framespacing="0" id="frame2">
      <frame name="leftpanel" id="leftpanel" scrolling="auto" src="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/oAKonwLeftPanel.act" noresize frameborder="0" framespacing="1" style="border-right: 1px solid #BBBBBB;">
      <frame name="mainpanel" id="mainpanel" scrolling="auto" src="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/leftPanel.act" frameborder="0">
    </frameset>
</frameset>