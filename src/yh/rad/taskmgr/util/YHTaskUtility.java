package yh.rad.taskmgr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import yh.core.data.YHSysOperator;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.rad.taskmgr.data.YHRadUser;
import yh.rad.taskmgr.data.YHTask;

public class YHTaskUtility {
  /**
   * 加载任务列表
   * @param filePath
   * @return 任务列表对象
   * @throws Exception
   */
  public static List loadTaskList(String filePath) throws Exception {
    
    List<String> lineList = new ArrayList<String>();
    YHFileUtility.loadLine2Array(filePath, lineList);
    List<YHTask> taskList = new ArrayList<YHTask>();
    for (String lineStr : lineList) {
      if (lineStr.trim().length() < 1) {
        continue;
      }
      taskList.add((YHTask)YHFOM.json2Obj(lineStr.trim(), YHTask.class));
    }
    return taskList;
  }
  /**
   * 加载任务
   * @param filePath
   * @return 任务
   * @throws Exception
   */
  public static YHTask loadTask(String filePath, String taskPath) throws Exception {
    if (taskPath == null) {
      return null;
    }
    List<YHTask> taskList = loadTaskList(filePath);
    for (YHTask task : taskList) {
      if (task.getTaskPath().equals(taskPath)) {
        return task;
      }
    }
    return null;
  }
  /**
   * 保存任务列表
   * @param taskList
   * @throws Exception
   */
  public static void save(String filePath, List<YHTask> taskList) throws Exception {
    List<String> lineList = new ArrayList<String>();
    for (YHTask task : taskList) {
      lineList.add(YHFOM.toJson(task).toString());
    }
    YHFileUtility.storeArray2Line(filePath, lineList);
  }
  /**
   * 保存
   * @param filePath
   * @param task
   * @throws Exception
   */
  public static void save(String filePath, YHTask task) throws Exception {
    List<YHTask> taskList = loadTaskList(filePath);
    YHTask oldTask = null;
    int index = 0;
    for (int i = 0; i < taskList.size(); i++) {
      YHTask taskInFile = taskList.get(i);
      if (taskInFile.getTaskPath().equals(task.getTaskPath())) {
        oldTask = taskInFile;
        break;
      }
      index++;
    }
    if (oldTask == null) {
      taskList.add(task);
    }else {
      taskList.set(index, task);
    }
    save(filePath, taskList);
  }
  
  /**
   * 删除任务
   * @param filePath
   * @param taskPath
   * @throws Exception
   */
  public static void remove(String filePath, String taskPath) throws Exception {
    List<YHTask> taskList = loadTaskList(filePath);
    boolean deleted = false;
    for (int i = 0; i < taskList.size(); i++) {
      YHTask taskInFile = taskList.get(i);
      if (taskInFile.getTaskPath().equals(taskPath)) {
        taskList.remove(i);
        deleted = true;
        break;
      }
    }
    if (deleted) {
      save(filePath, taskList);
    }
  }
  
  /**
   * 加载用户
   * @param filePath
   * @return
   * @throws Exception
   */
  public static List<YHRadUser> loadUserList(String filePath) throws Exception {
    List<YHRadUser> userList = new ArrayList();
    
    List<String> lineList = new ArrayList<String>();
    YHFileUtility.loadLine2Array(filePath, lineList);

    for (String lineStr : lineList) {
      if (lineStr.trim().length() < 1) {
        continue;
      }
      userList.add((YHRadUser)YHFOM.json2Obj(lineStr.trim(), YHRadUser.class));
    }
    return userList;
  }
  
  /**
   * 验证是否有效的用户
   * @param userList
   * @param name
   * @param pass
   * @return
   * @throws Exception
   */
  public static YHSysOperator getUser(List<YHRadUser> userList,
      String name,
      String pass) throws Exception  {
    
    //没有配置用户列表，不需要进行用户验证
    if (userList == null || userList.size() < 1) {
      throw new Exception("没有配置用户列表！");
    }
    if (name == null) {
      throw new Exception("没有传递用户名！");
    }
    if (pass == null) {
      pass = "";
    }
    for (int i = 0; i < userList.size(); i++) {
      YHRadUser user = userList.get(i);
      if (!user.getName().equals(name)) {
        continue;
      }
      if (!user.getPass().equals(pass)) {
        throw new Exception("无效的密码！");
      }
      YHSysOperator opt = new YHSysOperator();
      opt.setName(user.getName());
      opt.setFullName(user.getFullName());
      return opt;
    }
    return null;
  }
  
  /**
   * 取得模块的数量
   * @param moduleDir
   * @return
   */
  public static int getModuleCnt(String parentDir) {
    int rtCnt = 0;
    File parentFile = new File(parentDir);
    if (!parentFile.exists() || !parentFile.isDirectory()) {
      return rtCnt;
    }
    String[] fileArray = parentFile.list();
    for (int i = 0; i < fileArray.length; i++) {
      String tmpName = fileArray[i];
      File tmpFile = new File(parentDir + "\\" + tmpName);
      if (!tmpFile.isDirectory()) {
        continue;
      }
      tmpFile = new File(parentDir + "\\" + tmpName + "\\info.text");
      if (!tmpFile.exists()) {
        continue;        
      }
      rtCnt++;
    }
    return rtCnt;
  }
}
