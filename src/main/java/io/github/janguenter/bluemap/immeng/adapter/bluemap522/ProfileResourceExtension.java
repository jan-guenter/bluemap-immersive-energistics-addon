/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import io.github.janguenter.bluemap.immeng.activation.AddonRuntime;
import io.github.janguenter.bluemap.immeng.profile.ExactArtifactDetector;
import io.github.janguenter.bluemap.immeng.profile.ImmersiveEnergistics110BetaProfile;

import java.nio.file.Path;

/** Exact-artifact admission hook; family routing deliberately remains stock. */
final class ProfileResourceExtension implements ResourcePackExtension {

    private final ResourcePack resourcePack;
    private final AddonRuntime runtime;

    ProfileResourceExtension(ResourcePack resourcePack, AddonRuntime runtime) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.immeng.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactArtifactDetector.matchesAll(roots, ImmersiveEnergistics110BetaProfile.ARTIFACTS)) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }

        // SCAFFOLD_NOT_IMPLEMENTED: validate installed resources, register the
        // family renderer, route only owned hosts, then call runtime.activate().
        if (resourcePack.getBlockStates() == null) {
            runtime.fail("resource-pack-unavailable");
            return;
        }
        runtime.inactive("family-renderer-not-implemented");
    }
}
