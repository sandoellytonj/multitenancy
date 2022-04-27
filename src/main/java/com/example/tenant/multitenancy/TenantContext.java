package com.example.tenant.multitenancy;

import com.example.tenant.model.api.Base;

public class TenantContext {

    private static final ThreadLocal<Base> context = new ThreadLocal<>();

    public static void set(Base base) {
        context.set(base);
    }

    public static Base get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
