package albayed.moamin.composenotesattach.database

import albayed.moamin.composenotesattach.models.*
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {
    @Query("SELECT * from notes_table")
    fun getAllNotes(): Flow<List<Note>>

    @Query ("SELECT * from notes_table where id=:id")
    suspend fun getNoteById(id: String): Note

    @Insert
    suspend fun createNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)


    @Query ("SELECT * from images_table where noteId=:noteId")
    fun getAllImagesByNoteId(noteId: String): Flow<List<Image>>

    @Insert
    suspend fun createImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)


    @Query ("SELECT * from videos_table where noteId=:noteId")
    fun getAllVideosByNoteId(noteId: String): Flow<List<Video>>

    @Insert
    suspend fun createVideo(video: Video)

    @Delete
    suspend fun deleteVideo(video: Video)


    @Query ("SELECT * from audio_table where noteId = :noteId")
    fun getAllAudioClipsByNoteId (noteId: String): Flow<List<AudioClip>>

    @Insert
    suspend fun createAudioClip (audioClip: AudioClip)

    @Delete
    suspend fun deleteAudioClip (audioClip: AudioClip)


    @Query ("SELECT * from locations_table where noteId = :noteId")
    fun getAllLocationsByNoteId (noteId: String): Flow<List<Location>>

    @Insert
    suspend fun createLocation(location: Location)

    @Delete
    suspend fun deleteLocation (location: Location)


    @Query("SELECT * from alarms_table where noteId= :noteId")
    fun getAllAlarmsByNoteId (noteId: String): Flow<List<Alarm>>

    @Insert
    suspend fun createAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)



    @Query("UPDATE notes_table SET imagesCount=:imagesCount WHERE id=:noteId")
    suspend fun updateImagesCount (imagesCount: Int, noteId: String)

    @Query("UPDATE notes_table SET videosCount=:videosCount WHERE id=:noteId")
    suspend fun updateVideosCount (videosCount: Int, noteId: String)

    @Query("UPDATE notes_table SET audioClipsCount=:audioClipsCount WHERE id=:noteId")
    suspend fun updateAudioClipsCount (audioClipsCount: Int, noteId: String)

    @Query("UPDATE notes_table SET locationsCount=:locationsCount WHERE id=:noteId")
    suspend fun updateLocationsCount (locationsCount: Int, noteId: String)

    @Query("UPDATE notes_table SET alarmsCount=:alarmsCount WHERE id=:noteId")
    suspend fun updateAlarmsCount (alarmsCount: Int, noteId: String)

    @Query ("SELECT imagesCount from notes_table where id=:noteId")
    fun getImagesCount (noteId: String): Flow<Int>

    @Query ("SELECT videosCount from notes_table where id=:noteId")
    fun getVideosCount (noteId: String): Flow<Int>

    @Query ("SELECT audioClipsCount from notes_table where id=:noteId")
    fun getAudioClipsCount (noteId: String): Flow<Int>

    @Query ("SELECT locationsCount from notes_table where id=:noteId")
    fun getLocationsCount(noteId: String): Flow<Int>

    @Query ("SELECT alarmsCount from notes_table where id=:noteId")
    fun getAlarmsCount(noteId: String): Flow<Int>


}