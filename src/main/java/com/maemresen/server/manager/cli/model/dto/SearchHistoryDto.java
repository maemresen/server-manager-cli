package com.maemresen.server.manager.cli.model.dto;

import com.maemresen.server.manager.cli.model.entity.Status;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SearchHistoryDto {
  private LocalDate fromDate;
  private LocalDate toDate;
  private Status status;
  private Sort sort;
}
