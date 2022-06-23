package com.wlb.demo.user.model.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Eric Wang
 * @since 2022/6/22 17:57
 */
@Slf4j
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;
    private String phoneNumber;
    private String sex;
    private String birthday;
}
