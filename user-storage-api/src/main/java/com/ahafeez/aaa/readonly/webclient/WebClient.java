package com.ahafeez.aaa.readonly.webclient;

import com.ahafeez.aaa.readonly.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

// import jakarta.ws.rs.ProcessingException;
// import jakarta.ws.rs.client.Client;
// import jakarta.ws.rs.client.ClientBuilder;
// import jakarta.ws.rs.client.WebTarget;
// import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.jboss.logging.Logger;

public class WebClient {
   private String dataaccessUrl;
   private static final Logger logger = Logger.getLogger(WebClient.class);

   public WebClient(String dataaccessUrl) {
      this.dataaccessUrl = dataaccessUrl;
   }

   // public UserDto getUserByUserName(String username) {
   // System.out.println("HERE2"+username);
   // Client client = ClientBuilder.newBuilder().build();
   // WebTarget target = client.target(this.dataaccessUrl +
   // "/dataaccess/getUser?userId=" + username);

   // Response response;
   // try {
   // response = target.request().get();
   // } catch (ProcessingException var6) {
   // logger.warn("Dataaccess Server is not accessible");
   // return null;
   // }

   // logger.debug(" getUserByUsername: response.getStatus - " +
   // response.getStatus());
   // UserDto user;
   // if (response.getStatus() == 200) {
   // user = (UserDto)response.readEntity(UserDto.class);
   // user.setUserId(username);
   // logger.debug(" getUserByUsername: userDto - " + user);
   // user.setPassword(user.getPassword());
   // } else {
   // user = null;
   // }

   // response.close();
   // return user;
   // }

   public UserDto getUserByUserName(String username) {
      System.out.println("HERE2" + username);
      HttpURLConnection conn = null;
      try {
         String urlStr = this.dataaccessUrl + "/dataaccess/getUser?userId="
               + URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
         URL url = new URL(urlStr);
         conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setRequestProperty("Accept", "application/json");

         int status = conn.getResponseCode();
         logger.debug(" getUserByUsername: response.getStatus - " + status);

         if (status == 200) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
               StringBuilder jsonBuilder = new StringBuilder();
               String inputLine;
               while ((inputLine = in.readLine()) != null) {
                  jsonBuilder.append(inputLine);
               }

               String json = jsonBuilder.toString();
               ObjectMapper mapper = new ObjectMapper();
               UserDto user = mapper.readValue(json, UserDto.class);
               String[] tokens = username.split("-", 2);
               user.setUserId(tokens[1]);
               user.setPassword(user.getPassword()); // Optional if needed
               System.out.println(" getUserByUsername: userDto - " + user);

               logger.debug(" getUserByUsername: userDto - " + user);
               return user;
            }
         } else {
            return null;
         }
      } catch (Exception e) {
         logger.warn("Dataaccess Server is not accessible", e);
         return null;
      } finally {
         if (conn != null) {
            conn.disconnect();
         }
      }
   }
}
