package net.phadata.billing.model.base

import io.swagger.annotations.ApiModelProperty

/**
 * 关键字
 * @author linx
 * @since 2022-06-13 21:07
 */
open class Keyword {
    @ApiModelProperty(value = "关键字")
    var keyword: String? = null
}