package com.easterfg.mae2a.util.strategy;

import com.easterfg.mae2a.util.CableType;

/**
 * @author EasterFG on 2025/4/12
 */
public interface IStrategy {

    /**
     * 消耗特定存储中的内容
     *
     * @param expected  需求的数量
     * @param cableType 线缆类型
     * @return 取出数量
     */
    int consume(int expected, CableType cableType);

    /**
     * 退还物品
     * 
     * @param count     需要退还的数量
     * @param cableType 线缆类型
     * @return 退还成功的数量
     */
    int refund(int count, CableType cableType);
}
