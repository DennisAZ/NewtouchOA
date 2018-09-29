package oa.spring.service;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import oa.spring.base.BaseService;
import oa.spring.base.IService;
import oa.spring.base.TableFieldVO;
import oa.spring.base.VO;

import oa.spring.dao.jdbc.TestDao;
import oa.spring.db.dataSet.DataSet;
	/**
	 * @author lan
	 *
	 */
	public class TestService extends BaseService implements IService{
	private TestDao testDao;
    public TestService(){
    	System.out.println("*************************8testservice");
    }
	public TestDao getTestDao() {
		return testDao;
	}

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}


	public List getTables() throws Exception{
		List tables=this.getTestDao().getTables(null);
//		String sql1="update wiki_ask  set creator='dd2d' where seq_id=17";
//		this.testDao.execute(sql1);
//		String sql2="insert into wiki_ask_answer  (answer_user) values('ddd')";
//
//		this.testDao.execute(sql2);
		return tables;
	}
	public DataSet getTableValue(String sql){
	try {
		DataSet ds=this.getTestDao().getTableValue(sql);
		
//		int rowCount=ds.getRowCount();
//		int colCount=ds.getColCount();
//		while(ds.next()){
//		      for(int i=0;i<colCount;i++)
//	          //System.out.println("fileName="+ds.getName(i)+"   value="+ds.get(i));
//		     // System.out.println("---------------------����----------------------------");
//		      
//		}
		return ds;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return null;
		
	}
	public List getFieldsByTableName(String tableName){
		List list=null;
		try {
			 list=this.getTestDao().getColumnsByTableName(tableName);
			Iterator it=list.iterator();
			while(it.hasNext()){
				TableFieldVO vo=(TableFieldVO)it.next();
//		          System.out.println("fieldName="+it.next().toString());
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			return null;
			
		}
	public DataSet getAllTableValue(VO vo){
		if( vo==null)
		{
			System.out.println("空vo");
			return null;
		}

		

			StringBuffer sb=new StringBuffer();
		      
		    
			 sb.append("select   * from "+vo.getTableName()) ;
			 System.out.println(sb.toString());
			 if(vo.getPerRecords()>0)
			 {
				 try {
					 if(vo.getTotalNum()<0){
						 int num=this.getTestDao().queryCount("select count(*) from "+vo.getTableName());
						 vo.setTotalNum(num);
					 }
					return this.getTestDao().getTableValueByTimes(sb.toString(),vo.getNowTimes(),vo.getPerRecords());
				 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 return null;
			 }
			 else
			 {
			 return this.getTableValue(sb.toString());
			 }
		}

	public DataSet getTableValue(VO vo){
		if( vo==null)
		{
			System.out.println("vo is null");
			return null;
		}

		
		if(0==vo.getFieldName().length)
		{
			System.out.println("vo field is null");
			return null;
		}

			StringBuffer sb=new StringBuffer();
			StringBuffer sb1=new StringBuffer();
		
		      for(int i=0;i<vo.getFieldName().length;i++)
		      {
		    	  if(i>0)
		    	  {
		    		  sb1.append(",");
		    	  }
		    	  sb1.append(vo.getFieldName()[i]);
	          
		     }
		      
			 sb.append("select   "+sb1.toString()+" from "+vo.getTableName()) ;
			 System.out.println(sb.toString());
			 if(vo.getPerRecords()>0)
			 {
				 try {
					 if(vo.getTotalNum()<0){
						 int num=this.getTestDao().queryCount("select count(*) from "+vo.getTableName());
						 vo.setTotalNum(num);
					 }
					return this.getTestDao().getTableValueByTimes(sb.toString(),vo.getNowTimes(),vo.getPerRecords());
				 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
				 return null;
			 }else
			 {
			 return this.getTableValue(sb.toString());
			 }
		}

	}
