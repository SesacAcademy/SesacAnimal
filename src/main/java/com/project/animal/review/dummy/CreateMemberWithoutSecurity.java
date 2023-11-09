package com.project.animal.review.dummy;

import com.project.animal.member.domain.Member;

import java.time.LocalDateTime;

public class CreateMemberWithoutSecurity {
    public Member createMember(){
        Member member = new Member(1L,"user1@example.com", "password1", "John Doe", "123-456-7890", "Regular", "User", "A", LocalDateTime.parse("2023-11-06T10:00:00"), LocalDateTime.parse("2023-11-06T10:00:00"), LocalDateTime.parse("2023-11-05T14:30:00"));
        return member;
    }
    public void creatdDummymember(){
        Member member1 = new Member(1L,"user1@example.com", "password1", "John Doe", "123-456-7890", "Regular", "User", "A", LocalDateTime.parse("2023-11-06T10:00:00"), LocalDateTime.parse("2023-11-06T10:00:00"), LocalDateTime.parse("2023-11-05T14:30:00"));
        Member member2 = new Member(2L,"user2@example.com", "password2", "Jane Smith", "987-654-3210", "Premium", "User", "A", LocalDateTime.parse("2023-11-06T11:30:00"), LocalDateTime.parse("2023-11-06T11:30:00"), LocalDateTime.parse("2023-11-05T15:45:00"));
        Member member3 = new Member(3L,"admin@example.com", "adminpassword", "Admin User", "555-555-5555", "Admin", "Admin", "A", LocalDateTime.parse("2023-11-06T13:45:00"), LocalDateTime.parse("2023-11-06T13:45:00"), LocalDateTime.parse("2023-11-06T10:15:00"));
        Member member4 = new Member(4L,"user3@example.com", "password3", "Alice Johnson", "555-123-7890", "Regular", "User", "A", LocalDateTime.parse("2023-11-06T14:30:00"), LocalDateTime.parse("2023-11-06T14:30:00"), LocalDateTime.parse("2023-11-05T17:00:00"));
        Member member5 = new Member(5L,"user4@example.com", "password4", "Bob Wilson", "777-888-9999", "Regular", "User", "A", LocalDateTime.parse("2023-11-06T16:15:00"), LocalDateTime.parse("2023-11-06T16:15:00"), LocalDateTime.parse("2023-11-05T18:20:00"));
        Member member6 = new Member(6L,"manager@example.com", "managerpassword", "Manager One", "123-999-8888", "Manager", "Manager", "A", LocalDateTime.parse("2023-11-06T17:30:00"), LocalDateTime.parse("2023-11-06T17:30:00"), LocalDateTime.parse("2023-11-06T15:45:00"));
        Member member7 = new Member(7L,"user5@example.com", "password5", "Eva Brown", "333-222-1111", "Regular", "User", "A", LocalDateTime.parse("2023-11-06T18:45:00"), LocalDateTime.parse("2023-11-06T18:45:00"), LocalDateTime.parse("2023-11-05T19:10:00"));
        Member member8 = new Member(8L,"user6@example.com", "password6", "Sam Green", "555-777-6666", "Premium", "User", "A", LocalDateTime.parse("2023-11-06T20:00:00"), LocalDateTime.parse("2023-11-06T20:00:00"), LocalDateTime.parse("2023-11-06T17:30:00"));
        Member member9 = new Member(9L,"admin2@example.com", "adminpassword2", "Admin Two", "444-333-2222", "Admin", "Admin", "A", LocalDateTime.parse("2023-11-06T21:15:00"), LocalDateTime.parse("2023-11-06T21:15:00"), LocalDateTime.parse("2023-11-06T19:15:00"));

    }

}
