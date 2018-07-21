package cn.self.cloud.commonutils.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class Page implements Serializable {
    private static final long serialVersionUID = -2207250604794244665L;

    private final static Integer DEFAULTPAGESIZE = 25;

    private Integer pageNo = 1;
    private Integer pageSize = DEFAULTPAGESIZE;
    private Integer pages = 0;
    private Integer total = 0;
    private List<?> data = Collections.EMPTY_LIST;

    public Page() {
        super();
    }

    public Page(Integer pageSize) {
        super();
        this.pageSize = pageSize;
    }

    public Page(Integer pageNo, Integer pageSize) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Page(Integer pageNo, Integer pageSize, List<?> data) {
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

    public Integer getStart() {
        return (getPageNo() - 1) * getPageSize();
    }

    public Integer getEnd() {
        int end = (getPageNo() * getPageSize());
        return end > data.size() ? data.size() : end;
    }

    public Integer getPages() {
        if (total % pageSize == 0) {
            pages = total / pageSize;
        } else {
            pages = total / pageSize + 1;
        }
        return pages;
    }


    @Override
    public String toString() {
        return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", pages=" + pages + ", total=" + total + ", data=" + data + "]";
    }

    /**
     * 不分页
     *
     * @return
     */
    public Page setIsOnePage() {
        this.pageSize = Integer.MAX_VALUE;
        return this;
    }

    public void dealPaging() {
        if (data.isEmpty()) {
            return;
        }
        int start = getStart();
        if (start > getEnd()) {
            start = getEnd();
        }
        data = data.subList(start, getEnd());
    }
}
