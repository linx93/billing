package net.phadata.billing.model.statistics

import io.swagger.annotations.ApiModelProperty


/**
 * 环形图
 * @author linx
 * @since 2022-07-19 15:45
 *
 */
class DonutChart {
    @ApiModelProperty(value = "值")
    var value: Double = 0.0

    @ApiModelProperty(value = "项")
    var name: String = ""
}