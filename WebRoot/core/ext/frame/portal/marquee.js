
function Marquee(div, delay, step){

  var t = {
    moveEl: $(div),
    delay: delay,
    step: step,
    top: 0,
    flag: true,
    height: 0,
    init: function(){

      t.top = 0;
      
      //获取内容的高度
      t.height = t.moveEl.getHeight();
      
      //设置上层的div overflow:hidden
      t.moveEl.up('div',0).setStyle({
        'overflow':'hidden',
        'position':'relative',
        'backgroundColor':'white'
      });
      
      //设置移动元素overflow:visible
      t.moveEl.setStyle({
        'position':'relative',
        'overflow':'visible'
      });
      
      t.moveEl.up('div',0).observe('mouseover',function(){
        t.flag = false;
      });
      
      t.moveEl.up('div',0).observe('mouseleave',function(){
        t.flag = true;
        clearTimeout(t.animate);
        t.scroll();
      });
    },
    scroll: function(){
      
      //当内容移动出视窗后,重置内容的位置
      if((t.top + t.height) < 0){
        t.top = t.moveEl.up('div',0).getHeight();
      }
      
      t.top -= t.step;
      
      //移动内容
      t.moveEl.setStyle({'top':t.top + 'px'});
      
      //调用下一次移动
      if(t.flag){
        t.animate = setTimeout(t.scroll, t.delay);
      }
    }
  };
  
  t.init();
  t.scroll();
}
