package net.phadata.billing.constant

/**
 * <p>
 * 响应code
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
enum class ResultCode(var code: String, var msg: String) {
    SUCCESS("200000", "请求成功"),


    //客户端
    BAD_REQUEST("400000", "客户端异常"),
    UNAUTHORIZED("400001", "Unauthorized"),
    METHOD_NOT_ALLOWED("400005", "Method Not Allowed"),
    PARAMETER_VALIDATION_ERROR("400006", "参数校验错误"),

    DATA_DOES_NOT_EXIST("400010", "数据不存在"),
    DATA_ALREADY_EXISTS("400011", "数据已存在"),
    DATA_IS_NOT_UNIQUE("400012", "数据不唯一，存在多条数据"),

    //服务端
    FAILED("500000", "服务器错误"),
}