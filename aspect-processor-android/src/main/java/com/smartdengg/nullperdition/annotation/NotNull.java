package com.smartdengg.nullperdition.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间:  2017/03/13 15:21 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Retention(RetentionPolicy.RUNTIME) @Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Inherited public @interface NotNull {

  boolean debug() default true;
}
