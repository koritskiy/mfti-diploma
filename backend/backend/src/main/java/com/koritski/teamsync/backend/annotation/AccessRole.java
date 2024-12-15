package com.koritski.teamsync.backend.annotation;


import com.koritski.teamsync.backend.entity.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки роли пользователя
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessRole {
    /**
     * Список ролей, которые могут обращаться к определенному API
     *
     * @return передаются значения из {@code RoleEnum}
     */
    RoleEnum[] role();

    /**
     * Определяется название параметра токена авторизации в контроллере.
     * По умолчанию равен {@code token}, поле необязательно
     *
     * @return название параметра токена авторизации в методе
     */
    String tokenParamName() default "token";
}
