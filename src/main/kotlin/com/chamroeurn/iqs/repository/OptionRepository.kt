package com.chamroeurn.iqs.repository

import com.chamroeurn.iqs.repository.entity.OptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OptionRepository: JpaRepository<OptionEntity, UUID>