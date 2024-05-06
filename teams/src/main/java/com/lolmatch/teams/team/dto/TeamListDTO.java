package com.lolmatch.teams.team.dto;

import java.util.List;

public record TeamListDTO(List<TeamDTO> teams, int size, int page, long totalNumber) {
}
