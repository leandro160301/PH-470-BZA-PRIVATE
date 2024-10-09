package com.jws.jwsapi.pallet;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PalletDao {

    @Query("SELECT * FROM pallet ORDER BY id DESC")
    LiveData<List<Pallet>> getAllPallets();

    @Query("SELECT * FROM pallet WHERE is_closed = :open ORDER BY id DESC")
    LiveData<List<Pallet>> getAllPallets(Boolean open);

    @Query("SELECT * FROM pallet WHERE is_closed = :open ORDER BY id DESC")
    List<Pallet> getAllPalletsStatic(Boolean open);

    @Query("SELECT * FROM pallet WHERE id = :id and is_closed = :open")
    LiveData<Pallet> getPalletById(int id, Boolean open);

    @Query("DELETE FROM pallet")
    void deleteAllPallets();

    @Query("DELETE FROM pallet WHERE id = :id")
    void deletePallet(int id);

    @Insert
    void insertPallet(Pallet pallet);

    @Query("UPDATE pallet SET is_closed = :open WHERE id = :id")
    void updatePalletClosedStatus(int id, boolean open);

    @Query("UPDATE pallet SET done = done + 1 WHERE id = :id")
    void incrementDoneById(int id);

    @Query("UPDATE pallet SET total_net = total_net + :additionalNet WHERE id = :id")
    void updatePalletTotalNet(int id, String additionalNet);

    @Query("UPDATE pallet SET serial_number = :serialNumber WHERE id = :id")
    void updatePalletSerialNumber(int id, String serialNumber);
}