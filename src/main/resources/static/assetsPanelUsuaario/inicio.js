function toggleDropdown() {
	document.getElementById("myDropdown").classList.toggle("show");
}


// Cerrar el dropdown si el usuario hace clic fuera de él
window.onclick = function(event) {
	var userMenu = document.getElementById('userMenu');
				if (userMenu && userMenu.style.display === 'block') {
					// Verificar si el clic fue fuera del dropdown
					if (!event.target.closest('button') && !event.target.closest('#userMenu')) {
						userMenu.style.display = 'none';
					}
				}
			}
			
