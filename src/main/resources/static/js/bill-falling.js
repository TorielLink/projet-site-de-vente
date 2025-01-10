const duration = 7000;
const interval = 500;

document.addEventListener("DOMContentLoaded", function () {
    const billContainer = document.getElementById("falling-bills");

    function createFallingBill() {
        const bill = document.createElement("div");
        bill.classList.add("falling-bill");

        const billWidth = Math.random() * 50 + 30;
        const billLeft = Math.random() * window.innerWidth - billWidth;

        bill.style.width = `${billWidth}px`;
        bill.style.left = `${billLeft}px`;
        bill.style.backgroundImage = "url('/images/bill.png')";
        bill.style.backgroundSize = "contain";
        bill.style.backgroundPosition = "center";
        bill.style.backgroundRepeat = "no-repeat";

        billContainer.appendChild(bill);

        setTimeout(() => {
            bill.remove();
        }, duration);
    }

    setInterval(createFallingBill, interval);
});
