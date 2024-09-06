import mutil.Comment;
import mutil.Const;
import mutil.KV;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long total = startTime;

        // 获取视频的url
        List<String> urls = mutil.VideoURLScraper.getVideoURLs();
        System.out.println("【TIME】,get url 用时 " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();

        // 获取视频的cid
        List<Long> cids = mutil.Cid.getCIDS(urls);
        System.out.println("【TIME】,get cid 用时 " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();

        // 获取视频的评论并对相同的项进行合并
        ConcurrentMap<String, Integer> dst = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(Const.threadCount);
        int count = 0;
        for (long cid : cids) {
            count++;
            executor.submit(() -> {
                Map<String, Integer> comment = Comment.findComment(cid);
                synchronized (dst) {
                    mutil.Utils.combineMap(comment,dst);
                }
            });
            if (count % Const.threadCount == 0) {
                try {Thread.sleep(100);} catch (InterruptedException ignored) {}
            }

        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("【TIME】,get comment 用时 " + (System.currentTimeMillis() - startTime) + " ms");
        startTime = System.currentTimeMillis();

        // 依据出现的次数对评论进行排序
        List<KV> results = KV.sort(dst);
        mutil.Excel.saveDataToExcel(results);
        System.out.println("【TIME】,create excel 用时 " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("[TIME],共用时 " + (System.currentTimeMillis() - total) / 1000 + " s");
    }
}
