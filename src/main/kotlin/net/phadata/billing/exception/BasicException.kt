package net.phadata.billing.exception

import net.phadata.billing.constant.ResultCode


/**
 * 自定义基础异常
 * @author linx
 * @since 2022-06-13 22:25
 *
 */
abstract class BasicException : RuntimeException {
    var code: Int = ResultCode.FAILED.code
        protected set

    constructor()
    constructor(s: String?) : super(s)
    constructor(s: String?, throwable: Throwable?) : super(s, throwable)
    constructor(throwable: Throwable?) : super(throwable)

    constructor(code: Int, msg: String) : super(msg) {
        this.code = code
    }

    constructor(resultCode: ResultCode) : super(resultCode.msg) {
        code = resultCode.code
    }

    constructor(resultCode: ResultCode, e: Throwable) : super(resultCode.msg, e) {
        code = resultCode.code
    }
}