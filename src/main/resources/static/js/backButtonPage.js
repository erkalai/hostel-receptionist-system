// List of restricted pages where back button should be blocked
const restrictedPages = ['/bookings/checkout', '/bookings/checkin', '/user-edit', '/add-program'];

// Get the current page path
const currentPage = window.location.pathname;

// Check if the current page is in the restrictedPages array
if (restrictedPages.includes(currentPage)) {
    // Replace the current history entry to avoid navigating to the previous page
    history.replaceState(null, document.title, location.href);

    // Push a new state so that the user can't go back to the previous page
    window.history.pushState({ page: "restricted" }, "restricted", window.location.href);

    // Listen for the back button (popstate event)
    window.onpopstate = function (event) {
        // Redirect to the dashboard when the user tries to go back
        window.location.href = '/dashboard'; // Ensure this path matches your actual dashboard path
    };
}


/*

const restrictedCheckinPage = ['/bookings/checkin'];

const currentCheckinPage = window.location.pathname;

if (restrictedCheckinPage.includes(currentCheckinPage)) {
    window.history.pushState({page: "restricted"}, "restricted", window.location.href);

    window.onpopstate = function(event) {
        window.location.href = '/bookings/checkin';
    };

    window.onload = function() {
        history.replaceState(null, document.title, location.href);
    };
}
*/