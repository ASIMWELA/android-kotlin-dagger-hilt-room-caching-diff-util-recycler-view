package news.my.kotlin.db

import androidx.room.TypeConverter
import news.my.kotlin.model.Source


class TypeConvertors {
    @TypeConverter
    fun fromSource(source: Source):String {
        return source.name
    }

    @TypeConverter
    fun fromString(sourceName: String):Source {
        return Source(sourceName, sourceName)
    }
}