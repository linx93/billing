package net.phadata.billing.model.auth


/**
 * 请求鉴权中心创建token响应
 * @author linx
 * @since 2022-07-25 16:39
 *
 */
class CreateTokenResponse {
    var expires: String = ""
    var token: String = ""
    override fun toString(): String {
        return "CreateTokenResponse(expires='$expires', token='$token')"
    }
}