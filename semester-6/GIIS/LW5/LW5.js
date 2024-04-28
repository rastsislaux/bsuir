const canvas = document.getElementById("cnv");
const ctx = canvas.getContext("2d");
let points = [];

const drawCross = (x, y) => {
    // Устанавливаем цвет линии
    ctx.strokeStyle = 'red';

    // Устанавливаем толщину линии
    ctx.lineWidth = 2;

    // Рисуем горизонтальную линию
    ctx.beginPath();
    ctx.moveTo(x - 10, y);
    ctx.lineTo(x + 10, y);
    ctx.stroke();

    // Рисуем вертикальную линию
    ctx.beginPath();
    ctx.moveTo(x, y - 10);
    ctx.lineTo(x, y + 10);
    ctx.stroke();
}

function drawLine(x1, y1, x2, y2, color = "black") {
    ctx.strokeStyle = color;
    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
}

function checkConvexity() {
    if (points.length < 3) {
        alert("Минимальное количество точек для проверки выпуклости: 3");
        return;
    }

    const orientation = getOrientation(points[0], points[1], points[2]);
    const isClockwise = orientation < 0;

    for (let i = 1; i < points.length; i++) {
        const p1 = points[i];
        const p2 = points[(i + 1) % points.length];
        const p3 = points[(i + 2) % points.length];

        const currentOrientation = getOrientation(p1, p2, p3);
        if ((currentOrientation < 0 && !isClockwise) || (currentOrientation > 0 && isClockwise)) {
            alert("Полигон не является выпуклым");
            return;
        }
    }

    alert("Полигон является выпуклым");
}

const getOrientation = (p1, p2, p3) => {
    const val = (p2[1] - p1[1]) * (p3[0] - p2[0]) - (p2[0] - p1[0]) * (p3[1] - p2[1]);
    if (val === 0) {
        return 0;
    } else if (val > 0) {
        return 1;
    } else {
        return -1;
    }
};

function drawPolygon() {
    for (p of points) {
        drawCross(...p);
    }

    for (let i = 0; i < points.length; i++) {
        const point = points[i];
        if (i > 0) {
            const prevPoint = points[i - 1];
            drawLine(...prevPoint, ...point);
        }
    }

    if (points.length > 2) {
        const firstPoint = points[0];
        const lastPoint = points[points.length - 1];
        drawLine(...lastPoint, ...firstPoint);
    }
}

// 
function computeConvexHullGraham() {
    if (points.length < 3) {
        alert("Минимальное количество точек для вычисления выпуклой оболочки: 3");
        return;
    }

    const sortedPoints = points.slice().sort((a, b) => {
        if (a[1] !== b[1]) {
            return a[1] - b[1];
        }
        return a[0] - b[0];
    });

    const getOrientation = (p, q, r) => {
        const val = (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
        if (val === 0) {
            return 0;
        } else if (val > 0) {
            return 1;
        } else {
            return -1;
        }
    };

    const hullPoints = [sortedPoints[0], sortedPoints[1]];
    for (let i = 0; i < sortedPoints.length; i++) {
        while (
            hullPoints.length > 1 &&
            getOrientation(hullPoints[hullPoints.length - 2], hullPoints[hullPoints.length - 1], sortedPoints[i]) !== -1
            ) {
            hullPoints.pop();
        }
        hullPoints.push(sortedPoints[i]);
    }

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPolygon();

    for (let i = 0; i < hullPoints.length; i++) {
        const startPoint = hullPoints[i];
        const endPoint = hullPoints[(i + 1) % hullPoints.length];
        drawLine(startPoint[0], startPoint[1], endPoint[0], endPoint[1], "purple");
    }
}

// https://neerc.ifmo.ru/wiki/index.php?title=%D0%A1%D1%82%D0%B0%D1%82%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B5_%D0%B2%D1%8B%D0%BF%D1%83%D0%BA%D0%BB%D1%8B%D0%B5_%D0%BE%D0%B1%D0%BE%D0%BB%D0%BE%D1%87%D0%BA%D0%B8:_%D0%94%D0%B6%D0%B0%D1%80%D0%B2%D0%B8%D1%81,_%D0%93%D1%80%D1%8D%D1%85%D0%B5%D0%BC,_%D0%AD%D0%BD%D0%B4%D1%80%D1%8E,_%D0%A7%D0%B5%D0%BD,_QuickHull
function computeConvexHullJarvis() {
    if (points.length < 3) {
        alert("Минимальное количество точек для вычисления выпуклой оболочки: 3");
        return;
    }

    const getOrientation = (p, q, r) => {
        const val = (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
        if (val === 0) {
            return 0;
        } else if (val > 0) {
            return 1;
        } else {
            return -1;
        }
    };

    const leftmostPoint = points.reduce((leftmost, point) => (point[0] < leftmost[0] ? point : leftmost));

    const hullPoints = [];
    let p = leftmostPoint;
    let q;

    do {
        hullPoints.push(p);
        q = points[0];
        for (let i = 1; i < points.length; i++) {
            if (q === p || getOrientation(p, q, points[i]) === -1) {
                q = points[i];
            }
        }
        p = q;
    } while (p !== leftmostPoint);

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPolygon();

    for (let i = 0; i < hullPoints.length; i++) {
        const startPoint = hullPoints[i];
        const endPoint = hullPoints[(i + 1) % hullPoints.length];
        drawLine(startPoint[0], startPoint[1], endPoint[0], endPoint[1], "DeepPink");
    }
}

function calculateNormals() {
    if (points.length < 3) {
        alert("Минимальное количество точек для расчета нормалей: 3");
        return;
    }

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPolygon();

    for (let i = 0; i < points.length; i++) {
        const currPoint = points[i];
        const prevPoint = points[i === 0 ? points.length - 1 : i - 1];
        const nextPoint = points[(i + 1) % points.length];

        const normalX = nextPoint[1] - currPoint[1];
        const normalY = currPoint[0] - nextPoint[0];

        const normalLength = Math.sqrt(normalX * normalX + normalY * normalY);
        const normalizedNormalX = normalX / normalLength;
        const normalizedNormalY = normalY / normalLength;

        const normalStartX = currPoint[0] + normalizedNormalX * 100;
        const normalStartY = currPoint[1] + normalizedNormalY * 100;

        const normalEndX = currPoint[0] - normalizedNormalX * 100;
        const normalEndY = currPoint[1] - normalizedNormalY * 100;

        drawLine(normalStartX, normalStartY, normalEndX, normalEndY, "green");
    }
}

canvas.addEventListener("click", function(event) {
    const x = event.offsetX;
    const y = event.offsetY;

    points.push( [x, y] );

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPolygon();
});

function clearApp() {
    points = [];
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}
