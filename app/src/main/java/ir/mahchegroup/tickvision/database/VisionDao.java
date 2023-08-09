package ir.mahchegroup.tickvision.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VisionDao {
    @Insert
    long addVision(ModelVision modelVision);
    @Update
    int editVision(ModelVision modelVision);
    @Delete
    int removeVision(ModelVision modelVision);
    @Query("DELETE FROM vision_tbl")
    int clearAllVisions();
    @Query("SELECT * FROM vision_tbl WHERE title = :title")
    ModelVision getVision(String title);
    @Query("SELECT * FROM vision_tbl")
    List<ModelVision> getAllVisions();
    @Query("SELECT COUNT(id) FROM vision_tbl")
    int getCountVision();
}
