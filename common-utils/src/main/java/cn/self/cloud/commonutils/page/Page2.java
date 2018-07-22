package cn.self.cloud.commonutils.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
/**
 * 分页封装
 */
public final class Page2 implements Serializable{

	// 默认一页显示多少行数据
	private final static Integer DEFAULTPAGESIZE = 10;
	// 显示页数
	private Integer pageNo = 1;
	// 显示多少行数据
	private Integer pageSize = DEFAULTPAGESIZE;
	// 根据页数和显示多少行计算出的共显示多少页数据(总页数)
	private Integer pages = 0;
	// 总共多少数据
	private Integer total = 0;
	// 显示数据集合
	private List<?> data = Collections.EMPTY_LIST;

	// 空构造器
	public Page2() {
		super();
	}

	// 构造器(传入显示多少行数据)
	public Page2(Integer pageSize) {
		super();
		this.pageSize = pageSize;
	}

	// 构造器(页数，每页显示的记录数，显示数据)
	public Page2(Integer pageNo, Integer pageSize, List<?> data) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.data = data;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	// 获得首页
	public Integer getStart() {
		return (getPageNo() - 1) * getPageSize();
	}

	// 计算共可以显示多少页数据<总页数=(总记录数%每页显示记录==0)?(总记录数/每页显示记录):(总记录数/每页显示记录+1)>
	public Integer getPages() {
		if (total % pageSize == 0) {
			pages = total / pageSize;
		} else {
			pages = total / pageSize + 1;
		}
		return pages;
	}

	// 不分页(设置每页显示的记录数值为Integer最大值)
	public Page2 setIsOnePage() {
		this.pageSize = Integer.MAX_VALUE;
		return this;
	}
}