package cn.self.cloud.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
	//所有需要权限验证的url
	public final static List<String> ALLPERMISSIONURL = new ArrayList<String>();

	@Autowired
	private PermissionService permissionService;

	@PostConstruct
	private void init() {
		ALLPERMISSIONURL.clear();
		ALLPERMISSIONURL.addAll(permissionService.findAllUrl());
	}
}
