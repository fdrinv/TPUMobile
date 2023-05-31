package com.fedorinov.tpumobile.ui.model

import com.fedorinov.tpumobile.data.database.enum.ContentType
import java.util.UUID

data class LinkItem(
    val id: UUID,
    val name: String,
    val level: Int,
    val type: ContentType?,
    val position: Int,
    val imgUrl: String? = null,
    val children: List<LinkItem>
)