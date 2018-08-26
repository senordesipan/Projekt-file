package com.example.moritzschuck.vinylz;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by cip on 13.08.2018.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleFriend(Platte p);

    @Query("SELECT * FROM Platte WHERE plattenID =:plattenID")
    Platte findPlatteByID(String plattenID);

   @Query("SELECT * FROM Platte WHERE title =:title")
    Platte findPlatteByTitle(String title);

 /*   @Query("SELECT * FROM Platte WHERE band LIKE band")
    Platte findPlatteByBand(String plattenID);

    @Query("SELECT * FROM Platte WHERE year LIKE year")
    Platte findPlatteByYear(String plattenID);*/

}
