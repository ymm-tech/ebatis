package io.manbang.ebatis.core.extension;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public final class OperationExtensionRegistry {
    private static final Map<String, OperationExtension<?, ?>> EXTENSIONS = new ConcurrentHashMap<>();

    static {
        for (OperationExtension<?, ?> extension : ServiceLoader.load(OperationExtension.class)) {
            register(extension);
        }
    }

    private OperationExtensionRegistry() {
    }

    public static void register(OperationExtension<?, ?> extension) {
        if (extension == null || extension.name() == null || extension.name().trim().isEmpty()) {
            throw new IllegalArgumentException("operation extension and name must not be empty");
        }
        OperationExtension<?, ?> previous = EXTENSIONS.putIfAbsent(extension.name(), extension);
        if (previous != null && previous != extension) {
            throw new IllegalStateException("Operation extension already registered: " + extension.name());
        }
    }

    public static OperationExtension<?, ?> required(String name) {
        OperationExtension<?, ?> extension = EXTENSIONS.get(name);
        if (extension == null) {
            throw new IllegalStateException("Operation extension is not registered: " + name);
        }
        return extension;
    }

    public static void unregister(String name) {
        EXTENSIONS.remove(name);
    }
}
