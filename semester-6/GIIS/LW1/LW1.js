const canvas = document.getElementById("cnv");

const topCanvas = document.createElement("canvas")
topCanvas.width = canvas.width
topCanvas.height = canvas.height
topCanvas.style.position = 'absolute';
topCanvas.style.left = canvas.offsetLeft + 'px';
topCanvas.style.top = canvas.offsetTop + 'px';
topCanvas.style.width = canvas.width + 'px';
topCanvas.style.height = canvas.height + 'px';
topCanvas.style.fillStyle = "transparent"
const tempCtx = topCanvas.getContext('2d');
tempCtx.clearRect(0, 0, canvas.width, canvas.height);
topCanvas.addEventListener("click", (event) => {
    canvas.click(event)
})
document.body.appendChild(topCanvas);

const ctx = canvas.getContext("2d");
let drawCtx = {}

const tryInitCtx = (type) => {
    if (drawCtx.type !== type) {
        drawCtx = {type:type}
        return true
    }
    return false
}

const deinitCtx = () => {
    drawCtx = {}
    clearTemporary()
}

const point = (x, y) => {
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x+1, y+1);
    ctx.stroke();
}

// https://ru.wikipedia.org/wiki/%D0%90%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC_DDA-%D0%BB%D0%B8%D0%BD%D0%B8%D0%B8
const drawDDALine = (x1, y1, x2, y2) => {
    const dx = x2 - x1;
    const dy = y2 - y1;
    const steps = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
    const xIncrement = dx / steps;
    const yIncrement = dy / steps;

    let x = x1;
    let y = y1;

    ctx.beginPath();
    ctx.moveTo(x, y);

    for (let i = 0; i < steps; i++) {
        x += xIncrement;
        y += yIncrement;
        ctx.lineTo(Math.round(x), Math.round(y));
    }

    ctx.stroke();
}

const drawBresenhamLine = (x1, y1, x2, y2) => {
    const dx = Math.abs(x2 - x1);
    const dy = Math.abs(y2 - y1);
    const sx = (x1 < x2) ? 1 : -1;
    const sy = (y1 < y2) ? 1 : -1;
    let err = dx - dy;

    while (true) {
        ctx.fillRect(x1, y1, 1, 1);

        if (x1 === x2 && y1 === y2) {
            break;
        }

        const e2 = 2 * err;
        if (e2 > -dy) {
            err -= dy;
            x1 += sx;
        }
        if (e2 < dx) {
            err += dx;
            y1 += sy;
        }
    }
}

const drawWuLine = (x1, y1, x2, y2) => {
    const steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
    if (steep) {
      [x1, y1] = [y1, x1];
      [x2, y2] = [y2, x2];
    }
    if (x1 > x2) {
      [x1, x2] = [x2, x1];
      [y1, y2] = [y2, y1];
    }
  
    const dx = x2 - x1;
    const dy = y2 - y1;
    const gradient = dy / dx;
    let y = y1;
  
    for (let x = x1; x <= x2; x++) {
      if (steep) {
        ctx.fillStyle = `rgba(0, 0, 0, ${1 - (y - Math.floor(y))})`;
        ctx.fillRect(Math.floor(y), x, 1, 1);
        ctx.fillStyle = `rgba(0, 0, 0, ${y - Math.floor(y)})`;
        ctx.fillRect(Math.floor(y) + 1, x, 1, 1);
      } else {
        ctx.fillStyle = `rgba(0, 0, 0, ${1 - (y - Math.floor(y))})`;
        ctx.fillRect(x, Math.floor(y), 1, 1);
        ctx.fillStyle = `rgba(0, 0, 0, ${y - Math.floor(y)})`;
        ctx.fillRect(x, Math.floor(y) + 1, 1, 1);
      }
      y += gradient;
    }
}

const drawTemporaryCross = (x, y) => {
    // Устанавливаем цвет линии
    tempCtx.strokeStyle = 'red';

    // Устанавливаем толщину линии
    tempCtx.lineWidth = 2;

    // Рисуем горизонтальную линию
    tempCtx.beginPath();
    tempCtx.moveTo(x - 10, y);
    tempCtx.lineTo(x + 10, y);
    tempCtx.stroke();

    // Рисуем вертикальную линию
    tempCtx.beginPath();
    tempCtx.moveTo(x, y - 10);
    tempCtx.lineTo(x, y + 10);
    tempCtx.stroke();
}

const clearTemporary = () => {
    tempCtx.clearRect(0, 0, topCanvas.width, topCanvas.height);
}

// Обработчик события клика по холсту
topCanvas.addEventListener("click", function(event) {
    const x = event.offsetX;
    const y = event.offsetY;
    const selectedTool = document.querySelector('input[name="tool"]:checked').value;
    const isInited = tryInitCtx(selectedTool)
    drawTemporaryCross(x, y)
    const useTool = {
        dda: () => {
            if (isInited) {
                drawCtx.start = [x, y]
            } else {
                drawDDALine(drawCtx.start[0], drawCtx.start[1], x, y)
                deinitCtx()
            }
        },

        bresenham: () => {
            if (isInited) {
                drawCtx.start = [x, y]
            } else {
                drawBresenhamLine(drawCtx.start[0], drawCtx.start[1], x, y)
                deinitCtx()
            }
        },

        wu: () => {
            if (isInited) {
                drawCtx.start = [x, y]
            } else {
                drawWuLine(drawCtx.start[0], drawCtx.start[1], x, y)
                deinitCtx()
            }
        },
    }

    useTool[selectedTool]()
});
