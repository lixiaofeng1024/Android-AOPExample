package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.annotation.CollectValueMsg;
import org.android10.gintonic.aspect.utils.BroadcastUtils;
import org.android10.gintonic.aspect.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.android10.gintonic.aspect.utils.Constant.POINTCUT_PACKAGE;

/**
 * @author LiXiaoFeng
 * @date 2018/4/4
 */
public class CollectValueMsgAspect {
    private static String TAG = CollectValueMsgAspect.class.getSimpleName();
    private static final String POINTCUT_METHOD =
            "execution(@" + POINTCUT_PACKAGE + ".CollectValueMsg * *(..)";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        sendMsg(joinPoint, result);
        return result;
    }

    private void sendMsg(ProceedingJoinPoint joinPoint, Object result) {
        Method method = ReflectionUtils.getMethod(joinPoint);
        if (method == null) {
            Log.i(TAG, "method == null");
            return;
        }

        CollectValueMsg annotation = method.getAnnotation(CollectValueMsg.class);
        float value = (float) result;
        if (annotation != null) {
            BroadcastUtils.sendValueMsg(annotation.target(), method.getName(), value, annotation.description());
        }
    }
}

