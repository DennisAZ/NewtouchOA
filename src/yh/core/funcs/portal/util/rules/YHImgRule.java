package yh.core.funcs.portal.util.rules;


public class YHImgRule extends YHModulesRule {
  public static final String TYPE = "img";
  
  public YHImgRule (String title[], String src[]) {
    super();
    attribute.put("type", TYPE);
    map.put("text", title);
    map.put("src", src);
  }
  
  public YHImgRule (String title[],  String src[], String link[]) {
    super();
    attribute.put("type", TYPE);
    map.put("text", title);
    map.put("src", src);
    map.put("href", link);
  }
  
  public YHImgRule (String title, String src) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{title});
    map.put("src", new String[]{src});
  }
  
  public YHImgRule (String title,  String src, String link) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{title});
    map.put("src", new String[]{src});
    map.put("href", new String[]{link});
  }
}