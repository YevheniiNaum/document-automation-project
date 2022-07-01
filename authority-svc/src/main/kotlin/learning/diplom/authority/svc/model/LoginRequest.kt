package learning.diplom.authority.svc.model

data class LoginRequest(
    val email: String? = null,
    val password: String? = null
)
