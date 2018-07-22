package cn.self.cloud.service;

import cn.self.cloud.bean.Discount;
import cn.self.cloud.dao.DiscountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
	@Autowired
	private DiscountDao discountDao;

	public void set(Discount discount) {
		discountDao.set(discount);
	}

	public Discount find(Long storeId) {
		return discountDao.find(storeId);
	}
}
