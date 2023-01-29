package albayed.moamin.notesattach.data

import albayed.moamin.notesattach.models.Image
import albayed.moamin.notesattach.models.Note
import androidx.compose.runtime.MutableState
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
}