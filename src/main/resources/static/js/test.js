//해당 키워드 검색
    $('#keyword').change(function(){
        searchPlaces();
    });

    //ps: 카카오지도에서 키워드를 통해 장소를 검색할 수 있게 해주는 객체
    var ps = new kakao.maps.services.Places();

    var listEl = document.getElementById('placesList');

    //keyword의 값을 가져와서 값이 있는지 확인하고, 있으면 ps 객체의 keywordSearch함수의 인자로 keyword를 전달
    function searchPlaces() {
        console.log("11");
        var keyword = document.getElementById('keyword').value;

        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            removeAllChildNods(listEl);
            return false;
        }

        ps.keywordSearch( keyword, placesSearchCB);
    }

    //장소 검색이 완료되었을 때 호출되는 콜백함수
    //정상적으로 검색이 완료되었을 경우 displayPlaces함수에 리턴받은 data를 전달
    function placesSearchCB(data, status, pagination) {
        console.log("22");
        if (status === kakao.maps.services.Status.OK) {

            displayPlaces(data);

        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            removeAllChildNods(listEl);
            alert('검색 결과가 존재하지 않습니다.');
            return;

        } else if (status === kakao.maps.services.Status.ERROR) {

            alert('검색 결과 중 오류가 발생했습니다.');
            return;

        }
    }

    //검색된 데이터를 꺼내서 <ul>태그에 하나씩 넣어줌
    function displayPlaces(places) {
        console.log("33");
        var fragment = document.createDocumentFragment();

        removeAllChildNods(listEl);

        for ( var i=0; i<places.length; i++ ) {

            var itemEl = getListItem(i, places[i]);

            (function(name){
                itemEl.onclick = function(){
                    var key = document.getElementById('keyword');

                    key.value = name;

                    removeAllChildNods(listEl);
                }
            })(places[i].place_name);

            fragment.appendChild(itemEl);
        }

        listEl.appendChild(fragment);
    }

    //HTML 코드를 생성
    function getListItem(index, places) {
        console.log("44 진입");
        console.log("place_name",places.place_name);
        console.log("id",places.id);
        console.log("category_group_code",places.category_group_code);
        //place_name 장소명, 업체명
        //id 장소 ID
        //category_group_code 중요 카테고리만 그룹핑한 카테고리 그룹 코드
        var el = document.createElement('li'),
        itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                    '<div class="info">' +
                    '   <span class = "info-place-name"><b>' + places.place_name + '</b></span>'; //장소명, 업체명
        itemStr += '    <span class = "info-place-id" style="display:none;"><small>' + places.id + '</small></span>' //장소 ID
        itemStr += '    <span class = "info-category-group-code" style="display:none;"><small>' + places.category_group_code + '</small></span>' //중요 카테고리만 그룹핑한 카테고리 그룹 코드

        if (places.road_address_name) {
            itemStr += '    <br><span class = "info-address"><small>' + places.road_address_name + '</small></span>' //전체 도로명 주소
        } else {
            itemStr += '    <br><span class = "info-address"><small>' +  places.address_name  + '</small></span>'; //전체 지번 주소
        }

        el.innerHTML = itemStr;
        el.className = 'item';

        return el;
    }

    //<ul>태그 아래 생성된 자식노드들을 모두 제거
    function removeAllChildNods(el) {
        console.log("55");

        var place_name;
        var place_id;
        var place_category_group_code;

        var placeIdElement = el.querySelector('.info-place-name b');
        if (placeIdElement) {
            //console.log("info-place-name 값:", placeIdElement.innerText);
            place_name = placeIdElement.innerText;
        }

        var placeIdElement = el.querySelector('.info-place-id small');
        if (placeIdElement) {
            //console.log("info-place-id 값:", placeIdElement.innerText);
            place_id = placeIdElement.innerText;
        }

        var placeIdElement = el.querySelector('.info-category-group-code small');
        if (placeIdElement) {
            //console.log("info-category-group-code 값:", placeIdElement.innerText);
            place_category_group_code = placeIdElement.innerText;
        }

        console.log("최종");
        console.log(place_name, place_id, place_category_group_code);

        while (el.hasChildNodes()) {
            el.removeChild (el.lastChild);
        }

        sendDataToServer(place_name, place_id, place_category_group_code);
    }

    // 데이터를 서버로 전송하는 함수
    function sendDataToServer(place_name, place_id, place_category_group_code) {
        $.ajax({
           url : '../recommend/write',
           type : 'post',
           body: JSON.stringify({
                           place_name: place_name,
                           place_id: place_id,
                           place_category_group_code: place_category_group_code
                       })
           //data:{place_name:place_name, place_id:place_id, place_category_group_code:place_category_group_code},
           success : function(data) {
                console.log("성공");
                console.log(data);
           }, error : function() {
                   //console.log("fail");
           }
       });








        /*
        fetch('/your-server-endpoint', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                place_name: place_name,
                place_id: place_id,
                place_category_group_code: place_category_group_code
            })
        })
        .then(response => response.json())  // 서버에서 응답을 JSON 형식으로 받는다고 가정
        .then(data => {
            console.log('서버 응답:', data);
        })
        .catch(error => {
            console.error('오류 발생:', error);
        });
        */
    }