package net.phadata.billing.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 订单记录表
 * </p>
 *
 * @author linx
 * @since 2022-06-13
 */
@ApiModel(value = "OrderRecords对象", description = "订单记录表")
class OrderRecords : Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    var id: Long? = null

    @ApiModelProperty(value = "订单id")
    var orderId: String? = null

    @ApiModelProperty(value = "平台名称")
    var platformName: String? = null

    @ApiModelProperty(value = "平台代码")
    var platformCode: String? = null

    @ApiModelProperty(value = "创建时间")
    var createTime: Long? = null

    @ApiModelProperty(value = "更新时间")
    var updateTime: Long? = null

    @ApiModelProperty(value = "金额")
    var amount: BigDecimal? = null

    @ApiModelProperty(value = "描述信息")
    var descInfo: String? = null

    @ApiModelProperty(value = "支付时间")
    var payTime: Long? = null

    @ApiModelProperty(value = "客户名称")
    var consumerName: String? = null

    @ApiModelProperty(value = "客户code或者说是客户id，客户的唯一标识")
    var consumerCode: String? = null

    @ApiModelProperty(value = "开票状态[0：未开票、1：开票中、2：已开票]")
    var billingStatus: Int? = null

    @ApiModelProperty(value = "票据状态通知地址")
    var notifyUrl: String? = null

    @ApiModelProperty(value = "票据状态通知状态[0:未通知 1通知成功 2:通知失败]")
    var notifyStatus: Int? = null

    @ApiModelProperty(value = "票据地址")
    var billingUrl: String? = null

    @ApiModelProperty(value = "客户企业社会统一信用代码，开普票的时候使用")
    var consumerCompanyCode: String? = null

    @ApiModelProperty(value = "支付类型")
    var payType: String? = null


    override fun toString(): String {
        return "OrderRecords{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", platformName=" + platformName +
                ", platformCode=" + platformCode +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", amount=" + amount +
                ", descInfo=" + descInfo +
                ", payTime=" + payTime +
                ", consumerName=" + consumerName +
                ", consumerCode=" + consumerCode +
                ", billingStatus=" + billingStatus +
                ", notifyUrl=" + notifyUrl +
                ", billingUrl=" + billingUrl +
                ", consumerCompanyCode=" + consumerCompanyCode +
                ", payType=" + payType +
                "}"
    }
}
