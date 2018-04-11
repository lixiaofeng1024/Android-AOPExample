package org.android10.gintonic.aspect;

import android.util.Log;

import org.android10.gintonic.annotation.CollectSpentTimeSync;
import org.android10.gintonic.aspect.utils.BroadcastUtils;
import org.android10.gintonic.aspect.utils.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.android10.gintonic.aspect.utils.Constant.POINTCUT_PACKAGE;

@Aspect
public class CollectSpentTimeSyncAspect {
	private static final String POINTCUT_METHOD =
			"execution(@" + POINTCUT_PACKAGE + ".CollectSpentTimeSync * *(..))";

	private static final String POINTCUT_CONSTRUCTOR =
			"execution(@" + POINTCUT_PACKAGE + ".CollectSpentTimeSync *.new(..))";

	@Pointcut(POINTCUT_METHOD)
	public void methodAnnotated() {}

	@Pointcut(POINTCUT_CONSTRUCTOR)
	public void constructorAnnotated() {}

	@Around("methodAnnotated() || constructorAnnotated()")
	public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
		Log.i("weaveJoinPoint", "!!!!!!!!!!");

		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long end = System.currentTimeMillis();

		sendMsg(joinPoint, end-start);

		return result;
	}

	private void sendMsg(ProceedingJoinPoint joinPoint, long spentTime) {
		Method method = ReflectionUtils.getMethod(joinPoint);
		if (method == null) {
			Log.i("weaveJoinPoint", "method == null");
			return;
		}

        CollectSpentTimeSync annotation = method.getAnnotation(CollectSpentTimeSync.class);
        if (annotation != null) {
            BroadcastUtils.sendElapsedTime(annotation.target(), method.getName(), spentTime);
        }
	}

}
