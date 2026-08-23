#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Lint the generated Immersive Energistics gallery without Minecraft."""

from __future__ import annotations

import json
from pathlib import Path
import re
import sys

sys.dont_write_bytecode = True
import cases
import generate


ROOT = Path(__file__).resolve().parent
BLOCK_IDS = {"immeng:connector_me", "immeng:connector_me_relay"}
FACINGS = {"down", "up", "north", "south", "east", "west"}


def main() -> int:
    for relative, payload in generate.generated_files().items():
        path = ROOT / relative
        if not path.is_file() or path.read_bytes() != payload:
            raise ValueError(f"generated file differs: {relative}")

    json.loads((ROOT / "datapack/pack.mcmeta").read_text(encoding="utf-8"))
    load_tag = json.loads(
        (ROOT / "datapack/data/minecraft/tags/function/load.json").read_text(
            encoding="utf-8"
        )
    )
    if load_tag != {"values": [f"{cases.NAMESPACE}:load"]}:
        raise ValueError("load tag differs from the exact namespace")

    case_ids = tuple(case.case_id for case in cases.PLACEMENTS)
    if len(case_ids) != 17 or len(set(case_ids)) != len(case_ids):
        raise ValueError("gallery must contain 17 uniquely named block cases")
    for block_id in BLOCK_IDS:
        facings = {
            case.facing
            for case in cases.FACING_PLACEMENTS
            if case.block_id == block_id
        }
        if facings != FACINGS:
            raise ValueError(f"{block_id} must cover all six facing states")

    minimum_x, minimum_y, minimum_z, maximum_x, maximum_y, maximum_z = cases.ENVELOPE
    for case in cases.PLACEMENTS:
        positions = ((case.x, case.y, case.z), case.support)
        if any(
            not (
                minimum_x <= x <= maximum_x
                and minimum_y <= y <= maximum_y
                and minimum_z <= z <= maximum_z
            )
            for x, y, z in positions
        ):
            raise ValueError(f"case escaped the bounded envelope: {case.case_id}")

    if len(cases.WIRE_SPANS) != 2:
        raise ValueError("gallery must contain two manual wire spans")
    if {span.wire_item for span in cases.WIRE_SPANS} != {
        "immeng:wirecoil_me",
        "immeng:wirecoil_me_dense",
    }:
        raise ValueError("gallery must exercise both exact wire-coil items")
    for span in cases.WIRE_SPANS:
        if span.start_case not in cases.PLACEMENT_BY_ID or span.end_case not in cases.PLACEMENT_BY_ID:
            raise ValueError(f"wire span references an unknown endpoint: {span.span_id}")
        if span.length(cases.PLACEMENT_BY_ID) > span.maximum_length:
            raise ValueError(f"wire span exceeds its maximum length: {span.span_id}")

    absent_case = "absent-wire-relay-control"
    if cases.PLACEMENT_BY_ID[absent_case].expected != "stock-visible-no-wire":
        raise ValueError("gallery must retain one absent-wire fallback control")
    wired_cases = {
        case_id
        for span in cases.WIRE_SPANS
        for case_id in (span.start_case, span.end_case)
    }
    if absent_case in wired_cases:
        raise ValueError("absent-wire fallback control must remain isolated")

    function_root = ROOT / f"datapack/data/{cases.NAMESPACE}/function"
    functions = "\n".join(
        path.read_text(encoding="utf-8")
        for path in sorted(function_root.glob("*.mcfunction"))
    )
    placed = re.findall(
        r"^setblock .* immeng:connector_me(?:_relay)?\[",
        functions,
        re.MULTILINE,
    )
    if len(placed) != 17:
        raise ValueError("gallery must place exactly 17 Immersive Energistics blocks")
    if len(re.findall(r"^give @s immeng:wirecoil_me(?:_dense)? 2$", functions, re.MULTILINE)) != 2:
        raise ValueError("wire kit must provide both exact wire-coil items")
    lowered = functions.lower()
    for forbidden in ("summon ", "data merge", "op ", "deop ", "stop "):
        if forbidden in lowered:
            raise ValueError(f"forbidden gallery command: {forbidden}")

    volume = (
        (maximum_x - minimum_x + 1)
        * (maximum_y - minimum_y + 1)
        * (maximum_z - minimum_z + 1)
    )
    if volume > 32_768:
        raise ValueError("gallery clear command exceeds the fill limit")

    print(
        "Immersive Energistics gallery lint passed: 12 facing cases, "
        "2 manual wire spans, 1 absent-wire control"
    )
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (OSError, ValueError) as error:
        print(f"gallery lint failed: {error}", file=sys.stderr)
        raise SystemExit(1)
