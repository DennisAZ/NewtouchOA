package yh.subsys.oa.book.data;

  public class YHBookManager {
    
    private int seqId; //自增Id
    
    private String managerId; //管理员userId
    
    private String manageDeptId; //管理范围（部门）
    
    private String managerNames;
    
    private String deptNames;

    public String getManagerNames(){
      return managerNames;
    }
    public void setManagerNames(String managerNames){
      this.managerNames = managerNames;
    }
    public String getDeptNames(){
      return deptNames;
    }
    public void setDeptNames(String deptNames){
      this.deptNames = deptNames;
    }
    public int getSeqId() {
      return seqId;
    }
    public void setSeqId(int seqId) {
      this.seqId = seqId;
    }
    public String getManagerId() {
      return managerId;
    }
    public void setManagerId(String managerId) {
      this.managerId = managerId;
    }
    public String getManageDeptId() {
      return manageDeptId;
    }
    public void setManageDeptId(String manageDeptId) {
      this.manageDeptId = manageDeptId;
    }
    
  }
