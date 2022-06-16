package net.phadata.billing.exceptionimport net.phadata.billing.constant.ResultCode/** * 服务器自定义异常 * @author linx * @since 2022-06-13 21:07 * */open class ServiceException : BasicException {    constructor() : super(ResultCode.FAILED)    constructor(msg: String) : super(ResultCode.FAILED.code, msg)    constructor(code: Int, msg: String) : super(code, msg) {    }    constructor(resultCode: ResultCode) : super(resultCode)    constructor(resultCode: ResultCode, e: Throwable) : super(resultCode, e)}