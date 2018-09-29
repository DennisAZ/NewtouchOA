var touchGraphUrl = "";
function showTouchGrap(dataUrl 
    , clickHandler
    , cClickHandler
    , dbClickHandler 
    , cDbClickHandler ) {
  this.touchGraphUrl = dataUrl;
  var url = contextPath +  "/core/module/touchgraph/index.jsp?1=1";
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
  window.open(url);
}