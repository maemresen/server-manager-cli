package com.maemresen.server.manager.cli.beans.repository;

import com.google.inject.Inject;
import com.maemresen.server.manager.cli.beans.DataSource;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import com.maemresen.server.manager.cli.utils.DateTimeUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerEventRepository {

  private final DataSource dataSource;

  @Inject
  public ServerEventRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createNewEvent(Status status) throws SQLException {
    final String sql = "INSERT INTO SERVER_EVENT (STATUS, CREATION_TIME) VALUES (?1, ?2)";
    try (final Connection connection = dataSource.createConnection()) {
      try (final PreparedStatement statmement = connection.prepareStatement(sql)) {
        statmement.setString(1, status.toString());
        statmement.setObject(2, DateTimeUtils.now());
        statmement.execute();
      }
    }
  }

  public Optional<ServerEvent> findLatest() throws SQLException {
    final String sql =
        "SELECT id, status, creation_time FROM SERVER_EVENT ORDER BY creation_time DESC LIMIT 1";

    try (final Connection connection = dataSource.createConnection()) {
      try (final PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.execute();
        try (final ResultSet resultSet = statement.getResultSet()) {
          if (resultSet.next()) {
            return Optional.of(getServerEventFromResultSet(resultSet));
          } else {
            return Optional.empty();
          }
        }
      }
    }
  }

  public List<ServerEvent> searchHistory(SearchHistoryDto historySearchDto) throws SQLException {
    String sortOrder = Optional.ofNullable(historySearchDto.getSort()).map(Enum::name).orElse(null);
    String sql =
        "SELECT id, creation_time, status "
            + "FROM server_event "
            + "WHERE (? IS NULL OR creation_time >= ?) "
            + "  AND (? IS NULL OR creation_time <= ?) "
            + "  AND (? IS NULL OR status = ?) "
            + "ORDER BY creation_time "
            + (sortOrder != null && sortOrder.equalsIgnoreCase("DESC") ? "DESC" : "ASC");

    List<ServerEvent> results = new ArrayList<>();

    try (final Connection connection = dataSource.createConnection()) {
      try (final PreparedStatement pstmt = connection.prepareStatement(sql)) {
        LocalDate fromDate = historySearchDto.getFromDate();
        LocalDate toDate = historySearchDto.getToDate();
        final String statusName =
            Optional.ofNullable(historySearchDto.getStatus()).map(Enum::name).orElse(null);
        pstmt.setObject(1, fromDate);
        pstmt.setObject(2, fromDate);
        pstmt.setObject(3, toDate);
        pstmt.setObject(4, toDate);
        pstmt.setObject(5, statusName);
        pstmt.setObject(6, statusName);

        try (ResultSet resultSet = pstmt.executeQuery()) {
          while (resultSet.next()) {
            results.add(getServerEventFromResultSet(resultSet));
          }
        }

        return results;
      }
    }
  }

  private ServerEvent getServerEventFromResultSet(final ResultSet resultSet) throws SQLException {
    final long id = resultSet.getLong("id");
    final String statusName = resultSet.getString("status");
    final Status status = Status.valueOf(statusName.toUpperCase());
    final LocalDateTime creationTime = resultSet.getObject("creation_time", LocalDateTime.class);
    return new ServerEvent(id, status, creationTime);
  }
}
