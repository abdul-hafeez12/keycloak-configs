package com.ahafeez.services;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import com.ahafeez.dto.UserRequestDto;
import com.ahafeez.dto.UserResponseDto;
import com.ahafeez.dto.UserTlDto;
import com.ahafeez.querystores.UserTlUserAuthTlQuery;
import com.ahafeez.util.ObjectMapper;

import org.jboss.resteasy.api.validation.ViolationReport;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

      @Inject
      AgroalDataSource defaultDataSource;

    public UserResponseDto fnGetUser(UserRequestDto user) {
        UserTlDto userTlDto = new UserTlDto();
        String[] tokens = user.getUserId().split("-", 2);

        UserResponseDto userResDto = new UserResponseDto();

        if(isFourDigitNumber(tokens[0])){
        userTlDto.setUserName(tokens[1]);
        userTlDto.setBranchCode(new BigDecimal(tokens[0]));

         
                    String userQuery = UserTlUserAuthTlQuery.getUserWithAuthQuery(userTlDto);
                    System.out.println("fnGetUser: userQuery - {0}"+userQuery);
                    List<UserTlDto> users;
                    Optional<List<UserTlDto>> optionalUsers;
                    try {
                        optionalUsers = selectQuery(UserTlDto.class, userQuery,
                                Arrays.asList("branchCode", "userName", "password","fullName"), tokens[0]);
                        System.err.println("fnGetUser: optionalUsers - {0}"+optionalUsers);
                        users = optionalUsers.orElse(null);
                        if (users == null) {
                            ViolationReport violationReport = new ViolationReport();
                            violationReport.setException("DTO Parsing problem");
                            userResDto.setViolationReport(violationReport);
                            userResDto.setUserTlDto(null);
                        } else if (users.isEmpty()) {
                            userResDto.setViolationReport(null);
                            userResDto.setUserTlDto(null);
                        } else if (users.size() == 1) {
                            userResDto.setViolationReport(null);
                            userResDto.setUserTlDto(users.get(0));
                        }

                    } catch (SQLException e) {
                        ViolationReport violationReport = new ViolationReport();
                        violationReport
                                .setException("DB Sql Exception: Either server is down or not responding on time");
                        userResDto.setViolationReport(violationReport);
                        userResDto.setUserTlDto(null);
                        System.out.println(e.getMessage());
                        System.out.println(e.getStackTrace());
                    }

                    return userResDto;
                }
                else {
                    ViolationReport violationReport = new ViolationReport();
                            violationReport.setException("No User Found");
                            userResDto.setViolationReport(violationReport);
                            userResDto.setUserTlDto(null);

                            return userResDto;
                }
            }

        

    public <T> Optional<List<T>> selectQuery(Class<T> type, String query, List<String> projections, String branchCode)
            throws SQLException {

        List<T> list = new ArrayList<>();
        try (
                Connection conn = defaultDataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(query);) {
            while (rst.next()) {
                T t = type.getDeclaredConstructor().newInstance();
                ObjectMapper.loadResultSetIntoObject(rst, t, projections);
                list.add(t);
            }

        } catch (SQLException e) {
            throw new SQLException();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return Optional.empty();
        }
        return Optional.of(list);
    }

    public boolean isFourDigitNumber(String input) {
    return input != null && input.length() == 4 && input.matches("\\d{4}");
}

}
