package com.hybris.tlv.usecase.space.mapper

import com.hybris.tlv.usecase.space.model.CartesianPoint
import com.hybris.tlv.usecase.space.model.StellarHost
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

internal fun Double.roundTo(decimalPlaces: Int): Double {
    val factor = 10.0.pow(n = decimalPlaces)
    return round(x = this * factor) / factor
}

internal fun StellarHost.toCartesian(): CartesianPoint? {
    if (ra == null || dec == null || distance == null) return null
    val raRad = ra * PI / 180.0
    val decRad = dec * PI / 180.0
    return CartesianPoint(
        x = distance * cos(x = decRad) * cos(x = raRad),
        y = distance * cos(x = decRad) * sin(x = raRad),
        z = distance * sin(x = decRad)
    )
}

/**
 * Euclidean distance between two 3D points.
 */
internal fun CartesianPoint.distanceBetween(cp: CartesianPoint): Double =
    sqrt(x = (cp.x - x).pow(n = 2) + (cp.y - y).pow(n = 2) + (cp.z - z).pow(n = 2))

internal fun Double.stellarHostGravityToSunGravity(): Double = 10.0.pow(x = this - 4.4378)

internal fun Double.sunGravityToStellarHostGravity(): Double = log10(x = this) + 4.4378

internal fun Double.parsecsToLightYears(): Double = this * 3.26156

internal fun Double.lightYearsToParsecs(): Double = this / 3.26156
