# Add-on execution

This repository starts inactive and stock-safe. Implement only the smallest
observed Immersive Energistics rendering defect before staging.

Before running Gradle gates, activate a Python 3.11 or newer virtual
environment, initialize both exact source submodules, and install the exact
development-only toolkit into it:

```bash
git submodule update --init --recursive -- \
  tooling/bluemap-addon-toolkit modules/bluemap-addon-adapter-api
python -m pip install --disable-pip-version-check --no-deps \
  --require-hashes --only-binary=:all: \
  --requirement requirements/toolkit.txt
```

## Prototype

Acquire and verify the exact candidate JARs outside Git. Their Gradle
properties are:

- `-PimmersiveEnergisticsJar=/path/to/Immersive-Energistics-1.1.0-beta.jar`
- `-PimmersiveEngineeringJar=/path/to/ImmersiveEngineering-1.21.1-12.4.2-194.jar`
- `-PappliedEnergisticsJar=/path/to/appliedenergistics2-19.2.17.jar`

Then run:

```bash
gradle --no-daemon -PbluemapSourcePath=/path/to/BlueMap-at-7e07f4e7 \
  <exact-candidate-properties> clean prototypeCheck build
bash gallery/package.sh /tmp/immeng-gallery.zip
```

Deploy that JAR and gallery only to disposable staging, verify the intended
BlueMap link loads, and compare it with the matching client. Iterate from
observed defects until the owner explicitly accepts one exact staging JAR.

## Acceptance and release

The migration candidate records the production JAR, sources JAR, POM, and
Gradle module identities under `candidate_artifacts`. After visual acceptance,
change the provenance status to `owner-accepted-release-candidate` and record
the accepted combined integration run without changing those artifact bytes.

Run the exact candidate through a pull request with all exact inputs:

```bash
gradle --no-daemon -PbluemapSourcePath=/path/to/BlueMap-at-7e07f4e7 \
  <exact-candidate-properties> -PreleaseTag=v0.1.0-alpha.3 \
  clean build generatePomFileForAddonPublication \
  generateMetadataFileForAddonPublication verifyReleaseCandidate
```

Merge only after owner acceptance and final-head CI passes this gate. Create an annotated
`v<version>` tag at reviewed `main`; the release workflow independently checks
the tag, exact BlueMap checkout, accepted bytes and draft assets before making
the prerelease public. Publication never deploys to production.
