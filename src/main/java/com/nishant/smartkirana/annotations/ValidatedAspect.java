package com.nishant.smartkirana.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidatedAspect {
  private static final HashSet<Object> classesThatCannotValidate =
      new HashSet<>(
          List.of(
              Integer.class,
              Byte.class,
              Boolean.class,
              Short.class,
              Long.class,
              Float.class,
              Double.class,
              Character.class));

  /**
   * intercepts functions annotated with @Validated and validates their params
   *
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  @Around("@annotation(Validated)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] obj = joinPoint.getArgs();
    List<Object> objectsToValidate =
        Arrays.stream(obj)
            .filter(
                (o ->
                    Arrays.stream(o.getClass().getAnnotations())
                        .anyMatch(
                            annotation -> (Validated.class).equals(annotation.annotationType()))))
            .toList();
    objectsToValidate.forEach((object) -> {
      try {
        validateObject(object);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    });
    return joinPoint.proceed();
  }

  /**
   * validates object according to annotations, is called recursively for @Validated annotation
   *
   * @param object
   * @throws IllegalAccessException
   */
  public void validateObject(Object object) throws IllegalAccessException {
    if (classesThatCannotValidate.contains(object.getClass())) {
      throw new IllegalArgumentException("cant do it for wrappers");
    }
    for (Field field : object.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (Arrays.stream(field.getAnnotations())
          .anyMatch(annotation -> (NonBlank.class).equals(annotation.annotationType()))) {

        if (!(String.class).equals(object.getClass())) {
          throw new IllegalArgumentException("NonBlank is only for Strings");
        }

        String value = (String) field.get(object);

        if (value.trim().isEmpty()) {
          throw new IllegalArgumentException("cannot be blank");
        }

      } else if (Arrays.stream(field.getAnnotations())
          .anyMatch(annotation -> (Validated.class).equals(annotation.annotationType()))) {
        validateObject(field.get(object));
      }
    }
  }

  /**
   * not completed yet
   *
   * @param object
   * @param field
   */
  public void validate(Object object, Field field) {
    List<Object> validatedAnnotations = List.of(NonBlank.class);
    List<Object> fieldAnnotations =
        Arrays.stream(field.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList());
    List<Object> annotationsToPerform =
        validatedAnnotations.stream().filter(fieldAnnotations::contains).toList();
  }
}
