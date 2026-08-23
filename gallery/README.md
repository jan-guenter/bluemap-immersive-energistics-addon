# Immersive Energistics block and wire gallery

This compact gallery covers both installed block IDs in all six facing states.
It also provides one normal direct connector span, one dense connector-to-relay
span, and an isolated relay at `(204, 101, 202)` as the absent-wire fallback
control.

```bash
python gallery/generate.py
python gallery/generate.py --check
python gallery/lint.py
bash gallery/package.sh /tmp/immeng-gallery.zip
```

## In-game setup

Install the generated data pack in the disposable test world, reload it, and
run these commands as a player:

```text
/function immeng_gallery:build
/function immeng_gallery:tp
/function immeng_gallery:wire_kit
```

A vanilla data pack cannot create Immersive Engineering's global NeoForge wire
attachment. Use the supplied coils and make the two real, persisted spans by
right-clicking each pair in order:

1. Normal ME coil: `(176, 101, 190)` then `(188, 101, 190)`.
2. Dense ME coil: `(176, 101, 196)` then the relay at `(190, 101, 196)`.

Leave `(204, 101, 202)` unwired, then run `/save-all flush`. The server writes
the same attachment data BlueMap reads. `/function immeng_gallery:release`
removes the staging fixture and its endpoints.

The signed orientation rows are at `z=176` (`connector_me`) and `z=182`
(`connector_me_relay`). Their left-to-right order is `down`, `up`, `north`,
`south`, `east`, `west`. `placements.tsv` and `wires.tsv` are the exact review
manifests.
