package com.hotela.service

import com.hotela.model.database.PartnerAuth
import com.hotela.repository.PartnerAuthRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PartnerAuthService(
    private val partnerAuthRepository: PartnerAuthRepository,
) {
    suspend fun findById(id: UUID): PartnerAuth? = partnerAuthRepository.findById(id)

    suspend fun findByEmail(email: String): PartnerAuth? = partnerAuthRepository.findByEmail(email)

    suspend fun existsByEmail(email: String): Boolean = partnerAuthRepository.existsByEmail(email)

    suspend fun save(partnerAuth: PartnerAuth): PartnerAuth = partnerAuthRepository.save(partnerAuth)
}
