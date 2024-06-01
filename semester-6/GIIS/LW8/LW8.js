const canvas = document.getElementById('canvas');
const context = canvas.getContext('2d');

canvas.addEventListener('mousedown', startDrawing);
canvas.addEventListener('mousemove', drawLineWithEvent);
canvas.addEventListener('mouseup', endDrawing);

context.strokeRect(300, 300, canvas.width - 600, canvas.height - 600)

function startDrawing(event) {
    isDrawing = true;
    startX = event.offsetX;
    startY = event.offsetY;
}

function drawLineWithEvent(event) {
    if (!isDrawing) return;
    const x = event.offsetX;
    const y = event.offsetY;
    clearCanvas();
    drawLine(startX, startY, x, y);
}

function endDrawing(event) {
    isDrawing = false;
    clearCanvas();
    drawLineOnCanvas(startX, startY, event.offsetX, event.offsetY)

}

function drawLine(x1, y1, x2, y2) {
    context.beginPath();
    context.moveTo(x1, y1);
    context.lineTo(x2, y2);
    context.stroke();
}

function clearCanvas(){
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.strokeRect(300, 300, canvas.width - 600, canvas.height - 600)
}

function drawLineOnCanvas(x1, y1, x2, y2) {
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.strokeRect(300, 300, canvas.width - 600, canvas.height - 600)

    const selectedAlgorithm = document.querySelector('input[name="tool"]:checked').value;

    if (selectedAlgorithm === 'coen') {
        cohenSultherland(x1, y1, x2, y2);
    } else if (selectedAlgorithm === 'liang') {
        liangBarsky(x1, y1, x2, y2);
    } else {
        console.log('Выберите алгоритм отсечения');
    }
}

function cohenSultherland(x1, y1, x2, y2){

    let startXDraw = x1
    let startYDraw = y1
    let endXDraw = x2
    let endYDraw = y2

    const INSIDE = 0; // Код внутренней области
    const LEFT = 1; // Код левой границы
    const RIGHT = 2; // Код правой границы
    const BOTTOM = 4; // Код нижней границы
    const TOP = 8; // Код верхней границы

    // Определение кода точки
    function computeCode(x, y) {
        let code = INSIDE;

        if (x < 300) {
            code |= LEFT;
        } else if (x > canvas.width-300) {
            code |= RIGHT;
        }

        if (y < 300) {
            code |= BOTTOM;
        } else if (y > canvas.height-300) {
            code |= TOP;
        }

        return code;
    }

    let code1 = computeCode(x1, y1);
    let code2 = computeCode(x2, y2);
    let accept = false;

    while (true) {
        if (!(code1 | code2)) {
            accept = true;
            break;
        } else if (code1 & code2) {
            break;
        } else {
            let x, y, code;

            if (code1 !== INSIDE) {
                code = code1;
            } else {
                code = code2;
            }

            if (code & TOP) { // Пересечение с верхней границей
                x = x1 + (x2 - x1) * (canvas.height-300 - y1) / (y2 - y1);
                y = canvas.height-300;
            } else if (code & BOTTOM) { // Пересечение с нижней границей
                x = x1 + (x2 - x1) * (300 - y1) / (y2 - y1);
                y = 300;
            } else if (code & RIGHT) { // Пересечение с правой границей
                y = y1 + (y2 - y1) * (canvas.width-300 - x1) / (x2 - x1);
                x = canvas.width-300;
            } else if (code & LEFT) { // Пересечение с левой границей
                y = y1 + (y2 - y1) * (300 - x1) / (x2 - x1);
                x = 300;
            }

            if (code === code1) {
                x1 = x;
                y1 = y;
                code1 = computeCode(x1, y1);
            } else {
                x2 = x;
                y2 = y;
                code2 = computeCode(x2, y2);
            }
        }
    }

    if (accept) {
        console.log(x1, y1, x2, y2, startXDraw, startYDraw, endXDraw, endYDraw)

        drawLine(x1, y1, x2, y2);

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(startXDraw, startYDraw);
        context.lineTo(x1, y1);
        context.strokeStyle = "red"; // Установка цвета линии
        context.stroke();

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(x2, y2);
        context.lineTo(endXDraw, endYDraw);
        context.strokeStyle = "red"; // Установка цвета линии
        context.stroke();
        context.strokeStyle = "black"
    } else {
        console.log('Линия полностью отсекается');
    }
}

function liangBarsky(x1, y1, x2, y2) {
    const xmin = 100;
    const xmax = canvas.width - 100;
    const ymin = 100;
    const ymax = canvas.height - 100;

    let t0 = 0;
    let t1 = 1;

    const dx = x2 - x1;
    const dy = y2 - y1;

    function updateTValues(p, q) {
        if (p === 0 && q < 0) return;

        const r = q / p;

        if (p < 0) {
            if (r > t1) return;
            else if (r > t0) t0 = r;
        } else if (p > 0) {
            if (r < t0) return;
            else if (r < t1) t1 = r;
        }
    }

    if (dx !== 0) {
        const p1 = -dx;
        const p2 = dx;
        const q1 = x1 - xmin;
        const q2 = xmax - x1;
        updateTValues(p1, q1);
        updateTValues(p2, q2);
    }

    if (dy !== 0) {
        const p1 = -dy;
        const p2 = dy;
        const q1 = y1 - ymin;
        const q2 = ymax - y1;
        updateTValues(p1, q1);
        updateTValues(p2, q2);
    }

    if (t0 <= t1) {
        const clippedX1 = x1 + t0 * dx;
        const clippedY1 = y1 + t0 * dy;
        const clippedX2 = x1 + t1 * dx;
        const clippedY2 = y1 + t1 * dy;

        drawLine(clippedX1, clippedY1, clippedX2, clippedY2);

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(x1, y1);
        context.lineTo(clippedX1, clippedY1);
        context.strokeStyle = "red";
        context.stroke();

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(clippedX2, clippedY2);
        context.lineTo(x2, y2);
        context.strokeStyle = "red";
        context.stroke();
        context.strokeStyle = "black"

    } else {
        console.log("Линия полностью отсекается");
    }
}

function liangBarsky(x1, y1, x2, y2) {
    const xmin = 300;
    const xmax = canvas.width - 300;
    const ymin = 300;
    const ymax = canvas.height - 300;

    let t0 = 0;
    let t1 = 1;

    const dx = x2 - x1;
    const dy = y2 - y1;

    function updateTValues(p, q) {
        if (p === 0 && q < 0) return;

        const r = q / p;

        if (p < 0) {
            if (r > t1) return;
            else if (r > t0) t0 = r;
        } else if (p > 0) {
            if (r < t0) return;
            else if (r < t1) t1 = r;
        }
    }

    if (dx !== 0) {
        const p1 = -dx;
        const p2 = dx;
        const q1 = x1 - xmin;
        const q2 = xmax - x1;
        updateTValues(p1, q1);
        updateTValues(p2, q2);
    }

    if (dy !== 0) {
        const p1 = -dy;
        const p2 = dy;
        const q1 = y1 - ymin;
        const q2 = ymax - y1;
        updateTValues(p1, q1);
        updateTValues(p2, q2);
    }

    if (t0 <= t1) {
        const clippedX1 = x1 + t0 * dx;
        const clippedY1 = y1 + t0 * dy;
        const clippedX2 = x1 + t1 * dx;
        const clippedY2 = y1 + t1 * dy;

        drawLine(clippedX1, clippedY1, clippedX2, clippedY2);

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(x1, y1);
        context.lineTo(clippedX1, clippedY1);
        context.strokeStyle = "red";
        context.stroke();

        context.beginPath();
        context.strokeStyle = "black"
        context.moveTo(clippedX2, clippedY2);
        context.lineTo(x2, y2);
        context.strokeStyle = "red";
        context.stroke();
        context.strokeStyle = "black"

    } else {
        console.log("Линия полностью отсекается");
    }
}