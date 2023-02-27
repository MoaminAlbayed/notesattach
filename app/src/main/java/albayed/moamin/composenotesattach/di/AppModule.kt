package albayed.moamin.composenotesattach.di

import albayed.moamin.composenotesattach.database.NoteDatabase
import albayed.moamin.composenotesattach.database.NoteDatabaseDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDatabaseDao = noteDatabase.noteDao()

    @Singleton
    @Provides
    fun provideNoteDataBase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(context, NoteDatabase::class.java, name = "notes_db")
            .fallbackToDestructiveMigration().build()
    }
}