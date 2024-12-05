document.addEventListener("DOMContentLoaded", ()=> {
    const deleteGoogleOAuth = document.getElementById("deleteGoogleOAuth");

    deleteGoogleOAuth.addEventListener("click", async (e) => {
    console.log("연동해제 클릭이벤트");
        // get 방식으로 요청을 보내 토큰을 받아옴
        const memberId = document.getElementById("memberId").value; // 나중에 맞게 변경. loginId일수도
    console.log("아이디 = " + memberId);

        try {
            // 아이디를 보내 탈퇴처리하고 액세스토큰 받아옴
            const response = await fetch("/sjhealthy/member/delete/google/" + memberId);
            const accessToken = await response.text(); // 토큰이 json 형태 아니라고 오류나서
    console.log("accessToken = " + accessToken);

            // 토큰 만료시 재로그인, 로그아웃 연결도 해야하는데..

            try {
                console.log("토큰 = " + accessToken);
                // 액세스 토큰을 담아 구글에 연동 해제 요청
                const url = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
                const response = await fetch(url);

                if (response.ok){
                    alert("탈퇴가 완료되었습니다.");
                    window.location.href = "http://localhost:8081/sjhealthy";
                }
            } catch(error){
                console.log("error = ", error);
            }


        } catch (error){
            console.log("error = " + error);
        }
    });
});
