package com.joyfarm.farmstival.member.admin.repositories;

import com.joyfarm.farmstival.member.entities.Authorities;
import com.joyfarm.farmstival.member.entities.AuthoritiesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId> {
}
