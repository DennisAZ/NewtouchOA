{
    cls: 'window-1',
    cmpCls: 'jq-window',
    style: {
      background: 'transparent'
    },
    tbar: [{
      id: 'close'
    }, {
      id: 'pin'
    }, {
      id: 'unpin'
    }],
    //renderTo: windowCtn,
    autoHideBorders: true,
    autoHideTools: true,
    left: 600,
    top: 210,
    items: [
      YH.packCmp({
        style: {
          'margin': '0 auto',
          height: '150px',
          width: '150px'
        },
        html: '<div id="flsClock">'
          + '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="/yh/core/frame/inc/swflash.cab" width="150" height="150" style="cursor:pointer;">'
          + '<param name="movie" value="/yh/core/frame/webos/styles/style1/flash/clock.swf" />'
          + '<param name="quality" value="high" />'
          + '<param name="wmode" value="transparent">'
          + '<embed src="/yh/core/frame/webos/styles/style1/flash/clock.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="150" height="150" wmode="transparent"></embed>'
          + '</object>'
          + '</div>'
      })
    ],
    //parentEl: windowCtn,
    width: 210,
    height: 160
  }