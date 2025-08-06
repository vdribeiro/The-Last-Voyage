package com.hybris.tlv.usecase.space.model

import kotlin.math.pow
import kotlin.math.sqrt

internal data class CartesianPoint(
    val x: Double,
    val y: Double,
    val z: Double
) {
    /**
     * Euclidean distance between two 3D points.
     */
    internal fun distanceBetween(cp: CartesianPoint): Double =
        sqrt(x = (cp.x - x).pow(n = 2) + (cp.y - y).pow(n = 2) + (cp.z - z).pow(n = 2))
}
