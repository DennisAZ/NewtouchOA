var touchGraphUrl = "";
function showTouchGrap(dataUrl 
    , clickHandler
    , cClickHandler
    , dbClickHandler 
    , cDbClickHandler ,notNew , win) {
  this.touchGraphUrl = dataUrl;
  var url = contextPath + "/core/module/touchgraph/index.jsp?1=1";
  if (clickHandler) {
    url += "&clickHandler=" + clickHandler;
  }
  if (dbClickHandler) {
    url += "&dbClickHandler=" + dbClickHandler;
  }
  if (cClickHandler) {
    url += "&cClickHandler=" + cClickHandler;
  }
  if (cDbClickHandler) {
    url += "&cDbClickHandler=" + cDbClickHandler;
  }
  if (notNew) {
    url += "&window=window.parent";
    win.location = url;
  } else {
    window.open(url);
  }
}