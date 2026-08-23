#!/usr/bin/env python3
# SPDX-License-Identifier: MIT
"""Complete block-state and wire fixtures for the Immersive Energistics gallery."""

from __future__ import annotations

from dataclasses import dataclass
import math


NAMESPACE = "immeng_gallery"
ENVELOPE = (172, 99, 172, 206, 103, 204)
TELEPORT = (188, 110, 188, 0, 35)


@dataclass(frozen=True)
class Placement:
    case_id: str
    label: str
    x: int
    y: int
    z: int
    block_id: str
    facing: str
    expected: str

    @property
    def block_state(self) -> str:
        return f"{self.block_id}[facing={self.facing},waterlogged=false]"

    @property
    def support(self) -> tuple[int, int, int]:
        offsets = {
            "down": (0, -1, 0),
            "up": (0, 1, 0),
            "north": (0, 0, -1),
            "south": (0, 0, 1),
            "east": (1, 0, 0),
            "west": (-1, 0, 0),
        }
        offset_x, offset_y, offset_z = offsets[self.facing]
        return self.x + offset_x, self.y + offset_y, self.z + offset_z


@dataclass(frozen=True)
class WireSpan:
    span_id: str
    label: str
    wire_item: str
    start_case: str
    end_case: str
    maximum_length: int

    def length(self, placements: dict[str, Placement]) -> float:
        start = placements[self.start_case]
        end = placements[self.end_case]
        return math.dist((start.x, start.y, start.z), (end.x, end.y, end.z))


@dataclass(frozen=True)
class Label:
    x: int
    y: int
    z: int
    line_1: str
    line_2: str


def placement(
    case_id: str,
    label: str,
    x: int,
    z: int,
    block_id: str,
    facing: str = "down",
    expected: str = "stock-model-visible",
) -> Placement:
    return Placement(case_id, label, x, 101, z, block_id, facing, expected)


FACING_PLACEMENTS = tuple(
    placement(
        f"{short_name}-{facing}",
        f"{short_name.replace('-', ' ')} facing {facing}",
        176 + index * 4,
        z,
        block_id,
        facing,
    )
    for short_name, block_id, z in (
        ("connector-me", "immeng:connector_me", 176),
        ("connector-me-relay", "immeng:connector_me_relay", 182),
    )
    for index, facing in enumerate(("down", "up", "north", "south", "east", "west"))
)

NETWORK_PLACEMENTS = (
    placement("me-direct-a", "ME direct endpoint A", 176, 190, "immeng:connector_me", expected="wire-endpoint"),
    placement("me-direct-b", "ME direct endpoint B", 188, 190, "immeng:connector_me", expected="wire-endpoint"),
    placement("dense-relay-end", "dense ME relay endpoint", 176, 196, "immeng:connector_me", expected="wire-endpoint"),
    placement("dense-relay-hub", "dense ME relay hub", 190, 196, "immeng:connector_me_relay", expected="wire-relay"),
    placement(
        "absent-wire-relay-control",
        "isolated relay: absent-wire safe fallback",
        204,
        202,
        "immeng:connector_me_relay",
        expected="stock-visible-no-wire",
    ),
)

PLACEMENTS = FACING_PLACEMENTS + NETWORK_PLACEMENTS
PLACEMENT_BY_ID = {case.case_id: case for case in PLACEMENTS}

WIRE_SPANS = (
    WireSpan("me-direct", "normal ME direct connector span", "immeng:wirecoil_me", "me-direct-a", "me-direct-b", 16),
    WireSpan("dense-relay", "dense ME connector-to-relay span", "immeng:wirecoil_me_dense", "dense-relay-end", "dense-relay-hub", 32),
)

LABELS = (
    Label(174, 100, 176, "ME CONNECTOR", "SIX FACINGS"),
    Label(174, 100, 182, "ME RELAY", "SIX FACINGS"),
    Label(174, 100, 190, "ME DIRECT", "12 BLOCKS"),
    Label(174, 100, 196, "DENSE WIRE", "TO RELAY"),
    Label(202, 100, 202, "NO WIRE", "FALLBACK"),
)
