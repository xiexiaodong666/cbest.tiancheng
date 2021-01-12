package com.welfare.common.base;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

/**
 * Description： 分页请求
 */
public class RequestPage {
    /**
     * 页码，从1开始
     */
    @Min(value = 1,message="页码不能小于1")
    @ApiModelProperty(value = "第几页", required = true)
    private int currentPage = 1;

    @Min(value = 1,message="每页大小不能小于1")
    @ApiModelProperty(value = "每页大小", required = true)
    private int pageSize = 20;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
