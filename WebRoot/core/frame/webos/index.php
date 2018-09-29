<?
$index = 0;
include_once("../header.php");
?>
<script type="text/javascript">
$(document).ready(function() {

  var events = new T9.Panel({
    width: 'auto',
    id: 'events',
    url: 'events.txt',
    loadCallback: function() {
    },
    width: 500,
    height: "auto",
    style: {
    },
    icon: '../images/events_icon.jpg',
    tbar: []
  });
  
  var toBuy = new T9.Container({
    id: 'toBuy',
    style: {
      "padding-left": '40px',
      overflow: 'visible',
      width: '463px'
    },
    items: [T9.packCmp({
      cls: 'contact-icon'
    }), {
      items: [{
        cls: 'row',
        layout: 'columnlayout',
        layoutCfg: {
          sortable: false,
          style: {
            overflow: 'hidden'
          }
        },
        items: [T9.packCmp({
          html: '<span><img src="../images/contact_point.gif"></img>&nbsp;&nbsp;渠道加盟</span>',
          cls: 'title'
        }), {
          xtype: 'panel',
          height: 80,
          url: 'contact1.txt',
          autoWidth: true
        }]
      }]
    }, {
      items: [{
        layout: 'columnlayout',
        cls: 'row',
        layoutCfg: {
          sortable: false,
          style: {
            overflow: 'hidden'
          }
        },
        items: [T9.packCmp({
          html: '<span><img src="../images/contact_point.gif"></img>&nbsp;&nbsp;售前咨询</span>',
          cls: 'title'
        }), {
          autoWidth: true,
          xtype: 'panel',
          url: 'contact2.txt',
          height: 80
        }]
      }]
    }, {
      items: [{
        layout: 'columnlayout',
        cls: 'row',
        layoutCfg: {
          sortable: false,
          style: {
            overflow: 'hidden'
          }
        },
        items: [T9.packCmp({
          html: '<span><img src="../images/contact_point.gif"></img>&nbsp;&nbsp;技术支持</span>',
          cls: 'title'
        }), {
          xtype: 'panel',
          height: 80,
          url: 'contact3.txt',
          autoWidth: true
        }]
      }]
    }]
  });

  new T9.Container({
    layout: 'columnlayout',
    style: {
      height: "auto",
      overflow: "visible",
      "padding-top": '25px'
    },
    layoutCfg: {
      sortable: false,
      style: {
      overflow: "visible"
      },
      pixel: true
    },
    items: [
            events, toBuy
    ],
    renderTo: '#container'
  });
});

</script>
<div id="container" class="home">
  <div id="flash">
    <script type="text/javascript" src="../flash/swfobject.js"></script>
    <script type="text/javascript">
  var s1 = new SWFObject("../flash/imagerotator.swf","rotator","1003","268","7");
  s1.addParam("allowfullscreen","true");
  s1.addVariable("file","../flash/madrid.xml");
  s1.addVariable("width","1003");
  s1.addVariable("height","268");
  s1.write("flash");
    </script>
  </div>
  <div class="evaluation-btn"><a target="_blank" href="http://t9.go2oa.com:login.jsp">在线试用&gt;&gt;</a></div>
</div>
<?
include_once("../footer.php");
?>