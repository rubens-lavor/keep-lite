package com.rubenslavor.keeplite.domain.exceptions

class BusinessException(
    override val message: String?
): RuntimeException(message)