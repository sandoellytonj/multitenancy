package com.example.tenant.config;

import com.example.tenant.model.api.Base;
import com.example.tenant.multitenancy.TenantContext;
import com.example.tenant.service.TenantService;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements AsyncHandlerInterceptor {

    public static final String TENANT_HEADER = "Tenant";

    private final TenantService tenantService;

    public TenantInterceptor(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        TenantContext.clear();

        /*if (!request.getServletPath().startsWith(PathConstantes.API_PATH)
                || request.getServletPath().startsWith(PathConstantes.API_PATH_CONFIG)) {
            return true;
        }*/

        String tenant = request.getHeader(TENANT_HEADER);
        boolean tenantSet = false;

        if (StringUtils.isEmpty(tenant)) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Tenant não informado\"}");
            response.getWriter().flush();

        } else {
            try {
                Base base = tenantService.retornaCiclos(tenant);
                TenantContext.set(base);
                tenantSet = true;
            } catch (ResponseStatusException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\": \"Tenant informado inexistente\"}");
                response.getWriter().flush();
            }
        }


        return tenantSet;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) {
        TenantContext.clear();
    }

}

