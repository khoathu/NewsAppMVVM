package com.example.newsappmvvm.domain.model

import com.example.newsappmvvm.data.dto.Mapable
import com.example.newsappmvvm.data.dto.SourceDto
import java.io.Serializable

data class Source(
    val id: Any? = null,
    val name: String
) : Mapable<SourceDto>, Serializable {

    override fun map(): SourceDto {
        return SourceDto(id = id, name = name)
    }

}
