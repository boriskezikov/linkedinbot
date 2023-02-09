package com.tr.linkedinbot.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
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

    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    private BotState state;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(type = "list-array")
    @Column(
            name = "search_roles",
            columnDefinition = "varchar[]"
    )
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> searchRolesString;

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Role> searchRoles;

    private Integer pageNumber;

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
