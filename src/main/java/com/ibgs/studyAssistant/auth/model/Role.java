package com.ibgs.studyAssistant.auth.model;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Table(name = "role")
@Entity
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",unique = true, nullable = false)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.toString();
    }

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }
}
