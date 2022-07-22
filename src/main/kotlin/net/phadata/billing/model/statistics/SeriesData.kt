package net.phadata.billing.model.statistics

import io.swagger.annotations.ApiModelProperty


/**
 *
 * @author linx
 * @since 2022-07-22 17:05
 *
 */
class SeriesData {
    @ApiModelProperty("某个分类")
    var name: String = ""

    @ApiModelProperty("这个分类对应的数据集")
    var data: List<Double> = listOf()
}