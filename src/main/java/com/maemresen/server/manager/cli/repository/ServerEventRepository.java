package com.maemresen.server.manager.cli.repository;

import com.maemresen.server.manager.cli.DbConnection;
import com.maemresen.server.manager.cli.model.dto.SearchHistoryDto;
import com.maemresen.server.manager.cli.model.entity.ServerEvent;
import com.maemresen.server.manager.cli.model.entity.Status;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerEventRepository {

  public void createNewEvent(Status status) throws SQLException {
    final String sql = "INSERT INTO SERVER_EVENT (STATUS, CREATION_TIME) VALUES (?1, ?2)";
    try (final PreparedStatement statmement = DbConnection.getConnection().prepareStatement(sql)) {
      statmement.setString(1, status.toString());
      statmement.setObject(2, LocalDateTime.now());
      statmement.execute();
    }
  }

  public Optional<ServerEvent> findLatest() throws SQLException {
    final String sql =
        "SELECT id, status, creation_time FROM SERVER_EVENT ORDER BY creation_time DESC LIMIT 1";

    try (final PreparedStatement statement = DbConnection.getConnection().prepareStatement(sql)) {
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

  public List<ServerEvent> searchHistory(SearchHistoryDto historySearchDto) throws SQLException {
    String sortOrder =
        Optional.ofNullable(historySearchDto.getSortDirection()).map(Enum::name).orElse(null);
    String sql =
        "SELECT id, creation_time, status "
            + "FROM server_event "
            + "WHERE (? IS NULL OR creation_time >= ?) "
            + "  AND (? IS NULL OR creation_time <= ?) "
            + "  AND (? IS NULL OR status = ?) "
            + "ORDER BY creation_time "
            + (sortOrder != null && sortOrder.equalsIgnoreCase("DESC") ? "DESC" : "ASC");

    List<ServerEvent> results = new ArrayList<>();

    try (final PreparedStatement pstmt = DbConnection.getConnection().prepareStatement(sql)) {
      LocalDateTime fromDate = historySearchDto.getFromDate();
      LocalDateTime toDate = historySearchDto.getToDate();
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

  private ServerEvent getServerEventFromResultSet(final ResultSet resultSet) throws SQLException {
    final long id = resultSet.getLong("id");
    final String statusName = resultSet.getString("status");
    final Status status = Status.valueOf(statusName.toUpperCase());
    final LocalDateTime creationTime = resultSet.getObject("creation_time", LocalDateTime.class);
    return new ServerEvent(id, status, creationTime);
  }
}
