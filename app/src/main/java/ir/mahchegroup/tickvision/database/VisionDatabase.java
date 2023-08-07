package ir.mahchegroup.tickvision.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ModelVision.class}, version = 1)
public abstract class VisionDatabase extends RoomDatabase {
    private static VisionDatabase visionDatabase;

    public static VisionDatabase getVisionDatabase(Context context) {
        if (visionDatabase == null) {
            visionDatabase = Room.databaseBuilder(context, VisionDatabase.class, "vision_db")
                    .allowMainThreadQueries().build();
        }
        return visionDatabase;
    }

    public abstract VisionDao visionDao();
}
