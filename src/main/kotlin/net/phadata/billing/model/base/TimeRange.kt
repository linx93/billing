package net.phadata.billing.model.base

import io.swagger.annotations.ApiModelProperty

/**
 * 时间范围
 * @author linx
 * @since 2022-06-13 21:07
 */
class TimeRange {
    //时间戳
    @ApiModelProperty(value = "开始时间时间戳")
    var startTime: Long? = null

    //时间戳
    @ApiModelProperty(value = "结束时间时间戳")
    var endTime: Long? = null
}