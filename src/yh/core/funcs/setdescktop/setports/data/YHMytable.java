package yh.core.funcs.setdescktop.setports.data;

public class YHMytable {
  private int seqId;
  private int moduleNo;
	private String moduleFile;
	private String modulePos;
	private String viewType;
	private int moduleLines;
	private String moduleScroll;
	
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public int getModuleNo() {
    return moduleNo;
  }
  public void setModuleNo(int moduleNo) {
    this.moduleNo = moduleNo;
  }
  public String getModuleFile() {
    return moduleFile;
  }
  public void setModuleFile(String moduleFile) {
    this.moduleFile = moduleFile;
  }
  public String getModulePos() {
    return modulePos;
  }
  public void setModulePos(String modulePos) {
    this.modulePos = modulePos;
  }
  public String getViewType() {
    return viewType;
  }
  public void setViewType(String viewType) {
    this.viewType = viewType;
  }
  public int getModuleLines() {
    return moduleLines;
  }
  public void setModuleLines(int moduleLines) {
    this.moduleLines = moduleLines;
  }
  public String getModuleScroll() {
    return moduleScroll;
  }
  public void setModuleScroll(String moduleScroll) {
    this.moduleScroll = moduleScroll;
  }
  
  public boolean equals(Object o){
    return this.seqId == ((YHMytable)o).getSeqId();
  }
}
