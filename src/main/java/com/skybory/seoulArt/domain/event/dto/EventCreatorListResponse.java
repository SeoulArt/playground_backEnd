package com.skybory.seoulArt.domain.event.dto;

import java.util.List;

import com.skybory.seoulArt.domain.event.entity.Creator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class EventCreatorListResponse {
    private List<Creator> creators;

}