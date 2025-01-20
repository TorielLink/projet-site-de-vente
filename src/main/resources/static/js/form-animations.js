
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    form.classList.add("animate__animated", "animate__fadeInDown");
});

const inputs = document.querySelectorAll("input");
inputs.forEach((input) => {
    input.addEventListener("focus", () => {
        input.classList.add("is-focused");
    });

    input.addEventListener("blur", () => {
        input.classList.remove("is-focused");
    });
});

const button = document.querySelector("button");
button.addEventListener("click", (event) => {
    event.preventDefault();

    button.classList.add("animate__animated", "animate__bounce");
    setTimeout(() => {
        button.classList.remove("animate__animated", "animate__bounce");
        document.querySelector("form").submit();
    }, 600);
});
