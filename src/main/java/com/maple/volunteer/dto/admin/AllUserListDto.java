package com.maple.volunteer.dto.admin;

import com.maple.volunteer.dto.common.PaginationDto;
import com.maple.volunteer.dto.user.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AllUserListDto {

    private List<UserDto> userList;
    private PaginationDto paginationDto;

    @Builder
    public AllUserListDto(List<UserDto> userList, PaginationDto paginationDto) {
        this.userList = userList;
        this.paginationDto = paginationDto;
    }
}
