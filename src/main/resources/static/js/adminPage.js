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
        postNav.addEventListener("click", async (e)=> {
            e.preventDefault();

            content.innerHTML = ""; // 기존 내용 비움

            try {
                const response = await fetch("/sjhealthy/admin/post");
                const data = await response.json();

                if (!data.message){
                    const boardList = data.data;

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
                } else {
                    alert(message);
                }
            } catch (error){
                console.log("Error = " + error);
            }

        });
    });
