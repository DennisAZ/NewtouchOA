<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>世界时间</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript">

var OA_TIME = new Date();
var Today = new Date();
var tY = Today.getFullYear();
var tM = Today.getMonth();
var tD = Today.getDate();

//世界时间资料
var timeData = {
"Asia               亚洲": {   //----------------------------------------------
"Brunei             文莱    ":["+0800","","斯里巴加湾市"],
"Burma              缅甸    ":["+0630","","仰光"],
"Cambodia           柬埔寨  ":["+0700","","金边"],
"China              中国    ":["+0800","","北京、重庆、南京、天津"],
"Hong kong,Macau    香港·澳门":["+0800","","香港、澳门特区"],
"Indonesia          印尼    ":["+0700","","雅加达"],
"Japan              日本    ":["+0900","","东京、大阪、札幌"],
"Korea              韩国    ":["+0900","","汉城"],
"Laos               老挝    ":["+0700","","万象"],
"Malaysia           马来西亚":["+0800","","吉隆坡"],
"Mongolia           蒙古    ":["+0800","03L03|09L03","乌兰巴托、库伦"],
"Philippines        菲律宾  ":["+0800","04F53|10F53","马尼拉"],
"Russia(Anadyr)     俄罗斯  ":["+1300","03L03|10L03","阿纳德尔河"],
"Russia(Kamchatka)  俄罗斯  ":["+1200","03L03|10L03","堪察加半岛"],
"Russia(Magadan)    俄罗斯  ":["+1100","03L03|10L03","马加丹"],
"Russia(Vladivostok)俄罗斯  ":["+1000","03L03|10L03","符拉迪沃斯托克(海参崴)"],
"Russia(Yakutsk)    俄罗斯  ":["+0900","03L03|10L03","雅库茨克"],
"Singapore          新加坡  ":["+0800","","新加坡"],
"TaiPei             中国台北":["+0800","","台北、高雄"],
"Thailand           泰国    ":["+0700","","曼谷"],
"Urumchi            中国乌鲁木齐":["+0700","","乌鲁木齐"],
"Vietnam            越南    ":["+0700","","河内"]
},
"ME, India pen.     中东、印度半岛": {   //------------------------------------
"Afghanistan        阿富汗  ":["+0430","","喀布尔"],
"Arab Emirates      阿拉伯联合酋长国":["+0400","","阿布扎比"],
"Bahrain            巴林    ":["+0300","","麦纳麦"],
"Bangladesh         孟加拉  ":["+0600","","达卡"],
"Bhutan             不丹    ":["+0600","","廷布"],
"Cyprus             塞浦路斯":["+0200","","尼科西亚"],
"Georgia            乔治亚  ":["+0500","","第比利斯"],
"India              印度    ":["+0530","","新德里、孟买、加尔各答"],
"Iran               伊朗    ":["+0330","04 13|10 13","德黑兰"],
"Iraq               伊拉克  ":["+0300","04 13|10 13","巴格达"],
"Israel             以色列·巴勒斯坦":["+0200","04F53|09F53","耶路撒冷"],
"Jordan             约旦    ":["+0200","","安曼"],
"Kuwait             科威特  ":["+0300","","科威特城"],
"Lebanon            黎巴嫩  ":["+0200","03L03|10L03","贝鲁特"],
"Maldives           马尔代夫":["+0500","","马累"],
"Nepal              尼泊尔  ":["+0545","","加德满都"],
"Oman               阿曼    ":["+0400","","马斯喀特"],
"Pakistan           巴基斯坦":["+0500","","卡拉奇、伊斯兰堡"],
"Qatar              卡塔尔  ":["+0300","","多哈"],
"Saudi Arabia       沙特阿拉伯":["+0300","","利雅得"],
"Sri Lanka          斯里兰卡":["+0600","","科伦坡"],
"Syria              叙利亚  ":["+0200","04 13|10 13","大马士革"],
"Tajikistan         塔吉克斯坦":["+0500","","杜尚别"],
"Turkey             土耳其  ":["+0200","","伊斯坦堡"],
"Turkmenistan       土库曼斯坦":["+0500","","阿什哈巴德"],
"Uzbekistan         乌兹别克斯坦":["+0500","","塔什干"],
"Yemen              也门    ":["+0300","","萨那"]
},
"North Europe       北欧": {   //----------------------------------------------
"Denmark            丹麦":["+0100","04F03|10L03","哥本哈根"],
"Finland            芬兰":["+0200","03L01|10L01","赫尔辛基"],
"Iceland            冰岛":["+0000","","雷克雅未克"],
"Norwegian          挪威":["+0100","","奥斯陆"],
"Sweden             瑞典":["+0100","03L01|10L01","斯德哥尔摩"]
},
"Eastern Europe     中欧、东欧": {   //----------------------------------------
"Armenia            亚美尼亚":["+0400","","埃里温"],
"Austria            奥地利  ":["+0100","03L01|10L01","维也纳"],
"Azerbaijan         阿塞拜疆":["+0400","","巴库"],
"Czech              捷克    ":["+0100","","布拉格"],
"Estonia            爱沙尼亚":["+0200","","塔林"],
"Germany            德国    ":["+0100","03L01|10L01","柏林、波恩"],
"Hungarian          匈牙利  ":["+0100","","布达佩斯"],
"Kazakhstan(Astana) 哈萨克斯坦":["+0600","","阿斯塔纳、阿拉木图"],
"Kazakhstan(Aqtobe) 哈萨克斯坦":["+0500","","阿克托别"],
"Kazakhstan(Aqtau)  哈萨克斯坦":["+0400","","阿克图"],
"Kirghizia          吉尔吉斯":["+0500","","比斯凯克"],
"Latvia             拉脱维亚":["+0200","","里加"],
"Lithuania          立陶宛  ":["+0200","","维尔纽斯"],
"Moldova            摩尔多瓦":["+0200","","基希纳乌"],
"Poland             波兰    ":["+0100","","华沙"],
"Rumania            罗马尼亚":["+0200","","布加勒斯特"],
"Russia(Moscow)     俄罗斯  ":["+0300","03L03|10L03","莫斯科"],
"Russia(Volgograd)  俄罗斯  ":["+0300","03L03|10L03","伏尔加格勒"],
"Slovakia           斯洛伐克":["+0100","","布拉迪斯拉发"],
"Switzerland        瑞士    ":["+0100","","苏黎世"],
"Ukraine            乌克兰  ":["+0200","","基辅"],
"Ukraine(Simferopol)乌克兰  ":["+0300","","辛菲罗波尔"],
"Belarus            白俄罗斯":["+0200","03L03|10L03","明斯克"]
},
"Western Europe     西欧": {   //----------------------------------------------
"Belgium            比利时 ":["+0100","03L01|10L01","布鲁塞尔"],
"France             法国   ":["+0100","03L01|10L01","巴黎"],
"Ireland            爱尔兰 ":["+0000","03L01|10L01","都柏林"],
"Monaco             摩纳哥 ":["+0100","","摩纳哥市"],
"Netherlands        荷兰   ":["+0100","03L01|10L01","阿姆斯特丹"],
"Luxembourg         卢森堡 ":["+0100","03L01|10L01","卢森堡市"],
"United Kingdom     英国   ":["+0000","03L01|10L01","伦敦、爱丁堡"]
},
"South Europe       南欧": { //------------------------------------------------
"Albania            阿尔巴尼亚":["+0100","","地拉那"],
"Bulgaria           保加利亚":["+0200","","索菲亚"],
"Greece             希腊    ":["+0200","03L01|10L01","雅典"],
"Holy See           罗马教廷":["+0100","","梵蒂冈"],
"Italy              意大利  ":["+0100","03L01|10L01","罗马"],
"Malta              马耳他  ":["+0100","","瓦莱塔"],
"Portugal           葡萄牙  ":["+0000","03L01|10L01","里斯本"],
"San Marino         圣马利诺":["+0100","","圣马利诺"],
"Span               西班牙  ":["+0100","03L01|10L01","马德里"],
"Slovenia           斯洛文尼亚":["+0100","","卢布尔雅那"],
"Yugoslavia         南斯拉夫(塞尔维亚)":["+0100","","贝尔格莱德"]
},
"North America      北美洲": {   //--------------------------------------------
"Canada(NST)        加拿大":["-0330","04F02|10L02","纽芬兰、圣约翰、古斯湾"],
"Canada(AST)        加拿大":["-0400","04F02|10L02","冰河湾、Pangnirtung"],
"Canada(EST)        加拿大":["-0500","04F02|10L02","蒙特罗"],
"Canada(CST)        加拿大":["-0600","04F02|10L02","雷迦納、雨河鎮、Swift Current"],
"Canada(MST)        加拿大":["-0700","04F02|10L02","印奴维特港湾、埃德蒙顿、道森河"],
"Canada(PST)        加拿大":["-0800","04F02|10L02","温哥华"],
"US(Eastern)        美国(东岸)":["-0500","04F02|10L02","华盛顿、纽约"],
"US(Indiana)        美国      ":["-0500","","印第安纳"],
"US(Central)        美国(中部)":["-0600","04F02|10L02","芝加哥"],
"US(Mountain)       美国(山区)":["-0700","04F02|10L02","丹佛"],
"US(Arizona)        美国      ":["-0700","","亚历桑那"],
"US(Pacific)        美国(西岸)":["-0800","04F02|10L02","旧金山、洛杉矶"],
"US(Alaska)         美国      ":["-0900","","阿拉斯加、朱诺"]
},
"South America      中南美洲": {   //------------------------------------------
"Antigua & Barbuda  安提瓜岛及巴布达岛":["-0400","","圣约翰"],
"Argentina          阿根廷  ":["-0300","","布宜诺斯艾利斯"],
"Bahamas            巴哈马  ":["-0500","","拿骚"],
"Barbados           巴巴多斯岛":["-0400","","布里奇顿(桥镇)"],
"Belize             贝里斯  ":["-0600","","贝里斯"],
"Bolivia            玻利维亚":["-0400","","拉巴斯"],
"Brazil(AST)        巴西    ":["-0500","10F03|02L03","Porto Acre"],
"Brazil(EST)        巴西    ":["-0300","10F03|02L03","巴西利亚、里约热内卢"],
"Brazil(FST)        巴西    ":["-0200","10F03|02L03","诺罗纳"],
"Brazil(WST)        巴西    ":["-0400","10F03|02L03","库亚巴"],
"Chilean            智利    ":["-0500","10F03|03F03","Hanga Roa"],
"Chilean            智利    ":["-0300","10F03|03F03","圣地亚哥"],
"Colombia           哥伦比亚":["-0500","","波哥大"],
"Costa Rica         哥斯达黎加":["-0600","","圣何塞"],
"Cuba               古巴    ":["-0500","04 13|10L03","哈瓦那"],
"Dominican          多米尼加":["-0400","","圣多明各、罗梭"],
"Ecuador            厄瓜多尔":["-0500","","基多"],
"El Salvador        萨尔瓦多":["-0600","","圣萨尔瓦多"],
"Falklands          福克兰群岛":["-0300","09F03|04F03","史丹利"],
"Guatemala          危地马拉":["-0600","","危地马拉城"],
"Haiti              海地    ":["-0500","","太子港"],
"Honduras           洪都拉斯":["-0600","","特古西加尔巴"],
"Jamaica            牙买加  ":["-0500","","金斯敦"],
"Mexico(Mazatlan)   墨西哥  ":["-0700","","马萨特兰"],
"Mexico(首都)       墨西哥  ":["-0600","","墨西哥城"],
"Mexico(蒂华纳)     墨西哥  ":["-0800","","蒂华纳"],
"Nicaragua          尼加拉瓜":["-0500","","马那瓜"],
"Panama             巴拿马  ":["-0500","","巴拿马市"],
"Paraguay           巴拉圭  ":["-0400","10F03|02L03","亚松森"],
"Peru               秘鲁    ":["-0500","","利马"],
"Saint Kitts & Nevis 圣基茨和尼维斯":["-0400","","巴斯特尔(Basseterre)"],
"St. Lucia          圣卢西亚":["-0400","","卡斯特里"],
"St. Vincent & Grenadines 圣文森特和格林纳丁斯":["-0400","","金斯敦"],
"Suriname           苏里南":["-0300","","帕拉马里博(Paramaribo)"],
"Trinidad & Tobago  特立尼达和多巴哥":["-0400","","西班牙港"],
"Uruguay            乌拉圭  ":["-0300","","蒙得维的亚"],
"Venezuela          委内瑞拉":["-0400","","加拉加斯"]
},
"Africa             非洲": {   //----------------------------------------------
"Algeria            阿尔及利亚":["+0100","","阿尔及尔"],
"Angola             安哥拉  ":["+0100","","罗安达"],
"Benin              贝南    ":["+0100","","新港"],
"Botswana           博茨瓦纳":["+0200","","哈博罗内"],
"Burundi            布隆迪  ":["+0200","","布琼布拉"],
"Cameroon           喀麦隆  ":["+0100","","雅温得"],
"Cape Verde         佛德角  ":["-0100","","普拉亚"],
"Central African    中非共和国":["+0100","","班吉"],
"Chad               乍得    ":["+0100","","恩贾梅纳市"],
"Congo              刚果(布)":["+0100","","布拉柴维尔"],
"Djibouti           吉布提  ":["+0300","","吉布提"],
"Egypt              埃及    ":["+0200","04L53|09L43","开罗"],
"Equatorial Guinea  赤道几内亚":["+0100","","马博托"],
"Ethiopia           埃塞俄比亚":["+0300","","亚的斯亚贝巴"],
"Gabon              加蓬    ":["+0100","","利伯维尔"],
"Gambia             冈比亚  ":["+0000","","班珠尔"],
"Ghana              加纳    ":["+0000","","阿克拉"],
"Guinea             几内亚  ":["+0000","","科纳克里"],
"Ivory Coast        象牙海岸":["+0000","","阿比让、雅穆索戈"],
"Kenya              肯尼亚  ":["+0300","","内罗毕"],
"Lesotho            莱索托  ":["+0200","","马塞卢"],
"Liberia            利比里亚":["+0000","","蒙罗维亚"],
"Madagascar         马达加斯加":["+0300","","塔那那利佛"],
"Malawi             马拉维  ":["+0200","","利隆圭"],
"Mali               马里    ":["+0000","","巴马科"],
"Mauritania         毛里塔尼亚":["+0000","","努瓦克肖特"],
"Mauritius          毛里求斯":["+0400","","路易港"],
"Morocco            摩洛哥  ":["+0000","","卡萨布兰卡"],
"Mozambique         莫桑比克":["+0200","","马普托"],
"Namibia            纳米比亚":["+0200","09F03|04F03","温得和克"],
"Niger              尼日尔  ":["+0100","","尼亚美"],
"Nigeria            尼日利亚":["+0100","","阿布贾"],
"Rwanda             卢旺达  ":["+0200","","基加利"],
"Sao Tome           圣多美  ":["+0000","","圣多美"],
"Senegal            塞内加尔":["+0000","","达卡尔"],
"Sierra Leone       狮子山国":["+0000","","自由城"],
"Somalia            索马里  ":["+0300","","摩加迪沙"],
"South Africa       南非    ":["+0200","","开普敦、普利托里亚"],
"Sudan              苏丹    ":["+0200","","喀土穆"],
"Tanzania           坦桑尼亚":["+0300","","达累斯萨拉姆"],
"Togo               多哥    ":["+0000","","洛美隆"],
"Tunisia            突尼斯  ":["+0100","","突尼斯市"],
"Uganda             乌干达  ":["+0300","","坎帕拉"],
"Zaire              扎伊尔(刚果金)  ":["+0100","","金沙萨"],
"Zambia             赞比亚  ":["+0200","","卢萨卡"],
"Zimbabwe           津巴布韦":["+0200","","哈拉雷"]
},
"Oceania            大洋洲": { //----------------------------------------------
"American Samoa(US) 美属萨摩亚(美)":["-1100","","帕果帕果港"],
"Aus.(Adelaide)     澳大利亚  ":["+0930","10F03|03F03","阿得雷德"],
"Aus.(Brisbane)     澳大利亚  ":["+1000","10F03|03F03","布里斯班"],
"Aus.(Darwin)       澳大利亚  ":["+0930","10F03|03F03","达尔文"],
"Aus.(Hobart)       澳大利亚  ":["+1000","10F03|03F03","荷伯特"],
"Aus.(Perth)        澳大利亚  ":["+0800","10F03|03F03","佩思"],
"Aus.(Sydney)       澳大利亚  ":["+1000","10F03|03F03","悉尼"],
"Cook Islands(NZ)   库克群岛(新西兰)  ":["-1000","","阿瓦鲁阿"],
"Eniwetok           埃尼威托克岛":["-1200","","埃尼威托克岛"],
"Fiji               斐济      ":["+1200","11F03|02L03","苏瓦"],
"Guam               关岛      ":["+1000","","阿加尼亚"],
"Hawaii(US)         夏威夷(美)":["-1000","","檀香山"],
"Kiribati           基里巴斯  ":["+1100","","塔拉瓦"],
//"Mariana Islands    塞班岛    ":["","","塞班岛"],
"Marshall Is.       马绍尔群岛":["+1200","","马朱罗"],
"Micronesia         密克罗尼西亚联邦":["+1000","","帕利基尔(Palikir)"],
"Midway Is.(US)     中途岛(美)":["-1100","","中途岛"],
"Nauru Rep.         瑙鲁共和国":["+1200","","亚伦"],
"New Calednia(FR)   新克里多尼亚(法)":["+1100","","努美阿"],
"New Zealand        新西兰    ":["+1200","10F03|04F63","奥克兰"],
"New Zealand(CHADT) 新西兰    ":["+1245","10F03|04F63","惠灵顿"],
"Niue(NZ)           纽埃(新)      ":["-1100","","阿洛菲(Alofi)"],
"Nor. Mariana Is.   北马里亚纳群岛(美)":["+1000","","塞班岛"],
"Palau              帕劳群岛(帛琉群岛)      ":["+0900","","科罗尔"],
"Papua New Guinea   巴布亚新几内亚":["+1000","","莫尔斯比港"],
"Pitcairn Is.(UK)   皮特克恩群岛(英)":["-0830","","亚当斯敦"],
"Polynesia(FR)      玻利尼西亚(法)":["-1000","","巴比蒂、塔希提"],
"Solomon Is.        所罗门群岛":["+1100","","霍尼亚拉"],
"Tahiti             塔希提  ":["-1000","","帕佩特"],
"Tokelau(NZ)        托克劳群岛(新)    ":["-1100","","努库诺努、法考福、阿塔富"],
"Tonga              汤加    ":["+1300","10F63|04F63","努库阿洛法"],
"Tuvalu             图瓦卢  ":["+1200","","富纳富提"],
"Western Samoa      西萨摩亚":["-1100","","阿皮亚"],
"国际换日线                   ":["-1200","","国际换日线"]
}
};

var nStr1 = new Array('日','一','二','三','四','五','六','七','八','九','十');

/*****************************************************************************
                                  世界时间计算
*****************************************************************************/
var OneHour = 60*60*1000;
var OneDay = OneHour*24;
var TimezoneOffset = Today.getTimezoneOffset()*60*1000;

function showUTC(objD) {
   var dn,s;
   var hh = objD.getUTCHours();
   var mm = objD.getUTCMinutes();
   var ss = objD.getUTCSeconds();
   s = objD.getUTCFullYear() + "年" + (objD.getUTCMonth() + 1) + "月" + objD.getUTCDate() +"日 ("+ nStr1[objD.getUTCDay()] +")";

   if(hh>12) { hh = hh-12; dn = '下午'; }
   else dn = '上午';

   if(hh<10) hh = '0' + hh;
   if(mm<10) mm = '0' + mm;
   if(ss<10) ss = '0' + ss;

   s += " " + dn + ' ' + hh + ":" + mm + ":" + ss;
   return(s);
}

function showLocale(objD) {
   var dn,s;
   var hh = objD.getHours();
   var mm = objD.getMinutes();
   var ss = objD.getSeconds();
   s = objD.getFullYear() + "年" + (objD.getMonth() + 1) + "月" + objD.getDate() +"日 ("+ nStr1[objD.getDay()] +")";

   if(hh>12) { hh = hh-12; dn = '下午'; }
   else dn = '上午';

   if(hh<10) hh = '0' + hh;
   if(mm<10) mm = '0' + mm;
   if(ss<10) ss = '0' + ss;

   s += " " + dn + ' ' + hh + ":" + mm + ":" + ss;
   return(s);
}

//传入时差字串, 返回偏移之正负毫秒

function parseOffset(s) {
   var sign,hh,mm,v;
   sign = s.substr(0,1)=='-'?-1:1;
   hh = Math.floor(s.substr(1,2));
   mm = Math.floor(s.substr(3,2));
   v = sign*(hh*60+mm)*60*1000;
   return(v);
}

//返回UTC日期控件 (年,月-1,第几个星期几,几点)
function getWeekDay(y,m,nd,w,h){
   var d,d2,w1;
   if(nd>0){
      d = new Date(Date.UTC(y, m, 1));
      w1 = d.getUTCDay();
      d2 = new Date( d.getTime() + ((w<w1? w+7-w1 : w-w1 )+(nd-1)*7   )*OneDay + h*OneHour);
   }
   else {
      nd = Math.abs(nd);
      d = new Date( Date.UTC(y, m+1, 1)  - OneDay );
      w1 = d.getUTCDay();
      d2 = new Date( d.getTime() + (  (w>w1? w-7-w1 : w-w1 )-(nd-1)*7   )*OneDay + h*OneHour);
   }
   return(d2);
}

//传入某时间值, 日光节约字串 返回 true 或 false
function isDaylightSaving(d,strDS) {

   if(strDS == '') return(false);

   var m1,n1,w1,t1;
   var m2,n2,w2,t2;
   with (Math){
      m1 = floor(strDS.substr(0,2))-1;
      w1 = floor(strDS.substr(3,1));
      t1 = floor(strDS.substr(4,1));
      m2 = floor(strDS.substr(6,2))-1;
      w2 = floor(strDS.substr(9,1));
      t2 = floor(strDS.substr(10,1));
   }

   switch(strDS.substr(2,1)){
      case 'F': n1=1; break;
      case 'L': n1=-1; break;
      default : n1=0; break;
   }

   switch(strDS.substr(8,1)){
      case 'F': n2=1; break;
      case 'L': n2=-1; break;
      default : n2=0; break;
   }


   var d1, d2, re;

   if(n1==0)
      d1 = new Date(Date.UTC(d.getUTCFullYear(), m1, Math.floor(strDS.substr(2,2)),t1));
   else
      d1 = getWeekDay(d.getUTCFullYear(),m1,n1,w1,t1);

   if(n2==0)
      d2 = new Date(Date.UTC(d.getUTCFullYear(), m2, Math.floor(strDS.substr(8,2)),t2));
   else
      d2 = getWeekDay(d.getUTCFullYear(),m2,n2,w2,t2);

   if(d2>d1)
      re = (d>d1 && d<d2)? true: false;
   else
      re = (d>d1 || d<d2)? true: false;

   return(re);
}

var isDS = false;
var UTC_TIME = new Date();
//计算全球时间
function getGlobeTime() {
   var d,s;
   d = new Date();

   d.setTime(OA_TIME.getTime()+parseOffset(objTimeZone[0]));

   isDS=isDaylightSaving(d,objTimeZone[1]);
   if(isDS) d.setTime(d.getTime()+OneHour);
   return(showUTC(d));
}

var objTimeZone;
var objContinentMenu;
var objCountryMenu;

function tick() {
   LocalTime.innerHTML = showLocale(OA_TIME);
   GlobeTime.innerHTML = getGlobeTime();
   OA_TIME.setSeconds(OA_TIME.getSeconds()+1);
   window.setTimeout("tick()", 1000);
}

//指定自定索引时区
function setTZ(a,c){
   objContinentMenu.options[a].selected=true;
   chContinent();
   objCountryMenu.options[c].selected=true;
   chCountry();
}

//变更区域
function chContinent() {
   var key,i;
   continent = objContinentMenu.options[objContinentMenu.selectedIndex].value;
   for (var i = objCountryMenu.options.length-1; i >= 0; i--)
      objCountryMenu[0]=null;

   for (key in timeData[continent])   {
      var option = new Option();
      option.value = key;
      option.text = key;
      objCountryMenu.options[objCountryMenu.options.length]=option;
   }
   objCountryMenu.options[0].selected=true;
   chCountry();
}

//变更国家
function chCountry() {
   var txtContinent = objContinentMenu.options[objContinentMenu.selectedIndex].value;
   var txtCountry = objCountryMenu.options[objCountryMenu.selectedIndex].value;

   objTimeZone = timeData[txtContinent][txtCountry];

   getGlobeTime();

   //地图位移
   City.innerHTML = (isDS==true?"<SPAN STYLE='font-size:12pt;font-family:Wingdings; color:Red;'>R</span> ":'') + objTimeZone[2]; //首都
   var pos = Math.floor(objTimeZone[0].substr(0,3));
   if(pos<0) pos+=24;
   pos*=-10;
   $("world").style.left = pos;

}

function setCookie(name,value) {
   var today = new Date();
   var expires = new Date();
   expires.setTime(today.getTime() + 1000*60*60*24*365);
   document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString();
}

function getCookie(Name) {
   var search = Name + "=";
   if(document.cookie.length > 0) {
      offset = document.cookie.indexOf(search);
      if(offset != -1) {
         offset += search.length;
         end = document.cookie.indexOf(";", offset);
         if(end == -1) end = document.cookie.length;
         return unescape(document.cookie.substring(offset, end));
      }
      else return('');
   }
   else return('');
}


function initialize() {
   var key;
   //时间
   var map = $("map");
   map.filters.Light.Clear();
   map.filters.Light.addAmbient(255,255,255,60);
   map.filters.Light.addCone(120, 60, 80, 120, 60, 255,255,255,120,60);

   objContinentMenu=document.WorldClock.continentMenu;
   objCountryMenu=document.WorldClock.countryMenu;
   for (key in timeData){
      var option = new Option();
      option.value = key;
      option.text = key;
      objContinentMenu[objContinentMenu.length]=option;
   }

   var TZ1 = getCookie('TZ1');
   var TZ2 = getCookie('TZ2');


   if(TZ1=='') {TZ1=0; TZ2=3;}
   setTZ(TZ1,TZ2);

   tick();  
}

function terminate() {
   setCookie("TZ1",objContinentMenu.selectedIndex);
   setCookie("TZ2",objCountryMenu.selectedIndex);
}
</script>

<style>.todyaColor {
  background-color: aqua;
}
</style>

<body onload="initialize()" onunload="terminate()" topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/yh.gif" width="22" height="22"><span class="big3"> 世界时间</span>
    </td>
  </tr>
</table>
<br>

<table border="0" align="center" class="TableData">
  <tr>
    <form name=WorldClock>
    <td vAlign=top align=middle width=240>
    <font style="font-size: 9pt" size=2>本地时间</font>
    <br>
    <span id=LocalTime style="font-size: 11pt; color: #000080; font-family: Arial">0000年0月0日(　)午 00:00:00</span>
    <p>

    <span id=City style="font-size: 9pt; width: 150px;">中国</span>
    <br>
    <span id=GlobeTime style="font-size: 11pt; color: #000080; font-family: Arial">0000年0月0日(　)午 00:00:00</span>
    <br>

    <table style="font-size: 10pt; Y Wingdings">
      <tr>
       <TD align=middle>&Uacute;
 
          <DIV id=map style="position: relative;FILTER: Light; OVERFLOW: hidden; WIDTH: 240px; HEIGHT: 120px; BACKGROUND-COLOR: mediumblue; ">
            <FONT id=world style="FONT-SIZE: 185px; LEFT: 0px; COLOR: green; FONT-FAMILY: Webdings; POSITION: relative; TOP: -26px">ûû</FONT>
          </DIV>&Ugrave;
       </TD>
      </tr>
    </table>

    <br>
    <select style="font: 9pt; width: 240px; background-color: #e0e0ff" onchange=chContinent() name=continentMenu></select>
    <br>
    <select style="font: 9pt; width: 240px; background-color: #e0e0ff" onchange=chCountry() name=countryMenu></select>
  
   </td>
   </form>
  </tr>
</table>

</body>
</html>
