document.addEventListener("DOMContentLoaded", function() {
    // 등록 버튼을 클릭할 때 이벤트 처리
    const sign = document.getElementById("sign");
    sign.addEventListener("click", function() {

        const nickname = document.querySelector('input[name="nickname"]').value;

        // 폰 번호 값 가져오기
        const phoneNumber = document.querySelector('input[name="phoneNumber"]').value;


        // 회원 가입 양식에서 입력된 정보 가져오기
        const userData = {
            nickname: nickname,
            phoneNumber: phoneNumber,
        };

        // API 요청 보내기
        fetch('/maple/user/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
            .then(function(response) {
                if (response.status === 201) {
                    // 회원 가입 성공 시 처리
                    console.log("회원 가입이 성공적으로 완료되었습니다.");
                    // 여기에서 원하는 작업을 수행하세요. 예를 들어 로그인 페이지로 이동 등.
                    window.location.href = "index.html";
                } else {
                    // 회원 가입 실패 시 처리
                    console.error("회원 가입에 실패했습니다.");
                }
            })
            .catch(function(error) {
                console.error("API 요청 중 오류 발생:", error);
            });
    });
});
