package org.android10.gintonic.aspect.bean;

public class StartMethodInfo {
	private String methodName;
	private long startTime;

	public StartMethodInfo(String methodName, long startTime) {
		this.methodName = methodName;
		this.startTime = startTime;
	}

	public String getMethodName() {
		return methodName;
	}

	public long getStartTime() {
		return startTime;
	}

}
