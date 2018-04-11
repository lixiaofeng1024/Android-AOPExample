package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.annotation.CollectCountMsg;
import org.android10.gintonic.aspect.utils.BroadcastUtils;
import org.android10.gintonic.aspect.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.android10.gintonic.aspect.utils.Constant.POINTCUT_PACKAGE;

@Aspect
public class CollectCountMsgAspect {
    private static final String POINTCUT_METHOD =
            "execution(@" + POINTCUT_PACKAGE + ".CollectCountMsg * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Log.i("weaveJoinPoint", "i");
        sendMsg(joinPoint);
        return result;
    }

    private void sendMsg(ProceedingJoinPoint joinPoint) {
        Method method = ReflectionUtils.getMethod(joinPoint);
        if (method == null) {
            Log.i("weaveJoinPoint", "method == null");
            return;
        }

        CollectCountMsg annotation = method.getAnnotation(CollectCountMsg.class);
        if (annotation != null) {
            BroadcastUtils.sendCountMsg(annotation.target(), method.getName(), annotation.isSuccess(), annotation.description());
        }
    }

}