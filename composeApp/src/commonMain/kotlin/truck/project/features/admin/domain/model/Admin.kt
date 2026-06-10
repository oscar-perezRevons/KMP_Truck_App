package truck.project.features.admin.domain.model

import truck.project.core.domain.vo.Email

data class Admin(
    val id: String,
    val name: String,
    val email: Email,
    val company: String,
    val profileImageUrl: String? = null
)
