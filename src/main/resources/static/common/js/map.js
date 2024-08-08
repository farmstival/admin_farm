const mapLib = {
    /**
     * 지도 로드
     *
     * @param mapId : 지도를 출력할 요소 id 이름, Id 별로 지도 만듦
     * @param width : 지도 너비
     * @param height: 지도 높이
     * @param options - 옵션
     *               - center: { lat: 위도, lng: 경도 } - 필수 옵션
     *               - zoom : 확대 정도(1~10) / 숫자가 작을 수록 확대
     *               - markerImage : 공통 마커 이미지 주소, 개별 마커 이미지가 있는 경우는 그걸로 대체
     *               - marker : [{lat: 위도, lng: 경도, info: html 데이터(인포윈도우), image: 이미지 주소 - 마커이미지}]
     *               ?. : 옵셔널 체이닝->null값인 경우를 고려함(null safe)
     */
    load(mapId, width = 300, height = 300, options) {
        const mapEl = document.getElementById(mapId);
        if (!mapEl || !options?.center) return; //가운데 위치할 좌표가 없으면 종료

        mapEl.style.width = `${width}px`;
        mapEl.style.height = `${height}px`;

        let { center, marker, markerImage } = options;

        // 지도 가운데 좌표 처리 S
        const zoom = options?.zoom ?? 3; // 기본값 3
        const position = new kakao.maps.LatLng(center.lat, center.lng); //지도 객체에 좌표 넣어줌
        const map = new kakao.maps.Map(mapEl, {
            center: position, //가운데 좌표를 정해주지 않으면 이상한 곳이 나옴
            level: zoom, //레벨 숫자가 클 수록 멀리 보임
        });
        // 지도 가운데 좌표 처리 E

        //마커 출력 처리 S
        if(marker) {
            if(!Array.isArray(marker))marker = [marker];

            const markers = marker.map(m => {
                const opt = { position: new kakao.maps.LatLng(m.lat, m.lng)};

                //마커 이미지 처리
                const mi = markerImage ? markerImage : m.image;
                if (m1) {
                    const mImage = new kakao.maps.MarkerImage(mi, new kakao.maps.Size(64, 69), {offset: new kakao.maps.Point(27, 69)});
                    opt.image = mImage;
                }
                const _marker = new kakao.maps.Marker(opt);

                _marker.setMap(map);

                return _marker;
            });

        } // endif
        // 마커 출력 처리 E
    }
};