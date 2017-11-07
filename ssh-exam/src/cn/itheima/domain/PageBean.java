package cn.itheima.domain;

import java.util.List;

public class PageBean<T> {

    private int pageNum;// 当前页码
    private int totalPage;// 总页数
    private int totalCount;// 总条数
    private int currentCount;// 每页条数
    private List<T> currentContent;// 每页显示的数据

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public List<T> getCurrentContent() {
        return currentContent;
    }

    public void setCurrentContent(List<T> currentContent) {
        this.currentContent = currentContent;
    }

    @Override
    public String toString() {
        return "PageBean [pageNum=" + pageNum + ", totalPage=" + totalPage + ", totalCount=" + totalCount
                + ", currentCount=" + currentCount + ", currentContent=" + currentContent + "]";
    }

}
