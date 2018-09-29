package yh.rad.dsdef.data;


public class YHGridField{
  private String type;
  private String name;
  private String dbname;
  private String dataType;
  private String width;
  private String text;
  private String format;
  private String render;
  
  public String getDbname() {
    return dbname;
  }

  public void setDbname(String dbname) {
    this.dbname = dbname;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getRender() {
    return render;
  }

  public void setRender(String render) {
    this.render = render;
  }

  public YHGridField(String type, String name, String dataType,
      String text, String dbname) {
    super();
    this.type = type;
    this.name = name;
    this.dataType = dataType;
    this.width = "100";
    this.text = text;
    this.dbname = dbname;
  }

  @Override
  public String toString() {
    return "YHGridField [dataType=" + dataType + ", format=" + format
        + ", name=" + name + ", render=" + render + ", text=" + text
        + ", type=" + type + ", width=" + width + "]";
  }
  
}
