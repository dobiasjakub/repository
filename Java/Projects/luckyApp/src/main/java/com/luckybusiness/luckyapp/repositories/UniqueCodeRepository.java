package com.luckybusiness.luckyapp.repositories;

import com.luckybusiness.luckyapp.model.entities.UniqueCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniqueCodeRepository extends JpaRepository<UniqueCode, Long>, JpaSpecificationExecutor<UniqueCode> {
    // Použití nativního SQL dotazu místo JPA metody, která by generovala 'FETCH FIRST'  
@org.springframework.data.jpa.repository.Query("SELECT u FROM UniqueCode u WHERE u.code = ?1")
Optional<UniqueCode> findByCode(String code);

@org.springframework.data.jpa.repository.Query("SELECT u FROM UniqueCode u WHERE u.graduate.id = ?1 AND u.code = ?2")
Optional<UniqueCode> findByGraduateIdAndCode(Long id,String value);
}
