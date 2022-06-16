package net.phadata.billing.model.base

import io.swagger.annotations.ApiModelProperty

/**
 * 关键字
 * @author linx
 * @since 2022-06-13 21:07
 */
class Page {
    @ApiModelProperty(value = "页数，默认5",)
    var size: Int = 5

    @ApiModelProperty(value = "页码，从1开始，默认1")
    var current: Int = 1
}