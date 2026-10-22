// Auto-submit forms when a [data-autosubmit] control changes (filters, cart quantity, order status)
document.addEventListener("change", (e) => {
  if (e.target.matches("[data-autosubmit]")) e.target.form.submit();
});

// Confirm before destructive actions: <form data-confirm="Are you sure?">
document.addEventListener("submit", (e) => {
  const msg = e.target.dataset.confirm;
  if (msg && !window.confirm(msg)) { e.preventDefault(); return; }
  // Prevent double submits
  const btn = e.target.querySelector("button:not([type=button])");
  if (btn) setTimeout(() => (btn.disabled = true), 0);
});

// Client-side check: min price must not exceed max price
document.getElementById("filterForm")?.addEventListener("submit", (e) => {
  const f = e.target;
  const min = parseFloat(f.min.value), max = parseFloat(f.max.value);
  if (!isNaN(min) && !isNaN(max) && min > max) {
    e.preventDefault();
    alert("Minimum price cannot be greater than maximum price.");
  }
});

// Auto-hide flash messages
setTimeout(() => document.querySelectorAll(".alert.success").forEach((a) => (a.style.display = "none")), 4000);
