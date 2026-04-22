package com.ucb.app.truck.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "truck_brands")
data class TruckBrandEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val originCountry: String,
    val logoUrl: String
)
