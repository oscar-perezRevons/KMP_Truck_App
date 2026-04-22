package com.ucb.app.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoute {
    @Serializable
    object TruckBrands: NavRoute()

}
