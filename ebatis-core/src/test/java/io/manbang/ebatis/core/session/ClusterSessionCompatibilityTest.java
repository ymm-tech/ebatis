package io.manbang.ebatis.core.session;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class ClusterSessionCompatibilityTest {
    @Test
    public void addedOperationsMustRemainDefaultForExistingImplementations() throws Exception {
        assertDefault("countAsync");
        assertDefault("documentExistsAsync");
        assertDefault("getSourceAsync");
        assertDefault("openPointInTimeAsync");
        assertDefault("closePointInTimeAsync");
        assertDefault("reindexAsync");
        assertDefault("taskResultAsync");
    }

    private void assertDefault(String methodName) {
        for (Method method : ClusterSession.class.getMethods()) {
            if (method.getName().equals(methodName)) {
                assertTrue(methodName + " must be a default method", method.isDefault());
                return;
            }
        }
        throw new AssertionError("Missing method: " + methodName);
    }
}
