package com.psit.struts.util;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.psit.struts.entity.OrdStatistic;
import com.psit.struts.entity.StaTable;
import com.psit.struts.entity.StatIsCusSuc;

/**
 * 
 * 生成报表XML <br>
 *
 * create_date: Aug 20, 2010,3:01:49 PM<br>
 * @author zjr
 */
public class GetXml {
	//颜色数组
	private final static String[] colors=new String[]{
			"AFD8F8","F6BD0F","99cc33","fe5614","3399cc",
			"9933cc","fc8433","77a6e5","fcf258","74ec33",
			"d98f0e","08247f","e11ada","fdb79f","2cc818",
			"bbd6f8","f95458","94edc0","2741af","fdd23c",
			"9414a7","63abfc","e6771d","41e039","1e71a5",
			"fdf12b","f772d3","4a42f6","e7511c","54cfae",
			"f8c028","60a4f9","b7056e","cad0d8","494eb7",
			"f2575f","228408","f9e36c","54da73","1e60ab",
			"f6f44c","c85204","92c0fe","8230c2","fb48c2",
			"9be639","f65333","38597b","2daf20","fba706",
			"ac1010","5da3f9","5b1385","f2dc20","fc4d30",
			"5ee2d1","107506","fce629","2969fc","fc6e29",
			"24366e","fa4bd7","8afd2d","2dbbfd","85530c",
			"fd4967","24566d","3cc816","fcb713","135afc",
		};
	
	/**
	 * 使用统计封装对象生成XML <br>
	 * create_date: Feb 21, 2011,2:22:35 PM <br>
	 * @param staTab StaTable对象
	 * @param graphName 报表名称
	 * @param xName 报表X轴名称
	 * @param yName 报表y轴名称（如有双Y轴，以逗号分隔）
	 * @param hasRSum 是否包含右侧合计
	 * @return String 组合完成的XML字符串
	 */
	public static String getXml(StaTable staTab,String graphName, String xName, String yName,boolean hasRSum){
		Iterator<String[]> it = staTab.getDataList().iterator();
		int sumCol = hasRSum?1:0;
		String[][] datas = new String[staTab.getDataList().size()-1][staTab.getTopList().length-2-sumCol];
		//第一个-1去底部合计行,-2去除员工ID和员工号,-sumCol去除右侧合计列
		int i=0;
		while(i<datas.length){
//			LinkedHashMap hashMap = it.next();
			String[] dataArr = it.next();
			for(int j=0; j<datas[i].length;j++){
				datas[i][j] = dataArr[j+2];
			}
			i++;
		}
		String[] serNames = new String[staTab.getTopList().length-3-sumCol];
		String[] topArr = staTab.getTopList();
		for(int k = 0; k<serNames.length; k++){
			serNames[k] = topArr[k+3];
		}
		return getXml(datas, serNames, graphName, xName, yName);
	}
	
	/**
	 * 生成XML <br>
	 * create_date: Jan 6, 2011,11:38:50 AM <br>
	 * @param datas 报表内数据，由list转换，第一维的数组元素为list内的对象，第二维为对象内的属性（第一个属性为报表最左侧的表头）
	 * @param serNames 系列名数组（即list内对象除第一列的属性名，如有双Y轴，以逗号后的P表示属性属于主Y轴，S表示属于次Y轴）
	 * @param graphName 报表名称
	 * @param xName 报表X轴名称
	 * @param yName 报表y轴名称（如有双Y轴，以逗号分隔）
	 * @return String 组合完成的XML字符串
	 */
	public static String getXml(String[][]datas,String[] serNames, String graphName, String xName, String yName) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		int colorIndex = 0;
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		StringBuffer data = new StringBuffer("");
		StringBuffer[] values = new StringBuffer[serNames.length];
		for(int n=0; n<values.length; n++){
			values[n] = new StringBuffer("");
		}
		String dateStyle = "";
		if (datas != null && datas.length > 0) {
			try {
				data.append("<categories>");
				for (int i = 0; i < datas.length; i++) {
					data.append("<category label='" + (datas[i][0]==null?"无":datas[i][0]) + "'/>");
					for(int j=0; j < (datas[i].length-1); j++){
						values[j].append("<set value='" + datas[i][j+1] + "'/>");
					}
				}
				data.append("</categories>");
				for(int k=0; k< serNames.length ; k++){
					String serName = "";
					if (serNames[k].indexOf(",") != -1) {
						serName = "seriesName ='" + serNames[k].split(",")[0]
								+ "' parentYAxis='" + serNames[k].split(",")[1] + "'";
					} else { 
						serName = "seriesName ='" + serNames[k] + "'";
					}
					data.append("<dataset "+ serName +" color='"+colors[colorIndex]+"' showValues='0'>");
					data.append(values[k]);
					data.append("</dataset>");
					if(colorIndex < colors.length){
						colorIndex ++;
					}
					else{
						colorIndex = 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ graphName
				+ "' baseFont='宋体' baseFontSize='12' xAxisName='"
				+ xName + "' "
				+ ((yName.indexOf(",")!=-1)? 
						("PYAxisName= '" + yName.split(",")[0] + "' SYAxisName='"+ yName.split(",")[1]):
						("yAxisName='")+ yName)
				+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='c1c2c3' bgAlpha='30' chartRightMargin='30'  plotGradientColor='e8f0a5' "
				+ dateStyle
				+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
				+ data + "</graph>";
		return dataXML;
	}
	
	/**
	 * 生成XML <br>
	 * create_date: Aug 20, 2010,3:25:57 PM <br>
	 * @param list 将要生成图表的结果集
	 * @param graphName 图表标题
	 * @param xName X轴名称
	 * @param yName Y轴名称
	 * @param type 图表类型(1:2D柱图 2:3D柱图 3:2D条形图 4:折线图 5:2D面积图 6:2D饼图 7:3D饼图 8:圆环图)
	 * @return String 返回生成XML<br>
	 */
	public static String getXml(List list, String graphName, String xName,
			String yName, String type) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		String data = "";
		String color = "";
		// type 1:2D柱图 2:3D柱图 3:2D条形图 4:折线图 5:2D面积图 6:2D饼图 7:3D饼图 8:圆环图
		String dateStyle = "";
		String name = "";
		String value = "";
		if (list != null && list.size() > 0) {
			int j = 0;
			for (int i = 0; i < list.size(); i++) {
				if (i == 99)
					j = 0;
				OrdStatistic sta = (OrdStatistic) list.get(i);
				String staType = sta.getType();
				if (type != null && type.equals("5"))
					color = "e7e8e9";// 区域图的颜色
				else if (type != null && type.equals("4"))
					color = "fe5614";// 折线图的颜色
				// dataType 1:String&Long 2:String&Double
				else {
					color = colors[j];
				}
				if (staType.equals("1")) {//类别数量
					if (sta.getId() == null || sta.getId().equals("")) {
						name = "未选择";
					} else {
						name = sta.getName();
					}
					value = sta.getLnum().toString();
				} else if (staType.equals("2")) {//金额
					if (sta.getId() == null || sta.getId().equals("")) {
						name = "未选择";
					} else {
						name = sta.getName();
					}
					value = sta.getDnum().toString();
				} else if (staType.equals("3")) {
					name = sta.getName();
					value = sta.getDnum().toString();
				} else if (staType.equals("4")) {
					if (sta.getName() == null || sta.getName().equals("")) {
						name = "未选择";
					} else {
						name = sta.getName();
					}
					value = sta.getLnum().toString();
				}
				data += "<set name='" + name + "' value='" + value
						+ "' color='" + color + "' alpha='85'/>";
				j++;
			}
		}
		if (type != null && type.equals("5")) {
			dateStyle = "areaAlpha='60' areaBorderThickness='1' areaBorderColor='7B9D9D'";
		} else if (type != null && type.equals("15"))
			dateStyle = "isSliced='1' slicingDistance='4'";
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ graphName
				+ "' baseFont='宋体' chartRightMargin='30' baseFontSize='12' xAxisName='"
				+ xName
				+ "' yAxisName='"
				+ yName
				+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='c1c2c3' bgAlpha='30'  plotGradientColor='e8f0a5' "
				+ dateStyle
				+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
				+ data + "</graph>";
		return dataXML;

	}
	/**
	 * 
	 * 工作台销售业绩XML <br>
	 * create_date: Aug 20, 2010,4:09:16 PM <br>
	 * @param list 结果集
	 * @param graphName 图表标题
	 * @param xName X轴名称
	 * @param yName Y轴名称
	 * @param type 图表类型(1:2D柱图 2:3D柱图 3:2D条形图 4:折线图 5:2D面积图 6:2D饼图 7:3D饼图 8:圆环图)
	 * @return String 返回生成XML<br>
	 */
	public static String getXml2(List list, String graphName, String xName,
			String yName, String type) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		String data = "";
		String color = "";
		// type 1:2D柱图 2:3D柱图 3:2D条形图 4:折线图 5:2D面积图 6:2D饼图 7:3D饼图 8:圆环图
		String dateStyle = "";
		String name = "";
		Double value = 0.0;

		int j = 0;
		java.util.Date date = new Date();
		for (int k = 5; k >= 0; k--) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			String staDate = dateFormat.format(OperateDate.getMonth(date, -k));
			value = 0.0;
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {

					if (i == 99)
						j = 0;
					OrdStatistic sta = (OrdStatistic) list.get(i);
					if (type != null && type.equals("5"))
						color = "e7e8e9";// 区域图的颜色
					else if (type != null && type.equals("4"))
						color = "fe5614";// 折线图的颜色
					else {
						color = colors[j];
					}
					if (sta.getName().equals(staDate)) {
						value = sta.getDnum();
					}
				}
			}
			data += "<set name='" + staDate + "' value='" + value + "' color='"
					+ color + "' alpha='85'/>";
			j++;
		}
		if (type != null && type.equals("5")) {
			dateStyle = "areaAlpha='60' areaBorderThickness='1' areaBorderColor='7B9D9D'";
		} else if (type != null && type.equals("15"))
			dateStyle = "isSliced='1' slicingDistance='4'";
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ graphName
				+ "' baseFont='宋体' chartRightMargin='30' baseFontSize='12' xAxisName='"
				+ xName
				+ "' yAxisName='"
				+ yName
				+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='ffffff' bgAlpha='30' plotGradientColor='000000' "
				+ dateStyle
				+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0' showBorder='0'>"
				+ data + "</graph>";
		return dataXML;

	}
	/**
	 * 
	 * 生成XML（多系列） <br>
	 * create_date: Aug 20, 2010,4:28:14 PM <br>
	 * @param rs 结果集
	 * @param graphName 图表标题
	 * @param xName X轴名称
	 * @param yName Y轴名称
	 * @return String 返回生成XML<br>
	 */
	
	public static String getXml(ResultSet rs, String graphName, String xName,
			String yName) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		StringBuffer data = new StringBuffer("");
		// type 1:2D柱图 2:3D柱图 3:折线图 4:面积图 5:2D条形图 6:3D条形图
		String dateStyle = "";
		ResultSetMetaData colList;
		if (rs != null) {
			try {
				colList = rs.getMetaData();
				data.append("<categories>");
				for (int i = 3; i <= colList.getColumnCount() - 1; i++) {
					// 获得指定列的列名
					String columnName = colList.getColumnName(i);
					data.append("<category label='" + columnName + "'/>");
				}
				data.append("</categories>");
				int j = 0;
				while (rs.next()) {
					if (j == 99)
						j = 0;
					if (!rs.isLast()) {
						data
								.append("<dataset seriesName='"
										+ rs.getString(2) + "' color='"
										+ colors[j] + "' showValues='0'>");
						for (int i = 3; i <= colList.getColumnCount() - 1; i++) {
							data.append("<set value='" + rs.getString(i)
									+ "'/>");

						}
						data.append("</dataset>");
					}
					j++;
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ graphName
				+ "' baseFont='宋体' baseFontSize='12' xAxisName='"
				+ xName
				+ "' yAxisName='"
				+ yName
				+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='c1c2c3' bgAlpha='30'chartRightMargin='30'  plotGradientColor='e8f0a5' "
				+ dateStyle
				+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
				+ data + "</graph>";
		return dataXML;

	}
	/**
	 * 
	 * 生成XML(潜在已成客户统计) <br>
	 * create_date: Aug 20, 2010,4:34:24 PM <br>
	 * @param list 结果集
	 * @param graphName 图表标题
	 * @param xName X轴名称
	 * @param yName Y轴名称
	 * @return String 返回生成XML<br>
	 */
	public static String getXml(List list, String graphName, String xName,
			String yName) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		StringBuffer data = new StringBuffer("");
		StringBuffer poteData = new StringBuffer("");
		StringBuffer sucData = new StringBuffer("");
		// type 1:2D柱图 2:3D柱图 3:折线图 4:面积图 5:2D条形图 6:3D条形图
		String dateStyle = "";
		if (list != null && list.size() > 0) {
			try {
				data.append("<categories>");
				for (int i = 0; i < list.size(); i++) {
					StatIsCusSuc statIsCus = (StatIsCusSuc) list.get(i);
					poteData.append("<set value='"
							+ statIsCus.getPoteCusCount() + "'/>");// 获得潜在结果集
					sucData.append("<set value='"
							+ statIsCus.getIsSusCusCount() + "'/>");// 获得已成结果集
					data.append("<category label='" + statIsCus.getUserSeName()
							+ "'/>");
				}
				data.append("</categories>");
				data.append("<dataset seriesName='潜在' color='AFD8F8' showValues='0'>");
				data.append(poteData);
				data.append("</dataset>");
				data.append("<dataset seriesName='已成' color='F6BD0F' showValues='0'>");
				data.append(sucData);
				data.append("</dataset>");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String dataXML = utf8BomStr
				+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
				+ graphName
				+ "' baseFont='宋体' baseFontSize='12' xAxisName='"
				+ xName
				+ "' yAxisName='"
				+ yName
				+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='c1c2c3' bgAlpha='30'chartRightMargin='30'  plotGradientColor='e8f0a5' "
				+ dateStyle
				+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
				+ data + "</graph>";
		return dataXML;

	}
	/**
	 * 
	 * 销售目标XML <br>
	 * create_date: Aug 20, 2010,4:37:59 PM <br>
	 * @param list1 结果集1
	 * @param list2 结果集2
	 * @param tarName 目标标识
	 * @param tarYear 年份
	 * @param graphName 图表标题
	 * @param xName X轴名称
	 * @param yName Y轴名称
	 * @return String 返回生成XML<br>
	 */
	public static String getXml(List list1,List list2,String tarName,String tarYear,String graphName, String xName,
			String yName) {
		byte[] utf8Bom = new byte[] { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
		String utf8BomStr = "";
		try {
			utf8BomStr = new String(utf8Bom, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}// 定义BOM标记
		String dateStyle = "";
		StringBuffer data = new StringBuffer("");
		StringBuffer tarData = new StringBuffer("");
		StringBuffer fctData = new StringBuffer("");
		String mon[]=new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
		data.append("<categories>");
			String tarDate="";
			for(int k=0;k<mon.length;k++){
				tarDate=tarYear+"-"+mon[k];
				boolean f=false;
				boolean g=false;
				if(list1!=null&&list1.size()>0){
					for(int i=0;i<list1.size();i++){
						OrdStatistic st=(OrdStatistic) list1.get(i);//目标
						String date=st.getName();
						String num="0";
							if(tarName.equals("3")){
								if(st.getLnum()!=null)
									num=st.getLnum().toString();
							}else
							{
								if(st.getDnum()!=null)
									num=st.getDnum().toString();
							}
						if(date.equals(tarDate)){
							tarData.append("<set value='"+num+ "'/>");// 目标额
							f=true;
							break;
						}
					}
					if(!f){
						tarData.append("<set value='0'/>");// 目标额
					}
				}else{
					tarData.append("<set value='0'/>");// 目标额
				}
				
				if(list2!=null&&list2.size()>0){
					for(int j=0;j<list2.size();j++){
						OrdStatistic sat=(OrdStatistic) list2.get(j);//实际
						String num2="";
						if(tarName.equals("3")){
							num2=sat.getLnum().toString();
						}else
						{
							num2=sat.getDnum().toString();
						}
						if(tarDate.equals(sat.getName())){
							fctData.append("<set value='"+num2+ "'/>");// 实际额
							g=true;
							break;
						}
					}
					if(!g){
						fctData.append("<set value='0'/>");// 实际额
					}
				}else{
					fctData.append("<set value='0'/>");// 实际额
				}
				
			data.append("<category label='" +tarDate+ "'/>");
		}
		data.append("</categories>");
		data.append("<dataset seriesName='目标' color='AFD8F8' showValues='0'>");
		data.append(tarData);
		data.append("</dataset>");
		data.append("<dataset seriesName='完成' color='F6BD0F' showValues='0'>");
		data.append(fctData);
		data.append("</dataset>");
		
		String dataXML = utf8BomStr
		+ "<?xml version='1.0' encoding='UTF-8'?><graph caption='"
		+ graphName
		+ "' baseFont='宋体' baseFontSize='12' xAxisName='"
		+ xName
		+ "' yAxisName='"
		+ yName
		+ "' decimalPrecision='0' baseFontColor='1c254e' useRoundEdges='1' bgColor='c1c2c3' bgAlpha='30'chartRightMargin='30'  plotGradientColor='e8f0a5' "
		+ dateStyle
		+ "  showValues='1' outCnvBaseFont='1c254e' showAlternateHGridColor='1' formatNumberScale='0'>"
		+ data + "</graph>";
		return dataXML;
	}
	
}
