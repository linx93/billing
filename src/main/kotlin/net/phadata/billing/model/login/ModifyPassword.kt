package net.phadata.billing.model.login

import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank


/**
 * 修改密码
 * @author linx
 * @since 2022-07-22 10:34
 *
 */
class ModifyPassword {
   /* @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码")
    var oldPassword: String = ""*/

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码")
    var newPassword: String = ""

    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码")
    var confirmPassword: String = ""
}