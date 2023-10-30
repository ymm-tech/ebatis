package io.manbang.ebatis.core.meta;

import lombok.val;

import java.util.Deque;
import java.util.LinkedList;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class NestNameHolder {
    private static final ThreadLocal<NestNameHolder> holder = ThreadLocal.withInitial(NestNameHolder::new);
    private final Deque<String> namePrefixes = new LinkedList<>();

    public static NestNameHolder get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }

    public void push(String name) {
        namePrefixes.addLast(name);
    }

    public void pop() {
        namePrefixes.pollLast();
    }

    public String prefix() {
        return toString();
    }

    public void ifPresent(Consumer<String> consumer) {
        if (namePrefixes.isEmpty()) {
            return;
        }

        consumer.accept(prefix());
    }

    public String format(String name) {
        if (namePrefixes.isEmpty()) {
            return name;
        } else {
            return prefix() + "." + name;
        }
    }

    @Override
    public String toString() {
        val joiner = new StringJoiner(".");
        namePrefixes.forEach(joiner::add);
        return joiner.toString();
    }
}
