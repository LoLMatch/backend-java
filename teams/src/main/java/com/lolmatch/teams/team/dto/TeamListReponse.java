package com.lolmatch.teams.team.dto;

import java.util.List;

public record TeamListReponse(List<TeamDTO> teams, int size, int page, long totalNumber) {
}
