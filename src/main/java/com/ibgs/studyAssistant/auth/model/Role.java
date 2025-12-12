package com.ibgs.studyAssistant.auth.model;

import com.ibgs.studyAssistant.auth.enuns.RoleName;
import com.ibgs.studyAssistant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode(callSuper = true)
@Table(name = "role")
@Entity
@Data
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority{

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
