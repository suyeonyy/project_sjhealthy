package com.example.sjhealthy.controller;
/*정보 정리*/
public class InfotestController {

    /* DTO.java
       [lombok 라이브러리 종류]
        ############################################################
        @Getter
        @Setter
        => DTO 에 정의한 필드에 대해 Getter, Setter 메서드를 자동으로 만들어줌

        ############################################################
        @NoArgsConstructor
        => DTO 에 정의한 필드에 대해 파라미터가 없는 디폴트 생성자를 생성
            예시)
            @NoArgsConstructor
            public class Person {
                private String name;
                private int age;
                // getters and setters
            }
            -> @NoArgsConstructor 사용하면
            public class Person {
                private String name;
                private int age;

                public Person(){}
            }

        ############################################################
        @AllArgsConstructor
        => DTO 에 정의한 모든 필드 값을 파라미터로 받는 생성자를 생성
            예시)
            @AllArgsConstructor
            public class Person {
                private String name;
                private int age;
                // getters and setters
            }
            -> @AllArgsConstructor 사용하면
            public class Person {
                private String name;
                private int age;

                public Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
            }

        ############################################################
        @ToString
        => DTO 객체가 가진 필드 값 출력 시, ToString 메서드를 자동으로 생성
     */



    /* .html <-> DTO.java
        ############################################################
        - html의 name과 DTO의 필드가 동일하면, 스프링이 DTO 객체를 하나 만들어서
          Setter메서드를 만들어서 각각 호출하면서 html에 작성한 값을 담아줌
     */



    /* Controller.java
        ############################################################
        - 생성자 주입 = 생성자를 통해서 의존 관계를 주입 받음
        - 생성자 주입 사용해야 하는 이유
        => https://programforlife.tistory.com/111

            * 생성자를 주입함으로써, Controller 클래스에 대한 객체를 스프링빈으로 등록할 때, 자동적으로
            서비스에 대한 객체를 주입받음 (자원을 사용할 수 있는 권한이 생김)
            * 생성자 주입 시 @Autowired 선언해야함

        ############################################################
        @RequestMapping
        => 요청 정보를 매핑, 해당 url이 호출되면 해당 메소드가 호출

        ############################################################
        @GetMapping
        => HTTP GET 요청을 처리하는 메서드를 맵핑(@RequestMapping)하는 어노테이션.
           메서드(url)에 따라 어떤 페이지를 보여줄지 결정하는 역할

        ############################################################
        @PostMapping
        => 메시지 바디를 통해 서버로 요청 데이터 전달.
           서버는 요청 데이터를 처리(메시지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행)

        ############################################################
        @ModelAttribute와 @RequestBody의 차이
        => @ModelAttribute는 HTTP 요청의 파라미터를 객체로 바인딩하기 위해 사용하고,
           @RequestBody는 HTTP 요청의 본문(body)에 담긴 데이터를 객체로 바인딩하기 위해 사용
     */



    /* Service.java
        ############################################################
        - 레파지토리 호출
          -> 상단에 레파지토리 정의하여 레파지토리를 호출

            * 레파지토리 호출 시, final 붙이는 이유?
              ->런타임중에 주입된 객체의 변경을 방지하기 위해서는 자바의 기본문법인 final 키워드로 방어처리를 위함

        ############################################################
        @RequiredArgsConstructor
        => final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드에 대해 생성자를 자동으로 생성해줌

        ############################################################
        public void join(MemberDTO memberDTO) {
            1. dto -> entity 변환
               : Entity.java에서 toMemberEntity 메서드 생성
            2. repository의 join 메서드 호출
               : MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
            3. (조건: entity 객체를 넘겨야함)
               : memberRepository.save(memberEntity);
               : * Repository의 save 메서드는 만드는 게 아니라, jpa가 제공해주는 메서드임. 그래서 메서드 이름을 save로 넘겨야함

        }
     */



    /* Entity.java
        ############################################################
        - 테이블을 담당

        ############################################################
        @Id
        => primary키 정의
     */



    /* Repository.java
        ############################################################
        - public interface MemberRepository extends JpaRepository<MemberEntity, String>
          => Repository 인터페이스는 JpaRepository를 상속받는다
          => 첫 번째 인자 : 어떤 Entity 클래스를 받을 것인지 정의
          => 두 번째 인자 : Entity 클래스의 pk는 어떤 타입인지

        ############################################################
     */

}
