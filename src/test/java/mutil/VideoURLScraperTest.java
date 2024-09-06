package mutil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class VideoURLScraperTest {

    // 每次测试之前重置urls列表
    @BeforeEach
    public void setUp() {
        // 清空urls列表，确保每次测试的独立性
        VideoURLScraper.getVideoURLs().clear();
    }

    // 测试addVideoURLs()方法是否能成功提取视频URL
    @Test
    public void testAddVideoURLs() {
        // 传入一个有效的URL（可以是一个实际存在的测试页面）
        VideoURLScraper.addVideoURLs("https://search.bilibili.com/all?keyword=2024%E5%B7%B4%E9%BB%8E%E5%A5%A5%E8%BF%90%E4%BC%9A");

        // 验证提取的URL数量是否大于0
        assertFalse(VideoURLScraper.getVideoURLs().isEmpty(), "视频URL列表不应为空");
    }

    // 测试getVideoURLs()方法是否能返回至少100个视频链接
    @Test
    public void testGetVideoURLs() {
        // 调用getVideoURLs()方法
        List<String> videoURLs = VideoURLScraper.getVideoURLs();

        // 验证返回的视频URL数量是否达到了预期的100个
        assertEquals(100, videoURLs.size(), "视频URL数量应为100");
    }

    // 测试URL是否以"https://"开头并包含正确的spm参数
    @Test
    public void testURLsFormat() {
        // 调用getVideoURLs()方法
        List<String> videoURLs = VideoURLScraper.getVideoURLs();

        // 验证每个URL的格式
        for (String url : videoURLs) {
            assertTrue(url.startsWith("https://"), "视频URL应以https://开头");
            assertTrue(url.contains("spm_id_from=333.337.search-card.all.click"), "视频URL应包含spm_id_from参数");
        }
    }

    // 测试addVideoURLs()时的异常处理
    @Test
    public void testAddVideoURLsExceptionHandling() {
        // 模拟传入一个无效的URL
        assertDoesNotThrow(() -> VideoURLScraper.addVideoURLs("https://invalid.url"),
                "无效的URL不应抛出异常");
    }
}
