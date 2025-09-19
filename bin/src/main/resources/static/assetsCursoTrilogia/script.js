let currentBubble = 1;
function showBubble(n) {
  for (let i = 1; i <= 4; i++) {
    document.getElementById('bubble'+i).style.display = (i === n) ? 'block' : 'none';
  }
  const nextBtn = document.getElementById('nextBtn');
  if (n < 4) {
    nextBtn.textContent = 'Siguiente';
    nextBtn.onclick = showNextBubble;
  } else if (n === 4) {
    nextBtn.textContent = 'Llevar a inicio';
    nextBtn.onclick = function() { window.location.href = 'index.html'; };
  }
}
function showNextBubble() {
  if (currentBubble < 4) {
    currentBubble++;
    showBubble(currentBubble);
  }
}
function showPrevBubble() {
  if (currentBubble > 1) {
    currentBubble--;
    showBubble(currentBubble);
  }
}
// Inicializar el estado correcto del botón al cargar
window.addEventListener('DOMContentLoaded', function() {
  showBubble(currentBubble);
}); 

/*tabla de dibujo */
const canvas = document.getElementById('canvas');
const ctx = canvas.getContext('2d');
let dibujando = false;
let herramienta = 'pincel';
let color = document.getElementById('colorPicker').value;
let grosor = document.getElementById('sizePicker').value;
let inicioX, inicioY;
let imagenTemp;

document.getElementById('colorPicker').addEventListener('input', (e) => {
  color = e.target.value;
});
document.getElementById('sizePicker').addEventListener('input', (e) => {
  grosor = e.target.value;
});

function activarPincel() { herramienta = 'pincel'; }
function activarBorrador() { herramienta = 'borrador'; }
function dibujarForma(tipo) { herramienta = tipo; }

canvas.addEventListener('mousedown', (e) => {
  dibujando = true;
  inicioX = e.offsetX;
  inicioY = e.offsetY;
  imagenTemp = ctx.getImageData(0, 0, canvas.width, canvas.height);
  if (herramienta === 'pincel' || herramienta === 'borrador') {
    ctx.beginPath();
    ctx.moveTo(inicioX, inicioY);
  }
});

canvas.addEventListener('mousemove', (e) => {
  if (!dibujando) return;

  if (herramienta === 'pincel' || herramienta === 'borrador') {
    ctx.lineTo(e.offsetX, e.offsetY);
    ctx.strokeStyle = herramienta === 'borrador' ? "#ffffff" : color;
    ctx.lineWidth = grosor;
    ctx.lineCap = 'round';
    ctx.stroke();
  } else {
    ctx.putImageData(imagenTemp, 0, 0);
    ctx.lineWidth = grosor;
    ctx.strokeStyle = color;
    ctx.beginPath();

    if (herramienta === 'cuadrado') {
      ctx.rect(inicioX, inicioY, e.offsetX - inicioX, e.offsetY - inicioY);
      ctx.stroke();
    } else if (herramienta === 'circulo') {
      const radio = Math.sqrt(Math.pow(e.offsetX - inicioX, 2) + Math.pow(e.offsetY - inicioY, 2));
      ctx.arc(inicioX, inicioY, radio, 0, 2 * Math.PI);
      ctx.stroke();
    } else if (herramienta === 'linea') {
      ctx.moveTo(inicioX, inicioY);
      ctx.lineTo(e.offsetX, e.offsetY);
      ctx.stroke();
    }
  }
});

canvas.addEventListener('mouseup', (e) => {
  if (!dibujando) return;

  if (herramienta !== 'pincel' && herramienta !== 'borrador') {
    ctx.putImageData(imagenTemp, 0, 0);
    ctx.lineWidth = grosor;
    ctx.strokeStyle = color;
    ctx.beginPath();

    if (herramienta === 'cuadrado') {
      ctx.rect(inicioX, inicioY, e.offsetX - inicioX, e.offsetY - inicioY);
      ctx.stroke();
    } else if (herramienta === 'circulo') {
      const radio = Math.sqrt(Math.pow(e.offsetX - inicioX, 2) + Math.pow(e.offsetY - inicioY, 2));
      ctx.arc(inicioX, inicioY, radio, 0, 2 * Math.PI);
      ctx.stroke();
    } else if (herramienta === 'linea') {
      ctx.moveTo(inicioX, inicioY);
      ctx.lineTo(e.offsetX, e.offsetY);
      ctx.stroke();
    }
  }
  dibujando = false;
});

canvas.addEventListener('mouseleave', () => dibujando = false);

function limpiarCanvas() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  document.getElementById('imagenGuardada').innerHTML = '';
}

function guardarDibujo() {
  const imagen = canvas.toDataURL("image/png");
  document.getElementById('imagenGuardada').innerHTML = `
    <p>👇 Así se ve tu dibujo:</p>
    <img src="${imagen}" width="250">
    <p>Haz clic derecho sobre la imagen y selecciona "Guardar imagen como..." 📥</p>
  `;
}