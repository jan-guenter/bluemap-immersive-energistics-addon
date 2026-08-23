/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.immeng.profile;

import java.util.List;

/** Exact All the Mons 1.2.0 profile `immeng-1.1.0-beta`. */
public final class ImmersiveEnergistics110BetaProfile {

    public static final String PROFILE_ID = "immeng-1.1.0-beta";
    public static final List<ArtifactPin> ARTIFACTS = List.of(
            new ArtifactPin(
                    "immersiveEnergistics",
                    "immeng",
                    "1.1.0-beta",
                    "Immersive-Energistics-1.1.0-beta.jar",
                    40_560L,
                    "389b6671058915761d5e897a624055adc78e4680180c521994e39a0ef4e7c79b"
            ),
            new ArtifactPin(
                    "immersiveengineering",
                    "immersiveengineering",
                    "12.4.2-194",
                    "ImmersiveEngineering-1.21.1-12.4.2-194.jar",
                    14_232_121L,
                    "45942985a4a4aebf265b8e22a0c54a96208637471f36f2532ff5d4911322debc"
            ),
            new ArtifactPin(
                    "appliedenergistics2",
                    "ae2",
                    "19.2.17",
                    "appliedenergistics2-19.2.17.jar",
                    8_230_896L,
                    "460d779a0609b81409907d9956de8f6f70a1b0912257e3e5c3c7e75ac9630e95"
            )
    );

    private ImmersiveEnergistics110BetaProfile() {
    }
}
