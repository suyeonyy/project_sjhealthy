/* 아이디 중복체크 */
$("#memberId").blur(function() {
   //console.log("아이디 중복체크 진입");
   //let check = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/;
   var memberId = $('#memberId').val();

   $.ajax({
       url : '../member/idCheck.do',
       type : 'post',
       data:{memberId:memberId},
       success : function(data) {
           //console.log("success");
           if (data == 1) {
                   // 1 : 아이디가 중복되는 문구
                   $("#memberIdMsg").text("사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
                   $("#memberIdMsg").css("color","red");
                   $("#memberIdPass").val("N");
           } else {
               if( memberId == ""){
                   $("#memberIdMsg").text("아이디를 입력해주세요.");
                   $("#memberIdMsg").css("color","red");
                  val("N");
               /*
               }else if(!check.test(id)){
                   $("#memberIdMsg").text("6~12자 영문, 숫자를 조합하세요");
                   $("#memberIdMsg").css("color","red");
                   $("#memberIdCheck").val("N");
               */
               }else{
                   $("#memberIdMsg").text("");
                   $("#memberIdPass").val("Y");
               }

           }

       }, error : function() {
               //console.log("fail");
       }
   });
});


/* 비밀번호, 비밀번호 확인 */
//비밀번호
function passwordCheck(obj) {
    var memberPassword = $("#memberPassword").val();
    var memberPasswordCheck = $("#memberPasswordCheck").val();
    var passMsg = document.getElementById("passMsg");
    var cpassMsg = document.getElementById("cpassMsg");

    //비밀번호 정규식 : 8~16자 영문, 숫자, 특수문자
    var check = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

    if (obj.value == "") {
        passMsg.innerHTML = "비밀번호를 입력해주세요.";
        passMsg.style.color = "red";
        $("#memberPasswordPass").val("N");
        return false;
    } else if (!check.test(obj.value)) {
        passMsg.innerHTML = "8~16자 영문, 숫자, 특수문자를 이용하여 입력해주세요.";
        passMsg.style.color = "red";
        $("#memberPasswordPass").val("N");
        return false;
    } else {
        passMsg.innerHTML = "";
        $("#memberPasswordPass").val("Y");
        return true;
    }

    if (memberPassword != "" && memberPasswordCheck != "") {
        if (memberPassword != memberPasswordCheck) {
            cpassMsg.innerHTML = "비밀번호가 다릅니다.";
            cpassMsg.style.color = "red";
            $("#memberPasswordPass").val("N");
            return false;
        } else {
            cpassMsg.innerHTML = "";
            $("#memberPasswordPass").val("Y");
            return true;
        }
    }
}

//비밀번호 확인
function cpasswordCheck() {
    var memberPassword, memberPasswordCheck, cpassMsg;
    memberPassword = $("#memberPassword").val();
    memberPasswordCheck = $("#memberPasswordCheck").val();
    var cpassMsg = document.getElementById("cpassMsg");

    if (memberPassword != "" && memberPasswordCheck != "") {
        if (memberPassword != memberPasswordCheck) {
            cpassMsg.innerHTML = "비밀번호가 다릅니다.";
            cpassMsg.style.color = "red";
            $("#memberPasswordCheckPass").val("N");
            return false;
        } else {
            cpassMsg.innerHTML = "";
            $("#memberPasswordCheckPass").val("Y");
            return true;
        }
    } else if (memberPasswordCheck == "") {
        cpassMsg.innerHTML = "비밀번호 확인을 입력해주세요.";
        cpassMsg.style.color = "red";
        $("#memberPasswordCheckPass").val("N");
        return false;
    } else if (memberPasswordCheck !== "" || memberPassword == memberPasswordCheck) {
        cpassMsg.innerHTML = "";
        $("#memberPasswordCheckPass").val("Y");
        return true;
    }
}


/* 이름 */
function memberNameCheck(obj) {
    var nameMsg = document.getElementById("nameMsg");
    var str = obj.value;
    var check = /^[ㄱ-ㅎ가-힣a-zA-Z]+$/;

    if (obj.value == "") {
        nameMsg.innerHTML = "이름을 입력해주세요.";
        nameMsg.style.color = "red";
        $("#memberNamePass").val("N");
        return false;
    } else if (!check.test(str)) {
        nameMsg.innerHTML = "한글 또는 영문을 사용해주세요.";
        nameMsg.style.color = "red";
        $("#memberNamePass").val("N");
        return false;
    } else {
        nameMsg.innerHTML = "";
        $("#memberNamePass").val("Y");
        return true; // 확인이 완료 되었을 때
    }
}


/* 전화번호 */
function memberPnumCheck(obj) {
    var pnumMsg = document.getElementById("pnumMsg");
    var str = obj.value;
    var check = /^\d{11}$/;

    if (obj.value == "") {
        pnumMsg.innerHTML = "전화번호를 입력해주세요.";
        pnumMsg.style.color = "red";
        $("#memberPnumPass").val("N");
        return false;
    } else if (!check.test(str)) {
        pnumMsg.innerHTML = "전화번호는 11자리 숫자만 입력해주세요.";
        pnumMsg.style.color = "red";
        $("#memberPnumPass").val("N");
        return false;
    } else {
        pnumMsg.innerHTML = "";
        $("#memberPnumPass").val("Y");
        return true; // 확인이 완료 되었을 때
    }
}


/* 이메일 */
function memberEmailCheck() {
    var emailMsg = document.getElementById("emailMsg");
    memberEmailFirst = $("#memberEmailFirst").val();
    memberEmailLast = $("#memberEmailLast").val();
    memberEmail = $("#memberEmail").val();

    var str1 = memberEmailFirst;
    var str2 = memberEmailLast;
    var check1 = /^[^ㄱ-ㅎ가-힣]+$/;
    var check2 = /^(.*[a-zA-Z])(?=.*[.])[a-zA-Z\.]+$/;

    if (memberEmailFirst == "" || memberEmailLast == "") {
        emailMsg.innerHTML = "이메일을 입력해주세요.";
        emailMsg.style.color = "red";
        $("#memberEmailPass").val("N");
        return false;
    } else if (!check1.test(str1)) {
        emailMsg.innerHTML = "한글은 사용 불가능합니다.";   //영문,숫자 모두 사용하여 조합
        emailMsg.style.color = "red";
        $("#memberEmailPass").val("N");
        return false;
    } else if (!check2.test(str2)) {
        emailMsg.innerHTML = "이메일 형식을 확인해주세요.";
        emailMsg.style.color = "red";
        $("#memberEmailPass").val("N");
        return false;

    } else if (memberEmailFirst !== "" && memberEmailLast !== "") {
        emailMsg.innerHTML = "";
        $("#memberEmailPass").val("Y");
        return true; // 확인이 완료 되었을 때
    }
}


/* 생년월일 */
function memberBirthCheck(obj) {
    var birthMsg = document.getElementById("birthMsg");
    var str = obj.value;
    var check = /^\d{8}$/;

    if (obj.value == "") {
        birthMsg.innerHTML = "생년월일을 입력해주세요.";
        birthMsg.style.color = "red";
        $("#memberBirthPass").val("N");
        return false;
    } else if (!check.test(str)) {
        birthMsg.innerHTML = "생년월일은 8자리 숫자로 입력해주세요.";
        birthMsg.style.color = "red";
        $("#memberBirthPass").val("N");
        return false;
    } else {
        birthMsg.innerHTML = "";
        $("#memberBirthPass").val("Y");
        return true; // 확인이 완료 되었을 때
    }
}

/* 가입 */
function saveMember() {
    var form = document.getElementById('joinForm');

    var memberIdPass = $("#memberIdPass").val();
    var memberPasswordPass = $("#memberPasswordPass").val();
    var memberPasswordCheckPass = $("#memberPasswordCheckPass").val();
    var memberNamePass = $("#memberNamePass").val();
    var memberPnumPass = $("#memberPnumPass").val();
    var memberEmailPass = $("#memberEmailPass").val();
    var memberBirthPass = $("#memberBirthPass").val();

    //이메일 형식 생성
    var memberEmailFirst = form.elements['memberEmailFirst'].value.trim();
    var memberEmailLast = form.elements['memberEmailLast'].value.trim();
    var memberEmailMerge = ((memberEmailFirst != "" && memberEmailLast != "") ? memberEmailFirst + "@" + memberEmailLast : " ");
    form.elements['memberEmail'].value = memberEmailMerge;

    /*
    console.log("데이터 비교");
    console.log( $("#memberId").val() );
    console.log( $("#memberPassword").val() );
    console.log( $("#memberName").val() );
    console.log( $("#memberPnum").val() );
    console.log( $("#memberEmail").val() );
    console.log( $("#memberBirth").val() );
    console.log( $("input[name='memberGender']:checked").val() );
    */

    if (memberIdPass == 'N') {
        alert("아이디를 확인해주세요.");
        $("#memberId").focus();
        return false;
    } else if (memberPasswordPass == 'N') {
        alert("비밀번호를 확인해주세요.");
        $("#memberPassword").focus();
        return false;
    } else if (memberPasswordCheckPass == 'N') {
        alert("비밀번호 확인을 확인해주세요.");
         $("#memberPasswordCheck").focus();
        return false;
    } else if (memberNamePass == 'N') {
        alert("이름을 확인해주세요.");
         $("#memberName").focus();
        return false;
    } else if (memberPnumPass == 'N') {
        alert("전화번호를 확인해주세요.");
        $("#memberPnum").focus();
        return false;
    } else if (memberEmailPass == 'N') {
        alert("이메일을 확인해주세요.");
        $("#memberEmail").focus();
        return false;
    } else if (memberBirthPass == 'N') {
        alert("생년월일을 확인해주세요.");
        $("#memberBirth").focus();
        return false;
    }
    else {
        //console.log("폼 데이터를 서버로 전송");
        //console.log(form);

        // 폼 데이터를 서버로 전송
        form.submit();
    }
}
