package io.manbang.ebatis.core.meta;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NestNameHolderTest {

    @Test
    public void push() {
        val holder = new NestNameHolder();
        holder.push("model");
        holder.push("name");

        val name = holder.toString();
        assertEquals("model.name", name);
    }

    @Test
    public void pop() {
        val holder = new NestNameHolder();
        holder.push("model");
        holder.push("name");
        holder.push("last");

        assertEquals("model.name.last", holder.toString());

        holder.pop();
        assertEquals("model.name", holder.toString());
    }
}