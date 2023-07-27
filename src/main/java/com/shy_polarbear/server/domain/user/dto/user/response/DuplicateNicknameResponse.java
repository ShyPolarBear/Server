package com.shy_polarbear.server.domain.user.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateNicknameResponse {
    private Boolean available;
}
