document.getElementById('btn-pago').addEventListener('click', function(event) {
    event.preventDefault();
    document.getElementById('modal-metodo-pago').style.display = 'flex';
});
// Cerrar modal al hacer click fuera de la tarjeta
const modal = document.getElementById('modal-metodo-pago');
modal.addEventListener('click', function(e) {
  if (e.target === modal) {
    modal.style.display = 'none';
  }
});
// Cerrar modal con el botón X
const cerrarBtn = document.getElementById('cerrar-modal-metodo');
if (cerrarBtn) {
  cerrarBtn.onclick = function() {
    modal.style.display = 'none';
  };
} 