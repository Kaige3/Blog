package com.kaige.list;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    @Test
    public void main1() {
        // 构造从start到end的序列：
        final int start = 10;
        final int end = 20;
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        // 随机删除List中的一个元素:
        int removed = list.remove((int) (Math.random() * list.size()));
        int found = findMissingNumber(start, end, list);
        System.out.println(list.toString());
        System.out.println("missing number: " + found);
        System.out.println(removed == found ? "测试成功" : "测试失败");
    }
    @Test
    public void main2() {
        // 构造从start到end的序列：
        final int start = 10;
        final int end = 20;
        List<Integer> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(i);
        }
        // 洗牌算法shuffle可以随机交换List中的元素位置:
        Collections.shuffle(list);
        // 随机删除List中的一个元素:
        int removed = list.remove((int) (Math.random() * list.size()));
        int found = findMissingNumber2(start, end, list);
        System.out.println(list.toString());
        System.out.println("missing number: " + found);
        System.out.println(removed == found ? "测试成功" : "测试失败");
    }

//  查找从10 到 20 缺失的元素
     int findMissingNumber(int start, int end, List<Integer> list) {
        int i;
        for (i = 10; i < end; i++) {
            if (!list.contains(i)) {
                return i;
            }
        }
        return list.get(i);
    }
//    查找从10 到20 随机缺失的元素
    int findMissingNumber2(int start, int end, List<Integer> list) {
        int i;
        for (i = 10; i < end; i++) {
            if (!list.contains(i)) {
                return i;
            }
        }
        return list.get(i);
    }
//    不利用 contains,查找随机确实元素
    static int findMissingNumber3(int start, int end, List<Integer> list) {
        for (int i = start; i <= end; i++) {
            boolean exits = false;
            for (Integer n : list) {
                if (n == i) {
                    exits = true;
                    break;
                }
            }
            if (!exits) {return i;}
        }
        return 0;
    }
}
