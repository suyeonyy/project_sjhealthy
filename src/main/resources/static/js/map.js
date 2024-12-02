/*
$('#map_recommend').click(function () {

    if ("geolocation" in navigator) {	// geolocation 사용 가능
        navigator.geolocation.getCurrentPosition(function (data) {
            const longitude = data.coords.longitude;
            const latitude = data.coords.latitude;
            const page = $('#select_count').val();

            searchMapReccomend(longitude, latitude, page);
        }, function (error) {
            alert(error);
        }, {
            enableHighAccuracy: true,
            timeout: Infinity,
            maximumAge: 0
        });
    } else {	// geolocation 사용 불가능
        alert('geolocation 사용 불가능');
    }
});
*/
/*
function searchMapReccomend(longitude, latitude, page) {
    // 이미 한번이라도 호출했던 리스트 제거
    $('.map_list_content').remove();

    console.log('경도 = ' + longitude);
    console.log('위도 = ' + latitude);

    for (var i=1; i<parseInt(page)+1; i++) {
        const jsonData = {
            longitude: longitude,
            latitude: latitude,
            page: i
        }

        $.ajax({
            url: "../sjhealthy/map",
            type: "post",
            data: jsonData,
            dataType: "json",
            success: function (result) {
                console.log("성공");
                console.log(result);

                const mapDiv = $('#map_list');
                const resultList = Object.values(result)[0];

                resultList.forEach(function (item) {

                    const address = item.address_name;
                    const url = item.place_url;
                    const phone = item.phone;
                    const x = item.x;
                    const y = item.y;
                    const name = item.place_name;
                    const div = '<div class="map_list_content" ' + 'data-address=' + address + 'data-url=' + url + 'data-x=' + x + 'data-y=' + y + 'data-phone=' + phone + '>' + name + '</div>'
                    mapDiv.append(div);

                });

                $('#map_recommend').hide();
                $('#random_recommend').show();
            },
            error: function (e) {
                alert("Fail");
                console.log(e);
            }
        });
    }
}
*/



















