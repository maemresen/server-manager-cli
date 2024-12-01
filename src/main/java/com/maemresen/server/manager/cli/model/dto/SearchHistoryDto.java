package com.maemresen.server.manager.cli.model.dto;

import com.maemresen.server.manager.cli.model.entity.Status;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SearchHistoryDto {
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private Status status;
  private SortDirection sortDirection = SortDirection.DESC;
}
