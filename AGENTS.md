# Agent guide for the Immersive Energistics BlueMap add-on

This is an independent public add-on repository generated from the private
All the Mons orchestration scaffold. Read this file and `README.md` before
changing it.

## Exact baseline

- All the Mons `1.2.0`, pack commit `c7bb230f21d14d26859d0b92548f089b3a493ad9`
- Minecraft `1.21.1`
- NeoForge `21.1.248`
- Java `21`
- BlueMap `5.22-feature.backport-5.23-stateless-java-web-server-46`, commit `7e07f4e74ec1e92a6ead9aa1e66054af3e133aac`
- BlueMap API commit `285c9a60eff3ac2b0cab308ce1058d1565be0971`
- Adapter API `0.1.0-alpha.3`, commit
  `2f8bb7aa3d907f837ebda45c019e0498ed7e89af`, source tree
  `c0b5db049f6a7630ea589c7f2e658133ed8e2983`
- Exact profile `immeng-1.1.0-beta`

This is a standalone BlueMap add-on, not a NeoForge mod. Do not add client
classes, candidate binaries/assets/source, nested JARs, Minecraft classes,
Mixins, or world state.

## Development contract

- Preserve stock rendering while the runtime/profile is absent, duplicated,
  unsupported, malformed, disabled, or not yet implemented.
- Keep the BlueMap internal API behind `adapter/bluemap523`.
- Compile exactly the five pinned Adapter API helpers as source. Never install,
  bundle, or nest its standalone module JAR.
- Keep exact candidate identities and resource contracts in the profile.
- Keep state/NBT decoding, normalized data, and mesh emission separate.
- Unknown family data gets one bounded diagnostic and stock fallback.
- Use installed resources only after exact-artifact admission.
- Gallery cases and renderer facts are family-owned; do not move them back to
  the generic scaffold.

Generated not-implemented markers are permitted only during the fast prototype
phase. The release gate rejects them.

## Commands

Compile and test the safe seed:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
gradle --no-daemon -PbluemapSourcePath=/path/to/BlueMap-at-7e07f4e7 \
  clean check build
```

Verify a prototype with exact candidate JAR properties:

- `-PimmersiveEnergisticsJar=/path/to/Immersive-Energistics-1.1.0-beta.jar`
- `-PimmersiveEngineeringJar=/path/to/ImmersiveEngineering-1.21.1-12.4.2-194.jar`
- `-PappliedEnergisticsJar=/path/to/appliedenergistics2-19.2.17.jar`

Pass those properties to Gradle and run `prototypeCheck`. The owner-accepted
`0.1.0-alpha.3` migration candidate is sealed under `candidate_artifacts` and
must pass `verifyReleaseCandidate -PreleaseTag=v0.1.0-alpha.3`. Follow
`docs/EXECUTION.md` for the acceptance and publication sequence.

Never stage or commit generated build output, candidate JARs, galleries, worlds,
credentials, logs, or research evidence.
