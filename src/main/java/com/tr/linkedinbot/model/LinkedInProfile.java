package com.tr.linkedinbot.model;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "chatId")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class LinkedInProfile {

    @Id
    private Long chatId;

    private String tgUser;
    private String linkedInUrl;
    private LocalDateTime lastProfileGet;
    private LocalDateTime registeredAt;

    private String email;
    private Boolean awaitingEmail;

    @Enumerated(EnumType.STRING)
    private BotState state;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Role role;

    // массив строк (через Hibernate-Types)
    @Type(type = "list-array")
    @Column(name = "search_roles", columnDefinition = "varchar[]")
    private List<String> searchRolesString;

    @Transient
    private Set<Role> searchRoles;

    private Integer pageNumber;

    private Integer freeUsage = 0;

    @PostLoad
    public void loadSearchRoles() {
        searchRoles = Optional.ofNullable(searchRolesString)
                .orElse(Collections.emptyList())
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    public void removeSearchRole(Role role) {
        searchRoles.remove(role);
        searchRolesString = searchRoles.stream().map(Role::name).collect(Collectors.toList());
    }

    public void addSearchRole(Role role) {
        searchRoles.add(role);
        searchRolesString = searchRoles.stream().map(Role::name).collect(Collectors.toList());
    }

    public Set<Role> getSearchRoles() {
        return Collections.unmodifiableSet(searchRoles);
    }

    public boolean isComplete() {
        return country != null && !searchRoles.isEmpty() && role != null;
    }
}
