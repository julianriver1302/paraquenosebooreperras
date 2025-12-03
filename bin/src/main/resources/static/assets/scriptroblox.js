// Botón scroll-top
(function() {
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