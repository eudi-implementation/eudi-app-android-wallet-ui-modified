/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

package eu.europa.ec.storagelogic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import eu.europa.ec.storagelogic.dao.type.StorageDao
import eu.europa.ec.storagelogic.model.TransactionLog

@Dao
interface TransactionLogDao : StorageDao<TransactionLog> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun store(value: TransactionLog)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    override suspend fun storeAll(values: List<TransactionLog>)

    @Query("SELECT * FROM transactionLogs WHERE identifier = :identifier")
    override suspend fun retrieve(identifier: String): TransactionLog?

    @Query("SELECT * FROM transactionLogs")
    override suspend fun retrieveAll(): List<TransactionLog>

    @Update
    override suspend fun update(value: TransactionLog)

    @Query("DELETE FROM transactionLogs WHERE identifier = :identifier")
    override suspend fun delete(identifier: String)

    @Query("DELETE FROM transactionLogs")
    override suspend fun deleteAll()
}