/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.immeng.activation.AddonRuntime;

/** Resource extension factory installed before resource-pack construction. */
final class ProfileResourceExtensionType
        implements ResourcePack.Extension<ProfileResourceExtension> {

    private static final Key KEY = Key.parse("bluemap_immersive_energistics:exact_profile");
    private final AddonRuntime runtime;

    ProfileResourceExtensionType(AddonRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public ProfileResourceExtension create(ResourcePack pack) {
        return new ProfileResourceExtension(pack, runtime);
    }
}
