package com.projectsassy.sassy.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NewestListResponse {

    List<NewestHomeDto> newestList;

}
