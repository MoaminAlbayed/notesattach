package albayed.moamin.notesattach.data

import albayed.moamin.notesattach.models.Note
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {
    @Query("SELECT * from notes_table")
    fun getAllNotes(): Flow<List<Note>>

    @Insert
    suspend fun createNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}