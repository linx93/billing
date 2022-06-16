package net.phadata.billing.constant

enum class ResultCode(var code: Int, var msg: String) {
    SUCCESS(200000, "请求成功"),
    FAILED(500000, "服务器错误"),
    BAD_REQUEST(400000, "客户端异常"),
    UNAUTHORIZED(400001, "Unauthorized"),
    METHOD_NOT_ALLOWED(400005, "Method Not Allowed"),
}