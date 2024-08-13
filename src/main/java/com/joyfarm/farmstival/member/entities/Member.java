package com.joyfarm.farmstival.member.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyfarm.farmstival.global.entities.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;
    private String email;
    private String password;
    private String userName;
    private String mobile;
    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Authorities> authorities;
}
