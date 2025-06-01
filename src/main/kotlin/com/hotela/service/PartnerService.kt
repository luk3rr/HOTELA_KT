package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.db.Partner
import com.hotela.model.domain.Email
import com.hotela.model.dto.request.UpdatePartnerRequest
import com.hotela.repository.PartnerRepository
import com.hotela.util.getUserId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PartnerService(
    private val partnerRepository: PartnerRepository,
) {
    suspend fun findById(id: UUID): Partner? = partnerRepository.findById(id)

    suspend fun findByAuthId(authId: UUID): Partner? = partnerRepository.findByAuthId(authId)

    suspend fun findByEmail(email: Email): Partner? = partnerRepository.findByEmail(email)

    suspend fun existsByEmail(email: Email): Boolean = partnerRepository.existsByEmail(email)

    suspend fun create(partner: Partner): Partner {
        if (partnerRepository.existsByEmail(partner.contactInfo.email)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        return partnerRepository.create(partner)
    }

    suspend fun updatePartner(
        payload: UpdatePartnerRequest,
        token: JwtAuthenticationToken,
    ): Partner {
        val partnerId = token.getUserId()

        val existingPartner =
            partnerRepository.findById(partnerId)
                ?: throw HotelaException.PartnerNotFoundException(partnerId)

        val updatedPartner =
            existingPartner.copy(
                companyName = payload.companyName ?: existingPartner.companyName,
                legalName = payload.legalName ?: existingPartner.legalName,
                contactInfo = payload.contactInfo ?: existingPartner.contactInfo,
                documentId = payload.documentId ?: existingPartner.documentId,
                contractSignedAt =
                    payload.contractSignedAt
                        ?: existingPartner.contractSignedAt,
                status = payload.status ?: existingPartner.status,
                notes = payload.notes ?: existingPartner.notes,
            )

        return partnerRepository.update(updatedPartner)
    }
}
