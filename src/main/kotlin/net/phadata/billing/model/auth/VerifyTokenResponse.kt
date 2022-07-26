package net.phadata.billing.model.auth


/**
 * 验证token的响应
 * @author linx
 * @since 2022-07-25 17:28
 *
 */
class VerifyTokenResponse {
    var verify: Boolean = false
    var err: String = ""
}