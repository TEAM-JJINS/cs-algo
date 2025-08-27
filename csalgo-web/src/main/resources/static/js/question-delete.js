import {fetchWithOptions} from './http.js';

async function deleteQuestion(questionId) {
    if (!confirm("정말로 삭제하시겠습니까?")) return;

    const {ok, data} = await fetchWithOptions({
        url: `/admin/questions/${questionId}`,
        method: 'DELETE'
    });

    if (ok) {
        alert("삭제 완료");
        window.location.href = "/admin/dashboard/questions";
    } else {
        alert(data?.message || "삭제 실패");
    }
}

window.deleteQuestion = deleteQuestion;
