package com.rubenslavor.keeplite.infrastructure.persistence.mongo

import com.rubenslavor.keeplite.domain.user.entity.User
import com.rubenslavor.keeplite.domain.user.model.UserModel
import com.rubenslavor.keeplite.domain.user.repository.UserRepository
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.mapper.UserPersistenceMapper
import com.rubenslavor.keeplite.infrastructure.persistence.mongo.repository.UserMongoRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserRepositoryImpl(
    private val userMongoRepository: UserMongoRepository
): UserRepository {

    override fun save(user: User): User {
        val userDocument = UserPersistenceMapper.toDocument(user)
        val savedDocument = userMongoRepository.save(userDocument)
        return UserPersistenceMapper.toModel(savedDocument)
    }

    override fun findByEmail(email: String): User? {
        val foundDocument = userMongoRepository.findByEmail(email)
        return foundDocument?.let { UserPersistenceMapper.toModel(it) }
    }

    override fun findById(id: UUID): User? {
        val foundDocument = userMongoRepository.findById(id).orElse(null)
        return foundDocument?.let { UserPersistenceMapper.toModel(it) }
    }
}
