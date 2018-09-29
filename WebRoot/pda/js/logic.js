var height = (document.body || document.documentELement).scrollHeight;
var ctx = document.getElementById("list_main");
var temp = (document.getElementById("message") ? 20 : 0);
var borderHeight = document.getElementById("list_top").scrollHeight + 9 + temp;
var bottom = document.getElementById("list_bottom");
borderHeight += (bottom ? bottom.scrollHeight : 0);
if (ctx.scrollHeight < (height - borderHeight)) {
  ctx.style.height = height - borderHeight + "px";
}
