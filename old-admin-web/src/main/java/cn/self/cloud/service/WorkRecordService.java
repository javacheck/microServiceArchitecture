package cn.self.cloud.service;

import cn.self.cloud.bean.WorkRecord;
import cn.self.cloud.commonutils.id.IdService;
import cn.self.cloud.dao.WorkRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkRecordService {
	
	@Autowired
	private WorkRecordDao workRecorddao;
	
	@Autowired
	private IdService idService;
	
	public Long beginSave(WorkRecord workRecord){
		workRecord.setId(idService.getId());
		return workRecorddao.beginSave(workRecord);
	}
	public void endUpdate(WorkRecord workRecord){
		workRecorddao.endUpdate(workRecord);
	}
	public WorkRecord findById(Long id){
		return workRecorddao.findById(id);
	}
	public WorkRecord findByToken(String token){
		return workRecorddao.findByToken(token);
	}
	
}
