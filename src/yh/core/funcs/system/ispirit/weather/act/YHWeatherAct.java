package yh.core.funcs.system.ispirit.weather.act;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.sms.logic.YHSmsBoxLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHWeatherAct {
  private static String[][] cityNames = new String[][]{
    {"北京","南京","天津","重庆"},
    {"香港","澳门"},
    {"哈尔滨","齐齐哈尔","牡丹江","大庆","伊春","双鸭山","鹤岗","鸡西","佳木斯","七台河","黑河","绥化","大兴安岭"},
    {"长春","吉林","白山","白城","四平","松原","辽源","延吉","通化"},
    {"沈阳","大连","葫芦岛","旅顺","本溪","抚顺","铁岭","辽阳","营口","阜新","朝阳","锦州","丹东","鞍山","盘锦"},
    {"呼和浩特","乌兰浩特","包头","赤峰","阿拉善左旗","乌海","鄂尔多斯","锡林浩特","通辽","集宁","临河","呼伦贝尔"},
    {"石家庄","唐山","张家口","廊坊","邢台","邯郸","沧州","衡水","承德","保定","秦皇岛"},
    {"郑州","开封","洛阳","平顶山","焦作","鹤壁","新乡","安阳","濮阳","许昌","漯河","三门峡","南阳","商丘","信阳","周口","驻马店","济源"},
    {"济南","青岛","淄博","威海","临沂","烟台","枣庄","聊城","济宁","菏泽","泰安","日照","东营","德州","滨州","莱芜","潍坊"},
    {"太原","阳泉","晋城","晋中","临汾","运城","长治","朔州","忻州","大同","吕梁"},
    {"南京","苏州","南通","徐州","镇江","淮安","盐城","泰州","无锡","连云港","扬州","常州","宿迁"},
    {"合肥","巢湖","蚌埠","安庆","六安","滁州","马鞍山","阜阳","亳州","黄山","宣城","铜陵","淮北","芜湖","宿州","淮南","池州"},
    {"西安","韩城","安康","汉中","宝鸡","咸阳","榆林","渭南","商洛","铜川","延安"},
    {"银川","固原","中卫","石嘴山","吴忠"},
    {"兰州","白银","庆阳","酒泉","天水","武威","张掖","合作","临夏","平凉","定西","金昌","武都"},
    {"西宁","海北","海西","黄南","果洛","玉树","海东","海南"},
    {"武汉","宜昌","黄冈","恩施","荆州","神农架","十堰","咸宁","襄樊","孝感","随州","黄石","荆门","鄂州","天门","仙桃","潜江"},
    {"长沙","邵阳","常德","郴州","吉首","株洲","娄底","湘潭","永州","岳阳","益阳","衡阳","怀化","黔阳","张家界"},
    {"杭州","湖州","金华","宁波","丽水","绍兴","衢州","嘉兴","台州","舟山","温州"},
    {"南昌","萍乡","九江","上饶","抚州","吉安","鹰潭","宜春","新余","景德镇","赣州"},
    {"福州","厦门","龙岩","南平","宁德","莆田","泉州","三明","漳州"},
    {"贵阳","安顺","黔西","遵义","铜仁","六盘水","毕节","凯里","都匀"},
    {"成都","泸州","内江","凉山","阿坝","巴中","广元","乐山","绵阳","德阳","攀枝花","雅安","宜宾","自贡","甘孜","达州","资阳","广安","遂宁","眉山","南充"},
    {"广州","深圳","潮州","韶关","湛江","惠州","清远","东莞","江门","茂名","肇庆","汕尾","河源","揭阳","梅州","中山","德庆","阳江","云浮","珠海","汕头","佛山","东沙岛"},
    {"南宁","桂林","柳州","梧州","玉林","贺州","钦州","贵港","防城港","百色","北海","河池","来宾","崇左"},
    {"昆明","保山","楚雄","德宏","红河","临沧","怒江","曲靖","文山","玉溪","昭通","丽江","大理","普洱","景洪","中甸"},
    {"海口","三亚","儋州","琼山","通什","文昌","东方","临高","澄迈","昌江","白沙","琼中","定安","屯昌","琼海","清兰","保亭","万宁","陵水","西沙","珊瑚岛","永署礁","南沙岛","乐东"},
    {"乌鲁木齐","阿勒泰","阿克苏","昌吉","哈密","和田","喀什","克拉玛依","石河子","塔城","库尔勒","吐鲁番","伊宁","博乐","阿拉尔","阿图什"},
    {"拉萨","阿里","昌都","那曲","日喀则","山南","林芝"},
    {"台北县","高雄","台南","台中","桃园","新竹县","宜兰","马公","嘉义","花莲","台东","彭佳屿"}
  };
  
  private static String[][] cityCodes = new String[][]{
    {"54511","58367","54517","57516"},
    {"45005","45011"},
    {"50953","50745","54094","50842","50774","50884","50775","50978","50873","50971","50468","50853","50442"},
    {"54161","54172","54371","50936","54157","50946","54260","50945","54363"},
    {"54342","54662","54453","54660","54346","54353","54249","54347","54471","54237","54324","54337","54497","54339","54338"},
    {"53463","54102","53446","54218","50527","53512","53543","54103","54135","55135","55136","55137"},
    {"53698","54534","54401","54515","53798","53892","54616","54702","54423","54602","54449"},
    {"57083","57091","57073","57171","53982","53990","53986","53898","54900","57089","57186","57051","57178","58005","57297","57195","57290","57190"},
    {"54823","54857","54830","54775","54938","54765","58024","54806","54915","54906","54827","54945","54736","54714","54734","54828","54843"},
    {"53772","53782","53976","53778","53868","53959","53882","53578","53674","53487","53579"},
    {"58238","58357","58356","58259","58377","58027","58346","58352","58151","58246","58245","58343","58131"},
    {"58321","58326","58221","58424","58311","58236","58336","58203","58204","58205","58433","58429","58116","58334","58122","58224","58427"},
    {"57036","53955","57245","57127","57016","57048","53646","57045","57143","53947","53845"},
    {"53614","53817","53704","53518","53612"},
    {"52889","52896","53829","52533","57006","52679","52652","50741","52984","53915","52995","52675","52885"},
    {"52866","52754","52737","56065","56043","56029","52875","52856"},
    {"57494","57461","57498","57447","57476","57362","57256","57590","57278","57482","57381","58407","57377","57496","57397","57398","57399"},
    {"57687","57766","57662","57972","57649","57780","57763","57773","57866","57585","57872","57749","57771","57558","57678"},
    {"58457","58450","58549","58563","58646","58453","58633","58452","58660","58477","58659"},
    {"58606","57786","58502","58637","58617","57799","58627","57793","57796","58527","57993"},
    {"58847","59134","58927","58834","58846","58946","59137","58828","59126"},
    {"57816","57806","57609","57713","56693","57707","57825","57827","57826"},
    {"56294","57602","57504","56571","56171","57313","57206","56386","56196","56198","56666","56287","56492","56396","56146","57328","56298","57415","57405","56391","57411"},
    {"59287","59493","59312","59082","59658","59298","59280","59289","59473","59659","59278","59501","59293","59315","59117","59485","59269","59663","59471","59488","59316","59276","59279"},
    {"59432","57957","59051","59046","59265","59453","59254","59065","59632","59249","59635","59211","59023","59425"},
    {"56778","56748","56768","56844","56975","56951","56533","56783","56994","56875","56586","56651","56751","56752","56753","56754"},
    {"59758","59948","59845","59757","59941","59856","59857","59858","59859","59860","59861","59862","59863","59864","59865","59866","59867","59868","59869","59870","59871","59872","59873","59876"},
    {"51463","51076","51628","51368","52203","51828","51709","51243","51356","51133","51656","51573","51531","51535","51538","51539"},
    {"55591","55437","56137","55299","55578","55598","56312"},
    {"58968","59356","59357","59358","59359","59360","59361","59362","59363","59365","59366","59568"}
  };
  
  private static Map<String, String> codeMap = new HashMap<String, String>();
  
  static {
    codeMap.put("北京", "101010100");
    codeMap.put("重庆", "101040100");
    codeMap.put("南京", "101020100");
    codeMap.put("天津", "101030100");
    codeMap.put("澳门", "101330101");
    codeMap.put("香港", "101320101");
    codeMap.put("合肥", "101220101");
    codeMap.put("蚌埠", "101220201");
    codeMap.put("芜湖", "101220301");
    codeMap.put("淮南", "101220401");
    codeMap.put("马鞍山", "101220501");
    codeMap.put("安庆", "101220601");
    codeMap.put("宿州", "101220701");
    codeMap.put("阜阳", "101220801");
    codeMap.put("亳州", "101220901");
    codeMap.put("黄山", "101221001");
    codeMap.put("滁州", "101221101");
    codeMap.put("淮北", "101221201");
    codeMap.put("铜陵", "101221301");
    codeMap.put("宣城", "101221401");
    codeMap.put("六安", "101221501");
    codeMap.put("巢湖", "101221601");
    codeMap.put("池州", "101221701");
    codeMap.put("福州", "101230101");
    codeMap.put("厦门", "101230201");
    codeMap.put("宁德", "101230301");
    codeMap.put("莆田", "101230401");
    codeMap.put("泉州", "101230501");
    codeMap.put("漳州", "101230601");
    codeMap.put("龙岩", "101230701");
    codeMap.put("三明", "101230801");
    codeMap.put("南平", "101230901");
    codeMap.put("兰州", "101160101");
    codeMap.put("定西", "101160201");
    codeMap.put("平凉", "101160301");
    codeMap.put("庆阳", "101160401");
    codeMap.put("武威", "101160501");
    codeMap.put("金昌", "101160601");
    codeMap.put("张掖", "101160701");
    codeMap.put("酒泉", "101160801");
    codeMap.put("天水", "101160901");
    codeMap.put("武都", "101161001");
    codeMap.put("临夏", "101161101");
    codeMap.put("合作", "101161201");
    codeMap.put("白银", "101161301");
    codeMap.put("广州", "101280101");
    codeMap.put("韶关", "101280201");
    codeMap.put("惠州", "101280301");
    codeMap.put("梅州", "101280401");
    codeMap.put("汕头", "101280501");
    codeMap.put("深圳", "101280601");
    codeMap.put("珠海", "101280701");
    codeMap.put("佛山", "101280800");
    codeMap.put("肇庆", "101280901");
    codeMap.put("湛江", "101281001");
    codeMap.put("江门", "101281101");
    codeMap.put("河源", "101281201");
    codeMap.put("清远", "101281301");
    codeMap.put("云浮", "101281401");
    codeMap.put("潮州", "101281501");
    codeMap.put("东莞", "101281601");
    codeMap.put("中山", "101281701");
    codeMap.put("阳江", "101281801");
    codeMap.put("揭阳", "101281901");
    codeMap.put("茂名", "101282001");
    codeMap.put("汕尾", "101282101");
    codeMap.put("东沙岛", "101282105");
    codeMap.put("德庆", "101280905");
    codeMap.put("南宁", "101300101");
    codeMap.put("崇左", "101300201");
    codeMap.put("柳州", "101300301");
    codeMap.put("来宾", "101300401");
    codeMap.put("桂林", "101300501");
    codeMap.put("梧州", "101300601");
    codeMap.put("贺州", "101300701");
    codeMap.put("贵港", "101300801");
    codeMap.put("玉林", "101300901");
    codeMap.put("百色", "101301001");
    codeMap.put("钦州", "101301101");
    codeMap.put("河池", "101301201");
    codeMap.put("北海", "101301301");
    codeMap.put("防城港", "101301401");
    codeMap.put("贵阳", "101260101");
    codeMap.put("遵义", "101260201");
    codeMap.put("安顺", "101260301");
    codeMap.put("都匀", "101260401");
    codeMap.put("凯里", "101260501");
    codeMap.put("铜仁", "101260601");
    codeMap.put("毕节", "101260701");
    codeMap.put("六盘水", "101260801");
    codeMap.put("黔西", "101260901");
    codeMap.put("海口", "101310101");
    codeMap.put("琼山", "101310102");
    codeMap.put("三亚", "101310201");
    codeMap.put("东方", "101310202");
    codeMap.put("临高", "101310203");
    codeMap.put("澄迈", "101310204");
    codeMap.put("儋州", "101310205");
    codeMap.put("昌江", "101310206");
    codeMap.put("白沙", "101310207");
    codeMap.put("琼中", "101310208");
    codeMap.put("定安", "101310209");
    codeMap.put("屯昌", "101310210");
    codeMap.put("琼海", "101310211");
    codeMap.put("文昌", "101310212");
    codeMap.put("清兰", "101310213");
    codeMap.put("保亭", "101310214");
    codeMap.put("万宁", "101310215");
    codeMap.put("陵水", "101310216");
    codeMap.put("西沙", "101310217");
    codeMap.put("珊瑚岛", "101310218");
    codeMap.put("永署礁", "101310219");
    codeMap.put("南沙岛", "101310220");
    codeMap.put("乐东", "101310221");
    codeMap.put("通什", "101310222");
    codeMap.put("石家庄", "101090101");
    codeMap.put("保定", "101090201");
    codeMap.put("张家口", "101090301");
    codeMap.put("承德", "101090402");
    codeMap.put("唐山", "101090501");
    codeMap.put("廊坊", "101090601");
    codeMap.put("沧州", "101090701");
    codeMap.put("衡水", "101090801");
    codeMap.put("邢台", "101090901");
    codeMap.put("邯郸", "101091001");
    codeMap.put("秦皇岛", "101091101");
    codeMap.put("郑州", "101180101");
    codeMap.put("安阳", "101180201");
    codeMap.put("新乡", "101180301");
    codeMap.put("许昌", "101180401");
    codeMap.put("平顶山", "101180501");
    codeMap.put("信阳", "101180601");
    codeMap.put("南阳", "101180701");
    codeMap.put("开封", "101180801");
    codeMap.put("洛阳", "101180901");
    codeMap.put("商丘", "101181001");
    codeMap.put("焦作", "101181101");
    codeMap.put("鹤壁", "101181201");
    codeMap.put("濮阳", "101181301");
    codeMap.put("周口", "101181401");
    codeMap.put("漯河", "101181501");
    codeMap.put("驻马店", "101181601");
    codeMap.put("三门峡", "101181701");
    codeMap.put("济源", "101181801");
    codeMap.put("哈尔滨", "101050101");
    codeMap.put("齐齐哈尔", "101050201");
    codeMap.put("牡丹江", "101050301");
    codeMap.put("佳木斯", "101050401");
    codeMap.put("绥化", "101050501");
    codeMap.put("黑河", "101050601");
    codeMap.put("大兴安岭", "101050701");
    codeMap.put("伊春", "101050801");
    codeMap.put("大庆", "101050901");
    codeMap.put("七台河", "101051002");
    codeMap.put("鸡西", "101051101");
    codeMap.put("鹤岗", "101051201");
    codeMap.put("双鸭山", "101051301");
    codeMap.put("武汉", "101200101");
    codeMap.put("襄樊", "101200201");
    codeMap.put("鄂州", "101200301");
    codeMap.put("孝感", "101200401");
    codeMap.put("黄冈", "101200501");
    codeMap.put("黄石", "101200601");
    codeMap.put("咸宁", "101200701");
    codeMap.put("荆州", "101200801");
    codeMap.put("宜昌", "101200901");
    codeMap.put("恩施", "101201001");
    codeMap.put("十堰", "101201101");
    codeMap.put("神农架", "101201201");
    codeMap.put("随州", "101201301");
    codeMap.put("荆门", "101201401");
    codeMap.put("天门", "101201501");
    codeMap.put("仙桃", "101201601");
    codeMap.put("潜江", "101201701");
    codeMap.put("长沙", "101250101");
    codeMap.put("湘潭", "101250201");
    codeMap.put("株洲", "101250301");
    codeMap.put("衡阳", "101250401");
    codeMap.put("郴州", "101250501");
    codeMap.put("常德", "101250601");
    codeMap.put("益阳", "101250701");
    codeMap.put("娄底", "101250801");
    codeMap.put("邵阳", "101250901");
    codeMap.put("岳阳", "101251001");
    codeMap.put("张家界", "101251101");
    codeMap.put("怀化", "101251201");
    codeMap.put("黔阳", "101251301");
    codeMap.put("永州", "101251401");
    codeMap.put("吉首", "101251501");
    codeMap.put("长春", "101060101");
    codeMap.put("吉林", "101060201");
    codeMap.put("延吉", "101060301");
    codeMap.put("四平", "101060401");
    codeMap.put("通化", "101060501");
    codeMap.put("白城", "101060601");
    codeMap.put("辽源", "101060701");
    codeMap.put("松原", "101060801");
    codeMap.put("白山", "101060901");
    codeMap.put("南京", "101190101");
    codeMap.put("无锡", "101190201");
    codeMap.put("镇江", "101190301");
    codeMap.put("苏州", "101190401");
    codeMap.put("南通", "101190501");
    codeMap.put("扬州", "101190601");
    codeMap.put("盐城", "101190701");
    codeMap.put("徐州", "101190801");
    codeMap.put("淮安", "101190901");
    codeMap.put("连云港", "101191001");
    codeMap.put("常州", "101191101");
    codeMap.put("泰州", "101191201");
    codeMap.put("宿迁", "101191301");
    codeMap.put("南昌", "101240101");
    codeMap.put("九江", "101240201");
    codeMap.put("上饶", "101240301");
    codeMap.put("抚州", "101240401");
    codeMap.put("宜春", "101240501");
    codeMap.put("吉安", "101240601");
    codeMap.put("赣州", "101240701");
    codeMap.put("景德镇", "101240801");
    codeMap.put("萍乡", "101240901");
    codeMap.put("新余", "101241001");
    codeMap.put("鹰潭", "101241101");
    codeMap.put("沈阳", "101070101");
    codeMap.put("大连", "101070201");
    codeMap.put("鞍山", "101070301");
    codeMap.put("抚顺", "101070401");
    codeMap.put("本溪", "101070501");
    codeMap.put("丹东", "101070601");
    codeMap.put("锦州", "101070701");
    codeMap.put("营口", "101070801");
    codeMap.put("阜新", "101070901");
    codeMap.put("辽阳", "101071001");
    codeMap.put("铁岭", "101071101");
    codeMap.put("朝阳", "101071201");
    codeMap.put("盘锦", "101071301");
    codeMap.put("葫芦岛", "101071401");
    codeMap.put("旅顺", "101070205");
    codeMap.put("呼和浩特", "101080101");
    codeMap.put("包头", "101080201");
    codeMap.put("乌海", "101080301");
    codeMap.put("集宁", "101080401");
    codeMap.put("通辽", "101080501");
    codeMap.put("赤峰", "101080601");
    codeMap.put("鄂尔多斯", "101080701");
    codeMap.put("临河", "101080801");
    codeMap.put("锡林浩特", "101080901");
    codeMap.put("呼伦贝尔", "101081000");
    codeMap.put("乌兰浩特", "101081101");
    codeMap.put("阿拉善左旗", "101081201");
    codeMap.put("银川", "101170101");
    codeMap.put("石嘴山", "101170201");
    codeMap.put("吴忠", "101170301");
    codeMap.put("固原", "101170401");
    codeMap.put("中卫", "101170501");
    codeMap.put("西宁", "101150101");
    codeMap.put("海东", "101150201");
    codeMap.put("黄南", "101150301");
    codeMap.put("海南", "101150401");
    codeMap.put("果洛", "101150501");
    codeMap.put("玉树", "101150601");
    codeMap.put("海西", "101150701");
    codeMap.put("海北", "101150801");
    codeMap.put("济南", "101120101");
    codeMap.put("青岛", "101120201");
    codeMap.put("淄博", "101120301");
    codeMap.put("德州", "101120401");
    codeMap.put("烟台", "101120501");
    codeMap.put("潍坊", "101120601");
    codeMap.put("济宁", "101120701");
    codeMap.put("泰安", "101120801");
    codeMap.put("临沂", "101120901");
    codeMap.put("菏泽", "101121001");
    codeMap.put("滨州", "101121101");
    codeMap.put("东营", "101121201");
    codeMap.put("威海", "101121301");
    codeMap.put("枣庄", "101121401");
    codeMap.put("日照", "101121501");
    codeMap.put("莱芜", "101121601");
    codeMap.put("聊城", "101121701");
    codeMap.put("太原", "101100101");
    codeMap.put("大同", "101100201");
    codeMap.put("阳泉", "101100301");
    codeMap.put("晋中", "101100401");
    codeMap.put("长治", "101100501");
    codeMap.put("晋城", "101100601");
    codeMap.put("临汾", "101100701");
    codeMap.put("运城", "101100801");
    codeMap.put("朔州", "101100901");
    codeMap.put("忻州", "101101001");
    codeMap.put("吕梁", "101101100");
    codeMap.put("西安", "101110101");
    codeMap.put("咸阳", "101110200");
    codeMap.put("延安", "101110300");
    codeMap.put("榆林", "101110401");
    codeMap.put("渭南", "101110501");
    codeMap.put("商洛", "101110601");
    codeMap.put("安康", "101110701");
    codeMap.put("汉中", "101110801");
    codeMap.put("宝鸡", "101110901");
    codeMap.put("铜川", "101111001");
    codeMap.put("韩城", "101110510");
    codeMap.put("成都", "101270101");
    codeMap.put("攀枝花", "101270201");
    codeMap.put("自贡", "101270301");
    codeMap.put("绵阳", "101270401");
    codeMap.put("南充", "101270501");
    codeMap.put("达州", "101270601");
    codeMap.put("遂宁", "101270701");
    codeMap.put("广安", "101270801");
    codeMap.put("巴中", "101270901");
    codeMap.put("泸州", "101271001");
    codeMap.put("宜宾", "101271101");
    codeMap.put("内江", "101271201");
    codeMap.put("资阳", "101271301");
    codeMap.put("乐山", "101271401");
    codeMap.put("眉山", "101271501");
    codeMap.put("凉山", "101271601");
    codeMap.put("雅安", "101271701");
    codeMap.put("甘孜", "101271801");
    codeMap.put("阿坝", "101271901");
    codeMap.put("德阳", "101272001");
    codeMap.put("广元", "101272101");
    codeMap.put("台北县", "101340101");
    codeMap.put("高雄", "101340201");
    codeMap.put("台南", "101340301");
    codeMap.put("台中", "101340401");
    codeMap.put("桃园", "101340501");
    codeMap.put("新竹县", "101340601");
    codeMap.put("宜兰", "101340701");
    codeMap.put("马公", "101340801");
    codeMap.put("嘉义", "101340901");
    codeMap.put("花莲", "101341001");
    codeMap.put("台东", "101341101");
    codeMap.put("彭佳屿", "101341201");
    codeMap.put("拉萨", "101140101");
    codeMap.put("日喀则", "101140201");
    codeMap.put("山南", "101140301");
    codeMap.put("林芝", "101140401");
    codeMap.put("昌都", "101140501");
    codeMap.put("那曲", "101140601");
    codeMap.put("阿里", "101140701");
    codeMap.put("乌鲁木齐", "101130101");
    codeMap.put("克拉玛依", "101130201");
    codeMap.put("石河子", "101130301");
    codeMap.put("昌吉", "101130401");
    codeMap.put("吐鲁番", "101130501");
    codeMap.put("库尔勒", "101130601");
    codeMap.put("阿拉尔", "101130701");
    codeMap.put("阿克苏", "101130801");
    codeMap.put("喀什", "101130901");
    codeMap.put("伊宁", "101131001");
    codeMap.put("塔城", "101131101");
    codeMap.put("哈密", "101131201");
    codeMap.put("和田", "101131301");
    codeMap.put("阿勒泰", "101131401");
    codeMap.put("阿图什", "101131501");
    codeMap.put("博乐", "1011301601");
    codeMap.put("昆明", "101290101");
    codeMap.put("大理", "101290201");
    codeMap.put("红河", "101290301");
    codeMap.put("曲靖", "101290401");
    codeMap.put("保山", "101290501");
    codeMap.put("文山", "101290601");
    codeMap.put("玉溪", "101290701");
    codeMap.put("楚雄", "101290801");
    codeMap.put("普洱", "101290901");
    codeMap.put("昭通", "101291001");
    codeMap.put("临沧", "101291101");
    codeMap.put("怒江", "101291201");
    codeMap.put("中甸", "101291301");
    codeMap.put("丽江", "101291401");
    codeMap.put("德宏", "101291501");
    codeMap.put("景洪", "101291601");
    codeMap.put("杭州", "101210101");
    codeMap.put("湖州", "101210201");
    codeMap.put("嘉兴", "101210301");
    codeMap.put("宁波", "101210401");
    codeMap.put("绍兴", "101210501");
    codeMap.put("台州", "101210601");
    codeMap.put("温州", "101210701");
    codeMap.put("丽水", "101210801");
    codeMap.put("金华", "101210901");
    codeMap.put("衢州", "101211001");
    codeMap.put("舟山", "101211101");
  }
  
  /**
   * 查询天气的方法
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryWeather(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String cityCode = person.getWeatherCity();
      String id = mapCityCode(cityCode);
      if (YHUtility.isNullorEmpty(id)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "ID错误");
        return "/core/inc/rtjson.jsp";
      }
      String weather = new String(capture(id), "utf-8");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, weather);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询属性");
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 提供给精灵的查询天气的方法
   * @param request
   * @param response
   * @return http://192.168.0.80/yh/yh/core/funcs/system/ispirit/weather/act/YHWeatherAct/queryWeatherCode.act
   * @throws Exception
   */
  public String queryWeatherCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String cityCode = person.getWeatherCity();
      String id = mapCityCode(cityCode);
      if (YHUtility.isNullorEmpty(id)) {
        id = "101010100";
        //request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        //request.setAttribute(YHActionKeys.RET_MSRG, "ID错误");
        //return "/core/inc/rtjson.jsp";
      }
      String weather = new String(capture(id), "utf-8");
      Map<String, String> map = YHFOM.json2Map(weather);
      
      String city = map.get("city");
      String temp1 = map.get("temp1");
      String img1 = map.get("img1");
      String img2 = map.get("img2");
      response.setContentType("text/html;charset=UTF-8"); 
      PrintWriter pw = response.getWriter();
     // response.setCharacterEncoding("GBK");
      pw.format("%s|%s|%s|%s", img1, img2, temp1, city);
      pw.flush();
    } catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 使用httpclient抓取天气信息,解决跨域问题
   * @param id
   * @return
   * @throws IOException
   */
  public byte[] capture(String id) throws IOException {
    if (YHUtility.isNullorEmpty(id)) {
      return null;
    }
    String weatherPath = "http://m.weather.com.cn/data/" + id + ".html";
    HttpClient client = new HttpClient();
    GetMethod getMethod = new GetMethod(weatherPath);
    //设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
    getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
            new DefaultHttpMethodRetryHandler()); 
    //执行getMethod
    int statusCode = client.executeMethod(getMethod);
    if (statusCode != HttpStatus.SC_OK) {
      //System.err.println("Method failed: " + getMethod.getStatusLine());
    }
    byte[] responseBody = getMethod.getResponseBody();
    getMethod.releaseConnection();
    return responseBody;
  }
  
  private String mapCityCode(String cityCode) {
    if (YHUtility.isNullorEmpty(cityCode)) {
      return null;
    }
    String weatherCode = "";
    String cityName = "";
    for (int i = 0; i < cityCodes.length; i++) {
      for (int j = 0; j < cityCodes[i].length; j++) {
        if (cityCode.equals(cityCodes[i][j])) {
          cityName = cityNames[i][j];
          i = cityCodes.length;
          break;
        }
      }
    }
    
    weatherCode = codeMap.get(cityName);
    return weatherCode;
  }
}
