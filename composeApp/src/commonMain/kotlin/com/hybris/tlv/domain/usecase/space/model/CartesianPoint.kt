package com.hybris.tlv.domain.usecase.space.model

internal data class CartesianPoint(
    val x: Double,
    val y: Double,
    val z: Double
) {
    internal fun distanceSquaredBetween(cp: CartesianPoint): Double {
        val dx = x - cp.x
        val dy = y - cp.y
        val dz = z - cp.z
        return (dx * dx) + (dy * dy) + (dz * dz)
    }
}
