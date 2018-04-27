package com.huifenqi.hzf_platform.vo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huifenqi.hzf_platform.context.exception.ErrorMsgCode;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by YDM on 2017/8/14.
 */
public class ApiResult implements Serializable {

    public Status status;
    public JsonObject result;

    public ApiResult() {
        this(String.valueOf(ErrorMsgCode.ERRCODE_OK), "");
    }

    public ApiResult(JsonObject result) {
        this(String.valueOf(ErrorMsgCode.ERRCODE_OK), result);
    }

    /**
     * 返回请求的错误码和错误信息，用于错误的返回，没有数据体
     *
     * @param code 错误码
     * @param description 错误描述
     */
    public ApiResult(String code, String description) {
        this(code, description, new JsonObject());
    }

    /**
     * 返回错误码和数据，针对执行成功的请求返回
     *
     * @param code 错误码
     * @param description 错误结果
     */
    public ApiResult( int code, String description) {
        this(code+"",description );
    }

    /**
     * 返回错误码和数据，针对执行成功的请求返回
     *
     * @param code 错误码
     * @param result 错误结果
     */
    public ApiResult(String code, JsonObject result) {
        this(code, null, result);
    }

    /**
     *
     * @param code 错误码
     * @param description 错误信息
     * @param result 数据体
     */
    public ApiResult(String code, String description, JsonObject result) {
        this.result = result;
        this.status = new Status(code, description);
    }

    public class Status {
        public String code;
        public String description;

        Status(String code, String description) {
            this.code = code;
            this.description = StringUtils.isEmpty(description) ? "" : description;

            if (String.valueOf(ErrorMsgCode.ERRCODE_OK).equals(code) && StringUtils.isEmpty(description)) {
                this.description = "success";
            }
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
