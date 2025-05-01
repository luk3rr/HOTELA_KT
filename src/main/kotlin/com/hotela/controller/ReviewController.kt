package com.hotela.controller

import com.hotela.error.HotelaException
import com.hotela.model.dto.request.CreateReviewRequest
import com.hotela.model.dto.request.UpdateReviewRequest
import com.hotela.model.dto.response.ResourceCreatedResponse
import com.hotela.model.dto.response.ResourceUpdatedResponse
import com.hotela.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/review")
@EnableReactiveMethodSecurity
class ReviewController(
    private val reviewService: ReviewService,
) {
    @GetMapping("{id}")
    suspend fun getReviewById(
        @PathVariable id: UUID,
    ) = reviewService.findById(id)
        ?: throw HotelaException.ReviewNotFoundException(id)

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER)")
    suspend fun createReview(
        @RequestBody payload: CreateReviewRequest,
        principal: JwtAuthenticationToken,
    ): ResourceCreatedResponse {
        val createdReview = reviewService.createReview(payload, principal)

        return ResourceCreatedResponse(
            id = createdReview.id,
            message = "Review created successfully",
        )
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole(T(com.hotela.model.enum.Role).CUSTOMER)")
    suspend fun updateReview(
        @PathVariable id: UUID,
        @RequestBody payload: UpdateReviewRequest,
        principal: JwtAuthenticationToken,
    ): ResourceUpdatedResponse {
        reviewService.updateReview(id, payload, principal)

        return ResourceUpdatedResponse(
            message = "Review updated successfully",
        )
    }
}
