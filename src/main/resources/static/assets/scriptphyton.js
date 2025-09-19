// Botón scroll-top
(function() {
    // Crear el botón si no existe
    if (!document.getElementById('btn-scroll-top')) {
        const btn = document.createElement('button');
        btn.id = 'btn-scroll-top';
        btn.title = 'Ir arriba';
        btn.style.position = 'fixed';
        btn.style.bottom = '32px';
        btn.style.right = '32px';
        btn.style.zIndex = '50';
        btn.style.background = 'linear-gradient(90deg,#10b981 0%, #2563eb 100%)';
        btn.style.color = '#fff';
        btn.style.border = 'none';
        btn.style.borderRadius = '50%';
        btn.style.width = '56px';
        btn.style.height = '56px';
        btn.style.boxShadow = '0 4px 16px #2563eb33';
        btn.style.display = 'none';
        btn.style.alignItems = 'center';
        btn.style.justifyContent = 'center';
        btn.style.fontSize = '2rem';
        btn.style.cursor = 'pointer';
        btn.style.transition = 'background 0.3s';
        btn.innerHTML = '<span class="material-icons">arrow_upward</span>';
        document.body.appendChild(btn);
    }
    const btnScrollTop = document.getElementById('btn-scroll-top');
    window.addEventListener('scroll', function() {
        if (window.scrollY > 300) {
            btnScrollTop.style.display = 'flex';
        } else {
            btnScrollTop.style.display = 'none';
        }
    });
    btnScrollTop.addEventListener('click', function() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
})(); 