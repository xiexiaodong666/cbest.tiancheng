package com.welfare.service.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  11:16 AM
 */
public class PageUtils {


  public static<T>  Page<T>  toPage(Page page, List<T> content) {
    Page<T> tPage = new Page<T>();
    tPage.setRecords(content);
    tPage.setSize(page.getSize());
    tPage.setCurrent(page.getCurrent());
    tPage.setTotal(page.getTotal());
    tPage.setPages(page.getPages());
    return tPage;
  }
}