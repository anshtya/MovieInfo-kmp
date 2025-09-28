package com.anshtya.movieinfo.common.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.anshtya.movieinfo.common.data.local.database.dao.AccountDetailsDao
import com.anshtya.movieinfo.common.data.local.database.dao.FavoriteContentDao
import com.anshtya.movieinfo.common.data.local.database.dao.WatchlistContentDao
import com.anshtya.movieinfo.common.data.local.database.entity.AccountDetailsEntity
import com.anshtya.movieinfo.common.data.local.database.entity.FavoriteContentEntity
import com.anshtya.movieinfo.common.data.local.database.entity.WatchlistContentEntity

internal const val databaseName = "movie_info.db"

@Database(
    entities = [
        FavoriteContentEntity::class,
        WatchlistContentEntity::class,
        AccountDetailsEntity::class
    ],
    version = 12,
    exportSchema = true
)
@ConstructedBy(MovieInfoDatabaseConstructor::class)
internal abstract class MovieInfoDatabase : RoomDatabase() {
    abstract fun favoriteContentDao(): FavoriteContentDao

    abstract fun watchlistContentDao(): WatchlistContentDao

    abstract fun accountDetailsDao(): AccountDetailsDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    ALTER TABLE trending_content ADD COLUMN remote_id INTEGER NOT NULL
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    ALTER TABLE trending_content 
                    MODIFY COLUMN id INTEGER AUTOINCREMENT NOT NULL
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE temp_fc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent()
                )

                connection.execSQL(
                    """
                   INSERT INTO temp_fc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM free_content
                """.trimIndent()
                )

                connection.execSQL("DROP TABLE free_content")
                connection.execSQL("ALTER TABLE temp_fc RENAME TO free_content")

                connection.execSQL(
                    """
                    CREATE TABLE temp_pc (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                    remote_id INTEGER NOT NULL, 
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL, 
                    overview TEXT NOT NULL)
                    """.trimIndent()
                )

                connection.execSQL(
                    """
                   INSERT INTO temp_pc (remote_id, image_path, name, overview)
                   SELECT id, image_path, name, overview FROM popular_content
                   """.trimIndent()
                )

                connection.execSQL("DROP TABLE popular_content")
                connection.execSQL("ALTER TABLE temp_pc RENAME TO popular_content")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS free_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS popular_content_remote_key (
                    id INTEGER NOT NULL, 
                    nextKey INTEGER NULL, 
                    PRIMARY KEY(id))
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS entity_last_modified (
                    name TEXT PRIMARY KEY NOT NULL, 
                    last_modified INTEGER NOT NULL)
                    """.trimIndent()
                )

                connection.execSQL(
                    """
                    INSERT INTO entity_last_modified VALUES
                    ('trending_content', 0),
                    ('free_content', 0),
                    ('popular_content', 0)
                """.trimIndent()
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("ALTER TABLE trending_content DROP COLUMN overview")
                connection.execSQL("ALTER TABLE free_content DROP COLUMN overview")
                connection.execSQL("ALTER TABLE popular_content DROP COLUMN overview")
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE favorite_content (
                    id INTEGER PRIMARY KEY NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE watchlist_content (
                    id INTEGER PRIMARY KEY NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL("DROP TABLE entity_last_modified")
                connection.execSQL("DROP TABLE free_content")
                connection.execSQL("DROP TABLE free_content_remote_key")
                connection.execSQL("DROP TABLE popular_content")
                connection.execSQL("DROP TABLE popular_content_remote_key")
                connection.execSQL("DROP TABLE trending_content")
                connection.execSQL("DROP TABLE trending_content_remote_key")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE account_details (
                    id INTEGER PRIMARY KEY NOT NULL,
                    gravatar_hash TEXT NOT NULL,
                    include_adult INTEGER NOT NULL,
                    iso_639_1 TEXT NOT NULL, 
                    iso_3166_1 TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    tmdb_avatar_path TEXT NULL,
                    username TEXT NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE fc (
                    id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    PRIMARY KEY(id,media_type))
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    INSERT INTO fc SELECT * FROM favorite_content   
                    """.trimIndent()
                )
                connection.execSQL("DROP TABLE favorite_content")
                connection.execSQL("ALTER TABLE fc RENAME TO favorite_content")

                connection.execSQL(
                    """
                    CREATE TABLE wc (
                    id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    PRIMARY KEY(id,media_type))
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    INSERT INTO wc SELECT * FROM watchlist_content   
                    """.trimIndent()
                )
                connection.execSQL("DROP TABLE watchlist_content")
                connection.execSQL("ALTER TABLE wc RENAME TO watchlist_content")
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                    CREATE TABLE favorite_content_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    media_id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    INSERT INTO favorite_content_temp (media_id, media_type, image_path, name)
                    SELECT id, media_type, image_path, name FROM favorite_content
                    """.trimIndent()
                )
                connection.execSQL("DROP TABLE favorite_content")
                connection.execSQL("ALTER TABLE favorite_content_temp RENAME TO favorite_content")
                connection.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_favorite_content_media_id_media_type 
                    ON favorite_content (media_id, media_type)
                    """.trimIndent()
                )

                connection.execSQL(
                    """
                    CREATE TABLE watchlist_content_temp (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    media_id INTEGER NOT NULL,
                    media_type TEXT NOT NULL,
                    image_path TEXT NOT NULL, 
                    name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                    INSERT INTO watchlist_content_temp (media_id, media_type, image_path, name)
                    SELECT id, media_type, image_path, name FROM watchlist_content
                    """.trimIndent()
                )
                connection.execSQL("DROP TABLE watchlist_content")
                connection.execSQL("ALTER TABLE watchlist_content_temp RENAME TO watchlist_content")
                connection.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_watchlist_content_media_id_media_type 
                    ON watchlist_content (media_id, media_type)
                    """.trimIndent()
                )
            }
        }
    }
}