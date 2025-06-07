import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.memorymatch.data.dao.GameStatisticsDao
import com.example.memorymatch.data.model.GameStatisticsEntity

@Database(
    entities = [GameStatisticsEntity::class,],
version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameStatisticsDao(): GameStatisticsDao
    // остальные DAO...
}
