package mutil;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelTest {

    /**
     * 测试 saveDataToExcel() 方法是否正确保存数据到 Excel 文件中
     */
    @Test
    public void testSaveDataToExcel() {
        // 创建测试数据
        List<KV> testData = new ArrayList<>();
        testData.add(new KV("单词1", 10));
        testData.add(new KV("单词2", 20));
        testData.add(new KV("单词3", 30));

        // 调用 saveDataToExcel() 方法将数据保存到 Excel 文件
        Excel.saveDataToExcel(testData);

        // 验证 Excel 文件是否保存成功
        try (FileInputStream fis = new FileInputStream(Const.excelName);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // 验证工作簿是否存在
            assertNotNull(workbook, "工作簿不应为空");

            // 获取 Sheet 页
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet, "Sheet页应存在");

            // 验证表头
            Row headerRow = sheet.getRow(0);
            assertNotNull(headerRow, "表头行应存在");
            assertEquals("次数", headerRow.getCell(0).getStringCellValue(), "第一列表头应为'次数'");
            assertEquals("内容", headerRow.getCell(1).getStringCellValue(), "第二列表头应为'内容'");

            // 验证数据是否正确保存
            for (int i = 0; i < testData.size(); i++) {
                KV kv = testData.get(i);
                Row row = sheet.getRow(i + 1);  // 跳过表头，从第二行开始验证

                assertNotNull(row, "数据行不应为空");
                assertEquals(kv.v, (int) row.getCell(0).getNumericCellValue(), "次数列数据应匹配");
                assertEquals(kv.k, row.getCell(1).getStringCellValue(), "内容列数据应匹配");
            }
        } catch (IOException e) {
            fail("读取或解析Excel文件时出现异常: " + e.getMessage());
        }
    }
}
