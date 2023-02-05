package albayed.moamin.notesattach.database

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import albayed.moamin.notesattach.models.Video
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

    @Query ("SELECT * from videos_table where noteId=:noteId")
    fun getAllVideosByNoteId(noteId: String): Flow<List<Video>>

    @Insert
    suspend fun createImage(image: Image)

    @Delete
    suspend fun deleteImage(image: Image)

    @Insert
    suspend fun createVideo(video: Video)

    @Delete
    suspend fun deleteVideo(video: Video)


    @Query("UPDATE notes_table SET imagesCount=:imagesCount WHERE id=:noteId")
    suspend fun updateImagesCount (imagesCount: Int, noteId: String)

    @Query("UPDATE notes_table SET videosCount=:videosCount WHERE id=:noteId")
    suspend fun updateVideosCount (videosCount: Int, noteId: String)

    @Query("UPDATE notes_table SET voiceClipsCount=:voiceClipsCount WHERE id=:noteId")
    suspend fun updateVoiceClipsCount (voiceClipsCount: Int, noteId: String)

    @Query("UPDATE notes_table SET locationsCount=:locationsCount WHERE id=:noteId")
    suspend fun updateLocationsCount (locationsCount: Int, noteId: String)

    @Query("UPDATE notes_table SET alarmsCount=:alarmsCount WHERE id=:noteId")
    suspend fun updateAlarmsCount (alarmsCount: Int, noteId: String)

    @Query ("SELECT imagesCount from notes_table where id=:noteId")
    fun getImagesCount (noteId: String): Flow<Int>

    @Query ("SELECT videosCount from notes_table where id=:noteId")
    fun getVideosCount (noteId: String): Flow<Int>

    @Query ("SELECT voiceClipsCount from notes_table where id=:noteId")
    fun getVoiceClipsCount (noteId: String): Flow<Int>

    @Query ("SELECT locationsCount from notes_table where id=:noteId")
    fun getLocationsCount(noteId: String): Flow<Int>

    @Query ("SELECT alarmsCount from notes_table where id=:noteId")
    fun getAlarmsCount(noteId: String): Flow<Int>


}