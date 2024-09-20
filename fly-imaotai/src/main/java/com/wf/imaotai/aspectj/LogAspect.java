package com.wf.imaotai.aspectj;

import cn.hutool.core.util.StrUtil;
import com.wf.imaotai.annotation.ApiResource;
import com.wf.imaotai.entity.Log;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Value("${spring.application.name:}")
    private String springApplicationName;


    /**
     * 切所有的controller包
     */
    @Pointcut("execution(* *..controller..*(..))")
    public void webLog() {

    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "webLog()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        boolean ensureMakeLog = this.ensureMakeLog(joinPoint);
        if (!ensureMakeLog) {
            return;
        }
        // 获取接口上@GetMapper等的name属性
        Map<String, Object> annotationProp = getAnnotationProp(joinPoint);

        handleLog(joinPoint, annotationProp, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        boolean ensureMakeLog = this.ensureMakeLog(joinPoint);
        if (!ensureMakeLog) {
            return;
        }
        // 获取接口上@GetMapper等的name属性
        Map<String, Object> annotationProp = getAnnotationProp(joinPoint);

        handleLog(joinPoint, annotationProp, e, null);
    }

    /**
     * AOP获取 @GetMapping等 的属性信息
     *
     * @param joinPoint joinPoint对象
     * @return 返回K, V格式的参数，key是参数名称，v是参数值
     */
    private Map<String, Object> getAnnotationProp(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 通过map封装参数和参数值，key参数名，value是参数值
        Map<String, Object> propMap = new HashMap<>(2);

        // 获取接口上的@GetMapping等的name属性 填充到map
        ApiResource apiResource = method.getDeclaringClass().getAnnotation(ApiResource.class);

        for (Annotation annotation : method.getAnnotations()) {
            //若是 spring 的请求注解
            if (annotation.toString().contains("Mapping(")) {
                // 填充其他属性
                String name = invokeAnnotationMethod(annotation, "name", String.class);
                propMap.put("log_content", StringUtils.isEmpty(name) ? "" : name);
            }
        }

        propMap.put("app_name", apiResource != null && StrUtil.isNotBlank(apiResource.appCode()) ? apiResource.appCode()
                : springApplicationName);

        return propMap;
    }


    protected void handleLog(final JoinPoint joinPoint, Map<String, Object> annotationProp, final Exception e, Object jsonResult) {

        try {
            // *========数据库日志=========*//
            Log operLog = new Log();
            //设置appcode
            operLog.setAppName(annotationProp.get("app_name").toString());
            operLog.setLogName("API接口日志记录");
            operLog.setContent(annotationProp.get("log_content").toString());

            operLog.setStatus(0);

            if (e != null) {
                operLog.setStatus(0);
                operLog.setContent(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");

            // 处理设置注解上的参数 app name那些
            getControllerMethodDescription(joinPoint, operLog, jsonResult);
            operLog.setCreateTime(new Date());

        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log operLog, Object jsonResult) throws Exception {

        // 保存request，参数和值,获取参数的信息，传入到数据库中。
        setRequestValue(joinPoint, operLog);

        //保存response，参数和值
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, Log operLog) throws Exception {
        String requestMethod = operLog.getRequestMethod();
    }

    /**
     * 确定当前接口是否需要记录日志
     * 参考：https://gitee.com/stylefeng/guns
     */
    private boolean ensureMakeLog(JoinPoint point) {
        // 判断是否需要记录日志，如果不需要直接返回
        // 如果开关开启
        return true;
    }

    /**
     * 调用注解上的某个方法，并获取结果
     */
    private <T> T invokeAnnotationMethod(Annotation apiResource, String methodName, Class<T> resultType) {
        try {
            Class<? extends Annotation> annotationType = apiResource.annotationType();
            Method method = annotationType.getMethod(methodName);
            return (T) method.invoke(apiResource);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

        }
        return null;
    }
}
