package com.sky.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkSpaceService workSpaceService;
    /**
     * turnover statistics
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1)) {
            
            dateList.add(date);

            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime datEnd = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", dayStart);
            map.put("end", datEnd);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.getTurnover(map);
            turnoverList.add(turnover == null ? 0 : turnover);
        
        }
        return TurnoverReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .turnoverList(StringUtils.join(turnoverList, ","))
            .build();
    }

    /**
     * user statistics
     * @param begin
     * @param end
     * @return
     */    
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        for (LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1)) {
            
            dateList.add(date);

            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime datEnd = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", dayStart);
            map.put("end", datEnd);
            Integer newUserSum = userMapper.getNewUserSum(map);
            newUserList.add(newUserSum == null ? 0 : newUserSum);

            Integer totalUserSum = userMapper.getTotalUserSum(map);
            totalUserList.add(totalUserSum == null ? 0 : totalUserSum);
        }
        return UserReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .newUserList(StringUtils.join(newUserList, ","))
            .totalUserList(StringUtils.join(totalUserList, ","))
            .build();
    }

    /**
     * orders statistics
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> completedOrderList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer completedOrderCount = 0;
        for (LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1)) {
            
            dateList.add(date);

            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime datEnd = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", dayStart);
            map.put("end", datEnd);
            map.put("status", Orders.COMPLETED);
            Integer totalOrderNum = orderMapper.getToralOrderNum(map);
            totalOrderNum = totalOrderNum == null ? 0 : totalOrderNum;
            totalOrderCount += totalOrderNum;
            totalOrderList.add(totalOrderNum);

            Integer completedOrderNum = orderMapper.getOrderNumByStatusAndTime(map);
            completedOrderNum = completedOrderNum == null ? 0 : completedOrderNum;
            completedOrderCount += completedOrderNum;
            completedOrderList.add(completedOrderNum);
        }
        return OrderReportVO.builder()
            .dateList(StringUtils.join(dateList, ","))
            .orderCountList(StringUtils.join(totalOrderList, ","))
            .validOrderCountList(StringUtils.join(completedOrderList, ","))
            .totalOrderCount(totalOrderCount)
            .validOrderCount(completedOrderCount)
            .orderCompletionRate(completedOrderCount * 1.0 / totalOrderCount)
            .build();
    }

    /**
     * top 10
     * @return SalesTop10ReportVO
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (LocalDate date = begin; !date.isAfter(end); date = date.plusDays(1)) {
            
            dateList.add(date);

            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime datEnd = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", dayStart);
            map.put("end", datEnd);
            map.put("status", Orders.COMPLETED);
            List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getTop10(map);
            if (goodsSalesDTOList != null && goodsSalesDTOList.size() > 0) {
                for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
                    nameList.add(goodsSalesDTO.getName());
                    numberList.add(goodsSalesDTO.getNumber());
                }
            }
        }
        return SalesTop10ReportVO.builder()
            .nameList(StringUtils.join(nameList, ","))
            .numberList(StringUtils.join(numberList, ","))
            .build();
    }

    
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate beginDay = LocalDate.now().minusDays(30);
        LocalDate endDay = LocalDate.now().minusDays(1);
        LocalDateTime begin = LocalDateTime.of(beginDay, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(endDay, LocalTime.MAX);
        BusinessDataVO businessData = workSpaceService.getBusinessData(begin, end);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel = new XSSFWorkbook(in);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            sheet.getRow(1).getCell(1).setCellValue("Time from " + begin + " to " + end);
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            
            
            for (int i = 0; i < 30; i++) {
                LocalDate date = beginDay.plusDays(i);
                businessData = workSpaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());

            }
            
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            out.close();
            excel.close();
        } catch (Exception e) {
            log.error("导出运营数据报表失败: {}", e.getMessage());
        }
    }
    
}
