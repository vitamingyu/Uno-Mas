/**
 * 
 */

$(document).ready(function() {
});
function toggleReview(num) {
    // 선택된 게시글의 본문만 show로 바꾸고 
    var id = '#reviewContent' + num;
    $(id).toggle();
    
    // 나머지는 hide로 변경
    for (var i = 1; i <= 7; i++) {
        if (i == num) continue;
        id = '#reviewContent' + i;
        $(id).hide();
    }
}

function toggleQna(num) {
    // 선택된 게시글의 본문만 show로 바꾸고 
    var id = '#qnaContent' + num;
    $(id).toggle();
    
    // 나머지는 hide로 변경
    for (var i = 1; i <= 7; i++) {
        if (i == num) continue;
        id = '#qnaContent' + i;
        $(id).hide();
    }
}

function toggleWishlistBtn() {
    alert('위시리스트 메서드 호출');
}