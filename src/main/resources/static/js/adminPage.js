    document.addEventListener("DOMContentLoaded", ()=>{

        // 네비게이션바
        const memberNav = document.getElementById("memberNav");
        const postNav = document.getElementById("postNav");
        const content = document.getElementById("content");

        let currentPageM = 1;
        const pageSizeM = 10;
        
        // 회원 관리창
        memberNav.addEventListener("click", (e)=> {
            loadMemberData(currentPageM);
            memberNav.disabled = true; // 현재 버튼 비활성화
            postNav.disabled = false; // 나머지 버튼 활성화
        });

        // 멤버 관리창
        async function loadMemberData(page){
            content.innerHTML = ""; // 기존 내용 비움
            currentPageM = page;

            try {
                const response = await fetch("/sjhealthy/admin/member?page=" + page);
                
                if (response.status === 204){
                    // 게시물이 존재하지 않을 때
                    const h3 = document.createElement("h3");
                    h3.textContent = "게시물이 존재하지 않습니다.";
                    content.appendChild(h3);

                    const pagination = document.getElementById("pagination");
                    pagination.innerHTML = ""; // 기존 내용 초기화
                } else if(response.status === 403){
                    // 관리자 아님
                    alert("관리자 전용 기능입니다.");
                    window.location.href = "/sjhealthy";
                    return; 
                } else if (response.ok){ // 성공적으로 데이터를 받았을 때(message가 null로 응답됨)
                    const data = await response.json();

                    const memberList = data.data.content;

                    const table = document.createElement("table");
                    table.className ="table table-hover text-center table-bordered mt-4";

                    const thead = document.createElement("thead");
                    thead.innerHTML = `
                            <tr>
                                <th>탈퇴처리</th>
                                <th>회원 아이디</th>
                                <th>가입일</th>
                                <th>이름</th>
                                <th>이메일</th>
                                <th>성별</th>
                                <th>생년월일</th>
                            </tr>
                            `;

                    table.appendChild(thead);

                    const tbody = document.createElement("tbody");
                    memberList.forEach((member, index) =>{
                        const row = document.createElement("tr");

                        const deleteCell = row.insertCell(0);
                        const deleteBtn = document.createElement("input");
                        // deleteCell.className = "adminButton";
                        deleteBtn.type = "button";
                        deleteBtn.id = "deleteMemberBtn"+index;
                        deleteBtn.className = "adminButton";
                        deleteBtn.value = "삭제";
                        deleteBtn.onclick = (e) => deleteMember(index, member.memberId, member.memberDivision);
                        deleteCell.appendChild(deleteBtn);
                        // row.append(deleteCell);

                        const memberId = document.createElement("td");
                        memberId.textContent = member.memberId;
                        row.append(memberId);

                        const createDate = document.createElement("td");
                        const date = member.createDate;
                        const year = date.substring(0, 4);
                        const month = date.substring(4, 6);
                        const day = date.substring(6, 8);
                        createDate.textContent = year + "/" + month + "/" + day;
                        row.append(createDate);

                        const memberName = document.createElement("td");
                        memberName.textContent = member.memberName;
                        row.append(memberName);

                        const memberEmail = document.createElement("td");
                        memberEmail.textContent = member.memberEmail;
                        row.appendChild(memberEmail);

                        const memberGender = document.createElement("td");
                        memberGender.textContent = member.memberGender;
                        row.appendChild(memberGender);

                        const memberBirth = document.createElement("td");
                        const date2 = member.memberBirth;
                        const year2 = date2.substring(0, 4);
                        const month2 = date2.substring(4, 6);
                        const day2 = date2.substring(6, 8);
                        memberBirth.textContent = year2 + "/" + month2 + "/" + day2;
                        row.appendChild(memberBirth);

                        tbody.appendChild(row);
                    });
                    table.appendChild(tbody);
                    content.appendChild(table);

                    displayPagination(data.data.page.totalPages, page); // 페이지 버튼 생성
                    
                } 
            } catch(error){
                console.log("Error = " + error);
            }
        }

        function deleteMember(index, memberId, memberDivision){
            // 회원 탈퇴 버튼
            const deleteMemberBtn = "deleteMemberBtn" + index; 

            document.addEventListener("click", async (e) => {
                if (e.target.id === deleteMemberBtn){
                    if (!window.confirm("정말로 삭제하시겠습니까?")){
                        e.preventDefault();
                        return false;
                    }

                    if (memberDivision === "G"){
                        try {
                            // 아이디를 보내 탈퇴처리하고 액세스토큰 받아옴
                            const response = await fetch("/sjhealthy/member/delete/google/" + memberId);
                            const accessToken = await response.text(); // 토큰이 json 형태 아니라고 오류나서
                    
                            try {
                                // 액세스 토큰을 담아 구글에 연동 해제 요청
                                const url = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
                                const response = await fetch(url);
                    
                                if (response.ok){
                                    alert("탈퇴가 완료되었습니다.");
                                }
                            } catch(error){
                                console.log("error = ", error);
                            }
                        } catch (error){
                            console.log("error = " + error);
                        }

                    } else if (memberDivision === "K"){
                        try{
                            const response = await fetch("/sjhealthy/member/delete/kakao/" + memberId, { method: "GET" });
                            if (response.ok){
                                const result = await response.json();
                                alert("탈퇴가 완료되었습니다.");
                                window.location.href = "http://localhost:8081/sjhealthy";
                            }
                        } catch(error){
                            console.log("error = ", error);
                        }
                    }
                } else if (memberDivision === "S"){
                    try {
                        const response = await fetch("/sjhealthy/admin/member/delete/" + memberId);
                        const data = await response.json();

                        if (!data){
                            alert(data.message);
                        } else {
                            alert(data.message);
                            loadMemberData(currentPage);
                        }
                    } catch(error){
                        console.log("Error = " + error);
                    }
                }
            });
        }
        // 기본으로 회원 관리창
        loadMemberData(currentPageM);

        // 게시글 관리
        let currentPage = 1;
        const pageSize = 10;
        
        // post 네비버튼 누르면 자동으로 로드(1페이지로)
        postNav.addEventListener("click", (e)=> {
            loadBoardData(currentPage);
            postNav.disabled = true; // 현재 버튼 비활성화
            memberNav.disabled = false; // 나머지 버튼 활성화
        });

        // 페이지 눌러 해당 게시글 요청하고 게시글 목록 HTML로 
        async function loadBoardData(page){
            content.innerHTML = ""; // 기존 내용 비움
            currentPage = page;

            try {
                // 페이지 누를 때마다 해당 페이지의 게시글 10개를 요청해 받음
                const response = await fetch("/sjhealthy/admin/post?page=" + page);
                
                if (response.status === 204){
                    // 게시물이 존재하지 않을 때
                    const h3 = document.createElement("h3");
                    h3.textContent = "게시물이 존재하지 않습니다.";
                    content.appendChild(h3);

                    const pagination = document.getElementById("pagination");
                    pagination.innerHTML = ""; // 기존 내용 초기화(글이 없으니 페이지 없는 걸로)
                } else if(response.status === 403){
                    // 관리자 아님
                    alert("관리자 전용 기능입니다.");
                    window.location.href = "/sjhealthy";
                    return; 
                } else if (response.ok){
                    const data = await response.json();
                    const boardListDiv = document.getElementById("content");
                    boardListDiv.innerHTML = ""; // 기존 내용 초기화

                    // if (data.data.page.totalPages === 0){
                    //     // 게시물 0개일 경우는 여기서 걸러줌
                    //     // 게시물이 존재하지 않을 때
                    //     const h3 = document.createElement("h3");
                    //     h3.textContent = "게시물이 존재하지 않습니다.";
                    //     content.appendChild(h3);

                    //     const pagination = document.getElementById("pagination");
                    //     pagination.innerHTML = ""; // 기존 내용 초기화
                        
                    // } else {
                    const boardList = data._embedded.boardDTOList;
                    console.log(boardList);

                    const table = document.createElement("table");
                    table.className ="table table-hover text-center table-bordered mt-4";

                    const thead = document.createElement("thead");
                    thead.innerHTML = `
                        <tr>
                            <th>삭제</th>
                            <th>글 번호</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>조회수</th>
                        </tr>
                    `;

                    table.appendChild(thead);

                    const tbody = document.createElement("tbody");

                    boardList.forEach((board, index) => {
                        const row = document.createElement("tr");

                        const deleteCell = row.insertCell(0);
                        const deleteBtn = document.createElement("input");
                        // deleteCell.className = "adminButton";
                        deleteBtn.type = "button";
                        deleteBtn.id = "deletePostBtn"+ board.boardId;
                        deleteBtn.className = "adminButton";
                        deleteBtn.value = "삭제";
                        deleteBtn.onclick = () => deletePost(index, board.boardId);
                        
                        deleteCell.appendChild(deleteBtn);
                        // row.append(deleteCell);

                        const postNumber = document.createElement("td");
                        postNumber.textContent = board.boardId;
                        row.appendChild(postNumber);

                        const title = document.createElement("td");
                        const link = document.createElement("a");
                        link.href = "/sjhealthy/board/read?boardId=" + board.boardId;
                        link.textContent = board.boardTitle;
                        title.appendChild(link);
                        row.appendChild(title);

                        const writer = document.createElement("td");
                        writer.textContent = board.memberId;
                        row.appendChild(writer);

                        const createDate = document.createElement("td");
                        const date = board.createDate;
                        const year = date.substring(0, 4);
                        const month = date.substring(4, 6);
                        const day = date.substring(6, 8);
                        createDate.textContent = year + "/" + month + "/" + day;
                        row.appendChild(createDate);

                        const hits = document.createElement("td");
                        hits.textContent = board.boardViews;
                        row.appendChild(hits);

                        tbody.appendChild(row);
                    });

                    table.appendChild(tbody);
                    content.appendChild(table);

                    displayPagination(data.page.totalPages, page); // 페이지 버튼 생성
                    
                } else {
                    alert("관리자만 접근 가능한 페이지입니다.");
                    window.location.href = "/sjhealthy";
                }
            } catch (error){
                console.log("Error = " + error);
            }
        }


        function displayPagination(totalPages, currentPage){
            const pagination = document.getElementById("pagination");

            pagination.innerHTML = ""; // 기존 내용 초기화

            for (let i = 1; i <= totalPages; i++){
                const pageButton = document.createElement("button");
                pageButton.textContent = i;
                pageButton.disabled = (i === currentPage); // 현재 페이지는 버튼 비활성화
                
                pageButton.addEventListener("click", (e) => {
                    loadBoardData(i); // 클릭한 페이지 데이터 요청하고 html 생성
                    window.currentPage = i; // 클릭한 페이지를 현재 페이지로 저장
                });
                pagination.appendChild(pageButton); 
            }
        }
        // 게시물 삭제 버튼
        function deletePost(index, boardId){
            // const deletePosts = document.querySelectorAll(".deletePost");
            const deleteB = "deletePostBtn" + boardId; 
            document.addEventListener("click", async (e) => {
                if (e.target.id === deleteB){
                    if (!window.confirm("정말로 삭제하시겠습니까?")){
                        e.preventDefault();
                        return false;
                    }

                    try {
                        const response = await fetch("/sjhealthy/board/delete/"+boardId);
                        const data = await response.json();
        
                        if (response.ok){
                            alert(data.message);
                            loadBoardData(window.currentPage);
                        }

                    } catch (error){
                        alert("시스템 오류로 실패하였습니다.");
                    }
                }
            });
        }
    });
