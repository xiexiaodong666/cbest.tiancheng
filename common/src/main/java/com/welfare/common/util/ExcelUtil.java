package com.welfare.common.util;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author guopop
 * @date 2019/12/2 10:03
 */
public class ExcelUtil {

    public static  <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dtos, Class<T> clazz) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileNameUTF8 = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameUTF8 + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(dtos);
    }
}
