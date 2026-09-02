/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.adapter.bluemap523;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

class RegistrationPlanContractTest {

    @Test
    void preservesRegistrationOrderAndFailurePolicy() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/io/github/janguenter/bluemap/immeng/adapter/bluemap523/BlueMap523Adapter.java"
        ));
        int pass = source.indexOf(".add(RenderPassType.REGISTRY, WIRE_RENDER_PASS)");
        int extension = source.indexOf(".add(ResourcePack.Extension.REGISTRY, EXTENSION)");
        int preflight = source.indexOf("if (!REGISTRATIONS.canApply())");
        int collision = source.indexOf("RUNTIME.fail(\"registry-collision\")");
        int apply = source.indexOf("if (!REGISTRATIONS.apply())");
        int registrationFailure = source.indexOf("RUNTIME.fail(\"registry-registration-failed\")");

        assertTrue(pass >= 0 && pass < extension);
        assertTrue(preflight >= 0 && preflight < collision);
        assertTrue(apply > collision && apply < registrationFailure);
    }
}
