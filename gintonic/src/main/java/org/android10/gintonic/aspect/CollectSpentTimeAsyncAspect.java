package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.annotation.CollectSpentTimeAsync;
import org.android10.gintonic.aspect.bean.StartMethodInfo;
import org.android10.gintonic.aspect.utils.BroadcastUtils;
import org.android10.gintonic.aspect.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.android10.gintonic.aspect.utils.Constant.POINTCUT_PACKAGE;

@Aspect
public class CollectSpentTimeAsyncAspect {
    private static final String POINTCUT_METHOD =
            "execution(@" + POINTCUT_PACKAGE + ".CollectSpentTimeAsync * *(..))";

    private static HashMap<String, StartMethodInfo> sStartMethodList = new HashMap<>();

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("weaveJoinPoint", "!!!!!!!!!!");
        Method method = ReflectionUtils.getMethod(joinPoint);
        if (method == null) {
            Log.i("weaveJoinPoint", "method == null");
            return joinPoint.proceed();
        }

        CollectSpentTimeAsync annotation = method.getAnnotation(CollectSpentTimeAsync.class);
        if (annotation == null) {
            Log.i("weaveJoinPoint", "annotation == null");
            return joinPoint.proceed();
        }

        if (annotation.isEndPoint()) {
            Object result = joinPoint.proceed();
            sendMsg(annotation, method.getName());

            return result;
        } else {
            putStartInfo2Map(method, annotation);
            return joinPoint.proceed();
        }
    }

    private void putStartInfo2Map(Method method, CollectSpentTimeAsync annotation) {
//		Type[] a1 = method.getGenericParameterTypes();
//		Type a2 = method.getGenericReturnType();
//		Class<?>[] a3 = method.getParameterTypes();
//		TypeVariable<Method>[] a4 = method.getTypeParameters();
//		Class<?> a5 = method.getReturnType();

        long startTime = System.currentTimeMillis();
        sStartMethodList.put(annotation.target(), new StartMethodInfo(method.getName(), startTime));
    }

    private void sendMsg(CollectSpentTimeAsync annotation, String endName) {
        long endTime = System.currentTimeMillis();
        String target = annotation.target();
        StartMethodInfo startMethodInfo = sStartMethodList.get(target);
        if (startMethodInfo == null) {
            return;
        }

        long startTime = startMethodInfo.getStartTime();
        long spentTime = endTime - startTime;

        String methodName = "startName=" + startMethodInfo.getMethodName() + ",endName=" + endName;
        BroadcastUtils.sendElapsedTime(target, methodName, spentTime);
        sStartMethodList.remove(target);
    }

}