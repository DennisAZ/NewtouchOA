package oa.spring.db.dataSet;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;



public class CommonDataSet implements DataSet, Serializable {
    private int row;
    private int rowCount;
    private int endRow;
    private int colCount;
    private List dataList;
    private String[] names;

    public CommonDataSet(ResultSet rs) throws SQLException {
        row = 0;
        rowCount = 0;
        colCount = 0;
        dataList = new ArrayList();
        read(rs);
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public void first() {
        row = 0;
        return;
    }

    public void last() {
        row = endRow;
        return;
    }

    public boolean previous() {
        if (row > 0) {
            row--;
            return true;
        } else {
            return false;
        }
    }

    public String get(int i) {
        Object ob = dataList.get(row - 1);
        return ((String[]) ob)[i];
    }

    public String get(String name) {
        name = name.toUpperCase();
        int j = -1;
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) {
                j = i;
                break;
            }
        }
        if (j == -1) {
            throw new NullPointerException("字段名：" + name);
        	//return null;
        }
        Object ob = dataList.get(row - 1);
        return ((String[]) ob)[j];
    }

    public String get(String name, String defaultValue) {
        String value = get(name);
        if (value == null || value.equals(""))
            value = defaultValue;
        return value;
    }

    public boolean next() {
        if (row < endRow) {
            row++;
            return true;
        } else {
            return false;
        }
    }
    

    

    /**
     * 
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private void read(ResultSet rs) throws SQLException {
        dataList = new ArrayList();
        ResultSetMetaData rsmd = rs.getMetaData();
        String tbn=rsmd.getTableName(1);
//        System.out.println(rsmd.getTableName(2));
        colCount = rsmd.getColumnCount();
      

        while (rs.next()) {
            String[] values = new String[colCount];
            for (int i = 0; i < colCount; i++) {
//            	  System.out.println("********"+rsmd.getColumnName(i+1));
            	//System.out.println("*************"+i);
        		int type = rsmd.getColumnType(i+1);
        		//System.out.println("******************"+type);
        		//System.out.println(rsmd.getColumnName(i + 1).toUpperCase());
        		 Object obj =null;
                 if(type==91){
//                	 try{
                	 obj = rs.getDate(i+1);
                	 if(obj==null || obj.toString().equalsIgnoreCase(""))
                		 obj="0000-00-00 00:00:00.0";
                			
//                	 }catch(SQLException e){
//                		 obj=null; 
//                	 }
                 }else if(type==93){
//                	 try{
                    	 obj = rs.getTimestamp(i+1);
                    	 if(obj==null || obj.toString().equalsIgnoreCase(""))
                    		 obj="0000-00-00 00:00:00.0";
//                    	 }catch(SQLException e){
//                    		 obj=null; 
//                    	 }
                 }
                 else{
                   obj = rs.getObject(i+1);
//                   if(rsmd.getColumnName(i+1).equalsIgnoreCase("COMPRESS_CONTENT")){
//                	   System.out.println("**********"+type+"*********");  
//                   }
//                   if(obj.getClass().equalsIgnoreCase("[B")){
//                	   System.out.println("**********"+rsmd.getColumnName(i+1));
//                   }
                 }
                
                if (obj == null || "".equals(obj)) {
                    values[i] = "";
                } else {
                    if (obj instanceof BigDecimal) {
                        BigDecimal bigdecimal = (BigDecimal) obj;
                        if (rsmd.getScale(i+1) == 0) {
                            if (bigdecimal.toString().length() < 3) {
                                obj = new Byte(bigdecimal.byteValue());
                            } else if (bigdecimal.toString().length() < 5) {
                                obj = new Short(bigdecimal.shortValue());
                            } else if (bigdecimal.toString().length() < 10) {
                                obj = new Integer(bigdecimal.intValue());
                            } else if (bigdecimal.toString().length() < 19) {
                                obj = new Long(bigdecimal.longValue());
                            }
                            values[i] = obj.toString();
                        }else
                        	values[i] = obj.toString();
                    }else if(obj instanceof java.sql.Clob){
                    	java.sql.Clob tmpContent =(java.sql.Clob) obj;
                    	obj = new String(tmpContent.getSubString(1, (int) tmpContent.length()));
                    	 values[i] = obj.toString();
//                    	 System.out.println("**********8"+rsmd.getColumnName(i+1));
                    	 
                    	
                    }else if(type == -4){// mysql blob����
//                   	 System.out.println("**********9"+rsmd.getColumnName(i+1));
                    	 values[i] = obj.toString();
                    }
                    else if(obj instanceof String) {
                    	obj = ((String)obj).replaceAll("\r\n", "<br>");
                    	 values[i] = obj.toString();
                    }
                    else
                    values[i] = obj.toString();
                }
            }
            dataList.add(values);
        }
        names = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            names[i] = rsmd.getColumnName(i + 1).toUpperCase();
        }

        rowCount = dataList.size();
        endRow = rowCount;
    }

    /**
     * ����ݿ���ȡ�õĽ��ת����list
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private void read(ResultSet rs,long beginRow) throws SQLException {
        dataList = new ArrayList();
        ResultSetMetaData rsmd = rs.getMetaData();
        colCount = rsmd.getColumnCount();
 
        while (rs.next()) {
            String[] values = new String[colCount];
            for (int i = 0; i < colCount; i++) {
            	//System.out.println("*************"+i);
        		int type = rsmd.getColumnType(i+1);
        		//System.out.println("******************"+type);
        		//System.out.println(rsmd.getColumnName(i + 1).toUpperCase());
        		 Object obj =null;
                 if(type==91){
//                	 try{
                	 obj = rs.getDate(i+1);
//                	 }catch(SQLException e){
//                		 obj=null; 
//                	 }
                 }else if(type==93){
//                	 try{
                    	 obj = rs.getTimestamp(i+1);
//                    	 }catch(SQLException e){
//                    		 obj=null; 
//                    	 }
                 }
                 else{
                   obj = rs.getObject(i+1);
                 }
                if (obj == null || "".equals(obj)) {
                    values[i] = "";
                } else {
                    if (obj instanceof BigDecimal) {
                        BigDecimal bigdecimal = (BigDecimal) obj;
                        if (rsmd.getScale(i+1) == 0) {
                            if (bigdecimal.toString().length() < 3) {
                                obj = new Byte(bigdecimal.byteValue());
                            } else if (bigdecimal.toString().length() < 5) {
                                obj = new Short(bigdecimal.shortValue());
                            } else if (bigdecimal.toString().length() < 10) {
                                obj = new Integer(bigdecimal.intValue());
                            } else if (bigdecimal.toString().length() < 19) {
                                obj = new Long(bigdecimal.longValue());
                            }
                        }
                    }else if(obj instanceof java.sql.Clob){
                    	java.sql.Clob tmpContent =(java.sql.Clob) obj;
                    	obj = new String(tmpContent.getSubString(1, (int) tmpContent.length()).replaceAll("\r\n", "<br>"));
                    	
                    }else if(obj instanceof String) {
                    	obj = ((String)obj).replaceAll("\r\n", "<br>");
                    }
                    values[i] = obj.toString();
                }

            }
            dataList.add(values);
        }
        names = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            names[i] = rsmd.getColumnName(i + 1).toUpperCase();
        }

        rowCount = dataList.size();
        endRow = rowCount;
    }
    private String null2Str(Object obj) {
        if (obj == null || "".equals(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public void setCurrentRow(int i) {
        if (i > rowCount) {
           
            row = rowCount;
        } else {
            row = i;
        }
    }

    public int getCurrentRow() {
        return row;
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setEndRow(int i) {
        if (i > rowCount) {
           
            endRow = rowCount;
        } else {
            endRow = i;
        }
    }

    public int getEndRow() {
        return endRow;
    }

	@Override
	public String getName(int i) {
		// TODO Auto-generated method stub
		return names[i];
	}

}
