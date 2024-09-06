package mutil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoURLScraper {

    // 搜索页面的URL
    private static final String searchURL = "https://search.bilibili.com/all?spm_id_from=333.1007&search_source=3&from_source=webtop_search&keyword=2024%E5%B7%B4%E9%BB%8E%E5%A5%A5%E8%BF%90%E4%BC%9A";

    // 用户代理，用于模拟浏览器请求
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0";

    // 用于存储视频URL的列表
    private static List<String> urls = new ArrayList<>();
    /**
     * 发送请求并解析HTML页面，从中提取视频URL
     * @param url 要访问的页面URL
     */
    public static void addVideoURLs(String url) {
        try {
            // 使用Jsoup发送请求并获取文档
            Document doc = Jsoup.connect(url).userAgent(userAgent).get();

            // 查找包含视频信息的HTML元素
            Elements videoElements = doc.select("div.bili-video-card__info--right");

            // 遍历找到的元素，提取视频的链接
            for (Element element : videoElements) {
                Element linkElement = element.selectFirst("a");
                if (linkElement != null) {
                    String videoUrl = linkElement.attr("href");
                    // 确保提取到的URL是有效的，并且是视频链接
                    if (!videoUrl.isEmpty()) {
                        videoUrl = "https:" + videoUrl + "?spm_id_from=333.337.search-card.all.click&vd_source=6d8ea21e6f2f2c3344c170907eb4ca6c";
                        urls.add(videoUrl);
                    }
                    if (urls.size() >= Const.videoCount) {
                        return;
                    }
                }
            }

        } catch (IOException e) {
            // 错误处理，输出日志信息
            System.err.println("获取页面失败: " + e.getMessage());
        }
    }

    /**
     * 获取视频URL列表
     *
     * @return 返回视频链接的列表
     */
    public static List<String> getVideoURLs() {

        // 初始化第一页请求
        addVideoURLs(searchURL);
        int page = 1;
        // 模拟多页抓取
        while (urls.size() < Const.videoCount) {  // 假设需要抓取100个视频链接
            try {
                // 每次请求后暂停1秒，以避免过于频繁地访问服务器
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            page++;
            // 构建分页URL
            String nextPageUrl = String.format("%s&page=%d&o=%d", searchURL, page, (page - 1) * 36);
            // 继续抓取下一页的视频URL
            addVideoURLs(nextPageUrl);
        }

        return urls;
    }
}
