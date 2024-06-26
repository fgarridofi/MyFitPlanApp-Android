package es.upsa.myfitplan.data.db


import es.upsa.myfitplan.data.mapper.Converters
import android.animation.TypeConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.upsa.myfitplan.domain.model.routine.Routine

@Database(entities = [Routine::class], version = 2)
@TypeConverters(Converters::class)
abstract class DataBase : RoomDatabase() {

    abstract fun routinesDao(): RoutinesDao
}