package com.nishant.smartkirana.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidatedAspect {
  @Around("@annotation(Validated)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] obj = joinPoint.getArgs();
    List<Object> objectsToValidate =
        Arrays.stream(obj)
            .filter(
                (o ->
                    Arrays.stream(o.getClass().getAnnotations())
                        .anyMatch(
                            annotation -> annotation.annotationType().equals(Validated.class))))
            .toList();
    for (Object object : objectsToValidate) {
      validateObject(object);
    }
    return joinPoint.proceed();
  }

  public void validateObject(Object object) throws IllegalAccessException {
    if (List.of(
            Integer.class,
            Byte.class,
            Boolean.class,
            Short.class,
            Long.class,
            Float.class,
            Double.class,
            Character.class)
        .contains(object.getClass())){
      throw new IllegalArgumentException("cant do it for wrappers");
    }
    for (Field field : object.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (Arrays.stream(field.getAnnotations())
          .anyMatch(annotation -> annotation.annotationType().equals(NonBlank.class))) {
        String value = (String) field.get(object);
        if (value.trim().isEmpty()) {
          throw new IllegalArgumentException("cannot be blank");
        }
      }
      else if(Arrays.stream(field.getAnnotations())
              .anyMatch(annotation -> annotation.annotationType().equals(Validated.class))){
        validateObject(field.get(object));
      }
    }
  }
  public void validate(Object object, Field field){
    List<Object> validatedAnnotations = List.of(NonBlank.class);
    List<Object> fieldAnnotations = Arrays.stream(field.getAnnotations()).map(Annotation::annotationType).collect(Collectors.toList());
    List<Object> annotationsToPerform = validatedAnnotations.stream().filter(fieldAnnotations::contains).toList();
  }
}
