package com.edou.blog.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName QueryResult
 * @Description 返回值对象
 * @Author 中森明菜
 * @Date 2020/3/30 16:51
 * @Version 1.0
 */
@Data
@ToString
public class QueryResult<T> {
    //数据列表
    private List<T> list;
    //数据总数
    private long totalElements;
    //总页数
    private int totalPages;
    private int number = 0;
}

