import {fetchWithOptions} from './http.js';

async function updateQuestion(questionId) {
    const payload = {
        title: document.getElementById('titleInput').value,
        description: document.getElementById('descInput').value,
        solution: document.getElementById('solutionInput').value
    };

    const {ok, data} = await fetchWithOptions({
        url: `/admin/questions/${questionId}`,
        method: 'PUT',
        body: payload,
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (ok) {
        alert("수정 완료");
        window.location.reload();
    } else {
        alert(data?.message || "수정 실패");
    }
}

window.updateQuestion = updateQuestion;
