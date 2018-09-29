package yh.rad.taskmgr.data;

public class YHTask {
  private String taskPath = null;  
  private String taskDesc = null;
  private String startDate = null;
  private int workloads = 1;
  private String responsiblePerson = null;
  private String taskSort = null;
  private String funcsCmp = null;
  private String state = null;
  
  /**
   * Copy对象
   * @param other
   */
  public void copy(YHTask other) {
    if (other == null) {
      return;
    }
    this.taskPath = other.getTaskPath();
    this.taskDesc = other.getTaskDesc();
    this.startDate = other.getStartDate();
    this.workloads = other.getWorkloads();
    this.responsiblePerson = other.getResponsiblePerson();
    this.funcsCmp = other.getFuncsCmp();
    this.state = other.getState();
    this.taskSort = other.getTaskSort();
  }
  
  public String getTaskPath() {
    return taskPath;
  }
  public void setTaskPath(String taskPath) {
    this.taskPath = taskPath;
  }
  public String getTaskDesc() {
    return taskDesc;
  }
  public void setTaskDesc(String taskDesc) {
    this.taskDesc = taskDesc;
  }
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public int getWorkloads() {
    return workloads;
  }
  public void setWorkloads(int workloads) {
    this.workloads = workloads;
  }
  public String getResponsiblePerson() {
    return responsiblePerson;
  }
  public void setResponsiblePerson(String responsiblePerson) {
    this.responsiblePerson = responsiblePerson;
  }
  public String getFuncsCmp() {
    return funcsCmp;
  }
  public void setFuncsCmp(String funcsCmp) {
    this.funcsCmp = funcsCmp;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }

  public String getTaskSort() {
    return taskSort;
  }

  public void setTaskSort(String taskSort) {
    this.taskSort = taskSort;
  }
}
