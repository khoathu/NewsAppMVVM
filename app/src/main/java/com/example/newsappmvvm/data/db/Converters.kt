package com.example.newsappmvvm.data.db

import androidx.room.TypeConverter
import com.example.newsappmvvm.data.dto.SourceDto

class Converters {

    @TypeConverter
    fun fromSource(source: SourceDto) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : SourceDto
    {
        return SourceDto(name, name)
    }

}