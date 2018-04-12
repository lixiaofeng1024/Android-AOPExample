package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.annotation.CollectValueMsg;
import org.android10.gintonic.annotation.Tag;
import org.android10.gintonic.annotation.Value;
import org.android10.gintonic.aspect.utils.BroadcastUtils;
import org.android10.gintonic.aspect.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        sendMsg(joinPoint);
        return result;
    }

    private void sendMsg(ProceedingJoinPoint joinPoint) {
        Method method = ReflectionUtils.getMethod(joinPoint);
        if (method == null) {
            Log.i(TAG, "method == null");
            return;
        }

        CollectValueMsg annotation = method.getAnnotation(CollectValueMsg.class);
        if (annotation != null) {
            Annotation parameterAnnotations[][] = method.getParameterAnnotations();
            int valueIndex = -1;
            List<Annotation> tagAnnotations = new ArrayList<>();
            List<Integer> tagIndexes = new ArrayList<>();
            for (Annotation[] annotations : parameterAnnotations) {
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i].getClass().equals(Value.class)) {
                        valueIndex = i;
                    }
                    if (annotations[i].getClass().equals(Tag.class)) {
                        tagAnnotations.add(annotations[i]);
                        tagIndexes.add(i);
                    }
                }
            }

            if (valueIndex == -1) {
                return;
            }

            Object[] args = joinPoint.getArgs();
            StringBuilder stringBuilder = new StringBuilder();
            float value = (float) args[valueIndex];
            for (int i = 0; i < tagAnnotations.size(); i++) {
                String k = ((Tag) tagAnnotations.get(i)).name();
                String v = (String) args[i];
                stringBuilder.append("<" + k + "," + v + ">" + (i == tagAnnotations.size() - 1 ? "" : "\n"));
            }
            String tag = stringBuilder.toString();

            BroadcastUtils.sendValueMsg(annotation.target(), method.getName(), value, annotation.description(), tag);
        }
    }
}

