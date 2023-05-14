package com.thinkstu.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BistuLoginEntity {
    private String loginUrl;
    private String needcaptchaUrl;
    private String captchaUrl;
}
