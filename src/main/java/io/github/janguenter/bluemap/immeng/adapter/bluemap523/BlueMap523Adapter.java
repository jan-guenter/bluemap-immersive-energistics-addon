/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.RenderPassType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.RegistryGuard;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;
import io.github.janguenter.bluemap.immeng.activation.AddonRuntime;

/** BlueMap 5.23 feature-backport registration boundary. */
public final class BlueMap523Adapter {

    private static final AddonRuntime RUNTIME = AddonRuntime.INSTANCE;
    private static final RenderPassType WIRE_RENDER_PASS = new RenderPassType.Impl(
            Key.parse("bluemap_immersive_energistics:wires"),
            (pack, textures, settings) -> new WireRenderPass(pack, textures, RUNTIME)
    );
    private static final ResourcePack.Extension<ProfileResourceExtension> EXTENSION =
            new ResourceExtensionType<>(
                    Key.parse("bluemap_immersive_energistics:exact_profile"),
                    pack -> new ProfileResourceExtension(pack, RUNTIME)
            );

    private BlueMap523Adapter() {
    }

    /** Registers exact admission and the Immersive Energistics wire pass. */
    public static synchronized boolean install() {
        if (!RegistryGuard.canRegister(RenderPassType.REGISTRY, WIRE_RENDER_PASS)
                || !RegistryGuard.canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)) {
            RUNTIME.fail("registry-collision");
            return false;
        }
        if (!RegistryGuard.register(RenderPassType.REGISTRY, WIRE_RENDER_PASS)
                || !RegistryGuard.register(ResourcePack.Extension.REGISTRY, EXTENSION)) {
            RUNTIME.fail("registry-registration-failed");
            return false;
        }
        return true;
    }
}
