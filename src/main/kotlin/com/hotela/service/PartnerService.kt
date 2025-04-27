package com.hotela.service

import com.hotela.error.HotelaException
import com.hotela.model.database.Partner
import com.hotela.model.dto.request.UpdatePartnerRequest
import com.hotela.repository.PartnerAuthRepository
import com.hotela.repository.PartnerRepository
import com.hotela.util.getAuthId
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PartnerService(
    private val partnerRepository: PartnerRepository,
    private val partnerAuthRepository: PartnerAuthRepository,
) {
    suspend fun findById(id: UUID): Partner? = partnerRepository.findById(id)

    suspend fun findByEmail(email: String): Partner? = partnerRepository.findByEmail(email)

    suspend fun existsByEmail(email: String): Boolean = partnerRepository.existsByEmail(email)

    suspend fun createPartner(partner: Partner): Partner {
        if (partnerRepository.existsByEmail(partner.email)) {
            throw HotelaException.EmailAlreadyRegisteredException()
        }

        return partnerRepository.create(partner)
    }

    suspend fun updatePartner(
        payload: UpdatePartnerRequest,
        token: JwtAuthenticationToken,
    ): Partner {
        val partnerAuthId = token.getAuthId()

        val partnerAuth =
            partnerAuthRepository.findById(partnerAuthId)
                ?: throw HotelaException.PartnerAuthNotFoundException(partnerAuthId)

        val existingPartner =
            partnerRepository.findById(partnerAuth.partnerId)
                ?: throw HotelaException.PartnerNotFoundException(partnerAuth.partnerId)

        val updatedPartner =
            existingPartner.copy(
                name = payload.name ?: existingPartner.name,
                cnpj = payload.cnpj ?: existingPartner.cnpj,
                phone = payload.phone ?: existingPartner.phone,
                address = payload.address ?: existingPartner.address,
                contactName = payload.contactName ?: existingPartner.contactName,
                contactPhone = payload.contactPhone ?: existingPartner.contactPhone,
                contactEmail = payload.contactEmail ?: existingPartner.contactEmail,
                contractSigned = payload.contractSigned ?: existingPartner.contractSigned,
                status = payload.status ?: existingPartner.status,
                notes = payload.notes ?: existingPartner.notes,
            )

        return partnerRepository.update(updatedPartner)
    }
}
