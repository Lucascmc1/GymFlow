(() => {
    "use strict";

    const sidebar = document.getElementById("sidebar");
    const sidebarOverlay = document.getElementById("sidebarOverlay");
    const menuButton = document.getElementById("menuButton");

    function closeSidebar() {
        sidebar?.classList.remove("open");
        sidebarOverlay?.classList.remove("open");
    }

    menuButton?.addEventListener("click", () => {
        sidebar?.classList.toggle("open");
        sidebarOverlay?.classList.toggle("open");
    });
    sidebarOverlay?.addEventListener("click", closeSidebar);

    document.querySelectorAll("[data-toast]").forEach((toast) => {
        const close = () => {
            toast.style.opacity = "0";
            toast.style.transform = "translateY(-8px)";
            window.setTimeout(() => toast.remove(), 180);
        };
        toast.querySelector("[data-toast-close]")?.addEventListener("click", close);
        window.setTimeout(close, 4500);
    });

    document.querySelectorAll("[data-tabs]").forEach((tabs) => {
        const buttons = tabs.querySelectorAll("[data-tab]");
        const container = tabs.closest(".card") ?? document;
        const panels = container.querySelectorAll("[data-tab-panel]");
        buttons.forEach((button) => {
            button.addEventListener("click", () => {
                const target = button.dataset.tab;
                buttons.forEach((item) => item.classList.toggle("active", item === button));
                panels.forEach((panel) => {
                    panel.classList.toggle("active", panel.dataset.tabPanel === target);
                });
            });
        });
    });

    document.querySelectorAll("[data-modal-open]").forEach((trigger) => {
        trigger.addEventListener("click", () => {
            const modal = document.getElementById(trigger.dataset.modalOpen);
            if (!modal) return;

            const form = modal.querySelector("form");
            const title = modal.querySelector("[data-modal-title]");
            const description = modal.querySelector("[data-modal-description]");
            if (form && trigger.dataset.action) form.action = trigger.dataset.action;
            if (title && trigger.dataset.title) title.textContent = trigger.dataset.title;
            if (description && trigger.dataset.description) {
                description.textContent = trigger.dataset.description;
            }
            modal.classList.add("open");
            modal.querySelector("input, select, button")?.focus();
        });
    });

    document.querySelectorAll("[data-modal-close]").forEach((trigger) => {
        trigger.addEventListener("click", () => trigger.closest(".modal")?.classList.remove("open"));
    });

    document.querySelectorAll(".modal").forEach((modal) => {
        modal.addEventListener("click", (event) => {
            if (event.target === modal) modal.classList.remove("open");
        });
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closeSidebar();
            document.querySelectorAll(".modal.open").forEach((modal) => modal.classList.remove("open"));
        }
    });

    document.querySelectorAll("form[data-confirm]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            if (!window.confirm(form.dataset.confirm)) event.preventDefault();
        });
    });

    document.querySelectorAll("[data-mask='cpf']").forEach((input) => {
        input.addEventListener("input", () => {
            const digits = input.value.replace(/\D/g, "").slice(0, 11);
            input.value = digits
                .replace(/^(\d{3})(\d)/, "$1.$2")
                .replace(/^(\d{3})\.(\d{3})(\d)/, "$1.$2.$3")
                .replace(/\.(\d{3})(\d)/, ".$1-$2");
        });
    });

    document.querySelectorAll("[data-mask='phone']").forEach((input) => {
        input.addEventListener("input", () => {
            const digits = input.value.replace(/\D/g, "").slice(0, 11);
            input.value = digits
                .replace(/^(\d{2})(\d)/, "($1) $2")
                .replace(/(\d{5})(\d{4})$/, "$1-$2");
        });
    });

    const planSelect = document.querySelector("[data-plan-select]");
    const discountInput = document.querySelector("[data-discount]");
    const pricePreview = document.querySelector("[data-price-preview]");
    const updatePricePreview = () => {
        if (!planSelect || !discountInput || !pricePreview) return;
        const option = planSelect.selectedOptions[0];
        const price = Number(option?.dataset.price ?? 0);
        const discount = Math.min(100, Math.max(0, Number(discountInput.value ?? 0)));
        const total = price * (1 - discount / 100);
        pricePreview.textContent = total.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        });
    };
    planSelect?.addEventListener("change", updatePricePreview);
    discountInput?.addEventListener("input", updatePricePreview);
    updatePricePreview();
})();
