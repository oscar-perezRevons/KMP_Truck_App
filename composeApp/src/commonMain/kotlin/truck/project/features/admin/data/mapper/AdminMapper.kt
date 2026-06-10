package truck.project.features.admin.data.mapper

import truck.project.features.admin.data.local.AdminEntity
import truck.project.features.admin.domain.model.Admin
import truck.project.core.domain.vo.Email

fun AdminEntity.toDomain() = Admin(
    id = id,
    name = name,
    email = Email(email),
    company = company,
    profileImageUrl = profileImageUrl
)

fun Admin.toEntity(password: String) = AdminEntity(
    id = id,
    name = name,
    email = email.value,
    password = password,
    company = company,
    profileImageUrl = profileImageUrl
)
