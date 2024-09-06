package mutil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.util.List;

public class Excel {

    /**
     * 将数据保存到Excel文件
     *
     * @param kvs 要保存的键值对列表，每个键值对包含一个单词和它的出现次数
     */
    public static void saveDataToExcel(List<KV> kvs) {
        // 设置样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置水平对齐方式
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);

        // 基于注解的样式策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, (List<WriteCellStyle>) null);

        // 将生成的Excel文件保存到磁盘
        try (FileOutputStream outputStream = new FileOutputStream("output.xlsx")) {
            // 创建一个写操作
            EasyExcel.write(outputStream)
                    .registerWriteHandler(horizontalCellStyleStrategy) // 注册样式策略
                    .sheet("Sheet1") // 创建Sheet页
                    .doWrite(kvs); // 写入数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 测试数据
        List<KV> kvs = List.of(
                new KV("apple", 10),
                new KV("banana", 20),
                new KV("cherry", 30)
        );

        // 调用方法保存数据到Excel
        saveDataToExcel(kvs);
    }
}
