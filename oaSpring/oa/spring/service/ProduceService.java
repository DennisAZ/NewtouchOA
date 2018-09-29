package oa.spring.service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import oa.spring.dao.batis.ProduceMapper;
import oa.spring.util.StaticData;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class ProduceService {
	private ProduceMapper produceMapper;

	public ProduceMapper getProduceMapper() {
		return produceMapper;
	}

	public void setProduceMapper(ProduceMapper produceMapper) {
		this.produceMapper = produceMapper;
	}

	public String yhPageDataJson(Connection dbConn,Map map,String sql){
		try {
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String,Object>> getPlanCodeAndInsertBom(String planId,String type,String userId,String userName){
		//判断当前计划id在数据库中有没有bom清单，没有则添加，有就直接返回
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.put("planId", planId);
		paraMap.put("bom_type", type);
		List<Map<String,Object>> bomInfoList = produceMapper.getBOMInfoList(paraMap);
		if(bomInfoList == null || bomInfoList.size() ==0){
			//首先根据生产计划id生成一个默认的生产领料BOM清单
			//erp_produce_plan_product表中对于生产计划id的产品及需求数量list
			List<Map<String,Object>> ppnList = produceMapper.getPlanProNumberList(planId);
			Map<String,Object> newMap = new HashMap<String,Object>();
			if(ppnList != null && ppnList.size() > 0){
				if("1".equals(type)){
					for(int i=0;i<ppnList.size();i++){
						Map<String,Object> m = ppnList.get(i);
						String crafts_id = m.get("crafts_id").toString();//产品的工艺
						double n = ((Double)m.get("number")).doubleValue();//需要生产的数量
						
						List<Map<String,Object>> list = produceMapper.getBOMList(crafts_id);
						if(list != null && list.size() > 0){
							for(int j=0;j<list.size();j++){
								Map<String,Object> map = list.get(j);
								String pro_id = map.get("pro_id").toString();
								double number = ((Double)map.get("number")).doubleValue()*n;//这里还要乘以数量
								if(newMap.get(pro_id) == null){
									newMap.put(pro_id, number);
								}else{
									double number1 = ((Double)newMap.get(pro_id)).doubleValue();
									newMap.put(pro_id, number+number1);
								}
							}
						}
					}
				}
				else{
					for(int i=0;i<ppnList.size();i++){
						Map<String,Object> m = ppnList.get(i);
						String crafts_id = m.get("crafts_id").toString();//产品的工艺
						List<Map<String,Object>> list = produceMapper.getBOMList(crafts_id);
						if(list != null && list.size() > 0){
							for(int j=0;j<list.size();j++){
								Map<String,Object> map = list.get(j);
								String pro_id = map.get("pro_id").toString();
								newMap.put(pro_id, 0);
							}
						}
					}
				}
					//构建erp_produce_bom表数据
					String create_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					String bomId = UUID.randomUUID().toString();
					Map<String,Object> bomMap = new HashMap<String,Object>();
					bomMap.put("id", bomId);
					bomMap.put("code", "");
					bomMap.put("bom_type", type);//1:领料,2:退料
					bomMap.put("pp_id", planId);
					bomMap.put("status", "");
					bomMap.put("person_id", userId);
					bomMap.put("person", userName);
					bomMap.put("create_time", create_time);
					bomMap.put("remark", "");
					
					Map<String,Object> planCodeMap = produceMapper.getPlanCode(planId);
					
					//构建erp_produce_bom_product表数据
					List<Map<String,Object>> bomProList = new ArrayList<Map<String,Object>>();
					Iterator<Entry<String,Object>> it = newMap.entrySet().iterator();
					while(it.hasNext()){
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("plan_code", planCodeMap.get("code"));
						map.put("bom_id", bomId);
						Entry<String,Object> e = it.next();
						map.put("pro_id", e.getKey());
						Map<String,Object> proInfoMap = produceMapper.getProductInfo(e.getKey());
						map.put("pro_code", proInfoMap.get("pro_code"));
						map.put("pro_name", proInfoMap.get("pro_name"));
						map.put("pro_type", proInfoMap.get("pro_type"));
						map.put("unit_name", proInfoMap.get("unit_name"));
						map.put("number", e.getValue());
						bomProList.add(map);
					}
					produceMapper.addBOM(bomMap);
					produceMapper.addBOMPros(bomProList);
					return bomProList;
				}
		}
		return bomInfoList;
	}
	public List<Map<String,Object>> getWHExamDetial(String planId,String userId,String userName){
		//判断当前计划id在数据库中有没有入库请检数据，没有则添加，有就直接返回
		List<Map<String,Object>> examInfoList = produceMapper.getExamInfoList(planId);
		if(examInfoList == null || examInfoList.size() ==0){
			//首先根据生产计划id生成一个默认的入库请检数据
			
			//构建erp_produce_exam表数据
			String create_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String examId = UUID.randomUUID().toString();
			Map<String,Object> examMap = new HashMap<String,Object>();
			examMap.put("id", examId);
			examMap.put("code", "");
			examMap.put("pp_id", planId);
			examMap.put("status", "");
			examMap.put("person_id", userId);
			examMap.put("person", userName);
			examMap.put("create_time", create_time);
			examMap.put("remark", "");
			
			//erp_produce_plan_product表中对于生产计划id的产品及需求数量list
			List<Map<String,Object>> ppnList = produceMapper.getPlanProNumberList(planId);
			List<Map<String,Object>> examProList = new ArrayList<Map<String,Object>>();
			if(ppnList != null && ppnList.size() > 0){
				for(int i=0;i<ppnList.size();i++){
					Map<String,Object> m = ppnList.get(i);
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("exam_id", examId);
					map.put("pro_id", m.get("pro_id").toString());
					Map<String,Object> proInfoMap = produceMapper.getProductInfo(m.get("pro_id").toString());
					map.put("pro_code", proInfoMap.get("pro_code"));
					map.put("pro_name", proInfoMap.get("pro_name"));
					map.put("pro_type", proInfoMap.get("pro_type"));
					map.put("unit_name", proInfoMap.get("unit_name"));
					double number = ((Double)m.get("number")).doubleValue();
					map.put("number", number);
					map.put("qualified_number", number);
					map.put("exam_way", "抽检");
					examProList.add(map);
					}
				}
				
				produceMapper.addExam(examMap);
				produceMapper.addExamPros(examProList);
				return examProList;
			}
		return examInfoList;
	}
	
	public List<Map<String,Object>> getBOMDetial(String bom_id){
		Map<String,String> map = new HashMap<String,String>();
		map.put("pod_id", bom_id);
		map.put("flag", StaticData.PRODUCE_PICK);
		return produceMapper.getBOMDetial(map);
	}
	public List<Map<String,Object>> getBOMDetial1(String bom_id){
		Map<String,String> map = new HashMap<String,String>();
		map.put("pod_id", bom_id);
		map.put("flag", StaticData.PRODUCE_RETURN);
		return produceMapper.getBOMDetial(map);
	}
	public List<Map<String,Object>> getExamProDetial(String exam_id){
		Map<String,String> map = new HashMap<String,String>();
		map.put("pod_id", exam_id);
		map.put("flag", StaticData.CHECK_IN);
		return produceMapper.getExamProDetial(map);
	}
	public List<Map<String,Object>> getWareHouse(){
		return produceMapper.getWareHouse();
	}
	public Map<String,Object> getCrafts(String craftsId){
		return produceMapper.getCrafts(craftsId);
	}
	public Map<String,Object> getType(String typeId){
		return produceMapper.getType(typeId);
	}
	public Map<String,Object> getDrawingType(String drawingTypeId){
		return produceMapper.getDrawingType(drawingTypeId);
	}
	public Map<String,Object> selectProductByProId(String proId){
		return produceMapper.selectProductByProId(proId);
	}
	public Map<String,Object> getDrawingTypeByDtId(String dt_id){
		return produceMapper.getDrawingTypeByDtId(dt_id);
	}
	public Map<String,Object> getProduct(String proId){
		return produceMapper.getProduct(proId);
	}
	public Map<String,Object> getProcessByIds(String id){
		return produceMapper.getProcessByIds(id);
	}
	public Map<String,Object> getNotifyById(String notifyId){
		return produceMapper.getNotifyById(notifyId);
	}
	public Map<String,Object> getProcessIndex(Map map){
		return produceMapper.getProcessIndex(map);
	}
	public Map<String,Object> getPlan(String planId){
		return produceMapper.getPlan(planId);
	}
	public List<Map<String,Object>> getPlanPro(String planId){
		return produceMapper.getPlanPro(planId);
	}
	public List getMachineType(){
		return produceMapper.getMachineType();
	}
	public List getProcessById(String craftsId){
		return produceMapper.getProcessById(craftsId);
	}
	public List getNotifyProById(String notifyId){
		return produceMapper.getNotifyProById(notifyId);
	}
	public List getProcessPro(String process_id){
		return produceMapper.getProcessPro(process_id);
	}
	
	
	public void addCrafts(Map<String,Object> map){
		produceMapper.addCrafts(map);
	}
	public void addProcess(Map<String,Object> map,List list){
		produceMapper.addProcess(map);
		produceMapper.addProcessConsume(list);
	}
	public void addNotify(Map<String,Object> map,List<Map<String,Object>> list){
		produceMapper.addNotify(map);
		produceMapper.addNotifyPro(list);
	}
	public void addPlan(Map<String,Object> map,List list){
		produceMapper.addPlan(map);
		produceMapper.addPlanPro(list);
	}
	public void updatePlan(Map<String,Object> map,List list,String planId){
		produceMapper.updatePlan(map);
		produceMapper.deletePlanPro(planId);
		produceMapper.addPlanPro(list);
	}
	public void updateBOM(Map<String,Object> map,List list,String bom_id){
		produceMapper.updateBOMInfo(map);
		produceMapper.deleteBOMPro(bom_id);
		produceMapper.addBOMPros(list);
	}
	public void updateBOMDetial(List<Map<String,Object>> list,String bom_id,String flag){
		Map<String,String> map = new HashMap<String,String>();
		map.put("pod_id", bom_id);
		map.put("flag", flag);
		produceMapper.deleteDBLog(map);
		produceMapper.addDBLog(list);
	}
	public void updateExamDetial(List<Map<String,Object>> examProList,List<Map<String,Object>> dbLogList,String exam_id,Map<String, Object> examMap){
		Map<String,String> map = new HashMap<String,String>();
		map.put("pod_id", exam_id);
		map.put("flag", StaticData.CHECK_IN);
		produceMapper.deleteDBLog(map);
		produceMapper.addDBLog1(dbLogList);
		for(int i=0;i < examProList.size();i++){
			produceMapper.updateExamDetial(examProList.get(i));
		}
		produceMapper.updateExam(examMap);
	}
	public void updateProcess(Map<String,Object> map,List list,String processId){
		produceMapper.updateProcess(map);
		produceMapper.deleteProcessConsume(processId);
		produceMapper.addProcessConsume(list);
	}
	public void updateNotify(Map<String,Object> map,List list,String notifyId){
		produceMapper.updateNotify(map);
		produceMapper.deleteNoPro(notifyId);
		produceMapper.addNotifyPro(list);
	}
	
	public void updateCrafts(Map map){
		produceMapper.updateCrafts(map);
	}
	public void updateType(Map map){
		produceMapper.updateType(map);
	}
	public void updateDrawingType(Map map){
		produceMapper.updateDrawingType(map);
	}
	public void updateDrawing(Map map){
		produceMapper.updateDrawing(map);
	}
	public void addType(Map<String,Object> map){
		produceMapper.addType(map);
	}
	
	public void addDrawingType(Map<String,Object> map){
		produceMapper.addDrawingType(map);
	}
	
	public Map<String,Object> getDrawing(String drawingId){
		return produceMapper.getDrawing(drawingId);
	}
	public void deleteCrafts(String craftsId){
		produceMapper.deleteCrafts(craftsId);
		List<Map<String,Object>> list = produceMapper.getProcessById(craftsId);
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				deleteProcess(list.get(i).get("id").toString());
			}
		}
	}
	public void deleteProcess(String processId){
		produceMapper.deleteProcess(processId);
		produceMapper.deleteProcessConsume(processId);
	}
	public void deleteNotify(String notifyId){
		produceMapper.deleteNotify(notifyId);
		produceMapper.deleteNoPro(notifyId);
	}
	public void deleteType(String typeId){
		produceMapper.deleteType(typeId);
	}
	public void deleteDrawingType(String drawingTypeId){
		produceMapper.deleteDrawingType(drawingTypeId);
	}
	public void deleteDrawing(String drawingId){
		produceMapper.deleteDrawing(drawingId);
	}
	public void deletePlan(String planId){
		produceMapper.deletePlan(planId);
		produceMapper.deletePlanPro(planId);
	}
	
	public void changeExamWay(Map<String,String> map){
		produceMapper.changeExamWay(map);
	}
	
	public void addDrawing(Map map){
		produceMapper.addDrawing(map);
	}
	
}
