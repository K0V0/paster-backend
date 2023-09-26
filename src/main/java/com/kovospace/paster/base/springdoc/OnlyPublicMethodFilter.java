package com.kovospace.paster.base.springdoc;

import com.kovospace.paster.base.annotations.swagger.PublicEndpoint;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class OnlyPublicMethodFilter implements OpenApiMethodFilter {

    @Override
    public boolean isMethodToInclude(Method method) {
        return method.isAnnotationPresent(PublicEndpoint.class);
    }
}
