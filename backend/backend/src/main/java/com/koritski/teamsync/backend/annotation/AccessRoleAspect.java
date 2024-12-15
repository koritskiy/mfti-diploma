package com.koritski.teamsync.backend.annotation;

import com.koritski.teamsync.backend.entity.RoleEnum;
import com.koritski.teamsync.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.koritski.teamsync.backend.constants.AuthConstants.*;


@Aspect
@Component
@RequiredArgsConstructor
public class AccessRoleAspect {
    private final JwtUtil jwtUtil;

    @Around(value = "@annotation(accessRole)")
    public Object checkRoles(ProceedingJoinPoint proceedingJoinPoint,
                             AccessRole accessRole) throws Throwable {
        MethodSignature methodSignature
                = (MethodSignature) proceedingJoinPoint.getSignature();

        Object[] args = proceedingJoinPoint.getArgs();
        String[] parametersName = methodSignature.getParameterNames();

        int idx = Arrays.asList(parametersName)
                .indexOf(accessRole.tokenParamName());
        String token = args[idx].toString();

        Claims claims = jwtUtil.getClaims(token);

        boolean isActionAllowed = false;
        for (int i = 0; i < accessRole.role().length; i++) {
            if (accessRole.role()[i] == RoleEnum.ADMIN
                    && claims.get(IS_ADMIN_ROLE_EXIST, Boolean.class)) {
                isActionAllowed = true;
            } else if (accessRole.role()[i] == RoleEnum.MODERATOR
                    && claims.get(IS_MODERATOR_ROLE_EXIST, Boolean.class)) {
                isActionAllowed = true;
            }
        }

        if (!isActionAllowed) {
            throw new RuntimeException(PERMISSION_DESCRIPTION);
        }

        return proceedingJoinPoint.proceed();
    }
}
