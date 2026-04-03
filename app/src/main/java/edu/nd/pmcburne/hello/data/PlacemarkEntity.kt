package edu.nd.pmcburne.hello.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "placemarks")
data class Placemark(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

@Entity(
    tableName = "placemark_tags",
    foreignKeys = [ForeignKey(
        entity = Placemark::class,
        parentColumns = ["id"],
        childColumns = ["placemarkId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("placemarkId")],
    primaryKeys = ["placemarkId", "tag"]
)
data class PlacemarkTag(
    val placemarkId: Int,
    val tag: String
)
