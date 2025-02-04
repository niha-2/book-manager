package com.book.manager.application.service

import com.book.manager.domain.model.Rental
import com.book.manager.domain.repository.BookRepository
import com.book.manager.domain.repository.RentalRepository
import com.book.manager.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private const val RENTAL_TERM_DAYS = 14L

@Service
class RentalService(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val rentalRepository: RentalRepository
) {
    @Transactional
    fun startRental(bookId: Long, userId: Long) {
        userRepository.find(userId) ?: throw IllegalArgumentException("該当するユーザーが存在しません userId: $userId")
        val book = bookRepository.findWithRental(bookId) ?: throw IllegalArgumentException("該当する書籍が存在しません bookId: $bookId")

        // 貸出中のチェック
        if (book.isRental) throw IllegalArgumentException("貸出中の商品です bookId: $bookId")

        val rentalDatatime = LocalDateTime.now()
        val returnDeadline = rentalDatatime.plusDays(RENTAL_TERM_DAYS)
        val rental = Rental(bookId, userId, rentalDatatime, returnDeadline)

        rentalRepository.startRental(rental)
    }

    @Transactional
    fun endRental(bookId: Long, userId: Long) {
        userRepository.find(userId) ?: throw IllegalArgumentException("該当するユーザーが存在しません userId:$userId")
        val book = bookRepository.findWithRental(bookId) ?: throw IllegalArgumentException("該当する書籍が存在しません bookId: $bookId")

        // 貸出中のチェック
        if (!book.isRental) throw IllegalArgumentException("未貸出の商品です bookId: $bookId")
        if (book.rental!!.userId != userId) throw IllegalArgumentException("他のユーザーが貸出中の商品です userId: $userId bookId: $bookId")

        rentalRepository.endRental(bookId)
    }
}