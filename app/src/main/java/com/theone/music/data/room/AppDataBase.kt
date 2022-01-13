package com.theone.lover.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.theone.music.data.model.Music
import com.theone.music.data.room.MusicDao
import com.theone.mvvm.base.appContext


//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2021-10-08 10:03
 * @describe TODO
 *          (1) 使用entities来映射相关的实体类
 *          (2) version来指明当前数据库的版本号
 *          (3) 使用了单例模式来返回Database，以防止新建过多的实例造成内存的浪费
 *          (4)Room.databaseBuilder(context,klass,name).build()来创建Database，
 *              其中第一个参数为上下文，
 *              第二个参数为当前Database的class字节码文件，
 *              第三个参数为数据库名称
 *          注意事项：默认情况下Database是不可以在主线程中进行调用的。
 *              因为大部分情况，操作数据库都还算是比较耗时的动作。
 *              如果需要在主线程调用则使用allowMainThreadQueries进行说明。
 */
@Database(entities = [Music::class], version = 3, exportSchema = false)
abstract class AppDataBase:RoomDatabase() {

    abstract fun musicDao(): MusicDao

    companion object{
        private const val DB_NAME = "HifiNi.db"

        val INSTANCE:AppDataBase by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            Room.databaseBuilder(
                appContext,
                AppDataBase::class.java,
                DB_NAME
            ).allowMainThreadQueries().addMigrations(MIGRATION_2_3).build()
        }

        var MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate( database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE MusicInfo " + " ADD COLUMN collection INTEGER " + " NOT NULL DEFAULT 1")
            }
        }

    }

}