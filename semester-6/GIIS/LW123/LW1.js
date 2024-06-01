const canvas = document.getElementById("cnv");

const canvas = document.createElement("canvas")
canvas.width = canvas.width
canvas.height = canvas.height
canvas.style.position = 'absolute';
canvas.style.left = canvas.offsetLeft + 'px';
canvas.style.top = canvas.offsetTop + 'px';
canvas.style.width = canvas.width + 'px';
canvas.style.height = canvas.height + 'px';
canvas.style.fillStyle = "transparent"
const tempCtx = canvas.getContext('2d');
tempCtx.clearRect(0, 0, canvas.width, canvas.height);
canvas.addEventListener("click", (event) => {
    canvas.click(event)
})
document.body.appendChild(canvas);

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

const calculateDistance = (x1, y1, x2, y2) => {
    const dx = x2 - x1;
    const dy = y2 - y1;
  
    return Math.sqrt(dx * dx + dy * dy);
  }

const drawCircle = (centerX, centerY, radius) => {
    const twoPi = 2 * Math.PI;
    const step = 1 / radius;

    for (let theta = 0; theta < twoPi; theta += step) {
        const x = centerX + radius * Math.cos(theta);
        const y = centerY + radius * Math.sin(theta);
        ctx.fillRect(x, y, 1, 1);
    }
}

const plotEllipse4Points = (cx, cy, x, y) => {
    ctx.fillRect(cx+x, cy+y, 1, 1);
    ctx.fillRect(cx-x, cy+y, 1, 1);
    ctx.fillRect(cx-x, cy-y, 1, 1);
    ctx.fillRect(cx+x, cy-y, 1, 1);
}

const drawEllipse = (cx, cy, xradius, yradius) => {
    const twoASquare = 2 * xradius * xradius;
    const twoBSquare = 2 * yradius * yradius;

    let x = xradius;
    let y = 0;

    let xchange = yradius * yradius * (1 - 2 * xradius);
    let ychange = xradius * xradius;

    let ellipseError = 0;
    let stoppingX = twoBSquare * xradius;
    let stoppingY = 0;

    while (stoppingX >= stoppingY) {
        plotEllipse4Points(cx, cy, x, y);
        y++;
        stoppingY += twoASquare;
        ellipseError += ychange;
        ychange += twoASquare;

        if ( (2*ellipseError + xchange) > 0 ) {
            x--;
            stoppingX -= twoBSquare;
            ellipseError += xchange;
            xchange += twoBSquare;
        }
    }

    x = 0;
    y = yradius;
    xchange = yradius * yradius;
    ychange = xradius * xradius * (1 - 2 * yradius);
    ellipseError = 0;
    stoppingX = 0;
    stoppingY = twoASquare * yradius;

    while (stoppingX <= stoppingY) {
        plotEllipse4Points(cx, cy, x, y)
        x++;
        stoppingX += twoBSquare;
        ellipseError += xchange;
        xchange += twoBSquare;

        if ( (2*ellipseError + ychange) > 0 ) {
            y--;
            stoppingY -= twoASquare;
            ellipseError += ychange;
            ychange += twoASquare;
        }
    }
}

const drawHyperbola = (h, k, a, b) => {
    const step = 1 / (Math.max(a, b) * 5);
    ctx.beginPath();
    for (let angle = 0; angle < 2 * Math.PI; angle += step) {
        const x = h + a / Math.cos(angle);
        const y = k + b * Math.tan(angle); 
        putPixel(x, y); 
    }
    ctx.closePath();
}

const drawParabola = (h, k, a, b) => {
    const step = 1 / (Math.max(a, b) * 5);
    ctx.beginPath();
    for (let angle = -2 * Math.PI; angle < 2 * Math.PI; angle += step) {
        const x = h + a * angle;
        const y = k + b * Math.pow(angle, 2); 
        putPixel(x, y); 
    }
    ctx.closePath();
}

const drawHermite = (points) => {
    ctx.beginPath();
    for (let i = 0; i < points.length - 1; i++) {
        const p0 = points[i];
        const p1 = points[i + 1];
        const t0 = i > 0 ? (p1.x - points[i - 1].x) / 2 : 0;
        const t1 = i < points.length - 2 ? (points[i + 2].x - p0.x) / 2 : 0;

        for (let t = 0; t <= 1; t += 0.01) {
            const h1 = 2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1;
            const h2 = -2 * Math.pow(t, 3) + 3 * Math.pow(t, 2);
            const h3 = Math.pow(t, 3) - 2 * Math.pow(t, 2) + t;
            const h4 = Math.pow(t, 3) - Math.pow(t, 2);

            const x = h1 * p0.x + h2 * p1.x + h3 * t0 + h4 * t1;
            const y = h1 * p0.y + h2 * p1.y + h3 * t0 + h4 * t1;

            if (t === 0) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        }
    }
    ctx.stroke();
    ctx.closePath();
}

const drawBezier = (points) => {
    ctx.beginPath();
    if (points.length >= 4 && (points.length - 1) % 3 === 0) {
        for (let i = 0; i < points.length - 1; i += 3) {
            const p0 = points[i];
            const p1 = points[i + 1];
            const p2 = points[i + 2];
            const p3 = points[i + 3];

            const iterations = 10000; // Количество итераций дискретизации

            for (let j = 0; j <= iterations; j++) {
                const t = j / iterations;

                const x =
                    Math.pow(1 - t, 3) * p0.x +
                    3 * Math.pow(1 - t, 2) * t * p1.x +
                    3 * (1 - t) * Math.pow(t, 2) * p2.x +
                    Math.pow(t, 3) * p3.x;
                const y =
                    Math.pow(1 - t, 3) * p0.y +
                    3 * Math.pow(1 - t, 2) * t * p1.y +
                    3 * (1 - t) * Math.pow(t, 2) * p2.y +
                    Math.pow(t, 3) * p3.y;

                if (j === 0) {
                    ctx.moveTo(x, y);
                } else {
                    ctx.lineTo(x, y);
                }
            }
        }
    }
    ctx.stroke();
    ctx.closePath();
}

const drawBSpline = (points) => {
    ctx.beginPath();
    var xScale = d3.scaleLinear()
            .domain([0, d3.max(points, d => d.x)])
            .range([0, d3.max(points, d => d.x)]);

        var yScale = d3.scaleLinear()
            .domain([0, d3.max(points, d => d.y)])
            .range([0, d3.max(points, d => d.y)]);

        // Создаем генератор сплайна
        var lineGenerator = d3.line()
            .x(d => xScale(d.x))
            .y(d => yScale(d.y))
            .curve(d3.curveNatural);

        lineGenerator.context(ctx)(points);
    ctx.stroke();
    ctx.closePath();
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
    tempCtx.clearRect(0, 0, canvas.width, canvas.height);
}

const putPixel = (x, y) => {
    ctx.fillRect(x, y, 1, 1)
}

// Обработчик события клика по холсту
canvas.addEventListener("click", function(event) {
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

        circle: () => {
            if (isInited) {
                drawCtx.center = [x, y]
            } else {
                const radius = calculateDistance(drawCtx.center[0], drawCtx.center[1], x, y)
                drawCircle(drawCtx.center[0], drawCtx.center[1], radius)
                deinitCtx()
            }
        },

        ellipse: () => {
            if (isInited) {
                const a = Number(prompt("Enter RX: "))
                const b = Number(prompt("Enter RY: "))
                if (a === NaN || b === NaN) {
                    alert("Bad input.")
                    deinitCtx()
                    return
                }
                drawEllipse(x, y, a, b)
                deinitCtx()
            }
        },

        hyperbola: () => {
            if (isInited) {
                drawHyperbola(x, y, prompt("A:"), prompt("B:"))
                deinitCtx()
            }
        },

        parabola: () => {
            if (isInited) {
                drawParabola(x, y, prompt("A:"), prompt("B:"))
                deinitCtx()
            }
        },

        hermite: () => {
            if (isInited) {
                drawCtx.points = [{x:x,y:y}]
            } else {
                if (drawCtx.points[drawCtx.points.length - 1].x === x && drawCtx.points[drawCtx.points.length - 1].y === y) {
                    drawHermite(drawCtx.points)
                    deinitCtx()
                    return
                }
                drawCtx.points.push({x:x,y:y})
            }
        },

        bezier: () => {
            if (isInited) {
                drawCtx.points = [{x:x,y:y}]
            } else {
                if (drawCtx.points[drawCtx.points.length - 1].x === x && drawCtx.points[drawCtx.points.length - 1].y === y) {
                    drawBezier(drawCtx.points)
                    deinitCtx()
                    return
                }
                drawCtx.points.push({x:x,y:y})
            }
        },

        bspline: () => {
            if (isInited) {
                drawCtx.points = [{x:x,y:y}]
            } else {
                if (drawCtx.points[drawCtx.points.length - 1].x === x && drawCtx.points[drawCtx.points.length - 1].y === y) {
                    drawBSpline(drawCtx.points)
                    deinitCtx()
                    return
                }
                drawCtx.points.push({x:x,y:y})
            }
        }
    }

    useTool[selectedTool]()
});
