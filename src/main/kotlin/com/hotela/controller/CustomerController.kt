package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.db.Customer
import com.hotela.model.dto.request.UpdateCustomerRequest
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/customer")
@EnableReactiveMethodSecurity
class CustomerController(
    private val customerService: CustomerService,
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getCustomerById(
        @PathVariable id: UUID,
    ): Customer? = customerService.findById(id) ?: throw HotelaException.CustomerNotFoundException(id)

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER)")
    suspend fun updateCustomer(
        @RequestBody payload: UpdateCustomerRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        customerService.updateCustomer(payload, principal)

        return ResourceUpdatedResponse(
            message = "Customer updated successfully",
        )
    }
}
