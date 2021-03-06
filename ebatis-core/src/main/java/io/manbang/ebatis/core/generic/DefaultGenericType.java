package io.manbang.ebatis.core.generic;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @author 章多亮
 * @since 2020/6/9 11:31
 */
class DefaultGenericType implements GenericType {
    private static final GenericType NONE = new DefaultGenericType(null);
    private final Type type;

    DefaultGenericType(Type type) {
        this.type = type;
    }

    @Override
    public GenericType as(Class<?> type) { // NOSONAR
        if (this.type == null) {
            return NONE;
        }

        Type currentType = this.type;
        if (type == currentType) {
            return this;
        }

        if (currentType instanceof Class) {
            Type superclass = ((Class<?>) currentType).getGenericSuperclass();

            GenericType genericType = GenericType.forType(superclass);

            if (superclass == type) {
                return genericType;
            }
            genericType = genericType.as(type);
            if (genericType != null) {
                return genericType;
            }

            Type[] genericInterfaces = ((Class<?>) currentType).getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                genericType = GenericType.forType(genericInterface);
                if (genericInterface == type) {
                    return genericType;
                }

                genericType = genericType.as(type);
                if (genericType != null) {
                    return genericType;
                }
            }
        } else if (currentType instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) currentType).getRawType();
            if (rawType == type) {
                return this;
            }
            return GenericType.forType(rawType).as(type);
        } else if (currentType instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) currentType).getGenericComponentType();
            if (genericComponentType == type) {
                return this;
            }
        }
        return NONE;
    }

    @Override
    public Optional<Class<?>> resolveGenericOptional(int... indices) {
        Type currentType = type;

        for (int index : indices) {
            if (currentType instanceof ParameterizedType) {
                currentType = ((ParameterizedType) currentType).getActualTypeArguments()[index];
            } else if (currentType instanceof GenericArrayType) {
                currentType = ((GenericArrayType) currentType).getGenericComponentType();
            } else if (currentType instanceof Class && ((Class<?>) currentType).isArray()) {
                currentType = ((Class<?>) currentType).getComponentType();
            }
        }

        if (currentType instanceof Class) {
            return Optional.of((Class<?>) currentType);
        } else if (currentType instanceof ParameterizedType) {
            return Optional.ofNullable((Class<?>) ((ParameterizedType) currentType).getRawType());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Class<?>> resolveOptional() {
        if (type instanceof Class) {
            return Optional.of((Class<?>) type);
        } else if (type instanceof ParameterizedType) {
            return Optional.ofNullable((Class<?>) ((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            return GenericType.forType(genericComponentType).resolveOptional().map(clazz -> Array.newInstance(clazz, 0).getClass());
        }

        return Optional.empty();
    }

    @Override
    public GenericType resolveType(int... indices) {
        Type currentType = type;

        for (int index : indices) {
            if (currentType instanceof ParameterizedType) {
                currentType = ((ParameterizedType) currentType).getActualTypeArguments()[index];
            } else if (currentType instanceof GenericArrayType) {
                currentType = ((GenericArrayType) currentType).getGenericComponentType();
            }
        }

        return GenericType.forType(currentType);
    }
}
