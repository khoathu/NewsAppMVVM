package com.example.newsappmvvm.data.dto

import com.example.newsappmvvm.domain.model.Source
import java.io.Serializable

data class SourceDto(
    val id: Any? = null,
    val name: String
) : Mapable<Source>, Serializable {

    override fun map(): Source {
        return Source(
            id = id,
            name = name)
    }

}
