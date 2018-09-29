{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "采购订单交货提醒",
        url: contextPath + "/springViews/erp/purchase/purchaseRemind/index.jsp"
      });
    }
  }],
  width: "500px",
  "items":[{
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/notify/act/YHNotifyShowAct/queryAllPurchaseRemind.act"
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
       
      var code = $('<span class="title"></span>').html(e.code);
      var title = $('<span class="desc"></span>').html(e.title);
      var person = $('<span class="time"></span>').html(e.person);
      var deliveryDate = $('<span class="time"></span>').html(e.deliveryDate);
      var status = '';
      
		if(e.status=='0'){
			status = $('<span class="time"></span>').html("新建状态");
		}else if(status==1){
			status = $('<span class="time"></span>').html("审核中");
		}else if(status==2){
			status = $('<span class="time"></span>').html("审核通过");
		}else if(status==3){
			status = $('<span class="time"></span>').html("审核没通过");
		}else if(status==4){
			status = $('<span class="time"></span>').html("执行中");
		}else{
			status = $('<span class="time"></span>').html("已完成");
		}
	      
      return a.append("采购编号：").append("【").append(code).append("】").append("&nbsp;").append("采购主题：").append(title).append("&nbsp;").append("采购日期：").append(deliveryDate).append("&nbsp;").append("采购状态：").append(status);
    }
  }],
  "xtype": "panel",
  height: "auto",
  cls: "window-height",
  "cmpCls": "jq-window",
  "title": "采购订单交货提醒"
}