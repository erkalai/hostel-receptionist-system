// Ensure the menu starts as hidden
document.addEventListener("DOMContentLoaded", () => {
    const menu = document.getElementById('menu');
    menu.style.display = 'none';
});

// Toggle the menu's visibility
function toggleMenu() {
    const menu = document.getElementById('menu');
	const menubtn = document.getElementById('menu-btn');
	menubtn.style.display = 'none';
    if (menu.style.display === 'block') {
		
       menu.style.display = 'none';
		
    } else {
        menu.style.display = 'block';
    }
}

// Close the menu when the close button is clicked
function closeMenu() {
    const menu = document.getElementById('menu');
	const menubtn = document.getElementById('menu-btn');
    menu.style.display = 'none';
	menubtn.style.display = 'block';
}
