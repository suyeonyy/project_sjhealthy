document.addEventListener("DOMContentLoaded", ()=>{

    // 검색 정렬
    const searchButton = document.getElementById("searchButton");

    searchButton.addEventListener("click", async (e)=> {
        const search = document.getElementById("searchByStoreName").value;

        if (!search){
            e.preventDefault();
            alert("검색어를 입력하세요.");
            return false;
        }

        try {
            const url = "/sjhealthy/recommend/sort/" + encodeURIComponent(search);
            // 한글(가게 이름)을 보내면 깨질 수 있어 인코딩 해서 보내고 디코딩 해서 읽기

            const response = await fetch(url);
            const data = await response.json(); // 응답을 json으로 변환

            // 검색 결과가 1개가 아닐 때(=> 엔티티 배열이 아닐 때) 배열로 바꿔 처리
            // 안 바꾸면 forEach에서 오류가 난다.
            const array = Array.isArray(data) ? data : [data];

            // 검색 결과를 출력
            const tableBody = document.getElementById("recTable").getElementsByTagName("tbody")[0];
            // 이 메서드는 HtmlCollection 을 반환해서 tbody가 여러개면 뒤에 [0], [1] 등 인덱스를 사용해 접근
            // tbody 하나면 안 써도 된다는데 안 쓰니까 tbody랑 제대로 연결 안 돼서 씀

            // 내용 비움
            tableBody.innerHTML = "";

                array.forEach((item, index) => {
                    // 행 추가
                    const row = tableBody.insertRow();

                    // 열 추가
                    const cell1 = row.insertCell(0);
                    const cell2 = row.insertCell(1);
                    const cell3 = row.insertCell(2);
                    const cell4 = row.insertCell(3);
                    const cell5 = row.insertCell(4);
                    const cell6 = row.insertCell(5);
                    const cell7 = row.insertCell(6);
                    const cell8 = row.insertCell(7);
                    const cell9 = row.insertCell(8);

                    // 내용 추가
                    // cell1.value = item.recId; // td엔 value 없음
                    const input1 = document.createElement("input");
                    input1.type = "button";
                    input1.className ="detailBtn";
                    input1.value = "상세보기";
                    cell1.appendChild(input1);

                    const input2 = document.createElement("input");
                    input2.className = "recId";
                    input2.value = item.recId;
                    input2.type = "text";
                    input2.readOnly = true;
                    cell2.appendChild(input2);

                    cell3.textContent = item.recStore;
                    cell4.textContent = item.recMenu;
                    cell5.textContent = item.memberId;
                    cell6.textContent = item.createDate;
                    cell7.textContent = item.recViews;

                    const dataRecY = document.createElement("button");
                    dataRecY.className = "recY";
                    dataRecY.textContent = "좋아요";
                    dataRecY.onclick = (e) => chooseLikeButton(e, index); // 이렇게 함수의 참조를 넘겨줘야 한다. 이벤트가 발생할 때 함수 호출
                    // recY.onclick = chooseLikeButton(); 이렇게 하면 바로 실행되어 반환값을 넘겨주는 형태라 X
                    cell8.appendChild(dataRecY);

                    const dataRecN = document.createElement("button");
                    dataRecN.className = "recN";
                    dataRecN.textContent = "싫어요";
                    dataRecN.onclick = (e) => chooseDislikeButton(e, index);
                    cell9.appendChild(dataRecN);
                });
        } catch (error) {
            console.log('에러 = ' + error);
        }
    });


    const writeButton = document.getElementById("writeButton");
    const loginId = document.getElementById("loginId").value;
    console.log('loginId = ' + loginId);

    // 글 작성은 회원 전용
    if (writeButton){
        writeButton.addEventListener("click", async (e)=> {

            if (!loginId){
                // 값이 없을 때는 null 이 아니라 "" 빈 문자열이 보내진다.
                // 따라서 서버에서 보낸 값을 text로 설정하고 value를 읽어와도 해당 속성 값이 없으니 null이 아니라 ""를 읽어오게 됨
                // 서버에서 null을 보내도 렌더링 되는 과정에서 ""가 되기 때문에
                // 자바스크립트 조건문에서 null과 비교하는 건 의미가 없다.
                // !로 하면 null일 때랑 빈 문자열일 때 둘 다 false로 판단하니까 이렇게 하면 됨
                e.preventDefault(); // defaultPrevented는 이벤트가 취소됐는지 확인하는 메서드로 다른 거다. 잘못 쓰지 않도록 하자
                alert('글은 회원만 작성할 수 있습니다.');
                return false;
            }
        });
    }

    // 좋아요 싫어요 버튼 연결
    // const recY = document.getElementById("recY").value;
    // 버튼이 1개가 아니라 글마다 존재하니까 querySelectorAll()을 이용해야 한다.
    const recY = document.querySelectorAll(".recY");
    recY.forEach((Y, index) => { // index는 Y가 recY에서 몇 번째 요소인지를 나타내는 인덱스
        Y.addEventListener("click", (e)=> chooseLikeButton(e, index));
    });

    const recN = document.querySelectorAll(".recN");
    recN.forEach((N, index) => {
        // 싫어요 버튼 연결
        N.addEventListener("click", (e)=> chooseDislikeButton(e, index));
    });

    async function chooseLikeButton(e, index){
        console.log("좋아요 이벤트 리스너 연결 " + index);
        e.preventDefault();
        // 쿼리셀렉터로는 value를 가져올 수 있으나 All일 때는 이렇게 해서 가져와야 한다.
        const recIds = document.querySelectorAll(".recId");
        console.log("recIds = " + recIds);
        const recId = recIds[index].value; // 각 value를 가져옴
        console.log("recId = " + recId);


        // id당 1번 버튼 누르기 가능(좋아요/싫어요 중 택1)
        if (!loginId){
            e.preventDefault();
            console.log("회원 전용");
            alert('회원 전용 기능입니다.');
            return false;
        }

        try {
            const response = await fetch("/sjhealthy/recommend/like", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    recId: recId,
                    action: "like"
                    // memberId: loginId 세션에서 꺼내 씀
                })
            });
            const data = await response.json();
            alert(data.message);

            if (data.likeCount != null){
                const like = document.getElementById("detail-like"+index);
                const dislike = document.getElementById("detail-dislike"+index);

                like.textContent = data.likeCount;
                dislike.textContent = data.dislikeCount;
            }
            
        } catch (error){
            console.log('오류 발생: ', error);
        }
    };


    async function chooseDislikeButton(e, index){
        console.log("싫어요 이벤트 리스너 연결 " + index);
            e.preventDefault();
            const recIds = document.querySelectorAll(".recId");
            const recId = recIds[index].value;

            // id당 1번 버튼 누르기 가능(좋아요/싫어요 중 택1)
            if (!loginId){
                e.preventDefault();
                alert('회원 전용 기능입니다.');
                return false;
            }

            try {
            const response = await fetch("/sjhealthy/recommend/dislike", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    recId: recId,
                    action: "dislike"
                    // memberId: loginId 세션에서 꺼내 씀
                })
            });
            const data = await response.json();
            alert(data.message);

            if (data.likeCount != null){
                const like = document.getElementById("detail-like"+index);
                const dislike = document.getElementById("detail-dislike"+index);

                like.textContent = data.likeCount;
                dislike.textContent = data.dislikeCount;
            }
        } catch (error){
            console.log('오류 발생: ', error);
        }

    };

    const detailBtns = document.querySelectorAll(".detailBtn");
    
    // 마지막으로 열린 상세페이지 추적용
    let lastOpendRow = null;

    detailBtns.forEach((btn, index) => { // index는 Y가 recY에서 몇 번째 요소인지를 나타내는 인덱스
        
        btn.addEventListener("click", ()=> toggleDetail(index));
    });

    async function toggleDetail(index) {
        const recIds = document.querySelectorAll(".recId");
        console.log("recIds = " + recIds);
        const recId = recIds[index].value; // 각 value를 가져옴
        console.log("recId = " + recId);
        try {
            const response = await fetch("/sjhealthy/recommend/count/" + recId);
            const data = await response.json();
    
            const likeTotal = data.likeCount;
            const dislikeTotal = data.dislikeCount;
    
            
            // 받은 정보 출력
            const detailDivs = document.querySelectorAll(".detail");
            const detailDiv = detailDivs[index];

            // 요소의 최종 스타일 / detailDiv.style.display로 접근했더니 style을 읽지 못함
            // getComputedStyle: 스타일 속성을 객체로 반환
            if (detailDiv){
                if (lastOpendRow && lastOpendRow !== detailDiv){
                    lastOpendRow.style.display = "none"; // 다른 창 열려있으면 닫음
                }

                const computedStyle = window.getComputedStyle(detailDiv);
                
                // const detailBtn = document.getElementById("detailBtn");
                const likeCount = document.getElementById("detail-like"+index);
                const dislikeCount = document.getElementById("detail-dislike"+index);
        
                if (computedStyle.display === "none"){ // 이건 읽기만 가능이라 바꾸는 건 인라인 속성으로 바꿔줌
                    // 접혀 있으면 펴줌
                    detailDiv.style.display = "table-row"; // tr 열기
                    detailDiv.classList.add("expanded");
                    lastOpendRow = detailDiv;
                    // detailBtn.innerText = "접기";

                    likeCount.textContent = likeTotal;
                    dislikeCount.textContent = dislikeTotal;
                } else {
                    // 펴져 있으면 접어줌
                    detailDiv.style.display = "none";
                    detailDiv.classList.remove("expanded");
                    lastOpendRow = null;
                    // detailBtn.innerText = "상세보기";
                }}
        } catch (error){
            console.log("오류 발생: " + error);
        }
    };

});
