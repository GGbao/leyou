package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 4,max = 32,message = "用户名长度必须在4~32位")
    private String username;// 用户名

    @NotEmpty(message = "密码不能为空")
    @Length(min = 4,max = 32,message = "密码长度必须在4~32位")
    @JsonIgnore
    private String password;// 密码

    @Email
    private String mail;// 邮箱

    private Date created;// 创建时间

    @JsonIgnore
    private String salt;// 密码的盐值
}