package yh.core.funcs.portal.util.rules;


public class YHEventRule extends YHModulesRule {
  public static final String TYPE = "event";
  
  public YHEventRule (String text[], String func[]) {
    super();
    attribute.put("type", TYPE);
    map.put("text", text);
    map.put("event", func);
  }
  
  public YHEventRule (String text[],  String func[], String src[]) {
    super();
    attribute.put("type", TYPE);
    map.put("text", text);
    map.put("src", src);
    map.put("event", func);
  }
  
  public YHEventRule (String text, String func) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{text});
    map.put("event", new String[]{func});
  }
  
  public YHEventRule (String text,  String func, String src) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{text});
    map.put("src", new String[]{src});
    map.put("event", new String[]{func});
  }
}