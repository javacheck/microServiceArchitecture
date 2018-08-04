package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Discount;
import cn.lastmiles.dao.DiscountDao;

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

	public List<Discount> findAll() {
		return discountDao.findAll();
	}
}
