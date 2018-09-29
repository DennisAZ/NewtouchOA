{
  items: [
    YH.packCmp({
      cls: "port-search",
      html: '<div><input type="text"/></div>'
    }), {
    xtype: 'button',
    normalCls: 'port-search-btn',
    handler: function(e, t) {
      top.dispParts && top.dispParts(contextPath + '/core/funcs/search/result.jsp?key=' + encodeURI($('.port-search input').val()));
    }
  }],
  tbar: [{
    id: 'close'
  }, {
    id: 'pin'
  }, {
    id: 'unpin'
  }],
  height: "41px",
  width: "240px",
  cls: 'window-1',
  autoHideBorders: true,
  autoHideTools: true,
  "xtype": "panel",
  "cmpCls": "jq-window"
}