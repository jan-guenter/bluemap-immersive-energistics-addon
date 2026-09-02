/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.adapter.bluemap523;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import io.github.janguenter.bluemap.immeng.activation.AddonRuntime;
import io.github.janguenter.bluemap.immeng.profile.ExactArtifactDetector;
import io.github.janguenter.bluemap.immeng.profile.ImmersiveEnergistics110BetaProfile;

import java.nio.file.Path;
import java.util.Set;

/** Exact-artifact admission and installed IE wire-texture validation. */
final class ProfileResourceExtension implements ResourcePackExtension {

    private final ResourcePack resourcePack;
    private final AddonRuntime runtime;
    private boolean admitted;

    ProfileResourceExtension(ResourcePack resourcePack, AddonRuntime runtime) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        admitted = false;
        if (Boolean.getBoolean("bluemap.immeng.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactArtifactDetector.matchesAll(roots, ImmersiveEnergistics110BetaProfile.ARTIFACTS)) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }
        admitted = true;
    }

    @Override
    public Set<de.bluecolored.bluemap.core.util.Key> collectUsedTextureKeys() {
        return admitted ? Set.of(WireEmitter.WIRE_TEXTURE) : Set.of();
    }

    @Override
    public void bake() {
        if (!admitted) {
            return;
        }
        if (resourcePack.getTextures().get(WireEmitter.WIRE_TEXTURE) == null) {
            runtime.inactive("installed-wire-texture-missing");
            return;
        }
        runtime.activate();
        System.out.println("BlueMap Immersive Energistics add-on active: wire renderer ready.");
    }
}
