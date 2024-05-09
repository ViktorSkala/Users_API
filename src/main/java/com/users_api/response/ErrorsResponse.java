package com.users_api.response;

import com.users_api.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsResponse {

    private List<ErrorDto> errors;
}
