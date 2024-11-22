   /* 아이디 중복체크 */
   $("#memberId").blur(function() {
       console.log("아이디 중복체크 진입");
       //let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
       var memberId = $('#memberId').val();

       $.ajax({
           url : '../member/idCheck.do',
           type : 'post',
           data:{memberId:memberId},
           success : function(data) {
               console.log("success");
               if (data == 1) {
                       // 1 : 아이디가 중복되는 문구
                       $("#memberIdMsg").text("사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
                       $("#memberIdMsg").css("color","red");
                       $("#memberIdCheck").val("N");
               } else {
                   if( memberId == ""){
                       $("#memberIdMsg").text("아이디를 입력해주세요.");
                       $("#memberIdMsg").css("color","red");
                       $("#memberIdCheck").val("N");
                   /*
                   }else if(!check.test(id)){
                       $("#memberIdMsg").text("6~12자 영문, 숫자를 조합하세요");
                       $("#memberIdMsg").css("color","red");
                       $("#memberIdCheck").val("N");
                   */
                   }else{
                       $("#memberIdMsg").text("");
                       $("#memberIdCheck").val("Y");
                   }

               }

           }, error : function() {
                   console.log("fail");
           }
       });
    });

    /* 비밀번호, 비밀번호 확인 */
    //비밀번호
    function checkPassword(obj) {
        var passMsg = document.querySelector("#passMsg");
        var pass = document.getElementById("pass");
        var cpass = document.getElementById("cpass");
        var cpassMsg = document.getElementById("cpassMsg");

        //비밀번호 정규식 : 8~16자 영문, 숫자, 특수문자
        var check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

        if (obj.value == "") {
            passMsg.innerHTML = "비밀번호를 입력해주세요";
            passMsg.style.color = "red";
            return false;
        } else if (!check.test(obj.value)) {
            passMsg.innerHTML = "8~16자 영문, 숫자, 특수문자를 사용하세요";
            passMsg.style.color = "red";
            return false;
        } else {
            passMsg.innerHTML = "";
            return true;
        }

        if (pass.value != "" && cpass.value != "") {
            if (pass.value != cpass.value) {
                cpassmsg.innerHTML = "비밀번호가 다릅니다";
                cpassmsg.style.color = "red";
                return false;
            } else {
                cpassmsg.innerHTML = "";
                return true;
            }
        }

    }




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