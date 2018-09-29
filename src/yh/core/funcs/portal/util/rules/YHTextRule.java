package yh.core.funcs.portal.util.rules;

public class YHTextRule extends YHModulesRule {
  public static final String TYPE = "text";
  
  public YHTextRule (String[] text) {
    super();
    attribute.put("type", TYPE);
    map.put("text", text);
  }
  
  public YHTextRule (String text) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{text});
  }
}