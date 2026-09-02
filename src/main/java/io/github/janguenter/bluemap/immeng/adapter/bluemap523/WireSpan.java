/* SPDX-License-Identifier: MIT */

package io.github.janguenter.bluemap.immeng.adapter.bluemap523;

import java.util.Locale;

/** Validated Immersive Energistics wire span in absolute world coordinates. */
record WireSpan(WireKind kind, Point start, Point end) {

    static WireSpan canonical(WireKind kind, Point first, Point second) {
        return compare(first, second) <= 0
                ? new WireSpan(kind, first, second)
                : new WireSpan(kind, second, first);
    }

    private static int compare(Point left, Point right) {
        int x = Double.compare(left.x(), right.x());
        if (x != 0) {
            return x;
        }
        int y = Double.compare(left.y(), right.y());
        return y != 0 ? y : Double.compare(left.z(), right.z());
    }

    /** Absolute three-dimensional point. */
    record Point(double x, double y, double z) {

        Point add(Point other) {
            return new Point(x + other.x, y + other.y, z + other.z);
        }

        Point subtract(Point other) {
            return new Point(x - other.x, y - other.y, z - other.z);
        }

        Point scale(double factor) {
            return new Point(x * factor, y * factor, z * factor);
        }

        double length() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        Point normalize() {
            double magnitude = length();
            return magnitude == 0D ? new Point(0D, 0D, 0D) : scale(1D / magnitude);
        }

        Point cross(Point other) {
            return new Point(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
            );
        }
    }

    /** The two persisted wire types owned by Immersive Energistics 1.1.0-beta. */
    enum WireKind {
        ME("me", 0.03125D, 1.005D, 0x915dcd),
        ME_DENSE("me_dense", 0.0625D, 1.003D, 0x4e3c95);

        private final String persistedName;
        private final double diameter;
        private final double slack;
        private final int color;

        WireKind(String persistedName, double diameter, double slack, int color) {
            this.persistedName = persistedName;
            this.diameter = diameter;
            this.slack = slack;
            this.color = color;
        }

        static WireKind parse(String value) {
            if (value == null) {
                return null;
            }
            String normalized = value.trim().toLowerCase(Locale.ROOT);
            for (WireKind kind : values()) {
                if (kind.persistedName.equals(normalized)) {
                    return kind;
                }
            }
            return null;
        }

        double radius() {
            return diameter / 2D;
        }

        double slack() {
            return slack;
        }

        float red() {
            return ((color >>> 16) & 0xff) / 255F;
        }

        float green() {
            return ((color >>> 8) & 0xff) / 255F;
        }

        float blue() {
            return (color & 0xff) / 255F;
        }
    }
}
