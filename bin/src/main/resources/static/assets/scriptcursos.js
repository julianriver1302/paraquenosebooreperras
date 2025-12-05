function toggleHoloInfo(btn) {
  const info = btn.nextElementSibling;
  if (info.style.display === 'none' || info.style.display === '') {
    info.style.display = 'block';
    btn.textContent = 'Ver menos';
  } else {
    info.style.display = 'none';
    btn.textContent = 'Ver más';
  }
}
// Animación de entrada del header mejorada
window.addEventListener('DOMContentLoaded', () => {
    const header = document.getElementById('main-header');
    // El header ya tiene la clase de animación en el HTML
    // Animación secuencial para los elementos del nav
    const navLinks = document.querySelectorAll('.nav-link-glass');
    navLinks.forEach((link, index) => {
        link.style.opacity = '0';
        link.style.transform = 'translateY(20px)';
        setTimeout(() => {
            link.style.transition = 'opacity 0.6s cubic-bezier(0.4, 0, 0.2, 1), transform 0.6s cubic-bezier(0.4, 0, 0.2, 1)';
            link.style.opacity = '1';
            link.style.transform = 'translateY(0)';
        }, 300 + (index * 100));
    });
});
// Menú hamburguesa mejorado
const navToggle = document.getElementById('nav-toggle');
const mobileMenu = document.getElementById('mobile-menu');
navToggle.addEventListener('click', () => {
    navToggle.classList.toggle('active');
    mobileMenu.classList.toggle('active');
    // Prevenir scroll cuando el menú esté abierto
    document.body.style.overflow = mobileMenu.classList.contains('active') ? 'hidden' : 'auto';
});
// Cerrar menú al hacer click en un enlace
mobileMenu.addEventListener('click', (e) => {
    if (e.target.closest('.nav-link-glass')) {
        navToggle.classList.remove('active');
        mobileMenu.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
});
// Cerrar menú con ESC
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && mobileMenu.classList.contains('active')) {
        navToggle.classList.remove('active');
        mobileMenu.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
}); 