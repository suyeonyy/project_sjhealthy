    document.addEventListener("DOMContentLoaded", ()=>{
        // 네비게이션바
        const memberNav = document.getElementById("memberNav");
        const postNav = document.getElementById("postNav");
        const reportNav = document.getElementById("reportNav");
        const content = document.getElementById("content");

        // 회원 관리창
        memberNav.addEventListener("click", async (e)=> {
            e.preventDefault();

            content.innerHTML = ""; // 기존 내용 비움

            try {
                const response = await fetch("/sjhealthy/admin/member");
                const data = await response.json();

                if (!data.message){ // 성공적으로 데이터를 받았을 때(message가 null로 응답됨)
                    const memberList = data.data;

                    const table = document.createElement("table");
                    table.border = "1";

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
                    memberList.forEach(member =>{
                        const row = document.createElement("tr");

                        const deleteCell = document.createElement("td");
                        // deleteCell.className = "adminButton";
                        deleteCell.innerHTML = `<a href="/sjhealthy/admin/member/delete/${member.memberId}"><button class="adminButton">X</button></a>`;
                        row.append(deleteCell);

                        const memberId = document.createElement("td");
                        memberId.textContent = member.memberId;
                        row.append(memberId);

                        const createDate = document.createElement("td");
                        createDate.textContent = member.createDate;
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
                        memberBirth.textContent = member.memberBirth;
                        row.appendChild(memberBirth);

                        tbody.appendChild(row);

                    });
                    table.appendChild(tbody);

                    content.appendChild(table);

                    // 회원 탈퇴 버튼
                    const adminButtons = document.querySelectorAll(".adminButton");

                    adminButtons.forEach((adminButton, index) => {
                        adminButton.addEventListener("click", async (e)=> {
                            if (!window.confirm("정말로 삭제하시겠습니까?")){
                                e.preventDefault();
                                return false;
                            }

                            try {
                                const deleteMemberId = memberList[index].memberId;
                                const response = await fetch("/sjhealthy/admin/member/delete/" + deleteMemberId);
                                const data = await response.json();

                                if (!data){
                                    alert(data.message);
                                } else {
                                    alert(data.message);
                                }
                            } catch(error){
                                console.log("Error = " + error);
                            }
                        });
                    });
                } else {
                    alert(data.message);
                }
            } catch(error){
                console.log("Error = " + error);
            }
        });

        
        // 게시글 관리
        let currentPage = 1;
        const pageSize = 10;
        
        // post 네비버튼 누르면 자동으로 로드(1페이지로)
        postNav.addEventListener("click", (e)=> {
            loadBoardData(currentPage, e);
        });

        // 페이지 눌러 해당 게시글 요청하고 게시글 목록 HTML로 
        async function loadBoardData(page, e){
            e.preventDefault();

            content.innerHTML = ""; // 기존 내용 비움

            try {
                // 페이지 누를 때마다 해당 페이지의 게시글 10개를 요청해 받음
                const response = await fetch("/sjhealthy/admin/post?page=" + page);
                const data = await response.json();
                
                if (response.status === 200){
                    const boardListDiv = document.getElementById("content");
                    boardListDiv.innerHTML = ""; // 기존 내용 초기화
                    
                    const boardList = data.data;
                    console.log(boardList);

                    const table = document.createElement("table");
                    table.border = "1";

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

                    boardList.forEach(board => {
                        console.log(board.boardTitle);

                        const row = document.createElement("tr");

                        const deleteCell = document.createElement("td");
                        deleteCell.innerHTML = `<a href="/sjhealthy/board/delete?boardId=${board.boardId}"><button class="deletePost">X</button></a>`;
                        // deleteCell.className = "deletePost";
                        row.appendChild(deleteCell);

                        const postNumber = document.createElement("td");
                        postNumber.textContent = board.boardId;
                        row.appendChild(postNumber);

                        const title = document.createElement("td");
                        title.textContent = board.boardTitle;
                        row.appendChild(title);

                        const writer = document.createElement("td");
                        writer.textContent = board.memberId;
                        row.appendChild(writer);

                        const createDate = document.createElement("td");
                        createDate.textContent = board.createDate;
                        row.appendChild(createDate);

                        const hits = document.createElement("td");
                        hits.textContent = board.boardViews;
                        row.appendChild(hits);

                        tbody.appendChild(row);
                    });

                    table.appendChild(tbody);
                    content.appendChild(table);

                    displayPagination(data.totalPages, page); // 페이지 버튼 생성
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

            for (let i = 0; i < totalPages; i++){
                const pageButton = document.createElement("button");
                pageButton.textContent = i;
                pageButton.disabled = (i === currentPage); // 현재 페이지는 버튼 비활성화
                
                pageButton.addEventListener("click", (e) => {
                    loadBoardData(i, e); // 클릭한 페이지 데이터 요청하고 html 생성
                    currentPage = i; // 클릭한 페이지를 현재 페이지로 저장
                });
                pagination.appendChild(pageButton); 
            }
        }
        // 게시물 삭제 버튼
        const deletePosts = document.querySelectorAll(".deletePost");

        deletePosts.forEach((deletePost, index) => {
            deletePost.addEventListener("click", async (e)=> {
                if (!window.confirm("정말로 삭제하시겠습니까?")){
                    e.preventDefault();
                    return false;
                }
            });
        });
    
    });
