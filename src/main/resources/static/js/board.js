document.addEventListener("DOMContentLoaded", ()=> {
const deleteBtn = document.getElementById("deleteButton");

if (deleteBtn){
    deleteBtn.addEventListener("click", (e)=> {

            if (!window.confirm('정말로 삭제하시겠습니까?')){
                event.preventDefault(); // 취소시 이벤트 발생 없음
                return false;
            }
        }
    );
}});


/* 댓글 등록 */
function insertComment(boardId, memberId) {
    console.log("클릭",boardId,memberId);

    var commentContent = $("#commentContent").val();

    /*
    if(loginId==null){
        alert("로그인이 필요합니다.");
        return false;
    }
    */

    if (commentContent.value == "") {
        alert("댓글을 입력해 주세요.");
        $("#commentContent").focus();
        return false;
    }

    var uri = "../board/insertComment" ;
    var headers = {"Content-Type": "application/json", "X-HTTP-Method-Override": "POST"};
    var params = {"boardId": boardId, "commentContent": commentContent, "memberId": memberId};

    $.ajax({
        url: uri,
        type: "POST",
        headers: headers,
        dataType: "json",
        jsonb: false,
        data: JSON.stringify(params),
        success: function(response) {
            console.log("댓글등록 성공");
            console.log(response.result);
            return;

            if (response.result == true) {

            }else {
                alert("댓글 등록에 실패하였습니다.");
                return false;
            }

            printCommentList();
            content.value = "";
        },
        error: function(xhr, status, error) {
            alert("에러가 발생하였습니다.");
            return false;
        }
    });
}