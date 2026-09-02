/* SPDX-License-Identifier: MIT */

package io.github.janguenter.bluemap.immeng.adapter.bluemap523;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class WireAttachmentReaderTest {

    @Test
    void normalizesOnlyImmersiveEnergisticsWires() {
        WireNetworkData.Wire me = wire(
                new int[]{174, 106, 56},
                List.of(0.5D, 0.484375D, 0.5D),
                new int[]{160, 104, 56},
                List.of(0.5D, 0.484375D, 0.5D),
                (byte) 0,
                "me"
        );
        WireNetworkData.Wire reverseDuplicate = wire(
                new int[]{160, 104, 56},
                List.of(0.5D, 0.484375D, 0.5D),
                new int[]{174, 106, 56},
                List.of(0.5D, 0.484375D, 0.5D),
                (byte) 0,
                "ME"
        );
        WireNetworkData.Wire dense = wire(
                new int[]{160, 104, 60}, List.of(0.5D, 0.484375D, 0.5D),
                new int[]{174, 104, 60}, List.of(0.5D, 0.484375D, 0.5D),
                (byte) 0, "me_dense"
        );
        WireNetworkData.Wire ordinaryIe = wire(
                new int[]{160, 104, 64}, List.of(0.5D, 0.5D, 0.5D),
                new int[]{174, 104, 64}, List.of(0.5D, 0.5D, 0.5D),
                (byte) 0, "COPPER"
        );
        WireNetworkData.Wire internal = wire(
                new int[]{1, 2, 3}, List.of(0.5D, 0.5D, 0.5D),
                new int[]{2, 2, 3}, List.of(0.5D, 0.5D, 0.5D),
                (byte) 1, "me"
        );

        List<WireSpan> spans = WireAttachmentReader.normalize(root(List.of(
                me, reverseDuplicate, dense, ordinaryIe, internal
        )));

        assertEquals(2, spans.size());
        WireSpan span = spans.get(0);
        assertEquals(WireSpan.WireKind.ME, span.kind());
        assertEquals(new WireSpan.Point(160.5D, 104.484375D, 56.5D), span.start());
        assertEquals(new WireSpan.Point(174.5D, 106.484375D, 56.5D), span.end());
        assertEquals(WireSpan.WireKind.ME_DENSE, spans.get(1).kind());
    }

    @Test
    void recognizesOnlyTheTwoOwnedPersistedNamesAndProperties() {
        assertEquals(WireSpan.WireKind.ME, WireSpan.WireKind.parse(" me "));
        assertEquals(WireSpan.WireKind.ME_DENSE, WireSpan.WireKind.parse("ME_DENSE"));
        assertNull(WireSpan.WireKind.parse("COPPER"));
        assertNull(WireSpan.WireKind.parse("unknown"));
        assertNull(WireSpan.WireKind.parse(null));

        assertEquals(0.03125D / 2D, WireSpan.WireKind.ME.radius());
        assertEquals(1.005D, WireSpan.WireKind.ME.slack());
        assertEquals(0x91 / 255F, WireSpan.WireKind.ME.red());
        assertEquals(0x5d / 255F, WireSpan.WireKind.ME.green());
        assertEquals(0xcd / 255F, WireSpan.WireKind.ME.blue());
        assertEquals(0.0625D / 2D, WireSpan.WireKind.ME_DENSE.radius());
        assertEquals(1.003D, WireSpan.WireKind.ME_DENSE.slack());
        assertEquals(0x4e / 255F, WireSpan.WireKind.ME_DENSE.red());
        assertEquals(0x3c / 255F, WireSpan.WireKind.ME_DENSE.green());
        assertEquals(0x95 / 255F, WireSpan.WireKind.ME_DENSE.blue());
    }

    @Test
    void dropsMalformedRecordsWithoutHidingValidWire() {
        WireNetworkData.Wire malformed = wire(
                new int[]{1, 2, 3}, List.of(Double.NaN, 0.5D, 0.5D),
                new int[]{2, 2, 3}, List.of(0.5D, 0.5D, 0.5D),
                (byte) 0, "me"
        );
        WireNetworkData.Wire valid = wire(
                new int[]{4, 5, 6}, List.of(0.5D, 0.5D, 0.5D),
                new int[]{8, 5, 6}, List.of(0.5D, 0.5D, 0.5D),
                (byte) 0, "me_dense"
        );

        List<WireSpan> spans = WireAttachmentReader.normalize(root(List.of(malformed, valid)));

        assertEquals(1, spans.size());
        assertTrue(spans.stream().allMatch(span -> span.kind() == WireSpan.WireKind.ME_DENSE));
    }

    private static WireNetworkData root(List<WireNetworkData.Wire> wires) {
        return new WireNetworkData(new WireNetworkData.Data(
                new WireNetworkData.Network(List.of(
                        new WireNetworkData.LocalNetwork(wires)
                ))
        ));
    }

    private static WireNetworkData.Wire wire(
            int[] endA,
            List<Double> offsetA,
            int[] endB,
            List<Double> offsetB,
            byte internal,
            String type
    ) {
        return new WireNetworkData.Wire(
                new WireNetworkData.Endpoint(0, endA),
                new WireNetworkData.Endpoint(0, endB),
                offsetA,
                offsetB,
                internal,
                type
        );
    }
}
