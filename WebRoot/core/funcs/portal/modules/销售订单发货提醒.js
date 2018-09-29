{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "销售订单发货提醒",
        url: contextPath + "/springViews/erp/order/saleRemind/index.jsp"
      });
    }
  }],
  width: "500px",
  "items":[{
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/notify/act/YHNotifyShowAct/queryAllsendFormRemind.act"
    },
    rowRender: function(i, e) {
      var icon = "";
      function deskPurchaseRemind(seqId){
        var img = document.getElementById("img_" + seqId + "");
        if(img){
          document.getElementById("img_" + seqId + "").style.display= "none";
        }
        var URL = contextPath + '/springViews/erp/purchase/purchaseRemind/detail.jsp?purchaseId='+seqId+"&isCloseFlag=0";
        myleft= (screen.availWidth-500) / 2;
        window.open(URL, "deskPurchaseRemind", "height=600,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left=" + myleft + ", resizable=yes");    
      }
      var a = $("<a href='javascript:void(0);'></a>");
       a.click(function() {
        deskPurchaseRemind(e.seqId);
        var imgObj = $('#imgId'+i);
        if(icon == null || icon == "")
        {
        	return ;
        }else if(imgObj != null && imgObj != 'undefined'){
        	imgObj.remove();
        	return;
        }
      });
       
      var order_code = $('<span class="title"></span>').html(e.order_code);
      var po_code = $('<span class="title"></span>').html(e.po_code);
      var po_title = $('<span class="desc"></span>').html(e.po_title);
      var pod_sender_id = $('<span class="time"></span>').html(e.pod_sender_id);
      var pod_date = $('<span class="time"></span>').html(e.pod_date);
      var po_status = e.po_status;
      
	var rtStr="";
	var url = contextPath + "/SpringR/warehouse/getStatusName?status="+po_status;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){ 
		        rtStr = $('<span class="time"></span>').html(jsonData);
	   }
	 });
      return a.append("订单编号：").append("【").append(order_code).append("】").append("&nbsp;").append("出货单编号：").append("【").append(po_code).append("】").append("&nbsp;").append("出货单主题：").append(po_title).append("&nbsp;").append("发货日期：").append(pod_date).append("&nbsp;").append("采购状态：").append(rtStr);
    }
  }],
  "xtype": "panel",
  height: "auto",
  cls: "window-height",
  "cmpCls": "jq-window",
  "title": "销售订单发货提醒"
}