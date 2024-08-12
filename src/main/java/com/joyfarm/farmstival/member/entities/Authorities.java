package com.joyfarm.farmstival.member.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyfarm.farmstival.member.constants.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authorities {
    private Authority authority;
}
