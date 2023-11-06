package com.project.animal.missing.dummy;

import com.project.animal.missing.domain.MissingPost;
import com.project.animal.missing.dto.MissingListResDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MissingPostDummy {
  private static List<MissingPost> dummyPost = List.of(
          new MissingPost(1, 1,"Lost Friendly Dog, faepoefjawpofawpofjawfeawfoawpj", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd', "persian","Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd', "persian","Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd', "persian","Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd', "persian","Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true),
          new MissingPost(1, 1,"Lost Friendly Dog", 'd',"persian", "Brown", 150, "Central Park", LocalDateTime.now(), "Sma dog with a red collar and a friendly demeanor.", 0, 'L', true)


  );

  public static List<MissingPost> getList() {
    return dummyPost;
  }

  public static List<MissingListResDto> getDummyDto() {
   return dummyPost.stream().map((entity) -> MissingListResDto.fromMissingPost(entity)).collect(Collectors.toList());
  }
}
