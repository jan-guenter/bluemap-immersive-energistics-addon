# BlueMap Immersive Energistics Add-on

A Java 21 BlueMap add-on for the exact `immeng-1.1.0-beta` profile in All the Mons
`1.2.0` / Minecraft `1.21.1`.

Status: implemented staging candidate awaiting owner visual review. BlueMap
keeps the two connector blocks on their installed stock models and adds static
geometry for persisted `me` and `me_dense` wire spans. Ordinary Immersive
Engineering wires are deliberately ignored so the two add-ons can be installed
together without duplicate geometry.

## Build

```bash
gradle --no-daemon \
  -PbluemapSourcePath=../bluemap-backport \
  -PimmersiveEnergisticsJar=/path/to/Immersive-Energistics-1.1.0-beta.jar \
  -PimmersiveEngineeringJar=/path/to/ImmersiveEngineering-1.21.1-12.4.2-194.jar \
  -PappliedEnergisticsJar=/path/to/appliedenergistics2-19.2.17.jar \
  clean prototypeCheck build
```

`check` is the quick Java/checkstyle/archive gate. `prototypeCheck` additionally
requires every exact candidate JAR property and validates the complete compact
gallery. See `provenance/upstreams.json` for immutable artifact identities and
the [execution guide](docs/EXECUTION.md) for the prototype-to-release loop.

## Install

Place the production JAR in BlueMap's add-on pack directory and restart the
BlueMap JVM. Removal plus one restart restores stock behavior; the add-on
creates no custom world state.

Set `-Dbluemap.immeng.disabled=true` to leave the exact profile inactive.

## Scope boundary

The add-on reads the exact IE world attachment and installed IE wire texture
only after admitting the exact Immersive Energistics, Immersive Engineering,
and AE2 artifacts. Missing, malformed, internal, unknown, and ordinary IE wire
records emit no custom geometry. Live AE2 state, activity overlays, particles,
and animation remain out of scope.

No Immersive Energistics binary, source, class, asset, captured mesh, or gallery is
bundled in the add-on.
