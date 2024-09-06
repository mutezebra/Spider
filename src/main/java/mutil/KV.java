package mutil;

import java.util.*;

// MaxHeap 类实现了自定义的最大堆（优先队列）
class MaxHeap {
    // 使用PriorityQueue作为堆的底层实现
    private PriorityQueue<KV> heap;

    // 构造方法，初始化优先队列，使用自定义的比较器构建最大堆
    public MaxHeap() {
        // 比较器定义了按照 KV 对象中的 v 字段（次数）进行降序排列
        heap = new PriorityQueue<>((a, b) -> Integer.compare(b.v, a.v));
    }

    // 返回堆的大小
    public int size() {
        return heap.size();
    }

    // 向堆中插入一个元素
    public void push(KV kv) {
        heap.offer(kv); // 插入操作，使用 PriorityQueue 的 offer 方法
    }

    // 从堆中弹出最大值的元素
    public KV pop() {
        return heap.poll(); // 弹出堆顶元素
    }

    // 判断堆是否为空
    public boolean isEmpty() {
        return heap.isEmpty();
    }
}

// 工具类 Util
public class KV {
    int v;

    String k;

    // 构造方法
    public KV(String k, int v) {
        this.k = k;
        this.v = v;
    }

    /**
     * 对源 Map 进行排序，将其中的单词和对应出现次数按次数降序排列
     *
     * @param src 源数据 Map，存储单词及其出现次数
     * @return 返回排序后的 KV 数组
     */
    public static List<KV> sort(Map<String, Integer> src) {
        // 如果源数据为空，直接返回空列表
        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }

        // 初始化最大堆
        MaxHeap maxHeap = new MaxHeap();

        // 遍历源 Map，将每个键值对（单词和出现次数）存入最大堆中
        for (Map.Entry<String, Integer> entry : src.entrySet()) {
            maxHeap.push(new KV(entry.getKey(), entry.getValue()));
        }

        // 创建结果列表用于存储排序后的键值对
        List<KV> results = new ArrayList<>();

        // 从最大堆中依次弹出元素并加入结果列表
        while (!maxHeap.isEmpty()) {
            results.add(maxHeap.pop());
        }

        // 返回排序后的结果
        return results;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return k + ": " + v;
    }
}
