package cn.self.cloud.commonutils.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicholas on 2017/4/26.
 */
public class Page1 implements Serializable {
    private Long totalElements;
    private Integer totalPages;
    private Integer numberOfElements;
    private Integer number;
    private Integer size;
    private List content = new ArrayList();

    public Page1(List list, Integer number, Integer size) {
        this.number = number;
        this.size = size;
        this.content = list;
        this.totalElements = ((com.github.pagehelper.Page) list).getTotal();
        this.totalPages = ((com.github.pagehelper.Page) list).getPages();
        this.numberOfElements = ((com.github.pagehelper.Page) list).getEndRow();
    }

    public Page1(List list) {
        com.github.pagehelper.Page page = (com.github.pagehelper.Page) list;
        number = page.getPageNum();
        size = page.getPageSize();
        content = list;
        totalElements = page.getTotal();
        totalPages = page.getPages();
        numberOfElements = page.getEndRow();
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List getContent() {
        return content;
    }

    public void setContent(List content) {
        this.content = content;
    }
}
