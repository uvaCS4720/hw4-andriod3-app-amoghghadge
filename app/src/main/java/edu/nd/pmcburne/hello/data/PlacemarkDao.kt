package edu.nd.pmcburne.hello.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacemarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacemarks(placemarks: List<Placemark>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<PlacemarkTag>)

    @Query("SELECT DISTINCT tag FROM placemark_tags ORDER BY tag ASC")
    suspend fun getAllTags(): List<String>

    @Query(
        "SELECT p.* FROM placemarks p " +
        "INNER JOIN placemark_tags t ON p.id = t.placemarkId " +
        "WHERE t.tag = :tag"
    )
    suspend fun getPlacemarksByTag(tag: String): List<Placemark>
}
