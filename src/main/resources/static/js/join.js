    function saveMember() {
        var form = document.getElementById('joinForm');

        var memberEmailFirst = form.elements['memberEmailFirst'].value.trim();
        var memberEmailLast = form.elements['memberEmailLast'].value.trim();
        var memberEmailMerge = ((memberEmailFirst != "" && memberEmailLast != "") ? memberEmailFirst + "@" + memberEmailLast : " ");

        form.elements['memberEmail'].value = memberEmailMerge;

/*
        // 필수 입력값 검증
        if (form.elements['memberId'].value.trim() === "") {
            alert("아이디를 입력하세요.");
            return;  // 아이디가 비어 있으면 폼 제출을 중지
        }

        if (form.elements['memberPassword'].value.trim() === "") {
            alert("비밀번호를 입력하세요.");
            return;  // 비밀번호가 비어 있으면 폼 제출을 중지
        }

        if (form.elements['memberPasswordCheck'].value.trim() === "") {
            alert("비밀번호 확인을 입력하세요.");
            return;  // 비밀번호 확인이 비어 있으면 폼 제출을 중지
        }

        if (form.elements['memberName'].value.trim() === "") {
            alert("이름을 입력하세요.");
            return;  // 이름이 비어 있으면 폼 제출을 중지
        }

        if (form.elements['memberPnum'].value.trim() === "") {
            alert("전화번호를 입력하세요.");
            return;  // 전화번호가 비어 있으면 폼 제출을 중지
        }

        if (memberEmail === "") {
            alert("이메일을 입력하세요.");
            return;  // 이메일이 비어 있으면 폼 제출을 중지
        }

        if (form.elements['memberBirth'].value.trim() === "") {
            alert("생년월일을 입력하세요.");
            return;  // 생년월일이 비어 있으면 폼 제출을 중지
        }
*/

        console.log(form);

        // 폼 데이터를 서버로 전송
        form.submit();
    }






/*  아이디 유효성 검사(1 = 중복 / 0 != 중복)  */
    $("#memberId").blur(function() {
        //let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
        let memberId = $('#memberId').val();
        $.ajax({
            url : '/member/idcheck.do',
            type : 'post',
            data:{memberId:memberId},
            success : function(data) {
                console.log("1 = 아이디중복o / 0 = 중복x : "+ data);
                   return;
                if (data == 1) {
                        // 1 : 아이디가 중복되는 문구
                        $("#idmsg").text("사용중인 아이디입니다");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");

                } else {

                    if( id == ""){
                        $("#idmsg").text("아이디를 입력해주세요");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");
                    }else if(!check.test(id)){
                        $("#idmsg").text("6~12자 영문, 숫자를 조합하세요");
                        $("#idmsg").css("color","red");
                        $("#idcheck").val("N");
                    }else{
                        $("#idmsg").text("");
                        $("#idcheck").val("Y");
                    }

                }

            }, error : function() {
                    console.log("실패");
                }
        });
    });