package mutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Cid {

    static final String REFERER = "https://search.bilibili.com/all?";

    public static HttpURLConnection getCIDReq(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Referer", REFERER);
        connection.setRequestProperty("User-Agent", Utils.getUserAgent());
        return connection;
    }

    public static List<Long> getCIDS(List<String> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(Const.threadCount);
        List<Long> cids = new ArrayList<>(urls.size());
        for (int i = 0; i < urls.size(); i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    String url = urls.get(index);
                    if (url == null || url.isEmpty()) {
                        return;
                    }
                    HttpURLConnection connection = getCIDReq(url);

                    // 检查是否支持gzip
                    if ("gzip".equals(connection.getContentEncoding())) {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8))) {
                            findCid(cids, index, reader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                            findCid(cids, index, reader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException | RuntimeException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return cids;
    }

    static void findCid(List<Long> cids, int index, BufferedReader reader) throws IOException {
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        Matcher matcher = Pattern.compile("\"cid\":\\s*(\\d+)").matcher(content.toString());
        if (matcher.find()) {
            String cid = matcher.group(1);
            cids.add(Long.parseLong(cid));
            System.out.printf("Get 第 %d 个cid, %s\n", index + 1, cid);
        }
    }

    public static void main(String[] args) {
        List<String> urls = List.of("https://www.bilibili.com/video/BV1ki421677K/?spm_id_from=333.337.search-card.all.click&vd_source=6d8ea21e6f2f2c3344c170907eb4ca6c");
        List<Long> cids = getCIDS(urls);
        cids.forEach(cid -> System.out.println("CID: " + cid));
    }
}