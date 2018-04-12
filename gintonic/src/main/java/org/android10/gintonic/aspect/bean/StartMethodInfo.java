package org.android10.gintonic.aspect.bean;

public class StartMethodInfo {
    private String methodName;
    private long startTime;
    private String description;
    private String tag;

    public StartMethodInfo(String methodName, long startTime,String descrption,String tag) {
        this.methodName = methodName;
        this.startTime = startTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getDescription() {
        return description;
    }

    public String getTag() {
        return tag;
    }
}
