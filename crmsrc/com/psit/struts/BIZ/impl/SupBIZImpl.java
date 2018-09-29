package com.psit.struts.BIZ.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

import com.psit.struts.BIZ.SupBIZ;
import com.psit.struts.DAO.ProdInDAO;
import com.psit.struts.DAO.ProdOutDAO;
import com.psit.struts.DAO.ProdStoreDAO;
import com.psit.struts.DAO.PurOrdDAO;
import com.psit.struts.DAO.RPuoProDAO;
import com.psit.struts.DAO.SupInvoiceDAO;
import com.psit.struts.DAO.SupPaidPastDAO;
import com.psit.struts.DAO.SupProdDAO;
import com.psit.struts.DAO.SupplierDAO;
import com.psit.struts.DAO.WhRecDAO;
import com.psit.struts.DAO.impl.ProdStoreDAOImpl;
import com.psit.struts.entity.ERPPurchase;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ProdIn;
import com.psit.struts.entity.ProdOut;
import com.psit.struts.entity.ProdStore;
import com.psit.struts.entity.PurOrd;
import com.psit.struts.entity.ROrdPro;
import com.psit.struts.entity.RPuoPro;
import com.psit.struts.entity.SalPaidPast;
import com.psit.struts.entity.SupInvoice;
import com.psit.struts.entity.SupPaidPast;
import com.psit.struts.entity.SupProd;
import com.psit.struts.entity.Supplier;
import com.psit.struts.entity.WhRec;

/**
 * 锟酵伙拷锟斤拷锟斤拷BIZ实锟斤拷锟斤拷 <br>
 */
public class SupBIZImpl implements SupBIZ {
	SupplierDAO supplierDAO=null;
	PurOrdDAO purOrdDAO=null;
	RPuoProDAO rPuoProDAO=null;
	ProdInDAO prodInDAO = null;
	ProdOutDAO  prodOutDAO= null;
	ProdStoreDAO  prodStoreDAO= null;
	WhRecDAO whRecDAO = null;
	SupProdDAO supProdDAO = null;
	SupPaidPastDAO supPaidPastDAO = null;
	SupInvoiceDAO supInvoiceDAO = null;

	

	/* Supplier */
	public List<Supplier> listSupplier(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return supplierDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listSupplierCount(String[]args){
		return supplierDAO.listCount(args);
	}
	
	public Supplier findSupplierById(String supId){
		if(StringFormat.isEmpty(supId)){
			return null;
		}
		else{
			return supplierDAO.findById(Long.parseLong(supId)); 
		}
		
	}
	public void saveSupplier(Supplier supplier,LimUser curUser) {
		if(supplier.getSupId()==null){
			supplier.setSupCreTime(GetDate.getCurTime());
			supplier.setSupCreMan(curUser.getPerson().getUserName());
			supplierDAO.save(supplier);
		}
		else{
			Supplier oldSup = supplierDAO.findById(supplier.getSupId());
			
			supplier.setSupCreTime(oldSup.getSupCreTime());
			supplier.setSupCreMan(oldSup.getSupCreMan());
			supplier.setSupUpdTime(GetDate.getCurTime());
			supplier.setSupUpdMan(curUser.getPerson().getUserName());
			supplierDAO.merge(supplier);
		}
	}
	public void deleteSupplier(String id) {
		if(!StringFormat.isEmpty(id)){
			supplierDAO.deleteById(id);
		}
	}
	
	/* PurOrd */
	public List<PurOrd> listPurOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return purOrdDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	public List<ERPPurchase> listPurOrdERP(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		return purOrdDAO.listERP(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listPurOrdCount(String[]args){
		return purOrdDAO.listCount(args);
	}
	public int listPurOrdCountERP(String[]args){
		return purOrdDAO.listCountERP(args);
	}
	
	public PurOrd getPurOrdById(String id){
		if(StringFormat.isEmpty(id)){
			return null;
		}
		else{
			return purOrdDAO.findById(Long.parseLong(id));
		}
	}
	public void savePurOrd(PurOrd purOrd, LimUser curUser) {
		if(purOrd.getPuoId() == null){
			purOrd.setPuoCreTime(GetDate.getCurTime());
			purOrd.setPuoCreMan(curUser.getUserSeName());
			purOrdDAO.save(purOrd);
		}else{
			PurOrd old = purOrdDAO.findById(purOrd.getPuoId());
			purOrd.setPuoCreMan(old.getPuoCreMan());
			purOrd.setPuoCreTime(old.getPuoCreTime());
			purOrd.setPuoUpdMan(curUser.getUserSeName());
			purOrd.setPuoUpdTime(GetDate.getCurTime());
			purOrdDAO.merge(purOrd);
		}
	}
	
	public void updatePurOrd(PurOrd purOrd) {
		purOrdDAO.merge(purOrd);
	}
	public void deletePurOrd(String id) {
		if(!StringFormat.isEmpty(id)){
			purOrdDAO.deleteById(id);
		}
	}

	public int listRPuoProCount(String []args){
		return rPuoProDAO.listCount(args);
	}
    
	public List<RPuoPro> listRPuoPro(String[] args, String orderItem,String isDe,
			int currentPage, int pageSize ){
		return rPuoProDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
			
			
	/*prodIn*/
 	public int listProdInCount(String[] args){
 		return prodInDAO.listCount(args);
 	}
	
	public List<ProdIn> listProdIn(String[] args,String orderItem, String isDe,int currentPage, int pageSize){
		return prodInDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	
	public ProdIn getProdInById(String id){
		if(StringFormat.isEmpty(id)){
			return null;
		}else{
			return prodInDAO.findById(Long.parseLong(id));
		}
	}
	public void delProdIn(String id){
		if(!StringFormat.isEmpty(id)){
			//inStore(prodInDAO.findById(Long.parseLong(id)),null,0.0,2);
			prodInDAO.delProdInById(id);
		}
	}
	
	public void cancleProdIn(String id){
		if(!StringFormat.isEmpty(id)){
			ProdIn prodIn = prodInDAO.findById(Long.parseLong(id));
			inStore(prodIn,null,0.0,2);
			prodIn.setPinState("0");
			prodInDAO.merge(prodIn);
		}
	}
	
	public void saveProdIn(ProdIn prodIn,LimUser curUser){
		if(prodIn.getPinId() == null){
			prodIn.setPinCreMan(curUser.getUserSeName());
			prodIn.setPinCreTime(GetDate.getCurTime());
			prodIn.setPinState("1");
			prodInDAO.save(prodIn);
			inStore(prodIn,null,0.0,0);
		}else{
			ProdIn old = prodInDAO.findById(prodIn.getPinId());
			prodIn.setPinState(old.getPinState());
			prodIn.setPinCreMan(old.getPinCreMan());
			prodIn.setPinCreTime(old.getPinCreTime());
			prodIn.setPinUpdMan(curUser.getUserSeName());
			prodIn.setPinUpdTime(GetDate.getCurTime());
			inStore(prodIn,null,old.getPinInNum(),1);
			prodInDAO.merge(prodIn);
		}
		
	}
	
	private void inStore(ProdIn prodIn,ProdOut prodOut,double oldNum,int type){
		if(prodIn!=null){
			double pinNum = (prodIn.getPinInNum()!=null)? prodIn.getPinInNum():0;
			switch(type){
			case 0:break;//锟铰斤拷
			case 1:
				if(oldNum>0){
					pinNum = pinNum - oldNum; 
				}
				break;//锟斤拷锟斤拷
			case 2:
				pinNum = -pinNum;
				break;//删锟斤拷
			}
			if(prodIn.getProdStore()!=null){
				ProdStore prodStore = prodStoreDAO.findById(prodIn.getProdStore().getPstId());
				prodStore.setPstCount(prodStore.getPstCount()+pinNum);
				prodStoreDAO.merge(prodStore);
			}
			saveWhRecIn(prodIn,null,pinNum);
		}
		if(prodOut!=null){
			double outNum = (prodOut.getPouOutNum()!=null)? prodOut.getPouOutNum():0;
			switch(type){
			case 0:break;//锟铰斤拷
			case 1:
				if(oldNum>0){
					outNum = outNum - oldNum; 
				}
				break;//锟斤拷锟斤拷
			case 2:
				outNum = -outNum;
				break;//删锟斤拷
			}
			if(prodOut.getProdStore()!=null){
				ProdStore prodStore = prodStoreDAO.findById(prodOut.getProdStore().getPstId());
				prodStore.setPstCount(prodStore.getPstCount()-outNum);
				prodStoreDAO.merge(prodStore);
			}
			saveWhRecIn(null,prodOut,outNum);
		}
	}
	
	public void saveWhRecIn(ProdIn prodIn,ProdOut prodOut,double num){
		WhRec whRec = new WhRec();
		 ProdStore prodStore = null;
		if(prodIn!=null){
			prodStore = prodStoreDAO.findById(prodIn.getProdStore().getPstId());
			if(prodIn.getPinInNum() !=null){
				whRec.setWreCount(num);
			}else{
				whRec.setWreCount(0.0);
			}
			whRec.setWreType("1");
			if(prodIn.getPinRespMan()!=null){
				whRec.setWreMan(prodIn.getPinRespMan());
			}else{
				whRec.setWreMan(null);
			}
		}
		if(prodOut !=null){
			prodStore = prodStoreDAO.findById(prodOut.getProdStore().getPstId());
			if(prodOut.getPouOutNum() !=null){
				whRec.setWreCount(num);
			}else{
				whRec.setWreCount(0.0);
			}
			whRec.setWreType("0");
			if(prodOut.getPouRespMan()!=null){
				whRec.setWreMan(prodOut.getPouRespMan());
			}else{
				whRec.setWreMan(null);
			}
		}
		whRec.setProdStore(prodStore);
		whRec.setWreTime(GetDate.getCurTime());
		whRec.setWreLeftCount(prodStore.getPstCount());
		whRecDAO.save(whRec);
	}
	
	/*ProdStore*/
	public int listProdStoreCount(String[] args){
		return prodStoreDAO.listCount(args);
	}
	
	public List<ProdStore> listProdStore(String[] args, String orderItem,String isDe, int currentPage,int pageSize){
		return prodStoreDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	
	public ProdStore getProdStoreById(String id){
		if(StringFormat.isEmpty(id)){
			return null;
		}else{
			return prodStoreDAO.findById(Long.parseLong(id));
		}
	}
	
	public void saveProdStore(ProdStore prodStore, LimUser curUser){
		if(prodStore.getPstId() == null){
			prodStore.setPstCreMan(curUser.getUserSeName());
			prodStore.setPstCreTime(GetDate.getCurTime());
			prodStoreDAO.save(prodStore);
		}else{
			ProdStore old = prodStoreDAO.findById(prodStore.getPstId());
			prodStore.setPstCreMan(old.getPstCreMan());
			prodStore.setPstCreTime(old.getPstCreTime());
			prodStore.setPstUpdMan(curUser.getUserSeName());
			prodStore.setPstUpdTime(GetDate.getCurTime());
			saveUpdStoreRecord(prodStore,old.getPstCount(),curUser);
			prodStoreDAO.merge(prodStore);
			
		}
	}
	
	
	public void saveUpdStoreRecord(ProdStore prodStore,double num,LimUser curUser){
		double count = (prodStore.getPstCount()!=null)? prodStore.getPstCount():0;
		if(count != num){
			WhRec whRec = new WhRec();
			whRec.setWreCount(count - num);
			whRec.setWreType("2");
			whRec.setWreMan(curUser.getUserSeName());
			whRec.setProdStore(prodStore);
			whRec.setWreTime(GetDate.getCurTime());
			whRec.setWreLeftCount(prodStore.getPstCount());
			whRecDAO.save(whRec);
		}

	}
	
	public void deleteProdStore(String id){
		if(!StringFormat.isEmpty(id)){
			prodStoreDAO.delete(id);
		}
	}
	
	
	/*ProdOut*/
	public List<ProdOut> listProdOut(String[] args, String orderItem,
			String isDe, int currnetPage, int pageSize) {
		return prodOutDAO.list(args, orderItem, isDe, currnetPage, pageSize);
	}
	
	public int listProdOutCount(String[] args) {
		return prodOutDAO.listCount(args);
	}
	
	public void cancleProdOut(String id){
		if(!StringFormat.isEmpty(id)){
			ProdOut prodOut = prodOutDAO.findById(Long.parseLong(id));
			inStore(null,prodOut,0.0,2);
			prodOut.setPouState("0");
			prodOutDAO.merge(prodOut);
		}
	}
	
	public void delProdOut(String pouId){
		if(!StringFormat.isEmpty(pouId)){
			//inStore(null,prodOutDAO.findById(Long.parseLong(pouId)),0.0,2);
			prodOutDAO.deleteById(pouId);
		}
	}
	
	public ProdOut getProdOutById(String id){
		if(StringFormat.isEmpty(id)){
			return null;
		}else{
			return prodOutDAO.findById(Long.parseLong(id));
		}
	}
	
	public void saveProdOut(ProdOut prodOut, LimUser curUser){
		if(prodOut.getPouId()==null){
			prodOut.setPouCreMan(curUser.getUserSeName());
			prodOut.setPouCreTime(GetDate.getCurTime());
			prodOut.setPouState("1");
			prodOutDAO.save(prodOut);
			inStore(null,prodOut,0.0,1);
		}else{
			ProdOut old = prodOutDAO.findById(prodOut.getPouId());
			prodOut.setPouState(old.getPouState());
			prodOut.setPouCreMan(old.getPouCreMan());
			prodOut.setPouCreTime(old.getPouCreTime());
			prodOut.setPouUpdMan(curUser.getUserSeName());
			prodOut.setPouUpdTime(GetDate.getCurTime());
			inStore(null,prodOut,old.getPouOutNum(),1);
			prodOutDAO.merge(prodOut);
		}
	}
	/*WhRec*/
	public int listWhRecCount(String[] args){
		return whRecDAO.listCount(args);
	}
	
	public List<WhRec> listWhRec(String[] args, String orderItem, String isDe,int currentPage, int pageSize){
		return  whRecDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	
	/**
	 * 锟斤拷莨锟接︼拷锟絀d锟斤拷锟揭癸拷应锟教诧拷品<br>
	 * @param supId 锟斤拷应锟斤拷Id
	 * @return 锟斤拷应锟教诧拷品锟叫憋拷
	 */
	public List<SupProd> listBySupId(String supId){
		return supProdDAO.listBySupId(supId);
	}
	
	/**
	 * 锟斤拷锟斤拷晒锟斤拷锟斤拷锟较�<br>
	 */
	public void saveRpp(List<RPuoPro> rppList, String puoId){
		/**List<RPuoPro> saveList = new ArrayList();
		Iterator<RPuoPro> toSaveIt = rppList.iterator();
		List<RPuoPro> oldList = rPuoProDAO.findByPurOrd(puoId);
		Iterator<RPuoPro> oldIt = null;
		RPuoPro rordPro = null;
		RPuoPro oldRordPro = null;
		while(toSaveIt.hasNext()){
			rordPro = toSaveIt.next();
			oldIt = oldList.iterator();
			while(oldIt.hasNext()){
				oldRordPro = oldIt.next();
				if(oldRordPro.getWmsProduct().getWprId().equals(rordPro.getWmsProduct().getWprId())){
					rordPro.setRppOutNum(oldRordPro.getRppOutNum());
					rordPro.setRppRealNum(oldRordPro.getRppRealNum());
					oldList.remove(rordPro);
					break;
				}
			}
			saveList.add(rordPro);
		}*/
		
//		orderBiz.delByWCode(sodCode);//锟斤拷锟斤拷删锟斤拷
		
		rPuoProDAO.batSave(rppList,puoId); 
	}
	
	/**
	 * 锟缴癸拷锟斤拷锟斤拷品锟斤拷细锟叫憋拷<br>
	 * @param puoId 锟缴癸拷ID
	 * @return List<RPuoPro> 锟缴癸拷锟斤拷锟斤拷品锟斤拷细锟叫憋拷
	 */
	public List<RPuoPro> listPuoPro(String puoId){
		return rPuoProDAO.listPuoPro(puoId);
	}
	
	public List<SupProd> listSupProd(String[] args, String orderItem,
			String isDe, int currentPage, int pageSize) {
		return supProdDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listSupProdCount(String[] args) {
		return supProdDAO.listCount(args);
	}
	
	public SupProd getSupProdById(String id){
		if(StringFormat.isEmpty(id)){
			return null;
		}else{
			return supProdDAO.findById(Long.parseLong(id));
		}
		
	}
	
	public void saveSupProd(SupProd supProd, LimUser curUser){
		if(supProd.getSpId() == null){
			supProd.setSpCreUser(curUser.getUserSeName());
			supProd.setSpCreDate(GetDate.getCurTime());
			supProdDAO.save(supProd);
		}else{
			SupProd old = supProdDAO.findById(supProd.getSpId());
			supProd.setSpCreDate(old.getSpCreDate());
			supProd.setSpCreUser(old.getSpCreUser());
			supProd.setSpUpdDate(GetDate.getCurTime());
			supProd.setSpUpdUser(curUser.getUserSeName());
			supProdDAO.merge(supProd);
		}
	}
	
	public void delSupProd(String id){
		if(!StringFormat.isEmpty(id)){
			supProdDAO.deleteById(id);
		}
	}
	
	public List<SupPaidPast> listSupPaidPast(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize) {
		return supPaidPastDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	public int listSupPaidPastCount(String[] args) {
		return supPaidPastDAO.listCount(args);
	}
	
	public SupPaidPast getSupPaidPast(String sppId){
		if(StringFormat.isEmpty(sppId)){
			return null;
		}else{
			return supPaidPastDAO.findById(Long.parseLong(sppId));
		}
	}
	
	public void saveSupPaidPast(SupPaidPast supPaidPast,LimUser crUser){
		if(supPaidPast.getSppId() == null){
			supPaidPast.setSppCreDate(GetDate.getCurTime());
			supPaidPast.setSppCreUser(crUser.getUserSeName());
			supPaidPastDAO.save(supPaidPast);
		}else{
			SupPaidPast old = supPaidPastDAO.findById(supPaidPast.getSppId());
			supPaidPast.setSppCreDate(old.getSppCreDate());
			supPaidPast.setSppCreUser(old.getSppCreUser());
			supPaidPast.setSppUpdDate(GetDate.getCurTime());
			supPaidPast.setSppUpdUser(crUser.getUserSeName());
			supPaidPastDAO.merge(supPaidPast);
		}
	}
	
	public void delSupPaidPast(String id){
		if(!StringFormat.isEmpty(id)){
			supPaidPastDAO.deleteById(id);
		}
	}
	
	public List<SupInvoice> listSupInvoice(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize){
		return supInvoiceDAO.list(args, orderItem, isDe, currentPage, pageSize);
	}
	
	public int listSupInvoiceCount(String[] args){
		return supInvoiceDAO.listCount(args);
	}
	
	public SupInvoice getSupInvoice(String suiId){
		if(StringFormat.isEmpty(suiId)){
			return null;
		}else{
			return supInvoiceDAO.findById(Long.parseLong(suiId));
		}
	}
	
	public void saveSupInvoice(SupInvoice supInvoice, LimUser curUser){
		if(supInvoice.getSuiId() == null){
			supInvoice.setSuiCreUser(curUser.getUserSeName());
			supInvoice.setSuiCreDate(GetDate.getCurTime());
			supInvoiceDAO.save(supInvoice);
		}else{
			SupInvoice old = supInvoiceDAO.findById(supInvoice.getSuiId());
			supInvoice.setSuiCreDate(old.getSuiCreDate());
			supInvoice.setSuiCreUser(old.getSuiCreUser());
			supInvoice.setSuiUpdDate(GetDate.getCurTime());
			supInvoice.setSuiUpdUser(curUser.getUserSeName());
			supInvoiceDAO.merge(supInvoice);
		}
	}
	
	public void delSupInvoice(String suiId){
		if(!StringFormat.isEmpty(suiId)){
			supInvoiceDAO.deleteById(suiId);
		}
	}
	public void setSupplierDAO(SupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}
	public void setPurOrdDAO(PurOrdDAO purOrdDAO) {
		this.purOrdDAO = purOrdDAO;
	}
	public void setRpuoProDAO(RPuoProDAO rPuoProDAO){
		this.rPuoProDAO = rPuoProDAO;
	}
	public void setProdInDAO(ProdInDAO prodInDAO) {
		this.prodInDAO = prodInDAO;
	}
	public void setProdOutDAO(ProdOutDAO prodOutDAO) {
		this.prodOutDAO = prodOutDAO;
	}
	public void setProdStoreDAO(ProdStoreDAO prodStoreDAO) {
		this.prodStoreDAO = prodStoreDAO;
	}
	public void setWhRecDAO(WhRecDAO whRecDAO) {
		this.whRecDAO = whRecDAO;
	}

	public void setSupProdDAO(SupProdDAO supProdDAO) {
		this.supProdDAO = supProdDAO;
	}

	public void setSupPaidPastDAO(SupPaidPastDAO supPaidPastDAO) {
		this.supPaidPastDAO = supPaidPastDAO;
	}
	public void setSupInvoiceDAO(SupInvoiceDAO supInvoiceDAO) {
		this.supInvoiceDAO = supInvoiceDAO;
	}

	
}