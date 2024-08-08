const commonLib = {
    //공통 라이브러리
    /**
     * ajax 요청 공통 기능
     *
     * @Param responseType : 응답 데이터 타입(text - text로, 그외는 json)
     * @param url <-필수 데이터
     * @param method <-매개변수에 직접 기본값 정의 가능
     * @param data 전송 데이터
     * @param headers 헤더 정보
     */
  ajaxLoad(url, method="GET", data, headers, responseType) {
      if(!url) {
          return;
      }
      //npe(NullPointerException)에 안정적으로 만들기
      //?. : 옵셔널 체이닝, null, undefined일 때 오류 방지를 위함, 무시하고 undefined로 대체해서 내보냄
      const csrfToken = document.querySelector("meta[name='csrf_token']")?.content?.trim();
      const csrfHeader = document.querySelector("meta[name='crsf_header']")?.content?.trim();
      const rootUrl = document.querySelector("meta[name='rootUrl']")?.content?.trim() ?? '';
      //빈 문자열 넣어주기, nullish
        
        url = location.protocol + "://" + location + rootUrl;
        
      method = method.toUpperCase();
      if (method ==='GET') {
          data = null; //get 방식일 때는 바디 데이터 없음
      }
      if (!(data instanceof FormData) && typeof data !== 'string' && data instanceof Object) {
          data = JSON.stringify(data);
      }
      if (csrfHeader && csrfToken) {
          headers = headers ?? {};
          headers[csrfHeader] = csrfToken;
          //헤더를 보낼 때마다 토큰을 실어서 내보냄
      }
      const options = {
          method
      };
      if (data) options.body = data;
      if (headers) options.headers = headers;

      return new Promise((resolve, reject)=>{ //promise는 후속처리 편의를 위함
      fetch(url, options)
          .then(res => responseType === 'text' ? res.text() : res.json()) //res.json() -JSON / res.text()-텍스트
          .then(data => resolve(data)) //성공 시 응답 데이터
          .catch(err => reject(err)); //실패 시 에러 응답
      });
  }
};