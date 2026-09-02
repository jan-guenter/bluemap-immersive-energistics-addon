# Releasing

Prototype work is intentionally light. Before owner acceptance, run only the
focused Java checks, exact candidate verifier, gallery checks, and disposable
staging comparison needed to get useful visual feedback.

After the owner accepts the candidate:

1. Confirm the renderer and bounded gallery contain no scaffold-only markers.
2. Record the accepted combined integration run and set the provenance status
   to `owner-accepted-release-candidate`.
3. Require the production JAR, sources JAR, POM, and Gradle module metadata to
   match the already sealed `candidate_artifacts` bytes.
4. Run `verifyReleaseCandidate -PreleaseTag=v0.1.0-alpha.2` with all exact
   candidate JAR Gradle properties.
5. Merge the reviewed commit, create an annotated `v0.1.0-alpha.2` tag at that
   commit, and let `.github/workflows/release.yml` publish.
6. Compare every downloaded release asset to the locally accepted bytes.
7. Update the private root portfolio, queue, and `workspace.json` in a separate
   orchestration commit.

The release workflow refuses an unpublished migration status. The tag must
exactly equal `v<addon_version>`. No release authorizes production
deployment.

The command sequence and required release-provenance fields are recorded in
[`EXECUTION.md`](EXECUTION.md).
