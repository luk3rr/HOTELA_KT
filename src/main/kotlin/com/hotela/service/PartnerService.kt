package com.hotela.service

import com.hotela.model.database.Partner
import com.hotela.repository.PartnerRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PartnerService(
    private val partnerRepository: PartnerRepository,
) {
    suspend fun findById(id: UUID) = partnerRepository.findById(id)

    suspend fun findByEmail(email: String) = partnerRepository.findByEmail(email)

    suspend fun existsByEmail(email: String) = partnerRepository.existsByEmail(email)

    suspend fun createPartner(partner: Partner) = partnerRepository.create(partner)
}
