package com.edou.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Response
 * @Description 返回对象
 * @Author 中森明菜
 * @Date 2020/3/19 12:42
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    //是否响应成功
    private boolean success;
    //响应信息
    private String message;
    //响应数据
    private Object body;

    public Response(boolean success,String message){
        this.success = success;
        this.message = message;
    }
}
