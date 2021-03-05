package com.gdztyy.api.controller;

import com.alibaba.excel.EasyExcel;
import com.gdztyy.api.service.PubMonthlySalesService;
import com.gdztyy.api.vo.DemoData;
import com.gdztyy.api.vo.PubMonthlySales;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

///**
// * @author
// */

@RestController
@RequestMapping("api/report")

public class PubMonthlySalesController {

    @Resource
    private  PubMonthlySalesService pubMonthlySalesService;


    @RequestMapping(value = "/exportCandidateTempt")
    public void exportCandidateTempt(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //此处的list可以是自己从数据库中查询的数据
        String startTime = "2020-01-10 00:00:00";
        String endTime = "2020-02-10 00:00:00";
        //  String fileName = "商品销售数据";
       /* ExportParams params = new ExportParams(fileName, "商品销售数据");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timeStr = formatter.format(LocalDateTime.now());*/
        List<PubMonthlySales> pubMonthlySalesList = pubMonthlySalesService.selectListByTime(startTime, endTime);

       String fileName = String.format("销售报表.xlsx");
        try {
            ExcelUtils.export(fileName, pubMonthlySalesList, PubMonthlySales.class);
        } catch (IOException e) {
           // log.error("导出Excel异常,exception={}", e);
            throw new Exception("导出Excel异常：" + e.getMessage());
        }
       /* response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "商品销售数据.xlsx";
        fileName = URLEncoder.encode(fileName,"utf-8");
        response.setHeader("Content-Disposition","attachment;filename" + fileName);
        EasyExcel.write(response.getOutputStream(),PubMonthlySales.class).sheet("PubMonthlySales").doWrite(pubMonthlySalesList);*/

    }
    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}