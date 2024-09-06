package mutil;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    // 测试 getNextURL() 方法是否能够正确生成弹幕 API 请求的 URL
    @Test
    public void testGetNextURL() {
        // 假设一个cid值进行测试
        long testCid = 123456789L;
        Comment comment = new Comment(testCid);

        try {
            // 获取生成的第一个 URL
            URL url = comment.getNextURL();
            // 检查生成的 URL 是否包含正确的 cid 和 segment_index
            String expectedUrl = String.format("https://api.bilibili.com/x/v2/dm/web/seg.so?type=1&oid=%d&segment_index=1", testCid);
            assertEquals(expectedUrl, url.toString(), "生成的URL不正确");
        } catch (IOException e) {
            fail("生成URL时出现异常: " + e.getMessage());
        }
    }

    // 测试 findComment() 方法是否能够正确处理请求并返回评论数据
    @Test
    public void testFindComment() {
        // 假设一个有效的cid进行测试
        long testCid = 987654321L;

        // 调用 findComment() 方法获取评论数据
        Map<String, Integer> comments = Comment.findComment(testCid);

        // 假设cid下有评论，检查返回的Map是否非空
        assertNotNull(comments, "评论数据不应为空");
        assertFalse(comments.isEmpty(), "评论数据应该包含结果");

        // 如果有特定的评论格式可以进行更多详细检查
        // assertTrue(comments.containsKey("someExpectedCommentKey"), "评论数据应包含指定键值");
    }

    // 测试 findComment() 方法对无效CID的处理
    @Test
    public void testFindCommentWithInvalidCID() {
        // 使用无效的cid进行测试
        long invalidCid = 0L;

        // 调用 findComment() 方法
        Map<String, Integer> comments = Comment.findComment(invalidCid);

        // 检查返回的Map应为空，表示没有找到有效的评论
        assertNotNull(comments, "即使没有评论数据，返回的Map也不应为空");
        assertTrue(comments.isEmpty(), "无效CID应返回空的评论结果");
    }
}
