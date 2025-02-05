    document.addEventListener("DOMContentLoaded", ()=>{

        // 네비게이션바
        const memberNav = document.getElementById("memberNav");
        const postNav = document.getElementById("postNav");
        const reportNav = document.getElementById("reportNav");
        const content = document.getElementById("content");

        
        // 회원 관리창
        memberNav.addEventListener("click", (e)=> {
            loadMemberData();
        });

        // 멤버 관리창
        async function loadMemberData(){
            content.innerHTML = ""; // 기존 내용 비움

            try {
                const response = await fetch("/sjhealthy/admin/member");
                const data = await response.json();

                if (!data.message){ // 성공적으로 데이터를 받았을 때(message가 null로 응답됨)
                    const memberList = data.data;

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
                        deleteBtn.onclick = (e) => deleteMember(index, member.memberId);
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
                } else {
                    alert(data.message);
                }
            } catch(error){
                console.log("Error = " + error);
            }
        }

        function deleteMember(index, memberId){
            // 회원 탈퇴 버튼
            const deleteMemberBtn = document.getElementById("deleteMemberBtn"+index);

            deleteMemberBtn.addEventListener("click", async (e)=> {
                if (!window.confirm("정말로 삭제하시겠습니까?")){
                    e.preventDefault();
                    return false;
                }

                try {
                    const response = await fetch("/sjhealthy/admin/member/delete/" + memberId);
                    const data = await response.json();

                    if (!data){
                        alert(data.message);
                    } else {
                        alert(data.message);
                        loadMemberData();
                    }
                } catch(error){
                    console.log("Error = " + error);
                }
            });
        }
        // 기본으로 회원 관리창
        loadMemberData();

        // 게시글 관리
        let currentPage = 1;
        const pageSize = 10;
        
        // post 네비버튼 누르면 자동으로 로드(1페이지로)
        postNav.addEventListener("click", (e)=> {
            loadBoardData(currentPage);
        });

        // 페이지 눌러 해당 게시글 요청하고 게시글 목록 HTML로 
        async function loadBoardData(page){
            content.innerHTML = ""; // 기존 내용 비움
            currentPage = page;

            try {
                // 페이지 누를 때마다 해당 페이지의 게시글 10개를 요청해 받음
                const response = await fetch("/sjhealthy/admin/post?page=" + page);
                const data = await response.json();
                console.log(response.status);
                if (response.status === 200){
                    const boardListDiv = document.getElementById("content");
                    boardListDiv.innerHTML = ""; // 기존 내용 초기화
                    
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
                        deleteBtn.id = "deletePostBtn"+index;
                        deleteBtn.className = "adminButton";
                        deleteBtn.value = "삭제";
                        deleteBtn.onclick = (e) => deletePost(index, board.boardId);
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
                    currentPage = i; // 클릭한 페이지를 현재 페이지로 저장
                });
                pagination.appendChild(pageButton); 
            }
        }
        // 게시물 삭제 버튼
        function deletePost(index, boardId){
            // const deletePosts = document.querySelectorAll(".deletePost");
            const deleteBtn = document.getElementById("deletePostBtn"+index);

            deleteBtn.addEventListener("click", async (e)=> {
                if (!window.confirm("정말로 삭제하시겠습니까?")){
                    e.preventDefault();
                    return false;
                }

                try {
                    const response = await fetch("/sjhealthy/board/delete/"+boardId);
                    const data = await response.json();
    
                    if (response.ok){
                        alert(data.message);
                        loadBoardData(currentPage);
                    }

                } catch (error){
                    alert(data.message);
                }
            });

        }
    });
