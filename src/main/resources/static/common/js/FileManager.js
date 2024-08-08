/**
 * 파일 업로드, 삭제, 조회 공통 기능
 */

const fileManager = {
    //파일 업로드
    upload(files, gid, location) {
        try {
            if (!files || files.length == 0){
                throw new Error("파일을 선택하세요");
            }
            if (!gid || !gid.trim()) {
                throw new Error("필수 항목 누락입니다(gid).");
            }
            const formData = new FormData();
            formData.append("gid", gid.trim());

            for (const file of files) {
                formData.append("file", file);
            }
            if (location && locaiton.trim()) {
                formData.append("location", location.trim());
            }
            const {ajaxLoad}=commonLib; //필요한 기능들을 비구조 할당으로 꺼내서 사용

            ajaxLoad('file/upload', 'POST', formData)
                .then(res => {
                    if (!res.successs) {
                        alert(res.message);
                        return;
                    }
                    //파일 업로드 후 처리는 다양함, fileUploadCallback을 직접 상황에 맞게 정의 처린
                    if(typeof parent.fileUploadCallback === 'function') {
                        parent.fileUploadCallback(res.data);
                    }
                })
                .catch(err => alert(err.message));

        }catch (e) {
            console.error(e);
            alert(e.message);
        }
        
    },
    //파일 삭제
    delete() {
        
    },
    //파일 조회
    search() {
        
    }
};

window.addEventListener("DOMContentLoaded", function() {

    //파일 업로드 버튼 이벤트 처리 S
    const fileUploads = document.getElementsByClassName("fileUploads");
    const fileEl = document.createElement("input");
    fileEl.type = 'file';
    fileEl.multiple = true;
    //파일 데이터만 전송해준다.

    for (const el of fileUploads) {
        el.addEventListener("click", function(){
            fileEl.value = "";
            delete fileEl.gid;
            delete fileEl.location;
            //기존 값과 충돌 발생 가능성 있으므로 기존 값 지우기

            //dataset 속성을 통해 데이터 접근
            const dataset = this.dataset;
            fileEl.gid = dataset.gid; //파일 객체에 키, 값 넣어줌
            if (dataset.location) fileEl.location = dataset.location;

            fileEl.click();
        });
    }
    //파일 업로드 버튼 이벤트 처리 E

    //파일 업로드 처리
    fileEl.addEventListener("change", function (e) {
       //이벤트 객체에 선택한 파일 정보 담김
       const files = e.target.files;
        fileManager.upload(files, fileEl.gid, fileEl.location);
        //넣어준 키, 값 업로드
    });

});