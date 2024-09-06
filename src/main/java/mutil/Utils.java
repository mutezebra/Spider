package mutil;

import java.util.Map;

public class Utils {
    // 定义一个包含不同User-Agent字符串的数组
    private static final String[] AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36Edge/13.10586",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_4 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) CriOS/27.0.1453.10 Mobile/10B350 Safari/8536.25",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.11 (KHTML, like Gecko) Ubuntu/11.10 Chromium/27.0.1453.93 Chrome/27.0.1453.93 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19",
            "Mozilla/5.0 (Linux; Android 4.1.2; Nexus 7 Build/JZ054K) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19",
            "Mozilla/5.0 (Android; Mobile; rv:14.0) Gecko/14.0 Firefox/14.0",
            "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0"
    };

    // 定义一个用于记录请求次数的变量
    private static int count = 0;

    /**
     * 获取随机的User-Agent
     *
     * @return 返回一个随机的User-Agent字符串
     */
    public static String getUserAgent() {
        // 使用计数器方式获取不同的User-Agent
        count++;
        return AGENTS[count % AGENTS.length];
    }

    /**
     * 将源Map中的数据合并到目标Map中
     *
     * @param src 源Map，包含要合并的键值对
     * @param dst 目标Map，接收合并后的数据
     */
    public static void combineMap(Map<String, Integer> src, Map<String, Integer> dst) {
        // 遍历源Map的每个键值对
        for (Map.Entry<String, Integer> entry : src.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            // 如果目标Map中已经存在该键，则累加其值；否则，直接添加该键值对
            dst.put(key, dst.getOrDefault(key, 0) + value);
        }
    }
}