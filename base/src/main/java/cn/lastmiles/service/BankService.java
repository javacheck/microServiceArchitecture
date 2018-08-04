package cn.lastmiles.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.lastmiles.bean.Bank;
import cn.lastmiles.dao.BankDao;

@Service
public class BankService {
	@Autowired
	private BankDao bankDao;

	public List<Bank> findAll() {
		return bankDao.findAll();
	}
}
