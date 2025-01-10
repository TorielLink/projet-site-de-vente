// Animation d'apparition pour le formulaire
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    form.classList.add("animate__animated", "animate__fadeInDown"); // Ajoute des classes d'animation CSS
});

// Ajout d'animation focus/blur sur les champs du formulaire
const inputs = document.querySelectorAll("input");
inputs.forEach((input) => {
    input.addEventListener("focus", () => {
        input.classList.add("is-focused");
    });

    input.addEventListener("blur", () => {
        input.classList.remove("is-focused");
    });
});

// Effet "rebond" pour le bouton
const button = document.querySelector("button");
button.addEventListener("click", (event) => {
    event.preventDefault(); // Empêche temporairement la soumission pour montrer l'animation

    // Ajoute une animation Bootstrap-like
    button.classList.add("animate__animated", "animate__bounce");
    setTimeout(() => {
        button.classList.remove("animate__animated", "animate__bounce");
        // Simuler l'envoi du formulaire après l'animation
        document.querySelector("form").submit();
    }, 600); // Durée de l'animation en ms
});
