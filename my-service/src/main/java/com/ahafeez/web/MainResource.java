package com.ahafeez.web;

import com.ahafeez.dto.UserTlDto;
import com.ahafeez.dto.UserRequestDto;
import com.ahafeez.dto.UserResponseDto;
import com.ahafeez.services.UserService;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.api.validation.ViolationReport;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/dataaccess")
public class MainResource {

      @Inject
      UserService userService;

      @GET
      @Produces(MediaType.TEXT_PLAIN)
      public String hello() {
            return "Hello RESTEasy";
      }

      @Path("/getUser")
      @GET
      @Produces(MediaType.APPLICATION_JSON)
      @APIResponse(responseCode = "404", description = "User was not found in USER_TL and nothing is returned")
      @APIResponse(responseCode = "200", description = "The reponse of the endpoint returns with found user in USER_TL", content = @Content(schema = @Schema(implementation = UserTlDto.class)))
      @APIResponse(responseCode = "400", description = "Malformed request. The request was not in the format the endpoint expects it to be.", content = @Content(schema = @Schema(implementation = ViolationReport.class)))
      @APIResponse(responseCode = "503", description = "Malformed request", content = @Content(schema = @Schema(implementation = ViolationReport.class)))
      public Response getUser(@BeanParam @Valid UserRequestDto user) {
            System.out.println("getUser : {0}"+user.getUserId());
            UserResponseDto userResDto = userService.fnGetUser(user);

            if (userResDto.getViolationReport() == null && userResDto.getUserTlDto() != null) {
                  return Response.ok().type(MediaType.APPLICATION_JSON).entity(userResDto.getUserTlDto())
                              .build();
            } else if (userResDto.getViolationReport() != null) {
                  return Response.status(503).type(MediaType.APPLICATION_JSON)
                              .entity(userResDto.getViolationReport()).build();
            }
            return Response.status(404).build();

      }

}

